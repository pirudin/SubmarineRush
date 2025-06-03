package ru.samsung.gamestudio.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;
import ru.samsung.gamestudio.GameResources;
import ru.samsung.gamestudio.GameSettings;
import ru.samsung.gamestudio.MyGdxGame;
import ru.samsung.gamestudio.components.ButtonView;
import ru.samsung.gamestudio.components.MovingBackgroundView;
import ru.samsung.gamestudio.components.TextView;

public class MenuScreen extends ScreenAdapter {

    MyGdxGame myGdxGame;

    MovingBackgroundView backgroundView;
    TextView titleView;
    ButtonView startButtonView;
    ButtonView settingsButtonView;
    ButtonView exitButtonView;

    public MenuScreen(MyGdxGame myGdxGame) {
        this.myGdxGame = myGdxGame;
        int x = 770;
        backgroundView = new MovingBackgroundView(GameResources.BACKGROUND_IMG_PATH);
        titleView = new TextView(myGdxGame.largeWhiteFont, GameSettings.SCREEN_WIDTH / 2 - 225, 960, "Submarine Rush");
        startButtonView = new ButtonView(x, 646, 440, 70, myGdxGame.commonBlackFont, GameResources.BUTTON_LONG_BG_IMG_PATH, "Start");
        settingsButtonView = new ButtonView(x, 551, 440, 70, myGdxGame.commonBlackFont, GameResources.BUTTON_LONG_BG_IMG_PATH, "Settings");
        exitButtonView = new ButtonView(x, 456, 440, 70, myGdxGame.commonBlackFont, GameResources.BUTTON_LONG_BG_IMG_PATH, "Exit");
    }

    @Override
    public void render(float delta) {

        handleInput();

        myGdxGame.camera.update();
        myGdxGame.batch.setProjectionMatrix(myGdxGame.camera.combined);
        ScreenUtils.clear(Color.CLEAR);

        myGdxGame.batch.begin();

        backgroundView.draw(myGdxGame.batch);
        titleView.draw(myGdxGame.batch);
        exitButtonView.draw(myGdxGame.batch);
        settingsButtonView.draw(myGdxGame.batch);
        startButtonView.draw(myGdxGame.batch);

        myGdxGame.batch.end();
    }

    private void handleInput() {
        if (Gdx.input.justTouched()) {
            myGdxGame.touch = myGdxGame.camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));

            if (startButtonView.isHit(myGdxGame.touch.x, myGdxGame.touch.y)) {
                myGdxGame.setScreen(myGdxGame.gameScreen);
            }
            if (exitButtonView.isHit(myGdxGame.touch.x, myGdxGame.touch.y)) {
                Gdx.app.exit();
            }
            if (settingsButtonView.isHit(myGdxGame.touch.x, myGdxGame.touch.y)) {
                myGdxGame.setScreen(myGdxGame.settingsScreen);
            }
        }
    }
}