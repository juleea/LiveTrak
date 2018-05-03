package stanford.edu.livetrak2;

public class HandState {
    private RadioButtonGroup frequency;
    private String hand;
    private RadioButtonGroup interaction;
    private RadioButtonGroup object;
    private String previousObservation = "";

    public HandState(SessionActivity session, String hand, RadioButtonGroup object, RadioButtonGroup freq, RadioButtonGroup interaction) {
        this.hand = hand;
        this.object = object;
        this.frequency = freq;
        this.interaction = interaction;
        this.previousObservation = getHandState();
    }

    private String getHandState() {
        return this.hand + ',' + this.object + ',' + this.interaction;
    }

    public boolean isChanged() {
        return !getHandState().equals(this.previousObservation);
    }

    public String getAndUpdateHandStateString() {
        this.previousObservation = getHandState();
        return getHandState();
    }
}
