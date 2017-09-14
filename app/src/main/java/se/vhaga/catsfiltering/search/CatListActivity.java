package se.vhaga.catsfiltering.search;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import io.realm.Sort;
import se.vhaga.catsfiltering.R;
import se.vhaga.catsfiltering.network.models.CatImageModel;
import se.vhaga.catsfiltering.search.recycler.CatsGridAdapter;
import se.vhaga.catsfiltering.search.recycler.GridItemDecoration;

public class CatListActivity extends Activity implements CatListView {

    private static final int NUM_GRID_COLUMNS = 2;

    private CatListPresenter presenter;

    private TextView fieldSearch;
    private CatsGridAdapter catsAdapter;
    private RecyclerView catsRecycler;
    private ProgressBar loadingIndicator;
    private ImageView fullImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        fieldSearch = findViewById(R.id.searchField);
        catsRecycler = findViewById(R.id.catsRecycler);
        loadingIndicator = findViewById(R.id.loadingIndicator);
        fullImage = findViewById(R.id.fullImage);
        fullImage.setOnClickListener(onFullImageClickedListener);

        initRecycler();
        initFieldFilters();

        presenter = new CatListPresenter(this);
        presenter.onCreate();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }

    private void initRecycler() {
        catsAdapter = new CatsGridAdapter();
        catsAdapter.setOnCatClickListener(onCatClickListener);

        catsRecycler.setAdapter(catsAdapter);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, NUM_GRID_COLUMNS, OrientationHelper.VERTICAL, false);
        catsRecycler.setLayoutManager(gridLayoutManager);

        int margin = getResources().getDimensionPixelSize(R.dimen.grid_2x);
        catsRecycler.addItemDecoration(new GridItemDecoration(NUM_GRID_COLUMNS, margin));
    }

    private void initFieldFilters() {
        fieldSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                presenter.onFilterChanged(editable.toString());
            }
        });
    }

    @Override
    public void clearRealmInstance() {
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().build();
        Realm.deleteRealm(realmConfiguration);
    }

    @Override
    public Realm getRealmInstance() {
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().build();
        return Realm.getInstance(realmConfiguration);
    }

    @Override
    public void executePersistJsonTask(String url) {

        GetAndPersistJsonTask.OnTaskCompletedListener onTaskCompletedListener = new GetAndPersistJsonTask.OnTaskCompletedListener() {
            @Override
            public void onTaskCompleted() {
                presenter.onPersistTaskCompleted();
            }
        };

        GetAndPersistJsonTask persistJsonTask = new GetAndPersistJsonTask(url, onTaskCompletedListener);
        persistJsonTask.execute();
    }

    @Override
    public void showLoadingIndicator() {
        loadingIndicator.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoadingIndicator() {
        loadingIndicator.setVisibility(View.GONE);
    }

    @Override
    public void loadCats(Realm realm) {
        RealmResults<CatImageModel> cats = realm.where(CatImageModel.class)
                .findAll()
                .sort("id", Sort.ASCENDING);

        catsAdapter.setCats(new ArrayList<>(cats));
        catsAdapter.notifyDataSetChanged();
    }

    @Override
    public void filterCats(Realm realm, String string) {
        RealmResults<CatImageModel> cats = realm.where(CatImageModel.class)
                .contains("category", string)
                .findAll()
                .sort("id", Sort.ASCENDING);

        catsAdapter.setCats(cats);
        catsAdapter.notifyDataSetChanged();
    }

    @Override
    public void showFullImage(String url) {
        Picasso.with(this).load(url).into(fullImage);
        fullImage.setVisibility(View.VISIBLE);
    }

    private View.OnClickListener onFullImageClickedListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            fullImage.setVisibility(View.GONE);
        }
    };

    private CatsGridAdapter.OnCatClickedListener onCatClickListener = new CatsGridAdapter.OnCatClickedListener() {
        @Override
        public void onCatClicked(CatImageModel cat) {
            presenter.onCatClicked(cat);
        }
    };
}
