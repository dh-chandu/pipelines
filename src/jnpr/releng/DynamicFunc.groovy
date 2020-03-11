
package jnpr.releng

class DynamicFunc  {
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
    def sayHi(name){
        return  'Heee '+name
    }
}