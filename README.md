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

## ROADMAP
- display logs when 500
- fix docker
- add swagger
- add hypermedia links

## TODO
- save in the database

## Architecture

The application implements the hexagonal architecture.
<img src="./doc/hexagonal-architecture.svg">
