package ca.nismit.simplerssreader.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

import ca.nismit.simplerssreader.R;
import ca.nismit.simplerssreader.adapter.MainAdapter;
import ca.nismit.simplerssreader.observer.BackgroundGetFeed;
import ca.nismit.simplerssreader.rss.RssItem;

public class MainFragment extends Fragment {
    static final String TAG = MainFragment.class.getSimpleName();

    BackgroundGetFeed backgroundGetFeed = new BackgroundGetFeed();
    MainAdapter mainAdapter;
    ListView mListView;

    public MainFragment() {
    }

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "Started onCreateView");
        View v = inflater.inflate(R.layout.fragment_main, container, false);
        mListView = (ListView) v.findViewById(R.id.f_listview);
        init();
        return v;
    }

    @Override
    public void onStart() {
        Log.d(TAG, "Started onStart");
        super.onStart();
        kicks();
    }

    void init() {
        Log.d(TAG, "Started init()");
        mainAdapter = new MainAdapter(getContext());
    }

    private void kicks() {
        backgroundGetFeed.addObserver(observer);
        backgroundGetFeed.taskStart("http://android-developers.blogspot.com/atom.xml");
        //backgroundGetFeed.taskStart("https://www.smashingmagazine.com/feed/");
        //backgroundGetFeed.taskStart("http://gihyo.jp/design/feed/atom");
    }

    private void setResult(List<RssItem> items) {
        mainAdapter.setMainAdapater(items);
        mListView.setAdapter(mainAdapter);
    }

    private Observer observer = new Observer() {
        @Override
        public void update(Observable o, Object arg) {
            if(!(arg instanceof BackgroundGetFeed.Event)){
                return;
            }

            switch ((BackgroundGetFeed.Event)arg) {
                case START:
                    break;
                case FINISH:
                    Log.d(TAG, "GET DATA");
                    setResult(backgroundGetFeed.getItems());
                    break;
            }
        }
    };
}