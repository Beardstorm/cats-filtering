package se.vhaga.catsfiltering;

import android.app.Application;

import io.realm.Realm;

/**
 * Created by vhaga on 2017-09-14.
 */
public class CatsApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
    }
}