package org.ploxie.engine2.scenegraph.component;

import org.ploxie.engine2.scenegraph.GameObject;

import lombok.Data;

@Data
public abstract class Component {

	protected GameObject parent;	
	
	protected Component() {
		
	}
	
	public void initialize() {
		
	}
	
}
