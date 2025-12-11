import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public class ProstoReshenie {

    private static final String DICT_URL =
            "https://raw.githubusercontent.com/nikiiv/JavaCodingTestOne/master/scrabble-words.txt";

    public static void main(String[] args) throws IOException {

        // Load all words from the URL
        List<String> dict = loadAllWords(DICT_URL);

        long start = System.nanoTime();

        // HashSet - > hold unique elements and have O(1) for contains()
        HashSet<String> filteredWords = new HashSet<>();
        HashSet<String> filteredWords9 = new HashSet<>();

        // Filtering words that contain A or I and length<=9
        for (String w : dict) {
            int len = w.length();
            if (len <= 9 && (w.contains("A") || w.contains("I"))) {
                filteredWords.add(w);
                if (len == 9) {
                    filteredWords9.add(w);
                }
            }
        }
        
        // Remove 9-letter words from filteredWords (we only need them for intermediate steps)
        filteredWords.removeAll(filteredWords9);
        
        // Add "A" and "I" as valid single-letter words
        filteredWords.add("A");
        filteredWords.add("I");

        HashSet<String> fineFiltering = finefiltering(filteredWords9, filteredWords);
        
        long end = System.nanoTime();
        long duration = end - start;
        
        System.out.println("Time execution: " + (duration / 1_000_000_000.0) + " seconds");
        System.out.println("Total valid 9-letter words: " + fineFiltering.size());

    }

    private static HashSet<String> finefiltering(HashSet<String> filteredWords9, HashSet<String> filteredWords) {
        HashSet<String> fineFiltering = new HashSet<>();

        while (!filteredWords9.isEmpty()) {
            String word = filteredWords9.iterator().next();
            filteredWords9.remove(word);

            int count = countMatches(word, filteredWords);

            // Check if we found 8 matches (9 -> 8 -> 7 -> 6 -> 5 -> 4 -> 3 -> 2 -> 1)
            // and verify the final word is "A" or "I"
            if (count == 8) {
                fineFiltering.add(word);
            }
        }

        return fineFiltering;
    }

    public static int countMatches(String word, HashSet<String> dictionary) {
        // Use recursive method to check if we can reduce from word to "A" or "I"
        if (canReduceToSingleLetter(word, dictionary, 0)) {
            return 8; // Successfully reduced through 8 steps
        }
        return 0; // Not a valid chain
    }
    
    private static boolean canReduceToSingleLetter(String word, HashSet<String> dictionary, int depth) {
        // Base case: if we reached "A" or "I" and made exactly 8 steps
        if (word.length() == 1) {
            return (word.equals("A") || word.equals("I")) && depth == 8;
        }
        
        // If we've already made 8 steps but word is not single letter, invalid
        if (depth >= 8) {
            return false;
        }
        
        // Try removing each letter and check if resulting word is valid
        for (int i = 0; i < word.length(); i++) {
            StringBuilder sb = new StringBuilder(word);
            sb.deleteCharAt(i);
            String newWord = sb.toString();
            
            // Check if the new word is in dictionary or is "A" or "I"
            if (dictionary.contains(newWord) || newWord.equals("A") || newWord.equals("I")) {
                // Recursively try to reduce from this new word
                if (canReduceToSingleLetter(newWord, dictionary, depth + 1)) {
                    return true;
                }
            }
        }
        
        return false; // No valid path found
    }

    // Load words from URL
    private static List<String> loadAllWords(String url) throws IOException {
        URL wordsURL = new URL(url);

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(wordsURL.openConnection().getInputStream()))) {

            return br.lines().skip(2).collect(Collectors.toList());
        }
    }

}
