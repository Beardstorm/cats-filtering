package se.vhaga.catsfiltering.search;

import io.realm.Realm;

/**
 * Created by vhaga on 2017-09-14.
 */

interface CatListView {

    Realm getRealmInstance();
    void clearRealmInstance();
    void executePersistJsonTask(String urlHats);
    void showLoadingIndicator();
    void hideLoadingIndicator();
    void loadCats(Realm realm);
    void filterCats(Realm realm, String string);
    void showFullImage(String url);
}
