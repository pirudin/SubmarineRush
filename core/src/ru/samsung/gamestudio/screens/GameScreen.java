package ru.samsung.gamestudio.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;
import ru.samsung.gamestudio.*;
import ru.samsung.gamestudio.components.*;
import ru.samsung.gamestudio.managers.ContactManager;
import ru.samsung.gamestudio.managers.MemoryManager;
import ru.samsung.gamestudio.objects.TorpedoObject;
import ru.samsung.gamestudio.objects.ShipObject;
import ru.samsung.gamestudio.objects.EnemyShipObject;

import java.util.ArrayList;

public class GameScreen extends ScreenAdapter {

    MyGdxGame myGdxGame;
    GameSession gameSession;
    ShipObject shipObject;


    ArrayList<EnemyShipObject> enemyShipArray;
    ArrayList<TorpedoObject> torpedoArray;

    ContactManager contactManager;

    // PLAY state UI
    MovingBackgroundView backgroundView;
    ImageView topBlackoutView;
    LiveView liveView;
    TextView scoreTextView;
    ButtonView pauseButton;

    // PAUSED state UI
    ImageView fullBlackoutView;
    TextView pauseTextView;
    ButtonView homeButton;
    ButtonView continueButton;

    // ENDED state UI
    TextView recordsTextView;
    RecordsListView recordsListView;
    ButtonView homeButton2;


    public GameScreen(MyGdxGame myGdxGame) {
        this.myGdxGame = myGdxGame;
        gameSession = new GameSession();

        contactManager = new ContactManager(myGdxGame.world);

        enemyShipArray = new ArrayList<>();
        torpedoArray = new ArrayList<>();

        shipObject = new ShipObject(
                GameSettings.SCREEN_WIDTH / 2, 150,
                GameSettings.SHIP_WIDTH, GameSettings.SHIP_HEIGHT,
                GameResources.SHIP_IMG_PATH,
                myGdxGame.world
        );

        backgroundView = new MovingBackgroundView(GameResources.BACKGROUND_IMG_PATH);
        topBlackoutView = new ImageView(0, 1180, GameResources.BLACKOUT_TOP_IMG_PATH);
        liveView = new LiveView(305, 1215);
        scoreTextView = new TextView(myGdxGame.commonWhiteFont, 100, 35);
        pauseButton = new ButtonView(
                1850, 25,
                46, 54,
                GameResources.PAUSE_IMG_PATH
        );

        fullBlackoutView = new ImageView(0, 0, GameResources.BLACKOUT_FULL_IMG_PATH);
        pauseTextView = new TextView(myGdxGame.largeWhiteFont, GameSettings.SCREEN_WIDTH / 2 - 80, 842, "Pause");
        homeButton = new ButtonView(
                GameSettings.SCREEN_WIDTH / 2 - 400, 695,
                200, 70,
                myGdxGame.commonBlackFont,
                GameResources.BUTTON_SHORT_BG_IMG_PATH,
                "Home"
        );
        continueButton = new ButtonView(
                GameSettings.SCREEN_WIDTH / 2 + 200, 695,
                200, 70,
                myGdxGame.commonBlackFont,
                GameResources.BUTTON_SHORT_BG_IMG_PATH,
                "Continue"
        );

        recordsListView = new RecordsListView(myGdxGame.commonWhiteFont, 690);
        recordsTextView = new TextView(myGdxGame.largeWhiteFont, GameSettings.SCREEN_WIDTH / 2 - 270, GameSettings.SCREEN_HEIGHT / 2 + 300, "You lose. Last records:");
        homeButton2 = new ButtonView(
                GameSettings.SCREEN_WIDTH / 2 - 80, GameSettings.SCREEN_HEIGHT / 2 - 280,
                160, 70,
                myGdxGame.commonBlackFont,
                GameResources.BUTTON_SHORT_BG_IMG_PATH,
                "Home"
        );

    }

    @Override
    public void show() {
        restartGame();
    }

    @Override
    public void render(float delta) {

        handleInput();

        if (gameSession.state == GameState.PLAYING) {
            if (gameSession.shouldSpawnEnemyShip()) {
                EnemyShipObject enemyShip = new EnemyShipObject(
                        GameSettings.ENEMY_SHIP_WIDTH, GameSettings.ENEMY_SHIP_HEIGHT,
                        GameResources.ENEMY_SHIP_IMG_PATH,
                        myGdxGame.world
                );
                enemyShipArray.add(enemyShip);
            }

            if (shipObject.needToShoot()) {
                TorpedoObject torpedo = new TorpedoObject(
                        shipObject.getX() + shipObject.width / 2, shipObject.getY(),
                        GameSettings.TORPEDO_WIDTH, GameSettings.TORPEDO_HEIGHT, GameSettings.SHIP_TORPEDO_X_SPEED,
                        GameResources.TORPEDO_IMG_PATH,
                        myGdxGame.world
                );
                torpedoArray.add(torpedo);
                if (myGdxGame.audioManager.isSoundOn) myGdxGame.audioManager.shootSound.play();
            }

            for (EnemyShipObject enemyShip:
                    enemyShipArray) {
                if (enemyShip.getX() < 0) {
                    gameSession.endGame();
                    recordsListView.setRecords(MemoryManager.loadRecordsTable());
                }
            }


            if (!shipObject.isAlive()) {
                gameSession.endGame();
                recordsListView.setRecords(MemoryManager.loadRecordsTable());
            }

            updateEnemyShips();
            updateTorpedos();
            backgroundView.move();
            gameSession.updateScore();
            scoreTextView.setText("Destroyed: " + gameSession.getScore());
            liveView.setLeftLives(shipObject.getLiveLeft());

            myGdxGame.stepWorld();
        }

        draw();
    }

    private void handleInput() {
        if (Gdx.input.isTouched()) {
            myGdxGame.touch = myGdxGame.camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));

            switch (gameSession.state) {
                case PLAYING:
                    if (pauseButton.isHit(myGdxGame.touch.x, myGdxGame.touch.y)) {
                        gameSession.pauseGame();
                    }
                    shipObject.move(myGdxGame.touch);
                    break;

                case PAUSED:
                    if (continueButton.isHit(myGdxGame.touch.x, myGdxGame.touch.y)) {
                        gameSession.resumeGame();
                    }
                    if (homeButton.isHit(myGdxGame.touch.x, myGdxGame.touch.y)) {
                        myGdxGame.setScreen(myGdxGame.menuScreen);
                    }
                    break;

                case ENDED:

                    if (homeButton2.isHit(myGdxGame.touch.x, myGdxGame.touch.y)) {
                        myGdxGame.setScreen(myGdxGame.menuScreen);
                    }
                    break;
            }

        }
    }

    private void draw() {

        myGdxGame.camera.update();
        myGdxGame.batch.setProjectionMatrix(myGdxGame.camera.combined);
        ScreenUtils.clear(Color.CLEAR);

        myGdxGame.batch.begin();
        backgroundView.draw(myGdxGame.batch);
        for (EnemyShipObject enemyShip : enemyShipArray) enemyShip.draw(myGdxGame.batch);
        shipObject.draw(myGdxGame.batch);
        for (TorpedoObject torpedo : torpedoArray) torpedo.draw(myGdxGame.batch);
        topBlackoutView.draw(myGdxGame.batch);
        scoreTextView.draw(myGdxGame.batch);
        liveView.draw(myGdxGame.batch);
        pauseButton.draw(myGdxGame.batch);

        if (gameSession.state == GameState.PAUSED) {
            fullBlackoutView.draw(myGdxGame.batch);
            pauseTextView.draw(myGdxGame.batch);
            homeButton.draw(myGdxGame.batch);
            continueButton.draw(myGdxGame.batch);
        } else if (gameSession.state == GameState.ENDED) {
            fullBlackoutView.draw(myGdxGame.batch);
            recordsTextView.draw(myGdxGame.batch);
            recordsListView.draw(myGdxGame.batch);
            homeButton2.draw(myGdxGame.batch);
        }

        myGdxGame.batch.end();

    }

    private void updateEnemyShips() {
        for (int i = 0; i < enemyShipArray.size(); i++) {

            boolean hasToBeDestroyed = !enemyShipArray.get(i).isAlive() || !enemyShipArray.get(i).isInFrame();

            if (!enemyShipArray.get(i).isAlive()) {
                gameSession.destructionRegistration();
                if (myGdxGame.audioManager.isSoundOn) myGdxGame.audioManager.explosionSound.play(0.2f);
            }

            if (hasToBeDestroyed) {
                myGdxGame.world.destroyBody(enemyShipArray.get(i).body);
                enemyShipArray.remove(i--);
            }
        }
    }

    private void updateTorpedos() {
        for (int i = 0; i < torpedoArray.size(); i++) {
            if (torpedoArray.get(i).hasToBeDestroyed()) {
                myGdxGame.world.destroyBody(torpedoArray.get(i).body);
                torpedoArray.remove(i--);
            }
        }
    }

    private void restartGame() {

        for (int i = 0; i < enemyShipArray.size(); i++) {
            myGdxGame.world.destroyBody(enemyShipArray.get(i).body);
            enemyShipArray.remove(i--);
        }

        if (shipObject != null) {
            myGdxGame.world.destroyBody(shipObject.body);
        }

        shipObject = new ShipObject(
                GameSettings.SCREEN_WIDTH / 2, 150,
                GameSettings.SHIP_WIDTH, GameSettings.SHIP_HEIGHT,
                GameResources.SHIP_IMG_PATH,
                myGdxGame.world
        );

        torpedoArray.clear();
        gameSession.startGame();
    }

}