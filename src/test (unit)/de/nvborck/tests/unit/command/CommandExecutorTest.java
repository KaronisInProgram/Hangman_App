package de.nvborck.tests.unit.command;

import de.nvborck.hangman.command.*;
import de.nvborck.hangman.data.game.Game;
import de.nvborck.hangman.data.game.IGame;
import de.nvborck.hangman.data.game.IPlayerManager;
import de.nvborck.hangman.data.player.IPlayer;
import de.nvborck.hangman.data.player.Player;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.UUID;

public class CommandExecutorTest {

    @Test
    void executionEffectsTargetObject() throws IOException {

        // Arrange
        Game originalGame = new Game();

        IPlayer player = new Player();
        player.setName("Anna");
        player.setId(UUID.randomUUID());

        ((IPlayerManager) originalGame).addPlayer(player);
        ((IGame) originalGame).start("Kuechengeraet");

        ICommandExecutor executor = new CommandExecutor();
        ICommand command = new GuessCommand(originalGame, 'e', player);

        // Act
        executor.executeCommand(command);

        // Assert
        Assertions.assertEquals("__e__e__e__e_", ((IGame) originalGame).getMaskedWord());
        Assertions.assertEquals(1, ((IGame) originalGame).getUsedCharacter().size());
        Assertions.assertTrue(((IGame) originalGame).getUsedCharacter().contains('e'));
        Assertions.assertFalse(((IGame) originalGame).isFinished());
    }

    @Test
    void ifNoCommandIsProvidedTheExecutorThrowsAnError() {

        // Arrange
        ICommandExecutor executor = new CommandExecutor();

        // Act
        var exception = Assertions.assertThrows(IllegalArgumentException.class, () -> executor.executeCommand(null));

        // Assert
        Assertions.assertEquals("The command can't be null!", exception.getMessage());
    }

    @Test
    void theCommandListIsNotChangeable() throws IOException {

        // Arrange
        ICommandExecutor executor = new CommandExecutor();

        // Act
        executor.getCommands().add(new StartCommand(new Game(), "Hello"));

        // Assert
        Assertions.assertEquals(0, executor.getCommands().size());
    }
}
