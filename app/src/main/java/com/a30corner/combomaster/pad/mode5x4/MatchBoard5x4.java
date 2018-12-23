/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.a30corner.combomaster.pad.mode5x4;

import java.util.ArrayList;

/**
 *
 * @author vincent
 */
public class MatchBoard5x4 {

	public final int[][] board;
	public final ArrayList<Match5x4> matches;

	public MatchBoard5x4(int[][] board, ArrayList<Match5x4> matches) {
		this.board = board;
		this.matches = matches;
	}
}
