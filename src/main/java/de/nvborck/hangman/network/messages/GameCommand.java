package de.nvborck.hangman.network.messages;

import de.nvborck.hangman.command.*;
import net.sharksystem.asap.ASAPException;
import net.sharksystem.asap.utils.ASAPSerialization;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class GameCommand {

    private ICommand command;

    private byte[] serializedMessage;

    public GameCommand(ICommand command) throws IOException {
        this.command = command;
        this.serialize();
    }

    public GameCommand(byte[] serializedMessage) throws IOException, ASAPException {
        this.serializedMessage = serializedMessage;
        this.deserialize();
    }
    public ICommand getCommand() {
        return this.command;
    }

    private void serialize() throws IOException {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ASAPSerialization.writeCharSequenceParameter(this.command.getCorrelatedType().toString(), baos);
        ASAPSerialization.writeByteArray(this.command.asSerializableCommand().getSerializedMessage(), baos);

        this.serializedMessage = baos.toByteArray();
    }

    private void deserialize() throws IOException, ASAPException {

        InputStream is = new ByteArrayInputStream(this.serializedMessage);
        CommandType commandType = CommandType.valueOf(ASAPSerialization.readCharSequenceParameter(is));
        switch (commandType) {
            case start:
                this.command = new StartCommand(ASAPSerialization.readByteArray(is));
                break;
            case join:
                this.command = new JoinCommand(ASAPSerialization.readByteArray(is));
                break;
            case guess:
                this.command = new GuessCommand(ASAPSerialization.readByteArray(is));
                break;
            default:
                throw new IllegalArgumentException("Unknown CommandType!");
        }
    }

    public byte[] getSerializedMessage() {
        return this.serializedMessage;
    }
}
