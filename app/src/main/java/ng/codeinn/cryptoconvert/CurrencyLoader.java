package ng.codeinn.cryptoconvert;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

import ng.codeinn.cryptoconvert.NetworkUtils.QueryUtils;

/**
 * Created by Jer on 11/2/2017.
 */

public class CurrencyLoader extends AsyncTaskLoader {
    private String mCurrencyUrl;
    public CurrencyLoader(Context context, String currencyUrl) {
        super(context);
        mCurrencyUrl = currencyUrl;
    }

    @Override
    protected void onStartLoading() {

        forceLoad();
    }

    @Override
    public Object loadInBackground() {
        if (mCurrencyUrl == null){
            return null;
        }
        List<MyCurrency> currencies = QueryUtils.fetchCurrencyData(mCurrencyUrl, getContext());
        return currencies;
    }
}
