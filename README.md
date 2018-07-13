# Nanodegree Capstone
This branch shows state of application uploaded as Capstone Project to Android Developer Nanodegree.
There were some restrictions and requirements for Capstone Project:
* application written solely in Java
* use AsyncTask <a href="https://github.com/nani92/GoNoteIt/blob/master_nanodegree/app/src/main/java/eu/napcode/gonoteit/ui/about/GithubDescriptionDownloadAsyncTask.java">example here</a>
* at least two chosen Google services: <a href="https://github.com/nani92/GoNoteIt/blob/master_nanodegree/app/src/main/java/eu/napcode/gonoteit/ui/favorites/FavoritesFragment.java">AdMob</a> and <a href="https://github.com/nani92/GoNoteIt/blob/master_nanodegree/app/src/main/java/eu/napcode/gonoteit/ui/create/CreateActivity.java">Analytics</a>
* <a href="https://github.com/nani92/GoNoteIt/blob/master_nanodegree/app/build.gradle">Gradle singng configuration</a> and <a href="https://github.com/nani92/GoNoteIt/blob/master_nanodegree/test_gonoteit.jks">Keystore file </a>

# GoNoteIt

<img src="https://raw.githubusercontent.com/nani92/GoNoteIt/gh-pages/logo-primaryColor.png"/>

## About
*GoNoteIt* is an application to take and share notes. 
In this repository you can find Android application for GoNoteIt which 
was started as Capstone Project for Android Developer Nanodegree at Udacity.

App is written in Java as it is nanodegree requirement. I've used MVVM as architectural pattern and 
Android Architecture Components.

## Used libraries
* LiveData
* PagedList
* Room
* Glide
* Apollo - GraphQL compliant client for Android
* OkHttp
* Dagger
* RxAndroid + RxJava
* GSON

## How to run
* Use Android Studio to run this project.
* In your `local.properties` file add `admob` variable with testing id for admob:
<br />`admob = "ca-app-pub-3940256099942544/6300978111"`

## Author
Natalia Jastrzębska

