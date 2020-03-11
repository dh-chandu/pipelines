
package jnpr.releng

public class Tester implements Serializable {
    public String name = "test"

    Tester(String pName) {
        this.name = pName
    }

    def sayHi() {
        sh 'echo "Hello, ===== ${this.name}."'
    }
}
