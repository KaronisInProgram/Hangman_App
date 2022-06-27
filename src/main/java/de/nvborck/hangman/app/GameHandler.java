package de.nvborck.hangman.app;

import de.nvborck.hangman.command.*;
import de.nvborck.hangman.data.game.Game;
import de.nvborck.hangman.data.game.IGame;
import de.nvborck.hangman.data.player.IPlayer;
import de.nvborck.hangman.data.wordprovider.IWordProvider;
import de.nvborck.hangman.network.GameAPI;
import de.nvborck.hangman.network.IGameAPI;

import net.sharksystem.asap.ASAPException;
import net.sharksystem.asap.ASAPPeer;
import org.javatuples.Pair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class GameHandler implements IGameHandler, IGameNotifier {

    private Game game;
    private boolean activeGame = false;

    private final ICommandExecutor executor;
    private final IGameAPI api;
    private final IWordProvider provider;

    private final List<Pair<GameEvents, IGameListener>> listeners = new ArrayList<>();

    private List<IGame> openGames = new ArrayList<>();

    public GameHandler(ASAPPeer peer, IWordProvider wordProvider) {
        this.provider = wordProvider;

        this.api = new GameAPI(this, peer);
        this.executor = new CommandExecutor();
    }

    @Override
    public void initializeGame(IPlayer player) throws IOException, ASAPException {
        this.game = new Game(this.provider);
        this.game.start();

        this.handleCommand(new JoinCommand(this.game, player.getId(), player.getName()));

        activeGame = true;
    }

    @Override
    public void joinGame(UUID gameid, IPlayer player) {
        this.game = new Game(null);
        this.game.start("", gameid);
    }

    @Override
    public void searchGames() throws IOException, ASAPException {
        this.openGames.clear();
        this.api.searchGames();
    }

    @Override
    public void stoppSearching() {

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
    public void guess(char character, IPlayer player) throws IOException, ASAPException {
        ICommand guess = new GuessCommand(this.game, character, player.getId());
        this.handleCommand(guess);
    }

    @Override
    public List<IGame> getOpenGames() {
        return new ArrayList<>(this.openGames);
    }

    @Override
    public void addOpenGame(IGame game) {
        this.openGames.add(game);
    }

    @Override
    public UUID getGameId() {
        return this.game.getId();
    }

    @Override
    public String getSearchedWord() {
        return this.game.getSearchedWord();
    }

    private void handleCommand(ICommand command) throws IOException, ASAPException {

        command.setCoreObjectIfNull(this.game);
        this.executor.executeCommand(command);
        this.api.sendCommand(command, game.getId());

        if(this.game.isFinished()) {
            this.activeGame = false;
        }

        // Notify all registered Listener
        this.listeners.forEach((pair) -> {
            GameEvents event = pair.getValue0();
            IGameListener listener = pair.getValue1();

            if(event == command.getCorrelatedEvent()) {
                listener.getNotified();
            }
        });
    }

    private void handleExternalCommand(ICommand command) {
        this.executor.executeCommand(command);
    }

    @Override
    public void addGameListener(GameEvents event, IGameListener listener) {
        this.listeners.add(new Pair<>(event, listener));
    }

    @Override
    public void removeGameListener(IGameListener listener) {
        this.listeners.removeIf((pair) -> pair.getValue1() == listener);
    }
}
