import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

public class WordReduction {

    private static final String DICT_URL =
            "https://raw.githubusercontent.com/nikiiv/JavaCodingTestOne/master/scrabble-words.txt";

    public static void main(String[] args) {

        long start = System.nanoTime();

        try {
            // 1) Load all words from the URL
            List<String> dict = loadAllWords(DICT_URL);

            // 2) Put all words into a HashSet for O(1) lookup speed
            Set<String> dictSet = new HashSet<>(dict);

            // 3) Extract only the words with exactly 9 letters
            List<String> words9 = new ArrayList<>();
            for (String w : dict) {
                if (w.length() == 9) {
                    words9.add(w);
                }
            }

            // 4) For each 9-letter word, try to build a full reduction chain
            List<List<String>> chains = returnValidChains(words9, dictSet);

            long end = System.nanoTime();

            long duration = end - start;

            System.out.println("Time execution: " + (duration / 1_000_000.0) + " ms");

            // Print chains
            printChains(chains);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Attempts to build a full chain of valid words by removing 1 letter at a time
    private static List<String> buildValidChain(String word, Set<String> dict) {
        List<String> chain = new ArrayList<>();
        chain.add(word);
        String current = word;
        StringBuilder sb = new StringBuilder();

        // Continue until the word becomes a single letter
        while (current.length() > 1) {
            boolean found = false;

            // Try removing each letter using StringBuilder
            for (int i = 0; i < current.length(); i++) {

                sb.setLength(0);
                sb.append(current);
                sb.deleteCharAt(i); // remove one character
                String shorter = sb.toString();

                // "A" and "I" are valid standalone English words
                if (dict.contains(shorter) || shorter.equals("A") || shorter.equals("I")) {
                    chain.add(shorter);
                    current = shorter;
                    found = true;
                    break; // move on to the next reduction step
                }
            }

            // If no valid shorter word is found → chain is invalid
            if (!found) {
                return Collections.emptyList();
            }
        }

        return chain;
    }

    private static List<List<String>> returnValidChains(List<String> words9, Set<String> dictSet) {
        return words9.parallelStream()
                .map(w -> buildValidChain(w, dictSet))
                .filter(chain -> !chain.isEmpty())
                .collect(Collectors.toList());
    }

    private static void printChains(List<List<String>> chains) {
        for (List<String> w : chains) {
            System.out.println(w);
        }
        // separator between chains
        System.out.println();
    }

    // Loads words from a URL
    private static List<String> loadAllWords(String url) throws IOException {
        URL wordsURL = new URL(url);

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(wordsURL.openConnection().getInputStream()))) {

            // Skip first 2 lines (metadata)
            return br.lines().skip(2).collect(Collectors.toList());
        }
    }
}
