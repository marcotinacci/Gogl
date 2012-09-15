/*
 * GoManager.java
 * 
 * Copyright (c) 2012, YOUR_NAME. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3.0 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301  USA
 */
package org.unifi.turing.go;

import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.Observable;
import java.util.Queue;

import org.unifi.turing.go.Goban.Position;
import org.unifi.turing.go.players.Player;

/**
 * 
 * 
 * @author Michele Loreti, Pilu Crescenzi
 * 
 */
public class GoManager extends Observable {

	/**
	 * Integer constant used to identify black player
	 */
	public final static int BLACK_PLAYER = 0;

	/**
	 * Integer constant used to identify white player
	 */
	public final static int WHITE_PLAYER = 1;

	public final static String[] colors = { "black", "white" };

	private boolean matchActive = true;
		
	/**
	 * This flag is true when black player passed in the last turn
	 */
	private boolean[] passed = { false, false };

	/**
	 * Next player
	 */
	private int nextPlayer;

	/**
	 * Previous player
	 */
	private int previousPlayer = -1;
	
	/**
	 * References to player classes
	 */
	private Player[] player;

	/**
	 * A list of info messages published by the GoManager
	 */
	private LinkedList<GoMessageHandler> messages;

	/**
	 * Goban
	 */
	private Goban goban;
	
	private int[][] previous;
	private int[][] current;
	private long[] remainingBudget;
	private boolean[] byoyomi;
	private long budgetTime = DefaultValues.DEFAULT_BUDGET_TIME;
	private long byoyomiTime = DefaultValues.DEFAULT_BYOYOMI_TIME;
	private PrintWriter goLog;

	private char[] gobanLetters = new char[] { 'a', 'b', 'c', 'd', 'e', 'f',
			'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's' };

	/**
	 * Last positioned stone. 
	 */
	private int[] stone;

	private int winner;

	private double[] score;

	public GoManager( Player blackPlayer , Player whitePlayer , PrintWriter goLog ) {
		this( blackPlayer , whitePlayer , goLog , DefaultValues.DEFAULT_SIZE , DefaultValues.DEFAULT_BUDGET_TIME , DefaultValues.DEFAULT_BYOYOMI_TIME );
	}
	
	public GoManager( Player blackPlayer , Player whitePlayer , PrintWriter goLog , int size , long budgetType , long byoyomiTime ) {
		this.goban = new Goban(size);
		this.player = new Player[2];
		this.player[BLACK_PLAYER] = blackPlayer;
		this.player[WHITE_PLAYER] = whitePlayer;
		this.remainingBudget = new long[2];
		this.byoyomi = new boolean[2];
		this.budgetTime = budgetType;
		this.byoyomiTime = byoyomiTime;
		this.goLog = goLog;
		this.messages = new LinkedList<GoMessageHandler>();
		initGame();
		printGoLogHeader();
	}

	/**
	 * This method prints the header of the log file containing math movements.
	 */
	private void printGoLogHeader() {
		goLog.println("(\n;FF[4]\n GM[1]\n CA[utf-8]\n AP[Goban:3.2.12]\n "
				+ "US[Pilu Crescenzi]\n DT[2012-07-27]\n SZ[19]\n HA[0]\n KM[6.5]\n "
				+ "RU[Japanese]\n BR[NR]\n PB["
				+ player[0].getClass().getSimpleName() + "]\n WR[NR]\n PW["
				+ player[1].getClass().getSimpleName() + "]");
	}

	/**
	 * This method computes the final score of a game. The score of a player is
	 * equal to the number of the stones of the player plus the number of
	 * crossing points dominated by the player plus 7.5 if the player is the
	 * white one.
	 * 
	 * @return an array containing the final score.
	 */
	private double[] computeFinalScore() {
		// Mark territories
		for (int r = 0; r < goban.getSize(); r++) {
			for (int c = 0; c < goban.getSize(); c++) {
				if (goban.getStone(r, c) == Stone.NONE) {
					mark(r, c);
				}
			}
		}

		/*
		 * For each player, computes the number of the stones of the player plus
		 * the number of crossing points dominated by the player
		 */
		double[] rst = new double[2];
		rst[0] = 0;
		rst[1] = 0;
		for (int r = 0; r < goban.getSize(); r++) {
			for (int c = 0; c < goban.getSize(); c++) {
				if (goban.getStone(r, c) == Stone.BLACK) {
					rst[0] = rst[0] + 1;
				} else if (goban.getStone(r, c) == Stone.WHITE) {
					rst[1] = rst[1] + 1;
				}
			}
		}
		// Add 7.5 to the score of the white
		rst[1] = rst[1] + 7.5;
		return rst;
	}
	
	/**
	 * This method terminates a game by computing the final score and by
	 * communicating the winner.
	 */
	private void end() {
		score = computeFinalScore();
		winner = score[0] > score[1] ? 0 : 1;
		message("The game is finished. The score of black is "
				+ score[0] + ", the score of white is " + score[1] + ". The "
				+ winner + " player wins!");
		goLog.println(")");
		goLog.close();
		setChanged();
		notifyObservers();
	}

	/**
	 * This method implements the ko rule, that is, checks whether the last move
	 * has returned the game to the position previous to the one in which the
	 * game was before the move.
	 * 
	 * @return true if the ko rule is violated, false otherwise.
	 */
	private boolean isEqualToPrevious() {
		for (int r = 0; r < DefaultValues.DEFAULT_SIZE; r++) {
			for (int c = 0; c < DefaultValues.DEFAULT_SIZE; c++) {
				if (goban.getStone(r, c) == Stone.BLACK && previous[r][c] != 1) {
					return false;
				}
				if (goban.getStone(r, c) == Stone.WHITE && previous[r][c] != 2) {
					return false;
				}
				if (goban.getStone(r, c) == Stone.NONE && previous[r][c] != 0) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * This method computes the crossing points reachable from a specific
	 * crossing point and them mark all these crossing points as belonging to a
	 * black territory (if no white stone has been encountered) or to a white
	 * territory (if no black stone has been encountered). If this is not the
	 * case, the crossing points are marked as dame.
	 * 
	 * @return the marker at position (r,c)
	 */
	private void mark(int r, int c) {
		boolean[][] reached = new boolean[goban.getSize()][goban.getSize()];
		for (int i = 0; i < goban.getSize(); i++) {
			for (int j = 0; j < goban.getSize(); j++) {
				reached[i][j] = false;
			}
		}
		Queue<Integer> q = new LinkedList<Integer>();
		q.add(r * goban.getSize() + c);
		reached[r][c] = true;
		while (!q.isEmpty()) {
			int e = q.poll();
			int r1 = e / goban.getSize();
			int c1 = e % goban.getSize();
			Stone s = goban.getStone(r1, c1);
			if (s != Stone.WHITE && s != Stone.BLACK) {
				LinkedList<Position> neighbors = goban.getNeighbors(r1, c1);
				for (Position p : neighbors) {
					if (!reached[p.getX()][p.getY()]) {
						reached[p.getX()][p.getY()] = true;
						q.add(p.getX() * goban.getSize() + p.getY());
					}
				}
			}
		}
		boolean white = false;
		boolean black = false;
		for (int i = 0; i < goban.getSize(); i++) {
			for (int j = 0; j < goban.getSize(); j++) {
				if (reached[i][j] == true) {
					Stone s = goban.getStone(i, j);
					if (s == Stone.WHITE) {
						white = true;
					}
					if (s == Stone.BLACK) {
						black = true;
					}
				}
			}
		}
		if (white && !black) {
			for (int i = 0; i < goban.getSize(); i++) {
				for (int j = 0; j < goban.getSize(); j++) {
					if (reached[i][j] == true) {
						goban.setStone(Stone.WHITE, i, j);
					}
				}
			}
		} else if (black && !white) {
			for (int i = 0; i < goban.getSize(); i++) {
				for (int j = 0; j < goban.getSize(); j++) {
					if (reached[i][j] == true) {
						goban.setStone(Stone.BLACK, i, j);
					}
				}
			}
		} else if (black && white) {
			for (int i = 0; i < goban.getSize(); i++) {
				for (int j = 0; j < goban.getSize(); j++) {
					Stone s = goban.getStone(i, j);
					if (reached[i][j] == true && s != Stone.BLACK && s != Stone.WHITE) {
						goban.setStone(Stone.DAME, i, j);
					}
				}
			}
		}
	}

	/**
	 * This method reset the game.
	 */
	private void initGame() {
		previous = new int[DefaultValues.DEFAULT_SIZE][DefaultValues.DEFAULT_SIZE];
		current = new int[DefaultValues.DEFAULT_SIZE][DefaultValues.DEFAULT_SIZE];
		for (int r = 0; r < DefaultValues.DEFAULT_SIZE; r++) {
			for (int c = 0; c < DefaultValues.DEFAULT_SIZE; c++) {
				previous[r][c] = 0;
				current[r][c] = 0;
			}
		}
		player[BLACK_PLAYER].black(true);
		player[WHITE_PLAYER].black(false);
		player[BLACK_PLAYER].setTotalTime(budgetTime);
		player[WHITE_PLAYER].setTotalTime(budgetTime);
		player[BLACK_PLAYER].setRemainingTime(budgetTime);
		player[WHITE_PLAYER].setRemainingTime(budgetTime);
		player[BLACK_PLAYER].setByoyomiTime(budgetTime);
		player[WHITE_PLAYER].setByoyomiTime(budgetTime);
		player[BLACK_PLAYER].setByoyomi(false);
		player[WHITE_PLAYER].setByoyomi(false);
		passed[BLACK_PLAYER] = false;
		passed[WHITE_PLAYER] = false;
		nextPlayer = BLACK_PLAYER;
		remainingBudget[BLACK_PLAYER] = budgetTime;
		remainingBudget[WHITE_PLAYER] = budgetTime;
		byoyomi[BLACK_PLAYER] = false;
		byoyomi[WHITE_PLAYER] = false;
	}

	/*
	 * This method copy the goban into the current matrix configuration
	 */
	private void toMatrix() {
		current = new int[getSize()][getSize()];
		for (int r = 0; r < DefaultValues.DEFAULT_SIZE; r++) {
			for (int c = 0; c < DefaultValues.DEFAULT_SIZE; c++) {
				if (goban.getStone(r, c) == Stone.BLACK) {
					current[r][c] = 1;
				}
				if (goban.getStone(r, c) == Stone.WHITE) {
					current[r][c] = 2;
				}
				if (goban.getStone(r, c) == Stone.NONE) {
					current[r][c] = 0;
				}
			}
		}
	}
	
	public boolean step() {
		if (matchActive) {
			matchActive = _step();
			setChanged();
			notifyObservers();
		} 
		return matchActive;
	}

	/*
	 * This method executes one step of the game.
	 */
	private boolean _step() {
		passed[nextPlayer] = false;
		long startTime = System.currentTimeMillis();
		stone = player[nextPlayer].nextStone();
		long endTime = System.currentTimeMillis();
		long timeElapsed = endTime - startTime;
		logStone(nextPlayer, stone);
		// Show the last move
		if (stone != null) {
			passed[nextPlayer] = false;
			message("Player " + colors[nextPlayer] + " just played in ["
					+ stone[0] + "," + stone[1] + "]");
		} else {
			passed[nextPlayer] = true;
			message("Player " + colors[nextPlayer] + " just passed");
		}
		if (!checkTime(timeElapsed)) {
			winner = 1-nextPlayer;
			return false;
		}
		if (stone != null) {
			if (goban.putStoneAt((nextPlayer==0?Stone.BLACK:Stone.WHITE), stone[0], stone[1])) {
				// Black made a valid move
				if (!isEqualToPrevious()) {
					// Move does not violate the ko rule
					previous = current;
					toMatrix();
				} else {
					// Move violates the ko rule: the game ends
					message("Invalid move: The move of the " + colors[nextPlayer]
							+ " player in [" + stone[0] + "," + stone[1]
							+ "] is not valid since it generate a ko: the "
							+ colors[1 - nextPlayer] + " player wins!");
					winner = 1-nextPlayer;
					return false;
				}
			} else {
				// Black did not do a valid move: white wins
				message("Invalid move: The move of the " + colors[nextPlayer]
						+ " player in [" + stone[0] + "," + stone[1]
						+ "] is not valid: the " + colors[1 - nextPlayer]
						+ " player wins!");
				winner = 1-nextPlayer;
				return false;
			}
		}
		if (!byoyomi[nextPlayer]) {
			message("Player "+colors[nextPlayer]+" remaining time: "+remainingBudget[nextPlayer]);			
		}
		
		// Notify the last move to the opponent
		player[1-nextPlayer].nextStone(stone);

		// Store the last moving player
		previousPlayer = nextPlayer;
		
		// Update the player for the next move
		nextPlayer = 1 - nextPlayer;
		
		// If the two players both passed, the game is terminated
		if (passed[BLACK_PLAYER] && passed[WHITE_PLAYER]) {
			end();
			return false;
		}
		setChanged();
		notifyObservers();
		return true;
	}

	/**
	 * This method checks if the nextPlayer has performed the move in
	 * the assigned time slot.
	 * 
	 * @param timeElapsed time used by the player to do the move
	 * @return	true if the player has moved in assigned time
	 */
	private boolean checkTime(long timeElapsed) {
		if (!byoyomi[nextPlayer]
				&& remainingBudget[nextPlayer] + DefaultValues.TIME_DELAY < timeElapsed) {
			message("Time finished: The " + colors[nextPlayer]
					+ " used too much time");
			return false;
		} else if (!byoyomi[nextPlayer]
				&& remainingBudget[nextPlayer] + DefaultValues.TIME_DELAY >= timeElapsed) {
			remainingBudget[nextPlayer] = remainingBudget[nextPlayer]
					- timeElapsed;
			player[nextPlayer].setRemainingTime(remainingBudget[nextPlayer]);
			if (remainingBudget[nextPlayer] < 0) {
				message("Byoyomi: The " + colors[nextPlayer] + " enter byoyomi");
				byoyomi[nextPlayer] = true;
				player[nextPlayer].setByoyomi(true);
			}
		} else {
			if (timeElapsed > byoyomiTime + DefaultValues.BYOYOMI_DELAY) {
				message("Time finished: The " + colors[nextPlayer]
						+ " used too much time");
				return false;
			}
		}
		return true;
	}

	/**
	 * This method stores the movement of player p in the log file.
	 * 
	 * @param p player index
	 * @param stone position of released stone, a null value indicates that p has passed
	 */
	private void logStone(int p, int[] stone) {
		if (stone != null) {
			goLog.print(";" + (p == BLACK_PLAYER ? "B" : "W") + "["
					+ gobanLetters[stone[0]] + gobanLetters[stone[1]] + "]");
		} else {
			goLog.print(";" + (p == BLACK_PLAYER ? "B" : "W") + "[]");
		}
	}

	/** 
	 * Stores message in the message list.
	 * 
	 * @param string
	 */
	private void message(String message) {
		for (GoMessageHandler gmh : messages) {
			gmh.handle(message);
		}
	}

	public int getSize() {
		return goban.getSize();
	}

	public Stone getStone(int i, int j) {
		return goban.getStone(i, j);
	}

	public void addMessageHandler(GoMessageHandler handler) {
		messages.add(handler);
	}

	public boolean isMatchActive() {
		return matchActive;
	}

	public int getLastPlayer() {
		return previousPlayer;
	}
	
	public int[] getLastStone() {
		return stone;
	}

	public String getColor(int playerIndex) {
		return colors[playerIndex];
	}

	public int getWinner() {
		return winner;
	}

	public double[] getScore() {
		return score;
	}
}
