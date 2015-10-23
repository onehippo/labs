/*
 * Copyright 2010-2013 Hippo B.V. (http://www.onehippo.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

var geocoder;
var map;
function initialize() {
    if(document.getElementById("address")){
    geocoder = new google.maps.Geocoder();
    var latlng = new google.maps.LatLng(0, 0);
    var myOptions = {
      zoom: 10,
      center: latlng,
      mapTypeId: google.maps.MapTypeId.ROADMAP
    }
    map = new google.maps.Map(document.getElementById("map_canvas"), myOptions);
    }     
}

function codeAddress() {
    if(document.getElementById("address")){
    var address = document.getElementById("address").value;
    if (geocoder) {
        geocoder.geocode( { 'address': address}, function(results, status) {
            if (status == google.maps.GeocoderStatus.OK) {
                if (status != google.maps.GeocoderStatus.ZERO_RESULTS) {
                    map.setCenter(results[0].geometry.location);
                    var marker = new google.maps.Marker({
                        map: map,
                        position: results[0].geometry.location
                    });
                } else {
                    // GLog does not work, weird
                    //GLog.write("No results found");
                }
            } else {
                //GLog.write("Geocode was not successful for the following reason: " + status);
            }
        });
    }
  }
}

// get the location for given address and initialize a map to that position
function initializeLocationAddress(address) {
    geocoder = new google.maps.Geocoder();
    geocoder.geocode( { 'address': address}, function(results, status) {
        if (status == google.maps.GeocoderStatus.OK) {
            var newLocation = results[0].geometry.location;
            if(newLocation != null) {
                var mapPosition = newLocation;
                var myOptions = {
                		
                	   zoom: 10,
            	       center: mapPosition,
            	       mapTypeId: google.maps.MapTypeId.ROADMAP
            	     };
            	     map = new google.maps.Map(document.getElementById("map_canvas"), myOptions);
            	     marker = new google.maps.Marker({
        	         position: mapPosition, 
        	         map: map
        	     });
        	}
        }
    });
}