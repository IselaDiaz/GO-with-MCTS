package MCTS;

import bot.BotState;
import bot.BotParser;
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
        //System.out.println("im in selectMove");
		BotState currentBoardState = BotParser.currentState;
		int timeBank = currentBoardState.getTimebank();
		int maxRounds = currentBoardState.getMaxRounds();
		int roundNumber = currentBoardState.getRoundNumber();
        //System.out.println("timebank " +timeBank);
		double computationalTimePerMove = timeBank/(maxRounds-roundNumber+1);
        //System.out.println("comptime " +computationalTimePerMove);
		long start = System.currentTimeMillis();
		long end = (long) (start + computationalTimePerMove);
		//System.out.println("start " +start);
        //System.out.println("end " +end);
		AINode rootNode = new AINode(currentBoardState, null, null);
		while (System.currentTimeMillis() < end)  {

            //System.out.println("im in first while");
			AINode currNode = rootNode;
			//visited.add(currNode);

            while (currNode.getRemainingMoves().size() == 0){
                //System.out.println("im in second while ");
            	currNode = selectWithUCT(currNode);   //Selection with UCT
			}

            AINode newNode = expand(currNode);
            //System.out.println(newNode);
            //System.out.println("i came out after expand");
            //AINode newNode = selectWithUCT(currNode) ;
            //visited.add(newNode);
			int winId = rollOut(newNode);
            //System.out.println("winID " +winId);
			backpropagate(newNode, winId);
		}
        //System.out.println("i came out selectmove");
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
        //System.out.println("im in expand");
		Move randomMove = currNode.getRandomAction();
        //System.out.println("i got random action ");
		AINode childNode = processor.createNextStateFromMove(currNode, randomMove.toString() );
        //System.out.println("i got a childNode ");
        //System.out.println(childNode);
		boolean added = currNode.addChildtoArray(childNode);
        //System.out.println("i am returning a child node");
        return childNode;
	}

	
	
	
	public int rollOut(AINode stateNode){
        //System.out.println("i am in rollout");
		while(!processor.hasGameEnded(stateNode)) {
			Move move=stateNode.randomMove();
			stateNode=processor.createNextStateFromMove(stateNode, move.toString());
            //System.out.println("stateNode " +stateNode);
		}
        //System.out.println("i am done rolling out");
        Integer winID = Integer.valueOf(processor.getWinnerId(stateNode.getState()));
        //System.out.println("winID " +winID);
		return processor.getWinnerId(stateNode.getState());
		//Random r = new Random();
		//return r.nextInt(2);
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


	

