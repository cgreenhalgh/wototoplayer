/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
var app = {
    // Application Constructor
    initialize: function() {
        this.bindEvents();
    },
    // Bind Event Listeners
    //
    // Bind any events that are required on startup. Common events are:
    // 'load', 'deviceready', 'offline', and 'online'.
    bindEvents: function() {
        document.addEventListener('deviceready', this.onDeviceReady, false);
        $('#home-url-button').on('click', function(ev) { app.onload() });
        $('#home-scan-button').on('click', function(ev) { app.onscan() });
        $('#home-scanqr-button').on('click', function(ev) { app.onscanqr() });
    },
    // deviceready Event Handler
    //
    // The scope of 'this' is the event. In order to call the 'receivedEvent'
    // function, we must explicitly call 'app.receivedEvent(...);'
    onDeviceReady: function() {
    	console.log("deviceready");
    	$('#home-url-button').prop('disabled', false);
    	$('#home-scan-button').prop('disabled', false);
    	$('#home-scanqr-button').prop('disabled', false);
    },
    onload: function() {
    	var url = $('#home-url-text').val();
    	console.log("load "+url);
    	window.open(url);
    },
    onscan: function() {
    	aestheticodes.scan(JSON.stringify({
    		"op":"temp",
    		"id": "org.opensharingtoolkit.aestheticodes.dynamic",
    		"version": 1,
    		"name": "Aestheticodes/Wototo",
    		"minRegions": 5,
    		"maxRegions": 5,
    		"maxEmptyRegions": 0,
    		"maxRegionValue": 6,
    		"validationRegions": 2,
    		"validationRegionValue": 1,
    		"checksumModulo": 3,
    		"thresholdBehaviour": "temporalTile",
    		"markers": [
    			{
    				"code": "1:1:1:1:2",
    				"action": "http://www.opensharingtoolkit.org"
    			}
    		]
       	}), function(result) {
    		if (result.cancelled)
    			alert("cancelled");
    		else
    			alert("success: "+result);
    	}, function(error) {
    		alert("error: "+error);
    	});
    },
    onscanqr: function() {
    	cordova.plugins.barcodeScanner.scan(function(result) {
    		if (result.cancelled)
    			alert("cancelled");
    		else
    			alert("success: "+result.text+" "+result.format);
    	}, function(error) {
    		alert("error: "+error);
    	});
    }
};
