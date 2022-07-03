package de.nvborck.hangman.command;

import net.sharksystem.asap.ASAPException;

import java.io.IOException;

public interface ISerializableCommand<T> {

    byte[] getSerializedMessage();
}
