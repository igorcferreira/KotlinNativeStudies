# CloudKitFeatureFlag

This is a Kotlin Multiplatform project targeting Android, iOS.

I was reading a post made by [Gui Rambo](https://mastodon.social/@_inside) where he demonstrates the usage of [CloudKit for content hosting and feature flags](https://rambo.codes/posts/2021-12-06-using-cloudkit-for-content-hosting-and-feature-flags). In there, he mentions:

> If you'd like to consume the same content that you're hosting on the public CloudKit database from an Android app or from a web app, you can. You can use the CloudKit Web Services API, which lets you do pretty much everything that can be done through the CloudKit framework over HTTP.

And this triggered something in my mind: if CloudKit has an HTTP API, it can be used as a feature flag control in Kotlin Multiplatform solutions.

### Folder structure

* [/composeApp](./composeApp/src) contains the Android application.

* [/iosApp](./iosApp/iosApp) contains iOS applications. Even if you’re sharing your UI with Compose Multiplatform,
  you need this entry point for your iOS app. This is also where you should add SwiftUI code for your project.

* [/shared](./shared/src) is for the code that will be shared between all targets in the project.
  The most important subfolder is [commonMain](./shared/src/commonMain/kotlin). If preferred, you
  can add code to the platform-specific folders here too.

### Project configuration

This projects uses [Arkana](https://github.com/rogerluan/arkana) as a way to inject configuration.

Before running the project, you need to:

1. Install Arkana
2. Create a `.env`
3. Add `Container` (iCloud Drive container id) and `RecordName` (iCloud record name) into the .env file
4. Run `arkana -l kotlin`

The `.env.sample` has a demonstration of how this `.env` file should look like.

### Build and Run Android Application

To build and run the development version of the Android app, use the run configuration from the run widget
in your IDE’s toolbar or build it directly from the terminal:

- on macOS/Linux
  ```shell
  ./gradlew :composeApp:assembleDebug
  ```

### Build and Run iOS Application

To build and run the development version of the iOS app, use the run configuration from the run widget
in your IDE’s toolbar or open the [/iosApp](./iosApp) directory in Xcode and run it from there.

---

Learn more about [Kotlin Multiplatform](https://www.jetbrains.com/help/kotlin-multiplatform-dev/get-started.html)…
