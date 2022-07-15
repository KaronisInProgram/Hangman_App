package de.nvborck.hangman.network;

import de.nvborck.hangman.app.IGameHandler;
import de.nvborck.hangman.command.ICommand;
import de.nvborck.hangman.network.messages.GameCommand;
import de.nvborck.hangman.network.messages.OpenGame;
import jdk.jshell.execution.Util;
import net.sharksystem.asap.*;

import java.io.*;
import java.util.*;

import static de.nvborck.hangman.network.Utils.*;

public class CommandReceiver implements ASAPMessageReceivedListener {

    public static final String option_command = "command";

    private final IGameHandler handler;

    public CommandReceiver(IGameHandler handler) {
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

        // When Id equals to local Game then it's an answer ...
        String id = splits.getOrDefault(Utils.id, "");
        if(id.equals(this.handler.getGameId().toString())) {

            try {
                Iterator<byte[]> msgIter = asapMessages.getMessages();
                while(msgIter.hasNext()) {
                    GameCommand message = new GameCommand(msgIter.next());

                    boolean allreadyProcessed = false;
                        for (ICommand command: this.handler.getCommands()) {
                            if(command.getUniqeId().compareTo(message.getCommand().getUniqeId()) == 0) {
                                allreadyProcessed = true;
                                break;
                            }
                        }

                        if(allreadyProcessed) {
                            continue;
                        }

                    this.handler.handleCommandWithoutSharing(message.getCommand());
                }
            } catch (ASAPException e) {
                throw new RuntimeException(e);
            }
        }
    }


}
