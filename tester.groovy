@Library('shared_libs_cdh')
import jnpr.releng.*;

pipeline {
    agent none
    stages {
        stage('Demo') {
            steps{
                sayHello 'test'
                script{
                    def t = new Tester('Alice')
                    t.sayHi()
                }
            }
        }
    }

}