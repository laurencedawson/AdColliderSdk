
# AdCollider Android SDK
[![Maven Central](https://img.shields.io/maven-central/v/com.adcollider/sdk.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22com.adcollider%22%20AND%20a:%22sdk%22)
[![API](https://img.shields.io/badge/API-21%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=21)

### Overview
AdCollider is a community ad network designed to help drive downloads to your app in exchange for hosting ads in your app.

### Join AdCollider
Message contact@laurencedawson.com to obtain an API key.

### Setup 

Insert the following dependency to `build.gradle` file of your project.
```groovy
dependencies {
    implementation 'com.adcollider:sdk:2.0.1'
}
```

### Requirements
- Android API Version 19
- Google Play Services

### Usage

Initialise AdCollider with your API key
```java
AdCollider.init(this, "replace-with-your-own-key");
```
Create an AdView (either from programatically or from XML)
```java
AdView adView = new AdView(context);  
parent.addView(adView);  
```
Request the ad
```java
adView.requestAd();
```
Optionally style the AdView for nightmode
```java
adView.setNightMode();
```

