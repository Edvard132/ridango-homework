package com.ridango.cocktailGame.controller;

import com.ridango.cocktailGame.cocktailGame.CocktailGame;
import com.ridango.cocktailGame.exception.CocktailFetchingException;
import com.ridango.cocktailGame.model.Guess;
import com.ridango.cocktailGame.model.GameResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/cocktail")
@CrossOrigin
@Slf4j
public class CocktailController {

    private final CocktailGame cocktailGame;

    @Autowired
    CocktailController(CocktailGame cocktailGame){
        this.cocktailGame = cocktailGame;
    }

    /**
     * A REST endpoint that handles request for starting the game.
     * In case of errors returns the provided error message in the result parameter.
     *
     * @return A ResponseEntity with a GameResponse body containing the details of the game
     */
    @GetMapping("/random")
    public ResponseEntity<GameResponse> startGame(){
        try {
            GameResponse gameResponse = cocktailGame.startGame(false);
            return ResponseEntity.ok(gameResponse);
        } catch (CocktailFetchingException e) {
            GameResponse gameResponse = new GameResponse(null, null, null, null, null, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(gameResponse);
        } catch (Exception e) {
            GameResponse gameResponse = new GameResponse(null, null, null, null, null, "Unexpected error occurred.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(gameResponse);
        }
    }

    /**
     * A REST endpoint that handles request for making guesses throughout the game rounds.
     * In case of errors returns the provided error message in the result parameter.
     *
     * @return A ResponseEntity with a GameResponse body containing the updated details of the game.
     */
    @PostMapping("/guess")
    public ResponseEntity<GameResponse> makeGuess(@RequestBody Guess guess) {
        try {
            GameResponse gameResponse = cocktailGame.playRound(guess.guess());
            return ResponseEntity.ok(gameResponse);
        } catch (Exception e) {
            GameResponse gameResponse = new GameResponse(null, null, null, null, null, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(gameResponse);
        }
    }
}


