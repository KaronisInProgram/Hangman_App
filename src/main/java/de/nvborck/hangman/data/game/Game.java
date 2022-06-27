package de.nvborck.hangman.data.game;

import de.nvborck.hangman.data.player.IPlayer;
import de.nvborck.hangman.data.player.Player;
import de.nvborck.hangman.data.wordprovider.IWordProvider;
import org.javatuples.Pair;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class Game implements IGame, IPlayerManager{

    private final IWordProvider wordProvider;

    private final List<Pair<UUID, String>> players = new LinkedList<>();
    private Pair<UUID, String> activePlayer;

    private final List<Character> usedCharacters = new LinkedList<>();
    private String searchedWord = "";
    private String maskedWord = "";

    private UUID id = UUID.randomUUID();

    public Game(IWordProvider wordProvider) {
        this.wordProvider = wordProvider;
    }

    @Override
    public void start() {
        this.searchedWord = this.wordProvider.getRandomWord();
        this.maskedWord = "_".repeat(searchedWord.length());
    }

    @Override
    public void start(String word, UUID id) {
        this.id = id;
        this.searchedWord = word;
        this.maskedWord = "_".repeat(searchedWord.length());
    }

    @Override
    public boolean guess(char character, UUID player) {

        // Check if the requirements are meet
        if(!this.isFinished() && this.activePlayer == null || this.activePlayer.getValue0().compareTo(player) != 0) {
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
    public UUID getId() {
        return this.id;
    }

    @Override
    public void addPlayer(UUID id, String name) {
        Pair<UUID, String> playerTuple = new Pair<>(id, name);

        this.players.add(playerTuple);
        if(this.activePlayer == null) {
            this.activePlayer = playerTuple;
        }
    }

    @Override
    public IPlayer getActivePlayer() {
        if(this.activePlayer != null) {
            IPlayer player = new Player();

            player.setId(this.activePlayer.getValue0());
            player.setName(this.activePlayer.getValue1());

            return player;
        }

        return null;
    }

    @Override
    public void removePlayer(UUID player) {

        for (var pair : this.players) {
            if(pair.getValue0() == player) {
                this.players.remove(pair);
                setNewActivePlayer();
                break;
            }
        }
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
