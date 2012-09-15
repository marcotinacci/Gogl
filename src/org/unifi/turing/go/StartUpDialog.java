/*
 * StartUpDialog.java
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

import java.awt.Container;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.unifi.turing.go.players.AbstractPlayer;

/**
 * @author Michele Loreti, Pilu Crescenzi
 *
 */
public class StartUpDialog extends JDialog {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -691769659647523582L;

	private JTextField blackPlayerClass;
	
	private JTextField whitePlayerClass;
	
	private JTextField creationTime;

	private JTextField budgetTime;
	
	private JTextField byoyomiTime;
	
	private JTextField logDirectory;
	
	private boolean accepted = false;
	
	public StartUpDialog(Frame main) {
		super(main,true);
		setTitle("New match...");
		setContentPane(createContentPane());
		pack();
	}

	private Container createContentPane() {
		JPanel mainPane = new JPanel(new GridLayout(7, 1));
		JPanel foo = new JPanel();
		foo.add(new JLabel("Black Player:"));
		blackPlayerClass = new JTextField(50);
		foo.add(blackPlayerClass);
		mainPane.add(foo);
		foo = new JPanel();
		foo.add(new JLabel("White Player:"));
		whitePlayerClass = new JTextField(50);
		foo.add(whitePlayerClass);
		mainPane.add(foo);
		
		foo = new JPanel();
		foo.add(new JLabel("Log directory:"));
		logDirectory = new JTextField(50);
		foo.add(logDirectory);
		java.net.URL imgURL = getClass().getResource("icons/folder_explore.png");
		JButton button = new JButton(new ImageIcon(imgURL));
		button.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser jfc = new JFileChooser();
				jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				if (jfc.showOpenDialog(StartUpDialog.this)==JFileChooser.APPROVE_OPTION) {
					File selected = jfc.getSelectedFile();
					if (selected != null) {
						logDirectory.setText(selected.getAbsolutePath());
					}
				}
				
			}
			
		});
		foo.add(button);
		mainPane.add(foo);
		
		
		foo = new JPanel();
		foo.add(new JLabel("Player creation time:"));
		creationTime = new JTextField(10);
		creationTime.setText(DefaultValues.DEFAULT_PLAYER_CREATION_TIME+"");
		foo.add(creationTime);
		mainPane.add(foo);
		foo = new JPanel();
		foo.add(new JLabel("Match time (per player):"));
		budgetTime = new JTextField(10);
		budgetTime.setText(DefaultValues.DEFAULT_BUDGET_TIME+"");
		foo.add(budgetTime);
		mainPane.add(foo);
		foo = new JPanel();
		foo.add(new JLabel("Byoyomi time:"));
		byoyomiTime = new JTextField(10);
		byoyomiTime.setText(DefaultValues.DEFAULT_BYOYOMI_TIME+"");
		foo.add(byoyomiTime);
		mainPane.add(foo);
		
		foo = new JPanel();
		imgURL = getClass().getResource("icons/accept.png");
		button = new JButton(new ImageIcon(imgURL));
		button.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				doAccept(true);
			}
			
		});
		foo.add(button);
		imgURL = getClass().getResource("icons/cancel.png");
		button = new JButton(new ImageIcon(imgURL));
		button.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				doAccept(false);
			}
			
		});
		foo.add(button);
		mainPane.add(foo);
		return mainPane;
	}
	
	protected void doAccept(boolean b) {
		accepted = b;
		setVisible(false);
	}

	private boolean doValidateFields() {
		if (!validatePlayerField(blackPlayerClass.getText())) {
			return false;
		}
		if (!validatePlayerField(whitePlayerClass.getText())) {
			return false;
		}
		if (!validateLogField(logDirectory.getText())) {
			return false;
		}
		if (!validateTimeFields("Player creation time",creationTime.getText())) {
			return false;
		}
		if (!validateTimeFields("Match time",budgetTime.getText())) {
			return false;
		}
		if (!validateTimeFields("Byoyomi time",byoyomiTime.getText())) {
			return false;
		}
		return true;
	}

	private boolean validateTimeFields(String label, String text) {
		try {
			if (Long.parseLong(text)<=0) {
				JOptionPane.showMessageDialog(this, label+" has to be a positive integer!", "Error...", JOptionPane.ERROR_MESSAGE);
				return false;			
			}
			return true;
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(this, label+" is not an integer value!", "Error...", JOptionPane.ERROR_MESSAGE);
			return false;			
		}
	}

	private boolean validateLogField(String text) {
		File f = new File(text);
		if (!f.exists()) {
			JOptionPane.showMessageDialog(this, "Directory "+text+" does not exists!", "Error...", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		if (!f.isDirectory()) {
			JOptionPane.showMessageDialog(this, text+" is not a directory!", "Error...", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		return true;
	}

	protected boolean validatePlayerField( String className ) {
		try {
			Class<?> c = Class.forName(className);
			if (!(AbstractPlayer.class.isAssignableFrom(c))) {
				JOptionPane.showMessageDialog(this, className+" does not extend AbstractPlayer!", "Error...", JOptionPane.ERROR_MESSAGE);
				return false;
			}
		} catch (ClassNotFoundException e) {
			JOptionPane.showMessageDialog(this, className+" cannot be found!", "Error...", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		return true;
	}
	
	public boolean isAccepted() {
		return accepted;
	}

	public Class<?> getBlackPlayer() {
		try {
			return Class.forName( blackPlayerClass.getText() );
		} catch (ClassNotFoundException e) {
			e.printStackTrace(); //If field validation is passed, this exception is never thrown!
		}
		return null;
	}

	public Class<?> getWhitePlayer() {
		try {
			return Class.forName( whitePlayerClass.getText() );
		} catch (ClassNotFoundException e) {
			e.printStackTrace(); //If field validation is passed, this exception is never thrown!
		}
		return null;
	}

	public File getLogDirectory() {
		return new File(logDirectory.getText());
	}

	public long getCreationTime() {
		return Long.parseLong( creationTime.getText() );
	}
	
	public long getBudgetTime() {
		return Long.parseLong(  budgetTime.getText() );
	}
	
	public long getByoyomiTime() {
		return Long.parseLong(  byoyomiTime.getText() );
	}
	
	public boolean select() {
		do {
			setVisible(true);
		} while ((accepted)&&(!doValidateFields()));
		return accepted;
	}
}
