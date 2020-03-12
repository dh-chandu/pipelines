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
//                    def components = ['bcm', 'ui']
//                    for (component in components){
//                        switch (component){
//                            case 'bcm':
//                                stepsToRun[component] = bcm(component)
//                            case 'ui':
//                                stepsToRun[component] = ui(component)
//                            default:
//                                println 'waste'
//                        }
//                    }
                    stepsToRun['bcm'] = bcm('bcm')
                    stepsToRun['ui'] = ui('ui')
//                    for (i in stepsToRun){
//                        println '========================='+i+'================'
//                        println stepsToRun[i]
//                    }
                    parallel stepsToRun
                }
            }
        }
    }
}

