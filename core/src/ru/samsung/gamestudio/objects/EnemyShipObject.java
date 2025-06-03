package ru.samsung.gamestudio.objects;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import ru.samsung.gamestudio.GameSettings;
import java.util.Random;

public class EnemyShipObject extends GameObject {
    private static final int paddingVertical = 30;

    private int livesLeft;

    public EnemyShipObject(int width, int height, String texturePath, World world) {
        super(
                texturePath,
                GameSettings.SCREEN_WIDTH + width / 2,
                height / 2 + paddingVertical + (new Random()).nextInt((GameSettings.SCREEN_HEIGHT - 2 * paddingVertical - height)),
                width, height,
                GameSettings.ENEMY_SHIP_BIT,
                world
        );

        body.setLinearVelocity(new Vector2(-10, 0));
        livesLeft = 1;
    }

    public boolean isAlive() {
        return livesLeft > 0;
    }

    public boolean isInFrame() {
        return getX() + width / 2 > 0;
    }

    @Override
    public void hit() {
        livesLeft -= 1;
    }
}