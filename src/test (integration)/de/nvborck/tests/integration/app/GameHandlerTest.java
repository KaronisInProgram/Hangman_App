package de.nvborck.tests.integration.app;

import de.nvborck.hangman.app.GameHandler;
import de.nvborck.hangman.app.IGameHandler;
import de.nvborck.hangman.data.player.IPlayer;
import de.nvborck.hangman.data.player.Player;
import de.nvborck.hangman.data.wordprovider.SimpleWordProvider;
import de.nvborck.hangman.network.IGameAPI;
import de.nvborck.hangman.network.messages.OpenGame;
import net.sharksystem.asap.ASAPException;
import net.sharksystem.asap.apps.testsupport.ASAPTestPeerFS;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;


public class GameHandlerTest {

    static final CharSequence ALICE = "Alice";
    static final CharSequence BOB = "Bob";
    static final CharSequence CHARLIE = "Charlie";

    @Test
    void afterGuessesFromAllPlayerTheGamesAreSynchronized() throws IOException, ASAPException, InterruptedException {

        Collection<CharSequence> formats = new ArrayList<>();
        formats.add(IGameAPI.APP_FORMAT);
        ASAPTestPeerFS alicePeer = new ASAPTestPeerFS(ALICE, formats);
        ASAPTestPeerFS bobPeer = new ASAPTestPeerFS(BOB, formats);

        IGameHandler handlerAlice = new GameHandler(alicePeer, new SimpleWordProvider(), UUID.randomUUID());
        IGameHandler handlerBob = new GameHandler(bobPeer, new SimpleWordProvider(), UUID.randomUUID());

        IPlayer alicePlayer = new Player();
        alicePlayer.setName(ALICE.toString());
        IPlayer bobPlayer = new Player();
        bobPlayer.setName(BOB.toString());

        // Arrange
        alicePeer.startEncounter(de.nvborck.tests.integration.app.PortHelper.getPort(), bobPeer);
        handlerBob.initializeGame(bobPlayer);
        Thread.sleep(2000);

        // Act
        handlerAlice.searchGames();
        Thread.sleep(2000);

        OpenGame openGame = handlerAlice.getOpenGames().get(0);
        handlerAlice.joinGame(openGame.getId(), alicePlayer);
        Thread.sleep(3000);

        handlerBob.guess('e', bobPlayer);
        Thread.sleep(1000);
        handlerAlice.guess('a', alicePlayer);
        Thread.sleep(1000);
        handlerBob.guess('d', bobPlayer);
        Thread.sleep(1000);

        // Assert
        Assertions.assertEquals(handlerBob.getActivePlayer(), handlerAlice.getActivePlayer());
        Assertions.assertIterableEquals(handlerBob.getUsedCharacter(), handlerAlice.getUsedCharacter());
        Assertions.assertEquals(handlerBob.getMaskedWord(), handlerAlice.getMaskedWord());

    }

    @Test
    void searchingAsksNearbyDevicesIfTheyHaveAnOpenGame() throws IOException, ASAPException, InterruptedException {

        Collection<CharSequence> formats = new ArrayList<>();
        formats.add(IGameAPI.APP_FORMAT);
        ASAPTestPeerFS alicePeer = new ASAPTestPeerFS(ALICE, formats);
        ASAPTestPeerFS bobPeer = new ASAPTestPeerFS(BOB, formats);
        ASAPTestPeerFS charliePeer = new ASAPTestPeerFS(CHARLIE, formats);

        IGameHandler handlerAlice = new GameHandler(alicePeer, new SimpleWordProvider(), UUID.randomUUID());
        IGameHandler handlerBob = new GameHandler(bobPeer, new SimpleWordProvider(), UUID.randomUUID());
        IGameHandler handlerCharlie = new GameHandler(charliePeer, new SimpleWordProvider(), UUID.randomUUID());

        IPlayer alicePlayer = new Player();
        alicePlayer.setName(ALICE.toString());
        IPlayer bobPlayer = new Player();
        bobPlayer.setName(BOB.toString());

        // Arrange
        handlerAlice.initializeGame(alicePlayer);
        handlerBob.initializeGame(bobPlayer);

        charliePeer.startEncounter(de.nvborck.tests.integration.app.PortHelper.getPort(), alicePeer);
        charliePeer.startEncounter(de.nvborck.tests.integration.app.PortHelper.getPort(), bobPeer);

        Thread.sleep(500);

        // Act
        handlerCharlie.searchGames();

        Thread.sleep(2000);

        // Assert
        Assertions.assertEquals(2, handlerCharlie.getOpenGames().size());
    }

    @Test
    void joiningAnOpenGameSynchronizesTheGame() throws IOException, ASAPException, InterruptedException {

        Collection<CharSequence> formats = new ArrayList<>();
        formats.add(IGameAPI.APP_FORMAT);
        ASAPTestPeerFS alicePeer = new ASAPTestPeerFS(ALICE, formats);
        ASAPTestPeerFS bobPeer = new ASAPTestPeerFS(BOB, formats);

        IGameHandler handlerAlice = new GameHandler(alicePeer, new SimpleWordProvider(), UUID.randomUUID());
        IGameHandler handlerBob = new GameHandler(bobPeer, new SimpleWordProvider(), UUID.randomUUID());

        IPlayer alicePlayer = new Player();
        alicePlayer.setName(ALICE.toString());
        IPlayer bobPlayer = new Player();
        bobPlayer.setName(BOB.toString());

        // Arrange
        handlerAlice.initializeGame(alicePlayer);
        alicePeer.startEncounter(PortHelper.getPort(), bobPeer);
        Thread.sleep(1000);

        // Act
        handlerBob.searchGames();
        Thread.sleep(2000);

        OpenGame openGame = handlerBob.getOpenGames().get(0);
        handlerBob.joinGame(openGame.getId(), bobPlayer);
        Thread.sleep(2000);

        // Assert
        Assertions.assertEquals(handlerAlice.getGameId(), handlerBob.getGameId());
        Assertions.assertEquals(handlerAlice.getSearchedWord(), handlerBob.getSearchedWord());
        Assertions.assertEquals(alicePlayer, handlerAlice.getActivePlayer());
        Assertions.assertEquals(alicePlayer, handlerBob.getActivePlayer());
        Assertions.assertEquals(handlerAlice.getMaskedWord(), handlerBob.getMaskedWord());
        Assertions.assertIterableEquals(handlerAlice.getUsedCharacter(), handlerBob.getUsedCharacter());
    }
}
