package com.example.android0211.Refactor.Util.Game.EnglishGame;   //토익 단어 공부용

import com.example.android0211.R;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by rudgn on 2019-01-10.
 */

public class FileTable2 {

    InputStream fi;

    FileSplit0 word;

    // 단어공부 파일 읽어오기
    public void loadFile(int num) {

        fi = StudyView3.mContext.getResources().openRawResource(R.raw.toeic01 + num-1);

        try {
            byte[] data = new byte[fi.available()];
            fi.read(data);
            fi.close();
            String s = new String(data, "UTF-8");
            word = new FileSplit0(s);

        } catch (IOException e) {
        }

    }

}
