package arcadegame1.enemy;

import arcadegame1.Global;
import arcadegame1.ImageParticle;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.jsfml.graphics.Color;
import org.jsfml.graphics.FloatRect;
import org.jsfml.graphics.PrimitiveType;
import org.jsfml.graphics.RenderTarget;
import org.jsfml.graphics.Sprite;
import org.jsfml.graphics.Vertex;
import org.jsfml.graphics.VertexArray;
import org.jsfml.system.Vector2f;

class ImageParticleEntity
{
    public float x;
    public float y;
    public float angle;
    public float speed;
    
    public final Color color;
    
    public ImageParticleEntity(float x, float y, float angle, float speed, Color color)
    {
        this.x = x;
        this.y = y;
        this.angle = angle;
        this.speed = speed;
        this.color = color;
    }
}

public class ImageParticleCollection extends Entity
{    
    private final List<ImageParticleEntity> entities = new ArrayList<>();
    
    private final VertexArray arr = new VertexArray(PrimitiveType.LINE_STRIP);
    
    private final Random random = new Random();
    
    private final float gravityForce = 0.2f;
    ///!!!!try making math match the graphics. Do the math upside down, so there aren't two versions of everything,
    //and mistakes don't get made
    public static ImageParticleCollection from(String imageName, Sprite sprite, Vector2f position)
    {
        List<ImageParticle> particles = Global.imageParticles.get(imageName);
        
        return new ImageParticleCollection(new Vector2f(position.x - sprite.getOrigin().x, position.y + sprite.getOrigin().y), particles);
    }
    
    public ImageParticleCollection(Vector2f basePosition, List<ImageParticle> particles)
    {
        for (int i = 0; i < particles.size(); i += 8)
        {
            ImageParticle particle = particles.get(i);
            
            entities.add(new ImageParticleEntity(basePosition.x + particle.x, basePosition.y + particle.y, (float) (random.nextFloat()*Math.PI*2), random.nextFloat()*1 + 1, particle.color));
        }
    }
    
    @Override
    public void update()
    {
        for (ImageParticleEntity entity : entities)
        {
            double eX = Math.cos(entity.x)*entity.speed;
            double eY = Math.sin(entity.y)*entity.speed;
            
            double speed = Math.sqrt(Math.pow(eX, 2) + Math.pow(eY - gravityForce, 2));
            double angle = Math.atan2(eY - gravityForce, eX);
            
            entity.speed = (float) speed;
            entity.angle = (float) angle;
            
            entity.x += eX;
            entity.y += eY;
        }
    }
    
    @Override
    public void draw(RenderTarget target)
    {
        arr.clear();
        
        for (ImageParticleEntity entity : entities)
            arr.add(new Vertex(new Vector2f(entity.x, target.getSize().y - entity.y), entity.color));
        
        target.draw(arr);
    }
    
    @Override
    public FloatRect getBounds()
    {
        return null;
    }
}
