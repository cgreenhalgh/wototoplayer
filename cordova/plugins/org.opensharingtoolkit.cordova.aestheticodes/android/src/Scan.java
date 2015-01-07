/* Aestheticodes scanner cordova plugin android native class
 *
 * Chris Greenhalgh, University of Nottingham, 2015.
 */
package org.opensharingtoolkit.cordova.aestheticodes;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.CordovaInterface;
import org.json.JSONArray;
import org.json.JSONException;

import android.util.Log;

public class Scan extends CordovaPlugin {
    public static final String TAG = "aestheticodes-scan";

    @Override
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);
        // your init code here
    }
    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if ("scan".equals(action)) {
            Log.d(TAG,"Scan!");
            callbackContext.success();
            return true;
        }
        return false;  // Returning false results in a "MethodNotFound" error.
    }
}

