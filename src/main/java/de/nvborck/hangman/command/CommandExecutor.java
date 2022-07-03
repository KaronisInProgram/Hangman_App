package de.nvborck.hangman.command;

import java.util.ArrayList;
import java.util.List;

public class CommandExecutor implements ICommandExecutor {

    List<ICommand> commands = new ArrayList<>();

    @Override
    public void executeCommand(ICommand command) {
        if (command == null) {
            throw new IllegalArgumentException("The command can't be null!");
        }

        this.commands.add(command);
        command.execute();
    }

    @Override
    public List<ICommand> getCommands() {
        return new ArrayList<>(this.commands);
    }
}
