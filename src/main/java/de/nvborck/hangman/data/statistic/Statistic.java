package de.nvborck.hangman.data.statistic;

import de.nvborck.hangman.data.IWriteOperation;
import de.nvborck.hangman.data.IConfigurableOperations;
import de.nvborck.hangman.data.IReadOperation;

public class Statistic implements IStatistic, IConfigurableOperations<IStatistic>  {

    private int guessedWords = 0;

    private IWriteOperation<IStatistic> writeOperation;
    private IReadOperation<IStatistic> readOperation;

    @Override
    public void increaseGuessedWords() {
        this.guessedWords++;
    }

    @Override
    public int getGuessedWords() {
        return this.guessedWords;
    }

    @Override
    public void save() {
        if (writeOperation == null) {
            return;
        }
        this.writeOperation.write(this);
    }

    @Override
    public void load() {
        if (readOperation == null) {
            return;
        }
        this.readOperation.read(this);
    }

    @Override
    public IConfigurableOperations<IStatistic> AsConfigurable() {
        return this;
    }

    @Override
    public void withReadOperation(IReadOperation<IStatistic> operation) {
        this.readOperation = operation;
    }

    @Override
    public void withWriteOperation(IWriteOperation<IStatistic> operation) {
        this.writeOperation = operation;
    }
}
