# En-route Eatery
One paragraph to describe your project. Your description should include the project concept and key features implemented.

## Libraries
Provide a list of **ALL** the libraries you used for your project. Example:

**google-gson:** Gson is a Java library that can be used to convert Java Objects into their JSON representation. It can also be used to convert a JSON string to an equivalent Java object. Source [here](https://github.com/google/gson)

## Installation Notes
Installation instructions for markers.

## Code Examples
You will encounter roadblocks and problems while developing your project. Share 2-3 'problems' that your team solved while developing your project. Write a few sentences that describe your solution and provide a code snippet/block that shows your solution. Example:

**Problem 1: We needed a method to calculate a Fibonacci sequence**

A short description.
```
// The method we implemented that solved our problem
public static int fibonacci(int fibIndex) {
    if (memoized.containsKey(fibIndex)) {
        return memoized.get(fibIndex);
    } else {
        int answer = fibonacci(fibIndex - 1) + fibonacci(fibIndex - 2);
        memoized.put(fibIndex, answer);
        return answer;
    }
}

// Source: Wikipedia Java [1]
```

## Feature Section
List all the main features of your application with a brief description of each feature.
Network
Geo-location 
Storage

## Final Project Status
Write a description of the final status of the project. Did you achieve all your goals? What would be the next step for this project (if it were to continue)?

#### Minimum Functionality
- Show current location of user with search option to destination (Completed)
- Shows the routes on the map by walking or cycling as a mode of travel (Completed)

#### Expected Functionality
- Pin up the details of many restaurants on the map (Completed)
- Gives also nearby convenience store’s location	(Completed)
- Creating a user’s profile and saving their preferences (Not Implemented)
- Gives suggestion to users based on their preferences. (Not Implemented)

#### Bonus Functionality
- Expected time and possible routes to reaching the desired restaurant (Not Implemented)
- Expected time and possible routes to reaching the desired convenience store (Not Implemented)
- Providing the notifications which state about offers even a user is not using the application (Not Implemented)
- Provides route suggestion in case of no nearby restaurants found (Not Implemented)
- It provides recent offers of numerous restaurants which also reveals various payment accepting offers including DAL card (Not Implemented)


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

[1] "Java (programming language)", En.wikipedia.org, 2018. [Online]. Available: https://en.wikipedia.org/wiki/Java_(programming_language).