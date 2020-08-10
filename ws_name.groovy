def getSbname(machine, SB_LOCATION) {
    node(machine){
        def SPIN = 1
        time = new Date().format("yyyyMMdd")
        SB_NAME = SB_LOCATION+"pub-"+time+"."+SPIN
        while (fileExists(SB_NAME)) {
            SPIN+=1
            SB_NAME = SB_LOCATION+"pub-"+time+"."+SPIN
            folder = new File( SB_NAME )
        }
