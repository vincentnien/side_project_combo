package com.a30corner.combomaster.playground.stagedata;

import android.app.Activity;
import android.util.Log;
import android.util.Pair;
import android.util.SparseArray;

import com.a30corner.combomaster.pad.monster.MonsterSkill;
import com.a30corner.combomaster.pad.monster.MonsterVO;
import com.a30corner.combomaster.playground.IEnvironment;
import com.a30corner.combomaster.playground.StageGenerator;
import com.a30corner.combomaster.playground.entity.Enemy;
import com.a30corner.combomaster.provider.LocalDBHelper;
import com.a30corner.combomaster.utils.LogUtil;
import com.a30corner.combomaster.utils.ZipUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class IStage {


    public List<StageGenerator.SkillText> loadSkillText(IEnvironment env, String stage) {
        Activity activity = env.getScene().getActivity();
        try {
            if (ZipUtils.unpackZip(activity,
                    activity.getAssets().open("data/" + stage + ".zip"))) {
                Gson gson = new Gson();
                java.lang.reflect.Type listType = new TypeToken<List<StageGenerator.SkillText>>() {
                }.getType();
                String loc = Locale.getDefault().getLanguage();
                if (!"ja".equals(loc) && !"zh".equals(loc) && !"en".equals(loc)) {
                    loc = "en";
                }
                return gson.fromJson(
                        new FileReader(new File(activity.getCacheDir(),
                                "skill-" + loc + ".json")), listType);
            }
        } catch (IOException e) {
            Log.e("Vincent", e.toString());
        }
        LogUtil.e("Load skill text failed");
        return new ArrayList<StageGenerator.SkillText>();
    }

    static MonsterVO getMonster(IEnvironment env, int id) {
        return LocalDBHelper.getMonsterData(env.getScene().getActivity(), id);
    }

    protected  SparseArray<List<StageGenerator.SkillText>> loadSkillTextNew(IEnvironment env, String stage, int[] ids) {
        Activity activity = env.getScene().getActivity();
        SparseArray<List<StageGenerator.SkillText>> arr = new SparseArray<List<StageGenerator.SkillText>>();
        try {
            if (ZipUtils.unpackZip(activity,
                    activity.getAssets().open("data/" + stage + ".zip"))) {
                Gson gson = new Gson();
                java.lang.reflect.Type listType = new TypeToken<List<StageGenerator.SkillText>>() {
                }.getType();
                String loc = Locale.getDefault().getLanguage();
                if (!"ja".equals(loc) && !"zh".equals(loc) && !"en".equals(loc)) {
                    loc = "en";
                }
                for(int id :ids) {
                    List<StageGenerator.SkillText> list = gson.fromJson(new FileReader(new File(activity.getCacheDir(),
                            loc + "_" + id +".json")), listType);
                    arr.append(id, list);
                }
            }
        } catch (IOException e) {
            Log.e("Vincent", e.toString());
        }
        return arr;
    }

    protected  StageGenerator.SkillText[] loadSubText(SparseArray<List<StageGenerator.SkillText>> list, int id) {
        List<StageGenerator.SkillText> sublist = list.get(id);
        int size = sublist.size();
        StageGenerator.SkillText[] text = new StageGenerator.SkillText[size];
        for(int i=0; i<size; ++i) {
            text[i] = getSkillText(sublist, i);
        }
        return text;
    }

    protected StageGenerator.SkillText getSkillText(List<StageGenerator.SkillText> data, int index) {
        if (index < data.size()) {
            return data.get(index);
        }
        return new StageGenerator.SkillText();
    }

    protected Pair<Integer, Integer> getMonsterAttrs(IEnvironment env,
                                                             int id) {
        MonsterVO vo = LocalDBHelper.getMonsterData(env.getScene()
                .getActivity(), id);
        if (vo != null) {
            Pair<Integer, Integer> mp = vo.getMonsterProps();
            return new Pair<Integer, Integer>(mp.first, (mp.second.equals(mp.first)) ? -1 : mp.second);
        }
        return new Pair<Integer, Integer>(1, 0); // FIXME: throw exception?
    }
    protected List<MonsterSkill.MonsterType> getMonsterTypes(IEnvironment env,
                                                          int id) {
        MonsterVO vo = LocalDBHelper.getMonsterData(env.getScene()
                .getActivity(), id);
        List<MonsterSkill.MonsterType> list = new ArrayList<MonsterSkill.MonsterType>();
        Pair<MonsterSkill.MonsterType, MonsterSkill.MonsterType> types = vo
                .getMonsterTypes();
        list.add(types.first);
        if (types.second != MonsterSkill.MonsterType.NONE) {
            list.add(types.second);
        }
        if (vo.getMonsterType3() != MonsterSkill.MonsterType.NONE) {
            list.add(vo.getMonsterType3());
        }
        return list;
    }

    List<MonsterSkill.MonsterType> getMonsterTypes(MonsterVO vo) {
        List<MonsterSkill.MonsterType> list = new ArrayList<MonsterSkill.MonsterType>();
        Pair<MonsterSkill.MonsterType, MonsterSkill.MonsterType> types = vo
                .getMonsterTypes();
        list.add(types.first);
        if (types.second != MonsterSkill.MonsterType.NONE) {
            list.add(types.second);
        }
        if (vo.getMonsterType3() != MonsterSkill.MonsterType.NONE) {
            list.add(vo.getMonsterType3());
        }
        return list;
    }

    public void create(IEnvironment env, SparseArray<ArrayList<Enemy>> mStageEnemies, String mStage) {
    }


}
