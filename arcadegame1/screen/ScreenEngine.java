package arcadegame1.screen;

import org.jsfml.graphics.RenderTarget;


public class ScreenEngine 
{
    private Screen currentScreen;
    
    public void setScreen(Screen screen)
    {
        currentScreen = screen;
    }
    
    public void update(ScreenUpdateParameters uParams)
    {
        currentScreen.update(uParams);
    }
    
    public void draw(RenderTarget target)
    {
        currentScreen.draw(target);
    }
}
