package org.ploxie.engine2.context;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Configuration {

	private int multisamples;
	private int screenResolutionX;
	private int screenResolutionY;

	private String displayTitle;
	private int windowWidth;
	private int windowHeight;

	protected Configuration() {

		Properties properties = new Properties();
		try {
			InputStream stream = Configuration.class.getClassLoader().getResourceAsStream("configurations.properties");
			properties.load(stream);
			stream.close();
		} catch (FileNotFoundException e) {
			System.err.println("Configurations not found, creating file!");
			
		} catch (IOException e) {
			e.printStackTrace();
		}

		multisamples = Integer.valueOf(properties.getProperty("multisamples"));
		windowWidth = Integer.valueOf(properties.getProperty("display.width"));
		windowHeight = Integer.valueOf(properties.getProperty("display.height"));
		displayTitle = properties.getProperty("display.title");
		screenResolutionX = Integer.valueOf(properties.getProperty("screen.resolution.x"));
		screenResolutionY = Integer.valueOf(properties.getProperty("screen.resolution.y"));

	}
	


}
