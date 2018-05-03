package node;

import move.Move;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Random;

import bot.BotState;
import goMove.GoMove;

public class AINode {
	private Random random= new Random();
	
    BotState state;
    ArrayList<Move> remainingMoves;
    ArrayList<AINode> childArray = new ArrayList<AINode>();
    AINode parent;
    int totalScore;
    int numOfVisits;
    GoMove action;

    public AINode(BotState state, AINode parent,GoMove action) {
        this.state = state;
        this.parent = parent;
        setRemainingMoves();
        this.action=action;
        totalScore=0;
        numOfVisits=0;
    }

    public void setRemainingMoves() {
        this.remainingMoves = state.getBoard().getAvailableMoves();
    }

    public ArrayList<Move> getRemainingMoves() {
        return this.remainingMoves;
    }
    
    public Move getRandomAction() {
        int noOfPossibleMoves = this.remainingMoves.size();
        int selectRandom = (int) (Math.random() * ((noOfPossibleMoves - 1) + 1));
        Move mov = this.remainingMoves.get(selectRandom);
        remainingMoves.remove(mov);
        return mov;
    }

	public Move randomMove() {
		ArrayList <Move> availableMoves = state.getBoard().getAvailableMoves();
    	int moveCount = availableMoves.size();

    	if (moveCount <= 0) {
    		return null;
		}

    	return availableMoves.get(this.random.nextInt(moveCount));
    }
	
    public void addChildtoArray(AINode childNode) {
         this.childArray.add(childNode);
    }

    public ArrayList<AINode> getChildArray() {
        return this.childArray;
    }

    public AINode getParent() {
        return this.parent;
    }
    
    public boolean hasParent() {
		return parent!=null;
	}

    public void updateTotalScore(int winID) {
    	if (this.state.getBoard().getMyId() != winID)
            totalScore++;
    }

    public int getTotalScore() {
        return this.totalScore;
    }

    public void updateNumOfVisits() {
        numOfVisits++;
    }

    public int getNumOfVisits() {
        return this.numOfVisits;
    }

    public BotState getState() {
        return this.state;
    }

    public AINode getChildWithMaxScore() {
    	return this.childArray.stream().max(Comparator.comparing(AINode::getNumOfVisits)).get();
    }
    
    public GoMove getAction(){
        return this.action;
    }
}
