package de.nvborck.hangman.data.player;

import de.nvborck.hangman.data.IConfigurableOperations;

import java.util.UUID;

public interface IPlayer {

    String getName();

    void setName(String name);

    UUID getId();

    UUID renewId();

    void setId(UUID id);

    void save();

    void load();

    IConfigurableOperations<IPlayer> AsConfigurable();
}
