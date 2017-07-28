package arcadegame1.screens;

import arcadegame1.physics.CollisionResult;
import arcadegame1.physics.CollisionType;
import arcadegame1.physics.Force;
import arcadegame1.physics.Physics;
import arcadegame1.screen.Screen;
import arcadegame1.screen.ScreenConstructionParameters;
import arcadegame1.screen.ScreenUpdateParameters;
import java.util.List;
import org.jsfml.graphics.Color;
import org.jsfml.graphics.RenderTarget;
import org.jsfml.window.event.Event;
import java.util.ArrayList;
import java.util.Random;
import org.jsfml.graphics.FloatRect;
import org.jsfml.graphics.IntRect;
import org.jsfml.graphics.RectangleShape;
import org.jsfml.system.Vector2f;
import org.jsfml.system.Vector2i;
import org.jsfml.window.Keyboard;
import org.jsfml.window.Keyboard.Key;


public class MainMenuScreen extends Screen
{    
    private final RectangleShape player = new RectangleShape();
    
    private final List<RectangleShape> platforms = new ArrayList<>();
    
    private final Random random = new Random();
    
    private final Vector2i fieldSize;
    
    private final Force gravityForce = new Force(Math.PI/2, 0.05);
    
    private final Force playerForce = new Force(0, 0);
    
    private IntRect generateRectangle()
    {
        int left = random.nextInt(fieldSize.x);
        int top = random.nextInt(fieldSize.y);
        
        int width = random.nextInt(100) + 32;
        int height = random.nextInt(32) + 32;
        
        return new IntRect(left, top, width, height);
    }
    
    public MainMenuScreen(ScreenConstructionParameters params)
    {
        fieldSize = params.window.getSize();
        
        player.setFillColor(Color.RED);
        player.setSize(new Vector2f(32, 32));
        
        for (int i = 0; i < 6; ++i)
        {
            IntRect rect = generateRectangle();
            
            RectangleShape r = new RectangleShape();
            
            r.setFillColor(Color.WHITE);
            
            r.setSize(new Vector2f(rect.width, rect.height));
            
            r.setPosition(rect.left, rect.top);
            
            platforms.add(r);
        }
        
        player.setPosition(200, 300);
    }
    
    @Override
    public void update(ScreenUpdateParameters params)
    {
        List<Event> events = getEvents(params.window);

        List<FloatRect> rects = new ArrayList<>();
        
        for (RectangleShape rect : platforms)
            rects.add(rect.getGlobalBounds());
        
        float xMove = 0;
        float yMove = 0;
        
        float moveAmount = 3.5f;
        
        if (Keyboard.isKeyPressed(Key.RIGHT))
            xMove = moveAmount;
        
        if (Keyboard.isKeyPressed(Key.LEFT))
            xMove = -moveAmount;
        
        if (Keyboard.isKeyPressed(Key.UP))
            yMove = -moveAmount;
        
        if (Keyboard.isKeyPressed(Key.DOWN))
            yMove = moveAmount;
        
        playerForce.addForce(gravityForce);
        
        Vector2f offset = playerForce.getOffset();
        
        xMove += offset.x;
        yMove += offset.y;
        
        CollisionResult[] results = Physics.move(player.getGlobalBounds(), rects, xMove, yMove);
        
        player.setPosition(results[1].rect.left, results[1].rect.top);
        
        if (results[0].type != CollisionType.NONE || results[1].type != CollisionType.NONE)
            playerForce.speed = 0;
    }
    
    @Override
    public void draw(RenderTarget target)
    {
        target.clear(Color.BLACK);
        
        for (RectangleShape rect : platforms)
            target.draw(rect);
        
        target.draw(player);
    }
}
