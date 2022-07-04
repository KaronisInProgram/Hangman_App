package de.nvborck.hangman.command;

import de.nvborck.hangman.data.game.GameEvent;

import java.util.UUID;

public interface ICommand<T> {

    void execute();

    void setCoreObjectIfNull(T core);
    UUID getUniqeId();

    GameEvent getCorrelatedEvent();
    CommandType getCorrelatedType();

    ISerializableCommand asSerializableCommand();
}
