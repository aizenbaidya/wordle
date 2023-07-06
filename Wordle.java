import java.awt.Color;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Wordle {
    // Various game constants
    private final int TRIES = 6;
    private final int WORD_LENGTH = 5;
    private final int TILE_SIZE = 64;
    private final int TILE_SPACING = 8;
    private final int SHIFT = 32;
    private final String[] WORDS;
    private final String ANSWER;

    // Variables that get updated throughout the game
    private char[][] guesses;
    private Color[][] tileColors;
    private int currentGuessIndex;
    private int currentLetterIndex;
    private boolean correctWordGuessed;

    public Wordle() {
        WORDS = new String[14855];
        loadWords();
        ANSWER = WORDS[(int) (Math.random() * WORDS.length)];
        guesses = new char[TRIES][WORD_LENGTH];
        tileColors = new Color[TRIES][WORD_LENGTH];
        currentGuessIndex = 0;
        currentLetterIndex = 0;
        correctWordGuessed = false;
    }

    private void loadWords() {
        try {
            Scanner scanner = new Scanner(new File("Words.txt"));
            int index = 0;
            while (scanner.hasNextLine()) {
                WORDS[index] = scanner.nextLine();
                index++;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void update() {
        if (StdDraw.hasNextKeyTyped()) {
            char keyTyped = StdDraw.nextKeyTyped();
            if (Character.isAlphabetic(keyTyped) && currentLetterIndex < WORD_LENGTH) {
                guesses[currentGuessIndex][currentLetterIndex] = keyTyped;
                currentLetterIndex++;
            } else if (keyTyped == KeyEvent.VK_ENTER && currentLetterIndex == WORD_LENGTH) {
                String guess = new String(guesses[currentGuessIndex]);
                if (binarySearch(guess)) {
                    setTileColors(guess);
                    currentGuessIndex++;
                    currentLetterIndex = 0;
                    if (guess.equals(ANSWER)) {
                        correctWordGuessed = true;
                    }
                }
            } else if (keyTyped == KeyEvent.VK_BACK_SPACE && currentLetterIndex > 0) {
                guesses[currentGuessIndex][currentLetterIndex - 1] = Character.MIN_VALUE;
                currentLetterIndex--;
            }
        }
    }

    private boolean binarySearch(String value) {
        int low = 0;
        int high = WORDS.length - 1;
        while (low <= high) {
            int mid = (low + high) / 2;
            if (WORDS[mid].equals(value)) {
                return true;
            } else if (WORDS[mid].compareTo(value) < 0) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }
        return false;
    }

    private void setTileColors(String guess) {
        String charsLeft = ANSWER;
        List<Integer> indexesLeft = new ArrayList<>();
        // Green and gray tiles
        for (int i = 0; i < WORD_LENGTH; i++) {
            char guessChar = guess.charAt(i);
            if (guessChar == ANSWER.charAt(i)) {
                tileColors[currentGuessIndex][i] = StdDraw.GREEN;
                int index = charsLeft.indexOf(guessChar);
                charsLeft = charsLeft.substring(0, index) + charsLeft.substring(index + 1);
            } else if (!ANSWER.contains(guessChar + "")) {
                tileColors[currentGuessIndex][i] = StdDraw.GRAY;
            } else {
                indexesLeft.add(i);
            }
        }
        // Yellow tiles
        for (Integer index : indexesLeft) {
            char guessChar = guess.charAt(index);
            if (charsLeft.contains(guessChar + "")) {
                tileColors[currentGuessIndex][index] = StdDraw.YELLOW;
                int i = charsLeft.indexOf(guessChar);
                charsLeft = charsLeft.substring(0, i) + charsLeft.substring(i + 1);
            } else {
                tileColors[currentGuessIndex][index] = StdDraw.GRAY;
            }
        }
    }

    public void draw() {
        for (int row = 0; row < TRIES; row++) {
            for (int col = 0; col < WORD_LENGTH; col++) {
                int x = col * (TILE_SIZE + TILE_SPACING) + TILE_SIZE / 2 + SHIFT;
                int y = row * (TILE_SIZE + TILE_SPACING) + TILE_SIZE / 2 + SHIFT;
                StdDraw.setPenColor(StdDraw.GRAY);
                StdDraw.rectangle(x, y, TILE_SIZE / 2, TILE_SIZE / 2);
                Color tileColor = tileColors[row][col];
                if (tileColor != null) {
                    StdDraw.setPenColor(tileColor);
                    StdDraw.filledRectangle(x, y, TILE_SIZE / 2, TILE_SIZE / 2);
                }
                StdDraw.setPenColor(StdDraw.WHITE);
                StdDraw.text(x, y, Character.toUpperCase(guesses[row][col]) + "");
            }
        }
    }

    public boolean isGameOver() {
        return correctWordGuessed || currentGuessIndex == TRIES;
    }
}
