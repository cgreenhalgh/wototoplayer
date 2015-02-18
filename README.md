# WototoPlayer

Cordova-based application to host Wototo web apps and provide additional functionality. Initially for Android.

Includes (in place) plugin for doing aestheticode scanning.

Includes a custom plugin (also under cordova/plugins/org.opensharingtoolkit.aestheticodes) to link to aestheticodes scanning app.

## Cordova plugin management

Requires [plugman](http://cordova.apache.org/docs/en/4.0.0/plugin_ref_plugman.md.html). With node installed,
```
sudo npm install -g plugman
```
May also need cordova android tools from [dist](https://www.apache.org/dist/cordova/), specifically `platforms/cordova-android-3.x.x.tgz`.

E.g. install [com.phonegap.plugins.barcodescanner](http://plugins.cordova.io/#/package/com.phonegap.plugins.barcodescanner) from [repo](https://github.com/wildabeast/BarcodeScanner.git)
```
plugman install --platform android --project wototoplayer --plugin com.phonegap.plugins.barcodescanner
plugman install --platform android --project wototoplayer --plugin org.apache.cordova.geolocation
plugman install --platform android --project wototoplayer --plugin org.apache.cordova.vibration
plugman install --platform android --project wototoplayer --plugin org.apache.cordova.network-information
plugman install --platform android --project wototoplayer --plugin org.apache.cordova.device-orientation
plugman install --platform android --project wototoplayer --plugin org.apache.cordova.device-motion
plugman install --platform android --project wototoplayer --plugin org.apache.cordova.device
plugman install --platform android --project wototoplayer --plugin org.apache.cordova.battery-status
```

Note: git archive doesn't include complete plugin stuff, so may need to repeat the above when adding new plugin(s).

## Loading apps

Current registered for custom URI scheme `x-wototo`. Should open automatically on browse to app link with that scheme.

Also registered for custom mime type `application/x-wototo`. My phone downloads this and then opens it from `content://downloads/...`, which is then rejected by [CordovaBridge.java](https://github.com/apache/cordova-android/blob/master/framework/src/org/apache/cordova/CordovaBridge.java) with an error of `gap_init called from restricted origin` because there is a check that external origin starts with "http". Might work if the initial page just did a client-side redirect to the web version.

