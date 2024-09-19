package com.ridango.cocktailGame.cocktailGame;

import com.ridango.cocktailGame.model.Cocktail;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Holds the game state.
 */
@Getter
@Setter
@Component
public class GameState {
    private Integer score = 0;
    private Integer attemptsLeft = 5;
    private Cocktail currentCocktail;
    private String hiddenName;
    private int hiddenCharactersCount;
    private List<Integer> unrevealedIndices = new ArrayList<>();
    private Set<Integer> seenCocktails = new HashSet<>();
    private List<String> hints = new ArrayList<>();

    public void setCurrentCocktail(Cocktail cocktail) {
        this.currentCocktail = cocktail;
        seenCocktails.add(cocktail.getIdDrink());
        hideCocktailNameAndSetUnrevealedIndices(cocktail.getName());
        this.hints = prepareCocktailHints(cocktail);
    }

    private void hideCocktailNameAndSetUnrevealedIndices(String cocktailName) {
        StringBuilder hiddenNameBuilder = new StringBuilder();

        for (int i = 0; i < cocktailName.length(); i++) {
            char ch = cocktailName.charAt(i);
            if (Character.isLetterOrDigit(ch)) {
                hiddenNameBuilder.append('_');
                unrevealedIndices.add(i);
            } else {
                hiddenNameBuilder.append(ch);
            }
        }
        Collections.shuffle(unrevealedIndices);
        this.hiddenCharactersCount = hiddenNameBuilder.length();
        this.hiddenName = hiddenNameBuilder.toString();
    }

    public void incrementScore(int value) {
        this.score += value;
    }

    public void decrementAttempts() {
        this.attemptsLeft--;
    }

    private List<String> prepareCocktailHints(Cocktail cocktail) {
        List<String> details = new ArrayList<>();
        details.add(cocktail.getCategory());
        details.add(cocktail.getGlass());
        details.add(cocktail.getFormattedIngredients());
        details.add(cocktail.getImageUrl());
        return details;
    }

    public void resetGame() {
        this.attemptsLeft = 5;
        this.score = 0;
        this.seenCocktails.clear();
        this.unrevealedIndices.clear();
        this.hints.clear();
        this.currentCocktail = null;
        this.hiddenName = "";
    }

}
