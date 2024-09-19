package com.ridango.cocktailGame.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Holds the GameResponse object.
 */

@Getter
@Setter
public class GameResponse {
    private String hiddenName;
    private String instructions;
    private Integer attemptsLeft;
    private Integer score;
    private List<String> hints;
    private String result;

    public GameResponse(String hiddenName,String instructions,
                        Integer attemptsLeft,
                        Integer score, List<String> hints,
                        String result) {

        this.hiddenName = hiddenName;
        this.instructions = instructions;
        this.attemptsLeft = attemptsLeft;
        this.score = score;
        this.hints = hints;
        this.result = result;
    }

}
