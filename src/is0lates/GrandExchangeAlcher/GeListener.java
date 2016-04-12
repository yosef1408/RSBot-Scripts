package GrandExchangeAlcher;

import java.util.EventListener;
import GrandExchangeAlcher.GeEvent;

public interface GeListener extends EventListener {

    /**
     * Invoked when a Grand Exchange message is sent to the client.
     *
     * @param event The {@link GeEvent}.
     */
    public void onBuy(GeEvent event);

    /**
     * Invoked when a Grand Exchange message is sent to the client.
     *
     * @param event The {@link GeEvent}.
     */
    public void onSell(GeEvent event);
}