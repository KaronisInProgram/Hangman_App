package de.nvborck.hangman.app;

import de.nvborck.hangman.data.player.IPlayer;
import net.sharksystem.asap.ASAPException;

import java.io.IOException;

public class JoinAfterSynchronize implements IGameListener{

    IGameHandler handler;
    IPlayer player;

    public JoinAfterSynchronize(IGameHandler handler, IPlayer player) {
        this.handler = handler;
        this.player = player;
    }

    @Override
    public void getNotified() throws IOException, ASAPException {
        handler.joinGame(handler.getGameId(), this.player);
    }
}
