package se.vhaga.catsfiltering.search;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import io.realm.Realm;
import se.vhaga.catsfiltering.network.models.CatImageModel;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

/**
 * Created by vhaga on 2017-09-14.
 */
public class CatListPresenterTest {

    @Mock
    CatListView view;
    @Mock
    Realm realm;

    private CatListPresenter presenter;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        doReturn(realm).when(view).getRealmInstance();

        presenter = new CatListPresenter(view);
    }

    @Test
    public void onCreate_hasNotPersistedDataBefore_executeTasks() throws Exception {
        doReturn(false).when(view).hasPersistedData();
        presenter.onCreate();

        verify(view).getRealmInstance();
        verify(view).clearRealmInstance();
        verify(view, times(3)).executePersistJsonTask(anyString());
        verify(view).showLoadingIndicator();
    }

    @Test
    public void onCreate_hasPersistedDataBefore_loadCats() throws Exception {
        doReturn(true).when(view).hasPersistedData();
        presenter.realm = realm;

        presenter.onCreate();

        verify(view).getRealmInstance();
        verify(view).loadCats(realm);
        verify(view, never()).clearRealmInstance();
        verify(view, never()).executePersistJsonTask(anyString());
        verify(view, never()).showLoadingIndicator();
    }

    @Test
    public void onDestroy() throws Exception {
        presenter.realm = realm;
        presenter.onDestroy();
        verify(realm).close();
    }

    @Test
    public void onPersistTaskCompleted_lessThanStartedTasks_doNothing() throws Exception {
        presenter.tasksFinished = 0;
        presenter.onPersistTaskCompleted();
        verifyNoMoreInteractions(view);
    }

    @Test
    public void onPersistTaskCompleted_allTasksCompleted_loadCats() throws Exception {
        presenter.realm = realm;
        presenter.tasksFinished = 2;

        presenter.onPersistTaskCompleted();
        verify(view).hideLoadingIndicator();
        verify(view).loadCats(realm);
    }

    @Test
    public void onFilterChanged() throws Exception {
        String filter = "something";

        presenter.realm = realm;
        presenter.onFilterChanged(filter);

        verify(view).filterCats(realm, filter);
    }

    @Test
    public void onCatClicked_showFullImage() throws Exception {
        CatImageModel cat = new CatImageModel();
        cat.setUrl("someurl");

        presenter.onCatClicked(cat);

        verify(view).showFullImage(cat.getUrl());
    }
}