def call(String name = 'human') {
     return {
        stage (name) {
            stage(name+"checkout") {
                echo "start 1"
                sleep 1
                echo "done 1"
            }
            stage("build") {
                echo "start 2"
                sleep 1
                echo "done 2"
            }
            stage("builcdddd") {
                echo "start 2"
                sleep 1
                echo "done 2"
            }
            stage("build") {
                echo "start 2"
                sleep 1
                echo "done 2"
            }
        }
    }
}
