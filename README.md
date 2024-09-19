﻿# ridango-homework

## Backend 
### Technologies used
- Java 17
- Spring Boot

### Requirements

- Java 17
- Gradle

- ## Installation

To install and run the service, please follow these steps:

1. Clone the repository.
2. Navigate to `/assignment_cocktail_game`.
3. Run `CocktailGameApplication` class.

The default port is 8080.

## Frontend 
### Technologies used
- Node 20.11.0
- Angular 18.2.4

- ## Installation

To install and run the service, please follow these steps:

1. Navigate to `/assignment_cocktail_game_frontend`.
2. Run `npm install` and `npm start`.

The default port is 4200.

## Game logic 
* The game starts when user opens the frontend application in browser. Hidden cocktail name with no hints, 5 attempts to guess, score 0 and instructions are shown. 
* In case of successful guess, score is increased by the number of attempts left, new cocktail is shown and attempts are reset.
* In case of incorrect guess, attempts are decreased by 1. When 0 attempts are left, the user has an option to start a new game, where new cocktail is shown and both score and attempts are reset.

## Tests
Game logic tests can be found in `assignment_cocktail_game/src/test/java/com/ridango/cocktailGame` folder
