The IBM MQ clients require MQ clients jars.

I have:

Manifest-Version: 1.0
Specification-Title: WebSphere MQ classes for Java
Class-Path: com.ibm.mq.jar connector.jar jta.jar
Sealed: false
Created-By: 1.4.2 (IBM Corporation)
Implementation-Title: WebSphere MQ classes for Java
Specification-Vendor: IBM Corporation
Specification-Version: 6.0.2.10
Implementation-Version: 6.0.2.10 - j600-210-100702
Name: com/ibm/mq
Implementation-Vendor: IBM Corporation

You can add these to your local repository:

mvn install:install-file -Dfile=com.ibm.mq.jar -DgroupId=com.ibm.mq -DartifactId=com.ibm.mq -Dversion=6.0.2.10 -Dpackaging=jar
mvn install:install-file -Dfile=connector.jar -DgroupId=com.ibm.mq -DartifactId=com.ibm.mq.connector -Dversion=6.0.2.10 -Dpackaging=jar
mvn install:install-file -Dfile=jta.jar -DgroupId=com.ibm.mq -DartifactId=com.ibm.mq.jta -Dversion=6.0.2.10 -Dpackaging=jar

