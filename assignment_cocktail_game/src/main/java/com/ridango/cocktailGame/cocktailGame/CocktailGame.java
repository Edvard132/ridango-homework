package com.ridango.cocktailGame.cocktailGame;

import com.ridango.cocktailGame.model.GameResponse;
import com.ridango.cocktailGame.model.Cocktail;
import com.ridango.cocktailGame.service.CocktailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Game logic class.
 */

@Service
public class CocktailGame {

    private final GameState gameState;
    private final CocktailService cocktailService;
    @Autowired
    CocktailGame(CocktailService cocktailService, GameState gameState){
        this.cocktailService = cocktailService;
        this.gameState = gameState;
    }

    /**
     * Game starting method.
     * Checks if the game needs to be reset, in case of successful guess it shouldn't be.
     * Calls the cocktail fetching method. Sets the all the cocktail details.
     *
     * @return GameResponse with hidden name, instructions,
     * attempts left, score and result string.
     */
    public GameResponse startGame(boolean correctGuessMade) {
        if (!correctGuessMade){
            gameState.resetGame();
        }
        Cocktail cocktail;
        do {
            cocktail = cocktailService.fetchRandomCocktail();
        } while (gameState.getSeenCocktails().contains(cocktail.getIdDrink()));

        gameState.setCurrentCocktail(cocktail);

        return createGameResponse(
                gameState.getHiddenName(),
                null,
                "Make a guess!");
    }

    /**
     *  Game round method.
     *  Checks users guess and returns corresponding response.
     *
     * @param guess users guess.
     * @return GameResponse with hidden name, instructions,
     * attempts left, score, hints and result string.
     */
    public GameResponse playRound(String guess) {
        boolean correctGuess = checkGuess(guess);
        if (correctGuess) {
            updateGameStateOnCorrectGuess();

            return startGame(true);
        } else {
            gameState.decrementAttempts();
            if (gameState.getAttemptsLeft() > 0) {
                revealLetters();
                List<String> hints = getAvailableHints();

                return createGameResponse(
                        gameState.getHiddenName(),
                        hints,
                        "Try again!");
            } else {

                return createGameResponse(
                        gameState.getCurrentCocktail().getName(),
                        gameState.getHints(),
                        "Game over - out of attempts!");
            }
        }
    }

    private boolean checkGuess(String guess) {
        return guess.equalsIgnoreCase(gameState.getCurrentCocktail().getName());
    }

    private void updateGameStateOnCorrectGuess() {
        gameState.incrementScore(gameState.getAttemptsLeft());
        gameState.setAttemptsLeft(5);
    }

    private void revealLetters() {
        char[] hiddenArray = gameState.getHiddenName().toCharArray();
        char[] originalArray = gameState.getCurrentCocktail().getName().toCharArray();
        int lettersToRevealNow = getLettersToRevealNow(gameState.getHiddenCharactersCount());

        for (int i = 0; i < lettersToRevealNow; i++) {
            int revealIndex = gameState.getUnrevealedIndices().get(i);
            hiddenArray[revealIndex] = originalArray[revealIndex];
        }

        gameState.getUnrevealedIndices().subList(0, lettersToRevealNow).clear();
        gameState.setHiddenName(new String(hiddenArray));
    }

    private int getLettersToRevealNow(int totalLetters) {
        int attemptsLeft = gameState.getAttemptsLeft() + 1;
        if (attemptsLeft > totalLetters) {
            return 0;
        } if (attemptsLeft == totalLetters) {
            return 1;
        }
        return Math.max(1, totalLetters / 5);
    }

    private List<String> getAvailableHints() {
        int numberOfHintsToShow = 5 - gameState.getAttemptsLeft();
        numberOfHintsToShow = Math.min(numberOfHintsToShow, gameState.getHints().size());
        return gameState.getHints().subList(0, numberOfHintsToShow);
    }

    private GameResponse createGameResponse(String name, List<String> hints, String result){
        return new GameResponse(
                name,
                gameState.getCurrentCocktail().getInstructions(),
                gameState.getAttemptsLeft(),
                gameState.getScore(),
                hints,
                result);
    }

}