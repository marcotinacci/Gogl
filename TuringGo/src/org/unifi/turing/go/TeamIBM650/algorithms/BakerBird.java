/*
 * BakerBird.java
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
import org.unifi.turing.go.TeamIBM650.datastructures.Position;

/**
 * @author Marco Tinacci, Tommaso Turchi
 *
 */
public class BakerBird {

	private AhoCorasickGo ACGo = new AhoCorasickGo();

	private KnuthMorrisPratt KMP;

	/**
	 *
	 */
	public BakerBird(int[][] pattern) {
		this.KMP = new KnuthMorrisPratt(this.ACGo.scan(pattern));
	}

	public ArrayList<Position> match(int[][] text) {
		ArrayList<Position> res = new ArrayList<Position>();

		int[] KMPstates = new int[text[0].length];

		for (int i = 0; i < text.length; i++) {
			int[] r = this.getR(text[i]);

			for (int j = 0; j < r.length; j++) {
				MatchedState KMPres = this.KMP.searchStep(KMPstates[j], r[j]);

				KMPstates[j] = KMPres.getState();

				if (KMPres.getMatched())
					res.add(new Position(i - ACGo.patterns.length + 1, j - 1));

			}
		}
		return res;
	}

	private int[] getR(int[] row) {
		ArrayList<Position> acres = this.ACGo.search(row);
		int[] res = new int[row.length];
		for (int i = 0; i < acres.size(); i++)
			res[acres.get(i).getY()] = acres.get(i).getX() + 1;
		return res;
	}

}
