package de.nvborck.hangman.command;

import de.nvborck.hangman.network.IGameAPI;

public class CommandExecutor implements ICommandExecutor{

    @Override
    public void executeCommand(ICommand command) {
        if (command == null) {
            throw new IllegalArgumentException("The command can't be null!");
        }

        command.execute();
    }
}
