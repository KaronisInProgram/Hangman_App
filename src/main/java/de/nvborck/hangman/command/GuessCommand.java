package de.nvborck.hangman.command;

import de.nvborck.hangman.data.game.GameEvent;
import de.nvborck.hangman.data.game.IGame;
import de.nvborck.hangman.data.player.IPlayer;
import de.nvborck.hangman.data.player.Player;
import net.sharksystem.asap.ASAPException;
import net.sharksystem.asap.utils.ASAPSerialization;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

public class GuessCommand implements ICommand<IGame>, ISerializableCommand {
    private IGame game;
    private char character;
    private IPlayer player;
    private UUID id = UUID.randomUUID();

    private byte[] serializedMessage;

    public GuessCommand(IGame game, char character, IPlayer player) throws IOException {

        this.game = game;
        this.character = character;
        this.player = player;

        this.serialize();
    }

    public GuessCommand(byte[] serializedMessage) throws IOException, ASAPException {
        this.serializedMessage = serializedMessage;
        this.player = new Player();

        this.deserialize();
    }

    @Override
    public void execute() {
        this.game.guess(character, player);
    }

    @Override
    public GameEvent getCorrelatedEvent() {
        return GameEvent.searchedWordChange;
    }

    @Override
    public CommandType getCorrelatedType() {
        return CommandType.guess;
    }

    @Override
    public ISerializableCommand asSerializableCommand() {
        return this;
    }

    @Override
    public void setCoreObjectIfNull(IGame game) {
        if(this.game == null) {
            this.game = game;
        }
    }

    @Override
    public UUID getUniqeId() {
        return this.id;
    }

    @Override
    public byte[] getSerializedMessage() { return this.serializedMessage;}

    private void serialize() throws IOException {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ASAPSerialization.writeCharSequenceParameter(this.id.toString(), baos);
        ASAPSerialization.writeCharSequenceParameter(Character.toString(this.character), baos);
        ASAPSerialization.writeCharSequenceParameter(this.player.getName(), baos);
        ASAPSerialization.writeCharSequenceParameter(this.player.getId().toString(), baos);
        this.serializedMessage = baos.toByteArray();
    }

    private void deserialize() throws IOException, ASAPException {

        InputStream is = new ByteArrayInputStream(this.serializedMessage);
        this.id = UUID.fromString(ASAPSerialization.readCharSequenceParameter(is));
        this.character = ASAPSerialization.readCharSequenceParameter(is).charAt(0);
        this.player.setName(ASAPSerialization.readCharSequenceParameter(is));
        this.player.setId(UUID.fromString(ASAPSerialization.readCharSequenceParameter(is)));
    }
}
