package com.example.capstonedesign.MySQLite;

import android.provider.BaseColumns;

public final class DataBases {

    public static final class CreateDB implements BaseColumns {
        public static final String IMAGE_SRC= "profilePhoto";
        public static final String MESSAGE_TITLE = "title";
        public static final String MESSAGE_CONTENT = "content";
        public static final String _TABLENAME0 = "messagetable";
        public static final String _CREATE0 = "create table if not exists "+_TABLENAME0+"("
                +_ID+ " integer primary key autoincrement, "
                +IMAGE_SRC+" text not null , "
                +MESSAGE_TITLE+" text not null , "
                +MESSAGE_CONTENT+" text not null );";
    }
}
