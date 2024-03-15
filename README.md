# start an application

```
mvn clean install
```

```
cd server
mvn clean compile exec:java --% -Dexec.mainClass="fiit.vava.server.Server"
```

```
cd client
mvn compile org.openjfx:javafx-maven-plugin:0.0.8:run 
```