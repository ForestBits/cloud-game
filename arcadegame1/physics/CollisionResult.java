package arcadegame1.physics;

import org.jsfml.graphics.FloatRect;


public class CollisionResult
{
    public final FloatRect rect;
    public final CollisionType type;
    
    public CollisionResult(FloatRect rect, CollisionType type)
    {
        this.rect = rect;
        this.type = type;
    }
}