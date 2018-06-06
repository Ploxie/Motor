package org.ploxie.opengl.shader;

import static org.lwjgl.opengl.GL20.GL_COMPILE_STATUS;
import static org.lwjgl.opengl.GL20.GL_LINK_STATUS;
import static org.lwjgl.opengl.GL20.GL_VALIDATE_STATUS;
import static org.lwjgl.opengl.GL20.glAttachShader;
import static org.lwjgl.opengl.GL20.glCompileShader;
import static org.lwjgl.opengl.GL20.glCreateShader;
import static org.lwjgl.opengl.GL20.glGetProgramInfoLog;
import static org.lwjgl.opengl.GL20.glGetProgrami;
import static org.lwjgl.opengl.GL20.glGetShaderInfoLog;
import static org.lwjgl.opengl.GL20.glGetShaderi;
import static org.lwjgl.opengl.GL20.glLinkProgram;
import static org.lwjgl.opengl.GL20.glShaderSource;
import static org.lwjgl.opengl.GL20.glValidateProgram;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.ploxie.engine2.pipeline.shader.ShaderModule;
import org.ploxie.utils.FileUtils;

import lombok.Data;
import lombok.Getter;


public class GLShaderModule extends ShaderModule{
	
	@Getter
	private final int handle;
	
	public GLShaderModule(int type, String path) {
		String shaderSource = loadShader(path);
	
		handle = glCreateShader(type);
		if (handle == 0) {
			System.err.println(glGetShaderInfoLog(handle, 1024));
			throw new RuntimeException("Failed to create Shader");
		}
		
		glShaderSource(handle, shaderSource);
		glCompileShader(handle);

		if (glGetShaderi(handle, GL_COMPILE_STATUS) == 0) {
			System.err.println(glGetShaderInfoLog(handle, 1024));
			throw new RuntimeException("Failed to compile Shader");
		}		
	}
	
	private String loadShader(String fileName) {
			
		
		StringBuilder shaderSource = new StringBuilder();
		InputStream is = null;
		BufferedReader shaderReader = null;	

		try {
			is = FileUtils.getFile(fileName);
			InputStreamReader sr = new InputStreamReader(is, "UTF-8");
			shaderReader = new BufferedReader(sr);
			String line;
			while ((line = shaderReader.readLine()) != null) {
				shaderSource.append(line).append("\n");
			}

			shaderReader.close();
			

		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}

		return shaderSource.toString();
	}	
	
}
