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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import org.apache.cordova.*;

public class CordovaApp extends CordovaActivity
{
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
		checkIntent(intent);
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
				Log.d(TAG,"Load from rewritted wototo intent "+url);
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
