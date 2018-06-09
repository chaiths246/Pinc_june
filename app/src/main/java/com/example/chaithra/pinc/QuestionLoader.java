package com.example.chaithra.pinc;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;
import com.example.chaithra.pinc.model.Question;
import java.util.ArrayList;

public class QuestionLoader extends AsyncTaskLoader<ArrayList<Question>> {
    public static final String LOG_TAG = QuestionLoader.class.getSimpleName();
    private String url;

    public QuestionLoader(Context context, String url) {
        super(context);
        this.url = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public ArrayList<Question> loadInBackground() {
        if (url == null) {
            return null;
        }
        ArrayList<Question> newslist = Utils.fetchQuestionData(url);
        if (newslist.size() == 0) {
            Log.d(LOG_TAG, "news list is empty");
        }
        return newslist;
    }
}
