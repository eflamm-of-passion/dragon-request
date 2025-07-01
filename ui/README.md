To run the app with hot reload : 
```bash
 ./gradlew :composeApp:wasmJsBrowserDevelopmentRun -t
```

This is a Kotlin Multiplatform project targeting Web, Desktop.

* `/composeApp` is for code that will be shared across your Compose Multiplatform applications.
  It contains several subfolders:
  - `commonMain` is for code that’s common for all targets.
  - Other folders are for Kotlin code that will be compiled for only the platform indicated in the folder name.
    For example, if you want to use Apple’s CoreCrypto for the iOS part of your Kotlin app,
    `iosMain` would be the right folder for such calls.


Learn more about [Kotlin Multiplatform](https://www.jetbrains.com/help/kotlin-multiplatform-dev/get-started.html),
[Compose Multiplatform](https://github.com/JetBrains/compose-multiplatform/#compose-multiplatform),
[Kotlin/Wasm](https://kotl.in/wasm/)…

You can open the web application by running the `:composeApp:wasmJsBrowserDevelopmentRun` Gradle task.

## Roadmap
- branch to all the endpoints
- handle the results of API
- add CORS header only if dev mode
- find a way to debug
- dark mode
- logging
- create a Docker container
- check what I can do for testing
- store the endpoints on local storage before synchronizing on remote

## Tasks

- load the endpoints from the API
  - rework the model in the API
  - call the API
  - map the API model to the front model

- click Add
- create initiated endpoint
- select current endpoint
- fill the form