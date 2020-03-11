
package jnpr.releng

public class DynamicFunc implements Serializable  {
    public String name = ""
    DynamicFunc(String pName) {
        this.name = pName
    }
    def ui() {
        return {
            stage (this.name) {
                stage(this.name+"checkout") {
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
    def sayHi(){
        return  'Heee '+this.name
    }
}