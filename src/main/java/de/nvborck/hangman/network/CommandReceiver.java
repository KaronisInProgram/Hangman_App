package de.nvborck.hangman.network;

import de.nvborck.hangman.app.IGameHandler;
import de.nvborck.hangman.network.messages.GameCommand;
import de.nvborck.hangman.network.messages.OpenGame;
import jdk.jshell.execution.Util;
import net.sharksystem.asap.*;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

import static de.nvborck.hangman.network.Utils.*;
import static de.nvborck.hangman.network.Utils.extra_answer;

public class CommandReceiver implements ASAPMessageReceivedListener {

    public static final String option_command = "command";

    private ASAPPeer peer;
    private IGameHandler handler;

    public CommandReceiver(ASAPPeer peer, IGameHandler handler) {
        this.peer = peer;
        this.handler = handler;
    }


    @Override
    public void asapMessagesReceived(ASAPMessages asapMessages, String s, List<ASAPHop> list) throws IOException {

        // you could check uri, e.g. to figure out what chat is addressed, what running game, what POS offering...
        CharSequence uri = asapMessages.getURI();
        var splits = splitUri(uri.toString());

        Utils.log("CommandReceiver --> got messages ( uri | number ): (" + uri + " | " + asapMessages.size() + ")");

        String option = splits.getOrDefault("option", "");
        if(!option.equals(option_command)) {
            Utils.log("CommandReceiver --> No further processing!");
            return;
        }

        // When Id equals to local peer then it's an answer ...
        String id = splits.getOrDefault(Utils.id, "");
        if(id.equals(this.handler.getGameId().toString())) {
            try {

                Iterator<byte[]> msgIter = asapMessages.getMessages();
                while(msgIter.hasNext()) {
                    GameCommand message = new GameCommand(msgIter.next());
                    this.handler.handleCommandWithoutSharing(message.getCommand());
                }
            } catch (ASAPException e) {
                throw new RuntimeException(e);
            }
        }
    }


}
