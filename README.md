# Location-Reminder-App


Introduction
------------
1. Create a Login screen to ask users to login using an email address or a Google account.  Upon successful login, navigate the user to the Reminders screen.   If there is no account, the app should navigate to a Register screen.
2. Create a Register screen to allow a user to register using an email address or a Google account.
3. Create a screen that displays the reminders retrieved from local storage. If there are no reminders, display a   "No Data"  indicator.  If there are any errors, display an error message.
4. Create a screen that shows a map with the user's current location and asks the user to select a point of interest to create a reminder.
5. Create a screen to add a reminder when a user reaches the selected location.  Each reminder should include:
    - title
    - description
    - selected location

6. Reminder data should be saved to local storage.
7. For each reminder, create a geofencing request in the background that fires up a notification when the user enters the geofencing area.
8. Provide testing for the ViewModels, Coroutines and LiveData objects.
9. Create a FakeDataSource to replace the Data Layer and test the app in isolation.
10. Use Espresso and Mockito to test each screen of the app:
    - Test DAO (Data Access Object) and Repository classes.
    - Add testing for the error messages.
    - Add End-To-End testing for the Fragments navigation.
   
 
This app using the following techniques:

 - Fragments
 - Room
 - Databinding
 - Coroutines
 - Transformation map
 - Retrofit
 - Gson Converter
 - ViewModel
 - LiveData
 - Navigation Component
 - RecyclerView
 - FirebaseUi Auth
 - Geofence
 - Koin
 - Testing


 Ps. It's the fourth project to submit for Udacity Advanced Android Kotlin Development Nanodegree. 
 
 App Screenshots
 ---------------
 <p align="center">
 <img src="https://user-images.githubusercontent.com/14341736/205301987-03fe35ba-b820-4168-9799-d1c0f5a11fef.jpg" width="180" height="400">
 <img src="https://user-images.githubusercontent.com/14341736/205301994-66214903-a50c-408f-a31f-6d9dc2428597.jpg" width="180" height="400">
 <img src="https://user-images.githubusercontent.com/14341736/205302017-745ba5fb-cac9-47b1-bb7f-2d914c71503b.jpg" width="180" height="400">
 <img src="https://user-images.githubusercontent.com/14341736/205302022-ec627d27-ebed-4b4d-bcd3-d3f47f9f7c2c.jpg" width="180" height="400">
 <img src="https://user-images.githubusercontent.com/14341736/205302026-a3bd63d9-da17-449b-8cb9-50e4ad126774.jpg" width="180" height="400">
 <img src="https://user-images.githubusercontent.com/14341736/205302042-123e3648-855c-44f8-8d97-d5aa8fda880a.jpg" width="180" height="400">
 <img src="https://user-images.githubusercontent.com/14341736/205302049-6c960130-7ba9-4c94-a31f-df228bc92032.jpg" width="180" height="400">
 </p>
