package com.ridango.game.cocktailGame;

import com.ridango.game.model.Cocktail;
import com.ridango.game.service.CocktailGameService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Game logic class.
 */

@Component
@Slf4j
public class CocktailGame {

    private GameState gameState;
    private int score = 0;
    private int highScore = 0;
    private int attemptsLeft = 5;
    private final Set<Integer> seenCocktails = new HashSet<>();
    private final CocktailGameService cocktailService;
    private final Scanner scanner = new Scanner(System.in);
    @Autowired
    CocktailGame(CocktailGameService cocktailService){
        this.cocktailService = cocktailService;
    }

    /**
     * Starts the game loop. Fetches random cocktails, manages game state,
     * and processes player guesses until attempts run out or the game is stopped.
     */
    public void startGame() {
        printGameIntro();

        while (true) {
            Cocktail cocktail = cocktailService.fetchRandomCocktail();
            if (seenCocktails.contains(cocktail.getIdDrink())) {
                continue;
            }
            seenCocktails.add(cocktail.getIdDrink());

            playRound(cocktail);

            if (attemptsLeft == 0) {
                System.out.println("Game over! Your score: " + score);
                if (score > highScore) {
                    highScore = score;
                    System.out.println("New high score: " + highScore);
                }
//                resetGame();
                break;
            }
        }
        scanner.close();
        System.exit(0);
    }

    /**
     * Plays a single round of the game with the provided cocktail.
     * Manages player guesses, reveals letters, and provides hints.
     *
     * @param cocktail The cocktail object for the current round.
     */
    private void playRound(Cocktail cocktail) {
        String hiddenName = hideCocktailName(cocktail.getName());
        System.out.println("Cocktail instructions: " + cocktail.getInstructions());
        System.out.println();
        System.out.println("Guess the cocktail: " + hiddenName);

        List<String> details = prepareCocktailDetails(cocktail);

        while (attemptsLeft > 0) {
            System.out.print("Your guess: ");
            System.out.println();
            String guess = scanner.nextLine();
            if (guess.equalsIgnoreCase(cocktail.getName())) {
                System.out.println("Correct! The cocktail is: " + cocktail.getName());
                score += attemptsLeft;
                System.out.println("The score is " + score);
                System.out.println();
                attemptsLeft = 5;
                break;
            } else {
                attemptsLeft--;
                if (attemptsLeft > 0) {
                    hiddenName = revealLetters(hiddenName, cocktail.getName());
                    System.out.println("Wrong! Try again. Cocktail: " + hiddenName);
                    System.out.println("Attempts left: " + attemptsLeft);
                    revealRandomCocktailDetail(details);
                } else {
                    System.out.println("Out of attempts! The cocktail was: " + cocktail.getName());
                    break;
                }
            }
        }
    }

    /**
     * Hides all letters in the cocktail name with underscores.
     *
     * @param cocktailName The name of the cocktail.
     * @return The cocktail name with letters replaced by underscores.
     */
    private String hideCocktailName(String cocktailName) {
        return cocktailName.replaceAll("[a-zA-Z]", "_");
    }

    /**
     * Reveals a certain number of letters in the hidden cocktail name based on the remaining attempts.
     *
     * @param hiddenName The current state of the hidden cocktail name.
     * @param originalName The original cocktail name.
     * @return The updated hidden name with some letters revealed.
     */
    private String revealLetters(String hiddenName, String originalName) {
        char[] hiddenArray = hiddenName.toCharArray();
        char[] originalArray = originalName.toCharArray();

        List<Integer> indices = new ArrayList<>();

        for (int i = 0; i < hiddenArray.length; i++) {
            if (hiddenArray[i] == '_') {
                indices.add(i);
            }
        }
        Collections.shuffle(indices);

        int lettersToRevealNow = getLettersToRevealNow(originalName);

        for (int i = 0; i < Math.min(lettersToRevealNow, indices.size()); i++) {
            hiddenArray[indices.get(i)] = originalArray[indices.get(i)];
        }

        return new String(hiddenArray);
    }

    /**
     * Determines the number of letters to reveal based on the length of the cocktail name and attempts left.
     *
     * @param originalName The original cocktail name.
     * @return The number of letters to reveal.
     */
    private int getLettersToRevealNow(String originalName) {
        int totalLetters = (int) originalName.chars()
                .filter(Character::isLetterOrDigit)
                .count();

        if (attemptsLeft > totalLetters){
            return 0;
        }
        else if (attemptsLeft == totalLetters){
            return 1;
        } else {
            return Math.max(1, totalLetters / 5);
        }
    }

    /**
     * Prepares a list of details about the cocktail for hint purposes.
     *
     * @param cocktail The cocktail object.
     * @return A list of cocktail details.
     */
    private List<String> prepareCocktailDetails(Cocktail cocktail) {
        List<String> details = new ArrayList<>();
        details.add("Cocktail category is " + cocktail.getCategory());
        details.add("Cocktail glass is " + cocktail.getGlass());
        details.add("Cocktail ingredients are " + cocktail.getFormattedIngredients());

        return details;
    }

    /**
     * Reveals a random hint from the list of cocktail details.
     *
     * @param details The list of cocktail details.
     */
    private void revealRandomCocktailDetail(List<String> details) {
        if (!details.isEmpty()) {
            Random random = new Random();
            int randomIndex = random.nextInt(details.size());
            System.out.println("Hint: " + details.get(randomIndex));
            details.remove(randomIndex);
        }
    }

//    /**
//     * Resets the game state for a new game.
//     */
//    private void resetGame() {
//        score = 0;
//        attemptsLeft = 5;
//        seenCocktails.clear();
//    }

    /**
     * Prints the introductory message and ASCII art for the game.
     */
    private void printGameIntro() {
        System.out.println("========================================================================================================================");
        String asciiArt = """
                      ___           ___           ___           ___                         ___                              \s
                     /\\__\\         /\\  \\         /\\__\\         /|  |                       /\\  \\                             \s
                    /:/  /        /::\\  \\       /:/  /        |:|  |          ___         /::\\  \\       ___                  \s
                   /:/  /        /:/\\:\\  \\     /:/  /         |:|  |         /\\__\\       /:/\\:\\  \\     /\\__\\                 \s
                  /:/  /  ___   /:/  \\:\\  \\   /:/  /  ___   __|:|  |        /:/  /      /:/ /::\\  \\   /:/__/      ___     ___\s
                 /:/__/  /\\__\\ /:/__/ \\:\\__\\ /:/__/  /\\__\\ /\\ |:|__|____   /:/__/      /:/_/:/\\:\\__\\ /::\\  \\     /\\  \\   /\\__\\
                 \\:\\  \\ /:/  / \\:\\  \\ /:/  / \\:\\  \\ /:/  / \\:\\/:::::/__/  /::\\  \\      \\:\\/:/  \\/__/ \\/\\:\\  \\__  \\:\\  \\ /:/  /
                  \\:\\  /:/  /   \\:\\  /:/  /   \\:\\  /:/  /   \\::/~~/~     /:/\\:\\  \\      \\::/__/       ~~\\:\\/\\__\\  \\:\\  /:/  /\s
                   \\:\\/:/  /     \\:\\/:/  /     \\:\\/:/  /     \\:\\~~\\      \\/__\\:\\  \\      \\:\\  \\          \\::/  /   \\:\\/:/  / \s
                    \\::/  /       \\::/  /       \\::/  /       \\:\\__\\          \\:\\__\\      \\:\\__\\         /:/  /     \\::/  /  \s
                     \\/__/         \\/__/         \\/__/         \\/__/           \\/__/       \\/__/         \\/__/       \\/__/
        """;
        System.out.println(asciiArt);
        System.out.println("========================================================================================================================");
        System.out.println("Welcome to the 'Guess the Cocktail' game!");
        System.out.println("Game Rules:");
        System.out.println("1. A random cocktail with its instructions will be shown.");
        System.out.println("2. You'll see a hidden version of the cocktail's name, like this: ______");
        System.out.println("3. Guess the cocktail name, you have 5 attempts!");
        System.out.println("4. If you fail, we reveal letters gradually, and show more details.");
        System.out.println("5. Score points for correct guesses based on attempts left.");
        System.out.println("6. High score is saved between games.");
        System.out.println("====================================================");
        System.out.println("Get ready! The game is starting...");
        System.out.println("====================================================");
    }
}