package CryptocurrencyApp.Model;

import CryptocurrencyApp.BadRequestException;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URIBuilder;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * This class makes GET and POST requests to both the input and output API.
 */
public class RequestMaker {

    /**
     * Sends a GET request to the CoinMarketCap API for Cryptocurrencies in its database<br><br>
     * <b>Preconditions:</b><br>
     * Must have valid path and parameters arguments. Parameter arguments determine the number of Cryptocurrencies' data gathered.
     * <b>Postconditions:</b><br>
     * None<br>
     *
     * @param path The URI to send the request to.
     * @param parameters Parameters used in the request i.e. start = 1, limit = 500
     * @param apiKey Unique apiKey used to access the data during the request
     * @return A JSONObject that contains the cryptocurrency data requested
     * @throws BadRequestException If there is an error that caused the request to fail
     */
    public JSONObject cryptoDataRequest(String path, List<NameValuePair> parameters, String apiKey) throws BadRequestException {
        HttpClient client = HttpClient.newHttpClient();
        URI uri = null;
        try {
            uri = new URIBuilder(path)
                    .addParameters(parameters)
                    .build();
        } catch (URISyntaxException e) {
            return null;
        }
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .header("Accept", "application/json")
                .header("X-CMC_PRO_API_KEY", apiKey)
                .GET()
                .build();
        try{
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            //Response code 200 means request was successfull
            //if not 200, throw exception with the appropriate error message, this wil be displayed by the GUI
            if(response.statusCode() != 200){
                //Reponse code 500 means internal server error
                if(response.statusCode() == 500){
                    throw new BadRequestException("Warning: An internal server error occurred. Please try again later");
                }
                //Reponse code 401 means API key was invalid
                if(response.statusCode() == 401){
                    throw new BadRequestException("Warning: Your CoinMarketCap API Key is invalid. Please edit ConfigKeys.txt and reopen the application");
                }
                throw new BadRequestException(String.format("%s", response.statusCode()));
            }
            JSONObject obj = new JSONObject(response.body());
            return obj;
        }catch(InterruptedException | IOException e){
            return null;
        }
    }

    /**
     * Sends a POST request to the PasteBin API to paste a report<br><br>
     * <b>Preconditions:</b><br>
     * Must have valid path and parameters arguments, i.e. devAPIKey, api_option etc
     * <b>Postconditions:</b><br>
     * None<br>
     *
     * @param uri The URI to send the request to.
     * @param parameters Parameters used in the request
     * @return The String url of the pastebin report
     * @throws BadRequestException If there is an error that caused the request to fail
     */
    public String reportRequest(String uri, List<NameValuePair> parameters) throws BadRequestException{
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(uri))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(bodyPublisherMaker(parameters))
                .build();
        try{
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if(response.body().equals("Bad API request, invalid api_dev_key")){
                //throw exception with the appropriate error message, this wil be displayed by the GUI
                throw new BadRequestException("Warning: Your PasteBin API Key is invalid. Please edit ConfigKeys.txt and reopen the application");
            }
            return response.body();
        }catch(InterruptedException | IOException e){
            return null;
        }
    }

    //this method was inspired by https://mkyong.com/java/java-11-httpclient-examples/
    private HttpRequest.BodyPublisher bodyPublisherMaker(List<NameValuePair> parameters) {
        //Formats the parameters in the correct uri syntax
        var sb = new StringBuilder();
        for (NameValuePair pair : parameters) {
            if (sb.length() > 0) {
                sb.append("&");
            }
            sb.append(URLEncoder.encode(pair.getName(), StandardCharsets.UTF_8));
            sb.append("=");
            sb.append(URLEncoder.encode(pair.getValue(), StandardCharsets.UTF_8));
        }
        return HttpRequest.BodyPublishers.ofString(sb.toString());
    }
}
