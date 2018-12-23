package com.a30corner.combomaster.pad.mode7x6;

import java.util.ArrayList;
import java.util.List;

import com.a30corner.combomaster.pad.RowCol;

public class GameReplay7x6 {
	private ArrayList<RowCol> mReplayPath = new ArrayList<RowCol>();
	private int[][] board;
	private int position = 0;

	public GameReplay7x6() {
	}

	public void start(int[][] gameBoard) {
		clear();
		board = PadBoardAI7x6.copy_board(gameBoard);
	}

	public int[][] getBoard() {
		return board;
	}

	public void reset() {
		position = 0;
	}

	public void clear() {
		position = 0;
		mReplayPath.clear();
	}

	public void add(RowCol rc) {
		mReplayPath.add(rc);
	}

	public int current() {
		return position;
	}

	public boolean hasNext() {
		return (position < mReplayPath.size());
	}

	public final List<RowCol> getPath() {
		return mReplayPath;
	}

	public RowCol next() {
		if (position < mReplayPath.size()) {
			RowCol rc = mReplayPath.get(position);
			++position;
			return rc;
		}
		return null;
	}

	public boolean hasReplay() {
		return mReplayPath.size() > 0;
	}

	public int size() {
		return mReplayPath.size() - 1;
	}
}