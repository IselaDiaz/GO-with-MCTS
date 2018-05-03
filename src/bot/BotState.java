/*
 *  Copyright 2016 riddles.io (developers@riddles.io)
 *
 *      Licensed under the Apache License, Version 2.0 (the "License");
 *      you may not use this file except in compliance with the License.
 *      You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 *      Unless required by applicable law or agreed to in writing, software
 *      distributed under the License is distributed on an "AS IS" BASIS,
 *      WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *      See the License for the specific language governing permissions and
 *      limitations under the License.
 *
 *      For the full copyright and license information, please view the LICENSE
 *      file that was distributed with this source code.
 */

package bot;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import board.Board;
import player.Player;

/**
 * bot.BotState
 *
 * Stores the complete state of the bot as it currently is,
 * Contains settings given by the engine, but also stuff that changes
 * each round, such as the board and the score for each player.
 * 
 * @author Jim van Eeden <jim@riddles.io>
 */

public class BotState {
    private int MAX_TIMEBANK;
    private int TIME_PER_MOVE;
    private int MAX_ROUNDS;
    
    private int roundNumber;
    private int timebank;
    private String myName;
    private HashMap<String, Player> players;
    
    private Board board;

    
    public BotState() {
        this.board = new Board();
        this.players = new HashMap<>();
    }
    
    public void setTimebank(int value) {
        this.timebank = value;
    }
    
    public int getTimebank() {
        return this.timebank;
    }

    public int getMaxTimebank() {
        return MAX_TIMEBANK;
    }

    public void setMaxTimebank(int maxTimebank) {
        MAX_TIMEBANK = maxTimebank;
    }

    public int getTimePerMove() {
        return TIME_PER_MOVE;
    }

    public void setTimePerMove(int TimePerMove) {
        TIME_PER_MOVE = TimePerMove;
    }

    public int getMaxRounds() {
        return MAX_ROUNDS;
    }

    public void setMaxRounds(int MaxRounds) {
        MAX_ROUNDS = MaxRounds;
    }

    public int getRoundNumber() {
        return this.roundNumber;
    }

    public void setRoundNumber(int roundNumber) {
        this.roundNumber = roundNumber;
    }

    public String getMyName() {
        return this.myName;
    }

    public void setMyName(String myName) {
        this.myName = myName;
    }

    public HashMap<String, Player> getPlayers() {
        return this.players;
    }

    public void setPlayers(HashMap<String, Player> players) {
        this.players = players;
    }

    public Board getBoard() {
        return this.board;
    }
    //modified//
    public void setBoard(Board board) {
    	this.board=board;
    }
    
    public BotState clone() {
        BotState Clone = new BotState();
        
        Clone.setMaxTimebank(this.MAX_TIMEBANK);
        Clone.setTimePerMove(this.TIME_PER_MOVE);
        Clone.setMaxRounds(this.MAX_ROUNDS);
        Clone.setRoundNumber(this.roundNumber);
        Clone.setTimebank(this.timebank);
        Clone.setMyName(this.myName);
        Clone.setPlayers(this.players);
        Clone.setBoard(this.board.clone());
        //Clone.
        return Clone;
    }
    
    public void changePlayer() {
    	String origName=this.myName;
    	String playerName;
    	Set<String> playersString=this.players.keySet();

        Iterator<String> iterator = playersString.iterator();
         
        while(iterator.hasNext()) {
        	playerName=iterator.next();
        	if(playerName!=origName) {
        		this.setMyName(playerName);
        		break;
        	}
        }
        int oldMyId=this.board.getMyId();
		this.board.setMyId(this.board.getOpponentId());
		this.board.setOpponentId(oldMyId);
    }
}