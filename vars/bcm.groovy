def call(String name = 'human') {
     return {
        stage (name) {
            stage("BCM checkout") {
                echo "this is from BCM checkout"
                echo "BCM done"
            }
            stage("BCM Build") {
                echo "BCM build "
                echo "build done "
            }
        }
    }
}
