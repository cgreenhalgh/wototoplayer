package org.opensharingtoolkit.player;

import java.io.UnsupportedEncodingException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;

public class ArtcodeRunner extends Activity {

	private static final String SCHEME_ARTCODE_DATA = "x-artcode-data";
	private static final String TAG = "artcode-runner";
	private static final int SCAN_REQUEST = 1;
	private static final String KEY_DONE_SCAN = "done-scan";
	private String data;
	private JSONObject experience;
	private boolean doneScan = false;
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		readExperience();
		doneScan = savedInstanceState!=null ? savedInstanceState.getBoolean(KEY_DONE_SCAN, false) : false;
		if (!doneScan) {
			doScan();
		}
	}
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onSaveInstanceState(android.os.Bundle)
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putBoolean(KEY_DONE_SCAN, doneScan);
	}
	private void readExperience() {
		Intent i = getIntent();
		if (Intent.ACTION_VIEW.equals(i.getAction()) && SCHEME_ARTCODE_DATA.equals(i.getScheme())) {
			data = i.getData().toString();
			if (data.startsWith(SCHEME_ARTCODE_DATA+":"))
				// should!
				data = data.substring(SCHEME_ARTCODE_DATA.length()+1);
			try {
				data = new String(Base64.decode(data, Base64.DEFAULT), "UTF-8");
			} catch (UnsupportedEncodingException e) {
				Log.e(TAG,"Error decoding base64 data "+data+": "+e);
				finish();
				return;
			}
			try {
				experience = new JSONObject(data);
			}
			catch (JSONException e) {
				Log.e(TAG,"Error decoding json data "+data+": "+e);
				finish();
				return;
			}
		} else {
			Log.d(TAG,"start artcodes without data");
			finish();
			return;
		}
	}
	private void doScan() {
		doneScan = true;
		if (experience!=null && data!=null) {
			Log.d(TAG,"start artcodes: "+data);
			Intent ai = new Intent("uk.ac.horizon.aestheticodes.SCAN");
	        ai.putExtra("experience", data);
	        startActivityForResult(ai, SCAN_REQUEST);
		}
		else
			Log.d(TAG,"Cannot scan - no valid data");
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onActivityResult(int, int, android.content.Intent)
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode==Activity.RESULT_CANCELED) {
			Log.w(TAG,"scan cancelled");
			finish();
		} else if (resultCode==Activity.RESULT_OK) {
			String marker = data.getStringExtra("marker");
			if (marker==null) {
				Log.w(TAG,"scan result for null marker");
				finish();
			}
			String action = null;
			try {
				action = experience.has("defaultAction") ? experience.getString("defaultAction") : null;
				JSONArray jmarkers = experience.getJSONArray("markers");
				for (int i=0; i<jmarkers.length(); i++) {
					JSONObject jmarker = jmarkers.getJSONObject(i);
					if (marker.equals(jmarker.getString("code"))) {
						action = jmarker.getString("action");
					}
				}
			}
			catch (Exception e) {
				Log.w(TAG,"error mapping marker "+marker+" to action: "+e);
			}
			if (action==null) {
				Log.d(TAG,"No mapping for marker "+marker+" and no default action");
				finish();
				return;
			}
			action = action.replace("$marker", marker);
			Log.d(TAG,"Marker "+marker+" => "+action);
			Intent i = new Intent(Intent.ACTION_VIEW);
			i.addCategory(Intent.CATEGORY_BROWSABLE);
			i.setData(Uri.parse(action));
			startActivity(i);
			finish();
		} 
	}
	
}
