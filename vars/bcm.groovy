def call(String name = '') {
     return {
        stage (name) {
            node {
                stage(name+" checkout") {
                    sh "echo ${env.WORKSPACE}/../${env.JOB_NAME}"
                    sh " ls -ltr ${env.WORKSPACE}/../${env.JOB_NAME}"
                    echo "$name checkout "
                    echo "$name checkout done"
                }
                stage(name+" Build") {
                    echo "$name build "
                    echo "$name build done "
                }
                stage(name+" test") {
                    echo "$name Test "
                    echo "$name Test done "
                }
            }

        }
    }
}
