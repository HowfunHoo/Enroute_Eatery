# En-route Eatery
One paragraph to describe your project. Your description should include the project concept and key features implemented.

## Libraries
Provide a list of **ALL** the libraries you used for your project. Example:

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
Installation instructions for markers.

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
- Camera (Only works for Android 6.0)


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


## Sources
Use IEEE citation style.
What to include in your project sources:
- Stock images
- Design guides
	 https://developer.android.com/reference/android/support/design/widget/BottomNavigationView.html
- Programming tutorials
- Research material
- Android libraries
- Everything listed on the Dalhousie [*Plagiarism and Cheating*](https://www.dal.ca/dept/university_secretariat/academic-integrity/plagiarism-cheating.html)
- Remember AC/DC *Always Cite / Don't Cheat* (see Lecture 0 for more info)

[1]"Getting Bitmap or Drawable based on the url of pictures", Blog.csdn.net, 2017. [Online]. Available: https://blog.csdn.net/sinat_21376777/article/details/75157912. [Accessed: 01- Apr- 2018].  
[2]"Lab 6", Dal.brightspace.com, 2018. [Online]. Available: https://dal.brightspace.com/d2l/le/content/68186/viewContent/939768/View. [Accessed: 01- Apr- 2018].  
