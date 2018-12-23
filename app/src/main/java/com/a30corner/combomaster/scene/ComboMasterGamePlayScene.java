package com.a30corner.combomaster.scene;

import java.util.List;

import com.a30corner.combomaster.pad.enemy.EnemySkill;
import com.a30corner.combomaster.pad.monster.ActiveSkill;

//FIXME: dont use so many function, use observer instead?
public abstract class ComboMasterGamePlayScene extends BaseMenuScene {

	public abstract void updateHp();

	public abstract void displayBuffSkill(ActiveSkill skill, boolean display);

	public abstract void changeTheWorld(int time);

	public abstract void updateColorChangedBoard(int[][] gameBoard,
			int[][] original);

	public abstract void updateGameBoard(int[][] newBoard);

//	public abstract void updateGameBoard(int[][] newBoard,
//			IAnimationCallback callback);

	public abstract void notify(String from, EnemySkill skill);

	public abstract void displayGravityDamage(List<Double> damaged);

	public abstract void displayEnemySkill(EnemySkill skill, boolean display);

	public abstract void updateEnemySkill(EnemySkill skill, int turn);
}
