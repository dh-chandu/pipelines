
package jnpr.releng

public class Testerer implements Serializable {
    public String name = "test"

//    Testerer(String pName) {
//        this.name = pName
//    }

    def sayHi(String name) {
        println "Hello, ===== "+name
    }
}
