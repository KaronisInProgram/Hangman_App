package de.nvborck.hangman.network;

import de.nvborck.hangman.app.IGameHandler;
import de.nvborck.hangman.command.ICommand;
import net.sharksystem.asap.ASAPException;
import net.sharksystem.asap.ASAPPeer;

import java.util.UUID;

public class GameAPI implements IGameAPI{

    private IGameHandler handler;
    private ASAPPeer peer;

    public GameAPI(IGameHandler handler, ASAPPeer peer) {
        this.handler = handler;
        this.peer = peer;

        this.peer.addASAPMessageReceivedListener(APP_FORMAT, new CommandReceiver(this.peer));
        this.peer.addASAPMessageReceivedListener(APP_FORMAT, new SearchReceiver(this.peer, this.handler));
    }

    @Override
    public void sendCommand(ICommand command, UUID gameid) throws ASAPException {

        // need to send a message
        // 1st: serialize it:
        byte[] serializedMessage = command.asSerializableCommand().getSerializedMessage();

        // define a uri if you like
        String uri = "game/" + gameid.toString();

        // 2nd: send it with ASAP peer
        this.peer.sendASAPMessage(APP_FORMAT, uri, serializedMessage);

    }

    @Override
    public void searchGames() throws ASAPException {

        String uri = "search/" + this.peer.getPeerID() + "/request";

        this.peer.sendASAPMessage(APP_FORMAT, uri, "question".getBytes());
    }

    @Override
    public void joinGame(UUID gameid) {

    }

}
