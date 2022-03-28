package CryptocurrencyApp.Controller;

import CryptocurrencyApp.BadRequestException;
import CryptocurrencyApp.Model.RequestMaker;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class generates reports using the PasteBin API
 */
public class Report {
    final String chars = "abcdefghijklmnopqrstuvwxyz0123456789";
    private String uri = "https://pastebin.com/api/api_post.php";
    private String devApiKey;
    private RequestMaker requestMaker;
    private boolean online;

    /**
     * Initialises a new Report instance that will post a formatted report to the PasteBin API (online).<br><br>
     * <b>Preconditions:</b><br>
     * None<br>
     * <b>Postconditions:</b><br>
     * The data on cryptocurrencies will be formatted and posted on the PasteBin API.
     *
     * @param devApiKey unique API key for CoinMarketCap
     * @param requestMaker a RequestMaker object that will be used to send POST requests to the API.
     */
    public Report(String devApiKey, RequestMaker requestMaker){
        this.devApiKey = devApiKey;
        this.requestMaker = requestMaker;
        this.online = true;
    }

    /**
     * Initialises a new Report instance that will generate a dummy (offline) response.<br><br>
     * <b>Preconditions:</b><br>
     * None<br>
     * <b>Postconditions:</b><br>
     * This instance will generate a dummy response.<br><br>
     */
    public Report(){
        this.online = false;
    }


    /**
     * Retrieves a report link<br><br>
     * <b>Preconditions:</b><br>
     * None<br>
     * <b>Postconditions:</b><br>
     * If data is online, the user can access the generated report through the link.
     * If data is offline, the url generated will not be accessible as it is a dummy.
     *
     * @param currencyData an EntityData object that contains the currency's information.
     * @param title A String that will be shown as the title of the report i.e. the currency symbol
     * @return a supposed url of the pastebin report
     */
    public Map<String,String> getReportLink(EntityData currencyData, String title){
        Map<String, String> returnedMap = new HashMap<>();
        if(this.online){
            String pasteText = reportGenerator(currencyData);
            List<NameValuePair> parameters = new ArrayList<NameValuePair>();
            parameters.add(new BasicNameValuePair("api_dev_key", devApiKey));
            parameters.add(new BasicNameValuePair("api_option", "paste"));
            parameters.add(new BasicNameValuePair("api_paste_code", pasteText));
            //pastebin report will expire in 1 hour
            parameters.add(new BasicNameValuePair("api_paste_expire_date", "1H"));
            //pastebin report title will be the param title
            parameters.add(new BasicNameValuePair("api_paste_name", title));

            try {
                String link = this.requestMaker.reportRequest(uri,parameters);
                returnedMap.put("OK",link);
            } catch (BadRequestException e) {
                returnedMap.put(e.getMessage(),null);
            }
        }
        else{
            SecureRandom random = new SecureRandom();
            StringBuilder sb = new StringBuilder();

            //Generate a random string to create the dummy link
            for (int i = 0; i < 8; i++)
            {
                int randomIndex = random.nextInt(chars.length());
                sb.append(chars.charAt(randomIndex));
            }
            String link = "https://pastebin.com/" + sb.toString();
            returnedMap.put("OK",link);
        }
        return returnedMap;
    }

    //report formatter
    private String reportGenerator(EntityData currencyData){
        StringBuilder sb = new StringBuilder();
        sb.append("Cryptocurrency information report: \n");
        sb.append(String.format("\tSymbol: %s\n",currencyData.getSymbol()));
        sb.append(String.format("\tName: %s\n",currencyData.getName()));
        sb.append(String.format("\tCMC Rank: %s\n",currencyData.getCmcRank()));

        sb.append(String.format("\tPrice: %f USD\n", currencyData.getPrice()));
        sb.append(String.format("\tVolume Last 24H: %f USD\n", currencyData.getVolume24H()));
        sb.append(String.format("\tPercentage Change Last 1H: %f%%\n", currencyData.getPercentChange1H()));
        sb.append(String.format("\tPercentage Change Last 24H: %f%%\n", currencyData.getPercentChange24H()));
        sb.append(String.format("\tPercentage Change Last 7 Days: %f%%\n", currencyData.getPercentChange7D()));
        sb.append(String.format("\tMarket Cap: %f USD\n", currencyData.getPercentChange7D()));

        sb.append(String.format("\tPrice: %s\n",currencyData.getNumMarketPairs()));
        sb.append(String.format("\tCirculating Supply: %s\n",currencyData.getCirculatingSupply()));
        sb.append(String.format("\tTotal Supply: %s\n",currencyData.getTotalSupply()));
        sb.append(String.format("\tMax Supply: %s\n",currencyData.getMaxSupply()));
        sb.append(String.format("\tLast Updated: %s\n",currencyData.getLastUpdated()));
        sb.append(String.format("\tData Added: %s\n",currencyData.getDateAdded()));

        return sb.toString();
    }
}
