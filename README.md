# En-route Eatery
We have made an app for Dalhousie students who live off campus. Since they must commute to and from the university on daily basis. They can use this app to find en-route eatery or take away food from the eating place. When the user has entered their desired location, it will only show the details of eatery places along with the convenience stores which is on the way for the users. Further, it will show the duration and distance of the destination and it will guide them with minimal distance route. This app supports walk or cycling mode as a mode of travel. It also shows recent offers provided by the vendors along with their reviews. This app has a unique login system for the users and it also stores user’s preferences for suggesting them the restaurants of their choices. These suggestions also show the places which accept DAL card as a mode of payment. If the app is not being used it will also give reminders via notifications when someone passes by some restaurants.




## Libraries

**google-gson:** Gson is a Java library that can be used to convert Java Objects into their JSON representation. It can also be used to convert a JSON string to an equivalent Java object. Source [here](https://github.com/google/gson)

**Zomato API:** Zomato APIs provides detailed information of over 1.5 million restaurants across 10,000 cities globally. Source [here](https://developers.zomato.com/api)

**com.github.ittianyu:BottomNavigationViewEx:** The bottom navigation view that Android Studio provide has defult animation. T cancel the defult animation. Source [here](https://www.youtube.com/watch?v=QXy_kkQM2Kc&list=PLgCYzUzKIBE9XqkckEJJA0I1wVKbUAOdv&index=3)

**de.hdodenhof:circleimageview:** This package used to cut an image into circle to present the head portrait. Source [here](https://github.com/hdodenhof/CircleImageView)

**com.hyman:flowlayout-lib:** A layout package to provide flow layout to configure preference labels. Source [here](https://github.com/hongyangAndroid/FlowLayout)

**com.android.volley:** A library which is used for handling HTTP request. it's speed is faster and and it makes networking part easy for the developer [here](https://github.com/google/volley)

**org.apache.httpcomponents**

**com.google.android.gms:**

**firebase:**


## Installation Notes
The app is complied on Android 8.0. Beforing running the application, the SDK version need to be set as following if not using default app gradle - 
Android Studio --> File --> Project structure --> app --> Compile Sdk Version --> API 26: Android 8.0(Oreo) --> OK


## Code Examples
During this project we have came across several challenges which are mentioned as follows:

**Problem 1: We need to retrieve data from Firebase realtime database**

In order to show the special offer information of the restaurants, we need to store and retrieve data from Firebase Database. The challenge for this problem is that the retrieving operations with Firebase Database are synchronous. Thus, the list view always shows nothing because the data has not been retrieved when the adapter works. In this case, we studied many solutions and finally used Callbacks to address this problem.
```
// The method to retrieve restaurant information from Firebase DB and use Callback interface to send it to the adapter
public void retrieveRestaurant(final RestaurantCallbacks restaurantCallbacks) {

	db.addChildEventListener(new ChildEventListener() {
	    @Override
	    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
		for (DataSnapshot ds : dataSnapshot.getChildren()) {
		    Restaurant restaurant = ds.getValue(Restaurant.class);
		    if (restaurant != null && restaurant.getRid() != null) {
			restaurants.add(restaurant);
		    }
		}
		restaurantCallbacks.onRestaurantCallback(restaurants);
	    }
	    @Override
	    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
		for (DataSnapshot ds : dataSnapshot.getChildren()) {
		    Restaurant restaurant = ds.getValue(Restaurant.class);
		    if (restaurant != null && restaurant.getRid() != null) {
			restaurants.add(restaurant);
		    }
		}
		restaurantCallbacks.onRestaurantCallback(restaurants);
	    }
	    @Override
	    public void onChildRemoved(DataSnapshot dataSnapshot) {
	    }
	    @Override
	    public void onChildMoved(DataSnapshot dataSnapshot, String s) {
	    }
	    @Override
	    public void onCancelled(DatabaseError databaseError) {
	    }
	});
}
```
```
//This interface callbacks a Restaurant instance from searching restaurant in Firebase
public interface RestaurantCallbacks {
void onRestaurantCallback(ArrayList<Restaurant> restaurants);
}
```
```
//retrieveRestaurant() method used in Activity
firebasehelper.retrieveRestaurant(new RestaurantCallbacks() {
    @Override
    public void onRestaurantCallback(ArrayList<Restaurant> restaurants) {
	adapter = new SpecialOffersAdapter(getApplicationContext(), restaurants);
	lv_offers.setAdapter(adapter);
    }
});
```

**Problem 2: A reusable function which is for building direction response along with alternate route and 2 mode of travel (Walk & Cycle)**

For drawing a path between origin and destination we have to build a URL which can be used further for API calling. The main goal was to design a function in such a way that it can achieve many tasks at once such as including mode of transportation and alternate route in requested URL.

```
/**
* @param origin: take latlang of origin which will be user's live or current location,
* @param dest: take user's converted latlang from the entered string
* @param travel_mode: this is for selecting travel mode: walk or cycle
* @param alternate_route: this is for drawing alternate route or draw minimal route
* @return: which gives the whole url
*/
private String getUrl(LatLng origin, LatLng dest, String travel_mode, String alternate_route) {
    String mode = "mode=" + travel_mode + "&";
    String alternatives = "&alternatives=" + alternate_route ;
    String str_origin = "origin=" + origin.latitude + "," + origin.longitude;           // Origin of route
    String str_dest = "destination=" + dest.latitude + "," + dest.longitude;            // Destination of route
    String sensor = "sensor=false";                                                     // Sensor enabled
    String parameters = mode + str_origin + "&" + str_dest + "&" + sensor + alternatives;                     // Building the parameters to the web service
    String output = "json";                                                             // Output format

    // Building the url to the web service
    String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;
    return url;
 }

```

**Problem 3: A reusable function which gets the mode of travel and draw the route and put origin and destination marker**

```
/**
* @param travel_mode: either "walk" or "Cycle" mode
* @param alternate_mode: true for having alternate route and false for minimal distance route
*/

public void conditionFunction(String travel_mode, String alternate_mode){
      //mMap.clear(); // clearing the map first
      MarkerOptions options = new MarkerOptions();

      // Fixed Origin and Destination for test run
      LatLng origin = new LatLng(44.638061, -63.591360);
      LatLng dest = new LatLng(44.651775, -63.592513);

      /* For free search - uncomment it & put hardcoded lat lng in comment section
      LatLng origin = currentLoc;
      LatLng dest = new LatLng(dst_lat ,dst_lng);
      */

      // Getting URL to the Google Directions API
      String url = getUrl(origin, dest, travel_mode, alternate_mode);
      Log.d("onMapClick", url.toString());
      FetchUrl FetchUrl = new FetchUrl();

      // Start downloading json data from Google Directions API
      FetchUrl.execute(url);

      // Setting the position of the marker
      options.position(origin);
      options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
      options.title("Origin");
      mMap.addMarker(options);

      options.position(dest);
      options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
      options.title("Destination");
      mMap.addMarker(options);

      //move map camera
      mMap.moveCamera(CameraUpdateFactory.newLatLng(dest));
      mMap.animateCamera(CameraUpdateFactory.zoomTo(15)); //search path zoom
}

```

**Problem 4: A reusable class and/or method to find restaurants and/or convenience store while drawing route on Google Map **

Google do provide restaurants and convenience store nearby to specific lat/lang, however we need to show them along with route. To overcome this problem, we collected all lat/lang while route is drawn on map and requested for any nearby eatery and/or store.  

```
/*  Snippet code from MapsActivity.java */

LatLng position = new LatLng(lat, lng);
points.add(position);
//Start showing restaurants
String restUrl = getRestaurantUrl(position);
FetchRestUrl FetchRestUrl = new FetchRestUrl();
// Start downloading json data from Google search nearby API
FetchRestUrl.execute(restUrl);

/* New JSON parsing class for restaurants and convenience store */

RestDataParser.java

```


## Feature Section
The application mainly used four features:
- Network
- Geo-location
- Storage (Local & Cloud)

## Final Project Status
We have achieved most of our functionalities, the status for the same as mentioned below. We have also added future task for this project as an add-ons.

#### Minimum Functionality
- Show current location of user with search option to destination (Completed)
- Shows the routes on the map by walking or cycling as a mode of travel (Completed)

#### Expected Functionality
- Pin up the details of many restaurants on the map (Completed)
- Gives also nearby convenience store’s location (Completed)
- Creating a user’s profile and saving their preferences (Completed)
- Gives suggestion to users based on their preferences. (Completed)

#### Bonus Functionality
- Expected time and possible routes to reaching the desired restaurant (Partially Completed)
- Expected time and possible routes to reaching the desired convenience store (Partially Completed)
- Providing the notifications which state about offers even a user is not using the application (Completed)
- Provides route suggestion in case of no nearby restaurants found (Not Implemented)
- It provides recent offers of numerous restaurants which also reveals various payment accepting offers including DAL card (Completed)
#### Next step for this project (if it were to continue)
- Add pictures for the suggested restaurants
- Polishing UI

## Sources
[1] "Getting Bitmap or Drawable based on the url of pictures", Blog.csdn.net, 2017. [Online]. Available: [https://blog.csdn.net/sinat_21376777/article/details/75157912](https://blog.csdn.net/sinat_21376777/article/details/75157912). [Accessed: 01- Apr- 2018]

[2] "Lab 6", Dal.brightspace.com, 2018. [Online]. Available: [https://dal.brightspace.com/d2l/le/content/68186/viewContent/939768/View](https://dal.brightspace.com/d2l/le/content/68186/viewContent/939768/View). [Accessed: 01- Apr- 2018]

[3] "Build an Instagram Clone", Youtube.com, 2017. [Online]. Available:
[https://www.youtube.com/watch?v=qpJRgr6HzAw&list=PLgCYzUzKIBE9XqkckEJJA0I1wVKbUAOdv&index=1](https://www.youtube.com/watch?v=qpJRgr6HzAw&list=PLgCYzUzKIBE9XqkckEJJA0I1wVKbUAOdv&index=1). [Accessed: 30- Mar- 2018]

[4] "What to eat", Osun.com, 2017. [Online]. Avialable:
[https://www.osun.life/2017/07/22/where-to-eat/](https://www.osun.life/2017/07/22/where-to-eat/). [Accessed: 02- Apr- 2018]

[5] "Eatery", Urbaneatery.com, 2017. [Online]. Available:
[http://www.urbaneatery.co.ke/](http://www.urbaneatery.co.ke/). [Accessed: 02- Apr- 2018]

[6] "How to Upload Images to Firebase from an Android App", code.tutsplus.com, 2017. [Online]. Available:
[https://code.tutsplus.com/tutorials/image-upload-to-firebase-in-android-application--cms-29934](https://code.tutsplus.com/tutorials/image-upload-to-firebase-in-android-application--cms-29934). [Accessed: 02- Apr- 2018]

[7] "the functionality of head image chosen by Android development" blog.csdn.net, 2017. [Online]. Available:
[https://blog.csdn.net/qq_31546677/article/details/75667163](https://blog.csdn.net/qq_31546677/article/details/75667163). [Accessed: 26- Mar- 2018]

[8] "Google Maps Draw Route between two points using Google Directions" androidtutorialpoint.com, 2017. [Online]. Available:
[https://www.androidtutorialpoint.com/intermediate/google-maps-draw-path-two-points-using-google-directions-google-map-android-api-v2/](https://www.androidtutorialpoint.com/intermediate/google-maps-draw-path-two-points-using-google-directions-google-map-android-api-v2/). [Accessed: 03- Mar- 2018]

[9] "How to draw road directions between two geocodes in android google map v2?
" stackoverflow.com, 2017. [Online]. Available:
[https://stackoverflow.com/questions/14710744/how-to-draw-road-directions-between-two-geocodes-in-android-google-map-v2?utm_medium=organic&utm_source=google_rich_qa&utm_campaign=google_rich_qa](https://stackoverflow.com/questions/14710744/how-to-draw-road-directions-between-two-geocodes-in-android-google-map-v2?utm_medium=organic&utm_source=google_rich_qa&utm_campaign=google_rich_qa). [Accessed: 03- Mar- 2018]

[10] "Android How to show route between markers on googlemaps" stackoverflow.com, 2017. [Online]. Available:
[https://stackoverflow.com/questions/28295199/android-how-to-show-route-between-markers-on-googlemaps?utm_medium=organic&utm_source=google_rich_qa&utm_campaign=google_rich_qa](https://stackoverflow.com/questions/28295199/android-how-to-show-route-between-markers-on-googlemaps?utm_medium=organic&utm_source=google_rich_qa&utm_campaign=google_rich_qa). [Accessed: 03- Mar- 2018]

[11] "Parsing JSON from the google maps DistanceMatrix api in android
" stackoverflow.com, 2017. [Online]. Available:
[https://stackoverflow.com/questions/29439754/parsing-json-from-the-google-maps-distancematrix-api-in-android?utm_medium=organic&utm_source=google_rich_qa&utm_campaign=google_rich_qa](https://stackoverflow.com/questions/29439754/parsing-json-from-the-google-maps-distancematrix-api-in-android?utm_medium=organic&utm_source=google_rich_qa&utm_campaign=google_rich_qa). [Accessed: 04- Mar- 2018]

[12] "Draw route between two locations, Google Maps in Android" mytredin.com, 2017. [Online]. Available:
[https://www.mytrendin.com/draw-route-two-locations-google-&maps-android/](https://www.mytrendin.com/draw-route-two-locations-google-&maps-android/). [Accessed: 04- Mar- 2018]

[13] "Customizing InfoWindow Contents in Google Map Android API V2 using InfoWindowAdapter" wptrafficanalyzer.in, 2017. [Online]. Available:
[http://wptrafficanalyzer.in/blog/customizing-infowindow-contents-in-google-map-android-api-v2-using-infowindowadapter/](http://wptrafficanalyzer.in/blog/customizing-infowindow-contents-in-google-map-android-api-v2-using-infowindowadapter/). [Accessed: 05- Apr- 2018]




