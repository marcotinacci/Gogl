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
		for (int i = 1; i < 3; i++) {
			this.childPatterns.add(new Pattern(new int[][] { { i, 0 },
					{ 3 - i, i } }, new int[] { 0, 1 }));
			this.childPatterns.add(new Pattern(new int[][] { { i, 3 - i },
					{ 0, i } }, new int[] { 1, 0 }));
			this.childPatterns.add(new Pattern(
					new int[][] { { i, 0 }, { 0, i } }, new int[] { 1, 0 }));
			this.childPatterns.add(new Pattern(new int[][] { { 0, i },
					{ i, 3 - i } }, new int[] { 0, 0 }));
			this.childPatterns.add(new Pattern(new int[][] { { 3 - i, i },
					{ i, 0 } }, new int[] { 1, 1 }));
			this.childPatterns.add(new Pattern(
					new int[][] { { 0, i }, { i, 0 } }, new int[] { 0, 0 }));

			this.childPatterns.add(new Pattern(new int[][] { { i, 3 - i, i },
					{ 0, 0, 0 } }, new int[] { 1, 1 }));
			this.childPatterns.add(new Pattern(new int[][] { { 0, 0, 0 },
					{ i, 3 - i, i } }, new int[] { 0, 1 }));
			this.childPatterns.add(new Pattern(new int[][] { { i, 0 },
					{ 3 - i, 0 }, { i, 0 } }, new int[] { 1, 1 }));
			this.childPatterns.add(new Pattern(new int[][] { { 0, i },
					{ 0, 3 - i }, { 0, i } }, new int[] { 1, 0 }));

			for (int t = 0; t < 3; t++)
				for (int j = 0; j < 3; j++) {
					for (int k = 0; k < 3; k++) {
						this.childPatterns.add(new Pattern(new int[][] {
								{ i, 3 - i, k }, { i, 0, 0 }, { t, 0, j } },
								new int[] { 1, 1 }));
						this.childPatterns.add(new Pattern(new int[][] {
								{ k, 0, j }, { 3 - i, 0, 0 }, { i, i, t } },
								new int[] { 1, 1 }));
						this.childPatterns.add(new Pattern(new int[][] {
								{ i, i, t }, { 3 - i, 0, 0 }, { k, 0, j } },
								new int[] { 1, 1 }));
						this.childPatterns.add(new Pattern(new int[][] {
								{ t, 0, j }, { i, 0, 0 }, { i, 3 - i, k } },
								new int[] { 1, 1 }));

						this.childPatterns.add(new Pattern(new int[][] {
								{ i, 3 - i, k }, { 3 - i, 0, 3 - i },
								{ t, 0, j } }, new int[] { 1, 1 }));
						this.childPatterns.add(new Pattern(new int[][] {
								{ k, 3 - i, j }, { 3 - i, 0, 0 },
								{ i, 3 - i, t } }, new int[] { 1, 1 }));
						this.childPatterns.add(new Pattern(new int[][] {
								{ t, 0, j }, { 3 - i, 0, 3 - i },
								{ i, 3 - i, k } }, new int[] { 1, 1 }));

						for (int u = 0; u < 3; u++)
							for (int v = 0; v < 3; v++)
								if ((k != i) && (u != i) && (v != i)) {
									this.childPatterns.add(new Pattern(
											new int[][] { { t, i, j },
													{ 3 - i, 0, 3 - i },
													{ k, u, v } }, new int[] {
													1, 1 }));
									this.childPatterns.add(new Pattern(
											new int[][] { { j, 3 - i, v },
													{ i, 0, u },
													{ t, 3 - i, k } },
											new int[] { 1, 1 }));
									this.childPatterns.add(new Pattern(
											new int[][] { { k, u, v },
													{ 3 - i, 0, 3 - i },
													{ t, i, j } }, new int[] {
													1, 1 }));
								}
					}
					this.childPatterns.add(new Pattern(new int[][] {
							{ i, 3 - i, 0 }, { 0, 0, 0 }, { t, 0, j } },
							new int[] { 1, 1 }));
					this.childPatterns.add(new Pattern(new int[][] {
							{ 0, 0, j }, { 3 - i, 0, 0 }, { i, 0, t } },
							new int[] { 1, 1 }));
					this.childPatterns.add(new Pattern(new int[][] {
							{ i, 0, t }, { 3 - i, 0, 0 }, { j, 0, t } },
							new int[] { 1, 1 }));
					this.childPatterns.add(new Pattern(new int[][] {
							{ t, 0, j }, { 0, 0, 0 }, { i, 3 - i, 0 } },
							new int[] { 1, 1 }));
				}
		}
	}
}
