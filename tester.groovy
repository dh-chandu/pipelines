@Library('shared_libs_cdh')
import jnpr.releng.Testerer;

pipeline {
    agent none
    stages {
        stage('Demo') {
            steps{
                script{
                    def t = new Testerer('cddh')
                    println t.sayHi()
                    t.ui()
                }
            }
        }
    }

}