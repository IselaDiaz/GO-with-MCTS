package bot;

import move.Move;

import java.util.ArrayList;
import processor.GoProcessor;
public class AINode {

    BotState state;
    ArrayList<Move> availableMoves;
    ArrayList<AINode> children;
    int playerID;
    AINode parent;
    int totalScore;
    int numOfVisits;

    public AINode(BotState state, AINode parent){
        this.state = state;
        this.parent = parent;
        setAvailableMoves();
        this.playerID = state.getBoard().getMyId();
    }

    public void setAvailableMoves(){
        this.availableMoves = state.getBoard().getAvailableMoves();
    }

    public ArrayList<Move> getAvailableMoves(){
        return this.availableMoves;
    }


    public void setChildren(){
        GoProcessor processor = new GoProcessor();
        this.availableMoves.forEach(mov -> {
            BotState childBotState= new BotState();
            childBotState = processor.createNextStateFromMove(this, mov.toString());
            AINode childNode = new AINode(childBotState, this);
            this.children.add(childNode);
        });

    }
    public ArrayList<AINode> getChildren(){
        return this.children;
    }
    public void updateTotalScore(int winID){
        if (this.playerID == winID)
            this.totalScore = totalScore++;
    }
    public int getTotalScore(){
        return this.totalScore;
    }

    public void updateNumOfVisits() {
        this.numOfVisits = numOfVisits++;
    }
    public int getNumOfVisits(){
        return this.numOfVisits;
    }
    
    public BotState getState() {
		return this.state;
	}
}
