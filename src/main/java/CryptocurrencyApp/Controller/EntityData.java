package CryptocurrencyApp.Controller;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * This class store all information about a currency
 */
public class EntityData {
    private boolean isCached;
    private String symbol;
    private String name;
    private int cmcRank;
    private int numMarketPairs;
    private int circulatingSupply;
    private int totalSupply;
    private int maxSupply;
    private String lastUpdated;
    private String dateAdded;
    private double price;
    private double volume24H;
    private double percentChange1H;
    private double percentChange24H;
    private double percentChange7D;
    private double marketCap;

    /**
     * Initialises a EntityData instance<br><br>
     * <b>Preconditions:</b><br>
     * None<br>
     * <b>Postconditions:</b><br>
     * EntityData methods will return values depending on the data given in the JSONObject.
     * Data that was unable to be retrieved from the JSONObject will be replaced by 0 or "N/A"<br><br>
     *
     * @param currency JSONObject containing all information about the chosen currency
     * @param isCached if the data given is cached data or pulled from the CoinMarketCap API
     */
    public EntityData(JSONObject currency, boolean isCached) {
        this.isCached = isCached;
        this.symbol = currency.optString("symbol","N/A");
        this.name = currency.optString("name","N/A");
        this.cmcRank = currency.optInt("cmc_rank",0);
        this.numMarketPairs = currency.optInt("num_market_pairs",0);
        this.circulatingSupply = currency.optInt("circulating_supply",0);
        this.totalSupply = currency.optInt("total_supply",0);
        this.maxSupply = currency.optInt("max_supply",0);

        JSONObject quote = currency.getJSONObject("quote");
        this.price = quote.getJSONObject("USD").optDouble("price",0);
        this.volume24H = quote.getJSONObject("USD").optDouble("volume_24h",0);
        this.percentChange1H = quote.getJSONObject("USD").optDouble("percent_change_1h",0);
        this.percentChange24H = quote.getJSONObject("USD").optDouble("percent_change_24h",0);
        this.percentChange7D = quote.getJSONObject("USD").optDouble("percent_change_7d",0);
        this.marketCap = quote.getJSONObject("USD").optDouble("market_cap",0);

        SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        SimpleDateFormat output = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss 'UTC'");
        try {
            Date d = input.parse(currency.optString("last_updated","N/A"));
            this.lastUpdated = output.format(d);
            d = input.parse(currency.optString("date_added","N/A"));
            this.dateAdded = output.format(d);
        } catch (ParseException e) {
            this.lastUpdated = "N/A";
            this.dateAdded = "N/A";
        }
    }

    /**
     * Retrieves the symbol of the currency.<br><br>
     * <b>Preconditions:</b><br>
     * None<br>
     * <b>Postconditions:</b><br>
     * None<br><br>
     *
     * @return the symbol of the currency, will return "N/A" if not found
     */
    public String getSymbol() {
        return symbol;
    }

    /**
     * Retrieves the name of the currency.<br><br>
     * <b>Preconditions:</b><br>
     * None<br>
     * <b>Postconditions:</b><br>
     * None<br><br>
     *
     * @return the name of the currency, will return "N/A" if not found
     */
    public String getName() {
        return name;
    }

    /**
     * Retrieves the CoinMarketCap rank of the currency.<br><br>
     * <b>Preconditions:</b><br>
     * None<br>
     * <b>Postconditions:</b><br>
     * None<br><br>
     *
     * @return the CoinMarketCap rank of the currency, will return 0 if not found
     */
    public int getCmcRank() {
        return cmcRank;
    }

    /**
     * Retrieves the number of market pairs of the currency.<br><br>
     * <b>Preconditions:</b><br>
     * None<br>
     * <b>Postconditions:</b><br>
     * None<br><br>
     *
     * @return the number of market pairs of the currency, will return 0 if not found
     */
    public int getNumMarketPairs() {
        return numMarketPairs;
    }

    /**
     * Retrieves the circulating supply of the currency.<br><br>
     * <b>Preconditions:</b><br>
     * None<br>
     * <b>Postconditions:</b><br>
     * None<br><br>
     *
     * @return the circulating supply of the currency, will return 0 if not found
     */
    public int getCirculatingSupply() {
        return circulatingSupply;
    }

    /**
     * Retrieves the total supply of the currency.<br><br>
     * <b>Preconditions:</b><br>
     * None<br>
     * <b>Postconditions:</b><br>
     * None<br><br>
     *
     * @return the total supply of the currency, will return 0 if not found
     */
    public int getTotalSupply() {
        return totalSupply;
    }

    /**
     * Retrieves the max supply of the currency.<br><br>
     * <b>Preconditions:</b><br>
     * None<br>
     * <b>Postconditions:</b><br>
     * None<br><br>
     *
     * @return the max supply of the currency, will return 0 if not found
     */
    public int getMaxSupply() {
        return maxSupply;
    }

    /**
     * Retrieves the last updated date of the currency.<br><br>
     * <b>Preconditions:</b><br>
     * None<br>
     * <b>Postconditions:</b><br>
     * None<br><br>
     *
     * @return the last updated date of the currency, will return 0 if not found
     */
    public String getLastUpdated() {
        return lastUpdated;
    }

    /**
     * Retrieves the date the currency was added.<br><br>
     * <b>Preconditions:</b><br>
     * None<br>
     * <b>Postconditions:</b><br>
     * None<br><br>
     *
     * @return the date the currency was added, will return "N/A" if not found
     */
    public String getDateAdded() {
        return dateAdded;
    }

    /**
     * Retrieves the price in USD of the currency.<br><br>
     * <b>Preconditions:</b><br>
     * None<br>
     * <b>Postconditions:</b><br>
     * None<br><br>
     *
     * @return the price in USD of the currency, will return "N/A" if not found
     */
    public double getPrice() {
        return price;
    }

    /**
     * Retrieves the volume in the last 24 hours in USD of the currency.<br><br>
     * <b>Preconditions:</b><br>
     * None<br>
     * <b>Postconditions:</b><br>
     * None<br><br>
     *
     * @return the volume in the last 24 hours in USD of the currency, will return 0 if not found
     */
    public double getVolume24H() {
        return volume24H;
    }

    /**
     * Retrieves the percentage change in the last hour of the currency.<br><br>
     * <b>Preconditions:</b><br>
     * None<br>
     * <b>Postconditions:</b><br>
     * None<br><br>
     *
     * @return the percentage change in the last hour of the currency, will return 0 if not found
     */
    public double getPercentChange1H() {
        return percentChange1H;
    }

    /**
     * Retrieves the percentage change in the last 24 hours of the currency.<br><br>
     * <b>Preconditions:</b><br>
     * None<br>
     * <b>Postconditions:</b><br>
     * None<br><br>
     *
     * @return the percentage change in the last 24 hours of the currency, will return 0 if not found
     */
    public double getPercentChange24H() {
        return percentChange24H;
    }

    /**
     * Retrieves the percentage change in the last 7 days of the currency.<br><br>
     * <b>Preconditions:</b><br>
     * None<br>
     * <b>Postconditions:</b><br>
     * None<br><br>
     *
     * @return the percentage change in the last 7 days of the currency, will return 0 if not found
     */
    public double getPercentChange7D() {
        return percentChange7D;
    }

    /**
     * Retrieves the market cap in USD of the currency.<br><br>
     * <b>Preconditions:</b><br>
     * None<br>
     * <b>Postconditions:</b><br>
     * None<br><br>
     *
     * @return the market cap in USD of the currency, will return 0 if not found
     */
    public double getMarketCap() {
        return marketCap;
    }

    /**
     * Checks if the instance contains cached data or not.<br><br>
     * <b>Preconditions:</b><br>
     * None<br>
     * <b>Postconditions:</b><br>
     * None<br><br>
     *
     * @return if the instance contains cached data or not, i.e. true for cached
     */
    public boolean isCached(){ return isCached; }
}
