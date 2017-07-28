package arcadegame1.enemy;

import java.util.ArrayList;
import java.util.List;
import org.jsfml.graphics.FloatRect;
import org.jsfml.system.Vector2i;


public class WorldInfo 
{
    public final List<Entity> entities = new ArrayList<>();
    public final List<Entity> entitiesToAdd = new ArrayList<>();
    
    public final float baseCloudHeight;
    public final float cloudMargin;
    public final float groundHeight;
    
    public final Vector2i windowDim;
    
    public final FloatRect windowBounds;
    
    public Player player;
    
    public float windSpeed = 1.5f;
    
    public WorldInfo(float baseCloudHeight, float cloudMargin, float groundHeight, Vector2i windowDim)
    {
        this.baseCloudHeight = baseCloudHeight;
        this.cloudMargin = cloudMargin;
        this.groundHeight = groundHeight;
        this.windowDim = windowDim;
        this.windowBounds = new FloatRect(0, 0, windowDim.x, windowDim.y);
    }
    
    public List<Enemy> getEnemies()
    {
        List<Enemy> enemies = new ArrayList<>();
        
        for (Entity entity : entities)
            if (entity instanceof Enemy)
                enemies.add((Enemy) entity);
        
        return enemies;
    }
}
