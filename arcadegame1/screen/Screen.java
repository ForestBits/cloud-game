package arcadegame1.screen;

import java.util.ArrayList;
import java.util.List;
import org.jsfml.graphics.RenderTarget;
import org.jsfml.graphics.RenderWindow;
import org.jsfml.window.event.Event;


public abstract class Screen 
{
    public abstract void update(ScreenUpdateParameters uParams);
    
    public abstract void draw(RenderTarget target);
    
    public List<Event> getEvents(RenderWindow window)
    {
        List<Event> events = new ArrayList<>();
        
        while (true)
        {
            Event event = window.pollEvent();
            
            if (event == null)
                break;
            
            events.add(event);
        }
        
        return events;
    }
}
