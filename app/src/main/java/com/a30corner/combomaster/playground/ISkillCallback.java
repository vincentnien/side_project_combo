package com.a30corner.combomaster.playground;

public interface ISkillCallback {
    public void onFire(IEnvironment env);
    public void onCancel(IEnvironment env);
    public void onFire(IEnvironment env, int which);
}