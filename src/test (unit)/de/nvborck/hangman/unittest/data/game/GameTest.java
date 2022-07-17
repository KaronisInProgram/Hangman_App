package de.nvborck.hangman.unittest.data.game;

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

        IPlayer player = new Player();
        player.setName("Anna");
        player.setId(UUID.randomUUID());

        // Act
        ((IGame) originalGame).start("Kuechengeraet");

        ((IPlayerManager) originalGame).addPlayer(player);
        ((IGame) originalGame).guess('e', player);
        Assertions.assertEquals("__e__e__e__e_", ((IGame) originalGame).getMaskedWord());
        ((IGame) originalGame).guess('g', player);
        Assertions.assertEquals("__e__e_ge__e_", ((IGame) originalGame).getMaskedWord());

        // Assert
        Assertions.assertTrue(((IGame) originalGame).getUsedCharacter().contains('e'));
        Assertions.assertTrue(((IGame) originalGame).getUsedCharacter().contains('g'));
        Assertions.assertEquals(2, ((IGame) originalGame).getUsedCharacter().size());
        Assertions.assertFalse(((IGame) originalGame).isFinished());
    }

    @Test
    void afterAGuessTheNextPlayerIsActive() {

        // Arrange
        Game originalGame = new Game();

        IPlayer player1 = new Player();
        player1.setName("Anna");
        player1.setId(UUID.randomUUID());
        IPlayer player2 = new Player();
        player2.setName("Bob");
        player2.setId(UUID.randomUUID());

        // Act
        ((IGame) originalGame).start("Kuechengeraet");

        ((IPlayerManager) originalGame).addPlayer(player1);
        ((IPlayerManager) originalGame).addPlayer(player2);

        IPlayer activePlayer1 = ((IPlayerManager) originalGame).getActivePlayer();
        ((IGame) originalGame).guess('e', player1);
        IPlayer activePlayer2 = ((IPlayerManager) originalGame).getActivePlayer();
        ((IGame) originalGame).guess('d', player2);
        IPlayer activePlayer3 = ((IPlayerManager) originalGame).getActivePlayer();

        // Assert
        Assertions.assertNotEquals(activePlayer1.getId(), activePlayer2.getId());
        Assertions.assertNotEquals(activePlayer2.getId(), activePlayer3.getId());
    }

    @Test
    void afterAGuessFromTheLastPlayerTheyLoop() {

        // Arrange
        Game originalGame = new Game();

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
        ((IGame) originalGame).start("Kuechengeraet");

        ((IPlayerManager) originalGame).addPlayer(player1);
        ((IPlayerManager) originalGame).addPlayer(player2);
        ((IPlayerManager) originalGame).addPlayer(player3);

        IPlayer activePlayer1 = ((IPlayerManager) originalGame).getActivePlayer();
        ((IGame) originalGame).guess('e', player1);
        ((IGame) originalGame).guess('d', player2);
        ((IGame) originalGame).guess('f', player3);
        IPlayer activePlayer4 = ((IPlayerManager) originalGame).getActivePlayer();

        // Assert
        Assertions.assertEquals(activePlayer1, activePlayer4);
    }

    @Test
    void aGuessFromAnInactivePlayerIsResultsInNoChange() {

        // Arrange
        Game originalGame = new Game();

        IPlayer player1 = new Player();
        player1.setName("Anna");
        player1.setId(UUID.randomUUID());
        IPlayer player2 = new Player();
        player2.setName("Bob");
        player2.setId(UUID.randomUUID());

        // Act
        ((IGame) originalGame).start("Kuechengeraet");

        ((IPlayerManager) originalGame).addPlayer(player1);
        ((IPlayerManager) originalGame).addPlayer(player2);

        boolean guess1 = ((IGame) originalGame).guess('e', player1);

        String maskedWord = ((IGame) originalGame).getMaskedWord();
        List<Character> usedCharacter = ((IGame) originalGame).getUsedCharacter();

        boolean guess2 = ((IGame) originalGame).guess('d', player1);

        // Assert
        Assertions.assertTrue(guess1);
        Assertions.assertFalse(guess2);
        Assertions.assertEquals(player2, ((IPlayerManager) originalGame).getActivePlayer());
        Assertions.assertEquals(maskedWord, ((IGame) originalGame).getMaskedWord());
        Assertions.assertIterableEquals(usedCharacter, ((IGame) originalGame).getUsedCharacter());
    }

    @Test
    void aGuessWhenNoPlayerIsAddedResultsInNoChange() {

        // Arrange
        Game originalGame = new Game();

        IPlayer player1 = new Player();
        player1.setName("Anna");
        player1.setId(UUID.randomUUID());

        // Act
        ((IGame) originalGame).start("Kuechengeraet");

        String maskedWord = ((IGame) originalGame).getMaskedWord();
        List<Character> usedCharacter = ((IGame) originalGame).getUsedCharacter();

        boolean guess1 = ((IGame) originalGame).guess('e', player1);
        boolean guess2 = ((IGame) originalGame).guess('d', player1);

        // Assert
        Assertions.assertFalse(guess1);
        Assertions.assertFalse(guess2);
        Assertions.assertNull(((IPlayerManager) originalGame).getActivePlayer());
        Assertions.assertEquals(maskedWord, ((IGame) originalGame).getMaskedWord());
        Assertions.assertIterableEquals(usedCharacter, ((IGame) originalGame).getUsedCharacter());
    }

    @Test
    void aPlayerIsCorrectRemovedFormTheGame() {

        // Arrange
        Game originalGame = new Game();

        IPlayer player1 = new Player();
        player1.setName("Anna");
        player1.setId(UUID.randomUUID());

        // Act
        ((IGame) originalGame).start("Kuechengeraet");

        ((IPlayerManager) originalGame).addPlayer(player1);
        IPlayer tempActivePlayer = ((IPlayerManager) originalGame).getActivePlayer();
        ((IPlayerManager) originalGame).removePlayer(player1);

        // Assert
        Assertions.assertEquals(player1, tempActivePlayer);
        Assertions.assertNull(((IPlayerManager) originalGame).getActivePlayer());
    }

    @Test
    void whenAllCharactersAreGuessedTheGamesIsFinished() {

        // Arrange
        Game originalGame = new Game();

        IPlayer player1 = new Player();
        player1.setName("Anna");
        player1.setId(UUID.randomUUID());
        IPlayer player2 = new Player();
        player2.setName("Bob");
        player2.setId(UUID.randomUUID());

        String word = "aaaa";

        // Act
        ((IGame) originalGame).start(word);

        ((IPlayerManager) originalGame).addPlayer(player1);
        ((IPlayerManager) originalGame).addPlayer(player2);

        boolean guess1 = ((IGame) originalGame).guess('a', player1);

        String foundWord = ((IGame) originalGame).getMaskedWord();

        boolean guess2 = ((IGame) originalGame).guess('f', player1);

        // Assert
        Assertions.assertTrue(((IGame) originalGame).isFinished());
        Assertions.assertTrue(guess1);
        Assertions.assertFalse(guess2);
        Assertions.assertEquals(word, foundWord);
        Assertions.assertTrue(((IGame) originalGame).getUsedCharacter().contains('a'));
        Assertions.assertFalse(((IGame) originalGame).getUsedCharacter().contains('f'));
        Assertions.assertEquals(1, ((IGame) originalGame).getUsedCharacter().size());
    }

    @Test
    void aDirectChangeOnTheUsedCharacterListHasNoEffectOnInternList() {

        // Arrange
        Game originalGame = new Game();

        IPlayer player1 = new Player();
        player1.setName("Anna");

        // Act
        ((IGame) originalGame).start("Kuechengeraet");
        ((IPlayerManager) originalGame).addPlayer(player1);

        ((IGame) originalGame).guess('a', player1);
        ((IGame) originalGame).guess('e', player1);
        ((IGame) originalGame).guess('f', player1);
        ((IGame) originalGame).guess('d', player1);

        var list = ((IGame) originalGame).getUsedCharacter();
        list.add('b');
        list.add('g');
        list.add('x');

        // Assert
        Assertions.assertNotEquals(((IGame) originalGame).getUsedCharacter(), list);
        Assertions.assertEquals(4, ((IGame) originalGame).getUsedCharacter().size());
        Assertions.assertEquals(7, list.size());
    }

    @Test
    void everyCharacterIsOnlyTrackedOnce() {

        // Arrange
        Game originalGame = new Game();

        UUID gameId = UUID.randomUUID();
        IPlayer player1 = new Player();
        player1.setName("Anna");
        player1.setId(UUID.randomUUID());

        // Act
        ((IGame) originalGame).start("Kuechengeraet");
        ((IPlayerManager) originalGame).addPlayer(player1);

        ((IGame) originalGame).guess('a', player1);
        ((IGame) originalGame).guess('e', player1);
        ((IGame) originalGame).guess('a', player1);
        ((IGame) originalGame).guess('z', player1);
        ((IGame) originalGame).guess('e', player1);

        // Assert
        Assertions.assertEquals(3, ((IGame) originalGame).getUsedCharacter().size());
        Assertions.assertIterableEquals(Arrays.asList('a', 'e', 'z'), ((IGame) originalGame).getUsedCharacter());
    }
}