package arcadegame1;

import org.jsfml.graphics.Image;
import org.jsfml.graphics.IntRect;


public class SpriteSheetUtil 
{
    public static IntRect getRectangle(int xPos, int yPos, int tileSizeX, int tileSizeY)
    {
        return new IntRect(xPos*tileSizeX + xPos + 1, yPos*tileSizeY + yPos + 1, tileSizeX, tileSizeY);
    }
    
    public static Image loadTileAsImage(Image spritesheet, int xPos, int yPos, int tileSizeX, int tileSizeY)
    {
        Image n = new Image();
        
        n.create(tileSizeX, tileSizeY);
        
        IntRect rect = getRectangle(xPos, yPos, tileSizeX, tileSizeY);
        
        //n.copy(spritesheet, 0, 0, rect);
        ImageCopier.copy(spritesheet, n, 0, 0, rect);
        
        return n;
    }
}
