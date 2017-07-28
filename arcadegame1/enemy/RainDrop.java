package arcadegame1.enemy;

import arcadegame1.Global;
import java.util.Random;
import org.jsfml.graphics.FloatRect;
import org.jsfml.graphics.RenderTarget;
import org.jsfml.graphics.Sprite;
import org.jsfml.system.Vector2f;


public class RainDrop extends Enemy
{
    private float speed;
    
    private final WorldInfo world;
    
    private final Sprite sprite = new Sprite();
    
    private Targetable target;
    
    public RainDrop(WorldInfo world, Vector2f position, float speed)
    {
        this.world = world;
        this.speed = speed;
        
        sprite.setTexture(Global.textures.get("raindrop"));
        
        sprite.setOrigin(sprite.getLocalBounds().width/2, sprite.getLocalBounds().height/2);
        
        sprite.setPosition(position);
    }
    
    @Override
    public void update()
    {
        sprite.move(0, speed);
        
        if (sprite.getPosition().y >= world.windowDim.y - world.groundHeight)
            remove();
        
        if (getBounds().intersection(world.player.getBounds()) != null)
        {
            world.player.damage(1);
            
            remove();
        }
        
        for (Entity entity : world.entities)
            if (entity instanceof Rocket)
            {
                if (entity.getBounds().intersection(getBounds()) != null)
                {
                    ((Rocket) entity).hurt();
                    
                    remove();
                }
            }
    }
    
    @Override
    public void draw(RenderTarget target)
    {
        target.draw(sprite);
    }
    
    @Override
    public void setTarget(Targetable target)
    {
        this.target = target;
    }
    
    @Override
    public FloatRect getBounds()
    {
        return sprite.getGlobalBounds();
    }
}
