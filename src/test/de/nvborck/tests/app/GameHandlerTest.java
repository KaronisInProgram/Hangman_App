package de.nvborck.tests.app;

import de.nvborck.hangman.app.IGameHandler;
import net.sharksystem.asap.ASAPException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class GameHandlerTest {

    @Test
    void initializingOfAGGameSetsItAsTheActiveGame() {

        // Arrange
        IGameHandler handler = null;

        // Act

        // Assert
        Assertions.fail("Has To be implemented and add Assertions");
    }

    @Test
    void initializingAGameWhenThereIsAnActiveGameKicksUserFromFirstGame() {

        // Arrange
        IGameHandler handler = null;

        // Act

        // Assert
        Assertions.fail("Has To be implemented and add Assertions");
    }

    @Test
    void externalCommandAreHandledWithLocalGame() throws IOException, ASAPException {

        // Arrange
        IGameHandler handler = null;

        // Act


        // Assert
        Assertions.fail("Has To be implemented and add Assertions");
    }

    @Test
    void searchingAsksNearbyDevicesIfTheyHaveAnOpenGame() throws IOException, ASAPException {

        // Arrange
        IGameHandler handler = null;

        // Act
        handler.searchGames();

        // Assert
        Assertions.fail("Has To be implemented and add Assertions");
    }

    @Test
    void searchingOnlyStopsIfASearchWasStarted() {

        // Arrange
        IGameHandler handler = null;

        // Act
        handler.stoppSearching();

        // Assert
        Assertions.fail("Has To be implemented and add Assertions");
    }


    @Test
    void joiningAnOpenGameSynchronizesTheGame() {

        // Arrange
        IGameHandler handler = null;

        // Act


        // Assert
        Assertions.fail("Has To be implemented and add Assertions");
    }


}
