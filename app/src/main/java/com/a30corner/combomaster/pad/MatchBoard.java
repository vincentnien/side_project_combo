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
public class MatchBoard {

	public final int[][] board;
	public final ArrayList<Match> matches;

	public MatchBoard(int[][] board, ArrayList<Match> matches) {
		this.board = board;
		this.matches = matches;
	}
}
