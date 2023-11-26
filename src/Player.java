import java.util.ArrayList;
import java.util.HashMap;

public class Player {

    private String name;
    private int playerOrder = 0;
    public String playerSign;


    private boolean isCurrent = false;
    private ArrayList<String> playedPositions = new ArrayList<String>();
    private HashMap<String, ArrayList<String>> winConditions = Game2.getWinConditions();



    private int wonRounds = 0;

    Player(String name){
        this.name = name;
    }

    public void makeChoice(String buttonID){
        playedPositions.add(buttonID);
    }

    public String getName() {
        return name;
    }

    public int getPlayerOrder() {
        return playerOrder;
    }

    public void setPlayerOrder(int playerOrder) {
        this.playerOrder = playerOrder;
        if(playerOrder==1){
            this.playerSign = "X";
            this.isCurrent = true;
        } else {
            this.playerSign = "O";
            this.isCurrent = false;
        }
        this.playedPositions.clear();
    }

    public boolean isCurrent() {
        return isCurrent;
    }

    public void setCurrent(boolean current) {
        this.isCurrent = current;
    }

    public boolean checkForWin(){
        boolean wins = false;
        for(String key : winConditions.keySet()){
            if(playedPositions.containsAll(winConditions.get(key))){
                wins = true;
            }
        }
        return wins;
    }

    public int getWonRounds() {
        return wonRounds;
    }
    public void setWonRounds() {
        this.wonRounds++;
        System.out.println(name +" won " + wonRounds);
    }
}
