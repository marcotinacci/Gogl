package ibm650.gogl;

import ibm650.gogl.datastructure.Color;
import ibm650.gogl.exception.BadMoveException;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class Cross{ 
	
	private Color color = Color.EMPTY;
	private Cross master = this;
	private List<Cross> children;
	private Set<Cross> emptyNear;
	
	/**
	 * riferimento a goban utilizzato per controllare l'ultima mossa (KO)
	 * e per ricavare le connessioni ai vicini
	 */
	private Goban goban;
	private int id;
	private Set<Cross> neighbourhood;
	
	public Cross(Goban goban, int id) {
		children = new LinkedList<Cross>();
		children.add(this);
		emptyNear = new HashSet<Cross>();
		this.goban = goban;
		this.id = id;
	}
	
	public void setConnections(){
		// costruisci vicinato
		neighbourhood = new HashSet<Cross>();
		if(id >= goban.getDimension())
			neighbourhood.add(goban.getMatrix()[id - goban.getDimension()]);
		if(id < goban.getDimension()*(goban.getDimension()-1))
			neighbourhood.add(goban.getMatrix()[id + goban.getDimension()]);
		if(id % goban.getDimension() > 0)
			neighbourhood.add(goban.getMatrix()[id - 1]);
		if(id % goban.getDimension() < goban.getDimension()-1)
			neighbourhood.add(goban.getMatrix()[id + 1]);
	}
	
	public int size(){
		if(getMaster() == this) return children.size();
		return master.size();
	}
	
	public void move(Color color) throws BadMoveException{
		// controllo incrocio occupato
		if(getColor() != Color.EMPTY)
			throw new BadMoveException();
		calculateFreedomDegree();
		// controllo suicidio e KO
		if(checkSuicide(color) || checkKO(color))
			throw new BadMoveException();

		setColor(color);
		
		// aggiornamento gradi vicini
		for (Cross cross : neighbourhood)
			cross.updateFreedom(this);

		// unisci i gruppi
		for (Cross cross : neighbourhood)
			if(cross.getColor().equals(getColor()))
				union(cross);
		
	}

	private void updateFreedom(Cross cross) {
		if(!this.getColor().equals(Color.EMPTY)){
			this.getEmptyNear().remove(cross);
			if(getFreedom() == 0)
				destroyGroup();
		}
	}
	
	private boolean checkKO(Color color){
		return goban.getLastMove(color) == this;
	}
	
	private boolean checkSuicide(Color color) {
		// ci sono spazi vuoti
		if(getFreedom() > 0) return false;
		
		// si unisce a un gruppo di grado almeno 2
		for (Cross cross : neighbourhood)
			if(cross.getColor().equals(color) && cross.getFreedom() > 1)
				return false;
		
		// si cattura un gruppo nemico
		for (Cross cross : neighbourhood)
			if(!cross.getColor().equals(color) && cross.getFreedom() == 1)
				return false;

		return true;
	}

	private void calculateFreedomDegree(){
		for (Cross cross : neighbourhood)
			if(cross.getColor().equals(Color.EMPTY))
				getEmptyNear().add(cross);
	}
	
	public void union(Cross x){
		// controlla stesso gruppo
		if(this.getMaster().equals(x.getMaster()))
			return;
		
		if(x.size() > this.size()){
			x.getEmptyNear().addAll(this.getEmptyNear());
			for (Cross c : this.getChildren()) {
				x.getChildren().add(c);
				c.setMaster(x.getMaster());
			}
			this.children = null;
			this.emptyNear = null;
		}else{
			this.getEmptyNear().addAll(x.getEmptyNear());
			for (Cross c : x.getChildren()) {
				this.getChildren().add(c);
				c.setMaster(this.getMaster());
			}
			x.children = null;
			x.emptyNear = null;
		}
	}
	
	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public List<Cross> getChildren() {
		if(getMaster() == this){
			return children;
		}
		return getMaster().getChildren();
	}

	public void setChildren(List<Cross> children) {
		if(getMaster() == this)
			this.children = children; 
		else
			getMaster().setChildren(children);
	}

	public Cross getMaster() {
		return master;
	}
	
	public void setMaster(Cross x){
		master = x;
	}
	
	public int getFreedom() {
		return getEmptyNear().size();
	}

	public void destroyGroup() {
		if(getMaster() == this){
			for (Cross child : children) {
				child.destroyPiece();
			}
		}else{
			getMaster().destroyGroup();
		}
		
	}

	public void destroyPiece() {
		setColor(Color.EMPTY);
		for (Cross cross : neighbourhood) {
			cross.getEmptyNear().add(this);
		}
	}

	@Override
	public boolean equals(Object obj) {
		return obj != null 
				&& obj instanceof Cross 
				&& this.getMaster() == ((Cross)obj).getMaster(); 
	}

	public Set<Cross> getEmptyNear() {
		if(master == this)
			return emptyNear;
		return getMaster().getEmptyNear();
	}

	public void setEmptyNear(Set<Cross> emptyNear) {
		if(master == this){
			this.emptyNear = emptyNear;  
		}else{
			getMaster().setEmptyNear(emptyNear);
		}
	}
	
}
