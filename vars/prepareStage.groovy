def call(String name = 'human') {
     return {
        stage (name) {
            stage("1") {
                echo "start 1"
                sleep 1
                echo "done 1"
            }
            stage("2") {
                echo "start 2"
                sleep 1
                echo "done 2"
            }
        }
    }
}
