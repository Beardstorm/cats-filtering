package se.vhaga.catsfiltering.network.models;

import io.realm.RealmObject;

/**
 * Created by vhaga on 2017-09-14.
 */

public class CatImageModel extends RealmObject   {

    private String id;
    private String url;
    private String category;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
