package ng.codeinn.cryptoconvert;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Jer on 11/2/2017.
 */

public class CurrencyAdapter extends ArrayAdapter {
    public CurrencyAdapter(@NonNull Context context, @NonNull List objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null){
            listItemView = LayoutInflater
                    .from(getContext())
                    .inflate(R.layout.each_currency, parent, false);

        }

        MyCurrency currentCurrency = (MyCurrency) getItem(position);

        String currencyName = currentCurrency.getCurrencyName();
        String currencyID = "("+currentCurrency.getCurrencyID()+")";
        String btcRate = currentCurrency.getBtcRate();
        String ethRate = currentCurrency.getEthRate();

        TextView currencyNameTextView = (TextView) listItemView.findViewById(R.id.currency_name);
        TextView currencyIDTextView = (TextView) listItemView.findViewById(R.id.currency_id);
        TextView currencyBTCTectView = (TextView) listItemView.findViewById(R.id.btc_rate);
        TextView currencyETHTextView = (TextView) listItemView.findViewById(R.id.eth_rate);


        currencyNameTextView.setText(currencyName);
        currencyIDTextView.setText(currencyID);
        currencyBTCTectView.setText(btcRate);
        currencyETHTextView.setText(ethRate);

        return listItemView;
    }
}
