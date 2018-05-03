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

import MCTS.MCTS;

import board.Board;
import bot.BotState;
import goMove.GoMove;
import move.Point;
import node.AINode;
import board.BoardOperations;
import move.Move;


public class GoLogic {

    private int mFoundLiberties; /* Used in recursive flood function */
    private int mNrAffectedFields;
    private boolean[][] mAffectedFields; /* For checking groups */
    private boolean[][] mCheckedFields; /* For checking groups */
    private Boolean mIsTerritory = false;


    public GoLogic() {
        mAffectedFields = new boolean[256][256]; /* This maximizes the board to 256x256 */
        mCheckedFields = new boolean[256][256];
    }

    public void transform(BotState state, GoMove move){
    	switch(move.getMoveType()) {
    		case PLACE:
    			transformPlaceMove(state, move);
    			break;
    		default:
    			break;
            }
    }

    private void transformPlaceMove(BotState state, GoMove move) {
    	Board board = state.getBoard();
    	int playerId=board.getMyId();
    	
    	//MCTS.printing+=String.valueOf(playerId)+" ";
    	
        Move point = move.getCoordinate();

        String[][] originalBoard = getBoardArray(board);

        board.setFieldAt(point, String.valueOf(playerId));
        
        //board.setLastPosition(point);

        int stonesTaken = checkCaptures(board, playerId);
        move.setStonesTaken(stonesTaken);

        if (checkSuicideRule(board, point, String.valueOf(playerId))) { /* Check Suicide Rule */
            board.initializeFromArray(originalBoard);
        }
    }

    public String[][] getBoardArray(Board board) {
        String[][] clone = new String[board.getWidth()][board.getHeight()];
        for(int y = 0; y < board.getHeight(); y++)
            for(int x = 0; x < board.getWidth(); x++)
                clone[x][y] = board.field[x][y];
        return clone;
    }

    /*private Boolean hasNeighbors(GoBoard board, Point p) {
        if (p.x > 0 && !board.getFieldAt(new Point(p.x-1, p.y)).equals(GoBoard.EMPTY_FIELD)) return true;
        if (p.x < board.getWidth() - 1 	&& !board.getFieldAt(new Point(p.x+1, p.y)).equals(GoBoard.EMPTY_FIELD)) return true;
        if (p.y > 0 			&& !board.getFieldAt(new Point(p.x, p.y-1)).equals(GoBoard.EMPTY_FIELD)) return true;
        if (p.y < board.getHeight() - 1   && !board.getFieldAt(new Point(p.x, p.y+1)).equals(GoBoard.EMPTY_FIELD)) return true;
        return false;
    }*/

    private int checkCaptures(Board board, int playerId) {
        int stonesTaken = 0;
        for(int y = 0; y < board.getHeight(); y++) {
            for(int x = 0; x < board.getWidth(); x++) {
                Point point = new Point(x,y);
                String field = board.getFieldAt(point);
                if (!field.equals(Board.EMPTY_FIELD) && !field.equals(String.valueOf(playerId)) && !field.equals("-1")) {
                    mFoundLiberties = 0;
                    boolean[][] mark = new boolean[board.getWidth()][board.getHeight()];
                    for (int tx = 0; tx < board.getHeight(); tx++) {
                        for (int ty = 0; ty < board.getWidth(); ty++) {
                            mAffectedFields[tx][ty] = false;
                            mark[tx][ty] = false;
                        }
                    }
                    flood(board, mark, point, board.getFieldAt(point), 0);
                    if (mFoundLiberties == 0) { /* Group starves */
                        for (int tx = 0; tx < board.getHeight(); tx++) {
                            for (int ty = 0; ty < board.getWidth(); ty++) {
                                if (mAffectedFields[tx][ty]) {
                                    board.setFieldAt(new Move(tx, ty), Board.EMPTY_FIELD);
                                    stonesTaken++;
                                }
                            }
                        }
                    }
                }
            }
        }
        return stonesTaken;
    }

    public Boolean checkSuicideRule(Board board, Move p, String myPlayerId) {
        mFoundLiberties = 0;
        boolean[][] mark = new boolean[board.getWidth()][board.getHeight()];
        for (int tx = 0; tx < board.getWidth(); tx++) {
            for (int ty = 0; ty < board.getHeight(); ty++) {
                mAffectedFields[tx][ty] = false;
                mark[tx][ty] = false;
            }
        }
        flood(board, mark, new Point(p.getX(),p.getY()), myPlayerId, 0);
        return (mFoundLiberties == 0);
    }

    public boolean isBoardFull(Board board) {
        for(int y = 0; y < board.getHeight(); y++)
            for(int x = 0; x < board.getWidth(); x++)
                for (int playerId = 0; playerId <= 1; playerId++)
                    if ((board.getFieldAt(new Point(x,y)).equals(Board.EMPTY_FIELD) || board.getFieldAt(new Point(x,y)).equals("-1"))//should i add -1?
                            && !checkSuicideRule(board, new Move(x,y), String.valueOf(playerId)))
                        return false;
        // No move can be played
        return true;
    }

    // Returns player score according to Tromp-Taylor Rules
    public int calculateScore(Board board, int playerId) {
        int score = this.getPlayerStones(board, playerId);

        if (score <= 0) return 0;

        if (this.getPlayerStones(board, 2 - (playerId + 1)) == 0) { // opponent stones == 0
            if (score <= 1) {
                return score;
            }

            return board.getWidth() * board.getHeight();
        }

        /* Add empty points that reach only playerId color */
        boolean[][] mark = new boolean[board.getHeight()][board.getWidth()];
        mIsTerritory = false;
        mNrAffectedFields = 0;
        for(int y = 0; y < board.getHeight(); y++) {
            for(int x = 0; x < board.getWidth(); x++) {
                mCheckedFields[x][y] = false;
            }
        }

        for(int y = 0; y < board.getHeight(); y++) {
            for(int x = 0; x < board.getWidth(); x++) {
                Point point = new Point(x, y);
                if (board.getFieldAt(point).equals(Board.EMPTY_FIELD) && !mCheckedFields[x][y]) {
                    for (int tx = 0; tx < board.getHeight(); tx++) {
                        for (int ty = 0; ty < board.getWidth(); ty++) {
                            mAffectedFields[tx][ty] = false;
                            mark[tx][ty] = false;

                        }
                    }

                    mIsTerritory = true;
                    mNrAffectedFields = 0;
                    floodFindTerritory(board, mark, point, String.valueOf(playerId), 0);

                    if (mIsTerritory) {
                        score += mNrAffectedFields;
                        for (int tx = 0; tx < board.getHeight(); tx++) {
                            for (int ty = 0; ty < board.getWidth(); ty++) {
                                if (mAffectedFields[tx][ty]) {
                                    mCheckedFields[tx][ty] = true;
                                }
                            }
                        }
                    }
                }
            }
        }
        return score;
    }

    public int getPlayerStones(Board board, int value) {
        int stones = 0;
        for(int y = 0; y < board.getHeight(); y++) {
            for (int x = 0; x < board.getWidth(); x++) {
                if (board.getFieldAt(new Point(x, y)).equals(value + "")) {
                    stones++;
                }
            }
        }
        return stones;
    }

    private void flood(Board board, boolean [][]mark, Point p, String srcColor, int stackCounter) {
        // Make sure row and col are inside the board
        if (p.x < 0) return;
        if (p.y < 0) return;
        if (p.x >= board.getWidth()) return;
        if (p.y >= board.getHeight()) return;

        // Make sure this field hasn't been visited yet
        if (mark[p.x][p.y]) return;

        // Make sure this field is the right color to fill
        if (!board.getFieldAt(p).equals(srcColor)) {
            if (board.getFieldAt(p).equals(Board.EMPTY_FIELD) || board.getFieldAt(p).equals("-1")) {
                mFoundLiberties++;
            }
            return;
        }

        // Fill field with target color and mark it as visited
        mAffectedFields[p.x][p.y] = true;
        mark[p.x][p.y] = true;

        // Recursively check surrounding fields
        if (stackCounter < 1024) {
            flood(board, mark, new Point(p.x - 1, p.y), srcColor, stackCounter+1);
            flood(board, mark, new Point(p.x + 1, p.y), srcColor, stackCounter+1);
            flood(board, mark, new Point(p.x, p.y - 1), srcColor, stackCounter+1);
            flood(board, mark, new Point(p.x, p.y + 1), srcColor, stackCounter+1);
        }
    }

    private void floodFindTerritory(Board board, boolean [][]mark, Point p, String srcColor, int stackCounter) {
    /* Strategy:
     * If edge other than (playerid or 0 or board edge) has been found, then no territory.
     */
        // Make sure row and col are inside the board
        if (p.x < 0) return;
        if (p.y < 0) return;
        if (p.x >= board.getWidth()) return;
        if (p.y >= board.getHeight()) return;

        // Make sure this field hasn't been visited yet
        if (mark[p.x][p.y]) return;

        // Make sure this field is the right color to fill
        if (!board.getFieldAt(p).equals(Board.EMPTY_FIELD) && !board.getFieldAt(p).equals("-1")) {
            if (!board.getFieldAt(p).equals(srcColor)) {
                mIsTerritory = false;
            }
            return;
        }

        mAffectedFields[p.x][p.y] = true;

        // Mark field as visited
        mNrAffectedFields++;
        mark[p.x][p.y] = true;

        if (stackCounter < 1024) {
            floodFindTerritory(board, mark, new Point(p.x - 1, p.y), srcColor, stackCounter+1);
            floodFindTerritory(board, mark, new Point(p.x + 1, p.y), srcColor, stackCounter+1);
            floodFindTerritory(board, mark, new Point(p.x, p.y - 1), srcColor, stackCounter+1);
            floodFindTerritory(board, mark, new Point(p.x, p.y + 1), srcColor, stackCounter+1);
        }
    }

    public boolean detectKo(AINode stateNode) {
        String[][] originalBoard = getBoardArray(stateNode.getState().getBoard());
        String[][] middleBoard;
        String[][] compareBoard;

        if (stateNode.hasParent()) {
            BotState middleState = stateNode.getParent().getState();
            middleBoard = getBoardArray(middleState.getBoard());
            if (stateNode.getParent().hasParent()) {
                BotState compareState = stateNode.getParent().getParent().getState();
                compareBoard = getBoardArray(compareState.getBoard());
                if (BoardOperations.compareFields(originalBoard, compareBoard) &&
                        !BoardOperations.compareFields(originalBoard, middleBoard)) {
                    return true;
                }
            }
        }
        return false;
    }
}