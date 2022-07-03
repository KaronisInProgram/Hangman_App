package de.nvborck.hangman.network;

import de.nvborck.hangman.app.IGameHandler;
import de.nvborck.hangman.command.ICommand;
import de.nvborck.hangman.network.messages.GameCommand;
import de.nvborck.hangman.network.messages.SynchronizeGame;
import net.sharksystem.asap.ASAPException;
import net.sharksystem.asap.ASAPPeer;

import java.io.IOException;
import java.util.UUID;

public class GameAPI implements IGameAPI{

    private IGameHandler handler;
    private ASAPPeer peer;

    public GameAPI(IGameHandler handler, ASAPPeer peer) {
        this.handler = handler;
        this.peer = peer;

        this.peer.addASAPMessageReceivedListener(APP_FORMAT, new CommandReceiver(this.peer, this.handler));
        this.peer.addASAPMessageReceivedListener(APP_FORMAT, new SearchReceiver(this.peer, this.handler));
        this.peer.addASAPMessageReceivedListener(APP_FORMAT, new SynchronizeReceiver(this.peer, this.handler));
    }

    @Override
    public void sendCommand(ICommand command, UUID gameid) throws ASAPException, IOException {

        byte[] serializedMessage = new GameCommand(command).getSerializedMessage();
        String uri = CommandReceiver.option_command + "/" + gameid.toString();
        this.peer.sendASAPMessage(APP_FORMAT, uri, serializedMessage);

    }

    @Override
    public void searchGames() throws ASAPException {

        String uri = SearchReceiver.option_search + "/" + this.peer.getPeerID() + "/request";
        this.peer.sendASAPMessage(APP_FORMAT, uri, "question".getBytes());
    }

    @Override
    public void synchronizeGame(UUID gameid) throws ASAPException, IOException {

        String uri = SynchronizeReceiver.option_synchronize + "/" + gameid.toString() + "/request";
        this.peer.sendASAPMessage(APP_FORMAT, uri, "question".getBytes());
    }

}
