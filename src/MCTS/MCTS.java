package MCTS;

import java.util.ArrayList;
import java.util.Random;

import bot.BotState;
import move.Move;
import node.Node;
import processor.GoProcessor;

public class MCTS {

	GoProcessor processor;
	private Random random;
	
	public MCTS() {
		processor=new GoProcessor();
		random=new Random();
	}
	
	public int playout(Node stateNode) {
		
		while(!processor.hasGameEnded(stateNode)) {
			Move move=randomMove(stateNode.getState());
			stateNode=processor.createNextStateFromMove(stateNode, move.toString());
		}
		return processor.getWinnerId(stateNode.getState());
	}
	
	public Move randomMove(BotState state) {
    	ArrayList<Move> availableMoves = state.getBoard().getAvailableMoves();
    	int moveCount = availableMoves.size();

    	if (moveCount <= 0) {
    		return null;
		}

    	return availableMoves.get(this.random.nextInt(moveCount));
    }
	
}
 