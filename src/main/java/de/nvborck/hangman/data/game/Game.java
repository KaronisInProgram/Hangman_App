package de.nvborck.hangman.data.game;

import de.nvborck.hangman.data.player.IPlayer;
import de.nvborck.hangman.data.player.Player;
import de.nvborck.hangman.data.wordprovider.IWordProvider;
import org.javatuples.Pair;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class Game implements IGame, IPlayerManager{

    private final List<IPlayer> players = new LinkedList<>();
    private IPlayer activePlayer;

    private final List<Character> usedCharacters = new LinkedList<>();
    private String searchedWord = "";
    private String maskedWord = "";

    @Override
    public void start(String word) {
        this.searchedWord = word;
        this.maskedWord = "_".repeat(searchedWord.length());
    }

    @Override
    public boolean guess(char character, IPlayer player) {

        // Check if the requirements are meet
        if(!this.isFinished() && this.activePlayer == null || !this.activePlayer.equals(player)) {
            return false;
        }

        // Validate input
        boolean result = this.isValidGuess(character);

        // Set new acting Player
        setNewActivePlayer();

        if(!this.usedCharacters.contains(character)) {
            this.usedCharacters.add(character);
        }

        // Return validation
        if(!result) {
            return false;
        }

        this.updateMaskedWord(character);

        return true;
    }

    @Override
    public boolean isFinished() {
        return !this.maskedWord.contains("_");
    }

    @Override
    public String getMaskedWord() {
        return this.maskedWord;
    }

    @Override
    public String getSearchedWord() {
        return this.searchedWord;
    }

    @Override
    public List<Character> getUsedCharacter() {
        return new LinkedList<>(this.usedCharacters);
    }

    @Override
    public void addPlayer(IPlayer player) {

        this.players.add(player);
        if(this.activePlayer == null) {
            this.activePlayer = player;
        }
    }

    @Override
    public IPlayer getActivePlayer() {

        return this.activePlayer != null ? this.activePlayer : null;
    }

    @Override
    public void removePlayer(IPlayer player) {

        this.players.remove(player);
        setNewActivePlayer();
    }

    private boolean isValidGuess(Character character) {
        return this.searchedWord.contains(character.toString()) && !this.usedCharacters.contains(character);
    }

    private void updateMaskedWord(Character character) {
        List<Integer> indices = new LinkedList<>();

        // First encounter
        int encounterIndex = this.searchedWord.indexOf(character);
        if(encounterIndex >= 0) {
            indices.add(encounterIndex);
        }

        // All following Encounters
        while (encounterIndex >= 0) {
            encounterIndex = this.searchedWord.indexOf(character, encounterIndex + 1);

            if(encounterIndex != -1) {
                indices.add(encounterIndex);
            }
        }

        // Update if at least on Encounter was found
        char[] tempMaskedWord = this.maskedWord.toCharArray();
        for (int foundIndex : indices) {
            tempMaskedWord[foundIndex] = character;
        }
        this.maskedWord = String.valueOf(tempMaskedWord);
    }

    private void setNewActivePlayer() {
        if(this.players.size() > 0) {
            int playerIndex = this.players.indexOf(this.activePlayer);
            this.activePlayer = ((++playerIndex) >= this.players.size()) ? this.players.get(0) : this.players.get(playerIndex);
        }else {
            this.activePlayer = null;
        }
    }

}
