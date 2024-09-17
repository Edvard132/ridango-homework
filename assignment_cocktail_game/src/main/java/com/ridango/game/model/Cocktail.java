package com.ridango.game.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class Cocktail {
    @JsonProperty("idDrink")
    private Integer idDrink;

    @JsonProperty("strDrink")
    private String name;

    @JsonProperty("strCategory")
    private String category;

    @JsonProperty("strGlass")
    private String glass;

    @JsonProperty("strInstructions")
    private String instructions;

    @JsonProperty("strIngredient1")
    private String ingredient1;

    @JsonProperty("strIngredient2")
    private String ingredient2;

    @JsonProperty("strIngredient3")
    private String ingredient3;

    @JsonProperty("strIngredient4")
    private String ingredient4;

    @JsonProperty("strIngredient5")
    private String ingredient5;

    public List<String> getIngredients() {
        List<String> ingredients = new ArrayList<>();
        if (ingredient1 != null) ingredients.add(ingredient1);
        if (ingredient2 != null) ingredients.add(ingredient2);
        if (ingredient3 != null) ingredients.add(ingredient3);
        if (ingredient4 != null) ingredients.add(ingredient4);
        if (ingredient5 != null) ingredients.add(ingredient5);

        return ingredients;
    }

    public String getFormattedIngredients() {
        return String.join(", ", getIngredients());
    }
}

