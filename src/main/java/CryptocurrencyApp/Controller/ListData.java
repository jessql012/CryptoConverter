package CryptocurrencyApp.Controller;

import CryptocurrencyApp.BadRequestException;
import CryptocurrencyApp.Model.Database;
import CryptocurrencyApp.Model.RequestMaker;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * This class communicates with RequestMaker to make HTTP requests, communicates with Database to send SQL Database queries,
 * formats the returned data, and handles exceptions.
 */
public class ListData {
    private String uri = "https://pro-api.coinmarketcap.com/v1/";
    private String allCryptoLatest = "cryptocurrency/listings/latest";
    private String apiKey;

    private boolean online;
    private RequestMaker requestMaker;
    private Database database;

    private double fee;


    /**
     * Initialises a new ListData instance that will call from the CoinMarketCap API (online) to retrieve data.<br><br>
     * <b>Preconditions:</b><br>
     * None<br>
     * <b>Postconditions:</b><br>
     * This instance will pull data from the CoinMarketCap API.
     *
     * @param apiKey unique API key for CoinMarketCap
     * @param requestMaker a RequestMaker object that will be used to send GET requests to the API.
     * @param database a Database object that will be used to cache and retrieve data from.
     */
    public ListData(String apiKey, RequestMaker requestMaker, Database database){
        this.online = true;
        this.apiKey = apiKey;
        this.requestMaker = requestMaker;
        this.database = database;
    }

    /**
     * Initialises a new ListData instance for dummy (offline) data.<br><br>
     * <b>Preconditions:</b><br>
     * None<br>
     * <b>Postconditions:</b><br>
     * This instance will generate dummy data.
     *
     * @param database a Database object that will be used to cache and retrieve data from.
     */
    public ListData(Database database){
        this.online = false;
        this.database = database;
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
        Map<String,List<NameValuePair>> returnedMap = new HashMap<String,List<NameValuePair>>();
        //Retrieve the latest data from API, or dummy data if offline
        Map<String, JSONArray> dataMap = new HashMap<String, JSONArray>();
        if(online){
            dataMap = getAllCryptoData();
        }else{
            dataMap.put("OK",makeDummyData());
        }
        //If data retrieved successfully, retrieve symbol and name of each currency
        for(Map.Entry<String, JSONArray> entry : dataMap.entrySet()){
            //Success message "OK"
            if(entry.getKey().equals("OK")){
                JSONArray data = entry.getValue();
                List<NameValuePair> list = new ArrayList<NameValuePair>();
                for (int i = 0; i < data.length(); i++) {
                    list.add(new BasicNameValuePair(data.getJSONObject(i).optString("symbol"), data.getJSONObject(i).optString("name")));
                }
                returnedMap.put("OK",list);
            }
            //Data retrieval was unsuccessful (not "OK")
            else {
                //Return with error message
                returnedMap.put(entry.getKey(),null);
            }
        }
        return returnedMap;
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
        Map<String, EntityData> returnedMap = new HashMap<String, EntityData>();
        if(!isCached){
            Map<String, JSONArray> dataMap = new HashMap<String, JSONArray>();
            //Retrieve the latest data from API, or dummy data if offline
            if(online){
                dataMap = getAllCryptoData();
            }else{
                dataMap.put("OK",makeDummyData());
            }
            for(Map.Entry<String, JSONArray> entry : dataMap.entrySet()){
                //Success message "OK"
                if(entry.getKey().equals("OK")){
                    JSONArray data = entry.getValue();
                    if(!this.database.cacheNewData(data)){
                        returnedMap.put("Error: Was unable to cache data",null);
                        return returnedMap;
                    }
                    //Find the index of the currency of interest in the data retrieved
                    int indexOfInterest = 0;
                    for (int i = 0; i < data.length(); i++) {
                        if (data.getJSONObject(i).optString("symbol").equals(symbol)){
                            indexOfInterest = i;
                        }
                    }
                    //use the JSONObject at the index to create a new EntityData object
                    returnedMap.put("OK",new EntityData(data.getJSONObject(indexOfInterest),false));
                }
                //Data retrieval was unsuccessful (not "OK")
                else {
                    //Return with error message
                    returnedMap.put(entry.getKey(),null);
                }
            }
        }
        //cached data
        else{
            try{
                //return the single currency of interest from the cached data
                JSONArray cachedData = this.database.getCachedCryptoData(symbol);
                //index is 0 because getCachedCryptoData returns an array only containing one element
                returnedMap.put("OK",new EntityData(cachedData.getJSONObject(0),true));
            } catch (SQLException e){
                //Return with error message
                returnedMap.put(e.getMessage() ,null);
            }
        }


        return returnedMap;
    }


    /**
     * Retrieves the latest data of the cryptocurrencies, then converts amount from one currency to another.<br><br>
     * <b>Preconditions:</b><br>
     * The fee attribute must not be null<br>
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
    public Map<String, Double> conversion(String from, String to, String amount, boolean isCached){
        Map<String, Double> returnedMap = new HashMap<String, Double>();

        double amountNumerical = 0;
        try{
            //If any inputs were empty, throw exception
            if(from.equals("") || to.equals("") || amount.equals("")){
                throw new NullPointerException();
            }
            //if amount was not numerical, or outside of the required bounds to pass to CoinMarketCap, throw exception
            amountNumerical = Double.parseDouble(amount);
            if(amountNumerical <= 0 || amountNumerical > 1000000000){
                throw new NumberFormatException();
            }
        }catch (NumberFormatException e) {
            //Return with error message
            returnedMap.put("Warning: Please Enter a Numeric Input that is non-negative, non-zero and less than 1000000000",null);
            return returnedMap;
        } catch (NullPointerException e){
            //Return with error message
            returnedMap.put("Warning: Empty input detected",null);
            return returnedMap;
        }

        if(!isCached){
            Map<String, JSONArray> dataMap = new HashMap<>();
            //Retrieve the latest data from API, or dummy data if offline
            if(online){
                dataMap = getAllCryptoData();
            }else{
                dataMap.put("OK",makeDummyData());
            }
            for(Map.Entry<String, JSONArray> entry : dataMap.entrySet()) {
                //Success message "OK"
                if (entry.getKey().equals("OK")) {
                    JSONArray data = entry.getValue();
                    double fromPrice = -1;
                    double toPrice = -1;
                    //Find prices of both currencies in data
                    for(int i = 0; i < data.length(); i++){
                        if(from.toUpperCase().equals(data.getJSONObject(i).optString("symbol"))){
                            fromPrice = data.getJSONObject(i).getJSONObject("quote").getJSONObject("USD").optDouble("price",-1);
                        }
                        if(to.toUpperCase().equals(data.getJSONObject(i).optString("symbol"))){
                            toPrice = data.getJSONObject(i).getJSONObject("quote").getJSONObject("USD").optDouble("price",-1);
                        }
                        if(fromPrice != -1 && toPrice != -1){
                            break;
                        }
                    }
                    try {
                        //if values were unchanged, symbol was not found in data
                        if (fromPrice == -1 || toPrice == -1) {
                            throw new BadRequestException("Warning: Make sure you have enter A valid symbol. Find currency symbols on the previous page");
                        }
                        //Conversion
                        double convertedAmount = amountNumerical * (fromPrice / toPrice) * (1-(this.fee/100));
                        returnedMap.put("OK", convertedAmount);
                    } catch (BadRequestException e) {
                        //Return with error message
                        returnedMap.put(e.getMessage(),null);
                    }
                }
                //Data retrieval was unsuccessful (not "OK")
                else {
                    //Return with error message
                    returnedMap.put(entry.getKey(), null);
                }
            }
        }
        //Cached data
        else {
            try {
                JSONObject obj = this.database.getCachedConversionPrices(from.toUpperCase(), to.toUpperCase());
                //Conversion
                double convertedAmount = amountNumerical * (obj.optDouble(from.toUpperCase())/obj.optDouble(to.toUpperCase())) * (1-(this.fee/100));
                returnedMap.put("OK",convertedAmount);
            } catch (SQLException e) {
                //Return with error message
                returnedMap.put(e.getMessage(),null);
            }
        }

        return returnedMap;
    }

    //Retrieves the top 500 ranked cryptocurrencies from CoinMarketCap
    private Map<String, JSONArray> getAllCryptoData(){
        Map<String, JSONArray> returnedMap = new HashMap<String, JSONArray>();
        String path = uri + allCryptoLatest;
        List<NameValuePair> parameters = new ArrayList<NameValuePair>();
        parameters.add(new BasicNameValuePair("start", "1"));
        parameters.add(new BasicNameValuePair("limit", "1000"));
        try {
            JSONObject result = this.requestMaker.cryptoDataRequest(path,parameters,this.apiKey);
            JSONArray arr =  result.getJSONArray("data");
            returnedMap.put("OK",arr);
        } catch (BadRequestException e) {
            returnedMap.put(e.getMessage(),null);
        }
        return returnedMap;
    }

    //Dummy data mimics the format of the JSONArray returned by the CoinMarketCap API from an HTTP request
    private JSONArray makeDummyData(){
        JSONArray data = new JSONArray();
        //Returns 100 dummy cryptocurrencies
        for (int i = 0; i < 100; i++) {
            JSONObject currUsd = new JSONObject();
            JSONObject curr = new JSONObject();
            JSONObject currQuote = new JSONObject();

            currQuote.put("USD",currUsd);
            currUsd.put("price", 100);
            currUsd.put("volume_24h", 100);
            currUsd.put("percent_change_1h", 1);
            currUsd.put("percent_change_24h", -1);
            currUsd.put("percent_change_7d", 2);
            currUsd.put("market_cap", 100000);


            curr.put("symbol", "CRYPTO");
            curr.put("name", "Cryptocurrency");
            curr.put("cmc_rank", 1);
            curr.put("num_market_pairs", 2);
            curr.put("circulating_supply", 1000);
            curr.put("total_supply", 1000);
            curr.put("max_supply", 2000);
            curr.put("last_updated", "2000-01-01T00:00:00.000Z");
            curr.put("date_added", "2000-01-01T00:00:00.000Z");
            curr.put("quote",currQuote);

            data.put(curr);
        }
        return data;
    }

    /**
     * Sets the merchant fee percentage of the conversion if fee is a whole number between 0 and 99<br><br>
     * <b>Preconditions:</b><br>
     * None<br>
     * <b>Postconditions:</b><br>
     * If the parameter is valid, the entered value will become the merchant fee that will be used in the conversion method of this class <br>
     *
     * @param fee the percentage value that the user has entered
     * @return if the fee entered was successfully set as the merchant fee or not
     */
    public boolean setFee(String fee){
        //If any inputs were empty return false
        if(fee.equals("")){
            return false;
        }
        double feeNumerical = 0;
        try{
            //if amount was not numerical, or outside of the required bounds, throw exception
            feeNumerical = Double.parseDouble(fee);
            //out of bounds
            if(feeNumerical < 0 || feeNumerical > 99){
                throw new NumberFormatException();
            }
            //not a whole number
            if(feeNumerical % 1 != 0){
                throw new NumberFormatException();
            }
        }catch (NumberFormatException e) {
            return false;
        }
        this.fee = feeNumerical;
        return true;
    }

}
