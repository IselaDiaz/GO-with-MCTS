package bot;

import move.Move;

import java.util.ArrayList;

public class AINode {

    BotState state;
    ArrayList<Move> availableMoves;
    ArrayList<AINode> children;
    AINode parent;
    int totalScore;
    int numOfVisits;

    public AINode(BotState state, AINode parent){
        this.state = state;
        this.parent = parent;
        setAvailableMoves();
    }

    public void setAvailableMoves(){
        this.availableMoves = state.getBoard().getAvailableMoves();
    }

    public ArrayList<Move> getAvailableMoves(){
        return this.availableMoves;
    }


    public void setChildren(){
        this.availableMoves.forEach(mov -> {
            BotState childBotState= new BotState();
            //childBotState = BotState.getNextState(state, mov);
            AINode childNode = new AINode(childBotState, this);
            this.children.add(childNode);
        });

    }
    public ArrayList<AINode> getChildren(){
        return this.children;
    }
    public void addScore(double score){
        if (this.totalScore != Integer.MIN_VALUE)
            this.totalScore += score;
    }
    public int getTotalScore(){
        return this.totalScore;
    }

    public void incrementNumOfVisits() {
        this.numOfVisits = numOfVisits++;
    }
    public int getNumOfVisits(){
        return this.numOfVisits;
    }
}
