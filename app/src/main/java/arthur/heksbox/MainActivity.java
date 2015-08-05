package arthur.heksbox;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class MainActivity extends Activity implements View.OnClickListener {


    @Override

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Button newgrid = null;
        Button resume = null;

        setContentView(R.layout.activity_main);
        // On aurait très bien pu utiliser « setContentView(R.layout.activity_main) » bien sûr !
        newgrid = (Button) findViewById(R.id.newgrid);

        newgrid.setOnClickListener(this);
    }


    public void onClick(View v) {
        // On récupère l'identifiant de la vue, et en fonction de cet identifiant…
        switch (v.getId()) {

            // Si l'identifiant de la vue est celui du premier bouton
            case R.id.newgrid:
                Intent intent = new Intent(MainActivity.this, CreateGrid.class);
                startActivity(intent);
                break;

        }
    }

}

