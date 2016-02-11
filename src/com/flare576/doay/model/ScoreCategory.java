package com.flare576.doay.model;

/**
 * Created by Flare576 on 1/22/2016.
 */
public enum ScoreCategory {
    CHANCE (0),
    ONES (1),
    TWOS (2),
    THREES (3),
    FOURS (4),
    FIVES (5),
    SIXES (6),
    SEVENS (7),
    EIGHTS (8),
    ALLDIFFERENT (0),
    ALLSAME (0),
    FOUROFAKIND (0),
    FULLHOUSE (0),
    LARGESTRAIGHT (0),
    SMALLSTRAIGHT (0),
    THREEOFAKIND (0);

    final Integer value;
    final boolean basic;
    ScoreCategory(int val){
        this.value = val;
        basic = value > 0;
    }

    public Integer getValue() {
        return value;
    }

    public boolean isBasic() {
        return basic;
    }
}