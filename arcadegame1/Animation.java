package arcadegame1;

import org.jsfml.graphics.ConstTexture;
import org.jsfml.graphics.IntRect;
import org.jsfml.graphics.Texture;



public class Animation 
{
    private final AnimationData data;
    
    private double elapsed = 0;
    
    private long lastTime = System.currentTimeMillis();
    
    private int index = 0;
    
    private final boolean repeat;
    private boolean playing = false;
    
    public Animation(AnimationData data, boolean repeat)
    {
        this.data = data;
        this.repeat = repeat;
    }
    
    public ConstTexture getTexture()
    {
        return data.texture;
    }
    
    public void start()
    {
        elapsed = 0;
        
        index = 0;
        
        lastTime = System.currentTimeMillis();
        
        playing = true;
    }
    
    public IntRect currentRect()
    {
        if (!playing)
            throw new RuntimeException("Using animation that isn't playing.");
        
        double elap = (System.currentTimeMillis() - lastTime)/1000f;
        
        elapsed += elap;
        
        while (true)
        {
            double duration = data.frames[index].duration;
            
            if (elapsed > duration)
            {
                elapsed -= duration;
                
                ++index;
                
                if (index >= data.frames.length)
                {
                    if (repeat)
                        index = 0;
                    else
                    {
                        --index;
                        
                        playing = false;
                    }
                }
            }
            else
                break;
        }
        
        lastTime = System.currentTimeMillis();
        
        return data.frames[index].rect;
    }
    
    public boolean isPlaying()
    {
        return playing;
    }
}
