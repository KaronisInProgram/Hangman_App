package de.nvborck.hangman.data;

public interface IConfigurableOperations<T> {

    void withReadOperation(IReadOperation<T> operation);

    void withWriteOperation(IWriteOperation<T> operation);
}