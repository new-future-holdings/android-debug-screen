# DebugMenu (Android)
*A smart side drawer for debugging your Android apps.*

**This library is in very early stages of development. New features will be added and the API might change.**

### Usage
Add the following to your top level Gradle file:

```groovy
allprojects {
    repositories {
        …
        maven { url "https://jitpack.io" }
    }
}
```

...and this to the module-level build script (check the widget below the code snippet for the latest version):

```groovy
dependencies {
    …
    def debugMenuVersion = "0.0.10"
    debugImplementation "com.github.pandulapeter.debug-menu:debug-menu:$debugMenuVersion"
    releaseImplementation "com.github.pandulapeter.debug-menu:debug-menu-noop:$debugMenuVersion"
}
```

The latest version is:


[![](https://jitpack.io/v/pandulapeter/debug-menu.svg)](https://jitpack.io/#pandulapeter/debug-menu)

The library has to be initialized in the Application class by calling:

```kotlin
DebugMenu.attachToUi(this)
```

After this a list of modules needs to be provided, but this can be changed at any time and the UI will be automatically updated. See [this implementation](https://github.com/pandulapeter/debug-menu/blob/master/example/src/main/java/com/pandulapeter/debugMenuExample/DebugMenuExampleApplication.kt) for a detailed example.

### Modules
Any number of generic modules can be added in any order as long as they have a unique ID:
* [Text](https://github.com/pandulapeter/debug-menu/blob/master/debug-menu-core/src/main/java/com/pandulapeter/debugMenuCore/configuration/modules/TextModule.kt) - This module displays simple text content.
* LongText - TODO: Display a text that can be collapsed into a title.
* TextInput - TODO: Allows the user to enter free text.
* Slider - TODO: Allows the user to adjust a numeric value.
* ColorPicker - TODO: Allows the user to pick a color.
* [Toggle](https://github.com/pandulapeter/debug-menu/blob/master/debug-menu-core/src/main/java/com/pandulapeter/debugMenuCore/configuration/modules/ToggleModule.kt) - This module displays a switch with a custom title and behavior - ideal for feature toggles.
* [Button](https://github.com/pandulapeter/debug-menu/blob/master/debug-menu-core/src/main/java/com/pandulapeter/debugMenuCore/configuration/modules/ButtonModule.kt) - This module displays a button with configurable text and action.
* KeyValue - TODO: Displays a list of key-value pairs that can be collapsed into a title.
* [List](https://github.com/pandulapeter/debug-menu/blob/master/debug-menu-core/src/main/java/com/pandulapeter/debugMenuCore/configuration/modules/ListModule.kt) - This module displays an expandable list of items configured by you. When an item is selected, a custom callback gets invoked. Possible use cases could be providing a list of test accounts to make the login process faster.
* SingleSelectionList - TODO: Displays a list of radio buttons.
* MultipleSelectionList - TODO: Displays a lst of checkboxes.


Unique modules can only be added once as they are specific to a single use case:
* [Header](https://github.com/pandulapeter/debug-menu/blob/master/debug-menu-core/src/main/java/com/pandulapeter/debugMenuCore/configuration/modules/HeaderModule.kt) - This module will always stay at the top of the drawer and display general information about the app / build provided by you.
* [KeylineOverlayToggle](https://github.com/pandulapeter/debug-menu/blob/master/debug-menu-core/src/main/java/com/pandulapeter/debugMenuCore/configuration/modules/KeylineOverlayToggleModule.kt) - This module displays a switch that, when enabled, draws a grid over your app with configurable dimensions that you can use to check the alignments of your Views.
* [AppInfoButton](https://github.com/pandulapeter/debug-menu/blob/master/debug-menu-core/src/main/java/com/pandulapeter/debugMenuCore/configuration/modules/AppInfoButtonModule.kt) - This module displays a button that links to the Android App Info page for your app.
* ScreenshotButton - TODO: Displays a button that takes a screenshot of the current layout and allows the user to share it.
* [NetworkLogListModule](https://github.com/pandulapeter/debug-menu/blob/master/debug-menu-core/src/main/java/com/pandulapeter/debugMenuCore/configuration/modules/NetworkLogListModule.kt) - This module will display an expandable list of your OkHttp network activity. Each item can be tapped for more information. To use this functionality, the custom DebugMenuNetworkInterceptor needs to be added to the OkHTTP Client's builder, as implemented [here](https://github.com/pandulapeter/debug-menu/blob/master/example/src/main/java/com/pandulapeter/debugMenuExample/networking/NetworkingManager.kt).
* [LogListModule](https://github.com/pandulapeter/debug-menu/blob/master/debug-menu-core/src/main/java/com/pandulapeter/debugMenuCore/configuration/modules/LogListModule.kt) - This module will display an expandable list of your custom logs. Each item can be tapped for more information if you specified a payload. To log an event, simply call DebugMenu.log().
* DeviceConfigurationKeyValue - TODO: Display information about the device and the OS.

### Customization
* The UI of the drawer can be personalized by specifying a [UiCustomization](https://github.com/pandulapeter/debug-menu/blob/master/debug-menu-core/src/main/java/com/pandulapeter/debugMenuCore/configuration/UiCustomization.kt) instance when initializing the library.
* To properly support back navigation, all activities must check if the drawer consumes the event. This is implemented [here](https://github.com/pandulapeter/debug-menu/blob/master/example/src/main/java/com/pandulapeter/debugMenuExample/screens/MainActivity.kt).

### To do
* Add API to dynamically enable / disable the drawer
* Encapsulate the module list to make it safer to use
* Create a base class for dialogs with proper 2D scrolling
* Add support for dividers
* Add screenshots and a more detailed description to this readme

### License
```
Copyright 2019 Pandula Péter

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```