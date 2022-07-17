package de.nvborck.hangman.unittest.data.player;


import de.nvborck.hangman.data.IReadOperation;
import de.nvborck.hangman.data.IWriteOperation;
import de.nvborck.hangman.data.player.IPlayer;
import de.nvborck.hangman.data.player.Player;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class PlayerTest {

    @Test
    void theNameIsPredefinedWithAnonUserAndCanBeChanged() {

        // Arrange
        IPlayer player = new Player();

        // Act
        String name1 = player.getName();
        player.setName("Anna");
        String name2 = player.getName();

        // Assert
        Assertions.assertTrue(name1.contains("AnonUser#"));
        Assertions.assertNotEquals(name1, name2);
        Assertions.assertEquals("Anna", name2);
    }

    @Test
    void theIdIsRandomPredefinedAndCanBeChanged() {

        // Arrange
        IPlayer player = new Player();

        UUID idRandom = UUID.randomUUID();

        // Act
        UUID id1 = player.getId();
        player.setId(idRandom);
        UUID id2 = player.getId();
        UUID id3 = player.renewId();
        UUID id3Again = player.getId();

        // Assert
        Assertions.assertNotNull(id1);
        Assertions.assertEquals(idRandom, id2);
        Assertions.assertNotEquals(id1, id2);
        Assertions.assertNotEquals(id2, id3);
        Assertions.assertEquals(id3, id3Again);
    }

    @Test
    void whenOperationsAreDefinedTheyAreUsedBySaveAndLoad() {

        IReadOperation mockedRead = mock(IReadOperation.class);
        IWriteOperation mockedWrite = mock(IWriteOperation.class);

        // Arrange
        IPlayer player = new Player();

        // Act
        player.AsConfigurable().withReadOperation(mockedRead);
        player.AsConfigurable().withWriteOperation(mockedWrite);

        player.load();
        player.save();

        // Assert
        verify(mockedRead).read(player);
        verify(mockedWrite).write(player);
    }

    @Test
    void theyAreEqualsWhenIdIsTheSame() {

        // Arrange
        UUID id = UUID.randomUUID();

        IPlayer player1 = new Player();
        player1.setName("Anna");
        player1.setId(id);

        IPlayer player2 = new Player();
        player2.setName("Bob");
        player2.setId(id);

        // Act

        // Assert
        Assertions.assertEquals(player1, player2);
    }

    @Test
    void theyHaveTheSameHashcodeAsThereId() {

        // Arrange
        UUID id = UUID.randomUUID();

        IPlayer player1 = new Player();
        player1.setName("Anna");
        player1.setId(id);
        IPlayer player2 = new Player();
        player2.setName("Bob");
        player2.setId(id);

        // Act

        // Assert
        Assertions.assertEquals(player1.hashCode(), id.hashCode());
        Assertions.assertEquals(player2.hashCode(), id.hashCode());
        Assertions.assertEquals(player1.hashCode(), player2.hashCode());
    }
}
