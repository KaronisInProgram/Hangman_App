package de.nvborck.hangman.network;

import net.sharksystem.asap.ASAPHop;
import net.sharksystem.asap.ASAPMessageReceivedListener;
import net.sharksystem.asap.ASAPMessages;
import net.sharksystem.asap.ASAPPeer;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

import static de.nvborck.hangman.network.Utils.splitUri;

public class CommandReceiver implements ASAPMessageReceivedListener {

    private final String option_game = "game";

    private ASAPPeer peer;

    public CommandReceiver(ASAPPeer peer) {
        this.peer = peer;
    }


    @Override
    public void asapMessagesReceived(ASAPMessages asapMessages, String s, List<ASAPHop> list) throws IOException {

        // you could check uri, e.g. to figure out what chat is addressed, what running game, what POS offering...
        CharSequence uri = asapMessages.getURI();
        var splits = splitUri(uri.toString());

        String option = splits.getOrDefault("option", "");
        if(!option.equals(option_game)) {
            return;
        }
        this.log("CommandReceiver --> got messages ( uri | number ): (" + uri + " | " + asapMessages.size() + ")");
    }


    private void log(String msg) {
        StringBuilder sb = new StringBuilder();
        sb.append(">>>>>>>>>>>>>> YOUR APPLICATION | YOUR APPLICATION | YOUR APPLICATION <<<<<<<<<<<<<<<<<<<<<\n");
        sb.append(msg);
        sb.append("\n>>>>>>>>>>>>>> YOUR APPLICATION | YOUR APPLICATION | YOUR APPLICATION <<<<<<<<<<<<<<<<<<<<<\n");
        System.out.println(sb);
    }

}
