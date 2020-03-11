@Library('shared_libs_cdh@') import jnpr.releng.dynamicFuncCall.dynamicFunc;
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
                    def s = new dynamicFunc()
                    def components = ['hello', 'goodbye']
                    for (component in components){
                        stepsToRun[component] = s."${component}"(component)
                    }
                }
                parallel stepsToRun
            }
        }
    }
}




evenOrOdd(currentBuild.getNumber())