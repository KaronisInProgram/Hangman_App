package de.nvborck.hangman.app;

import de.nvborck.hangman.data.game.GameEvent;

public interface IGameNotifier {

    void addGameListener(GameEvent event, IGameListener listener);

    void removeGameListener(IGameListener listener);
}
