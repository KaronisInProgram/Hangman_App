package de.nvborck.hangman.network;

import de.nvborck.hangman.app.IGameHandler;
import de.nvborck.hangman.data.game.Game;
import de.nvborck.hangman.data.game.IGame;
import de.nvborck.hangman.network.messages.OpenGame;
import net.sharksystem.asap.*;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import static de.nvborck.hangman.network.Utils.*;

public class SearchReceiver implements ASAPMessageReceivedListener {

    public static final String option_search = "search";


    private ASAPPeer peer;
    private IGameHandler handler;

    public SearchReceiver(ASAPPeer peer, IGameHandler handler) {
        this.peer = peer;
        this.handler = handler;
    }


    @Override
    public void asapMessagesReceived(ASAPMessages asapMessages, String s, List<ASAPHop> list) throws IOException {

        // you could check uri, e.g. to figure out what chat is addressed, what running game, what POS offering...
        CharSequence uri = asapMessages.getURI();
        var splits = splitUri(uri.toString());

        // Only process if it's a search
        String option = splits.getOrDefault(Utils.option, "");
        if(!option.equals(option_search)) {
            return;
        }

        Utils.log("SearchReceiver --> got messages ( uri | number ): (" + uri + " | " + asapMessages.size() + ")");

        // When Id equals to local peer then it's an answer ...
        String id = splits.getOrDefault(Utils.id, "");
        String extra = splits.getOrDefault((Utils.extra + 1), "");
        if(this.peer.samePeer(id) && extra.equals(extra_answer)) {
            try {

                // Ignore all but the last message;
                Iterator<byte[]> msgIter = asapMessages.getMessages();
                byte[] lastMessage = null;
                while(msgIter.hasNext()) {
                    lastMessage = msgIter.next();
                }
                if(lastMessage != null) {
                    OpenGame message = new OpenGame(lastMessage);
                    this.handler.addOpenGame(message);
                }
            } catch (ASAPException e) {
                throw new RuntimeException(e);
            }

            return;
        }

        // ... Otherwise it's a request to answer with an open Game
        if(this.handler.hasActiveGame() && extra.equals(extra_request)) {
            try {
                String answerUri = option + "/" + id + "/" + extra_answer;
                OpenGame message = new OpenGame(this.handler.getGameId(), this.handler.getSearchedWord());

                this.peer.sendASAPMessage(IGameAPI.APP_FORMAT, answerUri, message.getSerializedMessage());
            } catch (ASAPException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
