package org.ploxie.engine.opengl;

import org.ploxie.engine.opengl.scenegraph.GLRenderInfo;
import org.ploxie.engine2.pipeline.uniformbuffers.CameraBuffer;
import org.ploxie.engine2.pipeline.uniformbuffers.TestBuffer;
import org.ploxie.engine2.scenegraph.GameObject;
import org.ploxie.utils.math.Math;
import org.ploxie.utils.math.matrix.Matrix4f;
import org.ploxie.utils.math.vector.Vector3f;

public class TestGameObject extends GameObject{

	private GLRenderInfo renderInfo;
	
	private Matrix4f mvp = new Matrix4f();
	private Vector3f pos = new Vector3f();
	
	private CameraBuffer cameraBuffer;
	private TestBuffer testBuffer;
	
	private long x;
	
	public TestGameObject(GLRenderInfo renderInfo) {
		this.renderInfo = renderInfo;
		
		cameraBuffer = (CameraBuffer) renderInfo.getPipeline().getUniformBuffers().get(0);
		testBuffer = (TestBuffer) renderInfo.getPipeline().getUniformBuffers().get(1);
		cameraBuffer.setMvp(mvp);
		testBuffer.setVec(pos);
		
		x = System.currentTimeMillis(); 
	}
	
	public void render() {		
		long y = System.currentTimeMillis() - x;
		float z = (float) Math.sin((y / 1000.0f));
						
		mvp.setScale(new Vector3f(Math.abs(z / 2.0f), 0.5f, 1.0f));
		pos.x = z;
		
		renderInfo.render();
	}
	
}
