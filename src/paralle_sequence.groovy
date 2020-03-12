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
                        def tmp = component
                        def tmp_component = tmp
                        switch (tmp_component){
                            case 'bcm':
                                stepsToRun[tmp_component] = bcm(tmp_component)
                            case 'ui':
                                stepsToRun[tmp_component] = ui(tmp_component)
                            default:
                                println 'waste'
                        }
                    }
//                    stepsToRun['bcm'] = bcm('bcm')
//                    stepsToRun['ui'] = ui('ui')
                    parallel stepsToRun
                }
            }
        }
    }
}

