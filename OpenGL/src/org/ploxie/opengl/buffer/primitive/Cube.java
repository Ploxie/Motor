package org.ploxie.opengl.buffer.primitive;

import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;

import java.util.Random;

import org.ploxie.opengl.buffer.VBO;
import org.ploxie.opengl.utilities.BufferUtils;

public class Cube extends VBO{

	public Cube() {
		
		float halfSize = 1.0f * 0.5f;
		
		float[] vertices = new float[]{
                -halfSize,halfSize,-halfSize,
                -halfSize,-halfSize,-halfSize,
                halfSize,-halfSize,-halfSize,
                halfSize,halfSize,-halfSize,
 
                -halfSize,halfSize,halfSize,
                -halfSize,-halfSize,halfSize,
                halfSize,-halfSize,halfSize,
                halfSize,halfSize,halfSize,
 
                halfSize,halfSize,-halfSize,
                halfSize,-halfSize,-halfSize,
                halfSize,-halfSize,halfSize,
                halfSize,halfSize,halfSize,
 
                -halfSize,halfSize,-halfSize,
                -halfSize,-halfSize,-halfSize,
                -halfSize,-halfSize,halfSize,
                -halfSize,halfSize,halfSize,
 
                -halfSize,halfSize,halfSize,
                -halfSize,halfSize,-halfSize,
                halfSize,halfSize,-halfSize,
                halfSize,halfSize,halfSize,
 
                -halfSize,-halfSize,halfSize,
                -halfSize,-halfSize,-halfSize,
                halfSize,-halfSize,-halfSize,
                halfSize,-halfSize,halfSize
    };
		
		int[] indices = new int[] {
				0,1,3,
                3,1,2,
                4,5,7,
                7,5,6,
                8,9,11,
                11,9,10,
                12,13,15,
                15,13,14,
                16,17,19,
                19,17,18,
                20,21,23,
                23,21,22
                };
		
		Random rand = new Random();
		
		float[] colors = new float[vertices.length];
		for(int i= 0 ; i < colors.length;i++) {
			colors[i] = Math.max(rand.nextFloat(),0.5f);
		}
		
		setIndexBufferData(indices, GL_STATIC_DRAW);
		setBufferData(BufferType.VERTEX, BufferUtils.createFlippedBuffer(vertices), 3, GL_STATIC_DRAW);
		setBufferData(BufferType.COLOR, BufferUtils.createFlippedBuffer(colors), 3, GL_STATIC_DRAW);
	}
	
}
