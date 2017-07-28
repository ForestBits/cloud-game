package arcadegame1.screens;

import arcadegame1.Global;
import arcadegame1.MyMath;
import arcadegame1.enemy.Entity;
import arcadegame1.enemy.Meteor;
import arcadegame1.enemy.Player;
import arcadegame1.enemy.RainCloud;
import arcadegame1.enemy.ThunderCloud;
import arcadegame1.enemy.WorldInfo;
import arcadegame1.screen.Screen;
import arcadegame1.screen.ScreenConstructionParameters;
import arcadegame1.screen.ScreenUpdateParameters;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.jsfml.graphics.Color;
import org.jsfml.graphics.FloatRect;
import org.jsfml.graphics.RectangleShape;
import org.jsfml.graphics.RenderTarget;
import org.jsfml.graphics.Sprite;
import org.jsfml.system.Vector2f;
import org.jsfml.window.event.Event;
import org.jsfml.window.event.Event.Type;


public class GameScreen extends Screen
{
    private final Sprite grassTop = new Sprite();
    private final Sprite grassBottom = new Sprite();
    private final Sprite heartSprite = new Sprite();
    
    private final int grassSize = 32;

    private final Vector2f heartDrawPosition = new Vector2f(10, 10);
    
    private final Player player;
    
    private final WorldInfo world;
    
    private final List<Event> events = new ArrayList<>();
    
    private final RectangleShape boundRectangle = new RectangleShape();
    
    private long lastEnemySpawn = System.currentTimeMillis();
    
    private final double baseEnemySpawnDelay = 1;
    private final double variableEnemySpawnDelay = 5;
    
    private double enemySpawnDelay = baseEnemySpawnDelay;
    
    private final Random random = new Random();
    
    private long startTime = System.currentTimeMillis();
    
    private long lastWindChange = 0;
    
    private final double windChangeDelay = 10;
    
    private int cloudsKilled;
    
    public GameScreen(ScreenConstructionParameters params)
    {
        grassTop.setTexture(Global.textures.get("grass"));
        grassBottom.setTexture(Global.textures.get("dirt"));
        heartSprite.setTexture(Global.textures.get("heart"));
        
        world = new WorldInfo(120, 150, grassSize*2, params.window.getSize());

        player = new Player(world, events);
        
        world.player = player;
        
        world.entities.add(player);
        
        boundRectangle.setFillColor(new Color(Color.RED, 50));
        
        enemySpawnDelay = 5;
    }
    
    private double gameTime()
    {
        return (System.currentTimeMillis() - startTime)/1000d;
    }
    
    private void spawnEnemy()
    {
        float centeredX = -100;
        float cloudY = world.baseCloudHeight + random.nextFloat()*world.cloudMargin;
        
        Vector2f cloudPos = new Vector2f(centeredX, cloudY);
        
        int type = random.nextInt(10);
        
        Entity enemy = null;
        
        switch (type)
        {
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
                enemy = new RainCloud(world, cloudPos);
                
                break;

            case 8:
                enemy = new ThunderCloud(world, cloudPos);
                
                break;
                
            case 9:
                float randomX = random.nextFloat()*world.windowDim.x;
                
                Vector2f pos = new Vector2f(randomX, -20);
                
                double angleToPlayer = MyMath.angle(pos, world.player.getPosition()) + random.nextFloat()*(Math.PI/16);
                
                enemy = new Meteor(world, pos, 20 + random.nextFloat()*100, angleToPlayer, 5 + random.nextFloat()*4, 0);
                
                break;
        }
            
        world.entities.add(enemy);
    }

    @Override
    public void update(ScreenUpdateParameters params)
    {
        events.clear();
        
        events.addAll(getEvents(params.window));
        
        for (Event event : events)
            if (event.type == Type.CLOSED)
                System.exit(0);
        
        if ((System.currentTimeMillis() - lastEnemySpawn)/1000d > enemySpawnDelay)
        {
            lastEnemySpawn = System.currentTimeMillis();
            
            enemySpawnDelay = baseEnemySpawnDelay + random.nextFloat()*variableEnemySpawnDelay - (gameTime()/10)*5;
            
            if (enemySpawnDelay < baseEnemySpawnDelay)
                enemySpawnDelay = baseEnemySpawnDelay;
          
            spawnEnemy();
        }
        
        if ((System.currentTimeMillis() - lastWindChange)/1000d > windChangeDelay)
        {
            lastWindChange = System.currentTimeMillis();
            
            world.windSpeed = 1.5f + random.nextFloat()*4;
        }
                
        for (Entity entity : world.entities)
            entity.update();
        
        world.entities.addAll(world.entitiesToAdd);
        
        world.entitiesToAdd.clear();
        
        List<Entity> toRemove = new ArrayList<>();
        
        for (Entity entity : world.entities)
            if (entity.shouldRemove())
                toRemove.add(entity);
        
        for (Entity entity : toRemove)
        {
            if (entity instanceof RainCloud || entity instanceof ThunderCloud)
                ++cloudsKilled;
            
            world.entities.remove(entity);
        }
        
        if (player.getLives() <= 0)
            params.screenSetter.changeScreen(new DeadScreen(params.cParams, cloudsKilled));
    }
    
    private void drawPlayerLives(RenderTarget target)
    {
        for (int i = 0; i < player.getLives(); ++i)
        {
            heartSprite.setPosition(heartDrawPosition.x + i*(heartSprite.getGlobalBounds().width + 10), heartDrawPosition.y);
            
            target.draw(heartSprite);
        }
    }
    
    @Override
    public void draw(RenderTarget target)
    {
        target.clear(new Color(153, 242, 222));       
       
        for (Entity entity : world.entities)
        {
            entity.draw(target);
            
            FloatRect bounds = entity.getBounds();
            
            if (bounds == null)
                continue;
            
            boundRectangle.setPosition(bounds.left, bounds.top);
            boundRectangle.setSize(new Vector2f(bounds.width, bounds.height));
            
            //target.draw(boundRectangle);
        }
        
        for (int x = 0; x < target.getSize().x; ++x)
        {
            grassBottom.setPosition(x*grassSize, target.getSize().y - grassSize);
            grassTop.setPosition(x*grassSize, target.getSize().y - grassSize*2);
            
            target.draw(grassBottom);
            target.draw(grassTop);
        }
        
        drawPlayerLives(target);
    }
}
