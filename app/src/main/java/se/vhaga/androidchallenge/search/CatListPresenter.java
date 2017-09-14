package se.vhaga.androidchallenge.search;

import io.realm.Realm;
import se.vhaga.androidchallenge.network.models.CatImageModel;

/**
 * Created by vhaga on 2017-09-14.
 */

public class CatListPresenter {

    private static final int ASYNC_TASKS_STARTED = 3;

    protected CatListView view;
    protected Realm realm;
    protected int tasksFinished;

    public CatListPresenter(CatListView view) {
        this.view = view;
    }

    public void onCreate() {

        view.clearRealmInstance();
        realm = view.getRealmInstance();

        String urlHats = "https://github.com/Beardstorm/cats-search/blob/master/resources/cats-hats.json?raw=true";
        String urlSinks = "https://github.com/Beardstorm/cats-search/blob/master/resources/cats-sinks.json?raw=true";
        String urlBoxes = "https://github.com/Beardstorm/cats-search/blob/master/resources/cats-boxes.json?raw=true";

        view.executePersistJsonTask(urlHats);
        view.executePersistJsonTask(urlSinks);
        view.executePersistJsonTask(urlBoxes);
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

    public void onCatClicked(CatImageModel cat) {
        view.showFullImage(cat.getUrl());
    }
}
