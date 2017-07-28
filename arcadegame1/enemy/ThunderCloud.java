package arcadegame1.enemy;

import arcadegame1.Animation;
import arcadegame1.Global;
import arcadegame1.MyMath;
import java.util.Random;
import org.jsfml.graphics.Color;
import org.jsfml.graphics.FloatRect;
import org.jsfml.graphics.RenderTarget;
import org.jsfml.graphics.Sprite;
import org.jsfml.system.Vector2f;


public class ThunderCloud extends Enemy
{
    private final Sprite sprite = new Sprite();
    private final Sprite thunderSprite = new Sprite();
    
    private final double cooldown = 5;
    private final double agroDistance = 200;
    
    private final float maxBoltMiss = 100;
    
    private long lastBolt = 0;
    
    private final Random random = new Random();
    
    private Targetable target;
    
    private final WorldInfo world;
    
    private final Animation deathAnimation = Global.animations.get("thundercloud").getAnimation(false);
    
    private boolean dying = false;
    private boolean fading = false;
    
    private long fadeStartTime;
    
    private final double fadeLength = 2;
    private final double boltHurtDistance = 20;
    
    private boolean striking = false;
    
    private Vector2f strikeTarget = null;
    
    private final int boltDisplayFrames = 10;
    
    private int framesDisplayed = 0;
    
    public ThunderCloud(WorldInfo world, Vector2f position)
    {
        this.world = world;
        
        sprite.setTexture(Global.textures.get("thundercloud"));
        
        sprite.setOrigin(sprite.getLocalBounds().width/2, sprite.getLocalBounds().height - 1);
        
        sprite.setPosition(position);
        
        thunderSprite.setTexture(Global.textures.get("thunderbolt"));
        
        thunderSprite.setOrigin(thunderSprite.getLocalBounds().width/2, 0);
    }
    
    private void strikeThunder(Vector2f target)
    {
        striking = true;
        
        strikeTarget = target;
    }
    
    private double elapsed()
    {
        return (System.currentTimeMillis() - lastBolt)/1000d;
    }
    
    @Override
    public void update()
    {
        if (dying)
            return;
        
        sprite.move(world.windSpeed, 0);
        
        if (sprite.getPosition().x - sprite.getGlobalBounds().width/2 > world.windowDim.x + 50)
            sprite.setPosition(-100, world.baseCloudHeight + random.nextFloat()*world.cloudMargin);
        
        if (striking && MyMath.distance(strikeTarget, world.player.getPosition()) < boltHurtDistance)
        {
            world.player.damage(1);
        }
        
        if (elapsed() > cooldown && Math.abs(world.player.getPosition().x - sprite.getPosition().x) < agroDistance)
        {
            lastBolt = System.currentTimeMillis();
            
            Vector2f pos = world.player.getPosition();
            
            float xDif = (random.nextBoolean() ? 1 : -1)*(random.nextFloat()*maxBoltMiss);
            
            strikeThunder(new Vector2f(pos.x + xDif, pos.y));
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
        
        if (striking)
        {
            ++framesDisplayed;
            
            if (framesDisplayed >= boltDisplayFrames)
            {
                striking = false;
                
                framesDisplayed = 0;
            }

            thunderSprite.setPosition(sprite.getPosition().x, sprite.getPosition().y - sprite.getGlobalBounds().height/2);
            
            double distance = Math.sqrt(Math.pow(sprite.getPosition().x - strikeTarget.x, 2) + Math.pow(sprite.getPosition().y - strikeTarget.y, 2)) + 50;
            
            thunderSprite.scale(1, (float) (distance/thunderSprite.getGlobalBounds().height));
            
            double angle = Math.atan2(sprite.getPosition().y - strikeTarget.y, sprite.getPosition().x - strikeTarget.x);
            
            thunderSprite.setRotation((float) ((Math.toDegrees(angle)) + 90));
            
            target.draw(thunderSprite);
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
