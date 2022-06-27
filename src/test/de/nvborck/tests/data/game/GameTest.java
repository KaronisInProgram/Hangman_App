package de.nvborck.tests.data.game;

import de.nvborck.hangman.data.game.*;
import de.nvborck.hangman.data.player.IPlayer;
import de.nvborck.hangman.data.player.Player;
import de.nvborck.hangman.data.wordprovider.IWordProvider;
import de.nvborck.hangman.data.wordprovider.SimpleWordProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class GameTest {

    IWordProvider provider = new SimpleWordProvider();

    @Test
    void byStartWithoutPredefinedParamsCorrectInitialization() {

        // Arrange
        IGame game = new Game(provider);

        // Act
        game.start();

        // Assert
        Assertions.assertNotNull(game.getMaskedWord());
        Assertions.assertNotEquals("", game.getMaskedWord());
        Assertions.assertNotNull(game.getId());
        Assertions.assertTrue(game.getUsedCharacter().isEmpty());
        Assertions.assertFalse(game.isFinished());
    }

    @Test
    void byStartWithPredefinedParamsCorrectInitialization() {

        // Arrange
        IGame game = new Game(provider);
        UUID id = UUID.randomUUID();

        // Act
        game.start("Test", id);

        // Assert
        Assertions.assertEquals("____", game.getMaskedWord());
        Assertions.assertEquals(id, game.getId());
        Assertions.assertTrue(game.getUsedCharacter().isEmpty());
        Assertions.assertFalse(game.isFinished());
    }

    @Test
    void aGuessUpdatesTheMaskedWordByHit() {

        // Arrange
        Game originalGame = new Game(provider);
        IGame game = originalGame;
        IPlayerManager playerManager = originalGame;

        UUID gameId = UUID.randomUUID();
        UUID playerId = UUID.randomUUID();
        String playerName = "Anna";

        // Act
        game.start("Kuechengeraet", gameId);

        playerManager.addPlayer(playerId, playerName);
        game.guess('e', playerId);
        Assertions.assertEquals("__e__e__e__e_", game.getMaskedWord());
        game.guess('g', playerId);
        Assertions.assertEquals("__e__e_ge__e_", game.getMaskedWord());

        // Assert
        Assertions.assertTrue(game.getUsedCharacter().contains('e'));
        Assertions.assertTrue(game.getUsedCharacter().contains('g'));
        Assertions.assertEquals(2, game.getUsedCharacter().size());
        Assertions.assertFalse(game.isFinished());
    }

    @Test
    void afterAGuessTheNextPlayerIsActive() {

        // Arrange
        Game originalGame = new Game(provider);
        IGame game = originalGame;
        IPlayerManager playerManager = originalGame;

        UUID gameId = UUID.randomUUID();

        UUID player1Id = UUID.randomUUID();
        String player1Name = "Anna";
        UUID player2Id = UUID.randomUUID();
        String player2Name = "Bob";

        // Act
        game.start("Kuechengeraet", gameId);

        playerManager.addPlayer(player1Id, player1Name);
        playerManager.addPlayer(player2Id, player2Name);

        IPlayer player1 = playerManager.getActivePlayer();
        game.guess('e', player1Id);
        IPlayer player2 = playerManager.getActivePlayer();
        game.guess('d', player2Id);
        IPlayer player3 = playerManager.getActivePlayer();

        // Assert
        Assertions.assertNotEquals(player1.getId(), player2.getId());
        Assertions.assertNotEquals(player2.getId(), player3.getId());
    }

    @Test
    void afterAGuessFromTheLastPlayerTheyLoop() {

        // Arrange
        Game originalGame = new Game(provider);
        IGame game = originalGame;
        IPlayerManager playerManager = originalGame;

        UUID gameId = UUID.randomUUID();

        UUID player1Id = UUID.randomUUID();
        String player1Name = "Anna";
        UUID player2Id = UUID.randomUUID();
        String player2Name = "Bob";
        UUID player3Id = UUID.randomUUID();
        String player3Name = "Chander";

        // Act
        game.start("Kuechengeraet", gameId);

        playerManager.addPlayer(player1Id, player1Name);
        playerManager.addPlayer(player2Id, player2Name);
        playerManager.addPlayer(player3Id, player3Name);

        IPlayer player1 = playerManager.getActivePlayer();
        game.guess('e', player1Id);
        game.guess('d', player2Id);
        game.guess('f', player3Id);
        IPlayer player4 = playerManager.getActivePlayer();

        // Assert
        Assertions.assertEquals(player1.getId(), player4.getId());
    }

    @Test
    void aGuessFromAnInactivePlayerIsResultsInNoChange() {

        // Arrange
        Game originalGame = new Game(provider);
        IGame game = originalGame;
        IPlayerManager playerManager = originalGame;

        UUID gameId = UUID.randomUUID();

        UUID player1Id = UUID.randomUUID();
        String player1Name = "Anna";
        UUID player2Id = UUID.randomUUID();
        String player2Name = "Bob";

        // Act
        game.start("Kuechengeraet", gameId);

        playerManager.addPlayer(player1Id, player1Name);
        playerManager.addPlayer(player2Id, player2Name);

        boolean guess1 = game.guess('e', player1Id);

        String maskedWord = game.getMaskedWord();
        List<Character> usedCharacter = game.getUsedCharacter();

        boolean guess2 = game.guess('d', player1Id);

        // Assert
        Assertions.assertTrue(guess1);
        Assertions.assertFalse(guess2);
        Assertions.assertEquals(player2Id, playerManager.getActivePlayer().getId());
        Assertions.assertEquals(maskedWord, game.getMaskedWord());
        Assertions.assertIterableEquals(usedCharacter, game.getUsedCharacter());
    }

    @Test
    void aGuessWhenNoPlayerIsAddedResultsInNoChange() {

        // Arrange
        Game originalGame = new Game(provider);
        IGame game = originalGame;
        IPlayerManager playerManager = originalGame;

        UUID gameId = UUID.randomUUID();

        UUID player1Id = UUID.randomUUID();

        // Act
        game.start("Kuechengeraet", gameId);

        String maskedWord = game.getMaskedWord();
        List<Character> usedCharacter = game.getUsedCharacter();

        boolean guess1 = game.guess('e', player1Id);
        boolean guess2 = game.guess('d', player1Id);

        // Assert
        Assertions.assertFalse(guess1);
        Assertions.assertFalse(guess2);
        Assertions.assertNull(playerManager.getActivePlayer());
        Assertions.assertEquals(maskedWord, game.getMaskedWord());
        Assertions.assertIterableEquals(usedCharacter, game.getUsedCharacter());
    }

    @Test
    void aPlayerIsCorrectRemovedFormTheGame() {

        // Arrange
        Game originalGame = new Game(provider);
        IGame game = originalGame;
        IPlayerManager playerManager = originalGame;

        UUID gameId = UUID.randomUUID();
        UUID player1Id = UUID.randomUUID();

        // Act
        game.start("Kuechengeraet", gameId);

        playerManager.addPlayer(player1Id, "Anna");
        IPlayer tempActivePlayer = playerManager.getActivePlayer();
        playerManager.removePlayer(player1Id);

        // Assert
        Assertions.assertEquals(player1Id, tempActivePlayer.getId());
        Assertions.assertNull(playerManager.getActivePlayer());
    }

    @Test
    void whenAllCharactersAreGuessedTheGamesIsFinished() {

        // Arrange
        Game originalGame = new Game(provider);
        IGame game = originalGame;
        IPlayerManager playerManager = originalGame;

        UUID gameId = UUID.randomUUID();

        UUID player1Id = UUID.randomUUID();
        String player1Name = "Anna";
        UUID player2Id = UUID.randomUUID();
        String player2Name = "Bob";

        String word = "aaaa";

        // Act
        game.start(word, gameId);

        playerManager.addPlayer(player1Id, player1Name);
        playerManager.addPlayer(player2Id, player2Name);

        boolean guess1 = game.guess('a', player1Id);

        String foundWord = game.getMaskedWord();

        boolean guess2 = game.guess('f', player1Id);

        // Assert
        Assertions.assertTrue(game.isFinished());
        Assertions.assertTrue(guess1);
        Assertions.assertFalse(guess2);
        Assertions.assertEquals(word, foundWord);
        Assertions.assertTrue(game.getUsedCharacter().contains('a'));
        Assertions.assertFalse(game.getUsedCharacter().contains('f'));
        Assertions.assertEquals(1, game.getUsedCharacter().size());
    }

    @Test
    void aDirectChangeOnTheUsedCharacterListHasNoEffectOnInternList() {

        // Arrange
        Game originalGame = new Game(provider);
        IGame game = originalGame;
        IPlayerManager playerManager = originalGame;

        UUID gameId = UUID.randomUUID();
        UUID player1Id = UUID.randomUUID();

        // Act
        game.start("Kuechengeraet", gameId);
        playerManager.addPlayer(player1Id, "Anna");

        game.guess('a', player1Id);
        game.guess('e', player1Id);
        game.guess('f', player1Id);
        game.guess('d', player1Id);

        var list = game.getUsedCharacter();
        list.add('b');
        list.add('g');
        list.add('x');

        // Assert
        Assertions.assertNotEquals(game.getUsedCharacter(), list);
        Assertions.assertEquals(4, game.getUsedCharacter().size());
        Assertions.assertEquals(7, list.size());
    }

    @Test
    void everyCharacterIsOnlyTrackedOnce() {

        // Arrange
        Game originalGame = new Game(provider);
        IGame game = originalGame;
        IPlayerManager playerManager = originalGame;

        UUID gameId = UUID.randomUUID();
        UUID player1Id = UUID.randomUUID();

        // Act
        game.start("Kuechengeraet", gameId);
        playerManager.addPlayer(player1Id, "Anna");

        game.guess('a', player1Id);
        game.guess('e', player1Id);
        game.guess('a', player1Id);
        game.guess('z', player1Id);
        game.guess('e', player1Id);

        // Assert
        Assertions.assertEquals(3, game.getUsedCharacter().size());
        Assertions.assertIterableEquals(Arrays.asList('a', 'e', 'z'), game.getUsedCharacter());
    }
}