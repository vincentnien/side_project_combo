package com.a30corner.combomaster.scene;

import java.util.List;

import org.andengine.entity.IEntity;

import android.util.Pair;

import com.a30corner.combomaster.pad.Match;
import com.a30corner.combomaster.pad.monster.ActiveSkill;
import com.a30corner.combomaster.pad.monster.TeamInfo;
import com.a30corner.combomaster.playground.PadEnvironment.ICastCallback;
import com.a30corner.combomaster.scene.PadGameScene.GameState;

public abstract class PlaygroundGameScene extends BaseMenuScene {

    public abstract void onPlayerTurn();
    public abstract void onFireSkills(boolean start);
    public abstract boolean skillFired(ActiveSkill skill, ICastCallback callback, boolean byEnemy);
    public abstract boolean isClearBoard();
    
    public abstract GameState getState();
    
    public abstract TeamInfo getTeam();
    public abstract List<Match> getMatches();
    public abstract int popBombCount();
    
    public abstract void clearBoard(ICastCallback callback);
    
    public abstract void attach(List<? extends IEntity> entity);
    public abstract void attach(IEntity entity);
    public abstract void detach(IEntity entity);
    public abstract void detach(List<IEntity> entity);
    
    public abstract int[][] getBoard();
    public abstract void setChangeColorBoard(final int[][] board, final ICastCallback callback, boolean byEnemy);
    public abstract Pair<Integer, Integer> getSize();
    public abstract void setBoardAlpha(float alpha, float to);
    public abstract void removeDropRateSkill(boolean negative);
    public abstract void removeDropLock();
    
    public abstract void setAdditionMoveTime(int msec);
    public abstract void setAdditionMoveTimeX(int x);
    
    public abstract void enableCrossShield(boolean enable);
    
    public abstract void updateSpecialLST();
    
    public abstract void removeAddComboSkill();

    public abstract void updateSuperDark(int turns);

    public abstract void removeSkill(String key);

    public boolean is7x6() {
        return false;
    }

    public abstract boolean isNullAwoken();


}
