package de.nvborck.tests.network;


import de.nvborck.hangman.app.GameHandler;
import de.nvborck.hangman.app.IGameHandler;
import de.nvborck.hangman.data.player.IPlayer;
import de.nvborck.hangman.data.player.Player;
import de.nvborck.hangman.data.wordprovider.SimpleWordProvider;
import de.nvborck.hangman.network.GameAPI;
import de.nvborck.hangman.network.IGameAPI;
import net.sharksystem.asap.ASAPException;
import net.sharksystem.asap.apps.testsupport.ASAPTestPeerFS;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

public class GameAPITest {

    static final CharSequence ALICE = "Alice";
    static final CharSequence BOB = "Bob";

    @Test
    void searchOfGamesReturnsAvailableGameIds() throws IOException, ASAPException {

        // Arrange
        IGameAPI api = null;

        // Act
        api.searchGames();

        // Assert
        Assertions.fail("Has To be implemented and add Assertions");
    }

    @Test
    void joinAnExistingGame() {

        // Arrange
        IGameAPI api = null;

        // Act
        api.joinGame(null);

        // Assert
        Assertions.fail("Has To be implemented and add Assertions");
    }

    @Test
    void sendCommandToConnectedGames() throws IOException, ASAPException {

        // Arrange
        IGameAPI api = null;

        // Act
        api.sendCommand(null,  null);

        // Assert
        Assertions.fail("Has To be implemented and add Assertions");
    }


    @Test
    void test() throws IOException, ASAPException, InterruptedException {

        Collection<CharSequence> formats = new ArrayList<>();
        formats.add(IGameAPI.APP_FORMAT);
        ASAPTestPeerFS alicePeer = new ASAPTestPeerFS(ALICE, formats);
        ASAPTestPeerFS bobPeer = new ASAPTestPeerFS(BOB, formats);

        IGameHandler handlerAlice = new GameHandler(alicePeer, new SimpleWordProvider());
        IGameHandler handlerBob = new GameHandler(bobPeer, new SimpleWordProvider());

        IPlayer alicePlayer = new Player();
        alicePlayer.setName(ALICE.toString());
        IPlayer bobPlayer = new Player();
        bobPlayer.setName(BOB.toString());

        // Arrange
        alicePeer.startEncounter(7777, bobPeer);

        Thread.sleep(500);

        handlerBob.initializeGame(bobPlayer);

        // Act
        handlerAlice.searchGames();

        Thread.sleep(5000);

        // Assert
        Assertions.assertEquals(1, handlerAlice.getOpenGames().size());
    }
}
