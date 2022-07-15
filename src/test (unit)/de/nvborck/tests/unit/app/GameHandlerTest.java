package de.nvborck.tests.unit.app;

import de.nvborck.hangman.app.GameHandler;
import de.nvborck.hangman.app.IGameHandler;
import de.nvborck.hangman.data.player.IPlayer;
import de.nvborck.hangman.data.player.Player;
import de.nvborck.hangman.data.wordprovider.SimpleWordProvider;
import de.nvborck.hangman.network.IGameAPI;
import de.nvborck.hangman.network.messages.OpenGame;
import de.nvborck.tests.integration.app.PortHelper;
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

    @Test
    void initializingOfAGGameSetsItAsTheActiveGame() throws IOException, ASAPException {

        Collection<CharSequence> formats = new ArrayList<>();
        formats.add(IGameAPI.APP_FORMAT);
        ASAPTestPeerFS alicePeer = new ASAPTestPeerFS(ALICE, formats);

        IPlayer alicePlayer = new Player();
        alicePlayer.setName(ALICE.toString());

        // Arrange
        IGameHandler handlerAlice = new GameHandler(alicePeer, new SimpleWordProvider(), UUID.randomUUID());

        // Act
        handlerAlice.initializeGame(alicePlayer);

        // Assert
        Assertions.assertTrue(handlerAlice.hasActiveGame());
    }

    @Test
    void initializingAGameWhenThereIsAnActiveGameOverridesFirstGame() throws IOException, ASAPException {

        Collection<CharSequence> formats = new ArrayList<>();
        formats.add(IGameAPI.APP_FORMAT);
        ASAPTestPeerFS alicePeer = new ASAPTestPeerFS(ALICE, formats);

        IPlayer alicePlayer = new Player();
        alicePlayer.setName(ALICE.toString());

        // Arrange
        IGameHandler handlerAlice = new GameHandler(alicePeer, new SimpleWordProvider(), UUID.randomUUID());

        // Act
        handlerAlice.initializeGame(alicePlayer);
        UUID gameId1 = handlerAlice.getGameId();
        handlerAlice.initializeGame(alicePlayer);
        UUID gameId2 = handlerAlice.getGameId();

        // Assert
        Assertions.assertNotEquals(gameId1, gameId2);
    }
}
