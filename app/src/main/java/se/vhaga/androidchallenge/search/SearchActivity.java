package se.vhaga.androidchallenge.search;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import io.realm.Sort;
import se.vhaga.androidchallenge.R;
import se.vhaga.androidchallenge.network.models.CatImageModel;
import se.vhaga.androidchallenge.search.recycler.CatsGridAdapter;
import se.vhaga.androidchallenge.search.recycler.GridItemDecoration;

public class SearchActivity extends Activity {

    private static final int NUM_GRID_COLUMNS = 2;
    private static final int ASYNC_TASKS_STARTED = 3;

    private Realm realm;
    private int tasksFinished;

    private CatsGridAdapter catsAdapter;
    private RecyclerView catsRecycler;
    private ProgressBar loadingIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        catsRecycler = findViewById(R.id.catsRecycler);
        loadingIndicator = findViewById(R.id.loadingIndicator);

        initRecycler();

        // Reset realm
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().build();
        Realm.deleteRealm(realmConfiguration);
        realm = Realm.getInstance(realmConfiguration);

        String urlHats = "https://github.com/Beardstorm/cats-search/blob/master/resources/cats-hats.json?raw=true";
        String urlSinks = "https://github.com/Beardstorm/cats-search/blob/master/resources/cats-sinks.json?raw=true";
        String urlBoxes = "https://github.com/Beardstorm/cats-search/blob/master/resources/cats-boxes.json?raw=true";

        GetAndPersistJsonTask.OnTaskCompletedListener onTaskCompletedListener = new GetAndPersistJsonTask.OnTaskCompletedListener() {
            @Override
            public void onTaskCompleted() {
                SearchActivity.this.onTaskCompleted();
            }
        };

        GetAndPersistJsonTask getCatsInHatsTask = new GetAndPersistJsonTask(urlHats, onTaskCompletedListener);
        GetAndPersistJsonTask getCatsInBoxesTask = new GetAndPersistJsonTask(urlSinks, onTaskCompletedListener);
        GetAndPersistJsonTask getCatsInSinksTask = new GetAndPersistJsonTask(urlBoxes, onTaskCompletedListener);

        getCatsInHatsTask.execute();
        getCatsInBoxesTask.execute();
        getCatsInSinksTask.execute();

        loadingIndicator.setVisibility(View.VISIBLE);
    }

    private void initRecycler() {
        catsAdapter = new CatsGridAdapter();
        catsRecycler.setAdapter(catsAdapter);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, NUM_GRID_COLUMNS, OrientationHelper.VERTICAL, false);
        catsRecycler.setLayoutManager(gridLayoutManager);

        int margin = getResources().getDimensionPixelSize(R.dimen.grid_2x);
        catsRecycler.addItemDecoration(new GridItemDecoration(NUM_GRID_COLUMNS, margin));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }

    private void onTaskCompleted() {
        tasksFinished++;

        if (tasksFinished < ASYNC_TASKS_STARTED) {
            return;
        }

        loadingIndicator.setVisibility(View.GONE);

        RealmResults<CatImageModel> cats = realm.where(CatImageModel.class).findAll().sort("id", Sort.ASCENDING);

        catsAdapter.addCats(new ArrayList<>(cats));
        catsAdapter.notifyDataSetChanged();
    }
}
