package de.nvborck.tests.data.statistic;

import de.nvborck.hangman.data.IReadOperation;
import de.nvborck.hangman.data.IWriteOperation;
import de.nvborck.hangman.data.statistic.IStatistic;
import de.nvborck.hangman.data.statistic.Statistic;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class StatisticTest {

    @Test
    void theStatisticIsPredefinedAndCanBeIncremented() {

        // Arrange
        IStatistic statistic = new Statistic();

        // Act
        int guessedWords1 = statistic.getGuessedWords();
        statistic.increaseGuessedWords();
        statistic.increaseGuessedWords();
        int guessedWords2 = statistic.getGuessedWords();

        // Assert
        Assertions.assertEquals(0, guessedWords1);
        Assertions.assertEquals(2, guessedWords2);
    }

    @Test
    void whenOperationsAreDefinedTheyAreUsedBySaveAndLoad() {

        IReadOperation mockedRead = mock(IReadOperation.class);
        IWriteOperation mockedWrite = mock(IWriteOperation.class);

        // Arrange
        IStatistic statistic = new Statistic();

        // Act
        statistic.AsConfigurable().withReadOperation(mockedRead);
        statistic.AsConfigurable().withWriteOperation(mockedWrite);

        statistic.load();
        statistic.save();

        // Assert
        verify(mockedRead).read(statistic);
        verify(mockedWrite).write(statistic);
    }
}
