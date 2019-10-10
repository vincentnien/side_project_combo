package com.a30corner.combomaster.playground.stagedata;

import android.util.SparseArray;

import com.a30corner.combomaster.playground.IEnvironment;
import com.a30corner.combomaster.playground.enemy.EnemyAttackMode;
import com.a30corner.combomaster.playground.enemy.attack.SequenceAttack;
import com.a30corner.combomaster.playground.entity.Enemy;
import com.a30corner.combomaster.utils.RandomUtil;

import java.util.ArrayList;

public class LvArena extends IStage {

    @Override
    public void create(IEnvironment env, SparseArray<ArrayList<Enemy>> mStageEnemies, String mStage) {
        ArrayList<Enemy> data;
        Enemy zero;
        mStageEnemies.clear();


        data = new ArrayList<Enemy>();
        {
//            int[] ids = {152,153,154};
            for (int i = 0; i < 1; ++i) {

                data.add(Enemy.create(env, 152, 10000000, 16120, 30, 2,getMonsterAttrs(env,152),getMonsterTypes(env, 152)));
                zero = data.get(0);

                zero.attackMode = new EnemyAttackMode();
                zero.attackMode.createSimpleAttackAction("", "", 100);

                //
                data.add(Enemy.create(env, 153, 20000000, 22600, 40, 3,getMonsterAttrs(env,153),getMonsterTypes(env, 153)));
                zero = data.get(1);

                zero.attackMode = new EnemyAttackMode();
                zero.attackMode.createSimpleAttackAction("", "", 100);

                //

                data.add(Enemy.create(env, 154, 20000000, 22600, 40, 3,getMonsterAttrs(env,153),getMonsterTypes(env, 153)));
                zero = data.get(2);

                zero.attackMode = new EnemyAttackMode();
                zero.attackMode.createSimpleAttackAction("", "", 100);


                //

                data.add(Enemy.create(env, 153, 20000000, 22600, 40, 3,getMonsterAttrs(env,153),getMonsterTypes(env, 153)));
                zero = data.get(3);

                zero.attackMode = new EnemyAttackMode();
                zero.attackMode.createSimpleAttackAction("", "", 100);



                //

                data.add(Enemy.create(env, 152, 10000000, 16120, 30, 2,getMonsterAttrs(env,152),getMonsterTypes(env, 152)));
                zero = data.get(4);

                zero.attackMode = new EnemyAttackMode();
                zero.attackMode.createSimpleAttackAction("", "", 100);

            }

        }
        mStageEnemies.put(1, data);


        data = new ArrayList<Enemy>();
        {
            int[] ids = {3483,3507};
            for (int i = 0; i < 1; ++i) {

                int rnd = RandomUtil.getInt(ids.length);
                int choose = ids[rnd];
                //text = loadSubText(list, choose);
                if (choose == 3483) {
                    data.add(Enemy.create(env, 3483, 79000000, 34300, 320000, 1,getMonsterAttrs(env,3483),getMonsterTypes(env, 3483)));
                    zero = data.get(i);

                    zero.attackMode = new EnemyAttackMode();
                    zero.attackMode.createSimpleAttackAction("", "", 100);

                }
                if (choose == 3507) {
                    data.add(Enemy.create(env, 3507, 85000000, 30500, 260000, 1,getMonsterAttrs(env,3507),getMonsterTypes(env, 3507)));
                    zero = data.get(i);

                    zero.attackMode = new EnemyAttackMode();


                    zero.attackMode.createSimpleAttackAction("", "", 100);

                }

            }

        }
        mStageEnemies.put(2, data);

        data = new ArrayList<Enemy>();
        {
            int[] ids = {155,156,157,158,159,246,247,248,249,250};
            for (int i = 0; i < RandomUtil.range(5, 6); ++i) {

                int rnd = RandomUtil.getInt(ids.length);
                int choose = ids[rnd];
                //text = loadSubText(list, choose);
                if (choose == 155) {
                    data.add(Enemy.create(env, 155, 11000000, 14000, 30, 1,getMonsterAttrs(env,155),getMonsterTypes(env, 155)));
                    zero = data.get(i);

                    zero.attackMode = new EnemyAttackMode();


                    zero.attackMode.createSimpleAttackAction("", "", 100);

                }
                if (choose == 156) {
                    data.add(Enemy.create(env, 156, 12000000, 13000, 30, 1,getMonsterAttrs(env,156),getMonsterTypes(env, 156)));
                    zero = data.get(i);

                    zero.attackMode = new EnemyAttackMode();


                    zero.attackMode.createSimpleAttackAction("", "", 100);

                }
                if (choose == 157) {
                    data.add(Enemy.create(env, 157, 10000000, 16000, 30, 1,getMonsterAttrs(env,157),getMonsterTypes(env, 157)));
                    zero = data.get(i);

                    zero.attackMode = new EnemyAttackMode();


                    zero.attackMode.createSimpleAttackAction("", "", 100);

                }
                if (choose == 158) {
                    data.add(Enemy.create(env, 158, 11000000, 15000, 30, 1,getMonsterAttrs(env,158),getMonsterTypes(env, 158)));
                    zero = data.get(i);

                    zero.attackMode = new EnemyAttackMode();


                    zero.attackMode.createSimpleAttackAction("", "", 100);

                }
                if (choose == 159) {
                    data.add(Enemy.create(env, 159, 10000000, 17000, 30, 1,getMonsterAttrs(env,159),getMonsterTypes(env, 159)));
                    zero = data.get(i);

                    zero.attackMode = new EnemyAttackMode();


                    zero.attackMode.createSimpleAttackAction("", "", 100);

                }
                if (choose == 246) {
                    data.add(Enemy.create(env, 246, 14, 55555, 300000000, 3,getMonsterAttrs(env,246),getMonsterTypes(env, 246)));
                    zero = data.get(i);

                    zero.attackMode = new EnemyAttackMode();


                    zero.attackMode.createSimpleAttackAction("", "", 100);

                }
                if (choose == 247) {
                    data.add(Enemy.create(env, 247, 14, 55555, 300000000, 3,getMonsterAttrs(env,247),getMonsterTypes(env, 247)));
                    zero = data.get(i);

                    zero.attackMode = new EnemyAttackMode();


                    zero.attackMode.createSimpleAttackAction("", "", 100);

                }
                if (choose == 248) {
                    data.add(Enemy.create(env, 248, 14, 55555, 300000000, 3,getMonsterAttrs(env,248),getMonsterTypes(env, 248)));
                    zero = data.get(i);

                    zero.attackMode = new EnemyAttackMode();


                    zero.attackMode.createSimpleAttackAction("", "", 100);

                }
                if (choose == 249) {
                    data.add(Enemy.create(env, 249, 14, 55555, 300000000, 3,getMonsterAttrs(env,249),getMonsterTypes(env, 249)));
                    zero = data.get(i);

                    zero.attackMode = new EnemyAttackMode();


                    zero.attackMode.createSimpleAttackAction("", "", 100);

                }
                if (choose == 250) {
                    data.add(Enemy.create(env, 250, 14, 55555, 300000000, 3,getMonsterAttrs(env,250),getMonsterTypes(env, 250)));
                    zero = data.get(i);

                    zero.attackMode = new EnemyAttackMode();


                    zero.attackMode.createSimpleAttackAction("", "", 100);

                }

            }

        }
        mStageEnemies.put(3, data);

        data = new ArrayList<Enemy>();
        {
            int[] ids = {4638,4640,4642,4644,4646};
            for (int i = 0; i < 2; ++i) {

                int rnd = RandomUtil.getInt(ids.length);
                int choose = ids[rnd];
                //text = loadSubText(list, choose);
                if (choose == 4638) {
                    data.add(Enemy.create(env, 4638, 54000000, 37300, 196000, 1,getMonsterAttrs(env,4638),getMonsterTypes(env, 4638)));
                    zero = data.get(i);

                    zero.attackMode = new EnemyAttackMode();


                    zero.attackMode.createSimpleAttackAction("", "", 100);

                }
                if (choose == 4640) {
                    data.add(Enemy.create(env, 4640, 53000000, 35100, 196000, 1,getMonsterAttrs(env,4640),getMonsterTypes(env, 4640)));
                    zero = data.get(i);

                    zero.attackMode = new EnemyAttackMode();


                    zero.attackMode.createSimpleAttackAction("", "", 100);

                }
                if (choose == 4642) {
                    data.add(Enemy.create(env, 4642, 51000000, 39200, 196000, 1,getMonsterAttrs(env,4642),getMonsterTypes(env, 4642)));
                    zero = data.get(i);

                    zero.attackMode = new EnemyAttackMode();


                    zero.attackMode.createSimpleAttackAction("", "", 100);

                }
                if (choose == 4644) {
                    data.add(Enemy.create(env, 4644, 60000000, 33500, 196000, 1,getMonsterAttrs(env,4644),getMonsterTypes(env, 4644)));
                    zero = data.get(i);

                    zero.attackMode = new EnemyAttackMode();


                    zero.attackMode.createSimpleAttackAction("", "", 100);

                }
                if (choose == 4646) {
                    data.add(Enemy.create(env, 4646, 55000000, 39600, 196000, 1,getMonsterAttrs(env,4646),getMonsterTypes(env, 4646)));
                    zero = data.get(i);

                    zero.attackMode = new EnemyAttackMode();


                    zero.attackMode.createSimpleAttackAction("", "", 100);

                }

            }

        }
        mStageEnemies.put(4, data);

        data = new ArrayList<Enemy>();
        {
            int[] ids = {4273,4264};
            for (int i = 0; i < 1; ++i) {

                int rnd = RandomUtil.getInt(ids.length);
                int choose = ids[rnd];
                //text = loadSubText(list, choose);
                if (choose == 4273) {
                    data.add(Enemy.create(env, 4273, 160000000, 68000, 260000, 2,getMonsterAttrs(env,4273),getMonsterTypes(env, 4273)));
                    zero = data.get(i);

                    zero.attackMode = new EnemyAttackMode();


                    zero.attackMode.createSimpleAttackAction("", "", 100);

                }
                if (choose == 4264) {
                    data.add(Enemy.create(env, 4264, 100000000, 46100, 190000, 1,getMonsterAttrs(env,4264),getMonsterTypes(env, 4264)));
                    zero = data.get(i);

                    zero.attackMode = new EnemyAttackMode();


                    zero.attackMode.createSimpleAttackAction("", "", 100);

                }

            }

        }
        mStageEnemies.put(5, data);

        data = new ArrayList<Enemy>();
        {
            int[] ids = {161,162,163,164,165,166,167,168,169,170,171,172,173,174,175};
            for (int i = 0; i < 4; ++i) {

                int rnd = RandomUtil.getInt(ids.length);
                int choose = ids[rnd];
                //text = loadSubText(list, choose);
                if (choose == 161) {
                    data.add(Enemy.create(env, 161, 6, 26000, 50000000, 1,getMonsterAttrs(env,161),getMonsterTypes(env, 161)));
                    zero = data.get(i);

                    zero.attackMode = new EnemyAttackMode();


                    zero.attackMode.createSimpleAttackAction("", "", 100);

                }
                if (choose == 162) {
                    data.add(Enemy.create(env, 162, 6, 26000, 50000000, 1,getMonsterAttrs(env,162),getMonsterTypes(env, 162)));
                    zero = data.get(i);

                    zero.attackMode = new EnemyAttackMode();


                    zero.attackMode.createSimpleAttackAction("", "", 100);

                }
                if (choose == 163) {
                    data.add(Enemy.create(env, 163, 6, 26000, 50000000, 1,getMonsterAttrs(env,163),getMonsterTypes(env, 163)));
                    zero = data.get(i);

                    zero.attackMode = new EnemyAttackMode();


                    zero.attackMode.createSimpleAttackAction("", "", 100);

                }
                if (choose == 164) {
                    data.add(Enemy.create(env, 164, 6, 26000, 50000000, 1,getMonsterAttrs(env,164),getMonsterTypes(env, 164)));
                    zero = data.get(i);

                    zero.attackMode = new EnemyAttackMode();


                    zero.attackMode.createSimpleAttackAction("", "", 100);

                }
                if (choose == 165) {
                    data.add(Enemy.create(env, 165, 6, 26000, 50000000, 1,getMonsterAttrs(env,165),getMonsterTypes(env, 165)));
                    zero = data.get(i);

                    zero.attackMode = new EnemyAttackMode();


                    zero.attackMode.createSimpleAttackAction("", "", 100);

                }
                if (choose == 166) {
                    data.add(Enemy.create(env, 166, 12, 35000, 80000000, 3,getMonsterAttrs(env,166),getMonsterTypes(env, 166)));
                    zero = data.get(i);

                    zero.attackMode = new EnemyAttackMode();


                    zero.attackMode.createSimpleAttackAction("", "", 100);

                }
                if (choose == 167) {
                    data.add(Enemy.create(env, 167, 12, 35000, 80000000, 3,getMonsterAttrs(env,167),getMonsterTypes(env, 167)));
                    zero = data.get(i);

                    zero.attackMode = new EnemyAttackMode();


                    zero.attackMode.createSimpleAttackAction("", "", 100);

                }
                if (choose == 168) {
                    data.add(Enemy.create(env, 168, 12, 35000, 80000000, 3,getMonsterAttrs(env,168),getMonsterTypes(env, 168)));
                    zero = data.get(i);

                    zero.attackMode = new EnemyAttackMode();


                    zero.attackMode.createSimpleAttackAction("", "", 100);

                }
                if (choose == 169) {
                    data.add(Enemy.create(env, 169, 12, 35000, 80000000, 3,getMonsterAttrs(env,169),getMonsterTypes(env, 169)));
                    zero = data.get(i);

                    zero.attackMode = new EnemyAttackMode();


                    zero.attackMode.createSimpleAttackAction("", "", 100);

                }
                if (choose == 170) {
                    data.add(Enemy.create(env, 170, 12, 35000, 80000000, 3,getMonsterAttrs(env,170),getMonsterTypes(env, 170)));
                    zero = data.get(i);

                    zero.attackMode = new EnemyAttackMode();


                    zero.attackMode.createSimpleAttackAction("", "", 100);

                }
                if (choose == 171) {
                    data.add(Enemy.create(env, 171, 18, 86000, 100000000, 4,getMonsterAttrs(env,171),getMonsterTypes(env, 171)));
                    zero = data.get(i);

                    zero.attackMode = new EnemyAttackMode();


                    zero.attackMode.createSimpleAttackAction("", "", 100);

                }
                if (choose == 172) {
                    data.add(Enemy.create(env, 172, 18, 86000, 100000000, 4,getMonsterAttrs(env,172),getMonsterTypes(env, 172)));
                    zero = data.get(i);

                    zero.attackMode = new EnemyAttackMode();


                    zero.attackMode.createSimpleAttackAction("", "", 100);

                }
                if (choose == 173) {
                    data.add(Enemy.create(env, 173, 18, 86000, 100000000, 4,getMonsterAttrs(env,173),getMonsterTypes(env, 173)));
                    zero = data.get(i);

                    zero.attackMode = new EnemyAttackMode();


                    zero.attackMode.createSimpleAttackAction("", "", 100);

                }
                if (choose == 174) {
                    data.add(Enemy.create(env, 174, 18, 86000, 100000000, 4,getMonsterAttrs(env,174),getMonsterTypes(env, 174)));
                    zero = data.get(i);

                    zero.attackMode = new EnemyAttackMode();


                    zero.attackMode.createSimpleAttackAction("", "", 100);

                }
                if (choose == 175) {
                    data.add(Enemy.create(env, 175, 18, 86000, 100000000, 4,getMonsterAttrs(env,175),getMonsterTypes(env, 175)));
                    zero = data.get(i);

                    zero.attackMode = new EnemyAttackMode();


                    zero.attackMode.createSimpleAttackAction("", "", 100);

                }

            }

        }
        mStageEnemies.put(6, data);

        data = new ArrayList<Enemy>();
        {
            int[] ids = {3241,3243};
            for (int i = 0; i < 1; ++i) {

                int rnd = RandomUtil.getInt(ids.length);
                int choose = ids[rnd];
                //text = loadSubText(list, choose);
                if (choose == 3241) {
                    data.add(Enemy.create(env, 3241, 110000000, 63000, 320000, 1,getMonsterAttrs(env,3241),getMonsterTypes(env, 3241)));
                    zero = data.get(i);

                    zero.attackMode = new EnemyAttackMode();


                    zero.attackMode.createSimpleAttackAction("", "", 100);

                }
                if (choose == 3243) {
                    data.add(Enemy.create(env, 3243, 140000000, 51600, 640000, 1,getMonsterAttrs(env,3243),getMonsterTypes(env, 3243)));
                    zero = data.get(i);

                    zero.attackMode = new EnemyAttackMode();


                    zero.attackMode.createSimpleAttackAction("", "", 100);

                }

            }

        }
        mStageEnemies.put(7, data);

        data = new ArrayList<Enemy>();
        {
            if (RandomUtil.getLuck(80)) {
                for (int i = 0; i < 4; ++i) {
                    data.add(Enemy.create(env, 797, 10, 33333, 300000000, 1, getMonsterAttrs(env, 797), getMonsterTypes(env, 797)));
                    zero = data.get(i);

                    zero.attackMode = new EnemyAttackMode();


                    zero.attackMode.createSimpleAttackAction("", "", 100);


                }
            } else {
                data.add(Enemy.create(env, 3827, 500000000, 777777, 140, 5,getMonsterAttrs(env,3827),getMonsterTypes(env, 3827)));
                zero = data.get(0);

                zero.attackMode = new EnemyAttackMode();


                zero.attackMode.createSimpleAttackAction("", "", 100);

            }

        }
        mStageEnemies.put(8, data);


        data = new ArrayList<Enemy>();
        {
            int[] ids = {3487,3488,3489,3490,3491};
            for (int i = 0; i < 1; ++i) {

                int rnd = RandomUtil.getInt(ids.length);
                int choose = ids[rnd];
                //text = loadSubText(list, choose);
                if (choose == 3487) {
                    data.add(Enemy.create(env, 3487, 150000000, 58300, 260000, 1,getMonsterAttrs(env,3487),getMonsterTypes(env, 3487)));
                    zero = data.get(i);

                    zero.attackMode = new EnemyAttackMode();


                    zero.attackMode.createSimpleAttackAction("", "", 100);

                }
                if (choose == 3488) {
                    data.add(Enemy.create(env, 3488, 160000000, 54400, 320000, 1,getMonsterAttrs(env,3488),getMonsterTypes(env, 3488)));
                    zero = data.get(i);

                    zero.attackMode = new EnemyAttackMode();


                    zero.attackMode.createSimpleAttackAction("", "", 100);

                }
                if (choose == 3489) {
                    data.add(Enemy.create(env, 3489, 50000000, 93000, 16000000, 3,getMonsterAttrs(env,3489),getMonsterTypes(env, 3489)));
                    zero = data.get(i);

                    zero.attackMode = new EnemyAttackMode();


                    zero.attackMode.createSimpleAttackAction("", "", 100);

                }
                if (choose == 3490) {
                    data.add(Enemy.create(env, 3490, 140000000, 56300, 240000, 1,getMonsterAttrs(env,3490),getMonsterTypes(env, 3490)));
                    zero = data.get(i);

                    zero.attackMode = new EnemyAttackMode();


                    zero.attackMode.createSimpleAttackAction("", "", 100);

                }
                if (choose == 3491) {
                    data.add(Enemy.create(env, 3491, 130000000, 60100, 190000, 1,getMonsterAttrs(env,3491),getMonsterTypes(env, 3491)));
                    zero = data.get(i);

                    zero.attackMode = new EnemyAttackMode();


                    zero.attackMode.createSimpleAttackAction("", "", 100);

                }

            }

        }
        mStageEnemies.put(9, data);

        data = new ArrayList<Enemy>();
        {
            int[] ids = {4411,4413,4415,4648,4650};
            for (int i = 0; i < 1; ++i) {

                int rnd = RandomUtil.getInt(ids.length);
                int choose = ids[rnd];
                //text = loadSubText(list, choose);
                if (choose == 4411) {
                    data.add(Enemy.create(env, 4411, 1200000000, 150300, 0, 2,getMonsterAttrs(env,4411),getMonsterTypes(env, 4411)));
                    zero = data.get(i);

                    zero.attackMode = new EnemyAttackMode();


                    zero.attackMode.createSimpleAttackAction("", "", 100);

                }
                if (choose == 4413) {
                    data.add(Enemy.create(env, 4413, 1200000000, 150100, 0, 2,getMonsterAttrs(env,4413),getMonsterTypes(env, 4413)));
                    zero = data.get(i);

                    zero.attackMode = new EnemyAttackMode();


                    zero.attackMode.createSimpleAttackAction("", "", 100);

                }
                if (choose == 4415) {
                    data.add(Enemy.create(env, 4415, 1200000000, 150400, 0, 2,getMonsterAttrs(env,4415),getMonsterTypes(env, 4415)));
                    zero = data.get(i);

                    zero.attackMode = new EnemyAttackMode();


                    zero.attackMode.createSimpleAttackAction("", "", 100);

                }
                if (choose == 4648) {
                    data.add(Enemy.create(env, 4648, 1200000000, 150200, 0, 2,getMonsterAttrs(env,4648),getMonsterTypes(env, 4648)));
                    zero = data.get(i);

                    zero.attackMode = new EnemyAttackMode();


                    zero.attackMode.createSimpleAttackAction("", "", 100);

                }
                if (choose == 4650) {
                    data.add(Enemy.create(env, 4650, 1200000000, 150500, 0, 2,getMonsterAttrs(env,4650),getMonsterTypes(env, 4650)));
                    zero = data.get(i);

                    zero.attackMode = new EnemyAttackMode();


                    zero.attackMode.createSimpleAttackAction("", "", 100);

                }

            }

        }
        mStageEnemies.put(10, data);

        data = new ArrayList<Enemy>();
        {
            int[] ids = {3700,3701,3702,3703};
            for (int i = 0; i < 2; ++i) {

                int rnd = RandomUtil.getInt(ids.length);
                int choose = ids[rnd];
                //text = loadSubText(list, choose);
                if (choose == 3700) {
                    data.add(Enemy.create(env, 3700, 500000000, 44000, 1000000, 1,getMonsterAttrs(env,3700),getMonsterTypes(env, 3700)));
                    zero = data.get(i);

                    zero.attackMode = new EnemyAttackMode();


                    zero.attackMode.createSimpleAttackAction("", "", 100);

                }
                if (choose == 3701) {
                    data.add(Enemy.create(env, 3701, 500000000, 44000, 1000000, 1,getMonsterAttrs(env,3701),getMonsterTypes(env, 3701)));
                    zero = data.get(i);

                    zero.attackMode = new EnemyAttackMode();


                    zero.attackMode.createSimpleAttackAction("", "", 100);

                }
                if (choose == 3702) {
                    data.add(Enemy.create(env, 3702, 500000000, 44000, 1000000, 1,getMonsterAttrs(env,3702),getMonsterTypes(env, 3702)));
                    zero = data.get(i);

                    zero.attackMode = new EnemyAttackMode();


                    zero.attackMode.createSimpleAttackAction("", "", 100);

                }
                if (choose == 3703) {
                    data.add(Enemy.create(env, 3703, 500000000, 44000, 1000000, 1,getMonsterAttrs(env,3703),getMonsterTypes(env, 3703)));
                    zero = data.get(i);

                    zero.attackMode = new EnemyAttackMode();


                    zero.attackMode.createSimpleAttackAction("", "", 100);

                }

            }

        }
        mStageEnemies.put(11, data);


    }
}
