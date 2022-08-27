# Powr Armor

## Introduction
Powr Armor uses the [Monster Hunter World API](https://docs.mhw-db.com/#introduction) to fetch a list of armor pieces. And the user can search an item among the same in
a very slick UI.

## UI

There are three different states of the UI as shown below:
|Loading State                       |Error State                             |Success State                  |
|------------------------------------|----------------------------------------|-------------------------------|
|![image](https://user-images.githubusercontent.com/19891009/187033388-4af58be6-971b-49ff-bb95-0696a3cb3079.png) | ![image](https://user-images.githubusercontent.com/19891009/187033409-e896ace1-80a9-46a2-b4a4-c642e1462895.png) | ![image](https://user-images.githubusercontent.com/19891009/187033441-0e94979b-4379-40b4-acd1-6e534bf10f65.png) |

## Features
The app presents quite a good amount of features.
- A smooth user experience and a simplistic user interface. 
- The app also supports a variety of devices with respect to size and 
is responsive to any orientation.
- Network efficient. The app does not call the API each time the device is rotated
or resumed from the background state. The data is persistent across the lifetime of the 
app.

## Technical Specifications

### Architecture
1. The app is built using MVVM(Model View- ViewModel) Architecture which is widely used within the industry due to it's following advantages:
    - MVVM facilitates easier parallel development of a UI and the building blocks that power it.
    - MVVM abstracts the View and thus reduces the quantity of business logic (or glue) required in the code behind it.
    - The ViewModel can be easier to unit test than in the case of event-driven code.
2. The app also uses coroutines for async processes like API calls. A coroutine is a concurrency design pattern that you can use on Android
to simplify code that executes asynchronously. These are lightweight and lead to fewer memory leak since each coroutine runs in its own scope.
3. The application uses material UI components.
4. Network calls are handled via a custom Interceptor which allows to catch an exception that can be thrown while sending a request due to
any connection error.

### Libraries used
1. [Okhttp](https://square.github.io/okhttp/)
2. [Retrofit2](https://square.github.io/retrofit/)
3. [Material UI](https://material.io/)
4. [Glide](https://github.com/bumptech/glide)
5. [Gson](https://github.com/google/gson)
6. [SwipeRefreshLayout](https://developer.android.com/jetpack/androidx/releases/swiperefreshlayout)
7. [Kotlin Coroutines](https://developer.android.com/kotlin/coroutines)
8. [Lifecycle extensions](https://developer.android.com/jetpack/androidx/releases/lifecycle)

