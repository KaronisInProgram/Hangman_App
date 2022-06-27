package de.nvborck.hangman.command;

import de.nvborck.hangman.app.GameEvents;
import de.nvborck.hangman.data.game.IGame;

public interface ICommand<T> {

    void execute();

    void setCoreObjectIfNull(T game);

    GameEvents getCorrelatedEvent();

    ISerializableCommand asSerializableCommand();
}
