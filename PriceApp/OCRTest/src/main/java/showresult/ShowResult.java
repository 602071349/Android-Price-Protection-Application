package showresult;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

import edu.sfsu.cs.orange.ocr.R;

public class ShowResult extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_result);
        EditText et1=(EditText)findViewById(R.id.productname1);
        EditText et2=(EditText)findViewById(R.id.productprice1);
        EditText et3=(EditText)findViewById(R.id.purchasedate);
        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        et1.setText(bundle.getString("name"));
        et2.setText(bundle.getString("price"));
        et3.setText("11/25/2017");

    }
}
