/*
 * Goban.java
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
package org.unifi.turing.go.players;

public abstract class AbstractPlayer implements Player {
	protected int[] lastAdversaryMove;

	private long totalTime;

	private long remainingTime;

	private boolean isByoyomi;

	private long byoyomiTime;

	@Override
	public void finalScore(double b, double w) {
	}

	@Override
	public final void nextStone(int[] position) {
		if (position == null) {
			lastAdversaryMove = null;
		} else {
			lastAdversaryMove = new int[2];
			lastAdversaryMove[0] = position[0];
			lastAdversaryMove[1] = position[1];
		}
	}

	public final long getTotalTime() {
		return totalTime;
	}

	public final void setTotalTime(long totalTime) {
		this.totalTime = totalTime;
	}

	public final long getRemainingTime() {
		return remainingTime;
	}

	public final void setRemainingTime(long remainingTime) {
		this.remainingTime = remainingTime;
	}

	public final boolean isByoyomi() {
		return isByoyomi;
	}

	public final void setByoyomi(boolean isByoyomi) {
		this.isByoyomi = isByoyomi;
	}

	public final long getByoyomiTime() {
		return byoyomiTime;
	}

	public final void setByoyomiTime(long byoyomiTime) {
		this.byoyomiTime = byoyomiTime;
	}




}
