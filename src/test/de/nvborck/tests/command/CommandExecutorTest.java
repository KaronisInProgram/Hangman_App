package de.nvborck.tests.command;

import de.nvborck.hangman.command.CommandExecutor;
import de.nvborck.hangman.command.GuessCommand;
import de.nvborck.hangman.command.ICommand;
import de.nvborck.hangman.command.ICommandExecutor;
import de.nvborck.hangman.data.game.Game;
import de.nvborck.hangman.data.game.IGame;
import de.nvborck.hangman.data.game.IPlayerManager;
import de.nvborck.hangman.data.player.IPlayer;
import de.nvborck.hangman.data.player.Player;
import de.nvborck.hangman.data.wordprovider.SimpleWordProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.UUID;

public class CommandExecutorTest {

    @Test
    void executionEffectsTargetObject() throws IOException {

        // Arrange
        Game originalGame = new Game();
        IGame game = originalGame;
        IPlayerManager playerManager = originalGame;

        IPlayer player = new Player();
        player.setName("Anna");
        player.setId(UUID.randomUUID());

        playerManager.addPlayer(player);
        game.start("Kuechengeraet");

        ICommandExecutor executor = new CommandExecutor();
        ICommand command = new GuessCommand(game, 'e', player);

        // Act
        executor.executeCommand(command);

        // Assert
        Assertions.assertEquals("__e__e__e__e_", game.getMaskedWord());
        Assertions.assertEquals(1, game.getUsedCharacter().size());
        Assertions.assertTrue(game.getUsedCharacter().contains('e'));
        Assertions.assertFalse(game.isFinished());
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
}
