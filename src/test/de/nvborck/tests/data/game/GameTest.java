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
        IGame game = new Game();

        // Act
        game.start(provider.getRandomWord());

        // Assert
        Assertions.assertNotNull(game.getMaskedWord());
        Assertions.assertNotEquals("", game.getMaskedWord());
        Assertions.assertTrue(game.getUsedCharacter().isEmpty());
        Assertions.assertFalse(game.isFinished());
    }

    @Test
    void byStartWithPredefinedParamsCorrectInitialization() {

        // Arrange
        IGame game = new Game();

        // Act
        game.start("Test");

        // Assert
        Assertions.assertEquals("____", game.getMaskedWord());
        Assertions.assertTrue(game.getUsedCharacter().isEmpty());
        Assertions.assertFalse(game.isFinished());
    }

    @Test
    void aGuessUpdatesTheMaskedWordByHit() {

        // Arrange
        Game originalGame = new Game();
        IGame game = originalGame;
        IPlayerManager playerManager = originalGame;

        IPlayer player = new Player();
        player.setName("Anna");
        player.setId(UUID.randomUUID());

        // Act
        game.start("Kuechengeraet");

        playerManager.addPlayer(player);
        game.guess('e', player);
        Assertions.assertEquals("__e__e__e__e_", game.getMaskedWord());
        game.guess('g', player);
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
        Game originalGame = new Game();
        IGame game = originalGame;
        IPlayerManager playerManager = originalGame;

        IPlayer player1 = new Player();
        player1.setName("Anna");
        player1.setId(UUID.randomUUID());
        IPlayer player2 = new Player();
        player2.setName("Bob");
        player2.setId(UUID.randomUUID());

        // Act
        game.start("Kuechengeraet");

        playerManager.addPlayer(player1);
        playerManager.addPlayer(player2);

        IPlayer activePlayer1 = playerManager.getActivePlayer();
        game.guess('e', player1);
        IPlayer activePlayer2 = playerManager.getActivePlayer();
        game.guess('d', player2);
        IPlayer activePlayer3 = playerManager.getActivePlayer();

        // Assert
        Assertions.assertNotEquals(activePlayer1.getId(), activePlayer2.getId());
        Assertions.assertNotEquals(activePlayer2.getId(), activePlayer3.getId());
    }

    @Test
    void afterAGuessFromTheLastPlayerTheyLoop() {

        // Arrange
        Game originalGame = new Game();
        IGame game = originalGame;
        IPlayerManager playerManager = originalGame;

        IPlayer player1 = new Player();
        player1.setName("Anna");
        player1.setId(UUID.randomUUID());
        IPlayer player2 = new Player();
        player2.setName("Bob");
        player2.setId(UUID.randomUUID());
        IPlayer player3 = new Player();
        player3.setName("Charlie");
        player3.setId(UUID.randomUUID());

        // Act
        game.start("Kuechengeraet");

        playerManager.addPlayer(player1);
        playerManager.addPlayer(player2);
        playerManager.addPlayer(player3);

        IPlayer activePlayer1 = playerManager.getActivePlayer();
        game.guess('e', player1);
        game.guess('d', player2);
        game.guess('f', player3);
        IPlayer activePlayer4 = playerManager.getActivePlayer();

        // Assert
        Assertions.assertEquals(activePlayer1, activePlayer4);
    }

    @Test
    void aGuessFromAnInactivePlayerIsResultsInNoChange() {

        // Arrange
        Game originalGame = new Game();
        IGame game = originalGame;
        IPlayerManager playerManager = originalGame;

        IPlayer player1 = new Player();
        player1.setName("Anna");
        player1.setId(UUID.randomUUID());
        IPlayer player2 = new Player();
        player2.setName("Bob");
        player2.setId(UUID.randomUUID());

        // Act
        game.start("Kuechengeraet");

        playerManager.addPlayer(player1);
        playerManager.addPlayer(player2);

        boolean guess1 = game.guess('e', player1);

        String maskedWord = game.getMaskedWord();
        List<Character> usedCharacter = game.getUsedCharacter();

        boolean guess2 = game.guess('d', player1);

        // Assert
        Assertions.assertTrue(guess1);
        Assertions.assertFalse(guess2);
        Assertions.assertEquals(player2, playerManager.getActivePlayer());
        Assertions.assertEquals(maskedWord, game.getMaskedWord());
        Assertions.assertIterableEquals(usedCharacter, game.getUsedCharacter());
    }

    @Test
    void aGuessWhenNoPlayerIsAddedResultsInNoChange() {

        // Arrange
        Game originalGame = new Game();
        IGame game = originalGame;
        IPlayerManager playerManager = originalGame;

        IPlayer player1 = new Player();
        player1.setName("Anna");
        player1.setId(UUID.randomUUID());

        // Act
        game.start("Kuechengeraet");

        String maskedWord = game.getMaskedWord();
        List<Character> usedCharacter = game.getUsedCharacter();

        boolean guess1 = game.guess('e', player1);
        boolean guess2 = game.guess('d', player1);

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
        Game originalGame = new Game();
        IGame game = originalGame;
        IPlayerManager playerManager = originalGame;

        IPlayer player1 = new Player();
        player1.setName("Anna");
        player1.setId(UUID.randomUUID());

        // Act
        game.start("Kuechengeraet");

        playerManager.addPlayer(player1);
        IPlayer tempActivePlayer = playerManager.getActivePlayer();
        playerManager.removePlayer(player1);

        // Assert
        Assertions.assertEquals(player1, tempActivePlayer);
        Assertions.assertNull(playerManager.getActivePlayer());
    }

    @Test
    void whenAllCharactersAreGuessedTheGamesIsFinished() {

        // Arrange
        Game originalGame = new Game();
        IGame game = originalGame;
        IPlayerManager playerManager = originalGame;

        IPlayer player1 = new Player();
        player1.setName("Anna");
        player1.setId(UUID.randomUUID());
        IPlayer player2 = new Player();
        player2.setName("Bob");
        player2.setId(UUID.randomUUID());

        String word = "aaaa";

        // Act
        game.start(word);

        playerManager.addPlayer(player1);
        playerManager.addPlayer(player2);

        boolean guess1 = game.guess('a', player1);

        String foundWord = game.getMaskedWord();

        boolean guess2 = game.guess('f', player1);

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
        Game originalGame = new Game();
        IGame game = originalGame;
        IPlayerManager playerManager = originalGame;

        IPlayer player1 = new Player();
        player1.setName("Anna");

        // Act
        game.start("Kuechengeraet");
        playerManager.addPlayer(player1);

        game.guess('a', player1);
        game.guess('e', player1);
        game.guess('f', player1);
        game.guess('d', player1);

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
        Game originalGame = new Game();
        IGame game = originalGame;
        IPlayerManager playerManager = originalGame;

        UUID gameId = UUID.randomUUID();
        IPlayer player1 = new Player();
        player1.setName("Anna");
        player1.setId(UUID.randomUUID());

        // Act
        game.start("Kuechengeraet");
        playerManager.addPlayer(player1);

        game.guess('a', player1);
        game.guess('e', player1);
        game.guess('a', player1);
        game.guess('z', player1);
        game.guess('e', player1);

        // Assert
        Assertions.assertEquals(3, game.getUsedCharacter().size());
        Assertions.assertIterableEquals(Arrays.asList('a', 'e', 'z'), game.getUsedCharacter());
    }
}