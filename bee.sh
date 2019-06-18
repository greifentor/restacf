export JAVA_HOME=~/prg/jdk-11.0.3

export BEE_HOME=bee

export LIB=$BEE_HOME/lib

export CP=$LIB/baccara.jar
export CP=$CP:$LIB/bee.jar
export CP=$CP:$LIB/commons-collections-2.1.jar
export CP=$CP:$LIB/commons-io-2.4.jar
export CP=$CP:$LIB/commons-lang-2.3.jar
export CP=$CP:$LIB/corent.jar
export CP=$CP:$LIB/corentx.jar
export CP=$CP:$LIB/log4j-1.2.13.jar

java -Xmx256m -Xms256m -Dbee.home=$BEE_HOME -Dlog4j.configuration=file:$BEE_HOME/conf/log4j.xml -cp $CP bee.Bee $1 $2 $3 $4 $5 $6 $7 $8 $9
