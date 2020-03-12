@Library('shared_libs_cdh')_
def stepsToRun = [:]
pipeline {
    agent none
    stages {
        stage ("Prepare Stages"){
            steps {
                script {
                    String[] components = ['bcm', 'ui']
                    def script_bash = libraryResource 'jnpr/releng/cdh.sh'
                    println script_bash
                    for (component in components){
                        switch (component){
                            case 'bcm':
                                stepsToRun[component] = bcm(component, src_path)
                                break;
                            case 'ui':
                                stepsToRun[component] = ui(component)
                                break;
                            default:
                                println 'waste'
                                break;
                        }
                        //stepsToRun[component] = generic_component(component)
                    }
                    parallel stepsToRun
                }
            }
        }
    }
}

