package org.unifi.turing.go;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;

public class GobanPanel extends JPanel implements Observer {
	private static final long serialVersionUID = -5518807760359656417L;
	private static final int STONE_RADIUS = 20;
	private int gobanSize;
	private int gobanWidth;
	private GoManager manager;

	public GobanPanel(GoManager goban) {
		super();
		gobanSize = (goban == null?DefaultValues.DEFAULT_SIZE:goban.getSize());
		gobanWidth = 2 * gobanSize * STONE_RADIUS;
		setSize(new Dimension(gobanWidth, gobanWidth));
		this.manager = goban;
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(gobanWidth, gobanWidth);
	}

	private void paintGoban(Graphics2D g) {
		g.setColor(Color.BLACK);
		for (int i = 0; i < gobanSize; i++) {
			g.drawLine(2 * i * STONE_RADIUS + STONE_RADIUS, STONE_RADIUS, 2 * i
					* STONE_RADIUS + STONE_RADIUS, 2 * (gobanSize - 1)
					* STONE_RADIUS + STONE_RADIUS);
			g.drawLine(STONE_RADIUS, 2 * i * STONE_RADIUS + STONE_RADIUS, 2
					* (gobanSize - 1) * STONE_RADIUS + STONE_RADIUS, 2 * i
					* STONE_RADIUS + STONE_RADIUS);
		}
	}

	@Override
	public void paint(Graphics arg0) {
		super.paint(arg0);
		Graphics2D g2 = (Graphics2D) arg0;
		paintGoban(g2);
		if (manager == null) {
			return ;
		}
		for (int i = 0; i < manager.getSize(); i++) {
			for (int j = 0; j < manager.getSize(); j++) {
				if (manager.getStone(i, j) != Stone.NONE) {
					if (manager.getStone(i, j) == Stone.BLACK) {
						g2.setColor(Color.BLACK);
					} else if (manager.getStone(i, j) == Stone.WHITE) {
						g2.setColor(Color.WHITE);
					} else if (manager.getStone(i, j) == Stone.WHITE_TERRITORY) {
						g2.setColor(Color.GREEN);
					} else if (manager.getStone(i, j) == Stone.BLACK_TERRITORY) {
						g2.setColor(Color.BLUE);
					} else if (manager.getStone(i, j) == Stone.DAME) {
						g2.setColor(Color.GRAY);
					}
					g2.fillOval(2 * i * STONE_RADIUS + 4, 2 * j * STONE_RADIUS
							+ 4, 2 * STONE_RADIUS - 8, 2 * STONE_RADIUS - 8);
				}
			}
		}
	}

	protected void setManager(GoManager manager) {
		this.manager = manager;
		this.manager.addObserver(this);
		repaint();
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		repaint();
	}


}