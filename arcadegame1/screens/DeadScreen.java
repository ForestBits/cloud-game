package arcadegame1.screens;

import arcadegame1.Global;
import arcadegame1.screen.Screen;
import arcadegame1.screen.ScreenConstructionParameters;
import arcadegame1.screen.ScreenUpdateParameters;
import java.util.List;
import org.jsfml.graphics.Color;
import org.jsfml.graphics.RenderTarget;
import org.jsfml.graphics.Text;
import org.jsfml.window.Keyboard;
import org.jsfml.window.Keyboard.Key;
import org.jsfml.window.event.Event;
import org.jsfml.window.event.Event.Type;


public class DeadScreen extends Screen
{
    private final int cloudsKilled;
    
    private final Text text = new Text();
    
    public DeadScreen(ScreenConstructionParameters params, int cloudsKilled)
    {
        this.cloudsKilled = cloudsKilled;
        
        text.setFont(Global.font);
        text.setColor(Color.BLACK);
        text.setCharacterSize(25);
        text.setString("You killed " + cloudsKilled + " clouds.\nPress space to play again.");
        text.setOrigin(text.getGlobalBounds().width/2, text.getGlobalBounds().height/2);
        text.setPosition(params.window.getSize().x/2, params.window.getSize().y/2);
    }
    
    @Override
    public void update(ScreenUpdateParameters params)
    {
        List<Event> events = getEvents(params.window);
        
        for (Event event : events)
            if (event.type == Type.CLOSED)
                System.exit(0);
        
        if (Keyboard.isKeyPressed(Key.SPACE))
            params.screenSetter.changeScreen(new GameScreen(params.cParams));
    }
    
    @Override
    public void draw(RenderTarget target)
    {
        target.clear(new Color(153, 242, 222));  
        
        target.draw(text);
    }
}
