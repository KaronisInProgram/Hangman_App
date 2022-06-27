package de.nvborck.hangman.data.wordprovider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class SimpleWordProvider implements IWordProvider{

    private Random random = new Random();

    private List<String> usedWords = new ArrayList<>();
    private List<String> words;

    public SimpleWordProvider() {
        this.words = new ArrayList<>(Arrays.asList("Test", "Bannane", "Kuchen", "Auto", "Programmieren", "Kueche", "Computer", "Natur", "Schiff", "Stein", "Lara Croft"));
    }

    public SimpleWordProvider(List<String> words) {
        this.words = new ArrayList<>(words);
    }

    @Override
    public String getRandomWord() {

        // When there is no word to provide then return empty
        if(this.words.size() == 0 && this.usedWords.size() == 0) {
            return "";
        }

        // In case the available word are empty return all used word
        if(this.words.size() == 0) {
            this.words = new ArrayList<>(this.usedWords);
            this.usedWords.clear();
        }

        String next = this.words.get(random.nextInt(words.size()));

        this.usedWords.add(next);
        this.words.remove(next);

        return next;
    }
}
