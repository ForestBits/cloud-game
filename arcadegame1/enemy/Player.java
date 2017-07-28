package arcadegame1.enemy;

import arcadegame1.Global;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.jsfml.graphics.Color;
import org.jsfml.graphics.FloatRect;
import org.jsfml.graphics.RenderTarget;
import org.jsfml.graphics.Sprite;
import org.jsfml.system.Vector2f;
import org.jsfml.system.Vector2i;
import org.jsfml.window.Keyboard;
import org.jsfml.window.Mouse;
import org.jsfml.window.event.Event;
import org.jsfml.window.event.MouseButtonEvent;

public class Player extends Entity implements Targetable
{
    private final Sprite sprite = new Sprite();
    
    private final float moveAmount = 6;
    
    private final WorldInfo world;
    
    private final List<Event> events;
    
    private boolean blinking = false;
    
    private long invincibilityStartTime;

    private final double invincibilityTime = 3;
    
    private long lastBlinkSwitch;
    
    private final double blinkPeriod = 0.5;
    
    private int lives = 10;
    
    private final List<Rocket> rockets = new ArrayList<>();
    
    private long lastFireTime = 0;
    
    private final double fireDelay = 0.1f;
    
    private final int maxRockets = 3;
    
    public Player(WorldInfo world, List<Event> events)
    {
        sprite.setTexture(Global.textures.get("player"));
        
        sprite.setPosition(0, world.windowDim.y - (world.groundHeight + sprite.getGlobalBounds().height) + 12);
        
        this.world = world;
        this.events = events;
    }
    
    @Override
    public void update()
    {
        if (Keyboard.isKeyPressed(Keyboard.Key.LEFT) || Keyboard.isKeyPressed(Keyboard.Key.A))
        {
            sprite.move(-moveAmount, 0);
            
            if (sprite.getPosition().x < 0)
                sprite.setPosition(0, sprite.getPosition().y);
        }
        
        if (Keyboard.isKeyPressed(Keyboard.Key.RIGHT) || Keyboard.isKeyPressed(Keyboard.Key.D))
        {
            sprite.move(moveAmount, 0);
            
            if (sprite.getPosition().x + sprite.getGlobalBounds().width >= world.windowDim.x)
                sprite.setPosition(world.windowDim.x - sprite.getGlobalBounds().width, sprite.getPosition().y);
        }
        
        for (Event event : events)
            if (event.type == Event.Type.MOUSE_BUTTON_PRESSED && event.asMouseButtonEvent().button == Mouse.Button.LEFT && (System.currentTimeMillis() - lastFireTime)/1000d > fireDelay && rockets.size() + 1 <= maxRockets)
            {
                lastFireTime = System.currentTimeMillis();
                
                MouseButtonEvent ev = event.asMouseButtonEvent();
                        
                Vector2i mPos = ev.position;
                
                Vector2i pos = new Vector2i(getPosition());
                
                double angle = Math.atan2(mPos.y - pos.y, mPos.x - pos.x);
                
                Rocket rocket = new Rocket(world, getPosition(), angle, 7, 0);
                
                rockets.add(rocket);
                
                world.entitiesToAdd.add(rocket);
            }
        
        if (blinking && elapsed(invincibilityStartTime, System.currentTimeMillis()) > invincibilityTime)
        {
            blinking = false;
        }
        
        for (Iterator<Rocket> iterator = rockets.iterator(); iterator.hasNext();)
        {
            Rocket rocket = iterator.next();
            
            if (rocket.shouldRemove())
                iterator.remove();
        }
    }
    
    public void damage(int damage)
    {
        if (blinking)
            return;
        
        lives -= damage;
        
        blinking = true;
        
        invincibilityStartTime = System.currentTimeMillis();
        
        lastBlinkSwitch = System.currentTimeMillis();
    }
    
    public int getLives()
    {
        return lives;
    }
    
    private double elapsed(long time1, long time2)
    {
        return (time2 - time1)/1000d;
    }
    
    private void drawSprite(RenderTarget target, int transparency)
    {
        sprite.setColor(new Color(Color.WHITE, transparency));
        
        target.draw(sprite);
    }
    
    @Override
    public void draw(RenderTarget target)
    {
        double elap = elapsed(lastBlinkSwitch, System.currentTimeMillis());
        
        if (blinking)
        {
            if (elap > blinkPeriod/2 && elap < blinkPeriod)
                drawSprite(target, 255);
            else if (elap < blinkPeriod/2)
                drawSprite(target, 100);
            else if (elap >= blinkPeriod)
                lastBlinkSwitch = System.currentTimeMillis();

        }
        else
            drawSprite(target, 255);
    }
    
    @Override
    public Vector2f getPosition()
    {
        return Vector2f.add(sprite.getPosition(), new Vector2f(sprite.getGlobalBounds().width/2, sprite.getGlobalBounds().height/2));
    }
    
    @Override
    public FloatRect getBounds()
    {
        return sprite.getGlobalBounds();
    }
}
