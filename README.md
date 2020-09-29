# Commons

Rewrite of Commons for Android. Under active development but not yet released.

# Buzzwords
Aside from being a useful tool, I am writing this app as a demo reel because I want to write software for a living.

Here are some of the app's technical selling points:

- Dependency injection using Dagger and Hilt.
- Written in Kotlin with coroutines and flow.
- Modular code structure.
- MotionLayout/ConstraintLayout for interactive layouts and overall UI design.
  - Where possible, new screens are now written with Jetpack Compose.
- Retrofit for network calls and extensive use of Room for local data persistence, using the repository pattern.
- Single activity architecture with the Jetpack Navigation Component.
- Social features use Google Sign-In for account creation.
- Unit tests ensure integrity of data structures and transformations.
- Custom views where they are useful: e.g. TimelineView renders the professional history of a Member as a scrollable chart.


# About the project
Several years ago I released an Android app called Commons which aimed to make it easier to keep up-to-date with UK politics. It looked nice and made a lot of data readable and easy to access... but outside of the time I spent developing it, I never actually used it. It was ultimately an encyclopaedia which failed to make its content engaging.

It also suffered from a very basic backend which required a lot of manual intervention to update.

# v2
I still believe the app should exist so this is the all-new remake with the following goals:

- Engagement - give people (myself included) a reason to use the app regularly.
  - Users can discuss and vote on any parliamentary divisions, bills, election results, members, etc.
  - Users will be able to follow and get updates on particular bills as they progress through parliament
  
- Accessability - data needs to be well-structured, readable, and easy to find.

- Maintainability - data updates are now fully automated.

- Transparency - source code is public for server and client(s).

# Server
The backend of the app is written in Django: https://github.com/beatonma/snommoc

It is running on AWS at https://snommoc.org - feel free to contact me if you want to know more about the API or would like access to it for your own project.
