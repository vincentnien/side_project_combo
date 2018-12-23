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
public class BoardRowCol {

	public static BoardRowCol make(int[][] board, RowCol rc) {
		return new BoardRowCol(board, rc);
	}

	public final int[][] board;
	public final RowCol rc;

	public BoardRowCol(int[][] board, RowCol rc) {
		this.board = board;
		this.rc = rc.clone();
	}
}
