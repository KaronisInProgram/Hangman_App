package de.nvborck.hangman.command;

import de.nvborck.hangman.data.game.GameEvent;
import de.nvborck.hangman.data.game.IPlayerManager;
import de.nvborck.hangman.data.player.IPlayer;
import de.nvborck.hangman.data.player.Player;
import net.sharksystem.asap.ASAPException;
import net.sharksystem.asap.utils.ASAPSerialization;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

public class JoinCommand implements ICommand<IPlayerManager>, ISerializableCommand {

    private IPlayerManager playerManager;
    private IPlayer player;
    private UUID id = UUID.randomUUID();

    private byte[] serializedMessage;

    public JoinCommand(IPlayerManager playerManager, IPlayer player) throws IOException {

        this.playerManager = playerManager;
        this.player = player;

        this.serialize();
    }

    public JoinCommand(byte[] serializedMessage) throws IOException, ASAPException {
        this.serializedMessage = serializedMessage;
        this.player = new Player();

        this.deserialize();
    }

    private void serialize() throws IOException {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ASAPSerialization.writeCharSequenceParameter(this.id.toString(), baos);
        ASAPSerialization.writeCharSequenceParameter(this.player.getId().toString(), baos);
        ASAPSerialization.writeCharSequenceParameter(this.player.getName(), baos);
        this.serializedMessage = baos.toByteArray();
    }

    private void deserialize() throws IOException, ASAPException {

        InputStream is = new ByteArrayInputStream(this.serializedMessage);
        this.id = UUID.fromString(ASAPSerialization.readCharSequenceParameter(is));
        this.player.setId(UUID.fromString(ASAPSerialization.readCharSequenceParameter(is)));
        this.player.setName(ASAPSerialization.readCharSequenceParameter(is));
    }

    @Override
    public void execute() {
        this.playerManager.addPlayer(this.player);
    }

    @Override
    public void setCoreObjectIfNull(IPlayerManager playerManager) {
        if(this.playerManager == null) {
            this.playerManager = playerManager;
        }
    }

    @Override
    public UUID getUniqeId() {
        return this.id;
    }

    @Override
    public GameEvent getCorrelatedEvent() {
        return GameEvent.playerChange;
    }

    @Override
    public CommandType getCorrelatedType() {
        return CommandType.join;
    }

    @Override
    public ISerializableCommand asSerializableCommand() {
        return this;
    }

    @Override
    public byte[] getSerializedMessage() {
        return this.serializedMessage;
    }

}
