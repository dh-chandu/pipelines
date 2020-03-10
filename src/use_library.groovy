@Library('shared_libs_cdh')_
pipeline {
    agent any
    stages {
        stage('test') {
            steps {
                sayHello 'Joe'
            }
        }
    }
}
