package se.vhaga.androidchallenge.search;

import io.realm.Realm;

/**
 * Created by vhaga on 2017-09-14.
 */

public class CatListPresenter {

    private static final int ASYNC_TASKS_STARTED = 3;

    private CatListView view;
    private Realm realm;
    private int tasksFinished;

    public CatListPresenter(CatListView view) {
        this.view = view;
    }

    public void onCreate() {

        view.clearRealmInstance();

        realm = view.getRealmInstance();

        String urlHats = "https://github.com/Beardstorm/cats-search/blob/master/resources/cats-hats.json?raw=true";
        String urlSinks = "https://github.com/Beardstorm/cats-search/blob/master/resources/cats-sinks.json?raw=true";
        String urlBoxes = "https://github.com/Beardstorm/cats-search/blob/master/resources/cats-boxes.json?raw=true";

        GetAndPersistJsonTask.OnTaskCompletedListener onTaskCompletedListener = new GetAndPersistJsonTask.OnTaskCompletedListener() {
            @Override
            public void onTaskCompleted() {
                onPersistTaskCompleted();
            }
        };

        GetAndPersistJsonTask getCatsInHatsTask = new GetAndPersistJsonTask(urlHats, onTaskCompletedListener);
        GetAndPersistJsonTask getCatsInBoxesTask = new GetAndPersistJsonTask(urlSinks, onTaskCompletedListener);
        GetAndPersistJsonTask getCatsInSinksTask = new GetAndPersistJsonTask(urlBoxes, onTaskCompletedListener);

        getCatsInHatsTask.execute();
        getCatsInBoxesTask.execute();
        getCatsInSinksTask.execute();

        view.showLoadingIndicator();
    }

    public void onDestroy() {
        realm.close();
    }

    public void onPersistTaskCompleted() {
        tasksFinished++;

        if (tasksFinished < ASYNC_TASKS_STARTED) {
            return;
        }

        view.hideLoadingIndicator();
        view.loadCats(realm);
    }

    public void onFilterChanged(String string) {
        view.filterCats(realm, string);
    }
}
