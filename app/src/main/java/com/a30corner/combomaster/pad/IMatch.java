package com.a30corner.combomaster.pad;

interface IMatch {
    int type();
    int count();

    boolean isHeartColumn();
    boolean isLFormat();
    boolean isOneRow();
    boolean isTwoWay();
    boolean isCross();
    boolean isSquare();
}
