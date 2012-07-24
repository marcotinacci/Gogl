package ibm650.gogl;

public class Goban {
	
	public static void main(String[] args) {
		Goban goban = new Goban();
		goban.setCross(Cross.WHITE, 0, 0);
//		goban.setCross(Cross.WHITE, DIMENSION-1, 0);
//		goban.setCross(Cross.BLACK, 0, DIMENSION-1);
		goban.setCross(Cross.BLACK, DIMENSION-1, DIMENSION-1);
		System.out.println(goban);
	}
	
	static private short DIMENSION = 9;
	private Cross[] matrix;
	
	public Goban() {
		matrix = new Cross[DIMENSION * DIMENSION];
		for (short i = 0; i < matrix.length; i++) {
			matrix[i] = Cross.EMPTY;
		}
	}
	
	public Cross getCross(int x, int y){
		return matrix[x*DIMENSION + y];
	}
	
	public boolean checkValidMove(Cross stone, int x, int y){
		// TODO add control stuff
		if(getCross(x, y) != Cross.EMPTY){
			return false;
		}
		return true;
	}
	
	public boolean setCross(Cross stone, int x, int y){
		if(checkValidMove(stone, x, y)){
			switch (stone) {
			case BLACK:
			case WHITE:
				matrix[x*DIMENSION + y] = stone;
				break;
			case EMPTY:
				return false;
			}
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
				string.append(getCross(x, y));
				string.append(' ');
			}
			string.append('\n');
		}
		return string.toString();
	}
}
