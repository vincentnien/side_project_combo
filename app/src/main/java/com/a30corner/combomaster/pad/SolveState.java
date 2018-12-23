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
public class SolveState {
	public int max_length;
	public int dir_step = 2;
	public int p = 0;
	public ArrayList<Solution> solutions;

	public SolveState(int len, int dir_step, ArrayList<Solution> solutions) {
		this.max_length = len;
		this.dir_step = dir_step;
		this.solutions = solutions;
	}
}
