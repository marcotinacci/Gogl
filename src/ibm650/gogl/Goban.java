	package ibm650.gogl;

import ibm650.gogl.datastructure.Color;
import ibm650.gogl.exception.BadMoveException;

public class Goban {
	
	public static void main(String[] args) {
		Goban goban = new Goban();
		try {
			goban.move(Color.WHITE, 0, 0);
			goban.move(Color.WHITE, DIMENSION-1, 0);
//			goban.move(Color.BLACK, 0, DIMENSION-1);
			goban.move(Color.BLACK, DIMENSION-1, DIMENSION-1);
		} catch (BadMoveException e) {
			e.printStackTrace();
		}
		System.out.println(goban);
	}
	
	static public short DIMENSION = 9;
	private Cross[] matrix;
	
	public Goban() {
		// instanciate crosses
		matrix = new Cross[DIMENSION * DIMENSION];
		for (int i = 0; i < DIMENSION; i++) {
			for (int j = 0; j < DIMENSION; j++) {
				setCross(new Cross(), i, j);
			}
		}
		
		// set connections
		for (short i = 0; i < DIMENSION; i++) {
			for(short j = 0; j < DIMENSION; j++){
				Cross left, right, up, down;
				if(i == 0){
					up = null;
					down = getCross(i+1, j);
				}else if(i == DIMENSION-1){
					up = getCross(i-1, j);
					down = null;
				}else{
					up = getCross(i-1, j);
					down = getCross(i+1, j);
				}
				if(j == 0){
					left = null;
					right = getCross(i, j+1);
				}else if(j == DIMENSION-1){
					left = getCross(i, j-1);
					right = null;
				}else{
					left = getCross(i, j-1);
					right = getCross(i, j+1);
				}
				getCross(i, j).setConnections(left, right, up, down);
			}
		}
	}
	
	public Cross getCross(int x, int y){
		return matrix[x*DIMENSION + y];
	}
	
	private void setCross(Cross cross, int x, int y){
		matrix[x*DIMENSION + y] = cross;
	}
	
	public void move(Color color, int x, int y) throws BadMoveException{
		matrix[x*DIMENSION + y].move(color);
	}
	
	@Override
	public String toString() {
		StringBuffer string = new StringBuffer();
		for (short x = (short) (DIMENSION-1); x >= 0; x--) {
			for (short y = 0; y < DIMENSION; y++) {
				string.append(getCross(x, y).getColor());
				string.append(' ');
			}
			string.append('\n');
		}
		return string.toString();
	}
}
