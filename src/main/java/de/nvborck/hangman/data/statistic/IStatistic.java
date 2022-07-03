package de.nvborck.hangman.data.statistic;

import de.nvborck.hangman.data.IConfigurableOperations;

public interface IStatistic {

    /**
     * Increases the Statistic record of the data.playerChange by 1 for the category "Guessed Words".
     */
    void increaseGuessedWords();

    /**
     * Returns the saved value of the "Guessed Words" category.
     *
     * @return the "Guessed Words"-Value.
     */
    int getGuessedWords();

    void save();

    void load();

    IConfigurableOperations<IStatistic> AsConfigurable();
}
