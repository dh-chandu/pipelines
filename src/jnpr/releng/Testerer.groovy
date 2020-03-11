
package jnpr.releng

public class Testerer implements Serializable {
    public String name = "test"

    Tester(String pName) {
        this.name = pName
    }

    def sayHi() {
        println "Hello, ===== ${this.name}."
    }
}
