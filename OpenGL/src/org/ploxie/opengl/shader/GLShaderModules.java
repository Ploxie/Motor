package org.ploxie.opengl.shader;

import static org.lwjgl.opengl.GL20.GL_LINK_STATUS;
import static org.lwjgl.opengl.GL20.GL_VALIDATE_STATUS;
import static org.lwjgl.opengl.GL20.glAttachShader;
import static org.lwjgl.opengl.GL20.glCreateProgram;
import static org.lwjgl.opengl.GL20.glGetProgramInfoLog;
import static org.lwjgl.opengl.GL20.glGetProgrami;
import static org.lwjgl.opengl.GL20.glLinkProgram;
import static org.lwjgl.opengl.GL20.glUseProgram;
import static org.lwjgl.opengl.GL20.glValidateProgram;

import org.ploxie.engine2.pipeline.shader.ShaderModules;

import lombok.Builder;
import lombok.Getter;

@Builder
public class GLShaderModules extends ShaderModules{

	private static int currentProgram = 0;
	
	private GLShaderModule vertexShader;
	private GLShaderModule fragmentShader;
	
	@Getter
	private int id;
	
	public void bind() {
		if (currentProgram != id) {
			glUseProgram(id);
		}
	}

	public void unbind() {
		glUseProgram(0);
	}
	
	public GLShaderModules init() {
		id = glCreateProgram();

		if (id == 0) {
			throw new RuntimeException("Failed to create Shader");
		}
		
		if(vertexShader != null) {
			glAttachShader(id, vertexShader.getHandle());			
		}
		if(fragmentShader != null) {			
			glAttachShader(id, fragmentShader.getHandle());
		}
		
		glLinkProgram(id);

		if (glGetProgrami(id, GL_LINK_STATUS) == 0) {
			System.err.println(glGetProgramInfoLog(id, 1024));
			throw new RuntimeException("Failed to link Shader");
		}

		glValidateProgram(id);

		if (glGetProgrami(id, GL_VALIDATE_STATUS) == 0) {
			System.err.println(glGetProgramInfoLog(id, 1024));
			throw new RuntimeException("Failed to validate Shader");
		}
		return this;
	}
	
	@Override
	public GLShaderModule getVertex() {
		return vertexShader;
	}

	@Override
	public GLShaderModule getFragment() {
		return fragmentShader;
	}

	
	
}
