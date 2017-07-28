package arcadegame1;

import org.jsfml.system.Vector2f;
import static org.jsfml.window.Keyboard.Key.M;


public class MyMath 
{
    public static double distance(Vector2f pos1, Vector2f pos2)
    {
        return Math.sqrt(Math.pow(pos1.x - pos2.x, 2) + Math.pow(pos1.y - pos2.y, 2));
    }
    
    public static double angle(Vector2f from, Vector2f to)
    {
        return Math.atan2(to.y - from.y, to.x - from.x);
    }
}
