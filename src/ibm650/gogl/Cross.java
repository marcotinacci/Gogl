package ibm650.gogl;

import ibm650.gogl.datastructure.Color;

import java.util.LinkedList;
import java.util.List;

public class Cross{
	private Color color = Color.EMPTY;
	private Cross master = this;
	private List<Cross> children;
	
	public Cross() {
		children = new LinkedList<Cross>();
		children.add(this);
	}
	
	public int size(){
		if(master == this) return children.size();
		return master.size();
	}
	
	public void union(Cross x){
		if(x.size() > this.size()){
			for (Cross c : this.getChildren()) {
				x.getChildren().add(c);
				c.setMaster(x.getMaster());
			}
			this.setChildren(null);
		}else{
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
		return children;
	}

	public void setChildren(List<Cross> children) {
		this.children = children;
	}

	public Cross getMaster() {
		return master;
	}
	
	public void setMaster(Cross x){
		master = x;
	}
	
}
