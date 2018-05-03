package MCTS;

import bot.BotState;
import goMove.GoMove;
import move.Move;
import node.AINode;
import processor.GoProcessor;


public class MCTS {
	
	//public static String printing=new String();
	//public static int a;
	
	GoProcessor processor = new GoProcessor();
    BotState currentBoardState ;
    public MCTS (BotState currentBoardState) {
        this.currentBoardState = currentBoardState;
    }
	public GoMove selectMove() {
		int timeBank = currentBoardState.getTimebank();
		int maxRounds = currentBoardState.getMaxRounds();
		int roundNumber = currentBoardState.getRoundNumber();
		double computationalTimePerMove = timeBank/(maxRounds-roundNumber);
		long startNano=System.nanoTime();
		long endNano=(long)(startNano+computationalTimePerMove*1e6);
		AINode rootNode = new AINode(currentBoardState, null, null);
		while (System.nanoTime()-endNano<-500000) {
			AINode currNode = rootNode;
            //while (currNode.getRemainingMoves().size() == 0){
            while (currNode.getRemainingMoves().isEmpty()){
            	currNode = selectWithUCT(currNode);   //Selection with UCT
			}
            AINode newNode = expand(currNode);
			int winId = rollOut(newNode);
			backpropagate(newNode, winId);
		}
		AINode bestNode = rootNode.getChildWithMaxScore();
		GoMove bestMove = bestNode.getAction();
		return bestMove;
	}


	public AINode selectWithUCT(AINode currNode){
		//double epsilon = 1e-6;
		//Random r = new Random();
		AINode selected = null;
		double bestValue = -1;
		for (AINode child : currNode.getChildArray()) {
			/*double uctValue = child.getTotalScore() / (child.getNumOfVisits()+ epsilon) +
					Math.sqrt(Math.log(child.getNumOfVisits()+1) / (child.getNumOfVisits() + epsilon)) ;*/
			double uctValue=child.getTotalScore()/child.getNumOfVisits()+
					Math.sqrt(2*Math.log(currNode.getNumOfVisits())/child.getNumOfVisits());
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
		while(node.getParent() != null){
			node.updateTotalScore(winID);
			node.updateNumOfVisits();
			node = node.getParent();
		}
		}
	}


	

