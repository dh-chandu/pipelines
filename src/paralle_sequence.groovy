@Library('shared_libs_cdh')_
def stepsToRun = [:]

pipeline {
    agent none

    stages {
        stage ("Prepare Stages"){
            steps {
                script {
                    for (int i = 1; i < 500; i++) {
                        stepsToRun["Step${i}"] = prepareStage("Step${i}")
                    }
                    parallel stepsToRun
                }
            }
        }
    }
}
