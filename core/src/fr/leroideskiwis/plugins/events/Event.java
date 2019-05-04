package fr.leroideskiwis.plugins.events;

public class Event {

    private boolean cancel;

    public boolean isCancelled() {
        return cancel;
    }

    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }
}
