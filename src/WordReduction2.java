import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

public class WordReduction2 {

    private static final String DICT_URL =
            "https://raw.githubusercontent.com/nikiiv/JavaCodingTestOne/master/scrabble-words.txt";

    public static void main(String[] args) {

        long start = System.nanoTime();

        try {
            // Load all words from the URL
            List<String> dict = loadAllWords(DICT_URL);

            // Map to hold words by length
            Map<Integer, List<String>> wordsByLength = new HashMap<>();

            // Add single-letter valid words
            wordsByLength.put(1, new ArrayList<>(List.of("A", "I")));

            // Filter words that contain A or I and length<=9
            for (String w : dict) {
                int len = w.length();
                if (len >= 2 && len <= 9 && (w.contains("A") || w.contains("I"))) {
                    wordsByLength.computeIfAbsent(len, k -> new ArrayList<>()).add(w);
                }
            }

            // Map for valid word
            Map<Integer, Set<String>> validWordsMap = new HashMap<>();
            validWordsMap.put(1, new HashSet<>(List.of("A", "I")));

            // Start with words with length = 2
            List<String> currentLevel = wordsByLength.getOrDefault(2, Collections.emptyList());


            for (int len = 3; len <= 9; len++) {
                List<String> upperLevel = wordsByLength.getOrDefault(len, Collections.emptyList());
                Set<String> nextLevel = new HashSet<>();

                // Find all 2-letter words that are substrings of any 3-letter words and keep the matches in validWordsMap
                for (String w2 : currentLevel) {
                    for (String wNext : upperLevel) {
                        if (wNext.contains(w2)) {

                            // Add words to validWordsMap while avoiding duplicates
                            validWordsMap.computeIfAbsent(len - 1, k -> new HashSet<>()).add(w2);
                            validWordsMap.computeIfAbsent(len, k -> new HashSet<>()).add(wNext);

                            // Store 3-letter words that matched, to check against 4-letter words
                            nextLevel.add(wNext);
                        }
                    }
                }

                // nextLevel
                currentLevel = new ArrayList<>(nextLevel);
            }

            long end = System.nanoTime();

            long duration = end - start;

            System.out.println("Time execution: " + (duration / 1_000_000.0) + " ms");

            System.out.println("Valid words: " + validWordsMap);
            System.out.println("Total valid words: " + validWordsMap.size());

        } catch (Exception e) {
            e.printStackTrace();
        }
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
