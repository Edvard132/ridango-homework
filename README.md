# ridango-homework

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
* Starting the Game: The game begins when the user opens the frontend application in their browser. The initial state displays the hidden cocktail name (with no hints), 5 attempts, a score of 0, and instructions.

* Successful Guess:
    * The score increases by the number of attempts left.
    * A new cocktail is shown.
    * Attempts are reset to 5.
    * Same cocktail won't appear until user loses. 
* Incorrect Guess:
    * The number of attempts decreases by 1.
    * When attempts reach 0, the user is given the option to start a new game.
* Starting a New Game:
    * A new cocktail is displayed.
    * Score and attempts are reset.

## Tests
Game logic tests can be found in `assignment_cocktail_game/src/test/java/com/ridango/cocktailGame` folder
