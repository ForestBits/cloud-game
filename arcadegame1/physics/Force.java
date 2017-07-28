package arcadegame1.physics;

import org.jsfml.system.Vector2f;


public class Force 
{
    public double angle;
    public double speed;
    
    public Force(double angle, double speed)
    {
        this.angle = angle;
        this.speed = speed;
    }
    
    public Vector2f getOffset()
    {
        float x = (float) (Math.cos(angle)*speed);
        float y = (float) (Math.sin(angle)*speed);
        
        return new Vector2f(x, y);
    }
    
    public void addForce(Force force)
    {
        Vector2f current = getOffset();
        Vector2f other = force.getOffset();
        
        Vector2f combined = Vector2f.add(current, other);
        
        speed = Math.sqrt(Math.pow(combined.x, 2) + Math.pow(combined.y, 2));
        angle = Math.atan2(combined.y, combined.x);
    }
}
