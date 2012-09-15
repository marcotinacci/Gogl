/*
 * GoMessagePanel.java
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

import java.awt.Color;

import javax.swing.JDialog;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * @author Michele Loreti, Pilu Crescenzi
 *
 */
public class GoMessageDialog extends JDialog implements GoMessageHandler {

	private JTextArea area;
	
	public GoMessageDialog( ) {
		super();
		setTitle("Go Manager: Info messages");
		setModal(false);
		this.area = new JTextArea();
		this.area.setEditable(false);
		this.area.setBackground(Color.WHITE);
		setContentPane(new JScrollPane(area));
		setSize(600, 400);
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void handle(String message) {
		area.append(message+"\n");
		area.setCaretPosition(area.getText().length());
//		repaint();
	}

	public void clear() {
		area.setText("");
	}

}
