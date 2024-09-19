package com.ridango.cocktailGame.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ridango.cocktailGame.exception.CocktailFetchingException;
import com.ridango.cocktailGame.model.Cocktail;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


/**
 * A service class that provides a method for fetching a random cocktail from provided API.
 */
@Service
@Slf4j
public class CocktailService {

    private static final String COCKTAIL_API_URL = "https://www.thecocktaildb.com/api/json/v1/1/random.php";

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Fetches a random cocktail from the CocktailDB API.
     * This method sends a GET request to the provided API endpoint to retrieve a random cocktail.
     * The JSON response is parsed to extract the cocktail details, which are then mapped to a `Cocktail` object.
     *
     * @throws CocktailFetchingException If there was an error while fetching or parsing the cocktail data or no cocktails were found.
     * @throws RuntimeException if cocktailApi response is not with status OK.
     */
    public Cocktail fetchRandomCocktail() {
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(COCKTAIL_API_URL, String.class);
            if (response.getStatusCode() == HttpStatus.OK) {
                JsonNode rootNode = objectMapper.readTree(response.getBody());
                JsonNode drinksArray = rootNode.path("drinks");
                if (drinksArray.isArray() && !drinksArray.isEmpty()) {
                    JsonNode cocktailNode = drinksArray.get(0);
                    Cocktail cocktail = objectMapper.treeToValue(cocktailNode, Cocktail.class);
                    log.info("Successfully fetched the cocktail");
                    log.info("Successfully fetched the cocktail {}", cocktail);
                    return cocktail;
                } else {
                    throw new CocktailFetchingException("No cocktails found in the response.");
                }
            } else {
                String errorMessage = "API request failed with status code: " + response.getStatusCodeValue();
                log.error(errorMessage);
                throw new CocktailFetchingException(errorMessage);
            }
        } catch (JsonProcessingException e) {
            log.error("Unexpected error occurred while fetching cocktail", e);
            throw new CocktailFetchingException("Error parsing cocktail", e);
        }
    }
}
