package ng.codeinn.cryptoconvert;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ConvertActivity extends AppCompatActivity {

    TextView convertedTextView;
    View btcView;
    View ethView;
    double btcRate;
    double ethRate;
    Button convertButton;
    double totalAmount;
    EditText convertAmountEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_convert);

        btcView = (View) findViewById(R.id.btc_view_id);
        ethView = (View) findViewById(R.id.eth_view_id);

        if(getIntent().getExtras() != null){
            Bundle bundle = getIntent().getExtras();
            btcRate = bundle.getDouble("btcRate", 0.0);
            ethRate = bundle.getDouble("ethRate", 0.0);
        }

        btcView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showConvertDialog();
            }
        });

    }

    public void showConvertDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.convert_dialog, null);
        builder.setView(dialogView);

        convertButton = dialogView.findViewById(R.id.convert_button_id);

        convertAmountEdit = (EditText) dialogView.findViewById(R.id.convert_amount_edit);
        convertedTextView = (TextView) dialogView.findViewById(R.id.converted_id);


        convertAmountEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (s.toString().trim().length() > 0) {
                    convertButton.setEnabled(true);
                } else {
                    convertButton.setEnabled(false);
                }

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        convertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double convertAmount = Double.parseDouble(convertAmountEdit.getText().toString());
                totalAmount = convertAmount * btcRate;
                convertedTextView.setText(""+totalAmount);
                Log.i("amount:: ", ""+totalAmount);
            }
        });
        builder.setTitle("Convert");
        builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                convertAmountEdit.setText("");
                finish();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                convertAmountEdit.setText("");
                finish();
            }
        });
        builder.setMessage(""+totalAmount);

        AlertDialog b = builder.create();
        b.show();
    }
}
