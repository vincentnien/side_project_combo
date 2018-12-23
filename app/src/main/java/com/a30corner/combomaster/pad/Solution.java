/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.a30corner.combomaster.pad;

import java.util.ArrayList;

/**
 *
 * @author vincent
 */
public class Solution {
	public int[][] board;
	public RowCol cursor;
	public RowCol init_cursor;
	public ArrayList<Integer> path;
	public boolean is_done;
	public double weight;
	public ArrayList<Match> matches;

	public Solution(Solution solution, int i, int j, RowCol init_rc) {
		this.board = PadBoardAI.copy_board(solution.board);
		cursor = RowCol.make(i, j);
		init_cursor = (init_rc.row == -1 && init_rc.col == -1) ? RowCol.make(i,
				j) : init_rc.clone();
		path = (ArrayList<Integer>) solution.path.clone();
		is_done = false;
		weight = 0;
		matches = new ArrayList<Match>();
	}

	public Solution(int[][] board) {
		this.board = PadBoardAI.copy_board(board);
		cursor = RowCol.make(0, 0);
		init_cursor = RowCol.make(0, 0);
		path = new ArrayList<Integer>();
		is_done = false;
		weight = 0;
		matches = new ArrayList<Match>();
	}

	public static Solution make(int[][] board) {
		return new Solution(board);
	}

	public static Solution clone(Solution solution) {
		return new Solution(solution, solution.cursor.row, solution.cursor.col,
				solution.init_cursor);
	}

	public static Solution copy_solution_with_cursor(Solution solution, int i,
			int j, RowCol init_cursor) {
		if (init_cursor == null) {
			init_cursor = RowCol.make(i, j);
		}
		return new Solution(solution, i, j, init_cursor);
	}
}
