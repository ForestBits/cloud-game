package arcadegame1;

import org.jsfml.graphics.Color;
import org.jsfml.graphics.Image;
import org.jsfml.system.Vector2i;


public class ImageScaler 
{
    public static Image scaleImage(Image image, double xScale, double yScale)
    {
        Image n = new Image();
        
        Vector2i size = image.getSize();
        
        int sizeX = (int) (size.x*xScale);
        int sizeY = (int) (size.y*yScale);
        
        n.create(sizeX, sizeY);
        
        for (int x = 0; x < sizeX; ++x)
            for (int y = 0; y < sizeY; ++y)
            {
                int otherX = (int) ((1/xScale)*x);
                int otherY = (int) ((1/yScale)*y);
                
                n.setPixel(x, y, image.getPixel(otherX, otherY));
            }
        
        return n;
    }
}
