pipeline {
  agent {
    label "something"
  }
  options {
    skipDefaultCheckout()
  }
  stages {
    stage("foo") {
      steps {
        ws("/Users/cdh/Jenkins_slaves/TEST") {
          checkout scm
          // rest of stage
        }
      }
    }
    stage("next") {
      steps {
        ws("/Users/cdh/Jenkins_slaves/TEST1") {
          checkout scm
          sh 'echo $WORKSPACE ; ls -ltr $WORKSPACE'
        }
      }
    }
  }
}
