if "%1" == "1" (
cls
Echo "Building Package with Tests"
mvn clean install -DskipTest=false
goto EOP)

cls
Echo "Building Package without Tests"
mvn clean install -DskipTest=true
EOP:
break
