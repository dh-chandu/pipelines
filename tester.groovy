@Library('shared_libs_cdh')
import jnpr.releng.*;

pipeline {
    stages {
        stage('Demo') {
            steps{
                echo 'Hello world'
                sayHello 'test'
                script{
                    def t = new Tester('Alice')
                    t.sayHi()
                }
            }
        }
    }

}