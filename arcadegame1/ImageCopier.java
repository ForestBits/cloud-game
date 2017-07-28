package arcadegame1;

//JSFML has a bug in image copying for this particular version

import org.jsfml.graphics.Color;
import org.jsfml.graphics.Image;
import org.jsfml.graphics.IntRect;

public class ImageCopier 
{
    public static void copy(Image source, Image dest, int xPos, int yPos, IntRect sourceRect)
    {
        for (int x = 0; x < sourceRect.width; ++x)
            for (int y = 0; y < sourceRect.height; ++y)
            {
                Color pixel = source.getPixel(x + sourceRect.left, y + sourceRect.top);
                
                dest.setPixel(xPos + x, yPos + y, pixel);
            }
        
        int i = 6;
        int bob = 2;
    }
}
