package jnpr.releng

class dynamicFunc{
    def ui(value) {
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
            }
        }
    }
    def bcm() {
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
            }
        }
    }
}