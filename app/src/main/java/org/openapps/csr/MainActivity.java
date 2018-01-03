package org.openapps.csr;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    Intent intent;
    public static final String EXTRA_MESSAGE = "org.openapps.csr";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void searchResult(View view) {
        intent = new Intent(this, DisplayResult.class);
        EditText editText = findViewById(R.id.editText_Usn);
        String usn = editText.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, usn);
        startActivity(intent);
    }
}
