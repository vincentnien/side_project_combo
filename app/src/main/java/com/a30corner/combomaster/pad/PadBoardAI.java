/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.a30corner.combomaster.pad;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;

import android.util.Pair;
import android.util.SparseIntArray;

import com.a30corner.combomaster.utils.LogUtil;
import com.a30corner.combomaster.utils.RandomUtil;

/**
 *
 * @author vincent
 */
public class PadBoardAI {
	public static final int COLS = 5;
	public static final int ROWS = 6;
	private static final int TYPES = 6;

	public PadBoardAI() {
		init();
	}

	public static boolean isValid(int x, int y) {
		return (x >= 0 && x < ROWS && y >= 0 && y < COLS);
	}

	public static int[][] create_empty_board() {
		int[][] result = new int[ROWS][COLS];
		for (int i = 0; i < ROWS; ++i) {
			for (int j = 0; j < COLS; ++j) {
				result[i][j] = UNDEFINED;
			}
		}
		return result;
	}

	public static final long TYPE_FIRE = 0x10;
	public static final long TYPE_LIGHT = 0x1000;
	public static final long TYPE_WATER = 0x10000;
	public static final long TYPE_WOOD = 0x100000;
	public static final long TYPE_DARK = 0x1;
	public static final long TYPE_HEART = 0x100;
	public static final long TYPE_POISON = 0x2;
	public static final long TYPE_JAMAR = 0x4;
	public static final long TYPE_E_POISON = 0x8;

	public static final int ORB_DARK = 0;
	public static final int ORB_FIRE = 1;
	public static final int ORB_HEART = 2;
	public static final int ORB_LIGHT = 3;
	public static final int ORB_WATER = 4;
	public static final int ORB_WOOD = 5;
	public static final int ORB_POISON = 6;
	public static final int ORB_JARMAR = 7;

	private static final String[] ORB_ARRAY = { "dark.png", "fire.png",
			"heart.png", "light.png", "water.png", "wood.png", "poison.png",
			"jammar.png", "enhanced_poison.png" };
	private static final String[] ORB_ARRAY_CW = { "dark_cw.png",
			"fire_cw.png", "heart_cw.png", "light_cw.png", "water_cw.png",
			"wood_cw.png", "poison.png", "jammar.png", "enhanced_poison.png" };

	private static final String[] TOS_GEM_ARRAY = { "gem_dark.png",
			"gem_fire.png", "gem_heart.png", "gem_light.png", "gem_water.png",
			"gem_wood.png", "poison.png", "jammar.png", "enhanced_poison.png" };

	public enum IMAGE_TYPE {
		TYPE_PAD, TYPE_TOS, TYPE_PAD_CW
	}

	private static final int[] RESTRAINT = { 3, 5, -1, 0, 1, 4 };

	public static boolean isRestraint(int attackFrom, int enemy) {
		if(attackFrom < 0 || attackFrom > 5) {
			return false;
		}
		return RESTRAINT[attackFrom] == enemy;
	}

	public static boolean beRestraint(int attackFrom, int enemy) {
		if(enemy < 0 || enemy > 5) {
			return false;
		}
		return RESTRAINT[enemy] == attackFrom;
	}

	public static List<String> getOrbList(IMAGE_TYPE type) {
		if (type == IMAGE_TYPE.TYPE_PAD) {
			return Arrays.asList(ORB_ARRAY);
		} else if (type == IMAGE_TYPE.TYPE_TOS) {
			return Arrays.asList(TOS_GEM_ARRAY);
		} else {
			return Arrays.asList(ORB_ARRAY_CW);
		}
	}

	public static int[][] colorChangedBoard(int[][] board,
			List<Integer> changeList) {
		return colorChangedBoard(board,
				changeList.toArray(new Integer[changeList.size()]));
	}

	public static boolean isSameBoard(int[][] board, int[][] stack) {
		for (int i = 0; i < PadBoardAI.ROWS; ++i) {
			for (int j = 0; j < PadBoardAI.COLS; ++j) {
				if (board[i][j] != stack[i][j]) {
					return false;
				}
			}
		}
		return true;
	}

	public static int[][] randomChangedBoard(int[][] board, Set<Integer> from, List<Integer> to) {
		int[][] newBoard = PadBoardAI.create_empty_board();
		int targetSize = to.size();
		for (int i = 0; i < ROWS; ++i) {
			for (int j = 0; j < COLS; ++j) {
				int orb = board[i][j]%10;
				if(from.contains(orb)) {
					int toOrb = (int)to.get(RandomUtil.getInt(targetSize));
					if(board[i][j]>=10 && toOrb <= 5) {
						toOrb += 10;
					}
					newBoard[i][j] = toOrb;
				} else {
					newBoard[i][j] = board[i][j];
				}
			}
		}
		return newBoard;
	}
	
	public static int[][] colorChangedBoard(int[][] board, Integer[] changeList) {
		if ((changeList.length % 2) != 0) {
			throw new RuntimeException("transform list should be multiple of 2");
		}
		int[][] newBoard = PadBoardAI.create_empty_board();
		SparseIntArray transformMap = new SparseIntArray();
		for (int i = 0; i < changeList.length; i += 2) {
			transformMap.put(changeList[i], changeList[i + 1]);
			LogUtil.d("change a to b = ", changeList[i] , "=>", changeList[i+1]);
		}
		for (int i = 0; i < ROWS; ++i) {
			for (int j = 0; j < COLS; ++j) {
				int orb = board[i][j];
				if (transformMap.get(orb, -1) != -1) {
					newBoard[i][j] = transformMap.get(orb);
				} else {
					newBoard[i][j] = orb;
				}
			}
		}
		return newBoard;
	}

	public static int[][] generateBoard(long type) {
		return generateBoard(type, false);
	}

	public static int[][] generateBoardWith3Up(List<Integer> data) {
		int[][] newBoard = create_empty_board();
		for (Integer orb : data) {
			int count = 0;
			while (count < 3) {
				int x = (int) (Math.random() * ROWS);
				int y = (int) (Math.random() * COLS);
				if (newBoard[x][y] == UNDEFINED) {
					newBoard[x][y] = orb;
					++count;
				}
			}
		}
		int size = data.size();
		for (int i = 0; i < ROWS; ++i) {
			for (int j = 0; j < COLS; ++j) {
				if (newBoard[i][j] == UNDEFINED) {
					int rnd = (int) (Math.random() * size);
					newBoard[i][j] = data.get(rnd);
				}
			}
		}
		return newBoard;
	}

	public static int[][] generateBoard(List<Integer> data) {
		long type = 0;
		for (Integer d : data) {
			type |= (1 << (d << 2));
		}
		return generateBoard(type, true);
	}

	static int prevTypes = 0;
	static int[] counter = {0,0,0,0,0,0};
	public static int[][] generateBoard(long type, boolean can3) {
		boolean[] orbs = new boolean[6];
		orbs[0] = (type & TYPE_DARK) != 0;
		orbs[1] = (type & TYPE_FIRE) != 0;
		orbs[2] = (type & TYPE_HEART) != 0;
		orbs[3] = (type & TYPE_LIGHT) != 0;
		orbs[4] = (type & TYPE_WATER) != 0;
		orbs[5] = (type & TYPE_WOOD) != 0;
		int types = 0;
		for (int i = 0; i < 6; ++i) {
			if (orbs[i]) {
				++types;
			}
		}
		
		if ( prevTypes != types ) {
		    PRIME = 22 * types;
		    prevTypes = types;
		}
		
		int[] mapping = new int[types];
		// put heart to last item
		int count = 0;
		for (int i = 0; i < 6; ++i) {
			if (orbs[i]) {
				mapping[count++] = i;
			}
		}
		int[][] board = create_empty_board();
		for (int i = 0; i < 6; ++i) {
			if (orbs[i] == false) {
				continue;
			}
			int count3 = 0;
			while (count3 < 3) {
				int x = (int) (Math.random() * ROWS);
				int y = (int) (Math.random() * COLS);
				if (board[x][y] == UNDEFINED) {
					board[x][y] = i;
					++count3;
				}
			}
		}

		for (int i = 0; i < ROWS; ++i) {
			for (int j = 0; j < COLS; ++j) {
				if (board[i][j] == UNDEFINED) {
					board[i][j] = mapping[getNewOrb(types)];
				}
			}
		}
		if (!can3 && type >= 3) {
			boolean matched;
			do {
				MatchBoard matchBoard = PadBoardAI.findMatches(board, null);
				if (matchBoard.matches.size() > 0) {
					matched = true;
					for (Match match : matchBoard.matches) {
						int size = match.list.size();
						int rand = (int) (Math.random() * size);
						RowCol rc = match.list.get(rand);
						int newOrbId = getNewOrb(types);
						while (mapping[newOrbId] == match.type) {
							newOrbId = getNewOrb(types);
						}
						board[rc.row][rc.col] = mapping[newOrbId];
					}
				} else {
					matched = false;
				}
			} while (matched);
			
			calcDropRate(board);
			
			return board;
		}
		return board;
	}
	
	private static final boolean TEST = false;
	private static void calcDropRate(int[][] board) {
	    if ( !TEST ) {
	        return ;
	    }
        for(int i=0; i<ROWS; ++i) {
            for(int j=0; j<COLS; ++j) {
                ++counter[board[i][j]];
            }
        }
        int total = 0;
        for(int i=0; i<6; ++i) {
            total += counter[i];
        }
	}
	

	private int[][] get_board() {
		// TODO: init board
		System.out.println("init board=");
		int[][] board = create_empty_board();
		return board;
	}

	public void set_board(int i, int j, int value) {
		if (i >= 0 && i < ROWS && j >= 0 && j < COLS) {
			global_board[i][j] = value;
		}
	}

	private static int PRIME = 132; // dont use PRIME NUMBER, just use a BLOCK * TYPES number
	private static final int BLOCK = PRIME / TYPES;

	public static int getNewOrb() {
		int rnd = (int) (Math.random() * PRIME);
		return (int)(Math.floor(rnd / BLOCK));
	}

	public static int getOrbType(long type) {
		if (type == TYPE_DARK) {
			return 0;
		} else if (type == TYPE_FIRE) {
			return 1;
		} else if (type == TYPE_HEART) {
			return 2;
		} else if (type == TYPE_LIGHT) {
			return 3;
		} else if (type == TYPE_WATER) {
			return 4;
		} else if (type == TYPE_WOOD) {
			return 5;
		}
		return 0;
	}

	public static boolean[] getSelectedOrbs(long type) {
		boolean[] orbs = new boolean[6];
		orbs[0] = (type & TYPE_DARK) != 0;
		orbs[1] = (type & TYPE_FIRE) != 0;
		orbs[2] = (type & TYPE_HEART) != 0;
		orbs[3] = (type & TYPE_LIGHT) != 0;
		orbs[4] = (type & TYPE_WATER) != 0;
		orbs[5] = (type & TYPE_WOOD) != 0;
		return orbs;
	}

	public static int getNewOrbRestrictChances(long type, List<Pair<Integer, Integer>> percent) {
	    if ( percent == null || percent.size() == 0 ) {
	        return getNewOrbRestrict(type);
	    }

	    final int MAX = 9;
        boolean[] orbs = new boolean[MAX];
        orbs[0] = (type & TYPE_DARK) != 0;
        orbs[1] = (type & TYPE_FIRE) != 0;
        orbs[2] = (type & TYPE_HEART) != 0;
        orbs[3] = (type & TYPE_LIGHT) != 0;
        orbs[4] = (type & TYPE_WATER) != 0;
        orbs[5] = (type & TYPE_WOOD) != 0;
        orbs[6] = (type & TYPE_POISON) != 0;
        orbs[7] = (type & TYPE_JAMAR) != 0;
        orbs[8] = (type & TYPE_E_POISON) != 0;
        int types = 0;
        for (int i = 0; i < MAX; ++i) {
            if (orbs[i]) {
                ++types;
            }
        }
        double total = 0.0;
        double allPercents[] = new double[MAX];
        for(int i=0; i<MAX; ++i) {
            allPercents[i] = 0.0;
        }
        for(Pair<Integer, Integer> pair : percent) {
            allPercents[pair.first] = pair.second;
            total += pair.second;
        }

        double avg = (100.0-total) / types;
        for(int i=0; i<MAX; ++i) {
            if (orbs[i]) {
                allPercents[i] += avg;
            } else if (allPercents[i]>0) {
            	++types;
            	orbs[i] = true;
            }
        }

        if ( prevTypes != types ) {
            PRIME = 22 * types;
            prevTypes = types;
        }
        
        double onepiece = PRIME / 100.0;
        int[] blocks = new int[MAX];
        for(int i=0; i<MAX; ++i) {
            blocks[i] = 0;
            if ( orbs[i] ) {
                blocks[i] = (int)(onepiece*allPercents[i]);
            }
        }

        int[] mappingBlocks = new int[types];  
        int[] mapping = new int[types];
        int count = 0;
        for (int i = 0; i < MAX; ++i) {
            if (orbs[i]) {
                mapping[count] = i;
                mappingBlocks[count] = blocks[i];
                if ( count > 0 ) {
                    mappingBlocks[count] += mappingBlocks[count-1];
                }
                ++count;
            }
        }
        
        int rnd = (int)(Math.random() * PRIME);
        int index=0;
        while(index<types) {
            if ( rnd < mappingBlocks[index] ) {
                return mapping[index];
            }
            ++index;
        }
        return mapping[(int)(Math.random()*types)];
	}
	
	public static int getNewOrbRestrict(long type) {
		boolean[] orbs = new boolean[6];
		orbs[0] = (type & TYPE_DARK) != 0;
		orbs[1] = (type & TYPE_FIRE) != 0;
		orbs[2] = (type & TYPE_HEART) != 0;
		orbs[3] = (type & TYPE_LIGHT) != 0;
		orbs[4] = (type & TYPE_WATER) != 0;
		orbs[5] = (type & TYPE_WOOD) != 0;
		int types = 0;
		for (int i = 0; i < 6; ++i) {
			if (orbs[i]) {
				++types;
			}
		}
		
		if ( prevTypes != types ) {
		    PRIME = 22 * types;
		    prevTypes = types;
		}
		int[] mapping = new int[types];
		int count = 0;
		for (int i = 0; i < 6; ++i) {
			if (orbs[i]) {
				mapping[count++] = i;
			}
		}
		return mapping[getNewOrb(types)];
	}

	private static int getNewOrb(int types) {
		int block = PRIME / types;
		int rnd = (int)(Math.random() * PRIME);
		int orb = (int)Math.floor(rnd / block);
		return orb;
	}

	public static int[][] getRandomBoard() {
		int[][] board = new int[ROWS][COLS];
		for (int i = 0; i < ROWS; ++i) {
			for (int j = 0; j < COLS; ++j) {
				board[i][j] = (int)(Math.random()*6);
			}
		}
		boolean matched = false;
		do {
			MatchBoard matchBoard = PadBoardAI.findMatches(board, null);
			if (matchBoard.matches.size() > 0) {
				matched = true;
				for (Match match : matchBoard.matches) {
					int size = match.list.size();
					int rand = (int) (Math.random() * size);
					RowCol rc = match.list.get(rand);
					int newOrb = (int)(Math.random()*6);
					if (newOrb == match.type) {
						newOrb = (newOrb + 1) % TYPES;
					}
					board[rc.row][rc.col] = newOrb;
				}
			} else {
				matched = false;
			}
		} while (matched);
		
		calcDropRate(board);
		return board;
	}

	public static int[][] copy_board(int[][] board) {
		int[][] newBoard = create_empty_board();
		for (int i = 0; i < ROWS; ++i) {
			for (int j = 0; j < COLS; ++j) {
				newBoard[i][j] = board[i][j];
			}
		}
		return newBoard;
	}

	public static void copy_board(int[][] from, int[][] to) {
		for (int i = 0; i < ROWS; ++i) {
			for (int j = 0; j < COLS; ++j) {
				to[i][j] = from[i][j];
			}
		}
	}

	public static final int UNDEFINED = -1;
	public static final int X_ORBS = 999;

	public static MatchBoard findMatches(final int[][] board) {
		return findMatches(board, 3, null);
	}

	public static MatchBoard findMatches(final int[][] board, List<Integer> cantRemove) {
		return findMatches(board, 3, cantRemove);
	}
	
	public static MatchBoard findMatches(final int[][] board, int removeN, List<Integer> cantRemove) {
		int[][] match_board = create_empty_board();
		// find all 3+ consecutives

		Set<Integer> cantRemoveSet;
		if (cantRemove == null) {
			cantRemoveSet = new HashSet<Integer>();
		} else {
			cantRemoveSet = new HashSet<Integer>(cantRemove);
		}

		// verticals
		int bound = 2;
		int[] prev = new int[bound];
		for (int j = 0; j < COLS; ++j) {
			for(int i=0; i<bound; ++i) {
				prev[i] = X_ORBS;
			}
			for (int i = 0; i < ROWS; ++i) {
				int cur_orb = board[i][j] % 10;
				if(board[i][j] != X_ORBS) {
					boolean same = true;
					for(int x=0; x<bound; ++x) {
						if(prev[x] != cur_orb) {
							same = false;
							break;
						}
					}
					if(same) {
						for(int x=0; x<bound+1; ++x) {
							match_board[i-x][j] = cur_orb;
						}
					}
					for(int x=0; x<bound-1; ++x) {
						prev[x] = prev[x+1];
					}
					prev[bound-1] = cur_orb;
				} else {
					for(int x=0; x<bound-1; ++x) {
						prev[x] = prev[x+1];
					}
					prev[bound-1] = board[i][j];
					match_board[i][j] = UNDEFINED;
				}

			}
		}
		for (int i = 0; i < ROWS; ++i) {
			for(int x=0; x<bound; ++x) {
				prev[x] = X_ORBS;
			}
			for (int j = 0;j < COLS; ++j) {
				int cur_orb = board[i][j] % 10;
				if(board[i][j] != X_ORBS) {
					boolean same = true;
					for(int x=0; x<bound; ++x) {
						if(prev[x] != cur_orb) {
							same = false;
							break;
						}
					}
					if(same) {
						for(int x=0; x<bound+1; ++x) {
							match_board[i][j-x] = cur_orb;
						}
					}
					for(int x=0; x<bound-1; ++x) {
						prev[x] = prev[x+1];
					}
					prev[bound-1] = cur_orb;
				} else {
					for(int x=0; x<bound-1; ++x) {
						prev[x] = prev[x+1];
					}
					prev[bound-1] = board[i][j];
				}

			}
		}
		
		ArrayList<Match> matches = new ArrayList<Match>();
		int[][] scratch_board = copy_board(match_board);
		for (int j = 0; j < COLS; ++j) {
			for (int i = 0; i < ROWS; ++i) {
				int current = scratch_board[i][j];
				if (current == UNDEFINED) {
					continue;
				}
				Stack<RowCol> stack = new Stack<RowCol>();
				stack.push(RowCol.make(i, j));
				int count = 0;
				int pcount = 0;
				ArrayList<RowCol> matchList = new ArrayList<RowCol>();
				while (!stack.isEmpty()) {
					RowCol n = stack.pop();
					int orb = scratch_board[n.row][n.col];
					if (orb != current) {
						continue;
					}
					++count;
					int original = board[n.row][n.col];
					if (original >= 10 && original != X_ORBS) {
						++pcount;
					}
					scratch_board[n.row][n.col] = UNDEFINED;
					matchList.add(n);
					if (n.row > 0) {
						stack.push(RowCol.make(n.row - 1, n.col));
					}
					if (n.row < ROWS - 1) {
						stack.push(RowCol.make(n.row + 1, n.col));
					}
					if (n.col > 0) {
						stack.push(RowCol.make(n.row, n.col - 1));
					}
					if (n.col < COLS - 1) {
						stack.push(RowCol.make(n.row, n.col + 1));
					}
				}
				if(matchList.size()>=removeN && !cantRemoveSet.contains(current)) {
					matches.add(Match.make(current, count, pcount, matchList));
				} else {
					for(RowCol rc : matchList) {
						match_board[rc.row][rc.col] = UNDEFINED;
					}
				}
			}
		}
		return new MatchBoard(match_board, matches);
	}

	public static int[][] in_place_remove_matches(int[][] board,
			int[][] match_board) {
		for (int i = 0; i < ROWS; ++i) {
			for (int j = 0; j < COLS; ++j) {
				if (match_board[i][j] != UNDEFINED) {
					board[i][j] = X_ORBS;
				}
			}
		}
		return board;
	}

	public void init() {
		global_board = get_board();
	}

	private int[][] global_board;


	public static long getType(boolean[] drops) {
		long type = 0;
		final long[] types = { TYPE_DARK, TYPE_FIRE, TYPE_HEART, TYPE_LIGHT,
				TYPE_WATER, TYPE_WOOD };
		for (int i = 0; i < 6; ++i) {
			if (drops[i]) {
				type |= types[i];
			}
		}

		return type;
	}

    public static int[][] generateBoardWithPlus(long drop, boolean can3,
            int[] plusOrbCount) {
        int[] percent = new int[6];
        for(int i=0; i<6; ++i) {
            percent[i] = plusOrbCount[i]*20;
        }
        int[][] board = generateBoard(drop, can3);
        for(int i=0; i<ROWS; ++i) {
            for(int j=0; j<COLS; ++j) {
                int orb = board[i][j];
                if ( orb < 6 ) { // normal orb
                    if ( RandomUtil.getLuck(percent[orb]) ) {
                        board[i][j] = orb+10;
                    }
                }
            }
        }
        return board;
    }
}
