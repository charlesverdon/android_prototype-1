# android_prototype

Get Started

*Clone repo to your machine (use something like SourceTree https://www.sourcetreeapp.com/)
*Open the root project folder using Android Studio
*Click play to build to a virtual device (you may need to set up a virtual device https://developer.android.com/studio/run/managing-avds.html)

How to use

*Signup in app
*Once signup is successful you will be pushed to login screen
*Log into Firebase Database and add key/value pair (firstName your first name)
*Log into app, go to account tab
*First name and email should be populated

How to add more data to app

*Log into Firebase Database and add desired key/value pair e.g.(telephone 0400000000)
*Go to project in Android Studio, select EITHER fragment_account or fragment_usage
*Add new TextView to either fragment
*Initialise the TextView in the onActivityCreated(Bundle savedInstanceState) method
*See fragments showData() method on how to retrieve data and render to screen
