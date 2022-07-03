package de.nvborck.hangman.network.messages;

import de.nvborck.hangman.command.*;
import de.nvborck.hangman.data.player.IPlayer;
import de.nvborck.hangman.data.player.Player;
import net.sharksystem.asap.ASAPException;
import net.sharksystem.asap.utils.ASAPSerialization;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SynchronizeGame {

    private UUID id;

    private List<GameCommand> gameCommands = new ArrayList<>();

    private byte[] serializedMessage;

    public SynchronizeGame(UUID id, List<ICommand> commands) throws IOException {
        this.id = id;

        for (ICommand command : commands) {
            this.gameCommands.add(new GameCommand(command));
        }

        this.serialize();
    }

    public SynchronizeGame(byte[] serializedMessage) throws IOException, ASAPException {
        this.serializedMessage = serializedMessage;
        this.deserialize();
    }

    public UUID getId() {
        return this.id;
    }

    public List<GameCommand> getCommands() {
        return this.gameCommands;
    }

    private void serialize() throws IOException {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ASAPSerialization.writeCharSequenceParameter(this.id.toString(), baos);

        ASAPSerialization.writeIntegerParameter(this.gameCommands.size(), baos);
        for (GameCommand command : this.gameCommands) {
            ASAPSerialization.writeByteArray(command.getSerializedMessage(), baos);
        }

        this.serializedMessage = baos.toByteArray();
    }

    private void deserialize() throws IOException, ASAPException {

        InputStream is = new ByteArrayInputStream(this.serializedMessage);
        this.id = UUID.fromString(ASAPSerialization.readCharSequenceParameter(is));

        int size = ASAPSerialization.readIntegerParameter(is);
        for (int i = 0; i < size; i++) {
            this.gameCommands.add(new GameCommand(ASAPSerialization.readByteArray(is)));
        }
    }

    public byte[] getSerializedMessage() {
        return this.serializedMessage;
    }

}
