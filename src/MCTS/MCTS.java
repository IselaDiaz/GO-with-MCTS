package MCTS;

import bot.BotState;

public class MCTS {

	public String playout(BotState node_state) {
		BotState [] players_states=new BotState[2];
		
		players_states[0]=node_state.clone();
		
		players_states[1]=node_state.clone();
		
		String player0_name=players_states[0].getMyName();
		if(player0_name.substring(player0_name.length() - 1).equals("0"))
			players_states[1].setMyName("player1");
		else players_states[1].setMyName("player0");
			
		
		players_states[1].setMyName(String myName)
		return 
	}
	
}
