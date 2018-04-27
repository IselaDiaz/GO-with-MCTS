package MCTS;

import bot.BotState;

public class MCTS {

	public String playout(BotState node_state) {
		BotState [] players_states=new BotState[2];
		
		players_states[0]=node_state.cloneMyPlayer();
		players_states[1]=node_state.cloneOppositePlayer();
		
		

		return 
	}
	
}
