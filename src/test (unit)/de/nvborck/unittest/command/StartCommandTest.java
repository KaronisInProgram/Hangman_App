package de.nvborck.unittest.command;

import de.nvborck.hangman.command.CommandType;
import de.nvborck.hangman.command.ICommand;
import de.nvborck.hangman.command.StartCommand;
import de.nvborck.hangman.data.game.Game;
import de.nvborck.hangman.data.game.GameEvent;
import de.nvborck.hangman.data.game.IGame;
import de.nvborck.hangman.data.player.IPlayer;
import de.nvborck.hangman.data.player.Player;
import net.sharksystem.asap.ASAPException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.UUID;

public class StartCommandTest {

    @Test
    void executionSetsAGuessInTheGame() throws IOException {

        // Arrange
        IGame game = new Game();

        StartCommand command = new StartCommand(game, "Test");

        // Act
        command.execute();

        // Assert
        Assertions.assertEquals("Test", game.getSearchedWord());
        Assertions.assertEquals("____", game.getMaskedWord());
    }

    @Test
    void aDeserializedCommandHasTheSameEffectAsTheOriginal() throws IOException, ASAPException {

        // Arrange
        IGame game1 = new Game();
        IGame game2 = new Game();

        StartCommand command = new StartCommand(game1, "Test");

        // Act
        byte[] serializedCommand = command.asSerializableCommand().getSerializedMessage();
        StartCommand deserializedCommand = new StartCommand(serializedCommand);

        ((ICommand) command).setCoreObjectIfNull(game2);
        ((ICommand) deserializedCommand).setCoreObjectIfNull(game2);

        command.execute();
        deserializedCommand.execute();

        // Assert
        Assertions.assertEquals("Test", game1.getSearchedWord());
        Assertions.assertEquals("____", game1.getMaskedWord());

        Assertions.assertEquals("Test", game2.getSearchedWord());
        Assertions.assertEquals("____", game2.getMaskedWord());
    }

    @Test
    void theCorrelatedInformationAreCorrect() throws IOException {

        // Arrange
        IGame game = new Game();
        game.start("Abcdefg");

        IPlayer player = new Player();
        player.setName("Anna");
        player.setId(UUID.randomUUID());

        StartCommand command = new StartCommand(game, "Test");

        // Act

        // Assert
        Assertions.assertNotNull(command.getUniqeId());
        Assertions.assertEquals(GameEvent.searchedWordChange, command.getCorrelatedEvent());
        Assertions.assertEquals(CommandType.start, command.getCorrelatedType());
    }
}
