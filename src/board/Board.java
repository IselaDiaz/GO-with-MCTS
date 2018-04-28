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

package board;

import java.util.ArrayList;

import move.Point;
import move.Move;

/**
 * board.Board
 * 
 * Handles everything that has to do with the board, such
 * as storing the current state and performing calculations
 * on the board.
 * 
 * @author Jim van Eeden <jim@riddles.io>
 */

public class Board {
    public static final String EMPTY_FIELD = ".";

    private int myId;
    private int opponentId;
    private int height;
    private int width;
    
	public String[][] field;

	public void parseFromString(String input) {
        this.field = new String[this.width][this.height];
        int x = 0;
        int y = 0;

		for (String cell : input.split(",")) {
		    this.field[x][y] = cell.trim();

            if (++x == this.width) {
                x = 0;
                y++;
            }
        }
	}

	/**
	 * Returns a list of all available moves. i.e. empty cells 
	 * that will not result in a suicide move or violate the Ko rule
	 * @return : a list of all available moves in this game state
	 */
	public ArrayList<Move> getAvailableMoves() {
	    ArrayList<Move> moves = new ArrayList<>();
	    
	    for (int y = 0; y < this.height; y++) {
            for (int x = 0; x < this.width; x++) {
                if (isEmptyPoint(x,y)) {
                    moves.add(new Move(x, y));
                }
            }
        }
	    
		return moves;
	}
	
	private boolean isEmptyPoint(int x, int y) {
	    return this.field[x][y].equals(EMPTY_FIELD);
	}
    
    public void setHeight(int value) {
        this.height = value;
    }
    
    public int getHeight() {
        return this.height;
    }

    public void setWidth(int value) {
	    this.width = value;
    }

    public int getWidth() {
	    return this.width;
    }
    
    public void setMyId(int id) {
        this.myId = id;
    }
    
    public void setOpponentId(int id) {
        this.opponentId = id;
    }
    
    //modified
    public int getMyId() {
    	return this.myId;
    }
    
    public int getOpponentId() {
    	return this.opponentId;
    }
    
    //public Board clone()
    public void setFieldAt(Move c, String f) {
        field[c.x][c.y] = f;
    }
    
    public String getFieldAt(Point c) {
        return field[c.x][c.y];
    }
    
    public void initializeFromArray(String[][] array) {
        for (int y = 0; y < this.height; y++) {
            for (int x = 0; x < this.width; x++) {
                field[x][y] = array[x][y];
            }
        }
    }
}