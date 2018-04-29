package MCTS;

import bot.BotState;
import node.Node;
import processor.GoProcessor;
import bot.AINode;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class MCTS {

	List<AINode> visited = new LinkedList<AINode>();

	public void selectMove() {

		BotState currentBoardState = new BotState();
		int timeBank = currentBoardState.getTimebank();
		int maxRounds = currentBoardState.getMaxRounds();
		int roundNumber = currentBoardState.getRoundNumber();

		double computationalTimePerMove = timeBank/(maxRounds-roundNumber);
		long start = System.currentTimeMillis();
		long end = (long) (start + computationalTimePerMove);

		AINode rootNode = new AINode(currentBoardState, null);
		while (System.currentTimeMillis() < end)  {

			AINode currNode = rootNode;
			visited.add(currNode);
            while (currNode.getChildren().size()!=0){
            	currNode = selectWithUCT(currNode);   //Selection with UCT
			}
            expand(currNode);
            AINode newNode = selectWithUCT(currNode) ;
            visited.add(newNode);
			int winId = rollOut(newNode);
			backpropagate(int winID);
		}
		}

	public AINode selectWithUCT(AINode currNode){
		double epsilon = 1e-6;
		Random r = new Random();
		AINode selected = null;
		double bestValue = Double.MIN_VALUE;
		for (AINode child : currNode.getChildren()) {
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
	public void expand(AINode currNode){
		currNode.setChildren();
	}

	public int rollOut(AINode newNode){
		int i = 0 ;
		return  i;
	}

	public void backpropagate(int winID){

		for(AINode node : visited){
          node.updateTotalScore(winID);
          node.updateNumOfVisits();

		}
	}
	}

	

