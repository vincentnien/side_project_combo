package com.a30corner.combomaster.activity.fragment;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.a30corner.combomaster.R;
import com.a30corner.combomaster.activity.MonsterDetailActivity;
import com.a30corner.combomaster.activity.SkillSetupActivity;
import com.a30corner.combomaster.activity.ui.DialogUtil;
import com.a30corner.combomaster.pad.PadBoardAI;
import com.a30corner.combomaster.pad.monster.MonsterVO;
import com.a30corner.combomaster.provider.LocalDBHelper;
import com.a30corner.combomaster.utils.FileUtil;
import com.a30corner.combomaster.utils.SharedPreferenceUtil;
import com.a30corner.combomaster.utils.SharedPreferenceUtil.Skill;

public class SkillListFragment extends CMBaseFragment {

	private Button[] mSkillButton;
	private ImageView[] mSkillImageView;
	private List<Integer> mSkillImages;

	private static class ViewHolder {
		private final View[] viewDetail;

		private ViewHolder(View v1, View v2) {
			viewDetail = new View[2];
			viewDetail[0] = v1;
			viewDetail[1] = v2;
		}

		public static ViewHolder create(View v1, View v2) {
			return new ViewHolder(v1, v2);
		}

		public View getView1() {
			return viewDetail[0];
		}

		public View getView2() {
			return viewDetail[1];
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.skill_list, null);
		mSkillButton = new Button[6];

		mSkillImages = new ArrayList<Integer>(SharedPreferenceUtil.getInstance(
				getActivity()).getSkillImages());
		mSkillImageView = new ImageView[6];

		final int[] resButtonId = { R.id.skill1, R.id.skill2, R.id.skill3,
				R.id.skill4, R.id.skill5, R.id.skill6 };
		final int[] resSkillId = { R.id.layout_skill_1, R.id.layout_skill_2,
				R.id.layout_skill_3, R.id.layout_skill_4, R.id.layout_skill_5,
				R.id.layout_skill_6 };
		final int[] resImageId = { R.id.skill_image_1, R.id.skill_image_2,
				R.id.skill_image_3, R.id.skill_image_4, R.id.skill_image_5,
				R.id.skill_image_6 };
		for (int i = 0; i < 6; ++i) {
			Button btn = (Button) view.findViewById(resButtonId[i]);
			btn.setOnClickListener(onClickListener);

			mSkillImageView[i] = (ImageView) view.findViewById(resImageId[i]);
			mSkillImageView[i].setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					int pos = 0;
					for (int i = 0; i < 6; ++i) {
						if (resImageId[i] == v.getId()) {
							pos = i;
							break;
						}
					}
					final int index = pos;
					DialogUtil.getNumberDialog(getActivity(),
							mSkillImages.get(index),
							new DialogUtil.ResultCallback() {

								@Override
								public void onResult(int value) {
									mSkillImages.set(index, value);
									SharedPreferenceUtil.getInstance(
											getActivity()).setImageId(index,
											value);
									;
									updateSkillImage(index);
								}

								@Override
								public void onCancel() {
								}
							}).show();
				}
			});

			View skillLayout = view.findViewById(resSkillId[i]);
			View v1 = skillLayout.findViewById(R.id.description_transform);
			View v2 = skillLayout.findViewById(R.id.description_stance);
			btn.setTag(ViewHolder.create(v1, v2));

			mSkillButton[i] = btn;
		}

		return view;
	}

	@Override
	public void onResume() {
		super.onResume();
		setSkillDetail();
	}

	private void updateSkillImage(int index) {
		int id = mSkillImages.get(index);
		if (id == 0) {
			mSkillImageView[index].setImageResource(R.drawable.i000);
		} else {
			setImage(index, id);
		}
	}

	private void setImage(int index, int no) {
		Activity context = getActivity();
		final File f = new File(context.getFilesDir(), no + "i.png");
		if (f.exists()) {
			Bitmap bitmap = BitmapFactory.decodeFile(f.getAbsolutePath());
			mSkillImageView[index].setImageBitmap(bitmap);
		} else {
			MonsterVO vo = LocalDBHelper.getMonsterData(context, no);
			if ( vo != null ) {
				mSkillImageView[index].setImageResource(R.drawable.i000);
				new DownloadTask(context, no, mSkillImageView[index]).execute(vo.getImageUrl());
			}
//			try {
//				vo = MonsterVO.fromAsset(context, no);
//				mSkillImageView[index].setImageResource(R.drawable.ic_skill);
//				new DownloadTask(context, no, mSkillImageView[index])
//						.execute(vo.getImageUrl());
//			} catch (IOException e) {
//				Toast.makeText(context, "Loading data fail.",
//						Toast.LENGTH_SHORT).show();
//			}

		}
	}

	private class DownloadTask extends AsyncTask<String, Void, Bitmap> {

		private ImageView mImageView;
		private Activity context;
		private int no;

		public DownloadTask(Activity context, int no, ImageView imageView) {
			this.context = context;
			this.no = no;
			mImageView = imageView;
		}

		@Override
		protected Bitmap doInBackground(String... url) {
			InputStream is = null;
			try {
				is = new URL(url[0]).openStream();
				Bitmap bitmap = BitmapFactory.decodeStream(is);
				FileUtil.saveImage(context, bitmap, no + "i.png");
				return bitmap;
			} catch (Exception e) {
				context.runOnUiThread(new Runnable() {

					@Override
					public void run() {
						Toast.makeText(context,
								context.getString(R.string.need_network),
								Toast.LENGTH_LONG).show();
						;
					}
				});
			} finally {
				if (is != null) {
					try {
						is.close();
					} catch (IOException e) {
					}
				}
			}
			return null;
		}

		@Override
		protected void onPostExecute(Bitmap result) {
			if (result == null) {
				return;
			}
			mImageView.setImageBitmap(result);
		}

	}

	private void setSkillDetail() {
		List<Skill> skillList = getSharedPreference().getSkillList();
		String[] skillTitle = getActivity().getResources().getStringArray(
				R.array.skills);
		for (int i = 0; i < 6; ++i) {
			Skill skill = skillList.get(i);
			Button btn = mSkillButton[i];
			ViewHolder holder = (ViewHolder) btn.getTag();
			View v1 = holder.getView1();
			View v2 = holder.getView2();
			switch (skill.getType()) {
			case SKILL_TRANSFORM:
				btn.setText(skillTitle[0]);
				v1.setVisibility(View.VISIBLE);
				v2.setVisibility(View.GONE);
				showTransformOrbs(v1, skill.getData());

				break;
			case SKILL_STANCE:
				btn.setText(skillTitle[1]);
				v2.setVisibility(View.VISIBLE);
				v1.setVisibility(View.GONE);
				showStance(v2, skill.getData());
				break;
			case SKILL_THE_WORLD:
				btn.setText(skillTitle[2]);
				v1.setVisibility(View.GONE);
				v2.setVisibility(View.GONE);
				break;
			default:
			}
			updateSkillImage(i);
		}
	}

	private void showTransformOrbs(View view, List<Long> data) {
		final int[] imageViewRes = { R.id.drop_type_1, R.id.drop_type_2,
				R.id.drop_type_3, R.id.drop_type_4, R.id.drop_type_5,
				R.id.drop_type_6 };
		boolean[] selected = PadBoardAI.getSelectedOrbs(data.get(0));
		for (int i = 0; i < 6; ++i) {
			ImageView image = (ImageView) view.findViewById(imageViewRes[i]);
			image.setVisibility(selected[i] ? View.VISIBLE : View.GONE);
		}
	}

	private void showStance(View view, List<Long> data) {
		final int[] imageViewRes = { R.id.from1, R.id.to1, R.id.from2, R.id.to2 };
		final int[] orbImageRes = { R.drawable.dark, R.drawable.fire,
				R.drawable.heart, R.drawable.light, R.drawable.water,
				R.drawable.wood };

		if (data.size() >= 2) {
			View stance1 = view.findViewById(R.id.transform1);
			for (int i = 0; i < 2; ++i) {
				ImageView image = (ImageView) stance1
						.findViewById(imageViewRes[i]);
				image.setImageResource(orbImageRes[PadBoardAI.getOrbType(data
						.get(i))]);
			}

			View stance2 = view.findViewById(R.id.transform2);
			if (data.size() >= 4) {
				long d3 = data.get(2);
				long d4 = data.get(3);
				if (d3 != d4) {
					stance2.setVisibility(View.VISIBLE);
					for (int i = 2; i < 4; ++i) {
						ImageView image = (ImageView) stance2
								.findViewById(imageViewRes[i]);
						if (data.size() > i) {
							image.setImageResource(orbImageRes[PadBoardAI
									.getOrbType(data.get(i))]);
						}
					}
				} else {
					stance2.setVisibility(View.GONE);
				}
			} else {
				stance2.setVisibility(View.GONE);
			}
		}

	}

	private OnClickListener onClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			int index = 0;
			switch (v.getId()) {
			case R.id.skill1:
				index = 0;
				break;
			case R.id.skill2:
				index = 1;
				break;

			case R.id.skill3:
				index = 2;
				break;
			case R.id.skill4:
				index = 3;
				break;
			case R.id.skill5:
				index = 4;
				break;
			case R.id.skill6:
				index = 5;
				break;
			}
			Intent intent = new Intent(getActivity(), SkillSetupActivity.class);
			intent.putExtra("index", index);
			startActivity(intent);
		}
	};
}
