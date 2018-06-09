package com.example.chaithra.pinc.view;

import android.app.LoaderManager;
import android.content.Loader;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.chaithra.pinc.PlaceholderFragment;
import com.example.chaithra.pinc.model.Question;
import com.example.chaithra.pinc.adapter.QuestionAdapter;
import com.example.chaithra.pinc.QuestionLoader;
import com.example.chaithra.pinc.R;
import com.example.chaithra.pinc.Utils;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class PincActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<Question>> {
    int URL_NEXT = 1;
    private static final int QUESTION_LOADER_ID = 1;
    ListView listView;
    QuestionAdapter questionAdapter;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private CircleImageView avataraTab;
    private int preLast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pinc);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        listView = (ListView) findViewById(R.id.list);
        avataraTab = (CircleImageView) findViewById(R.id.avatar_main);
        avataraTab.setImageResource(R.drawable.my_ppic);
        questionAdapter = new QuestionAdapter(this, 0);
        getSupportLoaderManager();
        listView.setAdapter(questionAdapter);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        listView.setAdapter(questionAdapter);

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        final android.app.LoaderManager loaderManager = getLoaderManager();
        loaderManager.initLoader(QUESTION_LOADER_ID, null, this);

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                switch (listView.getId()) {
                    case R.id.list:
                        final int lastItem = firstVisibleItem + visibleItemCount;

                        if (lastItem == totalItemCount) {
                            if (preLast != lastItem) {
                                //to avoid multiple calls for last item
                                preLast = lastItem;
                                if (URL_NEXT <= Utils.PAGE_URL_NUM) {
                                    URL_NEXT++;
                                    loaderManager.restartLoader(QUESTION_LOADER_ID, null, PincActivity.this);
                                    Toast.makeText(PincActivity.this, "Page: " + URL_NEXT, Toast.LENGTH_SHORT).show();
                                }

                            }
                        }
                }
            }
        });
    }

    private String getUrlBuilder() {
        StringBuilder strbul = new StringBuilder();
        strbul.append("https://staging-api.pincapp.com/api/questions?page%5Bnumber%5D=");
        strbul.append(URL_NEXT);
        strbul.append("\\u0026page%5Bsize%5D=20");
        return strbul.toString();
    }

    @Override
    public Loader<ArrayList<Question>> onCreateLoader(int id, Bundle args) {
        return new QuestionLoader(this, getUrlBuilder());
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Question>> loader, ArrayList<Question> data) {
//        bookAdapter.clear();
        if (data != null) {
            questionAdapter.addAll(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Question>> loader) {
        if (questionAdapter != null) {
            questionAdapter.clear();
        }
    }


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            return 3;
        }


    }
}
