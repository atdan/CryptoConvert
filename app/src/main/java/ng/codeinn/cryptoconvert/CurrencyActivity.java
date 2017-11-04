package ng.codeinn.cryptoconvert;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class CurrencyActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, LoaderManager.LoaderCallbacks<List<MyCurrency>> {

    private CurrencyAdapter mCurrencyAdapter;
    private String coinID;
    private String currencyRequetUrl;
    private TextView mEmptyTextView;
    private static final int CURRENCY_LOADER_ID = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_currency);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ListView currencyListView = (ListView) findViewById(R.id.list);
        mEmptyTextView = (TextView) findViewById(R.id.empty_text);

        currencyRequetUrl = "https://min-api.cryptocompare.com/data/pricemulti?fsyms=BTC,ETH&tsyms=USD,EUR,NGN,MXN,COP,JPY,ILS,GBP,CNY,BRL,CAD,ZAR,RUB,EGP,INR,TRY,SEK,MTL,ARS,KHR";

        ConnectivityManager connectManager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(CURRENCY_LOADER_ID, null, this);
        } else {
            View loadingIndicator = findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.GONE);
            mEmptyTextView.setText(R.string.no_internet_connection);
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(true);

        currencyListView.setEmptyView(mEmptyTextView);

        mCurrencyAdapter = new CurrencyAdapter(this, new ArrayList<MyCurrency>());

        currencyListView.setAdapter(mCurrencyAdapter);

        currencyListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MyCurrency currentCurrency = (MyCurrency) mCurrencyAdapter.getItem(position);
                double btcRate = Double.parseDouble(currentCurrency.getBtcRate());
                double ethRate = Double.parseDouble(currentCurrency.getEthRate());

                Intent convertIntent = new Intent(view.getContext(), ConvertActivity.class);
                convertIntent.putExtra("btcRate", btcRate);
                convertIntent.putExtra("ethRate", ethRate);
                startActivity(convertIntent);
            }
        });


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.currency, menu);
        return true;
    }

    @Override
    public Loader<List<MyCurrency>> onCreateLoader(int i, Bundle bundle) {

        return new CurrencyLoader(this, currencyRequetUrl);
    }
    @Override
    public void onLoadFinished(Loader<List<MyCurrency>> loader, List<MyCurrency> myCurrencies) {
        View loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);

        mEmptyTextView.setText(R.string.nothing_to_see_here);

        mCurrencyAdapter.clear();

        if (myCurrencies != null && !myCurrencies.isEmpty()){
            mCurrencyAdapter.addAll(myCurrencies);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<MyCurrency>> loader) {
        mCurrencyAdapter.clear();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_btc) {
            // TODO: GO TO THE BTC ACTIVITY
        } else if (id == R.id.nav_eth) {
            // TODO: GO TO THE ETH ACTIVITY
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}
