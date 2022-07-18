package de.nvborck.hangman.app;

import de.nvborck.hangman.command.*;
import de.nvborck.hangman.data.game.Game;
import de.nvborck.hangman.data.game.GameEvent;
import de.nvborck.hangman.data.player.IPlayer;
import de.nvborck.hangman.data.wordprovider.IWordProvider;
import de.nvborck.hangman.network.GameAPI;
import de.nvborck.hangman.network.IGameAPI;

import de.nvborck.hangman.network.messages.GameCommand;
import de.nvborck.hangman.network.messages.OpenGame;
import de.nvborck.hangman.network.messages.SynchronizeGame;
import net.sharksystem.asap.ASAPException;
import net.sharksystem.asap.ASAPPeer;
import org.javatuples.Pair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class GameHandler implements IGameHandler, IGameNotifier {

    private Game game;
    private ICommandExecutor executor = new CommandExecutor();
    private boolean activeGame = false;
    private boolean activeGameSynchronized = false;
    private final IWordProvider provider;
    private UUID gameId = UUID.fromString("00000000-0000-0000-0000-000000000000");

    private IGameListener afterSynchronize;

    private final IGameAPI api;
    private final List<Pair<GameEvent, IGameListener>> listeners = new ArrayList<>();

    private final List<OpenGame> openGames = new ArrayList<>();

    public GameHandler(ASAPPeer peer, IWordProvider wordProvider, UUID uniqeId) {

        this.provider = wordProvider;
        this.api = new GameAPI(this, peer, uniqeId);
    }

    @Override
    public void initializeGame(IPlayer player) throws IOException {
        this.executor = new CommandExecutor();
        this.game = new Game();
        this.gameId = UUID.randomUUID();

        this.handleCommand(new StartCommand(this.game, this.provider.getRandomWord()));
        this.handleCommand(new JoinCommand(this.game, player));

        this.activeGame = true;
        this.activeGameSynchronized = true;
    }

    @Override
    public void joinGame(UUID gameid, IPlayer player) throws IOException {

        if(this.gameId.equals(gameid)) {
            this.handleCommand(new JoinCommand(this.game, player));
        } else {
            try {
                this.api.synchronizeGame(gameid);
            } catch (ASAPException e) {
                System.out.println("HangmanInternals (GameHandler) --> Could not Communicate With Other Games! Possible Offline!");
                return;
            }

            this.afterSynchronize = () -> this.joinGame(this.getGameId(), player);
            this.addGameListener(GameEvent.gameSynchronized, afterSynchronize);
        }
    }

    @Override
    public void searchGames() throws IOException {
        this.openGames.clear();
        try {
            this.api.searchGames();
        } catch (ASAPException e) {
            System.out.println("HangmanInternals (GameHandler) --> Could not Communicate With Other Games! Possible Offline!");
        }
    }

    @Override
    public boolean hasActiveGame() {
        return this.activeGame;
    }

    @Override
    public String getMaskedWord() {
        return this.game.getMaskedWord();
    }

    @Override
    public List<Character> getUsedCharacter() {
        return this.game.getUsedCharacter();
    }

    @Override
    public IPlayer getActivePlayer() {
        return this.game.getActivePlayer();
    }

    @Override
    public void guess(char character, IPlayer player) throws IOException {
        ICommand guess = new GuessCommand(this.game, character, player);
        this.handleCommand(guess);
    }

    @Override
    public List<OpenGame> getOpenGames() {
        return new ArrayList<>(this.openGames);
    }

    @Override
    public void addOpenGame(OpenGame game) {
        this.openGames.add(game);
        notifyAllListenerOfEvent(GameEvent.openGameFound);
    }

    @Override
    public UUID getGameId() {
        return this.gameId;
    }

    @Override
    public String getSearchedWord() {
        return this.game.getSearchedWord();
    }

    @Override
    public void synchronizeGame(SynchronizeGame synchronizeGame) {

        this.executor = new CommandExecutor();
        this.gameId = synchronizeGame.getId();

        this.game = new Game();
        for (GameCommand gameCommand: synchronizeGame.getCommands()) {
            ICommand command = gameCommand.getCommand();
            command.setCoreObjectIfNull(this.game);
            this.executor.executeCommand(command);
        }

        this.activeGame = true;
        this.activeGameSynchronized = true;

        this.notifyAllListenerOfEvent(GameEvent.gameSynchronized);

        if(this.afterSynchronize != null) {
            this.removeGameListener(this.afterSynchronize);
            this.afterSynchronize = null;
        }
    }

    @Override
    public List<ICommand> getCommands() {
        return this.executor.getCommands();
    }

    private void handleCommand(ICommand command) throws IOException {

        this.handleCommandWithoutSharing(command);
        try {
            this.api.sendCommand(command, this.gameId);
        } catch (ASAPException e) {
            System.out.println("HangmanInternals (GameHandler) --> Could not Communicate With Other Games! Possible Offline!");
        }
    }

    @Override
    public void handleCommandWithoutSharing(ICommand command) {

        command.setCoreObjectIfNull(this.game);
        this.executor.executeCommand(command);

        if(this.game.isFinished()) {
            this.activeGame = false;
        }

        notifyAllListenerOfEvent(command.getCorrelatedEvent());
    }

    @Override
    public void addGameListener(GameEvent event, IGameListener listener) {
        this.listeners.add(new Pair<>(event, listener));
    }

    @Override
    public void removeGameListener(IGameListener listener) {
        this.listeners.removeIf((pair) -> pair.getValue1() == listener);
    }

    private void notifyAllListenerOfEvent(GameEvent sourceEvent) {
        this.listeners.forEach((pair) -> {
            GameEvent event = pair.getValue0();
            IGameListener listener = pair.getValue1();

            if(event == sourceEvent) {
                try {
                    listener.getNotified();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
}
