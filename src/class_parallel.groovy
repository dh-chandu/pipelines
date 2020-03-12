@Library('shared_libs_cdh')_
def stepsToRun = [:]

pipeline {
    agent none
    stages {
        stage ("Prepare Stages"){
            steps {
                script {
                    @NonCPS
                    String[] components = ['bcm', 'ui']
                    def dynamicArgs = [1,2]
                    def groovy = new GroovyObject(){}
                    GroovyObject.methods.each{
                        stepsToRun[component] = groovy."$it.name"("$it.name", *components)
                    }
//                    for (component in components){
//                        switch (component){
//                            case 'bcm':
//                                stepsToRun[component] = bcm(component)
//                                break;
//                            case 'ui':
//                                stepsToRun[component] = ui(component)
//                                break;
//                            default:
//                                println 'waste'
//                                break;
//                        }
//                    }
                    parallel stepsToRun
                }
            }
        }
    }
}

