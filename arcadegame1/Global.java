package arcadegame1;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jsfml.graphics.ConstFont;
import org.jsfml.graphics.ConstTexture;
import org.jsfml.graphics.Image;


public class Global 
{
    public static final Map<String, Image> images = new HashMap<>();
    public static final Map<String, ConstTexture> textures = new HashMap<>();
    public static final Map<String, AnimationData> animations = new HashMap<>();
    public static final Map<String, List<ImageParticle>> imageParticles = new HashMap<>();
    
    public static ConstFont font;
}
