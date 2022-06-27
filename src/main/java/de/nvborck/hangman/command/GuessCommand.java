package de.nvborck.hangman.command;

import de.nvborck.hangman.app.GameEvents;
import de.nvborck.hangman.data.game.IGame;
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
    private UUID player;

    private byte[] serializedMessage;

    public GuessCommand(IGame game, char character, UUID player) throws IOException {

        this.game = game;
        this.character = character;
        this.player = player;
        this.serialize();
    }

    public GuessCommand(byte[] serializedMessage) throws IOException, ASAPException {
        this.serializedMessage = serializedMessage;
        this.deserialize();
    }

    @Override
    public void execute() {
        this.game.guess(character, player);
    }

    @Override
    public GameEvents getCorrelatedEvent() {
        return GameEvents.guess;
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
    public byte[] getSerializedMessage() { return this.serializedMessage;}

    @Override
    public ICommand fromByteArray(byte[] data) throws IOException, ASAPException {
        return new JoinCommand(data);
    }

    private void serialize() throws IOException {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ASAPSerialization.writeCharSequenceParameter(Character.toString(this.character), baos);
        ASAPSerialization.writeCharSequenceParameter(this.player.toString(), baos);
        this.serializedMessage = baos.toByteArray();
    }

    private void deserialize() throws IOException, ASAPException {

        InputStream is = new ByteArrayInputStream(this.serializedMessage);
        this.character = ASAPSerialization.readCharSequenceParameter(is).charAt(0);
        this.player = UUID.fromString(ASAPSerialization.readCharSequenceParameter(is));
    }
}
