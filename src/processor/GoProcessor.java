/*
 * Copyright 2016 riddles.io (developers@riddles.io)
 *
 *     Licensed under the Apache License, Version 2.0 (the "License");
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 *
 *     For the full copyright and license information, please view the LICENSE
 *     file that was distributed with this source code.
 */

package processor;

//import java.util.ArrayList;

import MCTS.MCTS;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import bot.BotState;
import goMove.GoMove;
import goMove.GoMoveDeserializer;
import node.AINode;
//import node.Node;
import player.Player;

/**
 * io.riddles.go.game.processor.GoProcessor - Created on 6/27/16
 *
 * [description]
 *
 * @author Joost - joost@riddles.io, Jim van Eeden - jim@riddles.io
 */
public class GoProcessor{

    private GoLogic logic;

    public GoProcessor() {
        this.logic = new GoLogic();
    }

    public AINode createNextStateFromMove(AINode stateNode, String input) {

        /* Clone playerStates for next State */
        //ArrayList<GoPlayerState> nextPlayerStates = clonePlayerStates(state.getPlayerStates());

        //GoState nextState = new GoState(state, nextPlayerStates, roundNumber);
        //nextState.setPlayerId(input.getPlayerId());

        //GoPlayerState playerState = getActivePlayerState(nextPlayerStates, input.getPlayerId());
        //playerState.setPlayerId(input.getPlayerId());

    	 // parse the response
        GoMoveDeserializer deserializer = new GoMoveDeserializer();
        GoMove move = deserializer.traverse(input);
        //playerState.setMove(move);
    	
    	//parent updated here and also changed stuff for new player
    	AINode nextStateNode=new AINode(stateNode.getState().clone(),stateNode,move);//parent updated here
    	
        BotState nextState=nextStateNode.getState();
        
        try {
            logic.transform(nextState, move);
        } catch (Exception e) {
            //LOGGER.info(String.format("Unknown response: %s", input.getValue()));
        }

        /* Determine double passes */

        /* Set Ko */
        /*if (logic.detectKo(stateNode) {
            nextState.setKoPlayerId(input.getPlayerId());
        }*/

        /* Update player stats */
        Set<String> playersString=nextState.getPlayers().keySet();
        int score;
        
        Iterator<String> iterator = playersString.iterator();
        while(iterator.hasNext()) {
        	Player player=nextState.getPlayers().get(iterator.next());
        	score=logic.calculateScore(nextState.getBoard(), getIdFromPlayer(player,nextState));//add komi?
        	//MCTS.printing+="Player"+String.valueOf(getIdFromPlayer(player,nextState))+" "+score+" ";
        	player.setPoints(score);
        }

        //change rounds?
        updateRoundNumber(nextState);
        //MCTS.printing+=String.valueOf(nextState.getRoundNumber())+" ";
        //alternating? more stuff to change
        nextStateNode.getState().changePlayer();
        //MCTS.printing+=String.valueOf(nextState.getMyName());
        //nextStateNode.setAvailableMoves();
        nextStateNode.setRemainingMoves();
        //MCTS.printing+="I come out ";
        return nextStateNode;
    }

    
    public void updateRoundNumber(BotState state) {
    	if(state.getBoard().getMyId()==1)
    		state.setRoundNumber(state.getRoundNumber()+1);
    }
    
    
    private int getIdFromPlayer(Player player, BotState state) {
    	if(player.getName().equals(state.getMyName()))
    		return state.getBoard().getMyId();
    	else
    		return state.getBoard().getOpponentId();
    }


    public boolean hasGameEnded(AINode stateNode) {
    	BotState state=stateNode.getState();
    	//MCTS.printing+=state.getRoundNumber()+" "+state.getMaxRounds();
    	//System.out.println("Game ended?");
        if (state.getRoundNumber() >= /*50*/state.getMaxRounds()) {
        	//MCTS.printing+="TRUE ";
        	return true;
        }
        //MCTS.printing+=logic.isBoardFull(state.getBoard())+" "+logic.detectKo(stateNode);
        return logic.isBoardFull(state.getBoard()) || logic.detectKo(stateNode);
    }

    /* Returns winner playerId, or null if there's no winner. */
    public Integer getWinnerId(BotState state) {
        HashMap<String,Player> players = state.getPlayers();
        double scorePlayer0=0;
        double scorePlayer1=0;
        Integer winnerId = Integer.MIN_VALUE;


        //System.out.println("winID " +winnerId);
        Set<String> playersString=players.keySet();
        
        Iterator<String> iterator = playersString.iterator();
        MCTS.a=0;
        while(iterator.hasNext()) {
        	//MCTS.a++;
            //System.out.println("in get winner id while loop " );
        	Player player=players.get(iterator.next());
            //System.out.println("player  " +player);
        	int playerId=getIdFromPlayer(player,state);
            //System.out.println("playerId" +playerId);
			if(playerId==0) {
                scorePlayer0 = player.getPoints();
                //System.out.println("player 0 score " + scorePlayer0);
                //MCTS.printing+="player 0 score " + scorePlayer0+" ";
            }
        	else {
                scorePlayer1 = player.getPoints()+7.5;//komi
                //System.out.println("player 1 score " + scorePlayer1);
                MCTS.a++;
                //MCTS.printing+="player 1 score " + scorePlayer1+" ";
            }
        }
        //System.out.println("player 1 score " +scorePlayer1);
        if (scorePlayer0 > scorePlayer1) winnerId = 0;
        if (scorePlayer1 > scorePlayer0) winnerId = 1;
        
        return winnerId;
    }
}
