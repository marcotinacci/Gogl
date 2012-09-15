package org.unifi.turing.go.players;

public interface Player {
	public void black(boolean flag);
	
	public void finalScore(double b, double w);

	public int[] nextStone();

	public void nextStone(int[] position);
	
	public void setTotalTime( long time );

	public long getTotalTime( );

	public void setRemainingTime( long time );
	
	public long getRemainingTime( );
	
	public void setByoyomiTime( long time );

	public long getByoyomiTime();

	public void setByoyomi( boolean flag );
	
	public boolean isByoyomi();

}
