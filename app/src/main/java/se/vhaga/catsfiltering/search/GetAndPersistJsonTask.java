package se.vhaga.catsfiltering.search;

import android.os.AsyncTask;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import se.vhaga.catsfiltering.network.CatsResponseParser;
import se.vhaga.catsfiltering.network.NetworkClient;
import se.vhaga.catsfiltering.network.models.CatImageModel;

/**
 * Created by vhaga on 2017-09-14.
 */
public class GetAndPersistJsonTask extends AsyncTask {

    private final String url;
    private final NetworkClient networkClient;
    private OnTaskCompletedListener onCompletedListener;

    public GetAndPersistJsonTask(String url, OnTaskCompletedListener onCompletedListener) {
        this.onCompletedListener = onCompletedListener;
        this.networkClient = new NetworkClient();
        this.url = url;
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        String result = networkClient.run(url);

        List<CatImageModel> catImages = CatsResponseParser.parseCatsResponse(result);

        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().build();
        Realm realm = Realm.getInstance(realmConfiguration);
        realm.beginTransaction();
        realm.copyToRealm(catImages);
        realm.commitTransaction();
        realm.close();
        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        onCompletedListener.onTaskCompleted();
    }

    public interface OnTaskCompletedListener {
        void onTaskCompleted();
    }
}