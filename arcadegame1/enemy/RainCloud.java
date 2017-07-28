package arcadegame1.enemy;

import arcadegame1.Animation;
import arcadegame1.Global;
import java.util.Random;
import org.jsfml.graphics.Color;
import org.jsfml.graphics.FloatRect;
import org.jsfml.graphics.RenderTarget;
import org.jsfml.graphics.Sprite;
import org.jsfml.system.Vector2f;


public class RainCloud extends Enemy
{
    private final Sprite sprite = new Sprite();
    
    private final double cooldown = 0.3;
    private final double agroDistance = 300;
    
    private long lastDrop = 0;
    
    private final Random random = new Random();
    
    private Targetable target;
    
    private final WorldInfo world;
    
    private final Animation deathAnimation = Global.animations.get("raincloud").getAnimation(false);
    
    private boolean dying = false;
    private boolean fading = false;
    
    private long fadeStartTime;
    
    private final double fadeLength = 2;
    
    public RainCloud(WorldInfo world, Vector2f position)
    {
        this.world = world;
        
        sprite.setTexture(Global.textures.get("raincloud"));
        
        sprite.setOrigin(sprite.getLocalBounds().width/2, sprite.getLocalBounds().height - 1);
        
        sprite.setPosition(position);
    }
    
    private double elapsed()
    {
        return (System.currentTimeMillis() - lastDrop)/1000d;
    }
    
    @Override
    public void update()
    {
        if (dying)
            return;
        
        sprite.move(world.windSpeed, 0);
        
        if (sprite.getPosition().x - sprite.getGlobalBounds().width/2 > world.windowDim.x + 50)
            sprite.setPosition(-100, world.baseCloudHeight + random.nextFloat()*world.cloudMargin);
        
        if (elapsed() > cooldown && Math.abs(world.player.getPosition().x - sprite.getPosition().x) < agroDistance)
        {
            lastDrop = System.currentTimeMillis();
            
            float xPos = sprite.getPosition().x + (random.nextBoolean() ? 1 : -1)*random.nextFloat()*(sprite.getLocalBounds().width/2);
            
            world.entitiesToAdd.add(new RainDrop(world, new Vector2f(xPos, sprite.getPosition().y + 10), 4f));
        }
        
        for (Entity entity : world.entities)
            if (entity instanceof Rocket && entity.getBounds().intersection(getBounds()) != null)
            {
                Rocket rocket = (Rocket) entity;
                
                rocket.hit();
                
                dying = true;
                
                sprite.setTexture(deathAnimation.getTexture());
                
                deathAnimation.start();
                
                break;
            }
    }
    
    @Override
    public void draw(RenderTarget target)
    {
        if (dying && deathAnimation.isPlaying())
        {
            sprite.setTextureRect(deathAnimation.currentRect());
        }
        
        if (dying && !deathAnimation.isPlaying() && !fading)
        {
            fading = true;
            
            fadeStartTime = System.currentTimeMillis();
        }
        
        if (fading)
        {
            double elapsed = (System.currentTimeMillis() - fadeStartTime)/1000d;
            
            if (elapsed > fadeLength)
            {
                remove();
                
                return;
            }
            else
            {
                double percent = elapsed/fadeLength;
                
                int alpha = (int) ((1 - percent)*255);
                
                sprite.setColor(new Color(Color.WHITE, alpha));
            }
        }
        
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
