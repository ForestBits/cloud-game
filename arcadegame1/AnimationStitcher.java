package arcadegame1;

import org.jsfml.graphics.Color;
import org.jsfml.graphics.Image;
import org.jsfml.system.Vector2i;


public class AnimationStitcher 
{
    public static Image stitch(Image... images)
    {
        int totalWidth = 1;
        int tallest = 0;
        
        for (Image image : images)
        {
            Vector2i size = image.getSize();
            
            if (size.y > tallest)
                tallest = size.y;
            
            totalWidth += size.x + 1;
        }
        
        Image n = new Image();
        
        n.create(totalWidth, tallest + 2, new Color(255, 102, 204));
        
        int xPos = 1;
        
        for (int i = 0; i < images.length; ++i)
        {
            n.copy(images[i], xPos, 1);
            
            xPos += images[i].getSize().x + 1;
        }
        
        return n;
    }
}
