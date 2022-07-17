package de.nvborck.hangman.unittest.command;

import de.nvborck.hangman.data.game.GameEvent;
import de.nvborck.hangman.command.*;
import de.nvborck.hangman.data.game.Game;
import de.nvborck.hangman.data.game.IGame;
import de.nvborck.hangman.data.game.IPlayerManager;
import de.nvborck.hangman.data.player.IPlayer;
import de.nvborck.hangman.data.player.Player;
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

        IPlayer player = new Player();
        player.setName("Anna");
        player.setId(UUID.randomUUID());

        ((IPlayerManager) originalGame).addPlayer(player);
        ((IGame) originalGame).start("Kuechengeraet");

        ICommand command = new GuessCommand(originalGame, 'e', player);

        // Act
        command.execute();

        // Assert
        Assertions.assertEquals("__e__e__e__e_", ((IGame) originalGame).getMaskedWord());
        Assertions.assertEquals(1, ((IGame) originalGame).getUsedCharacter().size());
        Assertions.assertTrue(((IGame) originalGame).getUsedCharacter().contains('e'));
        Assertions.assertFalse(((IGame) originalGame).isFinished());
    }

    @Test
    void aDeserializedCommandHasTheSameEffectAsTheOriginal() throws IOException, ASAPException {

        // Arrange
        Game originalGame1 = new Game();

        IPlayer player = new Player();
        player.setName("Anna");
        player.setId(UUID.randomUUID());

        ((IPlayerManager) originalGame1).addPlayer(player);
        ((IGame) originalGame1).start("Kuechengeraet");

        Game originalGame2 = new Game();

        ((IPlayerManager) originalGame2).addPlayer(player);
        ((IGame) originalGame2).start("Kuechengeraet");

        GuessCommand command = new GuessCommand(originalGame1, 'e', player);

        // Act
        byte[] serializedCommand = command.asSerializableCommand().getSerializedMessage();
        GuessCommand deserializedCommand = new GuessCommand(serializedCommand);

        ((ICommand) command).setCoreObjectIfNull(originalGame2);
        ((ICommand) deserializedCommand).setCoreObjectIfNull(originalGame2);

        command.execute();
        deserializedCommand.execute();

        // Assert
        Assertions.assertEquals("__e__e__e__e_", ((IGame) originalGame1).getMaskedWord());
        Assertions.assertEquals(1, ((IGame) originalGame1).getUsedCharacter().size());
        Assertions.assertTrue(((IGame) originalGame1).getUsedCharacter().contains('e'));
        Assertions.assertFalse(((IGame) originalGame1).isFinished());

        Assertions.assertEquals("__e__e__e__e_", ((IGame) originalGame2).getMaskedWord());
        Assertions.assertEquals(1, ((IGame) originalGame2).getUsedCharacter().size());
        Assertions.assertTrue(((IGame) originalGame2).getUsedCharacter().contains('e'));
        Assertions.assertFalse(((IGame) originalGame2).isFinished());
    }

    @Test
    void theCorrelatedInformationAreCorrect() throws IOException {

        // Arrange
        IGame game = new Game();
        game.start("Abcdefg");

        IPlayer player = new Player();
        player.setName("Anna");
        player.setId(UUID.randomUUID());

        GuessCommand command = new GuessCommand(game, 'e', player);

        // Act

        // Assert
        Assertions.assertNotNull(command.getUniqeId());
        Assertions.assertEquals(GameEvent.searchedWordChange, command.getCorrelatedEvent());
        Assertions.assertEquals(CommandType.guess, command.getCorrelatedType());
    }
}
