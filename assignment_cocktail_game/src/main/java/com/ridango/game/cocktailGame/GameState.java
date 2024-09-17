package com.ridango.game.cocktailGame;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
@Getter
@Setter
public class GameState {
    private Integer score;
    private Integer highScore;
    private Integer attemptsLeft;
    private Set<Integer> seenCocktails;

    public GameState() {
        this.score = 0;
        this.highScore = 0;
        this.attemptsLeft = 5;
        this.seenCocktails = new HashSet<>();
    }

    public void incrementScore(Integer value) {
        this.score += value;
    }

    public void decrementAttempts() {
        this.attemptsLeft--;
    }
}
