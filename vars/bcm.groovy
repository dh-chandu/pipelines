def call(String name = '') {
     return {
        stage (name) {
            node {
                stage(name+" checkout") {
                    sh "ls -ltr ${env.WORKSPACE}/"
                    def cmd = 'ls -ltr '+env.WORKSPACE
                    def files = sh(returnStdout: true, script: cmd).trim()
                    def script_bash = libraryResource 'src/jnpr/releng/cdh.sh'
                    println '----script_bash---'+script_bash
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
