/*
 * PatternSeeker.java
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
package org.unifi.turing.go.TeamIBM650;

import java.util.LinkedList;
import java.util.List;

import org.unifi.turing.go.TeamIBM650.datastructures.Kaya;
import org.unifi.turing.go.TeamIBM650.datastructures.Pattern;
import org.unifi.turing.go.TeamIBM650.datastructures.Position;

/**
 * @author Marco Tinacci, Tommaso Turchi
 *
 */
public class PatternSeeker {

	protected Kaya ban;
	private List<Pattern> childPatterns = new LinkedList<Pattern>();

	/**
	 *
	 */
	public PatternSeeker(int n) {
		this.ban = new Kaya(n);
		this.populateLibrary();
	}

	protected LinkedList<Position> seek() {
		LinkedList<Position> candidates = new LinkedList<Position>();
		for (Pattern pattern : this.childPatterns) {
			candidates.addAll(pattern.match(ban.getGoban()));
		}
		return candidates;
	}

	private void populateLibrary() {
		/*
		 * | o | | | | o |
		 */
		this.childPatterns.add(new Pattern(new int[][] { { 1, 0 }, { 0, 1 } },
				new int[] { 0, 1 }));
		this.childPatterns.add(new Pattern(new int[][] { { 1, 2 }, { 0, 1 } },
				new int[] { 1, 0 }));
		this.childPatterns.add(new Pattern(new int[][] { { 1, 0 }, { 2, 1 } },
				new int[] { 0, 1 }));
		/*
		 * | | o | | o | |
		 */
		this.childPatterns.add(new Pattern(new int[][] { { 0, 1 }, { 1, 0 } },
				new int[] { 0, 0 }));
		this.childPatterns.add(new Pattern(new int[][] { { 2, 1 }, { 1, 0 } },
				new int[] { 1, 1 }));
		this.childPatterns.add(new Pattern(new int[][] { { 0, 1 }, { 1, 2 } },
				new int[] { 0, 0 }));

		/*
		 * | x | | | | x |
		 */
		this.childPatterns.add(new Pattern(new int[][] { { 2, 0 }, { 0, 2 } },
				new int[] { 0, 1 }));
		this.childPatterns.add(new Pattern(new int[][] { { 2, 1 }, { 0, 2 } },
				new int[] { 1, 0 }));
		this.childPatterns.add(new Pattern(new int[][] { { 2, 0 }, { 1, 2 } },
				new int[] { 0, 1 }));

		/*
		 * | | x | | x | |
		 */
		this.childPatterns.add(new Pattern(new int[][] { { 0, 2 }, { 2, 0 } },
				new int[] { 0, 0 }));
		this.childPatterns.add(new Pattern(new int[][] { { 1, 2 }, { 2, 0 } },
				new int[] { 1, 1 }));
		this.childPatterns.add(new Pattern(new int[][] { { 0, 2 }, { 2, 1 } },
				new int[] { 0, 0 }));

	}
}
