@Library('shared_libs_cdh')_
def stepsToRun = [:]

pipeline {
    agent none

    stages {
        stage ("Prepare Stages"){
            steps {
                script {
//                    for (int i = 1; i < 300; i++) {
//                        stepsToRun["Step${i}"] = prepareStage("Step${i}")
//                    }
                    def components = ['bcm', 'ui']
                    for (component in components){
                        stepsToRun[component] = assert "${component}"(component)
                        }
                    }
                    parallel stepsToRun
                }
            }
        }
    }

