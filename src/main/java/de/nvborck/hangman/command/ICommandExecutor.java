package de.nvborck.hangman.command;

import java.util.List;

public interface ICommandExecutor {

    void executeCommand(ICommand command);

    List<ICommand> getCommands();
}
