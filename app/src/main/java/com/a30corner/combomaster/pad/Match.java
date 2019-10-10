/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.a30corner.combomaster.pad;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author vincent
 */
public class Match implements IMatch {
	public final int type;
	public final int count;
	public final int pcount;
	public final List<RowCol> list;

	public Match(int type, int count, int pcount, List<RowCol> list) {
		this.type = type;
		this.count = count;
		this.list = list;
		this.pcount = pcount;
	}

	@Override
	public int type() {
		return type;
	}

	@Override
	public int count() {
		return count;
	}

	@Override
	public boolean isHeartColumn() {
		// check two way attack or one-row attack
		if (type == 2 && count >= 5) {
			int[] line = { 0, 0, 0, 0, 0, 0 };
			for (RowCol rc : list) {
				++line[rc.row];
			}
			for (int i = 0; i < 6; ++i) {
				if (line[i] == 5) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public boolean isLFormat() {
		if (count == 5) {
			int[] rows = {0, 0, 0, 0, 0, 0, 0};
			int[] cols = {0, 0, 0, 0, 0, 0};
			boolean isRow3 = false, isCol3 = false;
			int maxc = 0, minc = 99;
			int maxr = 0, minr = 99;
			for (RowCol rc : list) {
				++rows[rc.row];
				++cols[rc.col];
				if(rc.col>maxc) {
					maxc = rc.col;
				}
				if(rc.col<minc) {
					minc = rc.col;
				}
				if(rc.row>maxr) {
					maxr = rc.row;
				}
				if(rc.row<minr) {
					minr = rc.row;
				}
			}
			int ci = 0, ri = 0;
			for(int i=0; i<5; ++i) {
				if (cols[i] == 3) {
					isCol3 = true;
					ci = i;
				}
				if (rows[i] == 3) {
					isRow3 = true;
					ri = i;
				}
			}
			if(!isRow3) {
				isRow3 = rows[5] == 3;
				ri = 5;
			}
			if( isRow3 && isCol3 ) {
				return (ri == maxr || ri == minr) && (ci == maxc || ci == minc);
			}
		}
		return false;
	}

	@Override
	public boolean isOneRow() {
		// check two way attack or one-row attack
		if (count >= 6) {
			int[] line = { 0, 0, 0, 0, 0 };
			for (RowCol rc : list) {
				++line[rc.col];
			}
			for (int i = 0; i < 5; ++i) {
				if (line[i] == 6) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public boolean isTwoWay() {
		return count == 4;
	}

	public Match clone() {
		return new Match(type, count, pcount, list);
	}

	// public static Match make(int prop, int count) {
	// return new Match(prop, count);
	// }

	public static Match make(int current, int count2, int pcount,
			List<RowCol> matchList) {
		return new Match(current, count2, pcount, matchList);
	}

	@Override
	public boolean isCross() {
		if(count == 5) {
			List<Integer> data = new ArrayList<Integer>(5);
			for(int i=0; i<5; ++i) {
				RowCol rc = list.get(i);
				data.add(rc.row+rc.col*PadBoardAI.ROWS);
			}
			Collections.sort(data);
			int middle = data.get(2);
			return (data.get(1) == middle-1 && data.get(3) == middle+1 &&
					data.get(0) == middle-PadBoardAI.ROWS && data.get(4) == middle+PadBoardAI.ROWS);
		}
		return false;
	}

	@Override
	public boolean isSquare() {
		if(count == 9) {
			List<Integer> data = new ArrayList<Integer>(9);
			for(int i=0; i<9; ++i) {
				RowCol rc = list.get(i);
				data.add(rc.row+rc.col*PadBoardAI.ROWS);
			}
			Collections.sort(data);
			int middle = data.get(4);
			return data.get(3) == middle-1 && data.get(5) == middle+1 &&
					data.get(1) == middle-PadBoardAI.ROWS && data.get(7) == middle+PadBoardAI.ROWS &&
					data.get(0) == middle-PadBoardAI.ROWS-1 && data.get(2) == middle-PadBoardAI.ROWS+1 &&
					data.get(6) == middle+PadBoardAI.ROWS-1 && data.get(8) == middle+PadBoardAI.ROWS+1;
		}
		return false;
	}
}
