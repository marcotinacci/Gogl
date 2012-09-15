/*
 * JGoManagerView.java
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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Observable;
import java.util.Observer;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JToolBar;

import org.unifi.turing.go.players.AbstractPlayer;

public class JGoManagerView extends JFrame implements Observer {
	
	/**
	 * Actions used to create a new match
	 * 
	 * @author Michele Loreti, Pilu Crescenzi
	 *
	 */
	public class NewMatch extends AbstractAction {

		/**
		 * 
		 */
		private static final long serialVersionUID = -4142508296255007348L;


		public NewMatch() {
			super("New match",JGoManagerView.this.createImageIcon(NEW_ICON));
		}
		
		
		@Override
		public void actionPerformed(ActionEvent arg0) {
			StartUpDialog sud = new StartUpDialog(JGoManagerView.this);
			if (sud.select()) {
				doStoreManagerValuesAndCreate(sud.getBlackPlayer(),sud.getWhitePlayer(),sud.getLogDirectory(),sud.getCreationTime(),sud.getBudgetTime(),sud.getByoyomiTime());
			}
		}				
		
	}
	
	/**
	 * Actions used to close the application
	 * 
	 * @author Michele Loreti, Pilu Crescenzi
	 *
	 */
	public class QuitAction extends AbstractAction {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = -8654085762486789831L;

		public QuitAction() {
			super("Exit",JGoManagerView.this.createImageIcon(CLOSE_ICON));
		}
		
		@Override
		public void actionPerformed(ActionEvent arg0) {
			System.exit(1);
		}
		
	}
	
	/**
	 * This actions is used to restart the match.
	 * 
	 * @author Michele Loreti, Pilu Crescenzi
	 *
	 */
	public class RestartAction extends AbstractAction {

		/**
		 * 
		 */
		private static final long serialVersionUID = -1152440908631557646L;

		public RestartAction() {
			super("Restart",JGoManagerView.this.createImageIcon(RESET_ICON));
		}
		
		@Override
		public void actionPerformed(ActionEvent arg0) {
			createManager();
		}
		
	}
	
	/**
	 * This actions is used to display final results.
	 * 
	 * @author Michele Loreti, Pilu Crescenzi
	 *
	 */
	public class ResultAction extends AbstractAction {

		/**
		 * 
		 */
		private static final long serialVersionUID = -1152440908631557646L;

		public ResultAction() {
			super("Final score",JGoManagerView.this.createImageIcon(RESULT_ICON));
		}
		
		@Override
		public void actionPerformed(ActionEvent arg0) {
			showFinalResult();
		}
		
	}
	
	
	/**
	 * This actions is used to run a match until the end.
	 * 
	 * @author Michele Loreti, Pilu Crescenzi
	 *
	 */
	public class RunAction extends AbstractAction {

		/**
		 * 
		 */
		private static final long serialVersionUID = -1152440908631557646L;

		public RunAction() {
			super("Run",JGoManagerView.this.createImageIcon(RUN_ICON));
		}
		
		@Override
		public void actionPerformed(ActionEvent arg0) {
			Thread t = new Thread(new Runnable() {

				@Override
				public void run() {
					setRunning(true);
					while (isRunning()&&(manager.step())) {
						
					}
					setRunning(false);
				}
			});
			t.start();
		}

	}
	
	/**
	 * This actions is used to perform one step of the match.
	 * 
	 * @author Michele Loreti, Pilu Crescenzi
	 *
	 */
	public class StepAction extends AbstractAction {

		/**
		 * 
		 */
		private static final long serialVersionUID = -1152440908631557646L;

		public StepAction() {
			super("Step",JGoManagerView.this.createImageIcon(STEP_ICON));
		}
		
		@Override
		public void actionPerformed(ActionEvent arg0) {
			manager.step();
		}
		
	}
	
	/**
	 * This actions is used to suspend a match that is running.
	 * 
	 * @author Michele Loreti, Pilu Crescenzi
	 *
	 */
	public class StopAction extends AbstractAction {

		/**
		 * 
		 */
		private static final long serialVersionUID = -128037578909701543L;

		public StopAction() {
			super("Stop match",JGoManagerView.this.createImageIcon(STOP_ICON));
		}

		@Override
		public void actionPerformed(ActionEvent arg0) {
			setRunning(false);
		}
		
	}
	
	/**
	 * This action is used to display message panel.
	 * 
	 * @author Michele Loreti, Pilu Crescenzi
	 *
	 */
	public class ViewInfo extends AbstractAction {

		/**
		 * 
		 */
		private static final long serialVersionUID = 2916941242774186816L;

		public ViewInfo() {
			super("View messages",JGoManagerView.this.createImageIcon(INFO_ICON));
		}
		
		@Override
		public void actionPerformed(ActionEvent arg0) {
			messageArea.setVisible(true);
			enableActions();
		}
		
	}
		
	
	private static final String CLOSE_ICON = "icons/close.png";

	private static final String INFO_ICON = "icons/info.png";

	private static final String NEW_ICON = "icons/new.png";
	
	private static final String RESET_ICON = "icons/reset.png";
	
	private static final String RESULT_ICON = "icons/flag_yellow.png";

	private static final String RUN_ICON = "icons/run.png";

	private static final long serialVersionUID = 5189295122586732756L;
	
	private static final String STEP_ICON = "icons/step.png";
	
	private static final String STOP_ICON = "icons/stop.png";
	
	public static void main(String[] args) throws IOException {
		new JGoManagerView();
	}
	
	private Class<?> blackPlayerClass;
	
	private long budgetTime;
	
	private long byoyomiTime;
	
	private long creationTime;
	
	private GobanPanel drawingArea;
	
	private ViewInfo infoAction;
	
	private File logDirectory;
	
	private GoManager manager;
	
	private GoMessageDialog messageArea;
	
	private NewMatch newAction;

	private QuitAction quitAction;

	private RestartAction resetAction;

	private ResultAction resultAction;

	private RunAction runAction;

	private boolean running = false;

	private StepAction stepAction;
		
	private StopAction stopAction;

	private Class<?> whitePlayerClass;

	private JGoManagerView() throws IOException {
		super();
		setUp();
		setTitle("GO game manager");
		createActions();
		setUpMenuBar();
		setUpToolbar();
		setLocationRelativeTo(null);
		setResizable(true);
		setVisible(true);
	}
	
	private void createActions() {
		newAction = new NewMatch();
		stepAction = new StepAction();
		runAction = new RunAction();
		resetAction = new RestartAction();
		quitAction = new QuitAction();
		infoAction = new ViewInfo();
		stopAction = new StopAction();
		resultAction = new ResultAction();
	}
	
	/** Returns an ImageIcon, or null if the path was invalid. */
	private ImageIcon createImageIcon(String path) {
	    java.net.URL imgURL = getClass().getResource(path);
	    if (imgURL != null) {
	        return new ImageIcon(imgURL);
	    } else {
	        System.err.println("Couldn't find file: " + path);
	        return null;
	    }
	}

	/*
	 * This method creates a new manager.
	 */
	private void createManager() {
		this.manager = instantiateManager();
		this.drawingArea.setManager(manager);
		this.manager.addObserver(this);
		if (this.manager != null) {
			this.manager.addMessageHandler(messageArea);
		}
		enableActions();
		repaint();
	}

	protected void doStoreManagerValuesAndCreate(Class<?> blackPlayerClass, Class<?> whitePlayerClass,
			File logDirectory, long creationTime, long budgetTime,
			long byoyomiTime) {
		this.blackPlayerClass = blackPlayerClass; 
		this.whitePlayerClass = whitePlayerClass;
		this.logDirectory = logDirectory;
		this.creationTime = creationTime;
		this.budgetTime = budgetTime;
		this.byoyomiTime = byoyomiTime;
		createManager();
	}
	
	private void enableActions() {
		stepAction.setEnabled((manager != null)&&(manager.isMatchActive())&&!isRunning());
		runAction.setEnabled((manager != null)&&(manager.isMatchActive())&&!isRunning());
		resetAction.setEnabled(manager != null);
		stopAction.setEnabled(isRunning());
		resultAction.setEnabled((manager != null)&&(!manager.isMatchActive()));
	}

	private GoManager instantiateManager() {
		AbstractPlayer black;
		AbstractPlayer white;
		PrintWriter log;
		try {
			long start = System.currentTimeMillis();
			black = (AbstractPlayer) blackPlayerClass.newInstance();
			long end = System.currentTimeMillis();
			long elapsed = end-start;
			if (elapsed > creationTime) {
				JOptionPane.showMessageDialog(this, "Class "+blackPlayerClass.getSimpleName()+" excedes instantiation time!", "Error...", JOptionPane.ERROR_MESSAGE);
				return null;							
			}
		} catch (InstantiationException e) {
			JOptionPane.showMessageDialog(this, "Class "+blackPlayerClass.getSimpleName()+" instantiation error: "+e.getMessage(), "Error...", JOptionPane.ERROR_MESSAGE);
			return null;							
		} catch (IllegalAccessException e) {
			JOptionPane.showMessageDialog(this, "Class "+blackPlayerClass.getSimpleName()+" instantiation error: "+e.getMessage(), "Error...", JOptionPane.ERROR_MESSAGE);
			return null;							
		} 
		try {
			long start = System.currentTimeMillis();
			white = (AbstractPlayer) whitePlayerClass.newInstance();
			long end = System.currentTimeMillis();
			long elapsed = end-start;
			if (elapsed > creationTime) {
				JOptionPane.showMessageDialog(this, "Class "+whitePlayerClass.getSimpleName()+" excedes instantiation time!", "Error...", JOptionPane.ERROR_MESSAGE);
				return null;							
			}
		} catch (InstantiationException e) {
			JOptionPane.showMessageDialog(this, "Class "+whitePlayerClass.getSimpleName()+" instantiation error: "+e.getMessage(), "Error...", JOptionPane.ERROR_MESSAGE);
			return null;							
		} catch (IllegalAccessException e) {
			JOptionPane.showMessageDialog(this, "Class "+whitePlayerClass.getSimpleName()+" instantiation error: "+e.getMessage(), "Error...", JOptionPane.ERROR_MESSAGE);
			return null;							
		} 
		try {
			log = new PrintWriter(new File(logDirectory,"match_"+blackPlayerClass.getSimpleName()+"_"+whitePlayerClass.getSimpleName()+"_"+System.currentTimeMillis()+".sgf"));
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(this, e.getMessage(), "Error...", JOptionPane.ERROR_MESSAGE);
			return null;							
		}
		return new GoManager(black,white,log,DefaultValues.DEFAULT_SIZE,budgetTime,byoyomiTime);
	}
	
	private synchronized boolean isRunning() {
		return running;
	}
	
	private void resetTitle() {
		int playerIndex = manager.getLastPlayer();
		if (playerIndex >= 0) {
			int[] stone = manager.getLastStone();
			if (stone != null) {
				setTitle("GO game manager: "+manager.getColor(playerIndex)+" just played in ["
						+ stone[0] + "," + stone[1] + "]");
			} else {
				setTitle("GO game manager: "+manager.getColor(playerIndex)+" just passed");	
			}
		}
	}
	
	protected synchronized void setRunning(boolean b) {
		running = b;
		enableActions();
	}

	private void setUp() {
		Container content = getContentPane();
		content.setBackground(Color.lightGray);
		drawingArea = new GobanPanel(manager);
		drawingArea.setSize(new Dimension(400, 400));
		drawingArea.setBorder(BorderFactory.createLineBorder(Color.blue, 2));
		drawingArea.setBackground(new Color(252, 186, 84));
		content.add(drawingArea, BorderLayout.CENTER);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		messageArea = new GoMessageDialog();
		messageArea.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				enableActions();
			}
		});
	}

	private void setUpMenuBar() {
		JMenuBar menuBar = new JMenuBar();
		JMenu matchMenu = new JMenu("Match");
		matchMenu.add(new JMenuItem(newAction));
		matchMenu.add(new JMenuItem(stepAction));
		matchMenu.add(new JMenuItem(stopAction));
		matchMenu.add(new JMenuItem(runAction));
		matchMenu.add(new JMenuItem(resetAction));
		matchMenu.addSeparator();
		matchMenu.add(new JMenuItem(quitAction));
		menuBar.add(matchMenu);		
		JMenu viewMenu = new JMenu("View");
		viewMenu.add(new JMenuItem(infoAction));
		menuBar.add(viewMenu);
		setJMenuBar(menuBar);
		pack();
		enableActions();
	}
	
	private void setUpToolbar() {
		JToolBar toolBar = new JToolBar();
		toolBar.add(newAction);
		toolBar.add(resetAction);
		toolBar.add(stepAction);
		toolBar.add(stopAction);
		toolBar.add(runAction);
		toolBar.add(resultAction);
		toolBar.add(infoAction);
		getContentPane().add(toolBar,BorderLayout.NORTH);
	}
	


	private void showFinalResult() {
		int winner = manager.getWinner();
		double[] score = manager.getScore();
		setTitle("GO game manager: Player "+manager.getColor(winner)+" wins!");
		JOptionPane.showMessageDialog(this, "The game is finished."+ 
				(score!=null?"The score of "+manager.getColor(0)+" is "
				+ score[0] + ", the score of "+manager.getColor(1)+" is " + score[1] + ".":"")+" The "
				+ manager.getColor(winner) + " player wins!" ,"Final score...",JOptionPane.INFORMATION_MESSAGE);
	}


	@Override
	public void update(Observable arg0, Object arg1) {
		enableActions();
		resetTitle();
		if (!manager.isMatchActive()) {
			showFinalResult();
		} 
	}
}
