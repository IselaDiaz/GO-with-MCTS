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

package move;

/**
 * move.Move
 * 
 * Stores a move and can convert it to string for output.
 * 
 * @author Jim van Eeden <jim@riddles.io>
 */
public class Move {
	private int x;
	private int y;
	
	public Move(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public String toString() {
		return String.format("place_move %d %d", this.x, this.y);
	}
}
