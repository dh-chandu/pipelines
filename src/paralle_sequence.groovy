@Library('shared_libs_cdh')_
def stepsToRun = [:]

pipeline {
    agent none
    stages {
        stage ("Prepare Stages"){
            steps {
                script {
                    String[] components = ['bcm', 'ui']
                    for (component in components){
                        switch (component){
                            case 'bcm':
                                stepsToRun[component] = bcm(component)
                                break;
                            case 'ui':
                                stepsToRun[component] = ui(component)
                                break;
                            default:
                                println 'waste'
                                break;
                        }
                    }
                    parallel stepsToRun
                }
            }
        }
    }
}

