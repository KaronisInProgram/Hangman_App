package de.nvborck.hangman.data.game;

import de.nvborck.hangman.data.player.IPlayer;

import java.util.UUID;

public interface IPlayerManager {

    /**
     * Adds a new Player to the Game.
     * This Method should be abel to be called at every Time.
     * The process of adding a new Person should not disturb the other Player.
     * <p>
     * Every new Player should be added to do the last Action in the current Round.
     */
    void addPlayer(UUID player, String name);

    /**
     * Return the Player, which will do the next guess.
     * This Will change with each guess done in the Game, only exception is when there is only one Player.
     *
     * @return The Player which does the next guess.
     */
    IPlayer getActivePlayer();

    /**
     * Removes a Player if he does not participate anymore in a Game.
     * The Player should be removed without disturbing other active Players.
     */
    void removePlayer(UUID player);
}
