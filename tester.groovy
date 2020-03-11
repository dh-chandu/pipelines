@Library('shared_libs_cdh')
import jnpr.releng.*;

pipeline {
    agent none
    stages {
        stage('Demo') {
            steps{
                script{
                    def t = new Tester('cddh')
                    println t.sayHi()
                }
            }
        }
    }

}