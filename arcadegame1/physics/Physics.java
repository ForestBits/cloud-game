package arcadegame1.physics;

import java.util.List;
import org.jsfml.graphics.FloatRect;

public class Physics 
{
    private static FloatRect getMovedRect(FloatRect rect, float xMove, float yMove)
    {
        return new FloatRect(rect.left + xMove, rect.top + yMove, rect.width, rect.height);
    }
    
    public static CollisionResult moveX(FloatRect rect, List<FloatRect> rects, double xSpeed)
    {        
        for (FloatRect r : rects)
            if (r.intersection(rect) != null)
                return new CollisionResult(rect, CollisionType.IN_SOLID);
        
        int sign = (int) Math.signum(xSpeed);
        
        if (sign == 0)
            return new CollisionResult(rect, CollisionType.NONE);
        
        int abs = (int) Math.abs(xSpeed);
        
        for (int i = 1; i <= abs; ++i)
        {
            boolean intersects = false;
            
            FloatRect moved = getMovedRect(rect, i*sign, 0);
            
            for (FloatRect r : rects)
                if (r.intersection(moved) != null)
                {
                    intersects = true;
                    
                    break;
                }
            
            if (intersects)
            {
                if (sign > 0)
                    return new CollisionResult(getMovedRect(rect, i - 1, 0), CollisionType.RIGHT);
                else
                    return new CollisionResult(getMovedRect(rect, -i + 1, 0), CollisionType.LEFT);
            }
        }
        
        FloatRect moved = getMovedRect(rect, (float) xSpeed, 0);
        
        boolean collision = false;
        
        for (FloatRect r : rects)
            if (r.intersection(moved) != null)
            {
                collision = true;
                
                break;
            }
        
        if (collision)
        {
            return new CollisionResult(getMovedRect(rect, (int) xSpeed, 0), CollisionType.NONE);
        }
        else
            return new CollisionResult(getMovedRect(rect, (float) xSpeed, 0), CollisionType.NONE);
    }
    
    public static CollisionResult moveY(FloatRect rect, List<FloatRect> rects, double ySpeed)
    {
        for (FloatRect r : rects)
            if (r.intersection(rect) != null)
                return new CollisionResult(rect, CollisionType.IN_SOLID);
        
        int sign = (int) Math.signum(ySpeed);
        
        if (sign == 0)
            return new CollisionResult(rect, CollisionType.NONE);
        
        int abs = (int) Math.abs(ySpeed);

        for (int i = 1; i <= abs; ++i)
        {
            boolean intersects = false;
            
            FloatRect moved = getMovedRect(rect, 0, i*sign);
            
            for (FloatRect r : rects)
                if (r.intersection(moved) != null)
                {
                    intersects = true;
                    
                    break;
                }
            
            if (intersects)
            {
                if (sign > 0)
                    return new CollisionResult(getMovedRect(rect, 0, i - 1), CollisionType.TOP);
                else
                    return new CollisionResult(getMovedRect(rect, 0, -i + 1), CollisionType.BOTTOM);
            }
        }
        
        FloatRect moved = getMovedRect(rect, 0, (float) ySpeed);
        
        boolean collision = false;
        
        for (FloatRect r : rects)
            if (r.intersection(moved) != null)
            {
                collision = true;
                
                break;
            }
        
        if (collision)
        {
            return new CollisionResult(getMovedRect(rect, 0, (int) ySpeed), CollisionType.NONE);
        }
        else
            return new CollisionResult(getMovedRect(rect, 0, (float) ySpeed), CollisionType.NONE);
    }
    
    public static CollisionResult[] move(FloatRect rect, List<FloatRect> rects, double xSpeed, double ySpeed)
    {
        CollisionResult[] results = new CollisionResult[2];
        
        results[0] = moveX(rect, rects, xSpeed);
        results[1] = moveY(results[0].rect, rects, ySpeed);
        
        return results;
    }
}
