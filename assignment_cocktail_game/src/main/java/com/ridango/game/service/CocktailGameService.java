package com.ridango.game.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ridango.game.exception.CocktailFetchingException;
import com.ridango.game.exception.NoCocktailFoundException;
import com.ridango.game.model.Cocktail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;


/**
 * A service class that provides a method for fetching a random cocktail from provided API.
 */
@Service
public class CocktailGameService {

    private static final String COCKTAIL_API_URL = "https://www.thecocktaildb.com/api/json/v1/1/random.php";

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Fetches a random cocktail from the CocktailDB API.
     *
     * <p>This method sends a GET request to the provided API endpoint to retrieve a random cocktail.
     * The JSON response is parsed to extract the cocktail details, which are then mapped to a `Cocktail` object.
     *
     * @return A `Cocktail` object containing the details of the fetched cocktail, or `null` if there was an error or no cocktail data was found.
     * @throws CocktailFetchingException If there was an error while fetching or parsing the cocktail data.
     * @throws RuntimeException If there was a general error during the request, such as an invalid HTTP status code or a client-side error.
     */
    public Cocktail fetchRandomCocktail() {
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(COCKTAIL_API_URL, String.class);
            if (response.getStatusCode() == HttpStatus.OK) {
                JsonNode rootNode = objectMapper.readTree(response.getBody());
                JsonNode drinksArray = rootNode.path("drinks");
                if (drinksArray.isArray() && !drinksArray.isEmpty()) {
                    JsonNode cocktailNode = drinksArray.get(0);
                    return objectMapper.treeToValue(cocktailNode, Cocktail.class);
                } else {
                    throw new NoCocktailFoundException("No cocktails found in the response");
                }
            } else {
                throw new RuntimeException("API request failed with status code: " + response.getStatusCodeValue());
            }
        } catch (JsonProcessingException e) {
            throw new CocktailFetchingException("Error parsing JSON response", e);
        } catch (HttpClientErrorException e) {
            throw new RuntimeException("API request failed with client error: " + e.getMessage());
        }
    }

}
