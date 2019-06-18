@ECHO OFF

SET LIB=bee\lib

SET CP=%LIB%\baccara.jar
SET CP=%CP%;%LIB%\bee.jar
SET CP=%CP%;%LIB%\commons-collections-2.1.jar
SET CP=%CP%;%LIB%\commons-io-2.4.jar
SET CP=%CP%;%LIB%\commons-lang-2.3.jar
SET CP=%CP%;%LIB%\corent.jar
SET CP=%CP%;%LIB%\corentx.jar
SET CP=%CP%;%LIB%\log4j-1.2.13.jar

java -Xmx256m -Xms256m -Dbee.home=%BEE_HOME% -Dlog4j.configuration=file:%BEE_HOME%/conf/log4j.xml -cp %CP% bee.Bee %1 %2 %3 %4 %5 %6 %7 %8 %9
