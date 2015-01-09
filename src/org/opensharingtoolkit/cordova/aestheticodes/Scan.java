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

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

public class Scan extends CordovaPlugin {
    public static final String TAG = "aestheticodes-scan";
    private CallbackContext callbackContext;
    
    @Override
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);
        // your init code here
    }
    @Override
    public boolean execute(String action, JSONArray args, final CallbackContext callbackContext) throws JSONException {
        Log.d(TAG,"action "+action+" with "+args.length()+" arguments");
        if ("scan".equals(action)) {
        	final String experience = args.getString(0);
            Log.d(TAG,"Scan "+experience);
        	Intent i = new Intent("uk.ac.horizon.aestheticodes.SCAN");
        	i.putExtra("experience", experience);
        	// copied this tactic, but seems dubious...
        	this.callbackContext = callbackContext;
            cordova.startActivityForResult(this, i, 0);
            return true;
        }
        return false;  // Returning false results in a "MethodNotFound" error.
    }
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        // Result received okay
        if (resultCode == Activity.RESULT_OK) {
        	this.callbackContext.success();
        } else {
        	this.callbackContext.error("error return from activity");
        }
    }
}

