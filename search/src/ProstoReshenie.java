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
        HashSet<String> words = new HashSet<>();
        HashSet<String> filteredWords = new HashSet<>();
        HashSet<String> filteredWords9 = new HashSet<>();

        words.addAll(dict);

        // Filtering words that contain A or I and length<=9
        for (String w : words) {
            int len = w.length();
            if (len <= 9 && (w.contains("A") || w.contains("I"))) {
                filteredWords.add(w);
            }
        }

        // Next filtration
        for (String n : filteredWords) {
            int len9 = n.length();
            if (len9 == 9) {
                filteredWords9.add(n);
            }
        }

        filteredWords.removeAll(filteredWords9);

        HashSet<String> fineFiltering = finefiltering(filteredWords9, filteredWords);

    }

    private static HashSet<String> finefiltering(HashSet<String> filteredWords9, HashSet<String> filteredWords) {
        HashSet<String> fineFiltering = new HashSet<>();

        while (!filteredWords9.isEmpty()) {
            String word = filteredWords9.iterator().next();
            filteredWords9.remove(word);

            int count = countMatches(word, filteredWords);

            if (count == 8) {
                fineFiltering.add(word);
            }
        }

        return fineFiltering;
    }

    public static int countMatches(String word, HashSet<String> dictionary) {

        // Брояч на намерените съвпадения
        int matches = 0;

        // Работна променлива, която ще "смаляваме"
        String current = word;

        // Продължаваме докато не стигнем 8 съвпадения или думата стане празна
        while (current.length() > 0 && matches < 8) {

            boolean foundMatchThisRound = false;

            // Обхождаме буквите една по една
            for (int i = 0; i < current.length(); i++) {

                // Махаме една буква от позиция i
                StringBuilder sb = new StringBuilder(current);
                sb.deleteCharAt(i);
                String newWord = sb.toString();

                // Проверка дали я има в HashSet
                if (dictionary.contains(newWord)) {

                    // Увеличаваме броя на съвпаденията
                    matches++;

                    // Продължаваме с тази нова дума
                    current = newWord;

                    foundMatchThisRound = true;
                    break; // излизаме, за да продължим от новата дума
                }
            }

            // Ако в този цикъл не намерихме нито едно ново съвпадение → прекратяваме
            if (!foundMatchThisRound) {
                break;
            }
        }

        return matches;
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
