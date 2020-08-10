def exicute(command,stage='',ret=0){
    try{
        echo env.STAGE
        sh command
    }catch(err){
        sh 'echo '+command+' failed'
        if ( ret != 0 ){
            return err
        }else{
            script{
                env.STATE = stage
            }
            sh 'exit 1'
        }

    }
}

env.MAIL_LIST = 'cdh,'

def sendMail ( job_info,triggered_by='',mail_list='' ) {
    if( mail_list != null ){
        script{
            env.MAIL_LIST += mail_list
        }
    }
    if(job_info == null ){
        job_info = 'to all sites'
    }
    emailext body: '''Hi,

RSYNC Job '''+triggered_by+'''
for more details : $RUN_DISPLAY_URL

$BUILD_URL --- $BUILD_NUMBER


Regards,
Chandra
''', subject: "RSYNC JOB# "+job_info+" - "+triggered_by+"", to: env.MAIL_LIST, replyTo: env.MAIL_LIST

}


properties([disableConcurrentBuilds(),[$class: 'BuildDiscarderProperty',
                                       strategy: [$class: 'LogRotator', numToKeepStr: '6']],
            pipelineTriggers([cron('00 H/6 * * *')]),
])

pipeline {

    agent none
    options {
        timestamps()
        timeout(time: 14, unit: 'HOURS')
    }

    stages {
        stage('COPY_IMAGES') {
            steps {
                parallel(
                        'EVO_IMAGES_BNG' : {
                            node('bng') {
                                env.STAGE = 'EVO_IMAGES_BNG'
                                exicute('cd /build/home/evo-builder/for_rsync_jobs/jenkins-scripts; ./rsync_cicd_published.sh evo','EVO_IMAGES_TO_BNG')
                            }
                        },
                        'YOCTO_IMAGES_BNG' : {
                            node('bng') {
                                env.STAGE = 'YOCTO_IMAGES_BNG'
                                exicute('cd /build/home/evo-builder/for_rsync_jobs/jenkins-scripts; ./rsync_cicd_published.sh yocto','YOCTO_IMAGES_TO_BNG')
                            }
                        },
                )
            }
        }
    }
    post{
        success{
            sendMail(env.STATE,'Successful')
        }
        failure{
            sendMail(env.STATE,'Failed','psg-evo-build@juniper.net')
        }
        unstable{
            sendMail(env.STATE,'Unstable')
        }
    }

}
