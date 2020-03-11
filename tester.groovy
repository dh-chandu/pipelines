@Library('shared_libs_cdh')
import jnpr.releng.*;

pipeline {
    agent none
    stages {
        stage('Demo') {
            steps{
                sayHello 'test'
                script{
                    def t = new DynamicFunc()
                    t.sayHi()
                }
            }
        }
    }

}