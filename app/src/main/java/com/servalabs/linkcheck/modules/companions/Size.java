package com.servalabs.linkcheck.modules.companions;

import com.servalabs.linkcheck.R;
import com.servalabs.linkcheck.utilities.Enums;

/** A generic size enum */
public enum Size implements Enums.IdEnum, Enums.StringEnum {
    NONE(0, R.string.none),
    SMALL(1, R.string.small),
    NORMAL(2, R.string.normal),
    BIG(3, R.string.big),
    ;

    private final int id;
    private final int string;

    Size(int id, int string) {
        this.id = id;
        this.string = string;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public int getStringResource() {
        return string;
    }
}
