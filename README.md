# Splinter - Toy Audio Synthesizer App

![Banner Image][banner]

## About the project

Splinter is an audio synthesizer app for creating, playing and manipulating sound. It's a passion project for learning digital signal processing by writing native code on mobile.
The current aim of the project is to:
* create a visually and tactile focused playing and learning experience, avoiding "knob-cluttering" visible on most synthesizers
* create a sturdy architecture whose components can be reused to build flexible sound shapes

Currently, the app is being developed for Android, but is planned to migrate to Kotlin Multiplatform for iOS and Desktop.

### Built With

* Languages: Kotlin, C++
* UI: Jetpack Compose
* DI: Koin
* Dependency management: Version catalog
* Reactivity: Kotlin Flows
* Testing: JUnit
* Audio processing: Oboe
* Building: Gradle, CMake

## Installation instructions

Tested on:
- Android Studio Ladybug | 2024.2.1 Patch 3
- Samsung Galaxy 21 FE

1. Clone the repository:

```sh
git clone git@github.com:Borkec/splinter.git
```

2. Open Android Studio and point it at the folder with `File > Open...`.
3. Build and run the project

<!-- MARKDOWN LINKS & IMAGES -->

[banner]: docs/banner.png