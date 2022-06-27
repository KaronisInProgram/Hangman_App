package de.nvborck.hangman.app;


import de.nvborck.hangman.data.game.IGame;
import de.nvborck.hangman.command.ICommand;
import de.nvborck.hangman.data.player.IPlayer;
import net.sharksystem.asap.ASAPException;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface IGameHandler {

    void initializeGame(IPlayer player) throws IOException, ASAPException;

    void joinGame(UUID gameid, IPlayer player);

    void searchGames() throws IOException, ASAPException;

    void stoppSearching();

    boolean hasActiveGame();

    String getMaskedWord();

    List<Character> getUsedCharacter();

    IPlayer getActivePlayer();

    void guess(char character, IPlayer player) throws IOException, ASAPException;

    List<IGame> getOpenGames();

    void addOpenGame(IGame game);

    UUID getGameId();

    /**
     */
    String getSearchedWord();
}
