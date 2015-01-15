/*
       Licensed to the Apache Software Foundation (ASF) under one
       or more contributor license agreements.  See the NOTICE file
       distributed with this work for additional information
       regarding copyright ownership.  The ASF licenses this file
       to you under the Apache License, Version 2.0 (the
       "License"); you may not use this file except in compliance
       with the License.  You may obtain a copy of the License at

         http://www.apache.org/licenses/LICENSE-2.0

       Unless required by applicable law or agreed to in writing,
       software distributed under the License is distributed on an
       "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
       KIND, either express or implied.  See the License for the
       specific language governing permissions and limitations
       under the License.
 */

package org.opensharingtoolkit.player;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import org.apache.cordova.*;
import org.opensharingtoolkit.cordova.aestheticodes.Scan;

public class CordovaApp extends CordovaActivity
{
	public static String TAG = "wototoplayer";
	private boolean newActivity = true;
	private String wototoUrl = null;
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        super.init();
        // Set by <content src="index.html" /> in config.xml
        if (!checkIntent(getIntent()))
            loadUrl(launchUrl);
    }
	@Override
	protected void onNewIntent(Intent intent) {
		setIntent(intent);
		checkIntent(intent);
	}

	@Override
	protected void onResume() {
		newActivity = false;
		super.onResume();
	}
	public static String encodeURIComponent(String s) {
		String result = null;
		try {
			result = URLEncoder.encode(s, "UTF-8")
					.replaceAll("\\+", "%20")
					.replaceAll("\\%21", "!")
					.replaceAll("\\%27", "'")
					.replaceAll("\\%28", "(")
					.replaceAll("\\%29", ")")
					.replaceAll("\\%7E", "~");
		}
		// This exception should never occur.
		catch (UnsupportedEncodingException e) {
			result = s;
		}
		return result;
	}  
	/* (non-Javadoc)
	 * @see org.apache.cordova.CordovaActivity#onActivityResult(int, int, android.content.Intent)
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		Log.d(TAG, "onActivityResult "+requestCode+" "+resultCode+" "+(intent!=null ? "marker="+intent.getStringExtra("marker") : ""));
		// word-around aestheticode scan if process killed/restarted
		// onActivityResult seems to arrive after loadUrl and before Resume
		if (newActivity && requestCode==Scan.AESTHETICODES_SCAN_REQUEST_CODE && resultCode==Activity.RESULT_OK && intent!=null) {
			String marker = intent.getStringExtra(Scan.EXTRA_MARKER);
			if (wototoUrl!=null) {
				String url = wototoUrl;
				int ix = url.indexOf("#");
				if (ix>=0)
					url = url.substring(0,ix);
				url = url+"#unlock/artcode/"+encodeURIComponent(marker);
				Log.i(TAG, "scan return for new activity: "+marker+" -> "+url);
				loadUrl(url);
				return;
			} else
				Log.d(TAG, "scan return ("+marker+") for new activity without wototoUrl");
		}
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, intent);
	}

	static int MAX_LENGTH = 10000;
	private boolean checkIntent(Intent intent) {
		if (Intent.ACTION_VIEW.equals(intent.getAction())) {
			Uri uri = intent.getData();
			if ("x-wototo".equals(uri.getScheme())) {
				String url = uri.toString();
				url = "http"+url.substring(8);
				if (url.contains("?"))
					url = url+"&wototo=1";
				else
					url = url+"?wototo=1";
				Log.d(TAG,"Load from rewritten wototo intent "+url);
				wototoUrl = url;
				loadUrl(url);
			}
			else if ("content".equals(uri.getScheme())) {
				Log.d(TAG,"Check for redirect in content "+uri);
				try {
					InputStream is = this.getContentResolver().openInputStream(uri);
					Reader r = new InputStreamReader(new BufferedInputStream(is), "UTF-8");
					StringBuilder sb = new StringBuilder();
					char buf[] = new char[MAX_LENGTH];
					int len = r.read(buf);
					is.close();
					if (len>=0) {
						String in = new String(buf, 0, len);
						Log.d(TAG,"Read content "+in);
						Matcher m = Pattern.compile("<meta\\s+http-equiv=\"refresh\"\\s+content=\"[^;\"]*;?url=([^\"]*)\"").matcher(in);
						if (m.find()) {
							String url = m.group(1).replace("&amp;", "&");
							Log.d(TAG,"Load from redirect "+url);
							wototoUrl = url;
							loadUrl(url);
							return true;
						}
					}
					else
						Log.w(TAG,"Could not read any characters from "+uri);
				}
				catch (Exception e) {
					Log.w(TAG,"Error reading "+uri+": "+e);
				}
			}
			else {
				Log.d(TAG,"Load from intent "+uri);
				loadUrl(uri.toString());
			}
			return true;
		}
		return false;
	}
}
