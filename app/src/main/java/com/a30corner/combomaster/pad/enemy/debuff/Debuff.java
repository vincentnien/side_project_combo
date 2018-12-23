package com.a30corner.combomaster.pad.enemy.debuff;

public class Debuff {

	protected static final int INF = -1;

	public enum Type {
		POISON, REDUCE_DEF, DELAY, REDUCE_TIME,
	}

	// private ActiveSkill skill;
	private int turn;
	private int value;
	private Type type;

	public Debuff(Type type, int turn, int value) {
		this.type = type;
		this.turn = turn;
		this.value = value;
	}

	public Type getType() {
		return type;
	}

	public int getTurn() {
		return turn;
	}

	public int getValue() {
		return value;
	}

	public boolean countDown() {
		if (turn == INF) {
			return false;
		}
		return (--turn == 0);
	}

}
