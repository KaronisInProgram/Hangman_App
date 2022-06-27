package de.nvborck.tests.command;

import de.nvborck.hangman.command.*;
import de.nvborck.hangman.data.game.Game;
import de.nvborck.hangman.data.game.IGame;
import de.nvborck.hangman.data.game.IPlayerManager;
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
        Game originalGame = new Game(new SimpleWordProvider());
        IGame game = originalGame;
        IPlayerManager playerManager = originalGame;

        UUID gameId = UUID.randomUUID();
        UUID playerId = UUID.randomUUID();
        String playerName = "Anna";

        playerManager.addPlayer(playerId, playerName);
        game.start("Kuechengeraet", gameId);

        ICommand command = new GuessCommand(game, 'e', playerId);

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
        Game originalGame1 = new Game(new SimpleWordProvider());
        IGame game1 = originalGame1;
        IPlayerManager playerManager1 = originalGame1;

        UUID playerId = UUID.randomUUID();
        String playerName = "Anna";

        playerManager1.addPlayer(playerId, playerName);
        game1.start("Kuechengeraet", UUID.randomUUID());

        Game originalGame2 = new Game(new SimpleWordProvider());
        IGame game2 = originalGame2;
        IPlayerManager playerManager2 = originalGame2;

        playerManager2.addPlayer(playerId, playerName);
        game2.start("Kuechengeraet", UUID.randomUUID());

        GuessCommand command = new GuessCommand(game1, 'e', playerId);

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
}
