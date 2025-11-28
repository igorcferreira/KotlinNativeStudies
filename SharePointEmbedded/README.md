# SharePoint Embedded Sample

This sample code uses [Microsoft's Graph API](https://learn.microsoft.com/en-us/graph/api/overview?view=graph-rest-1.0&preserve-view=true) to integrate with [SharePoint Embedded](https://learn.microsoft.com/en-us/sharepoint/dev/embedded/overview).

In this sample, the following items are demonstrated:

- App Authentication, to allow the upload/download from folders not assigned to a user.
- User Authentication, to fetch basic user profile data.
- Show the content of a SharePoint Container to demonstrate navigation.
- Preview of SharePoint content using Microsoft's Graph API preview.

### Sample

| iOS                                  | Android                                      |
|--------------------------------------|----------------------------------------------|
| ![iOS Demo](docs/ios_recording.webp) | ![Android Demo](docs/android_recording.webp) |

### Architecture

This is a Kotlin Multiplatform project targeting Android, iOS.

* [/composeApp](./composeApp/src) is for code that will be shared across your Compose Multiplatform applications.
  It contains several subfolders:
    - [commonMain](./composeApp/src/commonMain/kotlin) is for code that’s common for all targets.
    - Other folders are for Kotlin code that will be compiled for only the platform indicated in the folder name.
      For example, if you want to use Apple’s CoreCrypto for the iOS part of your Kotlin app,
      the [iosMain](./composeApp/src/iosMain/kotlin) folder would be the right place for such calls.

* [/iosApp](./iosApp/iosApp) contains iOS applications. Even if you’re sharing your UI with Compose Multiplatform,
  you need this entry point for your iOS app. This is also where you should add SwiftUI code for your project.

* [/shared](./shared/src) is for the code that will be shared between all targets in the project.
  The most important subfolder is [commonMain](./shared/src/commonMain/kotlin). If preferred, you
  can add code to the platform-specific folders here too.

* [/graphapi](./graphapi/src) is for the code which interacts with [Microsoft's Graph API](https://learn.microsoft.com/en-us/graph/api/overview?view=graph-rest-1.0&preserve-view=true)
  to integrate with [SharePoint Embedded](https://learn.microsoft.com/en-us/sharepoint/dev/embedded/overview).

### Setup environment

This sample uses [Arkana](https://github.com/rogerluan/arkana) to support with its configuration while keeping application's client id and secret safe.
To run the sample, you will need to:

1. Copy the `.env.sample` file as `.env` and update the values, at the project root
2. Install the dependencies by running `bundle install`, using terminal, at the project root
3. Install the configuration by running `bundle exec arkana -l kotlin`, using terminal, at the project root

The exec command needs to be repeated every time in which an environment variable changes.

### Build and Run Android Application

To build and run the development version of the Android app, use the run configuration from the run widget
in your IDE’s toolbar or build it directly from the terminal:

- on macOS/Linux
  ```shell
  ./gradlew :composeApp:assembleDebug
  ```
- on Windows
  ```shell
  .\gradlew.bat :composeApp:assembleDebug
  ```

### Build and Run iOS Application

To build and run the development version of the iOS app, use the run configuration from the run widget
in your IDE’s toolbar or open the [/iosApp](./iosApp) directory in Xcode and run it from there.

---

Learn more about [Kotlin Multiplatform](https://www.jetbrains.com/help/kotlin-multiplatform-dev/get-started.html)…
