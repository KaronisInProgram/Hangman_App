package de.nvborck.hangman.network;

import de.nvborck.hangman.command.ICommand;
import net.sharksystem.asap.ASAPException;

import java.io.IOException;
import java.util.UUID;

public interface IGameAPI {
    String APP_FORMAT = "de.nvborck/asap_hangman_game";


    void sendCommand(ICommand command, UUID gameid) throws IOException, ASAPException;
    void searchGames() throws IOException, ASAPException;
    void joinGame(UUID gameid);
}
