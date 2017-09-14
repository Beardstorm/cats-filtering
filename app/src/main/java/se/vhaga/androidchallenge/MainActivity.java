package se.vhaga.androidchallenge;

import android.app.Activity;
import android.os.Bundle;

import se.vhaga.androidchallenge.network.NetworkClient;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NetworkClient networkClient = new NetworkClient();
        networkClient.run("http://thecatapi.com/api/images/get")
    }
}
