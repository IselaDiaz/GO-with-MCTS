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


/*def monte_carlo_tree_search(root):
    while resources_left(time, computational power):
        leaf = traverse(root) # leaf = unvisited node 
        simulation_result = rollout(leaf)
        backpropagate(leaf, simulation_result)
    return best_child(root)

def traverse(node):
    while fully_expanded(node):
        node = best_uct(node)
    return pick_univisted(node.children) or node # in case no children are present / node is terminal 

def rollout(node):
    while non_terminal(node):
        node = rollout_policy(node)
    return result(node) 

def rollout_policy(node):
    return pick_random(node.children)

def backpropagate(node, result):
   if is_root(node) return 
   node.stats = update_stats(node, result) 
   backpropagate(node.parent)

def best_child(node):
    pick child with highest number of visits*/
	
	
	
	//public Node()
	
	public int playout(Node stateNode) {
		
		while(!processor.hasGameEnded(stateNode)) {
			Move move=randomMove(stateNode);
			stateNode=processor.createNextStateFromMove(stateNode, move.toString());
		}
		return processor.getWinnerId(stateNode.getState());
	}
	
	public Move randomMove(Node stateNode) {
    	int moveCount = stateNode.getAvailableMoves().size();

    	if (moveCount <= 0) {
    		return null;
		}

    	return stateNode.getAvailableMoves().get(this.random.nextInt(moveCount));
    }
	
}
 