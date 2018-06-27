package org.ploxie.game.components;

import org.ploxie.engine2.pipeline.uniformbuffers.CameraBuffer;
import org.ploxie.engine2.pipeline.uniformbuffers.TestBuffer;
import org.ploxie.engine2.scenegraph.GameObject;
import org.ploxie.engine2.scenegraph.component.Component;
import org.ploxie.engine2.scenegraph.component.RenderComponent;
import org.ploxie.engine2.scenegraph.component.interfaces.Updatable;
import org.ploxie.utils.math.Math;
import org.ploxie.utils.math.matrix.Matrix4f;
import org.ploxie.utils.math.vector.Vector3f;

import lombok.Data;

@Data
public class TestComponent extends Component implements Updatable {
	
	private CameraBuffer cameraBuffer;
	private TestBuffer testBuffer;
	
	private long x;
	
	private Matrix4f mvp = new Matrix4f();
	private Vector3f pos = new Vector3f();
	
	@Override
	public void initialize() {
		RenderComponent renderComponent = getParent().getComponent(RenderComponent.class);
		cameraBuffer = (CameraBuffer) renderComponent.getRenderInfo().getPipeline().getUniformBuffers().get(0);
		testBuffer = (TestBuffer) renderComponent.getRenderInfo().getPipeline().getUniformBuffers().get(1);
		
		cameraBuffer.setMvp(mvp);
		testBuffer.setVec(pos);
		
		x = System.currentTimeMillis(); 
	}
	
	@Override
	public void update() {
		long y = System.currentTimeMillis() - x;
		float z = (float) Math.sin((y / 1000.0f));
				
		mvp.setScale(new Vector3f(Math.abs(z / 2.0f), 0.5f, 1.0f));
		pos.x = z;	
	}
}
