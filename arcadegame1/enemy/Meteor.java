package arcadegame1.enemy;

import arcadegame1.Animation;
import arcadegame1.AnimationData;
import arcadegame1.Global;
import java.util.ArrayList;
import java.util.List;
import org.jsfml.graphics.FloatRect;
import org.jsfml.graphics.IntRect;
import org.jsfml.graphics.RenderTarget;
import org.jsfml.graphics.Sprite;
import org.jsfml.system.Vector2f;


public class Meteor extends Enemy
{
    private final Sprite sprite = new Sprite();
    
    private final double angle;
    private double speed;
    private final double acceleration;
    
    private final WorldInfo world;
    
    private final List<Vector2f> particles = new ArrayList<>();
    
    private final AnimationData explosionAnimationData = Global.animations.get("rocketExplosion");
    private final Animation explosionAnimation = explosionAnimationData.getAnimation(false);
    
    private Targetable target;
    
    private final float rotation = 2;
    
    private boolean dying = false;
    
    private final float radius;
    
    private final long creation = System.currentTimeMillis();

    public Meteor(WorldInfo world, Vector2f position, float radius, double angle, double speed, double acceleration)
    {
        this.world = world;
        this.angle = angle;
        this.speed = speed;
        this.acceleration = acceleration;
        this.radius = radius;
        
        sprite.setTexture(Global.textures.get("meteor"));
        
        sprite.setOrigin(sprite.getGlobalBounds().width/2, sprite.getGlobalBounds().height/2);
        
        sprite.setPosition(position);
        
        float currentRadius = sprite.getGlobalBounds().width/2;
        
        float factor = radius/currentRadius;
        
        sprite.setScale(factor, factor);
    }
    
    private void move()
    {        
        double xMove = Math.cos(angle)*speed;
        double yMove = Math.sin(angle)*speed;
        
        sprite.move((float) xMove, (float) yMove);
    }
    
    @Override
    public void update()
    {
        if (dying)
            return;
        
        move();
        
        speed += acceleration;
        
        FloatRect bounds = getBounds();
        
        if (bounds.intersection(world.player.getBounds()) != null)
        {
            world.player.damage(1);
        }
        
        if (bounds.top + bounds.height >= world.windowDim.y - world.groundHeight)
            hit();
        
        if ((System.currentTimeMillis() - creation)/1000d > 4 && bounds.intersection(world.windowBounds) == null)
            remove();
    }
    
    @Override
    public void draw(RenderTarget target)
    {        
        if (!dying)
        {
            sprite.rotate(rotation);
        }
        else if (dying && explosionAnimation.isPlaying())
        {
            sprite.setTextureRect(explosionAnimation.currentRect());
        }
        else if (dying && !explosionAnimation.isPlaying())
        {
            remove();
            
            return;
        }
        
        target.draw(sprite);
    }
    
    @Override
    public FloatRect getBounds()
    {
        return sprite.getGlobalBounds();
    }
    
    public void hit()
    {
        sprite.move(0, radius);
        
        sprite.setTexture(explosionAnimation.getTexture());
        
        IntRect firstFrame = explosionAnimationData.frames[0].rect;
        
        sprite.setOrigin(firstFrame.width/2, firstFrame.height/2);
        
        sprite.setRotation(0);
        
        explosionAnimation.start();
        
        dying = true;
    }
    
    public void setTarget(Targetable target)
    {
        this.target = target;
    }
}
