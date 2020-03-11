def call(String name = 'human') {
     return {
        stage (name) {
            stage("BCM checkout") {
                echo "start 1"
                sleep 1
                echo "done 1"
            }
            stage("Build") {
                echo "start 2"
                sleep 1
                echo "done 2"
            }
        }
    }
}
