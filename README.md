# En-route Eatery
One paragraph to describe your project. Your description should include the project concept and key features implemented.

## Libraries

**google-gson:** Gson is a Java library that can be used to convert Java Objects into their JSON representation. It can also be used to convert a JSON string to an equivalent Java object. Source [here](https://github.com/google/gson)

**Zomato API:** Zomato APIs provides detailed information of over 1.5 million restaurants across 10,000 cities globally. Source [here](https://developers.zomato.com/api)  

**com.github.ittianyu:BottomNavigationViewEx:** The bottom navigation view that Android Studio provide has defult animation. T cancel the defult animation. Source [here](https://www.youtube.com/watch?v=QXy_kkQM2Kc&list=PLgCYzUzKIBE9XqkckEJJA0I1wVKbUAOdv&index=3)

**de.hdodenhof:circleimageview:** This package used to cut an image into circle to present the head portrait. Source [here](https://github.com/hdodenhof/CircleImageView)  

**com.hyman:flowlayout-lib:** A layout package to provide flow layout to configurate preference labels. Source [here](https://github.com/hongyangAndroid/FlowLayout)

**com.android.volley**

**org.apache.httpcomponents**

**com.google.android.gms**

**firebase:**


## Installation Notes
The app is based on Android 8.0. Beforing running it, the SDK version need to be set as following:
Android Studio --> File --> Project structure --> app --> Compile Sdk Version --> API 26: Android 8.0(Oreo) --> OK
If it does not work for the first time, please try more times following the same operations.

## Code Examples
You will encounter roadblocks and problems while developing your project. Share 2-3 'problems' that your team solved while developing your project. Write a few sentences that describe your solution and provide a code snippet/block that shows your solution. Example:

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

## Feature Section
The application mainly used four features:
- Network
- Geo-location 
- Storage (Local & Cloud)  

## Final Project Status
Write a description of the final status of the project. Did you achieve all your goals? What would be the next step for this project (if it were to continue)?

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
[1]"Getting Bitmap or Drawable based on the url of pictures", Blog.csdn.net, 2017. [Online]. Available: https://blog.csdn.net/sinat_21376777/article/details/75157912. [Accessed: 01- Apr- 2018]  
[2]"Lab 6", Dal.brightspace.com, 2018. [Online]. Available: https://dal.brightspace.com/d2l/le/content/68186/viewContent/939768/View. [Accessed: 01- Apr- 2018]    
[3]"Build an Instagram Clone", Youtube.com, 2017. [Online]. Available:  
https://www.youtube.com/watch?v=qpJRgr6HzAw&list=PLgCYzUzKIBE9XqkckEJJA0I1wVKbUAOdv&index=1. [Accessed: 30- Mar- 2018]  
[4]"What to eat", Osun.com, 2017. [Online]. Avialable:  
https://www.osun.life/2017/07/22/where-to-eat/. [Accessed: 02- Apr- 2018]  
[5]"Eatery", Urbaneatery.com, 2017. [Online]. Available:  
http://www.urbaneatery.co.ke/. [Accessed: 02- Apr- 2018]  
[6]"How to Upload Images to Firebase from an Android App", code.tutsplus.com, 2017. [Online]. Available: 
https://code.tutsplus.com/tutorials/image-upload-to-firebase-in-android-application--cms-29934. [Accessed: 02- Apr- 2018]  
[7]"the functionality of head image chosen by Android development" blog.csdn.net, 2017. [Online]. Available: 
https://blog.csdn.net/qq_31546677/article/details/75667163. [Accessed: 26- Mar- 2018]. 
