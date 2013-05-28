/*
 * KnuthMorrisPratt.java
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

import org.unifi.turing.go.TeamIBM650.datastructures.MatchedState;

/**
 * @author Marco Tinacci, Tommaso Turchi
 *
 */
public class KnuthMorrisPratt {

	protected int[] table;

	protected ArrayList<Integer> pattern;

	/**
	 *
	 */
	public KnuthMorrisPratt(ArrayList<Integer> pattern) {
		this.pattern = pattern;

		int m = pattern.size();
		this.table = new int[m];

		int k = 0;
		for (int q = 1; q < m; q++) {
			while (k > 0 && pattern.get(k) != pattern.get(q))
				k = table[k - 1];
			if (k > 0 || pattern.get(k) == pattern.get(q))
				k++;

			this.table[q] = k;
		}
	}

	public ArrayList<Integer> search(int[] text) {
		ArrayList<Integer> res = new ArrayList<Integer>();

		int state = 0;

		for (int i = 0; i < text.length; i++) {
			MatchedState kmpres = searchStep(state, text[i]);
			state = kmpres.getState();
			if (kmpres.getMatched())
				res.add(i);
		}
		return res;
	}

	public MatchedState searchStep(int state, int chr) {
		boolean matched = false;

		while ((state > 0) && (this.pattern.get(state) != chr))
			state = this.table[state - 1];

		if ((state > 0) || (this.pattern.get(state) == chr))
			state++;

		if (state == this.pattern.size()) {
			state = this.table[state - 1];
			matched = true;
		}
		return new MatchedState(state, matched);
	}

}
