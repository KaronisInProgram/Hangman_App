package de.nvborck.tests.unit.command;

import de.nvborck.hangman.data.game.GameEvent;
import de.nvborck.hangman.command.*;
import de.nvborck.hangman.data.game.Game;
import de.nvborck.hangman.data.game.IGame;
import de.nvborck.hangman.data.game.IPlayerManager;
import de.nvborck.hangman.data.player.IPlayer;
import de.nvborck.hangman.data.player.Player;
import de.nvborck.hangman.data.wordprovider.SimpleWordProvider;
import net.sharksystem.asap.ASAPException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.UUID;

public class GuessCommandTest {

    @Test
    void executionSetsAGuessInTheGame() throws IOException {

        // Arrange
        Game originalGame = new Game();
        IGame game = originalGame;
        IPlayerManager playerManager = originalGame;

        IPlayer player = new Player();
        player.setName("Anna");
        player.setId(UUID.randomUUID());

        playerManager.addPlayer(player);
        game.start("Kuechengeraet");

        ICommand command = new GuessCommand(game, 'e', player);

        // Act
        command.execute();

        // Assert
        Assertions.assertEquals("__e__e__e__e_", game.getMaskedWord());
        Assertions.assertEquals(1, game.getUsedCharacter().size());
        Assertions.assertTrue(game.getUsedCharacter().contains('e'));
        Assertions.assertFalse(game.isFinished());
    }

    @Test
    void aDeserializedCommandHasTheSameEffectAsTheOriginal() throws IOException, ASAPException {

        // Arrange
        Game originalGame1 = new Game();
        IGame game1 = originalGame1;
        IPlayerManager playerManager1 = originalGame1;

        IPlayer player = new Player();
        player.setName("Anna");
        player.setId(UUID.randomUUID());

        playerManager1.addPlayer(player);
        game1.start("Kuechengeraet");

        Game originalGame2 = new Game();
        IGame game2 = originalGame2;
        IPlayerManager playerManager2 = originalGame2;

        playerManager2.addPlayer(player);
        game2.start("Kuechengeraet");

        GuessCommand command = new GuessCommand(game1, 'e', player);

        // Act
        byte[] serializedCommand = command.asSerializableCommand().getSerializedMessage();
        GuessCommand deserializedCommand = new GuessCommand(serializedCommand);

        ICommand settableGame1 = command;
        settableGame1.setCoreObjectIfNull(game2);
        ICommand settableGame2 = deserializedCommand;
        settableGame2.setCoreObjectIfNull(game2);

        command.execute();
        deserializedCommand.execute();

        // Assert
        Assertions.assertEquals("__e__e__e__e_", game1.getMaskedWord());
        Assertions.assertEquals(1, game1.getUsedCharacter().size());
        Assertions.assertTrue(game1.getUsedCharacter().contains('e'));
        Assertions.assertFalse(game1.isFinished());

        Assertions.assertEquals("__e__e__e__e_", game2.getMaskedWord());
        Assertions.assertEquals(1, game2.getUsedCharacter().size());
        Assertions.assertTrue(game2.getUsedCharacter().contains('e'));
        Assertions.assertFalse(game2.isFinished());
    }

    @Test
    void theCorrelatedEventIsWordChanged() throws IOException {

        // Arrange
        IGame game = new Game();
        game.start("Abcdefg");

        IPlayer player = new Player();
        player.setName("Anna");
        player.setId(UUID.randomUUID());

        GuessCommand command = new GuessCommand(game, 'e', player);

        // Act

        // Assert
        Assertions.assertEquals(GameEvent.searchedWordChange, command.getCorrelatedEvent());
    }
}
