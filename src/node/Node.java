package node;

import bot.BotState;

public class Node {
	
	Node parent;
	BotState state;
	
	public Node(BotState state,Node parent) {
		this.state=state;
		this.parent=parent;
	}
	
	public Node(BotState state) {
		this.state=state;
		this.parent=null;
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
	
}
