package de.nvborck.hangman.data.game;

import java.util.List;
import java.util.UUID;

/**
 *
 */
public interface IGame {

    /**
     * Starts a new Round of GameHandler.
     * If It's called in an ongoing Round, this Round will be reset and be started with another Word.
     */
    void start();

    /**
     * Starts a new Round of GameHandler.
     * If It's called in an ongoing Round, this Round will be reset and be started with another Word.
     */
    void start(String word, UUID id);

    /**
     * Lets the active Player does a guess on the data.game.
     * If this is called from a Player which is not in his round, it will change nothing on the Game.
     *<p>
     * If the Character is not between [a-z] this Method should throw an IllegalArgumentException.
     */
    boolean guess(char character, UUID player) throws IllegalArgumentException, IllegalStateException;

    /**
     * Returns the searched Word in its masked Form.
     * Each already correct guessed Character will be visible and the other will be hidden with  an underscore.
     */
    boolean isFinished();

    /**
     * Returns the searched Word in its masked Form.
     * Each already correct guessed Character will be visible and the other will be hidden with  an underscore.
     */
    String getMaskedWord();

    /**
     */
    String getSearchedWord();

    /**
     * Returns all the Player guessed (used) Character in this Play-through.
     */
    List<Character> getUsedCharacter();

    /**
     * Returns all the Player guessed (used) Character in this Play-through.
     */
    UUID getId();

}