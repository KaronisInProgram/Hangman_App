package de.nvborck.hangman.unittest.command;

import de.nvborck.hangman.command.CommandType;
import de.nvborck.hangman.command.ICommand;
import de.nvborck.hangman.command.JoinCommand;
import de.nvborck.hangman.data.game.Game;
import de.nvborck.hangman.data.game.GameEvent;
import de.nvborck.hangman.data.game.IPlayerManager;
import de.nvborck.hangman.data.player.IPlayer;
import de.nvborck.hangman.data.player.Player;
import net.sharksystem.asap.ASAPException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.UUID;

public class JoinCommandTest {

    @Test
    void executionSetsANewPlayerInTheGame() throws IOException {

        // Arrange
        IPlayerManager game = new Game();

        IPlayer player = new Player();
        player.setName("Anna");
        player.setId(UUID.randomUUID());

        JoinCommand command = new JoinCommand(game, player);

        // Act
        command.execute();

        // Assert
        Assertions.assertEquals(player, game.getActivePlayer());
    }

    @Test
    void aDeserializedCommandHasTheSameEffectAsTheOriginal() throws IOException, ASAPException {

        // Arrange
        IPlayer player = new Player();
        player.setName("Anna");
        player.setId(UUID.randomUUID());

        IPlayerManager playerManager1 = new Game();

        Game originalGame2 = new Game();

        JoinCommand command = new JoinCommand(playerManager1, player);

        // Act
        byte[] serializedCommand = command.asSerializableCommand().getSerializedMessage();
        JoinCommand deserializedCommand = new JoinCommand(serializedCommand);

        ((ICommand) command).setCoreObjectIfNull(originalGame2);
        ((ICommand) deserializedCommand).setCoreObjectIfNull(originalGame2);

        command.execute();
        deserializedCommand.execute();

        // Assert
        Assertions.assertEquals(player, playerManager1.getActivePlayer());

        Assertions.assertEquals(player, ((IPlayerManager) originalGame2).getActivePlayer());
    }

    @Test
    void theCorrelatedInformationAreCorrect() throws IOException {

        // Arrange
        IPlayerManager game = new Game();

        IPlayer player = new Player();
        player.setName("Anna");
        player.setId(UUID.randomUUID());

        JoinCommand command = new JoinCommand(game, player);

        // Act

        // Assert
        Assertions.assertNotNull(command.getUniqeId());
        Assertions.assertEquals(GameEvent.playerChange, command.getCorrelatedEvent());
        Assertions.assertEquals(CommandType.join, command.getCorrelatedType());
    }
}
