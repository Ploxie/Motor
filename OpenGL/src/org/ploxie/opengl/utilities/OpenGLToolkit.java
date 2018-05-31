package org.ploxie.opengl.utilities;

import java.util.ArrayList;
import java.util.List;

public class OpenGLToolkit {

	private List<OpenGLObject> cleanupObjects = new ArrayList<OpenGLObject>();
		
	public void cleanup() {
		for(OpenGLObject object : cleanupObjects) {
			object.delete();
		}
	}
}
