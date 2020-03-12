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
 //                   for (int i = 0; i < components.length; i++) {
//                        def tmp = components[i]
                        def tmp = component
                        println '====tmp==='+tmp
                        def tmp_component = tmp
                        println '====tmp_component ==='+tmp_component
                        if (tmp == 'bcm'){
                            stepsToRun[tmp_component] = bcm(tmp_component)

                        }
                        if (tmp == 'ui'){
                            stepsToRun[tmp_component] = ui(tmp_component)
                        }
                    parallel stepsToRun
                }
            }
        }
    }
}

