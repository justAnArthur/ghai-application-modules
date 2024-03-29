# Changelog
## Resources should be in src/*/resources 
### Why?
It's standart.
https://maven.apache.org/guides/introduction/introduction-to-the-standard-directory-layout.html

For example, when building with maven /java/ folder files are ignored and not included in JAR file.


## JAR
Client is now can be launched both with:

cd client
mvn install org.openjfx:javafx-maven-plugin:0.0.8:run

or 

cd client

mvn package

java -jar /target/client-1.0-SNAPSHOT.jar

## There were some issues connected with JAR client packaging:

Client jar is built with Maven Shade plugin to include all needed dependencies in jar file. 

No main class in Manifest - Fixed

When Launching from jar file, the Main class should not extend Application, fixed by adding Wrapping class Main.java

Launching from JAR involving another method of route parsing – added, router's loadRoutes modified.

gRPC has 2 NameResolvers defined in Manifest, which led to NameResolver mismatch on application start, fixed by adding transformer to Maven-shade plugin configuration. 


## Minor changes:

fiit.vava.client.routes changed to fiit.vava.client.controllers as there are only controllers in the package.

added fxml folder to resources to hold fxml files. Routes are parsed from it now.

Offline mode is added for UI testing only, please don't delete while merging.
