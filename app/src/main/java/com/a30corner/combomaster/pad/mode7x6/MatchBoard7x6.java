/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.a30corner.combomaster.pad.mode7x6;

import java.util.ArrayList;

/**
 *
 * @author vincent
 */
public class MatchBoard7x6 {

	public final int[][] board;
	public final ArrayList<Match7x6> matches;

	public MatchBoard7x6(int[][] board, ArrayList<Match7x6> matches) {
		this.board = board;
		this.matches = matches;
	}
}
