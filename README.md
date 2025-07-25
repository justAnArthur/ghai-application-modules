# GHAI | Government Help Application For Immigrants

A digital document provider app and document-approving system all in one.

## Creators

Arthur Kozubov, Zaitsev Artem, Sichkaruk Mykhailo, Vadym Tilihuzov, Sira Dariia

## Subject

VAVA | Development of Applications With Multilayer Architecture

## Documentation

A detailed documentation of the project can be found in the following link:

[Documentation](/docs/vava.pdf)

You could also watch our video on YouTube:

[![Video](https://img.youtube.com/vi/MFfC30np-No/0.jpg)](https://youtu.be/MFfC30np-No)

### Practitioner

Mgr. Ing. Miroslav Reiter, MBA

# Start an application with Maven

```
mvn clean install
```

```
cd server
mvn compile exec:java --% -Dexec.mainClass="fiit.vava.server.Server"
```

```
cd client
mvn install org.openjfx:javafx-maven-plugin:0.0.8:run 
```

# Compile to .jar

```
cd server
mvn clean compile package
```

```
cd client
mvn clean package
```

.jar s are in target folders.