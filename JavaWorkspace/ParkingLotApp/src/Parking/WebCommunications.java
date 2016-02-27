package Parking;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.opencv.core.Mat;
import org.opencv.core.Range;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

/**
 * @author michaelh
 * @version 1.0
 * @created 19-Feb-2016 5:52:38 PM
 */
public class WebCommunications {

	private String error;

	public WebCommunications()
	{
		processImage();
	}

	public void finalize() throws Throwable {

	}
	public void getImage(){

	}

	public void processImage()
	{
		////////////////////////
		//Initialize Variables//
		////////////////////////
		Range xRange = new Range(405, 450);
		Range yRange = new Range(280, 355);
		Mat crop;
		Mat blur = null;
		
		System.loadLibrary("opencv_java2411");
		
	    //Load image from file
		Mat img = Highgui.imread("/Users/\"Ian McElhenny\"/git/SE300/JavaWorkspace/ParkingLotApp/src/main/resources");
		
		//LOOP:
			//Crop to the Nth spot: cropN = img[y:y+h, x:x+w]
			crop = img.submat(405, 450, 280, 335);
			
			//bilaterally filter the image: blurCropN = cv2.bilateralFilter(crop, 20, 75, 75)
//			Imgproc.bilateralFilter(crop, blur, 20, 75, 75);
			
			//Convert color space to HSV
			
			
			//Mask img with upper and lower limits
			
			
			//Count the white pixels and black pixels
			
			
			//Make decision about status of spot
			
			
		//GOTO top of loop

		 // Save the visualized detection.
//		    String filename = "faceDetection.png";
//		    System.out.println(String.format("Writing %s", filename));
//		    Highgui.imwrite(filename, crop);
		
//		    Image image1 = Mat2BufferedImage(img);
//		    displayImage(image1);
		

	}
	public BufferedImage Mat2BufferedImage(Mat m)
	{
		// Fastest code
		// output can be assigned either to a BufferedImage or to an Image

		int type = BufferedImage.TYPE_BYTE_GRAY;
		if ( m.channels() > 1 ) {
		    type = BufferedImage.TYPE_3BYTE_BGR;
		}
		int bufferSize = m.channels()*m.cols()*m.rows();
		byte [] b = new byte[bufferSize];
		m.get(0,0,b); // get all the pixels
		BufferedImage image = new BufferedImage(m.cols(),m.rows(), type);
		final byte[] targetPixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
		System.arraycopy(b, 0, targetPixels, 0, b.length);  
		return image;
		}
	
	public void displayImage(Image img2) 
	{
    	//BufferedImage img=ImageIO.read(new File("/HelloOpenCV/lena.png"));
    	ImageIcon icon=new ImageIcon(img2);
    	JFrame frame=new JFrame();
    	frame.setLayout(new FlowLayout());        
    	frame.setSize(img2.getWidth(null)+50, img2.getHeight(null)+50);     
    	JLabel lbl=new JLabel();
    	lbl.setIcon(icon);
    	frame.add(lbl);
    	frame.setVisible(true);
    	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	}
}//end WebCommunications