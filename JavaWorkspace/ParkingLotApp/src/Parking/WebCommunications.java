package Parking;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Range;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

/**
 * @author Ian McElhenny, Tim Christovitch
 * @version 1.0
 * @created 19-Feb-2016 5:52:38 PM
 */
public class WebCommunications implements MouseListener
{

	private String error;
	Mat img ;
	Mat crop;
	Mat blur = null;
    Mat hsv = null;
    Mat mask = null;
    Point start;
    Point end;
    
    
	public WebCommunications()
	{
		processImage();
		System.loadLibrary("opencv_java2411");

	}

	public void finalize() throws Throwable 
	{

	}
	
	public void getImage()
	{

	}

	//****NEEDS TO ADD IMG AS ARGUMENT IN FUNCTION CALL****
	//Takes a img of the parking lot, subdivides it into spots, process each spot in a forloop then 
	public void processImage()
	{
		System.loadLibrary("opencv_java2411");

		//Load image from file
		Mat img = Highgui.imread("src/main/resources/bottomOpen.JPG");
		
		////////////////////////
		//Initialize Variables//
		////////////////////////
		Mat crop;
		Mat blur = null;
		Mat hsv = null;
		Mat mask = null;
		int black = 0;
		int white = 0;
		double ratio = 0;
		ParkingLotGrid parkingLot = new ParkingLotGrid();
		//This should be dynamic but is hard coded for now.
		parkingLot.setGridSize(28);//I, ian, counted 28 spots that i think we can process effectively in the area
		ParkingSpots[] spotArray = parkingLot.getSpotArray();

		
		//loop through array or parking lot and process each spot
		for(int i = 0; i <= parkingLot.getSpotArray().length-1; i++)
		{
			//Crop to the Nth spot
			crop = img.submat(spotArray[i].getYRange(), spotArray[i].getXRange());
			
			//Create a Blur, hsv, and mask matrix same size and type as crop for bilateral filter return, hsv return and mask return
			Size size = new Size(crop.width(), crop.height());
			blur = Mat.zeros(size , 0);
			hsv = Mat.zeros(size , 0);
			mask = Mat.zeros(size , 0);

			//bilaterally filter the image
			Imgproc.bilateralFilter(crop, blur, 20, 75, 75);
			
			//Convert color space to HSV
			Imgproc.cvtColor(blur, hsv, Imgproc.COLOR_RGB2HSV);
			
			//Mask img with upper and lower limits
			Core.inRange(hsv, spotArray[i].getLowerHsv(), spotArray[i].getUpperHsv(), mask);

			//Count the white pixels and black pixels
			for(int x = 0; x <= mask.size().width - 1; x++)
			{
				for(int y = 0; y <= mask.size().height - 1; y++)
				{
					if(mask.get(y, x)[0] == 0.0)
					{
						black++;
					}
					else if(mask.get(y, x)[0] == 255.0)
					{
						white++;
					}
				}
			}
			
			System.out.println(black + ", " + white);
			
			//Make decision about status of spot
			ratio = (double)white/(white+black);
			if(ratio > 0.5)
			{
				System.out.println("Spot: " + (i+1) + " is open");
				parkingLot.setStatus(i, true);//spot empty
			}
			else if(ratio < 0.5)
			{
				System.out.println("Spot: " + (i+1) + " is taken");
				parkingLot.setStatus(i, false);//spot occupied
			}
			
			Image image1 = Mat2BufferedImage(crop);
		    displayImage(image1);
		}
		
	}
	
	public void procImage()
	{
		////////////////////////
		//Initialize Variables//
		////////////////////////
		Range xRange = new Range(405, 450);
		Range yRange = new Range(280, 335);
//		Mat crop;
//		Mat blur = null;
//	    Mat hsv = null;
//	    Mat mask = null;
	    Scalar lower = new Scalar(0,0,0);
	    //to make the upper liit better look at three always open spots and average the value of the spots to keep a good upper limit
	    Scalar upper = new Scalar(120,45,170); //Based on spot 6 open in bottom open jpg
	    int black = 0;
	    int white = 0;
	    double ratio = 0;
	    
		System.loadLibrary("opencv_java2411");
	    img = Highgui.imread("src/main/resources/bottomOpen.JPG");

	    //Load image from file
//		Mat img = Highgui.imread("src/main/resources/bottomOpen.JPG");
		
		//LOOP:
			//Crop to the Nth spot: cropN = img[y:y+h, x:x+w]
			crop = img.submat(yRange, xRange);
			
			//Create a Blur and hsv matrix same size and type ase crop for bilatereral filter return
			Size size = new Size(img.width(), img.height());
			blur = Mat.zeros(size , 0);
			hsv = Mat.zeros(size , 0);
			
			//bilaterally filter the image: blurCropN = cv2.bilateralFilter(crop, 20, 75, 75)
			Imgproc.bilateralFilter(img, blur, 20, 75, 75);
			
			//Convert color space to HSV
			Imgproc.cvtColor(blur, hsv, Imgproc.COLOR_RGB2HSV);
			
			//Mask img with upper and lower limits
			mask = Mat.zeros(size , 0);
			Core.inRange(hsv, lower, upper, mask);

			//Count the white pixels and black pixels
			for(int x = 0; x <= mask.size().width - 1; x++)
			{
				for(int y = 0; y <= mask.size().height - 1; y++)
				{
					if(mask.get(y, x)[0] == 0.0)
					{
						black++;
					}
					else if(mask.get(y, x)[0] == 255.0)
					{
						white++;
					}
				}
			}
			
			System.out.println(black + ", " + white);
			
			//Make decision about status of spot
			ratio = (double)white/(white+black);
			if(ratio > 0.5)
			{
				System.out.println("Open");
			}
			else if(ratio < 0.5)
			{
				System.out.println("Taken");
			}
			
		//GOTO top of loop

			
			
			
			///////////////////////
			//Display Image(Temp)//
			///////////////////////		
			
		 // Save the visualized detection.
//		    String filename = "faceDetection.png";
//		    System.out.println(String.format("Writing %s", filename));
//		    Highgui.imwrite(filename, crop);
		
		    Image image1 = Mat2BufferedImage(img);
		    displayImage(image1);
		    
		    /////////
		    //Notes//
		    /////////
//			byte buff[] = new byte[(int) (mask.total() * mask.channels())];
//			mask.get(0, 0, buff);
//			System.out.print(hsv.get(40, 15)[2]); //[110, 37, 104]

	}
	
	
	
	/////////////
	//Temporary//
	/////////////
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
    	lbl.addMouseListener(this);
    	frame.add(lbl);
    	frame.setVisible(true);
    	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	
	
	///////////////////
	//Mouse listeners//
	///////////////////
	@Override
	public void mouseClicked(MouseEvent e) 
	{
	       System.out.println("HSV 1: " + hsv.get(e.getX(), e.getY())[0]);
	       System.out.println("HSV 2: " + hsv.get(e.getX(), e.getY())[1]);
	       System.out.println("HSV 3: " + hsv.get(e.getX(), e.getY())[2]);

//	       System.out.println(e.getPoint());
    }

	@Override
	public void mouseEntered(MouseEvent arg0) 
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) 
	{
		// TODO Auto-generated method stub
		
	}

	
	public void mousePressed(MouseEvent e) 
	{
//		System.out.println("Entered ");
//   System.out.println(e.getPoint());
		
		start = e.getPoint();
		
	}

	
	public void mouseReleased(MouseEvent e) 
	{
//		System.out.println("Exited");
//   System.out.println(e.getPoint());
		end = e.getPoint();
		
		getHsvMax(start, end, hsv);
		getHsvAvg(start, end, hsv);

	}
	
	
	//For a given matrix of pixels and an area from start (x1, y1 pixel) to end (x2, y2 pixel) it tells the largest and smallest value of the pixels.
	public void getHsvMax(Point start, Point end, Mat hsv)
	{
		int x = (int)(end.getX()-start.getX());
		int y = (int)(end.getY()-start.getY());
		int oneh = 0;
		int twoh = 0;
		int threeh = 0;
		int onel = 255;
		int twol = 255;
		int threel = 255;
		
		for(int i = 0; i <= x; i++)
		{
			for(int j = 0; j <= y; j++)
			{
				//high
				if( (int)hsv.get(i, j)[0] > oneh)
				{
					oneh = (int)hsv.get(i, j)[0];

				}
				if((int)hsv.get(i, j)[1] > twoh)
				{
					twoh = (int)hsv.get(i, j)[1];

				}
				if((int)hsv.get(i, j)[2] > threeh)
				{
					threeh = (int)hsv.get(i, j)[2];
				}
				//low
				if( (int)hsv.get(i, j)[0] < onel)
				{
					onel = (int)hsv.get(i, j)[0];

				}
				if((int)hsv.get(i, j)[1] < twol)
				{
					twol = (int)hsv.get(i, j)[1];

				}
				if((int)hsv.get(i, j)[2] < threel)
				{
					threel = (int)hsv.get(i, j)[2];
				}
			}
			
		}
		System.out.println("MAX");
		System.out.println("HSV 1: " + oneh);
		System.out.println("HSV 2: " + twoh);
		System.out.println("HSV 3: " + threeh);
		
		System.out.println("Min");
		System.out.println("HSV 1: " + onel);
		System.out.println("HSV 2: " + twol);
		System.out.println("HSV 3: " + threel);
		
	}
	
	//For a given matrix of pixels and an area from start (x1, y1 pixel) to end (x2, y2 pixel) it tells the avg value of the pixels.
	public void getHsvAvg(Point start, Point end, Mat hsv)
	{
		int x = (int)(end.getX()-start.getX());
		int y = (int)(end.getY()-start.getY());
		int one = 0;
		int two = 0;
		int three = 0;
		
		for(int i = 0; i <= x; i++)
		{
			for(int j = 0; j <= y; j++)
			{
				one = (one + (int)hsv.get(i, j)[0])/2;
				two = (two + (int)hsv.get(i, j)[1])/2;
				three = (three + (int)hsv.get(i, j)[2])/2;
			}
			
		}
		System.out.println("Average");
		System.out.println("HSV 1: " + one);
		System.out.println("HSV 2: " + two);
		System.out.println("HSV 3: " + three + "\n\n");
		
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}//end WebCommunications