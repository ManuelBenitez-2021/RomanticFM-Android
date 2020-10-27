package tcking.github.com.giraffeplayer2;

/**
 * Created by Admin on 4/5/2018.
 */

public class EventBusDataSharing {


    GiraffePlayer currentPlayer;
    String status;

    public GiraffePlayer getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(GiraffePlayer currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
