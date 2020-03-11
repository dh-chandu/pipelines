
package jnpr.releng

public class DynamicFunction implements Serializable {
    public String name = "test"

    Tester(String pName) {
        this.name = pName
    }

    def sayHi() {
        println "Hello, ===== ${this.name}."
    }
}
