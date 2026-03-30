package be.nidel.kinomichi.gathering;

import java.util.ArrayList;
import java.util.function.Consumer;

public class SessionRequestEvent {

    protected ArrayList<Consumer<GatheringPayload>> observers = new ArrayList<>();
    public void connect(Consumer<GatheringPayload> action){
        observers.add(action);
    }
    public void disconnect(Consumer<GatheringPayload> action){
        observers.remove(action);
    }
    public void emit(GatheringPayload payload){
        for (Consumer<GatheringPayload> observer : observers) {
            observer.accept(payload);
        }
    }
}
