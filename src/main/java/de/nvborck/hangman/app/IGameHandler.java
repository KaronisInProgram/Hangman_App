package de.nvborck.hangman.app;

import de.nvborck.hangman.command.ICommand;
import de.nvborck.hangman.data.player.IPlayer;
import de.nvborck.hangman.network.messages.OpenGame;
import de.nvborck.hangman.network.messages.SynchronizeGame;
import net.sharksystem.asap.ASAPException;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface IGameHandler {

    void initializeGame(IPlayer player) throws IOException, ASAPException;

    void joinGame(UUID gameid, IPlayer player) throws IOException, ASAPException;

    void searchGames() throws IOException, ASAPException;

    boolean hasActiveGame();

    String getMaskedWord();

    List<Character> getUsedCharacter();

    IPlayer getActivePlayer();

    void guess(char character, IPlayer player) throws IOException, ASAPException;

    List<OpenGame> getOpenGames();

    void addOpenGame(OpenGame game);

    UUID getGameId();

    /**
     */
    String getSearchedWord();


    void synchronizeGame(SynchronizeGame synchronizeGame);

    List<ICommand> getCommands();

    void handleCommandWithoutSharing(ICommand command) throws IOException, ASAPException;
}
