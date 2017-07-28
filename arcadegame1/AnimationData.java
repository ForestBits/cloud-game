package arcadegame1;

import org.jsfml.graphics.ConstTexture;


public class AnimationData 
{
    public final ConstTexture texture;
    public final AnimationFrame[] frames;
    
    public AnimationData(ConstTexture texture, AnimationFrame... frames)
    {
        this.texture = texture;
        this.frames = frames;
    }
    
    public Animation getAnimation(boolean shouldRepeat)
    {
        return new Animation(this, shouldRepeat);
    }
}
