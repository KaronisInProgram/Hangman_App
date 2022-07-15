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

    private final ASAPPeer peer;
    private final UUID uniqeId;

    public GameAPI(IGameHandler handler, ASAPPeer peer, UUID uniqeId) {
        this.peer = peer;
        this.uniqeId = uniqeId;

        this.peer.addASAPMessageReceivedListener(APP_FORMAT, new CommandReceiver(handler));
        this.peer.addASAPMessageReceivedListener(APP_FORMAT, new SearchReceiver(this.peer, handler, uniqeId));
        this.peer.addASAPMessageReceivedListener(APP_FORMAT, new SynchronizeReceiver(this.peer, handler, uniqeId));
    }

    @Override
    public void sendCommand(ICommand command, UUID gameid) throws ASAPException, IOException {

        byte[] serializedMessage = new GameCommand(command).getSerializedMessage();
        String uri = CommandReceiver.option_command + "/" + uniqeId.toString() + "/" + UUID.randomUUID();
        this.peer.sendASAPMessage(APP_FORMAT, uri, serializedMessage);

    }

    @Override
    public void searchGames() throws ASAPException {

        String uri = SearchReceiver.option_search + "/" + uniqeId.toString() + "/request";
        this.peer.sendASAPMessage(APP_FORMAT, uri, "question".getBytes());
    }

    @Override
    public void synchronizeGame(UUID gameid) throws ASAPException {

        String uri = SynchronizeReceiver.option_synchronize + "/" + uniqeId.toString() + "/request";
        this.peer.sendASAPMessage(APP_FORMAT, uri, "question".getBytes());
    }

}
