
package jnpr.releng

public class Testerer implements Serializable {
    public String name = "test"

    Testerer(String pName) {
        this.name = pName
    }

    def sayHi(name) {
        println "Hello, ===== ${this.name}."
    }
}
