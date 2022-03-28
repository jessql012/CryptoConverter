package CryptocurrencyApp.Model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


/**
 * This class creates an SQL database, caches data on cryptocurrencies, and retrieves cached values from the database using SQL queries.
 */
public class Database {
    Connection connection = null;

    /**
     * Establishes a connection with the database, then clears the database and enters table column names.<br><br>
     * <b>Preconditions:</b><br>
     * None<br>
     * <b>Postconditions:</b><br>
     * None<br><br>
     *
     */
    public Database(){
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:cmc.db");
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.

            //Create new table in the db file
            statement.executeUpdate("drop table if exists cryptocurrencies");
            statement.executeUpdate("create table cryptocurrencies " +
                    "                   (symbol string, name string, cmcRank int, numMarketPairs int, " +
                                        "circulatingSupply int, totalSupply int, maxSupply int, lastUpdated string, dateAdded string," +
                                        "price float, volume24H float, percentChange1H float, percentChange24H float, percentChange7D float, marketCap float);");

        } catch(SQLException e) {
            System.err.println(e.getMessage());
        }
        finally {
            try {
                if(connection != null)
                    connection.close();
            }
            catch(SQLException e) {
                System.err.println(e.getMessage());
            }
        }
    }

    /**
     * Retrieves the cached data of a cryptocurrency<br><br>
     * <b>Preconditions:</b><br>
     * If data was not previously cached, or the symbols given were not found in the database, will throw an SQLException<br>
     * <b>Postconditions:</b><br>
     * None<br><br>
     *
     * @param symbol the symbol of the currency of interest
     * @return A JSONObject that contains the cached cryptocurrency data
     * @throws SQLException If there is no cached data available or symbol was not found
     */
    public JSONArray getCachedCryptoData(String symbol) throws SQLException{
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:cmc.db");

            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.

            //Get the cached data of the currency of interest by finding its symbol in the database
            ResultSet rs = statement.executeQuery(String.format("select * from cryptocurrencies where symbol == '%s';",symbol));

            //Mimic the format of the CoinMarketCap response to a HTTP request.
            JSONObject currUsd = new JSONObject();
            currUsd.put("price", rs.getFloat("price"));
            currUsd.put("volume_24h", rs.getFloat("volume24h"));
            currUsd.put("percent_change_1h", rs.getFloat("percentChange1h"));
            currUsd.put("percent_change_24h", rs.getFloat("percentChange24h"));
            currUsd.put("percent_change_7d", rs.getFloat("percentChange7d"));
            currUsd.put("market_cap", rs.getFloat("marketCap"));

            JSONObject currQuote = new JSONObject();
            currQuote.put("USD",currUsd);

            JSONObject curr = new JSONObject();
            curr.put("symbol", rs.getString("symbol"));
            curr.put("name", rs.getString("name"));
            curr.put("cmc_rank", rs.getInt("cmcRank"));
            curr.put("num_market_pairs", rs.getInt("numMarketPairs"));
            curr.put("circulating_supply", rs.getInt("circulatingSupply"));
            curr.put("total_supply", rs.getInt("totalSupply"));
            curr.put("max_supply", rs.getInt("maxSupply"));
            curr.put("last_updated", rs.getString("lastUpdated"));
            curr.put("date_added", rs.getString("dateAdded"));
            curr.put("quote",currQuote);

            JSONArray data = new JSONArray();
            data.put(curr);
            return data;
        } catch(SQLException e) {
            //This exception will be thrown when the SQL table contains no cached data
            throw new SQLException("Warning: No cached data was found");
        }
        finally {
            try {
                if(connection != null)
                    connection.close();
            }
            catch(SQLException e) {
                System.err.println(e.getMessage());
            }
        }
    }

    /**
     * Retrieves the cached data of two cryptocurrencies' prices<br><br>
     * <b>Preconditions:</b><br>
     * If data was not previously cached, or the symbols given were not found in the database, will throw an SQLException<br>
     * <b>Postconditions:</b><br>
     * None<br><br>
     *
     * @param from the symbol of the currency to convert from
     * @param to the symbol of the currency to convert to
     * @return A JSONObject that contains the cached cryptocurrency data
     * @throws SQLException If there is no cached data available or symbol was not found
     */
    public JSONObject getCachedConversionPrices(String from, String to) throws SQLException{
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:cmc.db");

            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.

            //returns the price of both cryptocurrencies in USD,
            //returned in JSONObject format to remain consistent with the RequestMaker class
            ResultSet rsFrom = statement.executeQuery(String.format("select symbol, price from cryptocurrencies where symbol == '%s';",from));
            ResultSet rsTo = statement.executeQuery(String.format("select symbol, price from cryptocurrencies where symbol == '%s';",to));

            JSONObject price = new JSONObject();
            price.put(from, rsFrom.getFloat("price"));
            price.put(to,rsTo.getFloat("price"));
            return price;
        } catch(SQLException e) {
            //This exception will be thrown when the SQL table does not contain any one of the symbols provided
            throw new SQLException("Warning: Make sure you have enter A valid symbol. Find available symbols on the previous page");
        }
        finally {
            try {
                if(connection != null)
                    connection.close();
            }
            catch(SQLException e) {
                System.err.println(e.getMessage());
            }
        }
    }

    /**
     * Caches and updates cryptocurrency data in the database<br><br>
     * <b>Preconditions:</b><br>
     * None<br>
     * <b>Postconditions:</b><br>
     * Any data retrieved from the database will now be of the newly cached data if successful.<br><br>
     *
     * @param data the JSONArray that contains all the cryptocurrencies' data to store
     * @return boolean that represents if the data was successfully cached or not
     */
    public boolean cacheNewData(JSONArray data){
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:cmc.db");

            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.

            //Check if database is empty
            int checkEmpty = statement.executeUpdate("select count(symbol) from cryptocurrencies;");
            if(checkEmpty != 0){
                //If not empty then safely delete all entries before inserting again
                statement.executeUpdate("delete * from cryptocurrency;");
            }
            //insert all data from the given JSONArray
            for(int i = 0; i < data.length(); i++){
                statement.executeUpdate(String.format("insert into cryptocurrencies values('%s','%s',%d,%d,%d,%d,%d,'%s','%s',%f,%f,%f,%f,%f,%f);",
                        data.getJSONObject(i).optString("symbol","N/A"),
                        data.getJSONObject(i).optString("name","N/A"),
                        data.getJSONObject(i).optInt("cmc_rank",0),
                        data.getJSONObject(i).optInt("num_market_pairs",0),
                        data.getJSONObject(i).optInt("circulating_supply",0),
                        data.getJSONObject(i).optInt("total_supply",0),
                        data.getJSONObject(i).optInt("max_supply",0),
                        data.getJSONObject(i).optString("last_updated","N/A"),
                        data.getJSONObject(i).optString("date_added","N/A"),
                        data.getJSONObject(i).getJSONObject("quote").getJSONObject("USD").optFloat("price",0),
                        data.getJSONObject(i).getJSONObject("quote").getJSONObject("USD").optFloat("volume_24h",0),
                        data.getJSONObject(i).getJSONObject("quote").getJSONObject("USD").optFloat("percent_change_1h",0),
                        data.getJSONObject(i).getJSONObject("quote").getJSONObject("USD").optFloat("percent_change_24h",0),
                        data.getJSONObject(i).getJSONObject("quote").getJSONObject("USD").optFloat("percent_change_7d",0),
                        data.getJSONObject(i).getJSONObject("quote").getJSONObject("USD").optFloat("market_cap",0)));
            }
            return true;
        } catch(SQLException e) {
            //This method will return false if data format was incorrect or if there was an error when accessing the database.
            return false;
        }
        finally {
            try {
                if(connection != null)
                    connection.close();
            }
            catch(SQLException e) {
                System.err.println(e.getMessage());
            }
        }
    }
}
