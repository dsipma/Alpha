package net.squidmonkey.Alpha;

import android.app.Activity;
import android.os.Bundle;

public class Game extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(new InGame(this));
    }
}
