package arcadegame1.screen;


public class ScreenSetter 
{
    private final ScreenEngine engine;
    
    public ScreenSetter(ScreenEngine engine)
    {
        this.engine = engine;
    }
    
    public void changeScreen(Screen screen)
    {
        engine.setScreen(screen);
    }
}
