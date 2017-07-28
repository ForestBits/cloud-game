package arcadegame1.enemy;

import arcadegame1.Animation;
import arcadegame1.AnimationData;
import arcadegame1.Global;
import java.util.ArrayList;
import java.util.List;
import org.jsfml.graphics.Color;
import org.jsfml.graphics.FloatRect;
import org.jsfml.graphics.IntRect;
import org.jsfml.graphics.RenderTarget;
import org.jsfml.graphics.Sprite;
import org.jsfml.system.Vector2f;


public class Rocket extends Entity
{
    private final Sprite sprite = new Sprite();
    private final Sprite exhaustSprite = new Sprite();
    
    private final double angle;
    private double speed;
    private final double acceleration;
    
    private final WorldInfo world;
    
    private static final Color[] exhaustColors = 
    {
        Color.RED,
        new Color(255, 51, 0),
        new Color(153, 0, 0),
        new Color(102, 0, 51),
        new Color(0, 0, 0),
        new Color(40, 40, 40),
        new Color(80, 80, 80),
        new Color(120, 120, 120),
        new Color(150, 150, 150)
    };
    
    private final List<Vector2f> particles = new ArrayList<>();
    
    private int exhaustSkipCount = 0;
    
    private final AnimationData explosionAnimationData = Global.animations.get("rocketExplosion");
    private final Animation explosionAnimation = explosionAnimationData.getAnimation(false);
    
    private boolean dying = false;
    
    private int health = 9;

    public Rocket(WorldInfo world, Vector2f position, double angle, double speed, double acceleration)
    {
        this.world = world;
        this.angle = angle;
        this.speed = speed;
        this.acceleration = acceleration;
        
        sprite.setTexture(Global.textures.get("rocket"));
        
        sprite.setOrigin(sprite.getGlobalBounds().width/2, sprite.getGlobalBounds().height/2);
        
        sprite.setPosition(position);
        
        sprite.setRotation((float) ((Math.toDegrees(angle)) + 90));
        
        exhaustSprite.setTexture(Global.textures.get("rocketExhaust"));
        
        exhaustSprite.setOrigin(exhaustSprite.getGlobalBounds().width/2, exhaustSprite.getGlobalBounds().height/2);
    }
    
    private void move()
    {
        if (exhaustSkipCount < 1)
        {
            ++exhaustSkipCount;
        }
        else
        {
            exhaustSkipCount = 0;
            
            particles.add(sprite.getPosition());
        }
        
        if (particles.size() > exhaustColors.length)
            particles.remove(0);
        
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
        
        if (sprite.getGlobalBounds().intersection(world.windowBounds) == null)
            remove();
    }
    
    @Override
    public void draw(RenderTarget target)
    {
        if (!dying)
            for (int i = particles.size() - 1; i >= 0; --i)
            {
                Vector2f particle = particles.get(i);

                exhaustSprite.setPosition(particle.x, particle.y);

                exhaustSprite.setColor(exhaustColors[particles.size() - (i + 1)]);

                target.draw(exhaustSprite);
            }
        
        if (dying && explosionAnimation.isPlaying())
            sprite.setTextureRect(explosionAnimation.currentRect());
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
        if (!dying)
            return sprite.getGlobalBounds();
        else
            return new FloatRect(-100, -100, 0, 0);
    }
    
    public void hit()
    {
        speed = 20;
        
        move();
        
        dying = true;
        
        sprite.setTexture(explosionAnimation.getTexture());
        
        IntRect firstFrame = explosionAnimationData.frames[0].rect;
        
        sprite.setOrigin(firstFrame.width/2, firstFrame.height/2);
        
        explosionAnimation.start();
    }
    
    public void hurt()
    {
        --health;
        
        if (health == 0)
            hit();
    }
}
