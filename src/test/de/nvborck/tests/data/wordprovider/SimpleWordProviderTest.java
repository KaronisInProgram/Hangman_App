package de.nvborck.tests.data.wordprovider;

import de.nvborck.hangman.data.wordprovider.IWordProvider;
import de.nvborck.hangman.data.wordprovider.SimpleWordProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SimpleWordProviderTest {

    @Test
    void theSelectedWordsAreNotTheSame() {

        // Arrange
        IWordProvider provider = new SimpleWordProvider();

        // Act
        String word1 = provider.getRandomWord();
        String word2 = provider.getRandomWord();

        // Assert
        Assertions.assertNotEquals(word1, word2);
    }

    @Test
    void whenMoreWordsAreSelectedAsDefinedTheyRepeat() {

        // Arrange
        IWordProvider provider = new SimpleWordProvider();

        // Act
        String word1 = provider.getRandomWord();
        String word2 = provider.getRandomWord();
        String word3 = provider.getRandomWord();
        String word4 = provider.getRandomWord();
        String word5 = provider.getRandomWord();
        String word6 = provider.getRandomWord();
        String word7 = provider.getRandomWord();
        String word8 = provider.getRandomWord();
        String word9 = provider.getRandomWord();
        String word10 = provider.getRandomWord();
        String word11 = provider.getRandomWord();

        String repeat = provider.getRandomWord();

        // Assert
        List<String> words = Arrays.asList(word1, word2, word3, word4, word5, word6, word7, word8, word9,word10, word11);

        Assertions.assertTrue(words.contains(repeat));
    }

    @Test
    void whenThereAreNoWordsDefinedReturnEmpty() {

        // Arrange
        IWordProvider provider = new SimpleWordProvider(new ArrayList<>());

        // Act
        String word1 = provider.getRandomWord();
        String word2 = provider.getRandomWord();

        // Assert
        Assertions.assertEquals("", word1);
        Assertions.assertEquals("", word2);
    }
}
