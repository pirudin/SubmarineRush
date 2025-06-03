package ru.samsung.gamestudio;
import com.badlogic.gdx.utils.TimeUtils;
import ru.samsung.gamestudio.managers.MemoryManager;

import java.util.ArrayList;


public class GameSession {

    public GameState state;
    long nextEnemyShipSpawnTime;
    long sessionStartTime;
    long pauseStartTime;
    private int score;
    int destructedEnemyShipNumber;

    public GameSession() {
    }

    public void startGame() {
        state = GameState.PLAYING;
        score = 0;
        destructedEnemyShipNumber = 0;
        sessionStartTime = TimeUtils.millis();
        nextEnemyShipSpawnTime = sessionStartTime + (long) (GameSettings.STARTING_ENEMY_SHIP_APPEARANCE_COOL_DOWN
                * getTrashPeriodCoolDown());
    }

    public void pauseGame() {
        state = GameState.PAUSED;
        pauseStartTime = TimeUtils.millis();
    }

    public void resumeGame() {
        state = GameState.PLAYING;
        sessionStartTime += TimeUtils.millis() - pauseStartTime;
    }

    public void endGame() {
        updateScore();
        state = GameState.ENDED;
        ArrayList<Integer> recordsTable = MemoryManager.loadRecordsTable();
        if (recordsTable == null) {
            recordsTable = new ArrayList<>();
        }
        int foundIdx = 0;
        for (; foundIdx < recordsTable.size(); foundIdx++) {
            if (recordsTable.get(foundIdx) < getScore()) break;
        }
        recordsTable.add(foundIdx, getScore());
        MemoryManager.saveTableOfRecords(recordsTable);
    }

    public void destructionRegistration() {
        destructedEnemyShipNumber += 1;
    }

    public void updateScore() {
        score = destructedEnemyShipNumber;
    }

    public int getScore() {
        return score;
    }

    public boolean shouldSpawnEnemyShip() {
        if (nextEnemyShipSpawnTime <= TimeUtils.millis()) {
            nextEnemyShipSpawnTime = TimeUtils.millis() + (long) (GameSettings.STARTING_ENEMY_SHIP_APPEARANCE_COOL_DOWN
                    * getTrashPeriodCoolDown());
            return true;
        }
        return false;
    }

    private float getTrashPeriodCoolDown() {
        return (float) Math.exp(-0.001 * (TimeUtils.millis() - sessionStartTime + 1) / 1000);
    }
}