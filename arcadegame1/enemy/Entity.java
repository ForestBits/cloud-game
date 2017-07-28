package arcadegame1.enemy;

import org.jsfml.graphics.FloatRect;
import org.jsfml.graphics.RenderTarget;


public abstract class Entity
{
    private boolean shouldRemove = false;
    
    public abstract void update();
    
    public abstract void draw(RenderTarget target);
    
    public void remove()
    {
        shouldRemove = true;
    }
    
    public boolean shouldRemove()
    {
        return shouldRemove;
    }
    
    public abstract FloatRect getBounds();
}
