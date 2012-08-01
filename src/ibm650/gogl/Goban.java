	package ibm650.gogl;

import ibm650.gogl.datastructure.Color;

public class Goban {
	
	public static void main(String[] args) {
		Goban goban = new Goban();
		goban.setCross(Color.WHITE, 0, 0);
		goban.setCross(Color.WHITE, DIMENSION-1, 0);
		goban.setCross(Color.BLACK, 0, DIMENSION-1);
		goban.setCross(Color.BLACK, DIMENSION-1, DIMENSION-1);
		System.out.println(goban);
	}
	
	static public short DIMENSION = 9;
	private Cross[] matrix;
	
	public Goban() {
		matrix = new Cross[DIMENSION * DIMENSION];
		for (short i = 0; i < matrix.length; i++) {
			matrix[i] = new Cross();
		}
	}
	
	public Color getColor(int x, int y){
		return matrix[x*DIMENSION + y].getColor();
	}
	
	public boolean checkValidMove(Color color, int x, int y){
		// TODO add control stuff
		// TODO suicide control
		if(getColor(x, y) != Color.EMPTY){
			return false;
		}
		return true;
	}
	
	public boolean move(Color color, int x, int y){
		if(color != Color.EMPTY && checkValidMove(color, x, y)){
			
			matrix[x*DIMENSION + y].setColor(color);
			return true;
		}
		return false;
	}
	
	@Override
	public String toString() {
		StringBuffer string = new StringBuffer();
		// TODO Auto-generated method stub
		for (short x = (short) (DIMENSION-1); x >= 0; x--) {
			for (short y = 0; y < DIMENSION; y++) {
				string.append(getColor(x, y));
				string.append(' ');
			}
			string.append('\n');
		}
		return string.toString();
	}
}
