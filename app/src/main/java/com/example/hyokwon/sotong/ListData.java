package com.example.hyokwon.sotong;

import java.text.Collator;
import java.util.Comparator;

import android.graphics.drawable.Drawable;

public class ListData {

    public String mTitle;
    public String mGle;
    public String mDate;
    public String mName;
    public Drawable mIcon;
    public String mStat;

    public static final Comparator<ListData> ALPHA_COMPARATOR = new Comparator<ListData>() {
        private final Collator sCollator = Collator.getInstance();

        @Override
        public int compare(ListData mListDate_1, ListData mListDate_2) {
            return sCollator.compare(mListDate_1.mTitle, mListDate_2.mTitle);
        }
    };
}