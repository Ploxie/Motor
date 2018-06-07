package org.ploxie.game;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import org.ploxie.utils.FileUtils;

public class ShaderCompiler {

	public static void main(String[] args) throws IOException, InterruptedException {
			File folder = FileUtils.getFile("res");
			String dir = folder.getAbsolutePath();
			File[] listOfFiles = folder.listFiles();
	
			for (int i = 0; i < listOfFiles.length; i++) {
				File file = listOfFiles[i];
				if (file.isFile()) {
					String name = file.getName();
					String ending = "";
					boolean correct = false;
					if (name.endsWith(".vert")) {					
						ending = "vert";
						correct = true;
					}else if(name.endsWith(".frag")) {
						ending = "frag";
						correct = true;
					}
					
					if(!correct) {
						continue;
					}
					
					String exec = "C:/VulkanSDK/1.1.70.1/Bin32/glslangValidator.exe -V "+dir+"\\shader."+ending+" -o "+dir+"\\"+name+".spv";			
					Process process = Runtime.getRuntime().exec(exec);
					Thread.sleep(1000);
					BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
					String line = null;
					while((line = reader.readLine()) != null) {
						System.out.println(line);
					}
					reader.close();
				}
			}
		
	}
	
}
