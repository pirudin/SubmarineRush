package ru.samsung.gamestudio.components;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import ru.samsung.gamestudio.GameSettings;

public class ImageView extends View {

    Texture texture;

    public ImageView(float x, float y, String imagePath) {
        super(x, y);
        texture = new Texture(imagePath);
        this.width = texture.getWidth() ;
        this.height = texture.getHeight() ;
    }

    @Override
    public void draw(SpriteBatch batch) {
        batch.draw(texture, 0, 0, GameSettings.SCREEN_WIDTH, height);
    }

    @Override
    public void dispose() {
        texture.dispose();
    }

}