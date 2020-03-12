@Library('shared_libs_cdh')_
def stepsToRun = [:]

pipeline {
    agent none

    stages {
        stage("Prepare Stages") {
            steps {
                script {
//                    for (int i = 1; i < 300; i++) {
//                        stepsToRun["Step${i}"] = prepareStage("Step${i}")
//                    }
                    String[] components = ['bcm', 'ui']
//                    for (component in components) {
                    for (int i = 0; i < components.length; i++) {
//                        println '-component---' + i + '-----' + components[i]
                        def tmp = components[i]
                        println '====tmp===' + tmp
                        if (tmp == 'bcm') {
                            stepsToRun[tmp] = bcm(tmp)

                        }
                        if (tmp == 'ui') {
                            stepsToRun[tmp] = ui(tmp)
                        }

//                    stepsToRun['bcm'] = bcm('bcm')
//                    stepsToRun['ui'] = ui('ui')
                        parallel stepsToRun
                    }
                }
            }
        }
    }

}