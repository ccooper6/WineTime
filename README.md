# WineTime
WineTime is an application by wine lovers, for wine lovers. 

To use WineTime, you must create an account upon launch. We recommend doing the quiz upon launch, located in your profile, to kickstart your wine browsing journey.

## Authors
- SENG202 Teaching team
- Isaac Macdonald
- Caleb Cooper
- Elise Newman
- Lydia Jackson
- Wen Sheng Thong
- Yuhao Zhang

## Prerequisites
- JDK >= 21 [click here to get the latest stable OpenJDK release (as of writing this README)](https://jdk.java.net/18/)
- Gradle [Download](https://gradle.org/releases/) and [Install](https://gradle.org/install/)


## Importing Project (Using IntelliJ)
IntelliJ has built-in support for Gradle. To import your project:

- Launch IntelliJ and choose `Open` from the start up window.
- Select the project and click open
- At this point in the bottom right notifications you may be prompted to 'load gradle scripts', If so, click load

**Note:** *If you run into dependency issues when running the app or the Gradle pop up doesn't appear then open the Gradle sidebar and click the Refresh icon.*

## Build Project
- Open a command line interface inside the project directory and run `./gradlew run` to build a .jar file. The file is located at build/libs/wino-1.0-SNAPSHOT.jar

## Run App
- If you haven't already, Build the project.
- Open a command line interface inside the project directory and run `cd build/libs` to change into the right directory.
- Run the command `java -jar wino-1.0-SNAPSHOT.jar` to open the application.

## How to run tests
- Open a command line interface inside the projection directory and run `./gradlew test`

## Credit
- Red, White, Rosé and Sparkling Wine Icons - https://www.flaticon.com/free-icon/wine-glass_763048
- Default Wine Icon - https://freepik.com/icon/wine-bottle_5622688
