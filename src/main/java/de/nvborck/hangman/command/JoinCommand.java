package de.nvborck.hangman.command;

import de.nvborck.hangman.app.GameEvents;
import de.nvborck.hangman.data.game.IGame;
import de.nvborck.hangman.data.game.IPlayerManager;
import net.sharksystem.asap.ASAPException;
import net.sharksystem.asap.utils.ASAPSerialization;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

public class JoinCommand implements ICommand<IPlayerManager>, ISerializableCommand {

    private IPlayerManager playerManager;
    private String name;
    private UUID id;

    private byte[] serializedMessage;

    public JoinCommand(IPlayerManager playerManager, UUID id, String name) throws IOException {

        this.playerManager = playerManager;
        this.id = id;
        this.name = name;
        this.serialize();
    }

    public JoinCommand(byte[] serializedMessage) throws IOException, ASAPException {
        this.serializedMessage = serializedMessage;
        this.deserialize();
    }

    private void serialize() throws IOException {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ASAPSerialization.writeCharSequenceParameter(this.id.toString(), baos);
        ASAPSerialization.writeCharSequenceParameter(this.name, baos);
        this.serializedMessage = baos.toByteArray();
    }

    private void deserialize() throws IOException, ASAPException {

        InputStream is = new ByteArrayInputStream(this.serializedMessage);
        this.id = UUID.fromString(ASAPSerialization.readCharSequenceParameter(is));
        this.name = ASAPSerialization.readCharSequenceParameter(is);
    }

    @Override
    public void execute() {
        this.playerManager.addPlayer(this.id, this.name);
    }

    @Override
    public void setCoreObjectIfNull(IPlayerManager playerManager) {
        if(this.playerManager == null) {
            this.playerManager = playerManager;
        }
    }

    @Override
    public GameEvents getCorrelatedEvent() {
        return GameEvents.player;
    }

    @Override
    public ISerializableCommand asSerializableCommand() {
        return this;
    }

    @Override
    public byte[] getSerializedMessage() {
        return this.serializedMessage;
    }

    @Override
    public ICommand fromByteArray(byte[] data) throws IOException, ASAPException {
        return new JoinCommand(data);
    }
}
