def call(String name = '') {
    return {
        stage (name) {
            stage(name+" checkout") {
                echo "$name checkout "
                echo "$name checkout done"
            }
            stage(name+" Build") {
                echo "$name build "
                echo "$name build done "
            }
            stage(name+" test") {
                echo "$name Test "
                echo "$name Test done "
            }
        }
    }
}
