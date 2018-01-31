# android_prototype

# Get Started

# SourceTree
* Download from https://www.sourcetreeapp.com/
* Sign in using your github credentials
* You should see a window (the top looks like this)
* <p><img src="https://i.imgur.com/9UseUPq.png" /></p>
* Go to remote, if you accepted the invite you should see the prototype
* <p><img src="https://i.imgur.com/ubd81FK.png" /></p>
* Click Clone, select where you want to save it (doesn't matter, up to you)
* Once you have cloned the repository, go to local and double click. This will open the repo window
* Click Branch (see picture)
* <p><img src="https://i.imgur.com/HS1aXBc.png" /></p>
* Name your branch your first name

# Android Studio
* Open Android Studio, you should see a screen like this
* <p><img src="https://i.imgur.com/Ml6peYk.png" /></p>
* Select import project and it will configure
* Android Studio may require tools to be downloaded if this is the first time you've used it, it should prompt you to download what it needs
* If you've done this correctly you can then go to top left corner of the window, select project on the side and then the drop down box select Android, if you can't do this you've opened the wrong folder (see picture)
* <p><img src="https://i.imgur.com/f0Efbny.png" /></p>
* You should now see a folder called app in the project navigator

# manifest folder
* Houses the manifest file of the project, this file tells the system what to do with the app, you probably won't need to worry about this file

# java folder
* This is where the code sits, don't worry about the (androidTest) and (test) folders.
* Activities are a screen with an interface
* Fragments are used with tab bars (fragments may or may not have underlying activities depending, don't worry about this for now)
* Anything else that isn't named Fragment or Activity are helper files

# res folder
* Your resources can be found here
* drawable folder is where your images go
* layout folder is where XML files go for fragment and activity interfaces
* menu folder helps you setup the apps menu like styles, what goes in the overflow etc
* mipmap folder is where your icons go (ONLY ICONS)
* values folder is where global variables are housed

# gradle scripts
* Gradle files automate your builds, lets you add external libraries and other stuff

# Run the app
* Click on the android virtual device manager icon (see picture)
* <p><img src="https://i.imgur.com/sMELRa0.png" /></p>
* Click on create virtual device (see picture)
* <p><img src="https://i.imgur.com/koOIcQx.png" /></p>
* Choose any device you like, I typically have one 5"+ device and 4" for layout testing
* Choose an image, use API level 26
* This will download the necessary files needed
* Click the play button in the top toolbar (see picture)
* <p><img src="https://i.imgur.com/389uA0l.png" /></p>

# How to use
* Signup in app
* Once signup is successful you will be pushed to login screen
* Log into Firebase and select Database
*<p><img src="https://imgur.com/LMnXggi" /></p>
* From here you can see the users within our database, expand the user you want to add information to
*<p><img src="https://imgur.com/BmAtFbx" /></p>
* From here we can see the users information
* <p><img src="https://imgur.com/zOCMF0w" /></p>

* We can now add key/value pairs which show up in the app.
* Current keys that work within the prototype:
* address
* dailyCost
* dailyUsage
* email 
* firstName 
* lastMonthCost
* lastMonthUsage
* monthlyCost
* monthlyUsage
* No keys currently have validation checking (costs and usages as numeric only etc)
* Log into app, (or stayed logged in to see the change live) and the app will be populated with data.
* Some of these fields are required, otherwise you will be shown an account currently processing messagem, best to add all key pairs.


# How to add more data to app
* Log into Firebase Database and add desired key/value pair e.g.(telephone 0400000000) (https://console.firebase.google.com)
* Go to project in Android Studio, select EITHER fragment_account or fragment_usage
* Add new TextView to either fragment
* Initialise the TextView in the onActivityCreated(Bundle savedInstanceState) method
* See fragments showData() method on how to retrieve data and render to screen

# As long as you branch off master, you can do what you like. If you break your branch just nuke it, rebranch and start again.
