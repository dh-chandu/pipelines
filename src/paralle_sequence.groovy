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
//                        def tmp = component
                        if (component == 'bcm'){
                            stepsToRun[component] = bcm(component)

                        }
                        if (component == 'ui'){
                            stepsToRun[component] = ui(component)
                        }

                    }
                    parallel stepsToRun
                }
            }
        }
    }
}

