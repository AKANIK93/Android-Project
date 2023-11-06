***Steps to Implement Welcome To Exam App Android App:***

User need to follow the below given steps to implement the project.

Step 1: Unzip the project code (which you downloaded from the github download section) and open android studio.<br>

Step 2: Click on Open an existing Project and select the project.<br>

Step 3: You can see the project loaded on your android studio.<br>

Step 4: We have to setup the backend for that app(We have to connect that app with firebase)
***Steps to add app in firebase***<br>

In this, the steps involve:<br>

***Create a firebase project***<br>
 1.Create a project by clicking on create project in the firebase console.<br>
   ![image](https://github.com/cse-250-2018/G55-Exam-System-Android/blob/main/firebase1.png)


   Fill the necessary details in the pop up window about the project. Edit the project ID if required.<br>
   ![image](https://github.com/cse-250-2018/G55-Exam-System-Android/blob/main/firebase2.png)<br>

Click on create project to finally create it.<br>
2.Now add this project to the android app
   Click on the Add firebase to your android app option on the starting window.<br>
   ![image](https://github.com/cse-250-2018/G55-Exam-System-Android/blob/main/firebase3.png)<br>


A prompt will open where to enter the package name of the app.
Now the app is connected to the Firebase. Now all the cloud based as well server based services can be easily used in the app.
Now the app will be registered with firebase.<br>

3.Also, the SHA1 certificate, can be given, of the app by following steps:
Go to android studio project<br>
 ↳ gradle<br>
   ↳ root folder<br>
     ↳ Tasks<br>
       ↳ Android<br>
         ↳ signingReport<br>
           ↳ copy paste SHA1 from console<br>
 ![image](https://github.com/cse-250-2018/G55-Exam-System-Android/blob/main/firebase4.png)<br>

4.Now download the google-services.json file if your downloaded project has no json file and
place it in the root directory of the android app.<br>
![image](https://github.com/cse-250-2018/G55-Exam-System-Android/blob/main/firebase5.png)<br>

5.Now add the following in the project if these are not existing your given directory folder

Adding the sdk in the project.
Add the following code to the PROJECT-LEVEL build.gradle of the app.<br>
buildscript {<br>
  dependencies {<br>
    classpath 'com.google.gms:google-services:4.0.0'<br>
  }
}
Add the following code to APP-LEVEL build.gradle of the app.<br>
dependencies {<br>
  compile 'com.google.firebase:firebase-core:16.0.0'<br>
}<br>
...
// Add to the bottom of the file<br>
apply plugin: 'com.google.gms.google-services'<br>


6.Now Sync the gradle by clicking on sync now.
7.After adding the above code(sdk), run the app to send the verification to the Firebase console.
Firebase is now successfully installed.

Step 5: Now, you can press the run button to test the application on the emulator or your device.



***To run successfully***
In firebase console,
Authentication->Sign-In Method->
     Enable Google and Enable Email/Password
