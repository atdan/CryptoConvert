package ng.codeinn.cryptoconvert;


/**
 * Created by Jer on 11/2/2017.
 */

public class MyCurrency {

    private String currencyName;
    private String currencyID;
    private String btcRate;
    private String ethRate;

    public MyCurrency(String currencyName, String currencyID, String btcRate, String ethRate) {
        this.currencyName = currencyName;
        this.currencyID = currencyID;
        this.btcRate = btcRate;
        this.ethRate = ethRate;
    }

    public String getCurrencyName() {
        return currencyName;
    }

    public String getCurrencyID() {
        return currencyID;
    }

    public String getBtcRate() {
        return btcRate;
    }

    public String getEthRate() {
        return ethRate;
    }
}
