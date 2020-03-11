@Library('shared_libs_cdh')
import jnpr.releng.*;

pipeline {
    agent none
    stages {
        stage('Demo') {
            steps{
                script{
                    def t = new DynamicFunction('cddh')
                    println t.sayHi()
                }
            }
        }
    }

}