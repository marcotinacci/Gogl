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
	private int freedom = -1;
	
	private Cross left;
	private Cross right;
	private Cross up;
	private Cross down;
	
	public Cross(Cross left, Cross right, Cross up, Cross down) {
		this.left = left;
		this.right = right;
		this.up = up;
		this.down = down;
		children = new LinkedList<Cross>();
		children.add(this);
	}
	
	public int size(){
		if(master == this) return children.size();
		return master.size();
	}
	
	public void move(Color color) throws BadMoveException{
		// controllo incrocio occupato
		if(getColor() != Color.EMPTY)
			throw new BadMoveException();
		calculateFreedomDegree();
		// controllo suicidio
		if(checkSuicide(color))
			throw new BadMoveException();
		
		setColor(color);
		
		// unisci i gruppi
		if(left != null && left.getColor().equals(getColor())) union(left);
		if(right != null && right.getColor().equals(getColor())) union(right);
		if(up != null && up.getColor().equals(getColor())) union(up);
		if(down != null && down.getColor().equals(getColor())) union(down);
		
		// aggiornamento nemici
		if(left != null && !left.getColor().equals(getColor()) && !left.getColor().equals(Color.EMPTY)) 
			left.decreaseFreedomDegree();
		if(right != null && !right.getColor().equals(getColor()) && !right.getColor().equals(Color.EMPTY)) 
			right.decreaseFreedomDegree();
		if(up != null && !up.getColor().equals(getColor()) && !up.getColor().equals(Color.EMPTY)) 
			up.decreaseFreedomDegree();
		if(down != null && !down.getColor().equals(getColor()) && !down.getColor().equals(Color.EMPTY)) 
			down.decreaseFreedomDegree();
		
	}
	
	private boolean checkSuicide(Color color) {
		// ci sono spazi vuoti
		if(freedom > 0) return false;
		
		// si unisce a un gruppo di grado almeno 2
		if(left != null && left.getColor().equals(color) && left.getFreedom() > 1) return false;
		if(right != null && right.getColor().equals(color) && right.getFreedom() > 1) return false;
		if(up != null && up.getColor().equals(color) && up.getFreedom() > 1) return false;
		if(down != null && down.getColor().equals(color) && down.getFreedom() > 1) return false;
		
		// si cattura un gruppo nemico
		if(left != null && !left.getColor().equals(color) && left.getFreedom() == 1) return false;
		if(right != null && !right.getColor().equals(color) && right.getFreedom() == 1) return false;
		if(up != null && !left.getColor().equals(color) && left.getFreedom() == 1) return false;
		if(down != null && !down.getColor().equals(color) && down.getFreedom() == 1) return false;
		
		return true;
	}

	private void calculateFreedomDegree(){
		if(left != null && left.getColor().equals(Color.EMPTY)) freedom++;
		if(right != null && right.getColor().equals(Color.EMPTY)) freedom++;
		if(up != null && up.getColor().equals(Color.EMPTY)) freedom++;
		if(down != null && down.getColor().equals(Color.EMPTY)) freedom++;
	}
	
	public void union(Cross x){
		// controlla stesso gruppo
		if(this.getMaster().equals(x.getMaster()))
			return;
		
		if(x.size() > this.size()){
			x.setFreedom(x.getFreedom() + this.getFreedom() - 1);
			for (Cross c : this.getChildren()) {
				x.getChildren().add(c);
				c.setMaster(x.getMaster());
			}
			this.setChildren(null);
		}else{
			this.setFreedom(x.getFreedom() + this.getFreedom() - 1);
			for (Cross c : x.getChildren()) {
				this.getChildren().add(c);
				c.setMaster(this.getMaster());
			}
			x.setChildren(null);
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
		if(getMaster() == this){
			this.children = children; 
		}
		setChildren(children);
	}

	public Cross getMaster() {
		return master;
	}
	
	public void setMaster(Cross x){
		master = x;
	}
	
	public int getFreedom() {
		if(getMaster() == this)
			return freedom;
		return getMaster().getFreedom();
	}
	
	private void setFreedom(int freedom) {
		if(getMaster() == this)
			this.freedom = freedom;
		getMaster().setFreedom(freedom);
	}
	
	public void decreaseFreedomDegree(){
		// decrementa grado di liberta'
		left.setFreedom(left.getFreedom()-1);
		if(getFreedom() == 0){
			// gruppo catturato
			this.destroyGroup();
		}
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
		freedom = -1;
		Set<Cross> checkedGroups = new HashSet<Cross>();
		if(left != null){
			left.increaseFreedomDegree();
			checkedGroups.add(left);
		}
		if(right != null && !checkedGroups.contains(right)){
			right.increaseFreedomDegree();
			checkedGroups.add(right);
		}
		if(up != null && !checkedGroups.contains(up)){
			up.increaseFreedomDegree();
			checkedGroups.add(up);
		}
		if(down != null && !checkedGroups.contains(down)){
			down.increaseFreedomDegree();
		}
	}

	private void increaseFreedomDegree() {
		setFreedom(getFreedom()+1);
	}

	@Override
	public boolean equals(Object obj) {
		return obj != null 
				&& obj instanceof Cross 
				&& this.getMaster() == ((Cross)obj).getMaster(); 
	}
	
}
