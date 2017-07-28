package arcadegame1;

import org.jsfml.graphics.IntRect;


public class AnimationFrame
{
    public final IntRect rect;
    public final double duration;
    
    public AnimationFrame(IntRect rect, double duration)
    {
        this.rect = rect;
        this.duration = duration;
    }
}