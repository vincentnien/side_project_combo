/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.a30corner.combomaster.pad;

/**
 *
 * @author vincent
 */
public class RowCol {

	public int row;
	public int col;

	public RowCol(int row, int col) {
		this.row = row;
		this.col = col;
	}

	public boolean equals(RowCol rc) {
		return row == rc.row && col == rc.col;
	}

	public RowCol clone() {
		return new RowCol(row, col);
	}

	public static RowCol make(int row, int col) {
		return new RowCol(row, col);
	}

	@Override
	public int hashCode() {
		return row + 16 * col;
	}

	public enum Direction {
		DIR_UP(0), DIR_DOWN(1), DIR_LEFT(2), DIR_RIGHT(3), DIR_LEFT_UP(4), DIR_RIGHT_UP(
				5), DIR_LEFT_DOWN(6), DIR_RIGHT_DOWN(7), DIR_NONE(8);

		private final int value;

		private Direction(int v) {
			value = v;
		}

		public int getValue() {
			return value;
		}
	}

	public static Direction direction(RowCol now, RowCol next) {
		int dr = next.row - now.row;
		int dc = next.col - now.col;
		if (dr == 0 && dc == 0) {
			return Direction.DIR_NONE;
		} else if (dr == -1 && dc == 0) {
			return Direction.DIR_LEFT;
		} else if (dr == 0 && dc == -1) {
			return Direction.DIR_UP;
		} else if (dr == 1 && dc == 0) {
			return Direction.DIR_RIGHT;
		} else if (dr == 0 && dc == 1) {
			return Direction.DIR_DOWN;
		} else if (dr == -1 && dc == -1) {
			return Direction.DIR_LEFT_UP;
		} else if (dr == 1 && dc == 1) {
			return Direction.DIR_RIGHT_DOWN;
		} else if (dr == -1 && dc == 1) {
			return Direction.DIR_LEFT_DOWN;
		} else if (dr == 1 && dc == -1) {
			return Direction.DIR_RIGHT_UP;
		}
		return Direction.DIR_NONE;
	}
}
