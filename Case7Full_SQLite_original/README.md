# Case3FlashLight

Android RuntimePermissionsBasic Sample

This basic sample shows runtime permissions available in the Android M and above.
It shows how to use the new runtime permissions API to check and request permissions through the
support library.

Introduction
------------

Android M introduced runtime permissions. Applications targeting M and above need to request their
permissions at runtime.
This sample introduces the basic use of the runtime permissions API through the support library by
verifying permissions (ActivityCompat#checkSelfPermission(Context, String)), requesting permissions (ActivityCompat#requestPermissions(Activity, String[], int))
and handling the permission request callback (ActivityCompat.OnRequestPermissionsResultCallback).
An application can display additional context and justification for a permission after calling
ActivityCompat#shouldShowRequestPermissionRationale#shouldShowRequestPermissionRationale(Activity, String).

See the "RuntimePermissions" sample for a more complete description and reference implementation.

Pre-requisites
--------------

- Android SDK 28
- Android Build Tools v28.0.3
- Android Support Repository

Screenshots
-------------

<img src="screenshots/screenshot-1.png" height="400" alt="Screenshot"/> 

Getting Started
---------------

This sample uses the Gradle build system. To build this project, use the
"gradlew build" command or use "Import Project" in Android Studio.

Support
-------

- Stack Overflow: http://stackoverflow.com/questions/tagged/android

If you've found an error in this sample, please file an issue:
https://github.com/android/permissions-samples

Patches are encouraged, and may be submitted by forking this project and
submitting a pull request through GitHub. Please see CONTRIBUTING.md for more details.


 <pre>
  Copyright 2021 The Learning Android with Cases Open Source Project

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
  
</pre>
