package de.nvborck.hangman.app;

import net.sharksystem.asap.ASAPException;

import java.io.IOException;

public interface IGameListener {

    void getNotified() throws IOException, ASAPException;
}
