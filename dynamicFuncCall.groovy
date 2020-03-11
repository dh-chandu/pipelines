@Library('shared_libs_cdh')
import jnpr.releng.*;
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
                    def s = new DynamicFunc()
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
