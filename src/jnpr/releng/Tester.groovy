
package de.schlumpf

public class Tester implements Serializable {
  public String name = "test"

    Tester(String pName) {
        this.name = pName
          }

            def sayHi() {
                echo "Hello, ${this.name}."
                  }

                    def sayHi(String name) {
                        echo "Hello, ${name}."
                          }
                          }
