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
```

