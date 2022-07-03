package de.nvborck.hangman.command;

import de.nvborck.hangman.data.game.GameEvent;

public interface ICommand<T> {

    void execute();

    void setCoreObjectIfNull(T core);

    GameEvent getCorrelatedEvent();
    CommandType getCorrelatedType();

    ISerializableCommand asSerializableCommand();
}
