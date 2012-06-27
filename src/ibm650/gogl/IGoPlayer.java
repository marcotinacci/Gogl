package ibm650.gogl;

/**
 * @author marcotinacci
 * Go player interface
 */
public interface IGoPlayer {
	
	/**
	 * Set the color of the player
	 * @param b if true the player is black, else is white
	 */
	public void setBlack( boolean b );
	
	/**
	 * Get the position of the player's next move
	 * @return coordinates of the decided position
	 */
	public int[] nextStone();
	
	/**
	 * Get the position of the enemy's next move
	 * @param coords coordinates of last enemy move, null if enemy have passed
	 */
	void nextStone(int[] coords);
	
	/**
	 * Receive final scores
	 * @param b score of black player
	 * @param w score of white player
	 */
	void finalScore( int b, int w );
}
