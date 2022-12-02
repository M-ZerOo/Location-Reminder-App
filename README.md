# Location-Reminder-App


Introductions
-------------
1. Create a Login screen to ask users to login using an email address or a Google account.  Upon successful login, navigate the user to the Reminders screen.   If there is no account, the app should navigate to a Register screen.
2. Create a Register screen to allow a user to register using an email address or a Google account.
3. Create a screen that displays the reminders retrieved from local storage. If there are no reminders, display a   "No Data"  indicator.  If there are any errors, display an error message.
4. Create a screen that shows a map with the user's current location and asks the user to select a point of interest to create a reminder.
5. Create a screen to add a reminder when a user reaches the selected location.  Each reminder should include
    a. title
    b. description
    c. selected location
6. Reminder data should be saved to local storage.
7. For each reminder, create a geofencing request in the background that fires up a notification when the user enters the geofencing area.
8. Provide testing for the ViewModels, Coroutines and LiveData objects.
9. Create a FakeDataSource to replace the Data Layer and test the app in isolation.
10. Use Espresso and Mockito to test each screen of the app:
    a. Test DAO (Data Access Object) and Repository classes.
    b. Add testing for the error messages.
    c. Add End-To-End testing for the Fragments navigation.
   
 
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
