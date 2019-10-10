package com.a30corner.combomaster.playground.stagedata;

import android.util.SparseArray;

import com.a30corner.combomaster.playground.IEnvironment;
import com.a30corner.combomaster.playground.enemy.EnemyAttackMode;
import com.a30corner.combomaster.playground.entity.Enemy;
import com.a30corner.combomaster.utils.RandomUtil;

import java.util.ArrayList;

public class ExtremePracticeArena extends IStage {

    @Override
    public void create(IEnvironment env,
                 SparseArray<ArrayList<Enemy>> mStageEnemies, String mStage) {
        ArrayList<Enemy> data;
        Enemy zero;
        mStageEnemies.clear();

        data = new ArrayList<Enemy>();
        {
            int[] ids = {46,48,50,52,54};
            int[] atk = {41200, 42000, 42960, 43800, 44800};
            for (int i = 0; i < ids.length; ++i) {

                data.add(Enemy.create(env, ids[i], 100000000, atk[i] , 10000000, 2,getMonsterAttrs(env,ids[i]),getMonsterTypes(env, ids[i])));
                zero = data.get(i);

                zero.attackMode = new EnemyAttackMode();
                zero.attackMode.createSimpleAttackAction("", "", 100);

            }

        }
        mStageEnemies.put(1, data);

        data = new ArrayList<Enemy>();
        {
            data.add(Enemy.create(env, 414, 301000000, 51800 , 50000000, 1,getMonsterAttrs(env,414),getMonsterTypes(env, 414)));
            zero = data.get(0);

            zero.attackMode = new EnemyAttackMode();
            zero.attackMode.createSimpleAttackAction("", "", 100);

            data.add(Enemy.create(env, 417, 306000000, 53600 , 50000000, 1,getMonsterAttrs(env,417),getMonsterTypes(env, 417)));
            zero = data.get(1);

            zero.attackMode = new EnemyAttackMode();
            zero.attackMode.createSimpleAttackAction("", "", 100);

        }
        mStageEnemies.put(2, data);

        data = new ArrayList<Enemy>();
        {
            data.add(Enemy.create(env, 428, 500000000, 95760, 10000000, 1,getMonsterAttrs(env,428),getMonsterTypes(env, 428)));
            zero = data.get(0);

            zero.attackMode = new EnemyAttackMode();
            zero.attackMode.createSimpleAttackAction("", "", 100);
        }
        mStageEnemies.put(3, data);

        data = new ArrayList<Enemy>();
        {
            data.add(Enemy.create(env, 4244, 1000000000, 136000, 10000000, 2,getMonsterAttrs(env,4244),getMonsterTypes(env, 4244)));
            zero = data.get(0);

            zero.attackMode = new EnemyAttackMode();
            zero.attackMode.createSimpleAttackAction("", "", 100);

        }
        mStageEnemies.put(4, data);

        data = new ArrayList<Enemy>();
        {
            int[] ids = {3318,3319,3320,3321,3322,3323,3324,3325,3700,3701,3702,3703};
            for (int i = 0; i < 2; ++i) {

                int rnd = RandomUtil.getInt(ids.length);
                int choose = ids[rnd];
                if(choose <= 3325) {
                    data.add(Enemy.create(env, choose, 500000000, 88000, 5000000, 1,getMonsterAttrs(env,choose),getMonsterTypes(env, choose)));
                    zero = data.get(i);

                    zero.attackMode = new EnemyAttackMode();
                    zero.attackMode.createSimpleAttackAction("", "", 100);
                } else {
                    data.add(Enemy.create(env, choose, 500000000, 88000, 1000000, 1,getMonsterAttrs(env,choose),getMonsterTypes(env, choose)));
                    zero = data.get(i);

                    zero.attackMode = new EnemyAttackMode();
                    zero.attackMode.createSimpleAttackAction("", "", 100);
                }
            }

        }
        mStageEnemies.put(5, data);

        data = new ArrayList<Enemy>();
        {
            int[] ids = {694,973,983,1271,1517};
            for (int i = 0; i < 1; ++i) {

                int rnd = RandomUtil.getInt(ids.length);
                int choose = ids[rnd];
                if (choose == 694) {
                    data.add(Enemy.create(env, choose, 200000000, 130400, 100000000, 2,getMonsterAttrs(env,choose),getMonsterTypes(env, choose)));
                    zero = data.get(i);

                    zero.attackMode = new EnemyAttackMode();
                    zero.attackMode.createSimpleAttackAction("", "", 100);
                } else if (choose == 973) {
                    data.add(Enemy.create(env, choose, 200000000, 131200, 100000000, 2,getMonsterAttrs(env,choose),getMonsterTypes(env, choose)));
                    zero = data.get(i);

                    zero.attackMode = new EnemyAttackMode();
                    zero.attackMode.createSimpleAttackAction("", "", 100);
                } else if (choose == 983) {
                    data.add(Enemy.create(env, choose, 200000000, 127800, 100000000, 2,getMonsterAttrs(env,choose),getMonsterTypes(env, choose)));
                    zero = data.get(i);

                    zero.attackMode = new EnemyAttackMode();
                    zero.attackMode.createSimpleAttackAction("", "", 100);
                } else if (choose == 1271) {
                    data.add(Enemy.create(env, choose, 200000000, 126600, 100000000, 2,getMonsterAttrs(env,choose),getMonsterTypes(env, choose)));
                    zero = data.get(i);

                    zero.attackMode = new EnemyAttackMode();
                    zero.attackMode.createSimpleAttackAction("", "", 100);
                } else {
                    data.add(Enemy.create(env, choose, 200000000, 129200, 100000000, 2,getMonsterAttrs(env,choose),getMonsterTypes(env, choose)));
                    zero = data.get(i);

                    zero.attackMode = new EnemyAttackMode();
                    zero.attackMode.createSimpleAttackAction("", "", 100);
                }
            }

        }
        mStageEnemies.put(6, data);

        data = new ArrayList<Enemy>();
        {
            data.add(Enemy.create(env, 4011, 700000000, 99600, 50000000, 1,getMonsterAttrs(env,4011),getMonsterTypes(env, 4011)));
            zero = data.get(0);

            zero.attackMode = new EnemyAttackMode();
            zero.attackMode.createSimpleAttackAction("", "", 100);
        }
        mStageEnemies.put(7, data);

        data = new ArrayList<Enemy>();
        {
            data.add(Enemy.create(env, 922, 50000000, 144000, 1000000000, 3,getMonsterAttrs(env,922),getMonsterTypes(env, 922)));
            zero = data.get(0);

            zero.attackMode = new EnemyAttackMode();
            zero.attackMode.createSimpleAttackAction("", "", 100);

        }
        mStageEnemies.put(8, data);

        data = new ArrayList<Enemy>();
        {
            int[] ids = {475,476,477,478,479};
            for (int i = 0; i < 2; ++i) {

                int rnd = RandomUtil.getInt(ids.length);
                int choose = ids[rnd];
                if (choose == 475) {
                    data.add(Enemy.create(env, choose, 324000000, 76800, 60000000, 1,getMonsterAttrs(env,choose),getMonsterTypes(env, choose)));
                    zero = data.get(i);

                    zero.attackMode = new EnemyAttackMode();
                    zero.attackMode.createSimpleAttackAction("", "", 100);
                } else if (choose == 476) {
                    data.add(Enemy.create(env, choose, 318000000, 63200, 60000000, 1,getMonsterAttrs(env,choose),getMonsterTypes(env, choose)));
                    zero = data.get(i);

                    zero.attackMode = new EnemyAttackMode();
                    zero.attackMode.createSimpleAttackAction("", "", 100);
                } else if (choose == 477) {
                    data.add(Enemy.create(env, choose, 306000000, 80600, 60000000, 1,getMonsterAttrs(env,choose),getMonsterTypes(env, choose)));
                    zero = data.get(i);

                    zero.attackMode = new EnemyAttackMode();
                    zero.attackMode.createSimpleAttackAction("", "", 100);
                } else if (choose == 478) {
                    data.add(Enemy.create(env, choose, 360000000, 69000, 60000000, 1,getMonsterAttrs(env,choose),getMonsterTypes(env, choose)));
                    zero = data.get(i);

                    zero.attackMode = new EnemyAttackMode();
                    zero.attackMode.createSimpleAttackAction("", "", 100);
                } else {
                    data.add(Enemy.create(env, choose, 330000000, 81400, 60000000, 1,getMonsterAttrs(env,choose),getMonsterTypes(env, choose)));
                    zero = data.get(i);

                    zero.attackMode = new EnemyAttackMode();
                    zero.attackMode.createSimpleAttackAction("", "", 100);
                }
            }

        }
        mStageEnemies.put(9, data);

        data = new ArrayList<Enemy>();
        {
            int[] ids = {3877,4995};
            for (int i = 0; i < 1; ++i) {

                int rnd = RandomUtil.getInt(ids.length);
                int choose = ids[rnd];
                if (choose == 3877) {
                    data.add(Enemy.create(env, choose, 700000000, 149000, 60000000, 2,getMonsterAttrs(env,choose),getMonsterTypes(env, choose)));
                    zero = data.get(i);

                    zero.attackMode = new EnemyAttackMode();
                    zero.attackMode.createSimpleAttackAction("", "", 100);
                } else {
                    data.add(Enemy.create(env, choose, 750000000, 152000, 10000000, 2,getMonsterAttrs(env,choose),getMonsterTypes(env, choose)));
                    zero = data.get(i);

                    zero.attackMode = new EnemyAttackMode();
                    zero.attackMode.createSimpleAttackAction("", "", 100);
                }
            }

        }
        mStageEnemies.put(10, data);

        data = new ArrayList<Enemy>();
        {
            int[] ids = {3638,3640,3642,3311};
            for (int i = 0; i < 1; ++i) {

                int rnd = RandomUtil.getInt(ids.length);
                int choose = ids[rnd];
                if (choose == 3638) {
                    data.add(Enemy.create(env, choose, 1500000000, 219000, 600000000, 1, getMonsterAttrs(env, choose), getMonsterTypes(env, choose)));
                    zero = data.get(i);

                    zero.attackMode = new EnemyAttackMode();
                    zero.attackMode.createSimpleAttackAction("", "", 100);
                } else if (choose == 3640) {
                        data.add(Enemy.create(env, choose, 2000000000, 223200, 100000000, 1,getMonsterAttrs(env,choose),getMonsterTypes(env, choose)));
                        zero = data.get(i);

                        zero.attackMode = new EnemyAttackMode();
                        zero.attackMode.createSimpleAttackAction("", "", 100);
                } else if (choose == 3642) {
                    data.add(Enemy.create(env, choose, 2000000000, 813600, 1000000000, 4,getMonsterAttrs(env,choose),getMonsterTypes(env, choose)));
                    zero = data.get(i);

                    zero.attackMode = new EnemyAttackMode();
                    zero.attackMode.createSimpleAttackAction("", "", 100);
                } else {
                    data.add(Enemy.create(env, choose, 2000000000, 291140, 500000000, 2,getMonsterAttrs(env,choose),getMonsterTypes(env, choose)));
                    zero = data.get(i);

                    zero.attackMode = new EnemyAttackMode();
                    zero.attackMode.createSimpleAttackAction("", "", 100);
                }
            }

        }
        mStageEnemies.put(11, data);


    }
}
