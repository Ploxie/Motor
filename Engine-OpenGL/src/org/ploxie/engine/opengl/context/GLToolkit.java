package org.ploxie.engine.opengl.context;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.nio.ByteBuffer;

import org.lwjgl.opengl.GL20;
import org.ploxie.engine.opengl.scenegraph.GLRenderInfo;
import org.ploxie.engine2.context.GraphicsToolkit;
import org.ploxie.engine2.model.Mesh;
import org.ploxie.engine2.pipeline.Pipeline;
import org.ploxie.engine2.pipeline.shader.ShaderModules;
import org.ploxie.engine2.scenegraph.RenderInfo;
import org.ploxie.opengl.shader.GLShaderModule;
import org.ploxie.opengl.shader.GLShaderModules;
import org.ploxie.utils.FileUtils;

public class GLToolkit extends GraphicsToolkit {

	@Override
	public RenderInfo createRenderInfo(Mesh mesh, Pipeline pipeline) {
		return new GLRenderInfo(mesh, pipeline);
	}

	@Override
	public ShaderModules loadShader(String name) {
		File folder = FileUtils.getFile("res");
		File[] listOfFiles = folder.listFiles(new FileFilter() {
			@Override
			public boolean accept(File name) {
				return name.getName().contains(name.getName());
			}
		});

		GLShaderModule vertex = null;
		GLShaderModule fragment = null;

		for (int i = 0; i < listOfFiles.length; i++) {
			File file = listOfFiles[i];
			if (file.getName().endsWith(".vert")) {
				if (vertex == null) {
					vertex = new GLShaderModule(GL20.GL_VERTEX_SHADER, "res/" + file.getName());
				}
			} else if (file.getName().endsWith(".frag")) {
				if (fragment == null) {
					fragment = new GLShaderModule(GL20.GL_FRAGMENT_SHADER, "res/" + file.getName());
				}
			}
		}
		GLShaderModules shaderModules = GLShaderModules.builder().vertexShader(vertex).fragmentShader(fragment).build().init();
		return shaderModules;

	}

}
