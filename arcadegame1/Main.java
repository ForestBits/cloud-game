package arcadegame1;

import arcadegame1.screen.ScreenConstructionParameters;
import arcadegame1.screen.ScreenEngine;
import arcadegame1.screen.ScreenSetter;
import arcadegame1.screen.ScreenUpdateParameters;
import arcadegame1.screens.GameScreen;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.jsfml.graphics.ConstTexture;
import org.jsfml.graphics.Font;
import org.jsfml.graphics.Image;
import org.jsfml.graphics.RenderWindow;
import org.jsfml.graphics.Texture;
import org.jsfml.graphics.TextureCreationException;
import org.jsfml.window.VideoMode;


public class Main 
{
    private static Path getPath(String filename)
    {
        return Paths.get("res/" + filename);
    }
    
    private static ConstTexture loadTexture(String filename) throws IOException
    {
        Texture texture = new Texture();
        
        texture.loadFromFile(getPath(filename));
        
        return texture;
    }
    
    private static Image loadImage(String filename) throws IOException
    {
        Image image = new Image();
        
        image.loadFromFile(getPath(filename));
        
        return image;
    }
    
    private static Image loadScaledImage(String filename, double xScale, double yScale) throws IOException
    {
        Image image = loadImage(filename);
        
        Image n = ImageScaler.scaleImage(image, xScale, yScale);
        
        return n;
    }
    
    private static Texture loadScaledTexture(String filename, double xScale, double yScale) throws IOException, TextureCreationException
    {
        Image image = loadScaledImage(filename, xScale, yScale);
        
        Texture texture = new Texture();
        
        texture.loadFromImage(image);
        
        return texture;
    }
    
    private static Texture loadTextureFromImage(Image image) throws TextureCreationException
    {
        Texture texture = new Texture();
        
        texture.loadFromImage(image);
        
        return texture;
    }
    
    private static Image scaleAnimation(Image spritesheet, int frames, int tileSizeX, int tileSizeY, double xScale, double yScale)
    {
        Image[] images = new Image[frames];
        
        for (int i = 0; i < frames; ++i)
            images[i] = SpriteSheetUtil.loadTileAsImage(spritesheet, i, 0, tileSizeX, tileSizeY);
        
        for (int i = 0; i < frames; ++i)
            images[i] = ImageScaler.scaleImage(images[i], xScale, yScale);
        
        return AnimationStitcher.stitch(images);
    }
    
    private static Image loadScaledAnimation(String filename, int frames, int tileSizeX, int tileSizeY, double xScale, double yScale) throws IOException
    {
        Image image = loadImage(filename);
        
        return scaleAnimation(image, frames, tileSizeX, tileSizeY, xScale, yScale);
    }
    
    private static Texture loadScaledAnimationAsTexture(String filename, int frames, int tileSizeX, int tileSizeY, double xScale, double yScale) throws IOException, TextureCreationException
    {
        Image image = loadScaledAnimation(filename, frames, tileSizeX, tileSizeY, xScale, yScale);
        
        return loadTextureFromImage(image);
    }
    
    private static Texture loadScaledImageAsTexture(String filename, double xScale, double yScale) throws TextureCreationException, IOException
    {
        return loadTextureFromImage(loadScaledImage(filename, xScale, yScale));
    }
    
    private static List<ImageParticle> loadImageParticles(Image image)
    {
        List<ImageParticle> particles = new ArrayList<>();
        
        for (int x = 0; x < image.getSize().x; ++x)
            for (int y = 0; y < image.getSize().y; ++y)
                if (image.getPixel(x, y).a != 0)
                particles.add(new ImageParticle(x, y, image.getPixel(x, y)));
        
        return particles;
    }
    
    public static void main(String[] args)
    {
        ScreenEngine engine = new ScreenEngine();
        
        ScreenConstructionParameters cParams = new ScreenConstructionParameters();
        
        ScreenUpdateParameters uParams = new ScreenUpdateParameters();
        
        ScreenSetter setter = new ScreenSetter(engine);
        
        RenderWindow window = new RenderWindow();
        
        Font font = new Font();
        
        Map<String, Image> images = Global.images;
        Map<String, ConstTexture> textures = Global.textures;
        Map<String, AnimationData> animations = Global.animations;
        
        try
        {
            font.loadFromFile(getPath("font.ttf"));
            
            images.put("grass", loadScaledImage("grass.png", 4, 4));
            images.put("dirt", loadScaledImage("dirt.png", 4, 4));
            images.put("player", loadScaledImage("player.png", 4, 4));
            images.put("heart", loadScaledImage("heart.png", 4, 4));
            images.put("cloud", loadScaledImage("cloud.png", 8, 8));
            images.put("raincloud", loadScaledImage("raincloud.png", 8, 8));
            images.put("rocket", loadScaledImage("rocket.png", 3, 3));
            images.put("rocketExhaust", loadScaledImage("rocketExhaust.png", 2, 2));
            images.put("raindrop", loadScaledImage("raindrop.png", 5, 5));
            images.put("thundercloud", loadScaledImage("thundercloud.png", 8, 8));
            images.put("thunderbolt", loadScaledImage("thunderbolt.png", 4, 1));
            images.put("meteor", loadScaledImage("meteor.png", 5, 5));
            
            for (String string : images.keySet())
            {
                Image image = images.get(string);
                
                textures.put(string, loadTextureFromImage(image));
                
                //Global.imageParticles.put(string, loadImageParticles(image));
            }
            
            ConstTexture raincloudAnimationTexture = loadScaledAnimationAsTexture("raincloudDeathAnimation.png", 3, 16, 8, 8, 8);
            
            AnimationData raincloudDeathAnimation = new AnimationData(raincloudAnimationTexture, 
                    new AnimationFrame(SpriteSheetUtil.getRectangle(0, 0, 16*8, 8*8), 1),
                    new AnimationFrame(SpriteSheetUtil.getRectangle(1, 0, 16*8, 8*8), 1),
                    new AnimationFrame(SpriteSheetUtil.getRectangle(2, 0, 16*8, 8*8), 1)          
            );
            
            ConstTexture thundercloudAnimationTexture = loadScaledAnimationAsTexture("thundercloudDeathAnimation.png", 3, 24, 10, 8, 8);
            
            AnimationData thundercloudDeathAnimation = new AnimationData(thundercloudAnimationTexture, 
                    new AnimationFrame(SpriteSheetUtil.getRectangle(0, 0, 24*8, 10*8), 1),
                    new AnimationFrame(SpriteSheetUtil.getRectangle(1, 0, 24*8, 10*8), 1),
                    new AnimationFrame(SpriteSheetUtil.getRectangle(2, 0, 24*8, 10*8), 1)          
            );
            
            ConstTexture rocketExplosionTexture = loadScaledAnimationAsTexture("rocketExplosionAnimation.png", 5, 16, 16, 5, 5);
            
            AnimationData rocketExplosionAnimation = new AnimationData(rocketExplosionTexture,
                    new AnimationFrame(SpriteSheetUtil.getRectangle(0, 0, 16*5, 16*5), 0.2),
                    new AnimationFrame(SpriteSheetUtil.getRectangle(1, 0, 16*5, 16*5), 0.2),
                    new AnimationFrame(SpriteSheetUtil.getRectangle(2, 0, 16*5, 16*5), 0.2),
                    new AnimationFrame(SpriteSheetUtil.getRectangle(3, 0, 16*5, 16*5), 0.2),
                    new AnimationFrame(SpriteSheetUtil.getRectangle(4, 0, 16*5, 16*5), 0.2)
            );
            
            Global.animations.put("raincloud", raincloudDeathAnimation);
            Global.animations.put("thundercloud", thundercloudDeathAnimation);
            Global.animations.put("rocketExplosion", rocketExplosionAnimation);
        }
        
        catch (Exception ex)//IOException | TextureCreationException ex)
        {
            throw new RuntimeException(ex);
        }
        
        Global.font = font;
        
        cParams.window = window;
        cParams.screenSetter = setter;

        uParams.cParams = cParams;
        uParams.screenSetter = setter;
        uParams.window = window;
        
        window.create(new VideoMode(800, 600), "Untitled");
        
        engine.setScreen(new GameScreen(cParams));
        
        try
        {
            while (true)
            {
                engine.update(uParams);
                
                engine.draw(window);
                
                window.display();

                Thread.sleep(16);
            }
        }
        
        catch (Exception ex)
        {
            throw new RuntimeException(ex);
        }
    }
}
