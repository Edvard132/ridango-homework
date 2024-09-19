package com.ridango.cocktailGame;

import com.ridango.cocktailGame.cocktailGame.CocktailGame;
import com.ridango.cocktailGame.cocktailGame.GameState;
import com.ridango.cocktailGame.model.Cocktail;
import com.ridango.cocktailGame.model.GameResponse;
import com.ridango.cocktailGame.service.CocktailService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CocktailGameTest {

    @Mock
    private CocktailService cocktailService;

    @Spy
    private GameState gameState;

    @InjectMocks
    private CocktailGame cocktailGame;

    private Cocktail cocktail;

    @BeforeEach
    void setup(){
        cocktail = new Cocktail(
                2,
                "Apple Cocktail",
                "Cocktail",
                "Cocktail glass",
                "Mix coffee and rum",
                "imageUrl",
                "Coffee",
                "Rum",
                "Water",
                "Ice",
                "Sugar"
        );
    }

    /**
     * New game started with score 0. Cocktail details must be present. Game state must be up-to-date.
     */
    @Test
    public void startGameWithScore0() {
        Mockito.when(cocktailService.fetchRandomCocktail()).thenReturn(cocktail);
        GameResponse gameResponse = cocktailGame.startGame(false);

        Assertions.assertEquals(5, gameResponse.getAttemptsLeft());
        Assertions.assertEquals(0, gameResponse.getScore());
        Assertions.assertEquals("_____ ________", gameResponse.getHiddenName());
        Assertions.assertEquals("Make a guess!", gameResponse.getResult());
        Assertions.assertEquals("Mix coffee and rum", gameResponse.getInstructions());
        Assertions.assertNull(gameResponse.getHints());
        Assertions.assertEquals(cocktail, gameState.getCurrentCocktail());
    }

    /**
     * Correct guess was made in previous game on 3rd attempt. Score must be increased, attempts should be reset.
     */
    @Test
    public void startGameWithScore3() {
        gameState.setScore(3);
        Mockito.when(cocktailService.fetchRandomCocktail()).thenReturn(cocktail);
        GameResponse gameResponse = cocktailGame.startGame(true);

        Assertions.assertEquals(5, gameResponse.getAttemptsLeft());
        Assertions.assertEquals(3, gameResponse.getScore());
    }

    /**
     * Correct guess was made on 1st attempt with initial score 0, new cocktail is displayed.
     * Score should be increased by 5. New cocktail details must be present and game state updated.
     */
    @Test
    public void playRoundSuccessfulGuessOnFirstTry() {
        String guess = "Apple cocktail";

        Cocktail nextCocktail = new Cocktail(
                3,
                "Strawberry Cocktail",
                "Cocktail",
                "Cocktail glass",
                "Mix coffee and rum",
                "imageUrl",
                "Coffee",
                "Rum",
                "Water",
                "Ice",
                "Sugar"
        );

        gameState.setCurrentCocktail(cocktail);

        Mockito.when(cocktailService.fetchRandomCocktail()).thenReturn(nextCocktail);
        GameResponse gameResponse = cocktailGame.playRound(guess);

        Assertions.assertEquals(5, gameResponse.getAttemptsLeft());
        Assertions.assertEquals(5, gameResponse.getScore());
        Assertions.assertEquals("__________ ________", gameResponse.getHiddenName());
        Assertions.assertEquals("Make a guess!", gameResponse.getResult());
        Assertions.assertEquals("Mix coffee and rum", gameResponse.getInstructions());
        Assertions.assertNull(gameResponse.getHints());
        Assertions.assertEquals(nextCocktail, gameState.getCurrentCocktail());
    }


    /**
     * Initial score 5.
     * Attempts must be decremented after unsuccessful guess.
     * At least 1 letter must be revealed in case of cocktail name with length >= 5.
     * 1 hint must be shown, result and instructions must be present.
     */
    @Test
    public void playRoundUnsuccessfulGuessWithScore0And5AttemptsLeft() {
        String guess = "Apple ";

        gameState.setCurrentCocktail(cocktail);
        GameResponse gameResponse = cocktailGame.playRound(guess);

        Assertions.assertEquals(4, gameResponse.getAttemptsLeft());
        Assertions.assertEquals(0, gameResponse.getScore());
        Assertions.assertTrue(gameResponse.getHiddenName().matches(".*[a-zA-Z0-9].*"));
        Assertions.assertEquals("Try again!", gameResponse.getResult());
        Assertions.assertEquals("Mix coffee and rum", gameResponse.getInstructions());
        Assertions.assertEquals(1, gameResponse.getHints().size());
    }

    /**
     * Incorrect guess was made on 5th attempt with initial score 0.
     * Cocktail name must be revealed.
     * 4 hints must be shown, result and instructions must be present.
     */
    @Test
    public void playRoundUnsuccessfulGuessWithScore0And1AttemptsLeft() {
        String guess = "Apple ";

        gameState.setAttemptsLeft(1);
        gameState.setCurrentCocktail(cocktail);
        GameResponse gameResponse = cocktailGame.playRound(guess);

        Assertions.assertEquals(0, gameResponse.getAttemptsLeft());
        Assertions.assertEquals(0, gameResponse.getScore());
        Assertions.assertEquals("Apple Cocktail", gameResponse.getHiddenName());
        Assertions.assertEquals("Game over - out of attempts!", gameResponse.getResult());
        Assertions.assertEquals(4, gameResponse.getHints().size());
    }

}
