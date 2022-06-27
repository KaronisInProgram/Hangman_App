package de.nvborck.hangman.app;

public interface IGameNotifier {

    void addGameListener(GameEvents event, IGameListener listener);

    void removeGameListener(IGameListener listener);
}
