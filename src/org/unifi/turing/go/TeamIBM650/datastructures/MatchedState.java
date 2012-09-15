/*
 * MatchedState.java
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

/**
 * @author Marco Tinacci, Tommaso Turchi
 *
 */
public class MatchedState {

	private int state;
	private boolean matched;

	public MatchedState(int state, boolean matched) {
		this.state = state;
		this.matched = matched;
	}

	public boolean equals(Object arg0) {
		if (arg0 instanceof MatchedState) {
			MatchedState p = (MatchedState) arg0;
			return (p.state == state) && (p.matched == matched);
		}
		return false;
	}

	public int getState() {
		return state;
	}

	public boolean getMatched() {
		return matched;
	}

	public String toString() {
		return "(" + state + "," + matched + ")";
	}

}
