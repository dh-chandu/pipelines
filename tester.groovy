@Library('pipeline-library-demo')
import de.schlumpf.*;

stages{
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