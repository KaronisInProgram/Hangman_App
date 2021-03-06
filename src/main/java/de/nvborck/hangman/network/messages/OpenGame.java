package de.nvborck.hangman.network.messages;

import net.sharksystem.asap.ASAPException;
import net.sharksystem.asap.utils.ASAPSerialization;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

public class OpenGame {

    private UUID id;
    private String name;

    private byte[] serializedMessage;

    public OpenGame(UUID id, String name) throws IOException {
        this.id = id;
        this.name = name;

        this.serialize();
    }

    public OpenGame(byte[] serializedMessage) throws IOException, ASAPException {
        this.serializedMessage = serializedMessage;
        this.deserialize();
    }

    public UUID getId() {
        return this.id;
    }

    public String getWord() {
        return this.name;
    }

    private void serialize() throws IOException {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ASAPSerialization.writeCharSequenceParameter(this.name, baos);
        ASAPSerialization.writeCharSequenceParameter(this.id.toString(), baos);
        this.serializedMessage = baos.toByteArray();
    }

    private void deserialize() throws IOException, ASAPException {

        InputStream is = new ByteArrayInputStream(this.serializedMessage);
        this.name = ASAPSerialization.readCharSequenceParameter(is);
        this.id = UUID.fromString(ASAPSerialization.readCharSequenceParameter(is));
    }

    public byte[] getSerializedMessage() {
        return this.serializedMessage;
    }

}
