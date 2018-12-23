package com.a30corner.combomaster.pad.mode5x4;

import java.util.Stack;

import com.a30corner.combomaster.utils.LogUtil;

public class BoardStack5x4 {

	private static final int STACK_SIZE = 10;
	private Stack<int[][]> mPreviousStack;

	public BoardStack5x4(int[][] init) {
		mPreviousStack = new Stack<int[][]>();
		mPreviousStack.add(PadBoardAI5x4.copy_board(init));
	}

	public void push(int[][] gameBoard) {
		int[][] temp = PadBoardAI5x4.copy_board(gameBoard);
		int size = mPreviousStack.size();
		if (size >= STACK_SIZE) {
			LogUtil.e("remove element at 0/", size);
			mPreviousStack.removeElementAt(0);
		}
		mPreviousStack.push(temp);
		//ComboMasterApplication.getsInstance().setBoardCache(gameBoard);
		LogUtil.d("push=>size=", size);
	}

	public int[][] pop() {
		LogUtil.d("pop=>newsize=", mPreviousStack.size() - 1);
		if (mPreviousStack.size() > 1) {
			return mPreviousStack.pop();
		} else {
			return mPreviousStack.peek();
		}
	}

	public int[][] peek() {
		LogUtil.d("peek=>size=", mPreviousStack.size());
		return mPreviousStack.peek();
	}
}
