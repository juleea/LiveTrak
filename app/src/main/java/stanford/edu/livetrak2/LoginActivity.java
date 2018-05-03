package stanford.edu.livetrak2;

import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;


public class LoginActivity extends AppCompatActivity {
    public static final String CODER_ID = "edu.stanford.livetraq.CODER_ID";
    public static final String CONFIG_FILE = "edu.stanford.livetraq.CONFIG_FILE";
    public static final String LANGUAGE = "edu.stanford.livetraq.LANGUAGE";
    public static final String SUBJECT_ID = "edu.stanford.livetraq.SUBJECT_ID";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!isExternalStorageWritable()) {
            new Builder(this).setMessage("App cannot run because external storage is not available. Please insert an SD card.").setNeutralButton("Ok", new OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    System.exit(0);
                }
            }).show();
        }
        setContentView(R.layout.activity_login);
    }

    public boolean isExternalStorageWritable() {
        if ("mounted".equals(Environment.getExternalStorageState())) {
            return true;
        }
        return false;
    }

    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.login, menu);
        return true;
    }

    private boolean isEmpty(EditText field) {
        return field.getText().toString().trim().isEmpty();
    }

    public void newSession(View v) {
        EditText coderId = (EditText) findViewById(R.id.coderId);
        if (isEmpty(coderId)) {
            coderId.setError("Please enter your ID");
            return;
        }
        EditText subjectId = (EditText) findViewById(R.id.subjectId);
        if (isEmpty(subjectId)) {
            subjectId.setError("Please enter the subject ID");
            return;
        }
        Intent intent = new Intent(this, SessionActivity.class);
        intent.putExtra(CODER_ID, coderId.getText().toString().trim());
        intent.putExtra(SUBJECT_ID, subjectId.getText().toString().trim());
        startActivity(intent);
    }
}
