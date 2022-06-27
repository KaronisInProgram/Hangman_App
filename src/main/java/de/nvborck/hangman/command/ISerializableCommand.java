package de.nvborck.hangman.command;

import net.sharksystem.asap.ASAPException;

import java.io.IOException;

public interface ISerializableCommand {

    byte[] getSerializedMessage();

    ICommand fromByteArray(byte[] data) throws IOException, ASAPException;
}
