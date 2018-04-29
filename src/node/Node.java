package node;

import java.util.ArrayList;

import bot.BotState;
import move.Move;

public class Node {
	
	BotState state;
	Node parent;
	ArrayList<Node> children;
	ArrayList<Move> availableMoves;
	int numOfWins;
    int numOfVisits;
	
	public Node(BotState state,Node parent) {
		this.state = state;
        this.parent = parent;
        setAvailableMoves();
        children=new ArrayList<Node>();
        numOfWins=0;
        numOfVisits=0;
	}
	
	public Node(BotState state) {
		this.state=state;
		this.parent=null;
	}
	
	public void setAvailableMoves(){
        this.availableMoves = state.getBoard().getAvailableMoves();
    }
	
	public ArrayList<Move> getAvailableMoves(){
        return this.availableMoves;
    }
	
	public Node getParent() {
		return this.parent;
	}
	
	public BotState getState() {
		return this.state;
	}
	
	public boolean hasParent() {
		return parent!=null;
	}
	
	public void addChild(Node childNode) {
		this.children.add(childNode);
	}

    public ArrayList<Node> getChildren(){
        return this.children;
    }
    public void addWin(double numOfWins){
            this.numOfWins++;
    }
    public int getnumOfWins(){
        return this.numOfWins;
    }

    public void incrementNumOfVisits() {
        this.numOfVisits++;
    }
    public int getNumOfVisits(){
        return this.numOfVisits;
    }
}
