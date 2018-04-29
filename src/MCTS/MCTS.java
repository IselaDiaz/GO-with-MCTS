package MCTS;

import bot.BotState;
import goMove.GoMove;
import move.Move;
import node.AINode;
import processor.GoProcessor;

//import java.util.LinkedList;
//import java.util.List;
import java.util.Random;

public class MCTS {
	GoProcessor processor = new GoProcessor();


	public GoMove selectMove() {

		BotState currentBoardState = new BotState();
		int timeBank = currentBoardState.getTimebank();
		int maxRounds = currentBoardState.getMaxRounds();
		int roundNumber = currentBoardState.getRoundNumber();

		double computationalTimePerMove = timeBank/(maxRounds-roundNumber);
		long start = System.currentTimeMillis();
		long end = (long) (start + computationalTimePerMove);

		AINode rootNode = new AINode(currentBoardState, null, null);
		while (System.currentTimeMillis() < end)  {

			AINode currNode = rootNode;
			//visited.add(currNode);

            while (currNode.getRemainingMoves().size() == 0){
            	currNode = selectWithUCT(currNode);   //Selection with UCT
			}

            AINode newNode = expand(currNode);
            //AINode newNode = selectWithUCT(currNode) ;
            //visited.add(newNode);
			int winId = rollOut(newNode);
			backpropagate(newNode, winId);
		}
		AINode bestNode = rootNode.getChildWithMaxScore();
		GoMove bestMove = bestNode.getAction();
		return bestMove;
		}


	public AINode selectWithUCT(AINode currNode){
		double epsilon = 1e-6;
		Random r = new Random();
		AINode selected = null;
		double bestValue = Double.MIN_VALUE;
		for (AINode child : currNode.getChildArray()) {
			double uctValue = child.getTotalScore() / (child.getNumOfVisits() + epsilon) +
					Math.sqrt(Math.log(child.getNumOfVisits()+1) / (child.getNumOfVisits() + epsilon)) +
					r.nextDouble() * epsilon;
			// small random number to break ties randomly in unexpanded nodes
			if (uctValue > bestValue) {
				selected = child;
				bestValue = uctValue;
			}
		}
		return selected;
	}
	public AINode expand(AINode currNode){
		Move randomMove = currNode.getRandomAction();
		AINode childNode = processor.createNextStateFromMove(currNode, randomMove.toString() );
		currNode.addChildtoArray(childNode);
        return childNode;
	}

	
	
	
	public int rollOut(AINode stateNode){

		while(!processor.hasGameEnded(stateNode)) {
			Move move=stateNode.randomMove();
			stateNode=processor.createNextStateFromMove(stateNode, move.toString());
		}
		return processor.getWinnerId(stateNode.getState());
	}


		

	
	
	
	public void backpropagate(AINode node, int winID){
		//AINode node = node ;
		while(node.getParent() != null){
			node.updateTotalScore(winID);
			node.updateNumOfVisits();
			node = node.getParent();

		}
		}
	}


	

