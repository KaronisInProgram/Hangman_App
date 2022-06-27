package de.nvborck.hangman.data.player;

import de.nvborck.hangman.data.IWriteOperation;
import de.nvborck.hangman.data.IConfigurableOperations;
import de.nvborck.hangman.data.IReadOperation;

import java.util.Random;
import java.util.UUID;

public class Player implements IPlayer, IConfigurableOperations<IPlayer> {

    private String name = "AnonUser#" + new Random().nextInt(999999);
    private UUID id = UUID.randomUUID();

    private IWriteOperation<IPlayer> writeOperation;
    private IReadOperation<IPlayer> readOperation;

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public UUID getId() {
        return this.id;
    }

    @Override
    public UUID renewId() {
        this.id = UUID.randomUUID();
        return this.id;
    }

    @Override
    public void setId(UUID id) {
        this.id = id;
    }

    @Override
    public void save() {
        if (writeOperation == null) {
            return;
        }
        this.writeOperation.write(this);
    }

    @Override
    public void load() {
        if (readOperation == null) {
            return;
        }
        this.readOperation.read(this);
    }

    @Override
    public IConfigurableOperations<IPlayer> AsConfigurable() {
        return this;
    }

    @Override
    public void withReadOperation(IReadOperation<IPlayer> operation) {
        this.readOperation = operation;
    }

    @Override
    public void withWriteOperation(IWriteOperation<IPlayer> operation) {
        this.writeOperation = operation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Player player = (Player) o;

        return id.equals(player.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
