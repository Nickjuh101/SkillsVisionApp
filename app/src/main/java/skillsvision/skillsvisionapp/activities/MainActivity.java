package skillsvision.skillsvisionapp.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import skillsvision.skillsvisionapp.R;

public class MainActivity extends AppCompatActivity {

    /** The view holder for this class **/
    private static Holder mHolder = new Holder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        mHolder.mButtonOpenMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mapsIntent = new Intent(MainActivity.this, MapsActivity.class);
                startActivity(mapsIntent);
            }
        });

    }

    private void initViews(){
        mHolder.mButtonOpenMap = (Button)findViewById(R.id.activity_main_open_map_activity_button);
    }


    private static class Holder {
        Button mButtonOpenMap;
    }
}
