package CryptocurrencyApp.Controller;

import org.apache.http.NameValuePair;

import java.util.List;
import java.util.Map;

/**
 * This class is used by the View layer to communicate with the other classes in the CryptocurrencyApp package
 */
public class Facade{
    private ListData data;
    private Report report;

    public Facade(ListData data, Report report){
        this.data = data;
        this.report = report;
    }


    /**
     * Retrieves the latest data of the cryptocurrencies, then retrieves the symbol and name of each cryptocurrency.<br><br>
     * <b>Preconditions:</b><br>
     * None<br>
     * <b>Postconditions:</b><br>
     * If the instance uses online data, the data will be refreshed to the latest data pulled from the CoinMarketCap API.
     * If the instance uses offline data, the data will remain the same.
     *
     * @return A Map that will either contain the success string "OK" as the key and the List of symbols and names as the value,
     * or will contain an error message as the key and null as the value.
     */
    public Map<String,List<NameValuePair>> listSymbolName(){
        return this.data.listSymbolName();
    }

    /**
     * Retrieves the latest data of the cryptocurrencies, then creates and returns a new EntityData containing information on the currency of interest.<br><br>
     * <b>Preconditions:</b><br>
     * None<br>
     * <b>Postconditions:</b><br>
     * If the instance uses online data, the latest data will be pulled from the CoinMarketCap API.
     * If the instance uses offline data, dummy data will be generated.
     *
     * @param symbol the symbol of the currency of interest
     * @param isCached a boolean that determines if the method retrieves cached data or the latest data.
     * @return A Map that will either contain the success string "OK" as the key and a new EntityData object containing information on the currency of interest,
     * or will contain an error message as the key and null as the value.
     */
    public Map<String, EntityData> getCurrencyInfo(String symbol, boolean isCached){
        return this.data.getCurrencyInfo(symbol, isCached);
    }

    /**
     * Retrieves the latest data of the cryptocurrencies, then converts amount from one currency to another.<br><br>
     * <b>Preconditions:</b><br>
     * None<br>
     * <b>Postconditions:</b><br>
     * If the instance uses online data, the latest data will be pulled from the CoinMarketCap API.
     * If the instance uses offline data, dummy data will be generated.
     *
     * @param from the symbol of the currency to convert from
     * @param to the symbol of the currency to convert to
     * @param amount the amount to be converted
     * @param isCached a boolean that determines if the method retrieves cached data or the latest data.
     * @return A Map that will either contain the success string "OK" as the key and the converted amount as the value,
     * or will contain an error message as the key and null as the value.
     */
    public Map<String, Double> conversion(String from, String to, String amount, boolean isCached) throws NumberFormatException{
        return this.data.conversion(from,to,amount,isCached);
    }

    /**
     * Initialises a new ListData instance for dummy (offline) data.<br><br>
     * <b>Preconditions:</b><br>
     * None<br>
     * <b>Postconditions:</b><br>
     * If data is offline, the url generated will not be accessible as it is a dummy.
     *
     * @param currencyData an EntityData object that contains the currency's information.
     * @param title A String that will be shown as the title of the report i.e. the currency symbol
     * @return a supposed url of the pastebin report
     */
    public Map<String, String> getReportLink(EntityData currencyData, String title){
        return this.report.getReportLink(currencyData,title);
    }

    /**
     * Sets the merchant fee percentage of the ListData object.<br><br>
     * <b>Preconditions:</b><br>
     * None<br>
     * <b>Postconditions:</b><br>
     * If the parameter is valid, the entered value will become the merchant fee that will be used in the currency conversions<br>
     *
     * @param fee the percentage value that the user has entered
     * @return if the fee entered was successfully set as the merchant fee or not
     */
    public boolean setFee(String fee){
        return this.data.setFee(fee);
    }

}
