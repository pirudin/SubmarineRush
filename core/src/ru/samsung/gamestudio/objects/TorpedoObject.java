package ru.samsung.gamestudio.objects;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import ru.samsung.gamestudio.GameSettings;

public class TorpedoObject extends GameObject {

    public boolean wasHit;

    public TorpedoObject(int x, int y, int width, int height, int xSpeed, String texturePath, World world) {
        super(texturePath, x, y, width, height, GameSettings.TORPEDO_BIT, world);
        body.setLinearVelocity(new Vector2(xSpeed, 0));
        body.setBullet(true);
        wasHit = false;
    }

    public boolean hasToBeDestroyed() {
        return wasHit || (getX() + width / 2 > GameSettings.SCREEN_WIDTH);
    }

    @Override
    public void hit() {
        wasHit = true;
    }
}