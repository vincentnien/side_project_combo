package com.a30corner.combomaster.activity.ui;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Pair;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;

import com.a30corner.combomaster.ComboMasterApplication;
import com.a30corner.combomaster.R;
import com.a30corner.combomaster.activity.fragment.TeamFragment;
import com.a30corner.combomaster.activity.ui.FilterDialog.IFilterCallback;
import com.a30corner.combomaster.activity.ui.MultiSpinner.MultiSpinnerListener;
import com.a30corner.combomaster.activity.ui.SortDialog.ISortCallback;
import com.a30corner.combomaster.pad.DamageCalculator;
import com.a30corner.combomaster.pad.Match;
import com.a30corner.combomaster.pad.PadBoardAI;
import com.a30corner.combomaster.pad.PadBoardAI.IMAGE_TYPE;
import com.a30corner.combomaster.pad.monster.ActiveSkill;
import com.a30corner.combomaster.pad.monster.LeaderSkill;
import com.a30corner.combomaster.pad.monster.MonsterInfo;
import com.a30corner.combomaster.pad.monster.MonsterSkill;
import com.a30corner.combomaster.pad.monster.MonsterSkill.AwokenSkill;
import com.a30corner.combomaster.pad.monster.MonsterSkill.MoneyAwokenSkill;
import com.a30corner.combomaster.pad.monster.TeamInfo;
import com.a30corner.combomaster.utils.Constants;
import com.a30corner.combomaster.utils.LogUtil;
import com.a30corner.combomaster.utils.SharedPreferenceUtil;
import com.a30corner.combomaster.utils.TinyUrlUtil;

public class DialogUtil {

	public interface ITeamSelectCallback {
		public void onSelect(int index, Map<String, Object> data);

		public void onCancel();
	}
	
	public interface ITeam2SelectCallback {
		public void onSelect(int index, int index2, Map<String, Object> data);

		public void onCancel();
	}
	
	public interface IPotentialCallback {
		public void onResult(MoneyAwokenSkill[] list);
		
		public void onCancel();
	}


	private static class MultipleTeamData {
		int no;
		List<MonsterData> dataList = new ArrayList<DialogUtil.MonsterData>(6);
		Context context;

		public MultipleTeamData(Context ctx) {
			context = ctx;
		}

		void addMember(MonsterData data) {
			dataList.add(data);
		}

		void setTeamNo(int no) {
			this.no = no;
		}

		int getNo() {
			return no;
		}

		void initMemberData() {
			ComboMasterApplication instance = ComboMasterApplication
					.getsInstance();
			TeamInfo team = ComboMasterApplication.getsInstance().getTeam(no);
			for (int i = 0; i < 5; ++i) {
				MonsterInfo info = team.getMember(i);
				MonsterData data = dataList.get(i);
				boolean set = false;
				if (info != null) {
					int no = info.getNo();
					Bitmap cache = instance.getBitmap(no);
					if (cache == null) {
						File file = new File(context.getFilesDir(), no
								+ "i.png");
						if (file.exists()) {
							Bitmap bitmap = BitmapFactory.decodeFile(file
									.getAbsolutePath());
							data.image.setImageBitmap(bitmap);
							set = true;
						}
					} else {
						set = true;
						data.image.setImageBitmap(cache);
					}
				}
				if (!set) {
					data.image.setImageResource(R.drawable.i000);
				}

			}
		}
	}
	
	private static class TeamData {
		int no;
		List<MonsterData> dataList = new ArrayList<DialogUtil.MonsterData>(6);
		Context context;
		TextView leaderSkill;
		TextView friendLSkill;

		public TeamData(Context ctx) {
			context = ctx;
		}

		void addMember(MonsterData data) {
			dataList.add(data);
		}

		void setTeamNo(int no) {
			this.no = no;
		}

		int getNo() {
			return no;
		}

		void initViews(Dialog parent) {
			leaderSkill = (TextView) parent.findViewById(R.id.leader_skill);
			friendLSkill = (TextView) parent
					.findViewById(R.id.friend_leader_skill);
		}

		void initMemberData() {
			// final int[] awsRes = {R.drawable.aws_01,R.drawable.aws_02,
			// R.drawable.aws_03,R.drawable.aws_04,R.drawable.aws_05,
			// R.drawable.aws_06,R.drawable.aws_07,R.drawable.aws_08,
			// R.drawable.aws_max};
			ComboMasterApplication instance = ComboMasterApplication
					.getsInstance();
			TeamInfo team = ComboMasterApplication.getsInstance().getTeam(no);
			for (int i = 0; i < 6; ++i) {
				MonsterInfo info = team.getMember(i);
				MonsterData data = dataList.get(i);
				boolean set = false;
				if (info != null) {
					int no = info.getNo();
					Bitmap cache = instance.getBitmap(no);
					if (cache == null) {
						File file = new File(context.getFilesDir(), no
								+ "i.png");
						if (file.exists()) {
							Bitmap bitmap = BitmapFactory.decodeFile(file
									.getAbsolutePath());
							data.image.setImageBitmap(bitmap);
							set = true;
						}
					} else {
						set = true;
						data.image.setImageBitmap(cache);
					}
				}
				if (!set) {
					data.image.setImageResource(R.drawable.i000);
				}

			}
			MonsterInfo leader = team.getMember(0);
			MonsterInfo friend = team.getMember(5);
			if (leader != null) {
				leaderSkill.setText(MonsterSkill.getLeaderSkillString(context,
						leader.getLeaderSkill()));
			} else {
				leaderSkill.setText("");
			}
			if (friend != null) {
				friendLSkill.setText(MonsterSkill.getLeaderSkillString(context,
						friend.getLeaderSkill()));
			} else {
				friendLSkill.setText("");
			}
		}
	}

	private static class MonsterData {
		ImageView image;

		// TextView lv;
		// ImageView aws;
		// TextView p297;

		public MonsterData(View view) {
			findViews(view);
		}

		static MonsterData load(View view) {
			return new MonsterData(view);
		}

		void findViews(View view) {
			image = (ImageView) view;//.findViewById(R.id.monster);
			// lv = (TextView) view.findViewById(R.id.monsterlv);
			// aws = (ImageView) view.findViewById(R.id.aws_count);
			// p297 = (TextView) view.findViewById(R.id.plus297);
		}
	}

	public static SetLvDialog getSetLvDialog(Context context) {
	    SetLvDialog dialog = new SetLvDialog(context);
	    dialog.setContentView(R.layout.dialog_set_lv);
	    return dialog;
	}
	
	public static FilterDialog getFilterDialog(final Context context, final IFilterCallback callback) {
		final FilterDialog dialog = new FilterDialog(context);
		dialog.setCallback(callback);
		dialog.setTitle(R.string.str_filter);
		dialog.setContentView(R.layout.dialog_filter);
		
		return dialog;
	}
	
	public static SortDialog getSortDialog(Context context, String pref, ISortCallback callback) {
		SortDialog dialog = new SortDialog(context);
	    dialog.setTitle(R.string.sort);
	    dialog.setCallback(callback);
	    dialog.setPreferenceFileName(pref);
	    dialog.setContentView(R.layout.dialog_sort);
		return dialog;
	}
	
	public static SortDialog getSortDialog(Context context, ISortCallback callback) {
	    SortDialog dialog = new SortDialog(context);
	    dialog.setTitle(R.string.sort);
	    dialog.setCallback(callback);
	    dialog.setContentView(R.layout.dialog_sort);
	    return dialog;
	}
	
	@Deprecated
	public static Dialog getTinyUrlDialog(final Activity activity,
			final int[][] board, final TeamInfo info, final ActiveSkill skill,
			final Map<String, Object> data) {
		final Dialog dialog = new Dialog(activity);
		dialog.setTitle(R.string.app_name);
		dialog.setContentView(R.layout.dialog_tinyurl);

		final View layoutLoading = dialog.findViewById(R.id.layout_loading);
		final View layoutUrl = dialog.findViewById(R.id.layout_url);
		final EditText etUrl = (EditText) dialog.findViewById(R.id.et_url);
		final Button btnCancel = (Button) dialog.findViewById(R.id.btn_cancel);

		new Thread(new Runnable() {

			@Override
			public void run() {
				String url = "http://combomaster.twgogo.org/cm/calc.php?";
				try {
					TinyUrlUtil.getTinyUrl(
							url + getQuery(board, info, skill, data),
							new Callback<Response>() {
								@Override
								public void success(Response result,
										Response response) {

									// Try to get response body
									BufferedReader reader = null;
									StringBuilder sb = new StringBuilder();
									try {
										reader = new BufferedReader(
												new InputStreamReader(result
														.getBody().in()));
										String line;
										try {
											while ((line = reader.readLine()) != null) {
												sb.append(line);
											}
										} catch (IOException e) {
											e.printStackTrace();
										}
									} catch (IOException e) {
										e.printStackTrace();
									}

									displayTinyUrl(activity, layoutLoading,
											layoutUrl, btnCancel, etUrl,
											sb.toString());
								}

								@Override
								public void failure(RetrofitError error) {
									displayTinyUrl(activity, layoutLoading,
											layoutUrl, btnCancel, etUrl,
											error.toString());
								}
							});
				} catch (Exception e) {
					displayTinyUrl(activity, layoutLoading, layoutUrl,
							btnCancel, etUrl, e.toString());
				}
			}
		}).start();
		btnCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		return dialog;
	}

	private static String getQuery(int[][] board, TeamInfo info,
			ActiveSkill skill, Map<String, Object> data) {
		StringBuilder sb = new StringBuilder("board=");
		for (int i = PadBoardAI.COLS - 1; i >= 0; --i) {
			for (int j = 0; j < PadBoardAI.ROWS; ++j) {
				int orb = board[j][i];
				if (orb >= 10 && orb < 20) {
					int ch = 'A' + (orb - 10);
					sb.append(Character.toString((char) ch));
				} else if (orb >= 0 && orb < 10) {
					int ch = '0' + orb;
					sb.append(Character.toString((char) ch));
				} else {
					sb.append("1");
				}
			}
		}
		sb.append(getMonsterQueryData(info, skill, data));
		String result = sb.toString();
		return result;
	}

	private static String getMonsterQueryData(TeamInfo info, ActiveSkill skill,
			Map<String, Object> data) {
		if (info != null) {
			boolean challengeMode = false;
			boolean nullAwoken = false;
			// int swapIndex = 0;
			List<Boolean> bindStatus = null;
			StringBuilder sb = new StringBuilder();

			if (data != null) {
				if (data.containsKey("challengeMode")) {
					challengeMode = (Boolean) data.get("challengeMode");
				}
				// if (data.containsKey("swapIndex")) {
				// swapIndex = (Integer)data.get("swapIndex");
				// }
				if (data.containsKey("bindStatus")) {
					bindStatus = (List<Boolean>) data.get("bindStatus");
				}
				if (data.containsKey("nullAwoken")) {
					nullAwoken = (Boolean) data.get("nullAwoken");
				}
			}
			if (bindStatus == null) {
				bindStatus = new ArrayList<Boolean>();
				for (int i = 0; i < 6; ++i) {
					bindStatus.add(false);
				}
			}

			MonsterInfo leader = info.getMember(0);
			MonsterInfo friend = info.getMember(5);
			for (int i = 0; i < 6; ++i) {
				MonsterInfo minfo = info.getMember(i);
				if (minfo != null) {
					Pair<Integer, Integer> props = minfo.getProps();
					int twoway = (nullAwoken) ? 0
							: minfo.getTargetAwokenCount(AwokenSkill.TWO_PRONGED_ATTACK, false);
					int lm = 0;
					int fm = 0;
					int am = 0;
					
					if ( leader != null ) {
						List<LeaderSkill> list = leader.getLeaderSkill();
						double multiplier = 1.0;
						for(LeaderSkill sk : list) {
							double m = DamageCalculator.getLeaderFactor(info, sk, minfo, data);
							multiplier *= m;
						}
						if ( multiplier > 1.0 ) {
							lm = 1;
						}
					}
					if ( friend != null ) {
						List<LeaderSkill> list = friend.getLeaderSkill();
						double multiplier = 1.0;
						for(LeaderSkill sk : list) {
							double m = DamageCalculator.getLeaderFactor(info, sk, minfo, data);
							multiplier *= m;
						}
						if ( multiplier > 1.0 ) {
							fm = 1;
						}
					}
					if ( skill != null ) {
						double amv = DamageCalculator.getActiveSkillMultiplier(skill, minfo);
						am = (amv>1.0)? 1:0;
					}
					
					sb.append("&i0" + i).append("=").append(minfo.getNo())
							.append(",").append(minfo.getAtk()).append(",")
							.append(minfo.getRecovery()).append(",")
							.append(twoway).append(",")
							.append(lm).append(",")
							.append(fm).append(",")
							.append(am).append(",")
							.append(props.first + "@" + props.second);
				}
			}

			if (!nullAwoken) {
				int[] rowSkill = { 0, 0, 0, 0, 0, 0 };
				int[] plusOSkill = { 0, 0, 0, 0, 0, 0 };
				
				for (int i = 0; i < 6; ++i) {
					MonsterInfo minfo = info.getMember(i);
					if (minfo == null || bindStatus.get(i)) {
						continue;
					}
					rowSkill[0] += minfo
							.getTargetAwokenCount(AwokenSkill.ENHANCE_DARK_ATT, false);
					rowSkill[1] += minfo
							.getTargetAwokenCount(AwokenSkill.ENHANCE_FIRE_ATT, false);
					rowSkill[3] += minfo
							.getTargetAwokenCount(AwokenSkill.ENHANCE_LIGHT_ATT, false);
					rowSkill[4] += minfo
							.getTargetAwokenCount(AwokenSkill.ENHANCE_WATER_ATT, false);
					rowSkill[5] += minfo
							.getTargetAwokenCount(AwokenSkill.ENHANCE_WOOD_ATT, false);

					plusOSkill[0] += minfo
							.getTargetAwokenCount(AwokenSkill.ENHANCE_DARK, false);
					plusOSkill[1] += minfo
							.getTargetAwokenCount(AwokenSkill.ENHANCE_FIRE, false);
					plusOSkill[3] += minfo
							.getTargetAwokenCount(AwokenSkill.ENHANCE_LIGHT, false);
					plusOSkill[4] += minfo
							.getTargetAwokenCount(AwokenSkill.ENHANCE_WATER, false);
					plusOSkill[5] += minfo
							.getTargetAwokenCount(AwokenSkill.ENHANCE_WOOD, false);
				}
				
				List<Integer> rowArray = new ArrayList<Integer>();
				List<Integer> plusArray = new ArrayList<Integer>();
				for(int i=0; i<6; ++i) {
					rowArray.add(rowSkill[i]);
					plusArray.add(plusOSkill[i]);
				}
				
				 sb.append("&po=").append(TextUtils.join(",",plusArray));
				 sb.append("&eo=").append(TextUtils.join(",",rowArray));
			}

			double lf = DamageCalculator.getMaxLeaderMultiplier(info, leader,
					data);
			sb.append("&lf=").append(String.format("%.2f", lf));

			if (!challengeMode) {
				double ff = DamageCalculator.getMaxLeaderMultiplier(info,
						info.getMember(5), data);
				sb.append("&ff=").append(String.format("%.2f", ff));
			}

			if (skill != null) {
				double[] multipiler = DamageCalculator
						.getActiveSkillMultiplier(skill);
				sb.append("&af=").append(String.format("%.2f", multipiler[0]));
				for (int i = 0; i < 5; ++i) {
					if (multipiler[i + 1] != 1.0) {
						sb.append("&cf")
								.append(i)
								.append("=")
								.append(String
										.format("%.2f", multipiler[i + 1]));
					}
				}
			}

			Locale locale = Locale.getDefault();
			String language = ("zh".equals(locale.getLanguage())) ? "big5"
					: "en";
			sb.append("&lang=").append(language);

			return sb.toString();
		}
		return "";
	}

	private static void displayTinyUrl(Activity activity, final View loading,
			final View urlView, final Button cancel, final EditText et,
			final String data) {
		activity.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				loading.setVisibility(View.GONE);
				urlView.setVisibility(View.VISIBLE);
				cancel.setText(android.R.string.ok);
				et.setText(data);
			}
		});

	}
//	
//	private static ArrayAdapter<String> getSimpleAdapter(Context context, int arrId) {
//		ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, R.layout.simple_dropdown, android.R.id.text1);
//		String[] arr = context.getResources().getStringArray(arrId);
//		adapter.addAll(arr);
//		return adapter;
//	}

	public static Dialog getTeamSelectDialog(Context context,
			final ITeamSelectCallback callback) {
		return getTeamSelectDialog(context, callback, false);
	}
	
	public static Dialog getTeamSelectDialog(Context context,
			final ITeamSelectCallback callback, boolean simulatorMode) {

		int no = ComboMasterApplication.getsInstance().getTargetNo();

		final Dialog dialog = new Dialog(context);

		SharedPreferences pref = context.getSharedPreferences("team",
				Context.MODE_PRIVATE);
		final List<String> teamNameList = new ArrayList<String>();
		for (int i = 0; i < TeamFragment.TEAM_SIZE; ++i) {
			teamNameList.add(pref.getString("Team" + i + "_name", "Team "
					+ (i + 1)));
		}
		dialog.setTitle(teamNameList.get(no));
		dialog.setContentView(R.layout.team_select_dialog);

		TeamData teamData = new TeamData(context);
		teamData.initViews(dialog);
		teamData.setTeamNo(no);
		teamData.addMember(MonsterData.load(dialog.findViewById(R.id.leader1)));
		teamData.addMember(MonsterData.load(dialog.findViewById(R.id.member1)));
		teamData.addMember(MonsterData.load(dialog.findViewById(R.id.member2)));
		teamData.addMember(MonsterData.load(dialog.findViewById(R.id.member3)));
		teamData.addMember(MonsterData.load(dialog.findViewById(R.id.member4)));
		teamData.addMember(MonsterData.load(dialog.findViewById(R.id.leader2)));
		teamData.initMemberData();

		if(simulatorMode) {
			dialog.findViewById(R.id.calculator_mode_only).setVisibility(View.GONE);
		}
		
		final CheckBox nullAwoken = (CheckBox) dialog
				.findViewById(R.id.cb_null_awoken_mode);
		final CheckBox copMode = (CheckBox) dialog.findViewById(R.id.cb_cop_mode);
		final Spinner mode = (Spinner) dialog.findViewById(R.id.game_mode);
		final MultiSpinner spinner = (MultiSpinner) dialog.findViewById(R.id.sp_power_up);
		final String[] strList = context.getResources().getStringArray(R.array.stage_power_up);
		spinner.setItems(Arrays.asList(strList).subList(1, strList.length), strList[0], new MultiSpinnerListener() {
			@Override
			public void onItemsSelected(boolean[] selected) {
			}
		});
		
		final Button btn = (Button) dialog.findViewById(R.id.btn_select);
		btn.setTag(teamData);
		btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
				if (callback != null) {
					int gameMode = mode.getSelectedItemPosition();

					TeamData data = (TeamData) btn.getTag();
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("nullAwoken", nullAwoken.isChecked());
					map.put("copMode", copMode.isChecked());
					map.put("gameMode", gameMode);
					map.put("monsterUp", spinner.getSelection());
					callback.onSelect(data.getNo(), map);
				}
			}
		});

		Button next = (Button) dialog.findViewById(R.id.btn_next);
		next.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				TeamData data = (TeamData) btn.getTag();
				int no = data.getNo();
				if (no == TeamFragment.TEAM_SIZE-1) {
					no = 0;
				} else {
					++no;
				}
				dialog.setTitle(teamNameList.get(no));
				data.setTeamNo(no);
				data.initMemberData();
			}
		});

		Button previous = (Button) dialog.findViewById(R.id.btn_previous);
		previous.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				TeamData data = (TeamData) btn.getTag();
				int no = data.getNo();
				if (no == 0) {
					no = TeamFragment.TEAM_SIZE-1;
				} else {
					--no;
				}
				dialog.setTitle(teamNameList.get(no));
				data.setTeamNo(no);
				data.initMemberData();
			}
		});

		dialog.setOnCancelListener(new OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog) {
				dialog.dismiss();
				if (callback != null) {
					callback.onCancel();
				}
			}
		});
		return dialog;
	}


	public static Dialog getTeam2SelectDialog(Context context,
			final ITeam2SelectCallback callback) {

		int no = ComboMasterApplication.getsInstance().getTargetNo();

		final Dialog dialog = new Dialog(context);

		SharedPreferences pref = context.getSharedPreferences("team",
				Context.MODE_PRIVATE);
		final List<String> teamNameList = new ArrayList<String>();
		for (int i = 0; i < TeamFragment.TEAM_SIZE; ++i) {
			teamNameList.add(pref.getString("Team" + i + "_name", "Team "
					+ (i + 1)));
		}
		dialog.setTitle(teamNameList.get(no));
		dialog.setContentView(R.layout.team_2_select_dialog);

		MultipleTeamData teamData = new MultipleTeamData(context);
		teamData.setTeamNo(no);
		teamData.addMember(MonsterData.load(dialog.findViewById(R.id.leader1)));
		teamData.addMember(MonsterData.load(dialog.findViewById(R.id.member1)));
		teamData.addMember(MonsterData.load(dialog.findViewById(R.id.member2)));
		teamData.addMember(MonsterData.load(dialog.findViewById(R.id.member3)));
		teamData.addMember(MonsterData.load(dialog.findViewById(R.id.member4)));
		teamData.initMemberData();
		
		MultipleTeamData teamData2 = new MultipleTeamData(context);
		teamData2.setTeamNo(1);
		teamData2.addMember(MonsterData.load(dialog.findViewById(R.id.leader1_2)));
		teamData2.addMember(MonsterData.load(dialog.findViewById(R.id.member1_2)));
		teamData2.addMember(MonsterData.load(dialog.findViewById(R.id.member2_2)));
		teamData2.addMember(MonsterData.load(dialog.findViewById(R.id.member3_2)));
		teamData2.addMember(MonsterData.load(dialog.findViewById(R.id.member4_2)));
		teamData2.initMemberData();

		final Button btn = (Button) dialog.findViewById(R.id.btn_select);
		btn.setTag(R.id.btn_01, teamData);
		btn.setTag(R.id.btn_02, teamData2);
		btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
				if (callback != null) {
					MultipleTeamData data = (MultipleTeamData) btn.getTag(R.id.btn_01);
					MultipleTeamData data2 = (MultipleTeamData) btn.getTag(R.id.btn_02);
					Map<String, Object> map = new HashMap<String, Object>();
					callback.onSelect(data.getNo(), data2.getNo(), map);
				}
			}
		});

		Button next = (Button) dialog.findViewById(R.id.btn_next);
		next.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				MultipleTeamData data = (MultipleTeamData) btn.getTag(R.id.btn_01);
				int no = data.getNo();
				if (no == TeamFragment.TEAM_SIZE-1) {
					no = 0;
				} else {
					++no;
				}
				dialog.setTitle(teamNameList.get(no));
				data.setTeamNo(no);
				data.initMemberData();
			}
		});

		Button previous = (Button) dialog.findViewById(R.id.btn_previous);
		previous.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				MultipleTeamData data = (MultipleTeamData) btn.getTag(R.id.btn_01);
				int no = data.getNo();
				if (no == 0) {
					no = TeamFragment.TEAM_SIZE-1;
				} else {
					--no;
				}
				dialog.setTitle(teamNameList.get(no));
				data.setTeamNo(no);
				data.initMemberData();
			}
		});


		Button next2 = (Button) dialog.findViewById(R.id.btn_next_2);
		next2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				MultipleTeamData data = (MultipleTeamData) btn.getTag(R.id.btn_02);
				int no = data.getNo();
				if (no == TeamFragment.TEAM_SIZE-1) {
					no = 0;
				} else {
					++no;
				}
				data.setTeamNo(no);
				data.initMemberData();
			}
		});

		Button previous2 = (Button) dialog.findViewById(R.id.btn_previous_2);
		previous2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				MultipleTeamData data = (MultipleTeamData) btn.getTag(R.id.btn_02);
				int no = data.getNo();
				if (no == 0) {
					no = TeamFragment.TEAM_SIZE-1;
				} else {
					--no;
				}
				data.setTeamNo(no);
				data.initMemberData();
			}
		});
		
		dialog.setOnCancelListener(new OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog) {
				dialog.dismiss();
				if (callback != null) {
					callback.onCancel();
				}
			}
		});
		return dialog;
	}
	
	public interface ResultCallback {
		public void onResult(int value);

		public void onCancel();
	}


	public static Dialog getNumberRangeDialog(Context context, int title,
			int value, final int min, final int max,
			final ResultCallback callback) {
		final Dialog dialog = new Dialog(context);
		dialog.setTitle(title);
		dialog.setContentView(R.layout.dialog_number_picker);

		final NumberPicker picker10 = (NumberPicker) dialog
				.findViewById(R.id.pick_ten);
		picker10.setVisibility(View.GONE);
		final NumberPicker picker1 = (NumberPicker) dialog
				.findViewById(R.id.pick_one);
		
		picker1.setMaxValue(max);
		picker1.setMinValue(min);

		if(value<min) {
			value = min;
		} else if(value>max) {
			value = max;
		}
		picker1.setValue(value);

		picker1.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

		Button btn = (Button) dialog.findViewById(R.id.btn_ok);
		btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (callback != null) {
					int value = picker1.getValue();
					if (value > max) {
						value = max;
					} else if (value < min) {
						value = min;
					}
					callback.onResult(value);
				}
				dialog.dismiss();
			}
		});
		Button cancelBtn = (Button) dialog.findViewById(R.id.btn_cancel);
		cancelBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (callback != null) {
					callback.onCancel();
				}
				dialog.dismiss();
			}
		});
		dialog.setOnCancelListener(new OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog) {
				if (callback != null) {
					callback.onCancel();
				}
				dialog.dismiss();
			}
		});
		return dialog;
	}
	
	public static Dialog getNumberPickerDialog(Context context, int title,
			int value, final int min, final int max,
			final ResultCallback callback) {
		final Dialog dialog = new Dialog(context);
		dialog.setTitle(title);
		dialog.setContentView(R.layout.dialog_number_picker);

		final NumberPicker picker10 = (NumberPicker) dialog
				.findViewById(R.id.pick_ten);
		final NumberPicker picker1 = (NumberPicker) dialog
				.findViewById(R.id.pick_one);

		picker10.setMaxValue(max / 10);
		if (max < 10) {
			picker1.setMaxValue(max % 10);
		} else {
			picker1.setMaxValue(9);
		}

		if(value<min) {
			value = min;
		} else if(value>max) {
			value = max;
		}
		
		picker10.setValue(value / 10);
		picker1.setValue(value % 10);

		picker10.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
		picker1.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

		Button btn = (Button) dialog.findViewById(R.id.btn_ok);
		btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (callback != null) {
					int value = picker10.getValue() * 10 + picker1.getValue();
					if (value > max) {
						value = max;
					} else if (value < min) {
						value = min;
					}
					callback.onResult(value);
				}
				dialog.dismiss();
			}
		});
		Button cancelBtn = (Button) dialog.findViewById(R.id.btn_cancel);
		cancelBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (callback != null) {
					callback.onCancel();
				}
				dialog.dismiss();
			}
		});
		dialog.setOnCancelListener(new OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog) {
				if (callback != null) {
					callback.onCancel();
				}
				dialog.dismiss();
			}
		});
		return dialog;
	}

	public static Dialog getNumberDialog(Context context, int value,
			final ResultCallback callback) {
		final Dialog dialog = new Dialog(context);
		dialog.setTitle(R.string.str_monster_no);
		dialog.setContentView(R.layout.dialog_number);

		final EditText edit = (EditText) dialog.findViewById(R.id.monster_no);
		edit.setText(String.valueOf(value));
		Button btn = (Button) dialog.findViewById(R.id.btn_ok);
		btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (callback != null) {
					String data = edit.getText().toString();
					if (!TextUtils.isEmpty(data)) {
						int value = Integer.parseInt(data);
						callback.onResult(value);
					} else {
						callback.onCancel();
					}
				}
				dialog.dismiss();
			}
		});
		Button cancelBtn = (Button) dialog.findViewById(R.id.btn_cancel);
		cancelBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (callback != null) {
					callback.onCancel();
				}
				dialog.dismiss();
			}
		});

		return dialog;
	}

	public static Dialog getDamageDialog(Context context, TeamInfo team,
			List<Pair<Double, Double>> data) {
		final Dialog dialog = new Dialog(context);

		return dialog;
	}

	private static void initCombo(Context context, Dialog dialog,
			List<Match> matches) {
		final int[] id = { R.id.orb1_text, R.id.orb2_text, R.id.orb3_text,
				R.id.orb4_text, R.id.orb5_text, R.id.orb6_text, R.id.orb7_text,
				R.id.orb8_text };

		int[] imageId;
		SharedPreferenceUtil utils = SharedPreferenceUtil.getInstance(context);
		IMAGE_TYPE imagetype = (utils.getType() == 2) ? IMAGE_TYPE.TYPE_TOS
				: IMAGE_TYPE.TYPE_PAD;
		if (imagetype == IMAGE_TYPE.TYPE_PAD) {
			imageId = new int[] { R.drawable.dark, R.drawable.fire,
					R.drawable.heart, R.drawable.light, R.drawable.water,
					R.drawable.wood, R.drawable.poison, R.drawable.jammar };
		} else {
			imageId = new int[] { R.drawable.gem_dark, R.drawable.gem_fire,
					R.drawable.gem_heart, R.drawable.gem_light,
					R.drawable.gem_water, R.drawable.gem_wood,
					R.drawable.poison, R.drawable.jammar };
		}

		Resources res = context.getResources();
		StringBuilder[] sb = new StringBuilder[9];
		for (int i = 0; i < 9; ++i) {
			sb[i] = new StringBuilder();
		}
		int[] count = { 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		int total = 0;
		for (Match m : matches) {
			int type = m.type;
			if(type>8) {
				++total;
				continue;
			}
			++count[type];
			sb[type].append(m.count);
			sb[type].append("x ");
		}

		List<TextView> textView = new ArrayList<TextView>();
		for (int i = 0; i < id.length; ++i) {
			TextView tv = (TextView) dialog.findViewById(id[i]);
			textView.add(tv);
			tv.setCompoundDrawablesWithIntrinsicBounds(
					res.getDrawable(imageId[i]), null, null, null);
			if (count[i] == 0) {
				tv.setText(" 0c");
				// tv.setVisibility(View.GONE);
			} else {
				tv.setText(" " + count[i] + "c=" + sb[i].toString());
				// tv.setVisibility(View.VISIBLE);
			}
			total += count[i];
		}

		TextView combo = (TextView) dialog.findViewById(R.id.total_combo);
		combo.setText(context.getString(R.string.total_combo, total));
	}

	public static Dialog getScoreDialog(Context context, List<Match> matches) {

		final Dialog dialog = new Dialog(context);
		dialog.setTitle(R.string.result);
		dialog.setContentView(R.layout.combo_dialog);

		initCombo(context, dialog, matches);

		Button btn = (Button) dialog.findViewById(R.id.btn_ok);
		btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		return dialog;
	}

	public static Dialog getUpdateContentDialog(Context context, String message) {
		final Dialog dialog = new Dialog(context);
		dialog.setTitle(R.string.update);
		dialog.setContentView(R.layout.dialog_update);
		TextView text = (TextView) dialog.findViewById(R.id.update_content);
		text.setText(message);

		Button button = (Button) dialog.findViewById(R.id.btn_ok);
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		return dialog;
	}

	public static ProgressDialog getUpdateDialog(Context context, boolean download) {
		final ProgressDialog dialog = new ProgressDialog(context) {
		};

        try {
            // setProgressNumberFormat throws NullPointerException in android 3.1 Optimus Pad (l06c)
            dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            dialog.setProgressNumberFormat("%1d / %2d ");
        } catch(Exception e) {
        }
        try {
            dialog.setCancelable(false);
            dialog.setIndeterminate(false);
            dialog.setTitle(R.string.update);
            if(download) {
            	dialog.setMessage(context.getString(R.string.downloading));
            } else {
            	dialog.setMessage(context.getString(R.string.copying));
            }
        } catch(Exception e) {
        }
		return dialog;
	}

	public static Dialog getSuperAwokenDialog(Context context, IPotentialCallback callback, AwokenSkill skill) {
		return null;
	}

	public static Dialog getPotentialDialog(Context context, IPotentialCallback callback, MoneyAwokenSkill[] skills) {
		PotentialPickerDialog dialog = new PotentialPickerDialog(context);
		dialog.setSkills(skills);
		dialog.setContentView(R.layout.dialog_potential_picker);
		dialog.setCallback(callback);
		return dialog;
	}

	private static String getTurns(Context context, int counter, int cd) {
		int turns = (cd-counter);
		if(turns < 0) {
			turns = 0;
		}
		return context.getString(R.string.in_turns, turns);
	}
	
	public static Dialog getSkillDialog(Context context,
			MonsterInfo info,
			int counter,  int cd1, int cd2,
			DialogInterface.OnClickListener okListener,
			DialogInterface.OnClickListener cancelListener) {
		
		String message = context.getString(R.string.dialog_skill_1, getTurns(context, counter, cd1), cd1);
		
		if(cd2>0) {
			message = message + "\n" + context.getString(R.string.dialog_skill_2, getTurns(context, counter, cd1+cd2), cd1+cd2);
		}
		boolean enable = true;
		String useSkill;
		if(counter>=(cd1+cd2) && cd2 > 0) {
			String skill = "";
			for(ActiveSkill as : info.getActiveSkill2()) {
				skill += MonsterSkill.getViewableString(context, as);
			}
			//context.getString(R.string.use_skill) + " " +
			useSkill = "\n" + skill + 
					context.getString(R.string.use_skill) + " " +
					context.getString(R.string.skill_2) + " ?";
		} else if(counter>=cd1) {
			String skill = "";
			for(ActiveSkill as : info.getActiveSkill()) {
				skill += MonsterSkill.getViewableString(context, as);
			}
			//
			useSkill = "\n" + skill + 
					context.getString(R.string.use_skill) + " " + context.getString(R.string.skill_1) + " ?";
		} else {
			useSkill = "";
			enable = false;
		}
		
		AlertDialog.Builder builder = new Builder(context);
		builder.setMessage(message + useSkill)
				.setTitle(R.string.skill)
				.setPositiveButton(android.R.string.cancel, cancelListener);
		if(enable) {
			builder.setNegativeButton(android.R.string.yes, okListener);
		}
		return builder.create();
	}
}
