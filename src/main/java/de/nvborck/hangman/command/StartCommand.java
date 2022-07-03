package de.nvborck.hangman.command;

import de.nvborck.hangman.data.game.GameEvent;
import de.nvborck.hangman.data.game.IGame;
import net.sharksystem.asap.ASAPException;
import net.sharksystem.asap.utils.ASAPSerialization;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

public class StartCommand implements ICommand<IGame>, ISerializableCommand {
    private IGame game;
    private String word;

    private byte[] serializedMessage;

    public StartCommand(IGame game, String word) throws IOException {

        this.game = game;
        this.word = word;
        this.serialize();
    }

    public StartCommand(byte[] serializedMessage) throws IOException, ASAPException {
        this.serializedMessage = serializedMessage;
        this.deserialize();
    }

    @Override
    public void execute() {
        this.game.start(this.word);
    }

    @Override
    public GameEvent getCorrelatedEvent() {
        return GameEvent.searchedWordChange;
    }

    @Override
    public CommandType getCorrelatedType() {
        return CommandType.start;
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


    private void serialize() throws IOException {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ASAPSerialization.writeCharSequenceParameter(this.word, baos);
        this.serializedMessage = baos.toByteArray();
    }

    private void deserialize() throws IOException, ASAPException {

        InputStream is = new ByteArrayInputStream(this.serializedMessage);
        this.word = ASAPSerialization.readCharSequenceParameter(is);
    }
}
