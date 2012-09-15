/*
 * BasicChain.java
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

import java.util.Set;

import org.unifi.turing.go.Goban.Position;

/**
 * @author Michele Loreti, Pilu Crescenzi
 *
 */
public class BasicChain implements Chain {

	private Chain parent;
	private Position element;
	
	public BasicChain(int i, int j) {
		this.element = null;
	}

	/* (non-Javadoc)
	 * @see org.unifi.turing.go.Chain#freedomSet()
	 */
	@Override
	public Set<Position> freedomSet() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.unifi.turing.go.Chain#chainElements()
	 */
	@Override
	public Set<Position> chainElements() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.unifi.turing.go.Chain#getStone()
	 */
	@Override
	public Stone getStone() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.unifi.turing.go.Chain#freedomDegree()
	 */
	@Override
	public int freedomDegree() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void remove() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Chain join(Chain c) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Chain getParent() {
		// TODO Auto-generated method stub
		return null;
	}

}
