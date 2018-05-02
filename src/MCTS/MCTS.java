package MCTS;

import bot.BotStarter;
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
	
	public static String printing=new String();
	public static int a;
	
	GoProcessor processor = new GoProcessor();
    BotState currentBoardState ;
    public MCTS (BotState currentBoardState) {
        this.currentBoardState = currentBoardState;
    }
	public GoMove selectMove() {
        //System.out.println("im in selectMove");

		int timeBank = currentBoardState.getTimebank();
		int maxRounds = currentBoardState.getMaxRounds();
		int roundNumber = currentBoardState.getRoundNumber();
        //System.out.println("timebank " +timeBank);
		double computationalTimePerMove = timeBank/(maxRounds-roundNumber+1);
        //System.out.println("comptime " +computationalTimePerMove);
		long start = System.currentTimeMillis();
		long startNano=System.nanoTime();
		long end = (long) (start + computationalTimePerMove);
		long endNano=(long)(startNano+computationalTimePerMove*1e6);
		//System.out.println("start " +start+" end "+end);
        //System.out.println("end " +end);
		double startNano1=startNano/1e6;
		double endNano1=endNano/1e6;
		//System.out.println("startNano "+startNano+"endNano "+endNano);
		//System.out.println("startNano "+startNano1+" endNano "+endNano1+" start "+start+" end"+end);
		AINode rootNode = new AINode(currentBoardState, null, null);
		//while (System.currentTimeMillis() < end)  {
		//System.out.println(System.nanoTime()-endNano);
		while (System.nanoTime()-endNano<500000) {
			//System.out.println(System.nanoTime()-endNano);
            //System.out.println("im in first while");
			AINode currNode = rootNode;
			//visited.add(currNode);

            while (currNode.getRemainingMoves().size() == 0){
                //System.out.println("im in second while ");
            	currNode = selectWithUCT(currNode);   //Selection with UCT
			}
            MCTS.printing+="after selection "+String.valueOf(System.nanoTime())+" ";
            AINode newNode = expand(currNode);
            MCTS.printing+="after expansion "+String.valueOf(System.nanoTime())+" ";
            //System.out.println(newNode);
            //System.out.println("i came out after expand");
            //AINode newNode = selectWithUCT(currNode) ;
            //visited.add(newNode);
			int winId = rollOut(newNode);
			MCTS.printing+="after rollout "+String.valueOf(System.nanoTime())+" ";
            //System.out.println("winID " +winId);
			backpropagate(newNode, winId);
			//System.out.println(printing);
			MCTS.printing+="after backprop "+String.valueOf(System.nanoTime())+" ";
		}
		//System.out.println("startNano "+startNano1+" endNano "+endNano1+" start "+start+" end "+end+" finalTimeNano "+System.nanoTime()+" finalTime "+System.currentTimeMillis());
		//System.out.println("startNano "+startNano1+" endNano "+endNano1+" times "+MCTS.printing);
        //System.out.println("i came out selectmove");
		AINode bestNode = rootNode.getChildWithMaxScore();
		GoMove bestMove = bestNode.getAction();
		return bestMove;
		//return null;
	}


	public AINode selectWithUCT(AINode currNode){
		double epsilon = 1e-6;
		Random r = new Random();
		AINode selected = null;
		double bestValue = Double.MIN_VALUE;
		for (AINode child : currNode.getChildArray()) {
			double uctValue = child.getTotalScore() / (child.getNumOfVisits()/* + epsilon*/) +
					Math.sqrt(Math.log(child.getNumOfVisits()/*+1*/) / (child.getNumOfVisits()/* + epsilon*/)) ;
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
		int c=0;
		String str=new String();
		//System.out.println(!processor.hasGameEnded(stateNode));
		boolean f=!processor.hasGameEnded(stateNode);
		//System.out.println(printing);
		while(!processor.hasGameEnded(stateNode)) {
			//System.out.println("here ");
			Move move=stateNode.randomMove();
			//str+=move.toString()+" ";
			stateNode=processor.createNextStateFromMove(stateNode, move.toString());
			//System.out.println("here ");
			c++;
            //System.out.println("stateNode " +stateNode);
		}
		//System.out.println(printing+"  "+c);
		//System.out.println(c);
		//System.out.println(str);
        //System.out.println("i am done rolling out");
        Integer winID = processor.getWinnerId(stateNode.getState());
		//System.out.println(" " +winID);
        //printing+="  "+"winId "+winID;
        //System.out.println(printing+" "+a);
        //System.out.println("winID " +winID);
		return processor.getWinnerId(stateNode.getState());
		/*Random r = new Random();
		return r.nextInt(2);*/
	}


		

	
	
	
	public void backpropagate(AINode node, int winID){
		//AINode node = node ;
		//printing+="score "+node.getTotalScore()+"no visits "+node.getNumOfVisits()+"parent "+node.getParent()+" ";
		while(node.getParent() != null){
			node.updateTotalScore(winID);
			node.updateNumOfVisits();
			//printing+="score "+node.getTotalScore()+"no visits "+node.getNumOfVisits()+"parent "+node.getParent();
			node = node.getParent();
		}
		}
	}


	

