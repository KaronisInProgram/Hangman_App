package de.nvborck.hangman.network;

import de.nvborck.hangman.app.IGameHandler;
import de.nvborck.hangman.network.messages.OpenGame;
import de.nvborck.hangman.network.messages.SynchronizeGame;
import net.sharksystem.asap.*;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.List;

import static de.nvborck.hangman.network.Utils.*;

public class SynchronizeReceiver implements ASAPMessageReceivedListener {

    public static final String option_synchronize = "synchronize";

    private ASAPPeer peer;
    private IGameHandler handler;

    public SynchronizeReceiver(ASAPPeer peer, IGameHandler handler) {
        this.peer = peer;
        this.handler = handler;
    }


    @Override
    public void asapMessagesReceived(ASAPMessages asapMessages, String s, List<ASAPHop> list) throws IOException {

        // you could check uri, e.g. to figure out what chat is addressed, what running game, what POS offering...
        CharSequence uri = asapMessages.getURI();
        var splits = splitUri(uri.toString());

        Utils.log("SynchronizeReceiver --> got messages ( uri | number ): (" + uri + " | " + asapMessages.size() + ")");

        // Only process if it's a search
        String option = splits.getOrDefault(Utils.option, "");
        if(!option.equals(option_synchronize)) {
            Utils.log("SynchronizeReceiver --> No further processing!");
            return;
        }

        // When Id equals to local peer then it's an answer ...
        String id = splits.getOrDefault(Utils.id, "");
        String extra = splits.getOrDefault((Utils.extra + 1), "");
        if(extra.equals(extra_answer)) {
            try {

                // Ignore all but the last message;
                Iterator<byte[]> msgIter = asapMessages.getMessages();
                byte[] lastMessage = null;
                while(msgIter.hasNext()) {
                    lastMessage = msgIter.next();
                }
                if(lastMessage != null) {
                    SynchronizeGame message = new SynchronizeGame(lastMessage);
                    handler.synchronizeGame(message);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            return;
        }

        // ... Otherwise it's a request to answer with an SynchronizeGame
        if(this.handler.hasActiveGame() && extra.equals(extra_request)) {
            try {
                String answerUri = option + "/" + id + "/" + extra_answer;
                SynchronizeGame message = new SynchronizeGame(this.handler.getGameId(), this.handler.getCommands());

                this.peer.sendASAPMessage(IGameAPI.APP_FORMAT, answerUri, message.getSerializedMessage());
            } catch (ASAPException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
