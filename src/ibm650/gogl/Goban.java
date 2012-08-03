	package ibm650.gogl;

import ibm650.gogl.datastructure.Color;
import ibm650.gogl.exception.BadMoveException;

public class Goban {
	
	public static void main(String[] args) {
		Goban goban = new Goban(9);
		try {
			goban.move(Color.WHITE, 0, 0);
			goban.move(Color.WHITE, goban.getDimension()-1, 0);
//			goban.move(Color.BLACK, 0, DIMENSION-1);
			goban.move(Color.BLACK, goban.getDimension()-1, goban.getDimension()-1);
		} catch (BadMoveException e) {
			e.printStackTrace();
		}
		System.out.println(goban);
	}
	
	private short DIMENSION;
	private Cross[] matrix;
	
	private Cross lastBlackMove = null;
	private Cross lastWhiteMove = null;
	
	public Goban(){
		this(19);
	}
	
	public Goban(int dim) {
		DIMENSION = (short)dim;
		
		// instanciate crosses
		matrix = new Cross[DIMENSION * DIMENSION];
		for (int i = 0; i < DIMENSION; i++) {
			for (int j = 0; j < DIMENSION; j++) {
				setCross(new Cross(this, i*DIMENSION+j), i, j);
			}
		}
		for (Cross cross : matrix) {
			cross.setConnections();
		}
	}
	
	public Cross getCross(int x, int y){
		return matrix[x*DIMENSION + y];
	}
	
	private void setCross(Cross cross, int x, int y){
		matrix[x*DIMENSION + y] = cross;
	}
	
	public void move(Color color, int x, int y) throws BadMoveException{
		getCross(x, y).move(color);
		updateLastMove(getCross(x, y));
	}
	
	public short getDimension(){
		return DIMENSION;
	}
	
	@Override
	public String toString() {
		StringBuffer string = new StringBuffer();
		for (short x = (short) (DIMENSION-1); x >= 0; x--) {
			for (short y = 0; y < DIMENSION; y++) {
				string.append(getCross(x, y).getColor().toString().charAt(0));
				string.append(' ');
			}
			string.append('\n');
		}
		return string.toString();
	}

	private void updateLastMove(Cross cross){
		if(cross.getColor().equals(Color.BLACK)){
			lastBlackMove = cross;
		}else if(cross.getColor().equals(Color.WHITE)){
			lastWhiteMove = cross;
		}
	}
	
	public Cross getLastMove(Color color) {
		return color.equals(Color.WHITE) ? lastWhiteMove : lastBlackMove;
	}

	public Cross[] getMatrix() {
		return matrix;
	}
}
