package Parking;

import org.bytedeco.javacv.CanvasFrame;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.opencv.core.Core;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class PullImage {

	public static void main(String[] args) throws Exception {
		/*Use this if your library path is giving you trouble, i.e.:
		 * Exception in thread "main" java.lang.UnsatisfiedLinkError: no opencv_java2411 in java.library.path
		 * 
		 * Use directory of .dll file on machine...
		 */
		System.load("C:/Users/tchristovich/Documents/opencv/build/java/x64/opencv_java2411.dll");
		
		/* Use this if you know that your library path is configured correctly */
		//System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		
		
		FrameGrabber grabber = new FFmpegFrameGrabber("http://construction1.db.erau.edu/mjpg/video.mjpg"); 
	    grabber.setFormat("mjpeg");
	    System.out.println("Making connection...");
		
	    grabber.start();
	    
	    Frame frame1;
		frame1 = grabber.grab();
		CanvasFrame canvasFrame = new CanvasFrame("Camera");
	    canvasFrame.setCanvasSize(frame1.imageWidth, (frame1.imageHeight));
	    while (canvasFrame.isVisible() && (frame1 = grabber.grab()) != null) {
	        canvasFrame.showImage(frame1);
	        saveFrame(frame1);
	    }
	    
	    grabber.stop();
	    canvasFrame.dispose();
	    
	    System.exit(0);
	}
	
	public static void saveFrame(Frame frame){
		Java2DFrameConverter javaconverter = new Java2DFrameConverter(); 
		BufferedImage image = javaconverter.convert(frame);
		try {
			ImageIO.write(image, "jpg", new File("currentImg.jpg"));
		} catch (IOException e) {
			System.out.println("Failed.");
		}
	}
	
}


