env.MAILER="cdh@juniper.net"
env.J_NAME=env.BRANCH_NAME.replace("/","%2F")
env.PROJECT=env.BRANCH_NAME.replace("/","-")
env.SB_ROOT='/b/evo-builder/push_autodeps/'+env.BRANCH_NAME+'/'
env.STATE=''
def sendMail ( status='', reason='' ) {
    if(status == 'Successful'){
        reason = sh label: '', returnStdout: true, script: '''
           cat ${SBPATH}/logs/process-autodeps.last
           '''
    }
    
    emailext body: '''Hi,
  
  Push autodeps job '''+status+''' for '''+env.BRANCH_NAME+'''-- Build $BUILD_NUMBER
  
  '''+reason+'''
  '''+env.STATE+'''
  
  Check console output at '''+env.RUN_DISPLAY_URL+''' to view the results.
  
  Host: '''+env.NODE_NAME+'''
  Sandbox location: '''+env.SBPATH+'''
  
  Regards,
  scm-tools
  
  ''', subject: "SCM-TOOLS push autodeps job $status for $BRANCH_NAME - Build $BUILD_NUMBER", to: env.MAILER
  
  }
  def getSbname(SB_LOCATION) {
        def SPIN = 1
        time = new Date().format("yyyyMMdd")
        SB_NAME = SB_LOCATION+"push-autodeps-"+time+"."+SPIN
        SB_PATTERN = "push-autodeps-"+time
        while (fileExists(SB_NAME)) {
            SPIN+=1
            SB_NAME = SB_LOCATION+"push-autodeps-"+time+"."+SPIN
            folder = new File( SB_NAME )
        }
        return [SB_NAME, SB_PATTERN]
    }
properties([disableConcurrentBuilds(),[$class: 'BuildDiscarderProperty',
    strategy: [$class: 'LogRotator', numToKeepStr: '6']],
pipelineTriggers([cron('H 04 * * *')]),
])
pipeline { 
    agent { label 'autodeps'}
    stages {
        stage('cleanup Old sbs'){
            steps{
                sshagent( credentials: ['evo-builder-ssh-passphrase']){
                    script{
                        try{
                            def (sbpath,sbpattern)=getSbname(env.SB_ROOT)
                            env.SBPATH=sbpath
                            env.SB_PATTERN=sbpattern
                            output=sh label: '', returnStdout: true, script: '''
                            mkdir -p ${SB_ROOT}
                            cd ${SB_ROOT}
                            hostname
                            pwd
                            if ls  ${SB_ROOT}push-autodeps* 1> /dev/null 2>&1 
                            then
                                ls -ld ${SB_ROOT}* |grep -v ${SB_PATTERN} |xargs rm -rf
                            fi
                            '''
                        }
                        catch(err){
                            sh "echo  SB create /pull  failed $err"
                            env.STATE = stage
                            currentBuild.result = "FAILURE"
                        }
                    }
                  }
                }
            }
        stage('checkout') {
            steps {
                sshagent( credentials: ['evo-builder-ssh-passphrase']){
                    script{
                        try{
                            output = sh label: '', returnStdout: true, script: '''
                            
                            [ -d ${SBPATH} ] && rm -rf ${SBPATH}
        
                            cd ${SB_ROOT}
                            hostname
                            pwd
                            unset JAVA_HOME;
                            export PATH=/volume/evo/files/share/buildtools/sandbox-tools/bin:$PATH
                            export SANDBOX_TOOLS_LIB=/volume/evo/files/share/buildtools/sandbox-tools/lib
                            cd $SB_ROOT
                            sb create -p ${PROJECT}  -n ${SBPATH} -L 
                            '''
                        }
                        catch(err){
                            sh "echo  SB create /pull  failed $err"
                            env.STATE = stage
                            currentBuild.result = "FAILURE"
                            error "This pipeline stops here!"
                        }
                    }
                }
            }
        }
        stage('build') {
            steps {
                sshagent( credentials: ['evo-builder-ssh-passphrase']){
                    script{
                        try{
                            output = sh label: '', returnStdout: true, script: '''
                            cd ${SBPATH}
                            hostname
                            pwd
                            export SB_FORCE_LOG=yes
                            unset JAVA_HOME;
                            export PATH=/volume/evo/files/share/buildtools/sandbox-tools/bin:$PATH
                            export SANDBOX_TOOLS_LIB=/volume/evo/files/share/buildtools/sandbox-tools/lib
                            sb make DAILY_BUILD=yes  UT_FAIL_IGNORE=1
                            '''
                        }
                        catch(err){
                            echo "Build Failed: "+err
                            sh 'cat ${SBPATH}/logs/make.last | tail -100'
                            currentBuild.result = "FAILURE"
                            error "This pipeline stops here!"
                        }
                    }
                }
            }
        }
        stage('push_autodeps') {
            steps {
                sshagent( credentials: ['evo-builder-ssh-passphrase']){
                    script{
                        try{
                            output = sh label: '', returnStdout: true, script: '''
                            cd ${SBPATH}
                            hostname
                            pwd
                            export SB_FORCE_LOG=yes
                            unset JAVA_HOME;
                            export PATH=/volume/evo/files/share/buildtools/sandbox-tools/bin:$PATH
                            export SANDBOX_TOOLS_LIB=/volume/evo/files/share/buildtools/sandbox-tools/lib
                            sb process-autodeps -p 
                            '''
                        }
                        catch(err){
                            echo "Build Failed: "+err
                            sh 'cat ${SBPATH}/logs/process-autodeps.last| tail -100'
                            sendMail ( 'Build Failed' )
                            currentBuild.result = "FAILURE"
                            error "This pipeline stops here!"
                        }
                    }
            }
            }
        }
    }
    post { 
        success{
            sendMail('Successful' )
        }
        failure{
            sendMail('Failed')
        }
        unstable{
            sendMail('Unstable')
        }
    }
}

