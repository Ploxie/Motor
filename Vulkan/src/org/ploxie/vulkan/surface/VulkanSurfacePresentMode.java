package org.ploxie.vulkan.surface;

public enum VulkanSurfacePresentMode {

	/**
	 * This is for applications that don't care about tearing, or have some way
	 * of synchronizing with the display (which Vulkan doesn't yet provide).
	 * Images submitted by your application are transferred to the screen right
	 * away, which may result in tearing.
	 */
	IMMEDIATE, 

	/**
	 * I'm guessing that this is for applications that generally render/present
	 * a new frame every refresh cycle, but are occasionally early. In this
	 * case, they want the new image to be displayed instead of the
	 * previously-queued-for-presentation image that has not yet been displayed.
	 * This is another variation of the second mode. Instead of blocking the
	 * application when the queue is full, the images that are already queued
	 * are simply replaced with the newer ones. This mode can be used to
	 * implement triple buffering, which allows you to avoid tearing with
	 * significantly less latency issues than standard vertical sync that uses
	 * double buffering.
	 */
	MAILBOX,
	
	/**
	 * This is for applications that don't want tearing ever. It's difficult to
	 * say how fast they may be, whether they care about stuttering/latency. The
	 * swap chain is a queue where the display takes an image from the front of
	 * the queue when the display is refreshed and the program inserts rendered
	 * images at the back of the queue. If the queue is full then the program
	 * has to wait. This is most similar to vertical sync as found in modern
	 * games. The moment that the display is refreshed is known as "vertical
	 * blank".
	 */
	FIFO, 

	/**
	 * This is for applications that generally render/present a new frame every
	 * refresh cycle, but are occasionally late. In this case (perhaps because
	 * of stuttering/latency concerns), they want the late image to be
	 * immediately displayed, even though that may mean some tearing. This mode
	 * only differs from VK_PRESENT_MODE_FIFO_KHR if the application is late and the
	 * queue was empty at the last vertical blank. Instead of waiting for the
	 * next vertical blank, the image is transferred right away when it finally
	 * arrives. This may result in visible tearing.
	 */
	FIFO_RELAXED,;

	public static VulkanSurfacePresentMode get(int i) {
		return values()[i];
	}

	public int getID() {
		return ordinal();
	}
	
}
