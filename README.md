# Dragon reQuest

Run the app :

```shell script
./gradlew run
```

To build and run the app :

```shell script
./gradlew clean build
java -jar infrastructure/build/libs/infrastructure-1.0-SNAPSHOT.jar
```

To list all the modules that are loaded :

```shell script
./gradlew -q projects
```

## TODO

- check the diagram
- integrate a frontend
- add hypermedia links

## Architecture

The application implements the hexagonal architecture.
<img src="./doc/hexagonal-architecture.svg">
