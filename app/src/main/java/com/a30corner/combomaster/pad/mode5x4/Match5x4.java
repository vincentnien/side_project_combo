/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.a30corner.combomaster.pad.mode5x4;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.a30corner.combomaster.pad.Match;
import com.a30corner.combomaster.pad.PadBoardAI;
import com.a30corner.combomaster.pad.RowCol;

/**
 *
 * @author vincent
 */
public class Match5x4 extends Match {

	public Match5x4(int type, int count, int pcount, ArrayList<RowCol> list) {
		super(type, count, pcount, list);
	}

	@Override
	public boolean isOneRow() {
		// check two way attack or one-row attack
		if (count >= 5) {
			int[] line = { 0, 0, 0, 0, 0, 0 };
			for (RowCol rc : list) {
				++line[rc.col];
			}
			for (int i = 0; i < 4; ++i) {
				if (line[i] == 5) {
					return true;
				}
			}
		}
		return false;
	}
	
	@Override
	public boolean isCross() {
		if(count == 5) {
			List<Integer> data = new ArrayList<Integer>(5);
			for(int i=0; i<5; ++i) {
				RowCol rc = list.get(i);
				data.add(rc.row+rc.col*PadBoardAI5x4.ROWS);
			}
			Collections.sort(data);
			int middle = data.get(2);
			return (data.get(1) == middle-1 && data.get(3) == middle+1 &&
					data.get(0) == middle-PadBoardAI5x4.ROWS && data.get(4) == middle+PadBoardAI5x4.ROWS);
		}
		return false;
	}

	public static Match5x4 make(int current, int count2, int pcount,
			ArrayList<RowCol> matchList) {
		return new Match5x4(current, count2, pcount, matchList);
	}
}
