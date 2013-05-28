/*
 * AhoCorasick.java
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
package org.unifi.turing.go.TeamIBM650.algorithms;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import org.unifi.turing.go.TeamIBM650.datastructures.Position;

/**
 * @author Marco Tinacci, Tommaso Turchi
 *
 */
abstract class AhoCorasick {

	protected int alphaSize;

	protected int[][] table;

	protected int[] failFun;

	protected ArrayList<Integer>[] outFun;

	protected int[][] patterns;

	/**
	 *
	 */
	public AhoCorasick(int alphaSize) {
		this.alphaSize = alphaSize;
	}

	abstract int getIndex(int alpha);

	@SuppressWarnings("unchecked")
	public ArrayList<Integer> scan(int[][] patterns) {
		ArrayList<Integer> res = new ArrayList<Integer>();

		this.patterns = patterns;

		int m = 1;
		for (int i = 0; i < patterns.length; i++)
			m += patterns[i].length;

		this.table = new int[m][alphaSize];

		this.outFun = new ArrayList[m];
		for (int i = 0; i < m; i++)
			this.outFun[i] = new ArrayList<Integer>();

		this.failFun = new int[m];

		int state = 0;

		int stateCount = 1;

		for (int i = 0; i < this.patterns.length; i++) {
			for (int j = 0; j < this.patterns[i].length; j++) {
				int idxAlpha = this.getIndex(this.patterns[i][j]);

				if (this.table[state][idxAlpha] == 0)
					this.table[state][idxAlpha] = stateCount++;

				state = this.table[state][idxAlpha];
			}

			if (this.outFun[state].size() == 0)
				this.outFun[state].add(i);

			res.add(this.outFun[state].get(0) + 1);

			state = 0;
		}

		this.makeFailFun();

		return res;
	}

	public ArrayList<Position> search(int[] text) {
		ArrayList<Position> res = new ArrayList<Position>();

		int state = 0;

		int i = 0;
		while (i < text.length) {
			int idxAlpha = this.getIndex(text[i]);

			int nextState = this.table[state][idxAlpha];

			if (nextState != 0) {
				state = nextState;

				for (int k = 0; k < this.outFun[state].size(); k++)
					res.add(new Position(this.outFun[state].get(k), i));

				i++;
			} else if (state == 0)
				i++;
			else
				state = this.failFun[state];
		}

		return res;
	}

	protected void makeFailFun() {

		Queue<Integer> q = new LinkedList<Integer>();

		q.add(0);

		while (!q.isEmpty()) {
			int state = q.poll();

			for (int c = 0; c < this.alphaSize; c++) {
				int nextState = this.table[state][c];

				if (nextState != 0) {
					if (state != 0) {
						int rp = this.failFun[state];
						while (rp != 0)
							if (this.table[rp][c] != 0)
								break;
							else
								rp = this.failFun[rp];

						this.failFun[nextState] = this.table[rp][c];

						for (int k = 0; k < this.outFun[this.failFun[nextState]]
								.size(); k++)
							this.outFun[nextState]
									.add(this.outFun[this.failFun[nextState]]
											.get(k));
					}

					q.add(nextState);
				}
			}
		}
	}

}
