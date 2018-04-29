package node;

import move.Move;
//import node.Node;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

import bot.BotState;
import goMove.GoMove;

//import processor.GoProcessor;
public class AINode {
	private Random random= new Random();
	
    BotState state;
    //ArrayList<Move> availableMoves;
    ArrayList<Move> remainingMoves;
    ArrayList<AINode> childArray;
    //int playerID;
    AINode parent;
    int totalScore;
    int numOfVisits;
    GoMove action;

    public AINode(BotState state, AINode parent,GoMove action) {
        this.state = state;
        this.parent = parent;
        //setAvailableMoves();
        //this.playerID = state.getBoard().getMyId();
        setRemainingMoves();
        this.action=action;
        totalScore=0;
        numOfVisits=0;
    }

    /*public void setAvailableMoves() {
        this.availableMoves = state.getBoard().getAvailableMoves();
    }*/

    public void setRemainingMoves() {
        this.remainingMoves = state.getBoard().getAvailableMoves();
    }

    
    /*public ArrayList<Move> getAvailableMoves() {
        return this.availableMoves;
    }*/

    public ArrayList<Move> getRemainingMoves() {
        return this.remainingMoves;
    }

    /* public void setChildren(){
         GoProcessor processor = new GoProcessor();
         this.availableMoves.forEach(mov -> {
             BotState childBotState= new BotState();
             childBotState = processor.createNextStateFromMove(this, mov.toString());
             AINode childNode = new AINode(childBotState, this);
             this.children.add(childNode);
         });
     }*/
    
    
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
        childArray.add(childNode);
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
        if (this.state.getBoard().getMyId() == winID)
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
        //return Collections.max(this.childArray, Comparator.comparing(AINode::getNumOfVisits));
    	return this.childArray.stream().max(Comparator.comparing(AINode::getNumOfVisits)).get();
        /*int bestValue = Integer.MIN_VALUE;
        AINode bestChild ;
        for (AINode child : childArray) {
            int childVal = child.numOfVisits;
            if(childVal > bestValue){
                bestChild = child;
            }
            return bestChild;*/

    }
    
    public GoMove getAction(){
        return this.action;
    }
}
