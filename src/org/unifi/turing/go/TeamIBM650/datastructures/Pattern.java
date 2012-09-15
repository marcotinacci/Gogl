/*
 * Pattern.java
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
package org.unifi.turing.go.TeamIBM650.datastructures;

import java.util.ArrayList;

import org.unifi.turing.go.TeamIBM650.algorithms.BakerBird;

/**
 * @author Marco Tinacci, Tommaso Turchi
 *
 */
public class Pattern {

	private BakerBird patternSearch;

	private int[] move;

	/**
	 *
	 */
	public Pattern(int[][] pattern, int[] move) {
		this.patternSearch = new BakerBird(pattern);
		this.move = move;
	}

	/**
	 * @param text
	 * @return
	 * @see org.unifi.turing.go.TeamIBM650.algorithms.BakerBird#match(int[][])
	 */
	public ArrayList<Position> match(int[][] text) {
		ArrayList<Position> matches = patternSearch.match(text);
		for (Position match : matches) {
			match.setX(match.getX() + this.move[0] - 1);
			match.setY(match.getY() + this.move[1] - 1);
		}
		return matches;
	}

}
