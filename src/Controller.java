import java.awt.*;
import java.io.File;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;


public class Controller
{
	private Image baseImage;
	private ImageRegions compressedImage;
 	private Compressor m_compressor;
	public Controller()
	{
	}

	/**
	 * Takes a File from the Menu that the user selected, reads it in with ImageIO.read() into a BufferedImage,
	 * and sets the Controller's baseImage to that image.
	 */
	public boolean setImageFile(File f)
	{
		return false;
	}

	/**
	 * Returns the base image that the controller is working with.
	 */
	public Image getBaseImage()
	{
		BufferedImage reading_Img;
            try{
                reading_Img = ImageIO.read(f);
                baseImage = reading_Img;
                return true;
            }catch(Exception e){
                baseImage = null;
                return false;
            }	  
	}
	public Image getBaseImage()
	{
            if (baseImage!= null)
            {
                return baseImage;
            }else
            {
                return null;
            }
	}

        public void getCompressImage()
        {
            compressedImage = m_compressor.ir;
        }
	/**
	 * Calls the Compressor on the base image, given the options from the Menu.
	 * Need to think about what failure conditions might be.
	 */
	public boolean compressImage(int percent, boolean animate)
	{
	 try{
                compressedImage = new ImageRegions((BufferedImage)baseImage);
                m_compressor = new Compressor(compressedImage);
                m_compressor.segment(percent);
                return true;
            }catch(Exception e){}
		return false;

	}

	/**
	 * Saves the compressed image as a PNG to the disk at the location specified by the user.
	 */
	public boolean saveImageAsPNG(File f)
	{
	 try{
                ImageIO.write(compressedImage.image, "png", f);
                return true;
            }catch(Exception e)
            {  }
            return false;
	}

	/**
	 * Saves the compressed image as a CIF to the disk at the location specified by the user.
	 */
	public boolean saveImageAsCIF(File f)
	{
	 try{
                ImageIO.write(compressedImage.image, "cif", f);
                return true;
            }catch(Exception e)
            {  }
            return false;
	}
}
