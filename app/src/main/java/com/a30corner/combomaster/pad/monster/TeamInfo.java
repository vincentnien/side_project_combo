package com.a30corner.combomaster.pad.monster;

import java.io.IOException;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.widget.Toast;

import com.a30corner.combomaster.BuildConfig;
import com.a30corner.combomaster.pad.DamageCalculator;
import com.a30corner.combomaster.provider.LocalDBHelper;
import com.a30corner.combomaster.provider.vo.MonsterDO;
import com.a30corner.combomaster.utils.Constants;
import com.a30corner.combomaster.utils.LogUtil;

public class TeamInfo {

	private Context mContext;
	private int no;
	private MonsterInfo[] mInfo = new MonsterInfo[6];
	private int[] mInfoIndex = new int[6];

	private int mTotalHp;
	private int mRecovery;
	
	public TeamInfo(Context context, int no) {
		mContext = context;
		this.no = no;
	}

	public void switchMember(int index) {
		MonsterInfo member = mInfo[index];
		mInfo[index] = mInfo[0];
		mInfo[0] = member;
	}

	public boolean is7x6LeaderOnly() {
		if(mInfo[0] != null) {
			for(LeaderSkill ls : mInfo[0].getLeaderSkill()) {
				if(ls.getType() == LeaderSkill.LeaderSkillType.LST_BOARD) {
					return true;
				}
			}
		}
		return false;
	}
	public boolean is7x6() {
		if(mInfo[0] != null) {
			for(LeaderSkill ls : mInfo[0].getLeaderSkill()) {
				if(ls.getType() == LeaderSkill.LeaderSkillType.LST_BOARD) {
					return true;
				}
			}
		}
		if(mInfo[5] != null) {
			for(LeaderSkill ls : mInfo[5].getLeaderSkill()) {
				if(ls.getType() == LeaderSkill.LeaderSkillType.LST_BOARD) {
					return true;
				}
			}
		}
		return false;
	}
	
	public void setFriend(MonsterInfo info) {
		mInfo[5] = info;
	}
	
	public int getNo() {
		return no;
	}

	private String getMemberKey(int teamId, int index) {
		return "Team" + teamId + "_" + index;
	}

	private String getTeamNameKey(int teamId) {
		return "Team" + teamId + "_name";
	}

	public MonsterInfo getMember(int index) {
		if (index >= 0 && index < 6) {
			return mInfo[index];
		}
		return null;
	}
	
	public int getMemberIndex(int index) {
		if ( index >= 0 && index < 6 ) {
			return mInfoIndex[index];
		}
		return -1;
	}

	public boolean containAny(int boxIndex) {
		SharedPreferences prefs = mContext.getSharedPreferences("team",
				Context.MODE_PRIVATE);
		for (int i = 0; i < 6; ++i) {
			int index = prefs.getInt(getMemberKey(no, i), -1);
			// db record is from 0, but the prefs is from 1... orz bad design
			if ((index - 1) == boxIndex) {
				return true;
			}
		}
		return false;
	}

	public void henshin(int i, int index, boolean[] bind) {
		mInfo[i].henshin(mContext, index);
		refresh(bind);
	}

	public void load() {
		SharedPreferences prefs = mContext.getSharedPreferences("team",
				Context.MODE_PRIVATE);

		for (int i = 0; i < 6; ++i) {
			int index = prefs.getInt(getMemberKey(no, i), -1);
			mInfo[i] = null;
			mInfoIndex[i] = -1;
			if (index != -1) {
				mInfoIndex[i] = index-1;
				MonsterDO mdo = LocalDBHelper
						.getMonsterBox(mContext, index - 1);
				
				try {
					if (mdo != null) {
						MonsterVO vo1 = LocalDBHelper.getMonsterData(mContext, mdo.no);
						int cd = 0;
						if(vo1!=null){
							cd = vo1.slv1;
						}

						int cd1 = (mdo.skill1_cd<=0 || mdo.skill1_cd>=99)? cd:mdo.skill1_cd;
						int cd2 = -1;
						if(mdo.active2>0) {
							MonsterVO vo = LocalDBHelper.getMonsterData(mContext, mdo.active2);
							cd2 = (mdo.skill2_cd<=0 || mdo.skill2_cd>=99)? cd:mdo.skill2_cd;

							int hpa, atka, rcva;
							if(vo1 != null && vo != null && vo1.getMonsterProps().first.equals(vo.getMonsterProps().first)) {
								hpa = (int)((vo.getHp(99)+990) * Constants.HP_ADDITION);
								atka = (int)((vo.getAtk(99)+495)* Constants.ATK_ADDITION);
								rcva = (int)((vo.getRecovery(99)+297)* Constants.RCV_ADDITION);
							} else {
								hpa = atka = rcva = 0;
							}
							mInfo[i] = MonsterInfo.load(mContext, mdo.no, mdo.lv,
									mdo.egg1, mdo.egg2, mdo.egg3, mdo.awoken, mdo.potentialList,
									cd1, cd2, hpa, atka, rcva, vo, MonsterSkill.AwokenSkill.get(mdo.super_awoken));
						} else {
							mInfo[i] = MonsterInfo.load(mContext, mdo.no, mdo.lv,
									mdo.egg1, mdo.egg2, mdo.egg3, mdo.awoken, mdo.potentialList,
									cd1, 0, 0, 0, 0, null, MonsterSkill.AwokenSkill.get(mdo.super_awoken));
						}
					}
				} catch (IOException e) {
					Toast.makeText(mContext,
							"loading monster data fail @ " + no,
							Toast.LENGTH_SHORT).show();
				}
			}
		}
	}
	
	public int gameMode = 0;
	private boolean isCopMode = false;
	
	public boolean isCopMode() {
		return isCopMode;
	}
	
	public void refresh(boolean[] bind) {
		mTotalHp = 0;
		mRecovery = 0;
		MonsterInfo leader = mInfo[0];
		MonsterInfo friend = mInfo[5];

		for (int i = 0; i < 6; ++i) {
			MonsterInfo info = mInfo[i];
			if (info == null) {
				continue;
			}

			int enhancedHp = 0;
			int newHp = 0;
			int enhancedRcv = 0;
			int newRcv = 0;
			
			if(!bind[0]) {
				newHp = DamageCalculator.getEnhancedPower(leader, info,
						info.getHP(), 0, false);
				newRcv = DamageCalculator.getEnhancedPower(leader, info,
						info.getRecovery(), 2, false);
			} else {
				newHp = info.getHP();
				newRcv = info.getRecovery();
			}
			if(!bind[5]) {
				enhancedHp = DamageCalculator.getEnhancedPower(friend, info, newHp,
						0, false);

				enhancedRcv = DamageCalculator.getEnhancedPower(friend, info,
						newRcv, 2, false);
			} else {
				enhancedHp = newHp;
				enhancedRcv = newRcv;
			}

			mTotalHp += enhancedHp;
			mRecovery += enhancedRcv;

			int teamHpUp = info.getTargetAwokenCount(MonsterSkill.AwokenSkill.TEAM_HP_UP, isCopMode);
			if(teamHpUp > 0) {
				mTotalHp = (int)(mTotalHp * (1.0f+teamHpUp*0.05f));
			}
			int teamRcvUp = info.getTargetAwokenCount(MonsterSkill.AwokenSkill.TEAM_RCV_UP, isCopMode);
			if(teamRcvUp > 0) {
				mRecovery = (int)(mRecovery * (1.0f+teamRcvUp*0.1f));
			}
		}
	}
	
	public void init(int mode, boolean[] powerUp, boolean copMode) {
		mTotalHp = 0;
		mRecovery = 0;
		MonsterInfo leader = mInfo[0];
		MonsterInfo friend = mInfo[5];
		
		gameMode = mode;
		boolean isChallengeMode = mode == Constants.MODE_CHALLENGE;
		isCopMode = copMode;

		for (int i = 0; i < 6; ++i) {
			MonsterInfo info = mInfo[i];
			if (info == null) {
				continue;
			}

			int enhancedHp = 0;
			int newHp = DamageCalculator.getEnhancedPower(leader, info,
					info.getHP(isCopMode), 0, isCopMode);
			if ( isChallengeMode ) {
				enhancedHp = newHp;
			} else {
				enhancedHp = DamageCalculator.getEnhancedPower(friend, info, newHp,
					0, isCopMode);
			}
			int enhancedRcv = 0;
			int newRcv = DamageCalculator.getEnhancedPower(leader, info,
					info.getRecovery(isCopMode), 2, isCopMode);
			if ( isChallengeMode ) {
				enhancedRcv = newRcv;
			} else {
				enhancedRcv = DamageCalculator.getEnhancedPower(friend, info,
					newRcv, 2, isCopMode);
			}
			float up = DamageCalculator.getPowerUp(powerUp, info);
			enhancedHp *= up;
			enhancedRcv *= up;

			mTotalHp += enhancedHp;
			mRecovery += enhancedRcv;

			int teamHpUp = info.getTargetAwokenCount(MonsterSkill.AwokenSkill.TEAM_HP_UP, isCopMode);
			if(teamHpUp > 0) {
				mTotalHp = (int)(mTotalHp * (1.0f+teamHpUp*0.05f));
			}
			int teamRcvUp = info.getTargetAwokenCount(MonsterSkill.AwokenSkill.TEAM_RCV_UP, isCopMode);
			if(teamRcvUp > 0) {
				mRecovery = (int)(mRecovery * (1.0f+teamRcvUp*0.1f));
			}
		}
	}

	public int getHp() {
//		if (BuildConfig.DEBUG) {
//			return 5000000;
//		}
		return mTotalHp;
	}

	public int getRecovery(boolean[] powerUp) {
		boolean isMultiMode = isCopMode;
		float addition = 0;
		for(int i=0; i<6; ++i) {
			MonsterInfo info = mInfo[i];
			if (info != null) {
				float up = DamageCalculator.getPowerUp(powerUp, info);
				addition += info.getRecovery(isMultiMode) * (up - 1f);
			}
		}
		
		return mRecovery + (int)addition;
	}
	
	public int getRecovery() {
		return mRecovery;
	}
	
}
