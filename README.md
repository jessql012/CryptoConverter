# CryptoConverter
## Launching the App
Java 11 and Gradle 7.0 and above are recommended to run this app.

To run the GUI application, type the following command in the command line:  `gradle run --args="{CoinMarketCap} {Pastebin}"`
where the input argument for `CoinMarketCap` indicates if you would like the currency data to be online or dummy data, and the input argument for`Pastebin` indicates if you would like the report link to be a legitimate URL or a dummy value. Setting the argument as `online` indicates online data, and `offline` indicates offline/dummy data.

You will need a CoinMarketCap and PasteBin account to use the online versions of the associated features. Once your account is made you will receive a unique API key for each account.

Please put in your unique API keys for CoinMarketCap and PasteBin in the ConfigKeys.txt file, in the required order. Put the CoinMarketCap API key or dummy value in the first line of the file, and PasteBin devAPIkey or dummy value in the second line of the file.

E.g. For `gradle run --args="offline online"`, the file should look like this:

somedummyvaluethatwillnotbeused  
YOUR-PASTE-BIN-API-KEY

E.g. For `gradle run --args="online offline"`, the file should look like this:

YOUR-CMC-API-KEY  
somedummyvaluethatwillnotbeused

E.g. For `gradle run --args="online online"`, the file should look like this:

YOUR-CMC-API-KEY  
YOUR-PASTE-BIN-API-KEY

For `gradle run --args="offline offline"`, the file can be empty


## App Features
* Displays the top 1000 cryptocurrencies on CoinMarketCap sorted in order of market cap
* Asks for a merchant fee for the conversion feature (this will be subtracted from currency conversion)
* Selecting a cryptocurrency will bring the user to a page with further information on the currency
* Cryptocurrency conversion from and to selected currency
* User can obtain a Pastebin report which contains detailed information about a cryptocurrency
* Once a currency is selected, a cached copy of its data will be stored and can be accessed when the user selects the currency again. Each time a fresh copy of the data is obtained from the API, the database is updated.
* Application can be tested by using live input/output from CoinMarketCap or Pastebin (online), or dummy versions of them (offline).

## Development Features
* Concurrency between GUI and backend layers to improve performance, latency and user experience
* Implemented caching data into a local SQLite database for faster data retrieval
* Software design follows the MVP and facade design patterns to ensure maintainability and modularity of the software.

## Contributors
Jessica Liu

## References
Some code from previous assignment in the ListCryptoPage class  
Parameter formatting in RequestMaker: https://mkyong.com/java/java-11-httpclient-examples/
