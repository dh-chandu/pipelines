properties([disableConcurrentBuilds(),parameters([string(defaultValue: '/evobuilds2/pvt-builds/opengrok_sbs', description: 'sandbox basedir', name: 'SB_BASE_DIR', trim: false), string(defaultValue: 'v/master/LATEST_LG', description: 'default revision to be checkout', name: 'EVO_REVISION', trim: false), string(defaultValue: 'svl-evobuild-03', description: 'host on which build has to run', name: 'HOST', trim: false), string(defaultValue: 'evo', description: 'Name of the branch in evo (component-manifest repo)', name: 'EVO_PROJECT', trim: false)]), pipelineTriggers([gerrit(customUrl: '', gerritProjects: [[branches: [[compareType: 'ANT', pattern: 'refs/tags/v/master/pub-*']], compareType: 'PLAIN', disableStrictForbiddenFileVerification: false, pattern: 'component-manifest']], serverName: 'svl-evogit-01', triggerOnEvents: [refUpdated()])])])
def isJobStartedByTimer() {
    def startedByTimer = false
    try {
        def buildCauses = currentBuild.rawBuild.getCauses()
        for(buildCause in buildCauses) {
            if(buildCause != null) {
                def causeDescription = buildCause.getShortDescription()
                if(causeDescription.contains("Triggered by Gerrit")) {
                    startedByTimer = true
                }
                else{
                    println causeDescription
                }
            }
        }
    } catch(err) {
        echo "Error determining build cause"
    }
    return startedByTimer
}
def sendMail ( triggered_by='',mail_list='' ) {
    if( mail_list != null ){
        script{
            env.MAIL_LIST += mail_list
        }
    }
    emailext body: '''Hello,
OPENGROK master LG build and RSYNC build for tag: '''+env.EVO_REVISION+''' '''+triggered_by+''' 
For more details : $RUN_DISPLAY_URL
Sandbox details:/net/'''+env.HOST+'''/'''+env.SB_PATH+'''
$BUILD_URL
Regards,
psg-evo-build
''', subject: "OPENGROK master LG build and RSYNC SB $EVO_REVISION # -"+triggered_by, to: env.MAIL_LIST, replyTo: env.MAIL_LIST
}
env.MAIL_LIST = 'psg-evo-build,opengrok-admin, '
env.SCRIPT_ROOT='/volume/evo/files/share/buildtools/'
env.RELEASE_TOOLS='/volume/evo/files/share/buildtools/release-tools/'
env.CLS_SB_SCRIPT = env.RELEASE_TOOLS+'jenkins-scripts/cleanup_sbs.sh -r 2 -c 204800'
def build_trigger = isJobStartedByTimer()
env.SANDBOX_TOOLS_LIB = env.SCRIPT_ROOT+'sandbox-tools/lib'
env.SANDBOX_TOOLS_BIN = env.SCRIPT_ROOT+'sandbox-tools/bin'
if (build_trigger){
    env.EVO_REVISION = env.GERRIT_REFNAME.split(/tags\//)[1]
    
}
else{
    println "Manual trigger"
}
env.SB_PATH = env.SB_BASE_DIR+'/'+env.EVO_REVISION.split(/\//)[-1]
env.DST_DIR = '/build/opengrok_scm/source_data_parallel/EVO_TOT_BUILD'
pipeline{
    agent none
    options { skipDefaultCheckout() }
    
    stages{
        stage('CLEAN UP'){
            agent {
                label HOST
            }
            steps{
                echo 'cleanup'
                script{
                    def cls_res = sh(script:'''
                        hostname -a
                        ${CLS_SB_SCRIPT} -d $SB_BASE_DIR ''',returnStatus: true)
                        if (cls_res != 0){
                            sh 'echo CLEAN up failed !!!'
                            sh 'exit 1'
                        }
                }
            }
        }
        stage('CHECKOUT'){
            agent {
                label HOST
            }
            steps{
                echo "CHECKOUT: $EVO_REVISION"
                script{
                    def checkout_status = sh(script:'''
                    hostname
                    if [ -d $SB_PATH ] 
                    then 
                        rm -rf $SB_PATH
                    fi
                    if [ ! -d $SB_PATH ]
                    then
                        rm -rf 
                        export PATH=${SANDBOX_TOOLS_BIN}:${PATH}
                        export SANDBOX_TOOLS_LIB=${SANDBOX_TOOLS_LIB}
                        $RELEASE_TOOLS/build-tools/checkout.py -p $EVO_PROJECT -s $SB_PATH -R $EVO_REVISION 
                    else
                        echo Re-using SB : $SB_PATH
                        echo Skip checkout
                    fi''',returnStatus: true)
                    if ( checkout_status != 0){
                        sh 'echo CHECKOUT failed'
                        sh 'exit 1'
                    }
                }
            }
        }
        stage('BUILD'){
            agent {
                label HOST
            }
            steps{
                echo "RUNNING GNU MAKE for :  ${EVO_REVISION}"
                script{
                    def emake = sh(script:'''
                    export PATH=${SANDBOX_TOOLS_BIN}:${PATH}
                    export SANDBOX_TOOLS_LIB=${SANDBOX_TOOLS_LIB}
                    $RELEASE_TOOLS/build-tools/continuous-build.py --use-sb $SB_PATH  -B  -p ${EVO_PROJECT} -m \'ENABLE_OBJECT_HASHES=1 DAILY_BUILD=yes  CICD_BUILD=yes --emake-class=publish-cycle UT_FAIL_IGNORE=1 NOPKG=1 \' ''',returnStatus: true)
                    if (emake != 0 ){
                        sh 'echo BUILD failed'
                        sh 'exit 1'
                    }   
                }
            }
        }
        stage('RSYNC_SB'){
            agent {
                label HOST
            }
            steps{
                echo "RUNNING RSYNC :  ${EVO_REVISION}"
                script{
                    def rsync = sh(script:'''
                    export PATH=${SANDBOX_TOOLS_BIN}:${PATH}
                    export SANDBOX_TOOLS_LIB=${SANDBOX_TOOLS_LIB}
                    export DST_DIR="/build/opengrok_scm/source_data_parallel/EVO_TOT_BUILD"
                    python $RELEASE_TOOLS/build-tools/rsync_img.py -s $SB_PATH -d ${DST_DIR} -r 'svl-evodev-opengrok' -t normal  -n 2 ''',returnStatus: true)
                    if (rsync != 0 ){
                        sh 'echo RSYNC failed'
                        sh 'exit 1'
                    }   
                }
            }
        }
    }
    
    post{
        failure{
            sendMail('Failed','cdh@juniper.net')
        }
        unstable{
            sendMail('Unstable')
        }
    }
}
