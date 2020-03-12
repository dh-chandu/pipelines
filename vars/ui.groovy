def call( name = '') {
     return {
        stage (name) {
            stage(name+"checkout") {
                echo "start 1"
                echo "UI checkout done"
            }
            stage("build") {
                echo "Ui build  "
                echo "UI done "
            }
            stage("test") {
                echo "UI test start "
                echo "UI testdone "
            }
            stage("build UI extra ") {
                echo "start 2"
                echo "done "
            }
            stage("CDH") {
                echo "UI cdh start 2"
                echo "done cdh"
            }
            stage("RAM") {
                echo "start 2"
                echo "done 2"
            }
        }
    }
}
