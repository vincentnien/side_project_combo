package com.a30corner.combomaster.pad;

import java.util.Stack;

import com.a30corner.combomaster.ComboMasterApplication;
import com.a30corner.combomaster.utils.LogUtil;

public class BoardStack {

	private static final int STACK_SIZE = 10;
	private Stack<int[][]> mPreviousStack;

	public BoardStack(int[][] init) {
		mPreviousStack = new Stack<int[][]>();
		mPreviousStack.add(PadBoardAI.copy_board(init));
	}

	public void push(int[][] gameBoard) {
		int[][] temp = PadBoardAI.copy_board(gameBoard);
		int size = mPreviousStack.size();
		if (size >= STACK_SIZE) {
			LogUtil.e("remove element at 0/", size);
			mPreviousStack.removeElementAt(0);
		}
		mPreviousStack.push(temp);
		ComboMasterApplication.getsInstance().setBoardCache(gameBoard);
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
