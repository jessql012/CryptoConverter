package CoinMarketCap;

import CryptocurrencyApp.Controller.EntityData;
import CryptocurrencyApp.Controller.Facade;
import CryptocurrencyApp.Controller.ListData;
import CryptocurrencyApp.Controller.Report;
import CryptocurrencyApp.Model.Database;
import CryptocurrencyApp.Model.RequestMaker;
import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class FacadeTest {
    private RequestMaker requestMaker = mock(RequestMaker.class);
    private Database database = mock(Database.class);

    public void mockCryptoData() {
        JSONObject btcUSD = new JSONObject();
        btcUSD.put("price", 9283.92);
        btcUSD.put("volume_24h", 715568000);
        btcUSD.put("percent_change_1h", -0.152774);
        btcUSD.put("percent_change_24h", 0.518894);
        btcUSD.put("percent_change_7d", 0.986573);
        btcUSD.put("market_cap", 1580550242);

        JSONObject btcQuote = new JSONObject();
        btcQuote.put("USD",btcUSD);

        JSONObject btc = new JSONObject();
        btc.put("symbol", "BTC");
        btc.put("name", "Bitcoin");
        btc.put("cmc_rank", 1);
        btc.put("num_market_pairs", 9728);
        btc.put("circulating_supply", 18721681);
        btc.put("total_supply", 18721681);
        btc.put("max_supply", 21000000);
        btc.put("last_updated", "2018-06-02T22:51:28.209Z");
        btc.put("date_added", "2013-04-28T00:00:00.000Z");
        btc.put("quote",btcQuote);

        JSONObject ethUSD = new JSONObject();
        ethUSD.put("price", 2420.92);
        ethUSD.put("volume_24h", 34956800);
        ethUSD.put("percent_change_1h", -0.152774);
        ethUSD.put("percent_change_24h", 0.518894);
        ethUSD.put("percent_change_7d", 0.986573);
        ethUSD.put("market_cap", 1580550432);

        JSONObject ethQuote = new JSONObject();
        ethQuote.put("USD",ethUSD);

        JSONObject eth = new JSONObject();
        eth.put("symbol", "ETH");
        eth.put("name", "Ethereum");
        eth.put("cmc_rank", 2);
        eth.put("num_market_pairs", 6005);
        eth.put("circulating_supply", 116082098);
        eth.put("total_supply", 116082098);
        eth.put("max_supply", 0);
        eth.put("last_updated", "2021-06-02T22:51:28.209Z");
        eth.put("date_added", "2015-04-28T00:00:00.000Z");
        eth.put("quote",ethQuote);

        JSONObject usdtUSD = new JSONObject();
        usdtUSD.put("price", 1.0001);
        usdtUSD.put("volume_24h", 99332057);
        usdtUSD.put("percent_change_1h", 0.023);
        usdtUSD.put("percent_change_24h", 0.518894);
        usdtUSD.put("percent_change_7d", -0.067);
        usdtUSD.put("market_cap", 617466386);


        JSONObject usdtQuote = new JSONObject();
        usdtQuote.put("USD",usdtUSD);

        JSONObject usdt = new JSONObject();
        usdt.put("symbol", "USDT");
        usdt.put("name", "Tether");
        usdt.put("cmc_rank", 3);
        usdt.put("num_market_pairs", 13295);
        usdt.put("circulating_supply", 160082098);
        usdt.put("total_supply", -96082098);
        usdt.put("max_supply", 0);
        usdt.put("last_updated", "2021-06-02T22:51:28.209Z");
        usdt.put("date_added", "2015-02-28T00:00:00.000Z");
        usdt.put("quote",usdtQuote);

        JSONArray data = new JSONArray();
        data.put(btc);
        data.put(eth);
        data.put(usdt);

        JSONObject listDataObj = new JSONObject();
        listDataObj.put("data", data);
        when(requestMaker.cryptoDataRequest(anyString(),anyList(),anyString())).thenReturn(listDataObj);
    }

    public void mockConversion(){
        JSONObject price = new JSONObject();
        price.put("BTC", 100);
        price.put("ETH", 100);
        price.put("USDT", 1);
        price.put("CRYPTO", 1);

        List<String> list = new ArrayList<>();
        list.add("BTC");
        list.add("ETH");
        list.add("USDT");
        list.add("CRYPTO");

        try {
            when(database.getCachedConversionPrices(anyString(),anyString()))
                    .thenAnswer(new Answer()
                    {
                        @Override
                        public Object answer(InvocationOnMock invocation) throws Throwable {
                            String symbolFrom = invocation.getArgument(0);
                            if(!list.contains(symbolFrom)){
                                throw new SQLException("Warning: Make sure you have enter A valid symbol. Find currency symbols on the previous page");
                            }
                            String symbolTo = invocation.getArgument(1);
                            if(!list.contains(symbolTo)){
                                throw new SQLException("Warning: Make sure you have enter A valid symbol. Find currency symbols on the previous page");
                            }
                            return price;
                        }
                    });
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void mockReportRequest(){
        when(requestMaker.reportRequest(anyString(),anyList())).thenReturn("https://pastebin.com/abcdefg");
    }

    public void mockDatabase(){
        JSONObject usdtUSD = new JSONObject();
        usdtUSD.put("price", 1.0001);
        usdtUSD.put("volume_24h", 99332057);
        usdtUSD.put("percent_change_1h", 0.023);
        usdtUSD.put("percent_change_24h", 0.518894);
        usdtUSD.put("percent_change_7d", -0.067);
        usdtUSD.put("market_cap", 617466386);

        JSONObject usdtQuote = new JSONObject();
        usdtQuote.put("USD",usdtUSD);

        JSONObject usdt = new JSONObject();
        usdt.put("symbol", "USDT");
        usdt.put("name", "Tether");
        usdt.put("cmc_rank", 3);
        usdt.put("num_market_pairs", 13295);
        usdt.put("circulating_supply", 160082098);
        usdt.put("total_supply", -96082098);
        usdt.put("max_supply", 0);
        usdt.put("last_updated", "2021-06-02T22:51:28.209Z");
        usdt.put("date_added", "2015-02-28T00:00:00.000Z");
        usdt.put("quote",usdtQuote);

        JSONArray data = new JSONArray();
        data.put(usdt);
        try {
            when(database.getCachedCryptoData(anyString())).thenReturn(data);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        when(database.cacheNewData(any(JSONArray.class))).thenReturn(true);
    }

    @Test
    public void listSymbolNameOnlineTest() {
        mockCryptoData();
        mockDatabase();
        ListData onlineData = new ListData("api-key", this.requestMaker, this.database);
        Report onlineReport = new Report("api-key", this.requestMaker);
        Facade facade = new Facade(onlineData,onlineReport);
        Map<String,List<NameValuePair>> listMap = facade.listSymbolName();
        List<NameValuePair> list = listMap.get("OK");

        assertEquals(3, list.size());
        assertEquals("BTC",list.get(0).getName());
        assertEquals("Bitcoin",list.get(0).getValue());
        assertEquals("ETH",list.get(1).getName());
        assertEquals("Ethereum",list.get(1).getValue());
        assertEquals("USDT",list.get(2).getName());
        assertEquals("Tether",list.get(2).getValue());

        //replace with offline report
        Report offlineReport = new Report();
        facade = new Facade(onlineData,offlineReport);
        listMap = facade.listSymbolName();
        list = listMap.get("OK");

        assertEquals(3, list.size());
        assertEquals("BTC",list.get(0).getName());
        assertEquals("Bitcoin",list.get(0).getValue());
        assertEquals("ETH",list.get(1).getName());
        assertEquals("Ethereum",list.get(1).getValue());
        assertEquals("USDT",list.get(2).getName());
        assertEquals("Tether",list.get(2).getValue());
    }

    @Test
    public void listSymbolNameOfflineTest() {
        mockDatabase();
        ListData offlineData = new ListData(this.database);
        Report onlineReport = new Report("api-key", this.requestMaker);
        Facade facade = new Facade(offlineData,onlineReport);
        Map<String,List<NameValuePair>> map = facade.listSymbolName();
        List<NameValuePair> list = map.get("OK");

        assertEquals(100, list.size());
        for(int i = 0; i < 100; i ++ ){
            assertEquals("CRYPTO",list.get(i).getName());
            assertEquals("Cryptocurrency",list.get(i).getValue());
        }

        //replace with offline report
        Report offlineReport = new Report();
        facade = new Facade(offlineData,offlineReport);
        map = facade.listSymbolName();
        list = map.get("OK");
        assertEquals(100, list.size());
        for(int i = 0; i < 100; i ++ ){
            assertEquals("CRYPTO",list.get(i).getName());
            assertEquals("Cryptocurrency",list.get(i).getValue());
        }
    }

    @Test
    public void getCurrencyInfoOnlineTest(){
        mockCryptoData();
        mockDatabase();
        ListData onlineData = new ListData("api-key", this.requestMaker, this.database);
        Report onlineReport = new Report("api-key", this.requestMaker);
        Facade facade = new Facade(onlineData,onlineReport);

        Map<String,List<NameValuePair>> listMap = facade.listSymbolName();
        List<NameValuePair> list = listMap.get("OK");

        Map<String, EntityData> map = facade.getCurrencyInfo(list.get(0).getName(),false);
        verify(this.database, atLeastOnce()).cacheNewData(any(JSONArray.class));
        for(Map.Entry<String, EntityData> btc : map.entrySet()) {
            assertEquals(list.get(0).getName(),btc.getValue().getSymbol());
            assertEquals(list.get(0).getValue(),btc.getValue().getName());
            assertEquals(1,btc.getValue().getCmcRank());
            assertEquals(9728,btc.getValue().getNumMarketPairs());
            assertEquals(18721681,btc.getValue().getCirculatingSupply());
            assertEquals(18721681,btc.getValue().getTotalSupply());
            assertEquals(21000000,btc.getValue().getMaxSupply());
            assertEquals("2018-06-02 22:51:28 UTC",btc.getValue().getLastUpdated());
            assertEquals("2013-04-28 00:00:00 UTC",btc.getValue().getDateAdded());
            assertEquals(9283.92,btc.getValue().getPrice(),0);
            assertEquals(715568000,btc.getValue().getVolume24H(), 1000);
            assertEquals( -0.152774,btc.getValue().getPercentChange1H(), 0);
            assertEquals( 0.518894,btc.getValue().getPercentChange24H(), 0);
            assertEquals( 0.986573,btc.getValue().getPercentChange7D(), 0);
            assertEquals(1580550432,btc.getValue().getMarketCap(), 1000);
        }


        //swap to offlinereport
        Report offlineReport = new Report();
        facade = new Facade(onlineData,offlineReport);

        listMap = facade.listSymbolName();
        list = listMap.get("OK");

        map = facade.getCurrencyInfo(list.get(2).getName(),false);
        for(Map.Entry<String, EntityData> usdt : map.entrySet()) {
            assertEquals(list.get(2).getName(),usdt.getValue().getSymbol());
            assertEquals(list.get(2).getValue(),usdt.getValue().getName());
            assertEquals(3,usdt.getValue().getCmcRank());
            assertEquals(13295,usdt.getValue().getNumMarketPairs());
            assertEquals(160082098,usdt.getValue().getCirculatingSupply());
            assertEquals(-96082098,usdt.getValue().getTotalSupply());
            assertEquals(0,usdt.getValue().getMaxSupply());
            assertEquals("2021-06-02 22:51:28 UTC",usdt.getValue().getLastUpdated());
            assertEquals("2015-02-28 00:00:00 UTC",usdt.getValue().getDateAdded());
            assertEquals(1.0001,usdt.getValue().getPrice(),0);
            assertEquals(99332057,usdt.getValue().getVolume24H(), 1000);
            assertEquals( 0.023,usdt.getValue().getPercentChange1H(), 0);
            assertEquals( 0.518894,usdt.getValue().getPercentChange24H(), 0);
            assertEquals( -0.067,usdt.getValue().getPercentChange7D(), 0);
            assertEquals(617466386,usdt.getValue().getMarketCap(),1000);
        }
    }

    @Test
    public void getCurrencyInfoCachedOnlineTest(){
        mockCryptoData();
        mockDatabase();
        ListData onlineData = new ListData("api-key", this.requestMaker, this.database);
        Report onlineReport = new Report("api-key", this.requestMaker);
        Facade facade = new Facade(onlineData,onlineReport);

        Map<String,List<NameValuePair>> listMap = facade.listSymbolName();
        List<NameValuePair> list = listMap.get("OK");
        facade.getCurrencyInfo(list.get(0).getName(),false);
        verify(this.database, times(1)).cacheNewData(any(JSONArray.class));
        facade.getCurrencyInfo(list.get(0).getName(),true);
        try {
            verify(this.database,atLeastOnce()).getCachedCryptoData(anyString());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        //swap to offlinereport
        Report offlineReport = new Report();
        facade = new Facade(onlineData,offlineReport);

        listMap = facade.listSymbolName();
        list = listMap.get("OK");

        facade.getCurrencyInfo(list.get(0).getName(),false);
        verify(this.database, times(2)).cacheNewData(any(JSONArray.class));
        facade.getCurrencyInfo(list.get(0).getName(),true);
        try {
            verify(this.database,atLeastOnce()).getCachedCryptoData(anyString());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Test
    public void getCurrencyInfoOfflineTest(){
        mockDatabase();
        ListData offlineData = new ListData(this.database);
        Report onlineReport = new Report("api-key",this.requestMaker);
        Facade facade = new Facade(offlineData,onlineReport);
        Map<String,List<NameValuePair>> listMap = facade.listSymbolName();
        List<NameValuePair> list = listMap.get("OK");

        Map<String, EntityData> map = facade.getCurrencyInfo(list.get(14).getName(),false);
        verify(this.database, atLeastOnce()).cacheNewData(any(JSONArray.class));
        for(Map.Entry<String, EntityData> dummy : map.entrySet()) {
            assertEquals("CRYPTO",dummy.getValue().getSymbol());
            assertEquals("Cryptocurrency",dummy.getValue().getName());
            assertEquals(1,dummy.getValue().getCmcRank());
            assertEquals(2,dummy.getValue().getNumMarketPairs());
            assertEquals(1000,dummy.getValue().getCirculatingSupply());
            assertEquals(1000,dummy.getValue().getTotalSupply());
            assertEquals(2000,dummy.getValue().getMaxSupply());
            assertEquals("2000-01-01 00:00:00 UTC",dummy.getValue().getLastUpdated());
            assertEquals("2000-01-01 00:00:00 UTC",dummy.getValue().getDateAdded());
            assertEquals(100,dummy.getValue().getPrice(),0);
            assertEquals(100,dummy.getValue().getVolume24H(), 0);
            assertEquals( 1,dummy.getValue().getPercentChange1H(), 0);
            assertEquals( -1,dummy.getValue().getPercentChange24H(), 0);
            assertEquals( 2,dummy.getValue().getPercentChange7D(), 0);
            assertEquals(100000,dummy.getValue().getMarketCap(),0);
        }


        //swap to offline report
        Report offlineReport = new Report();
        facade = new Facade(offlineData,offlineReport);
        listMap = facade.listSymbolName();
        list = listMap.get("OK");

        map = facade.getCurrencyInfo(list.get(9).getName(),false);
        for(Map.Entry<String, EntityData> dummy : map.entrySet()) {
            assertEquals("CRYPTO",dummy.getValue().getSymbol());
            assertEquals("Cryptocurrency",dummy.getValue().getName());
            assertEquals(1,dummy.getValue().getCmcRank());
            assertEquals(2,dummy.getValue().getNumMarketPairs());
            assertEquals(1000,dummy.getValue().getCirculatingSupply());
            assertEquals(1000,dummy.getValue().getTotalSupply());
            assertEquals(2000,dummy.getValue().getMaxSupply());
            assertEquals("2000-01-01 00:00:00 UTC",dummy.getValue().getLastUpdated());
            assertEquals("2000-01-01 00:00:00 UTC",dummy.getValue().getDateAdded());
            assertEquals(100,dummy.getValue().getPrice(),0);
            assertEquals(100,dummy.getValue().getVolume24H(), 0);
            assertEquals( 1,dummy.getValue().getPercentChange1H(), 0);
            assertEquals( -1,dummy.getValue().getPercentChange24H(), 0);
            assertEquals( 2,dummy.getValue().getPercentChange7D(), 0);
            assertEquals(100000,dummy.getValue().getMarketCap(),0);
        }
    }

    @Test
    public void getCurrencyInfoCachedOfflineTest(){
        mockDatabase();
        ListData offlineData = new ListData(this.database);
        Report onlineReport = new Report("api-key",this.requestMaker);
        Facade facade = new Facade(offlineData,onlineReport);
        Map<String,List<NameValuePair>> listMap = facade.listSymbolName();
        List<NameValuePair> list = listMap.get("OK");

        facade.getCurrencyInfo(list.get(6).getName(),true);
        Map<String, EntityData> map = facade.getCurrencyInfo(list.get(6).getName(),false);
        verify(this.database, times(1)).cacheNewData(any(JSONArray.class));
        try {
            verify(this.database,atLeastOnce()).getCachedCryptoData(anyString());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        for(Map.Entry<String, EntityData> dummy : map.entrySet()) {
            assertEquals("CRYPTO",dummy.getValue().getSymbol());
            assertEquals("Cryptocurrency",dummy.getValue().getName());
            assertEquals(1,dummy.getValue().getCmcRank());
            assertEquals(2,dummy.getValue().getNumMarketPairs());
            assertEquals(1000,dummy.getValue().getCirculatingSupply());
            assertEquals(1000,dummy.getValue().getTotalSupply());
            assertEquals(2000,dummy.getValue().getMaxSupply());
            assertEquals("2000-01-01 00:00:00 UTC",dummy.getValue().getLastUpdated());
            assertEquals("2000-01-01 00:00:00 UTC",dummy.getValue().getDateAdded());
            assertEquals(100,dummy.getValue().getPrice(),0);
            assertEquals(100,dummy.getValue().getVolume24H(), 0);
            assertEquals( 1,dummy.getValue().getPercentChange1H(), 0);
            assertEquals( -1,dummy.getValue().getPercentChange24H(), 0);
            assertEquals( 2,dummy.getValue().getPercentChange7D(), 0);
            assertEquals(100000,dummy.getValue().getMarketCap(),0);
        }
        //swap to offlineReport
        Report offlineReport = new Report();
        facade = new Facade(offlineData,offlineReport);
        listMap = facade.listSymbolName();
        list = listMap.get("OK");

        map = facade.getCurrencyInfo(list.get(0).getName(),false);
        try {
            verify(this.database,atLeastOnce()).getCachedCryptoData(anyString());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        for(Map.Entry<String, EntityData> dummy : map.entrySet()) {
            assertEquals("CRYPTO",dummy.getValue().getSymbol());
            assertEquals("Cryptocurrency",dummy.getValue().getName());
            assertEquals(1,dummy.getValue().getCmcRank());
            assertEquals(2,dummy.getValue().getNumMarketPairs());
            assertEquals(1000,dummy.getValue().getCirculatingSupply());
            assertEquals(1000,dummy.getValue().getTotalSupply());
            assertEquals(2000,dummy.getValue().getMaxSupply());
            assertEquals("2000-01-01 00:00:00 UTC",dummy.getValue().getLastUpdated());
            assertEquals("2000-01-01 00:00:00 UTC",dummy.getValue().getDateAdded());
            assertEquals(100,dummy.getValue().getPrice(),0);
            assertEquals(100,dummy.getValue().getVolume24H(), 0);
            assertEquals( 1,dummy.getValue().getPercentChange1H(), 0);
            assertEquals( -1,dummy.getValue().getPercentChange24H(), 0);
            assertEquals( 2,dummy.getValue().getPercentChange7D(), 0);
            assertEquals(100000,dummy.getValue().getMarketCap(),0);
        }
    }

    @Test
    public void setFeeTest(){
        //integer edge case check <0 or >99 or not int(string or float)
        ListData onlineData = new ListData("api-key", this.requestMaker, this.database);
        Report offlineReport = new Report();
        Facade facade = new Facade(onlineData,offlineReport);
        assertFalse(facade.setFee("INVALID"));
        assertFalse(facade.setFee("100"));
        assertFalse(facade.setFee("-1"));
        assertFalse(facade.setFee("1.23923"));
        assertFalse(facade.setFee(""));
        for(int i = 0; i < 100; i ++){
            assertTrue(facade.setFee(String.format("%d",i)));
        }
    }

    @Test
    public void conversionOnlineTest(){
        mockConversion();
        mockDatabase();
        mockCryptoData();

        ListData onlineData = new ListData("api-key", this.requestMaker, this.database);
        Report offlineReport = new Report();
        Facade facade = new Facade(onlineData,offlineReport);

        facade.setFee("10");

        Map<String, Double> btcToEth = facade.conversion("BTC","ETH","1",false);
        for(Map.Entry<String, Double> entry : btcToEth.entrySet()) {
            assertEquals(3.4514, entry.getValue(), 0.01);
            assertEquals("OK",entry.getKey());
        }

        facade.setFee("1");

        Map<String, Double> btcToUSDT = facade.conversion("BTC","USDT","100",false);
        for(Map.Entry<String, Double> entry : btcToUSDT.entrySet()) {
            assertEquals(919016.1784, entry.getValue(), 0.1);
            assertEquals("OK",entry.getKey());
        }

        facade.setFee("99");

        Map<String, Double> usdtToETH = facade.conversion("USDT","ETH","3000",false);
        for(Map.Entry<String, Double> entry : usdtToETH.entrySet()) {
            assertEquals(0.01239, entry.getValue(), 0.01);
            assertEquals("OK",entry.getKey());
        }
    }

    @Test
    public void conversionErrorOnlineTest(){
        mockConversion();
        mockDatabase();
        mockCryptoData();

        ListData onlineData = new ListData("api-key", this.requestMaker, this.database);
        Report offlineReport = new Report();
        Facade facade = new Facade(onlineData,offlineReport);
        facade.setFee("99");
        Map<String, Double> usdtToBtc = facade.conversion("USDT","BTC","OK",false);
        for(Map.Entry<String, Double> entry : usdtToBtc.entrySet()) {
            assertNull(entry.getValue());
            assertEquals("Warning: Please Enter a Numeric Input that is non-negative, non-zero and less than 1000000000",entry.getKey());
        }
        Map<String, Double> invalidFrom = facade.conversion("FAKE","BTC","300",false);
        for(Map.Entry<String, Double> entry : invalidFrom.entrySet()) {
            assertNull(entry.getValue());
            assertEquals("Warning: Make sure you have enter A valid symbol. Find currency symbols on the previous page",entry.getKey());
        }

        Map<String, Double> invalidTo = facade.conversion("ETH","djsjfdkshaj","300",false);
        for(Map.Entry<String, Double> entry : invalidTo.entrySet()) {
            assertNull(entry.getValue());
            assertEquals("Warning: Make sure you have enter A valid symbol. Find currency symbols on the previous page",entry.getKey());
        }

        Map<String, Double> numberTooLarge = facade.conversion("ETH","BTC","99999999999",false);
        for(Map.Entry<String, Double> entry : numberTooLarge.entrySet()) {
            assertNull(entry.getValue());
            assertEquals("Warning: Please Enter a Numeric Input that is non-negative, non-zero and less than 1000000000",entry.getKey());
        }

        Map<String, Double> numberZero = facade.conversion("ETH","BTC","0",false);
        for(Map.Entry<String, Double> entry : numberZero.entrySet()) {
            assertNull(entry.getValue());
            assertEquals("Warning: Please Enter a Numeric Input that is non-negative, non-zero and less than 1000000000",entry.getKey());
        }

        Map<String, Double> numberNegative = facade.conversion("ETH","BTC","-100",false);
        for(Map.Entry<String, Double> entry : numberNegative.entrySet()) {
            assertNull(entry.getValue());
            assertEquals("Warning: Please Enter a Numeric Input that is non-negative, non-zero and less than 1000000000",entry.getKey());
        }

        Map<String, Double> emptyFrom = facade.conversion("","BTC","1",false);
        for(Map.Entry<String, Double> entry : emptyFrom.entrySet()) {
            assertNull(entry.getValue());
            assertEquals("Warning: Empty input detected",entry.getKey());
        }

        Map<String, Double> emptyTo = facade.conversion("BTC","","1",false);
        for(Map.Entry<String, Double> entry : emptyTo.entrySet()) {
            assertNull(entry.getValue());
            assertEquals("Warning: Empty input detected",entry.getKey());
        }

        Map<String, Double> emptyAmount = facade.conversion("BTC","BTC","",false);
        for(Map.Entry<String, Double> entry : emptyAmount.entrySet()) {
            assertNull(entry.getValue());
            assertEquals("Warning: Empty input detected",entry.getKey());
        }
    }

    @Test
    public void conversionCachedOnlineTest(){
        mockConversion();
        mockDatabase();
        mockCryptoData();
        ListData onlineData = new ListData("api-key", this.requestMaker, this.database);
        Report onlineReport = new Report("api-key", this.requestMaker);
        Facade facade = new Facade(onlineData,onlineReport);

        facade.conversion("BTC","ETH","1",true);
        try {
            verify(this.database,atLeastOnce()).getCachedConversionPrices(anyString(),anyString());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        facade.setFee("10");

        Map<String, Double> btcToEth = facade.conversion("BTC","ETH","1",true);
        for(Map.Entry<String, Double> entry : btcToEth.entrySet()) {
            assertEquals(0.9, entry.getValue(), 0.01);
            assertEquals("OK",entry.getKey());
        }

        facade.setFee("23");

        Map<String, Double> btcToUSDT = facade.conversion("BTC","USDT","99",true);
        for(Map.Entry<String, Double> entry : btcToUSDT.entrySet()) {
            assertEquals(7623, entry.getValue(), 0.01);
            assertEquals("OK",entry.getKey());
        }

        facade.setFee("99");

        Map<String, Double> usdtToETH = facade.conversion("USDT","ETH","3000",true);
        for(Map.Entry<String, Double> entry : usdtToETH.entrySet()) {
            assertEquals(0.3, entry.getValue(), 0.01);
            assertEquals("OK",entry.getKey());
        }
    }

    @Test
    public void conversionErrorCachedOnlineTest(){
        mockConversion();
        mockDatabase();
        mockCryptoData();
        ListData onlineData = new ListData("api-key", this.requestMaker, this.database);
        Report onlineReport = new Report("api-key", this.requestMaker);
        Facade facade = new Facade(onlineData,onlineReport);

        facade.setFee("50");

        Map<String, Double> numberTooLarge = facade.conversion("ETH","BTC","99999999999",true);
        for(Map.Entry<String, Double> entry : numberTooLarge.entrySet()) {
            assertNull(entry.getValue());
            assertEquals("Warning: Please Enter a Numeric Input that is non-negative, non-zero and less than 1000000000",entry.getKey());
        }

        Map<String, Double> numberZero = facade.conversion("ETH","BTC","0",true);
        for(Map.Entry<String, Double> entry : numberZero.entrySet()) {
            assertNull(entry.getValue());
            assertEquals("Warning: Please Enter a Numeric Input that is non-negative, non-zero and less than 1000000000",entry.getKey());
        }

        Map<String, Double> numberNegative = facade.conversion("ETH","BTC","-100",true);
        for(Map.Entry<String, Double> entry : numberNegative.entrySet()) {
            assertNull(entry.getValue());
            assertEquals("Warning: Please Enter a Numeric Input that is non-negative, non-zero and less than 1000000000",entry.getKey());
        }

        Map<String, Double> emptyFrom = facade.conversion("","BTC","1",true);
        for(Map.Entry<String, Double> entry : emptyFrom.entrySet()) {
            assertNull(entry.getValue());
            assertEquals("Warning: Empty input detected",entry.getKey());
        }

        Map<String, Double> emptyTo = facade.conversion("BTC","","1",true);
        for(Map.Entry<String, Double> entry : emptyTo.entrySet()) {
            assertNull(entry.getValue());
            assertEquals("Warning: Empty input detected",entry.getKey());
        }

        Map<String, Double> emptyAmount = facade.conversion("BTC","BTC","",true);
        for(Map.Entry<String, Double> entry : emptyAmount.entrySet()) {
            assertNull(entry.getValue());
            assertEquals("Warning: Empty input detected",entry.getKey());
        }
    }

    @Test
    public void conversionOfflineTest(){
        mockDatabase();
        ListData offlineData = new ListData(this.database);
        Report offlineReport = new Report();
        Facade facade = new Facade(offlineData,offlineReport);

        facade.setFee("50");
        Map<String, Double> dummy50 = facade.conversion("CRYPTO","CRYPTO","1",false);
        for(Map.Entry<String, Double> entry : dummy50.entrySet()) {
            assertEquals(0.5, entry.getValue(), 0.01);
            assertEquals("OK",entry.getKey());
        }

        facade.setFee("0");
        Map<String, Double> dummy = facade.conversion("CRYPTO","CRYPTO","250",false);
        for(Map.Entry<String, Double> entry : dummy.entrySet()) {
            assertEquals(250, entry.getValue(), 0.01);
            assertEquals("OK",entry.getKey());
        }

        facade.setFee("1");
        Map<String, Double> dummy1 = facade.conversion("CRYPTO","CRYPTO","250",false);
        for(Map.Entry<String, Double> entry : dummy1.entrySet()) {
            assertEquals(247.5, entry.getValue(), 0.01);
            assertEquals("OK",entry.getKey());
        }

        facade.setFee("99");
        Map<String, Double> dummy99 = facade.conversion("CRYPTO","CRYPTO","250",false);
        for(Map.Entry<String, Double> entry : dummy99.entrySet()) {
            assertEquals(2.5, entry.getValue(), 0.01);
            assertEquals("OK",entry.getKey());
        }
    }

    @Test
    public void conversionErrorOfflineTest(){
        mockDatabase();
        mockConversion();
        ListData offlineData = new ListData(this.database);
        Report offlineReport = new Report();
        Facade facade = new Facade(offlineData,offlineReport);

        facade.setFee("1");
        Map<String, Double> dummy = facade.conversion("CRYPTO","DUMMY","1",false);
        for(Map.Entry<String, Double> entry : dummy.entrySet()) {
            assertNull(entry.getValue());
            assertEquals("Warning: Make sure you have enter A valid symbol. Find currency symbols on the previous page",entry.getKey());
        }
        Map<String, Double> invalidSymbol = facade.conversion("CRYPTO","DUMMY","40",true);
        for(Map.Entry<String, Double> entry : invalidSymbol.entrySet()) {
            assertNull(entry.getValue());
            assertEquals("Warning: Make sure you have enter A valid symbol. Find currency symbols on the previous page",entry.getKey());
        }

        Map<String, Double> numberTooLarge = facade.conversion("CRYPTO","CRYPTO","99999999999",false);
        for(Map.Entry<String, Double> entry : numberTooLarge.entrySet()) {
            assertNull(entry.getValue());
            assertEquals("Warning: Please Enter a Numeric Input that is non-negative, non-zero and less than 1000000000",entry.getKey());
        }

        Map<String, Double> numberZero = facade.conversion("CRYPTO","CRYPTO","0",false);
        for(Map.Entry<String, Double> entry : numberZero.entrySet()) {
            assertNull(entry.getValue());
            assertEquals("Warning: Please Enter a Numeric Input that is non-negative, non-zero and less than 1000000000",entry.getKey());
        }

        Map<String, Double> numberNegative = facade.conversion("CRYPTO","CRYPTO","-100",false);
        for(Map.Entry<String, Double> entry : numberNegative.entrySet()) {
            assertNull(entry.getValue());
            assertEquals("Warning: Please Enter a Numeric Input that is non-negative, non-zero and less than 1000000000",entry.getKey());
        }

        Map<String, Double> emptyFrom = facade.conversion("","BTC","1",false);
        for(Map.Entry<String, Double> entry : emptyFrom.entrySet()) {
            assertNull(entry.getValue());
            assertEquals("Warning: Empty input detected",entry.getKey());
        }

        Map<String, Double> emptyTo = facade.conversion("BTC","","1",false);
        for(Map.Entry<String, Double> entry : emptyTo.entrySet()) {
            assertNull(entry.getValue());
            assertEquals("Warning: Empty input detected",entry.getKey());
        }

        Map<String, Double> emptyAmount = facade.conversion("BTC","BTC","",false);
        for(Map.Entry<String, Double> entry : emptyAmount.entrySet()) {
            assertNull(entry.getValue());
            assertEquals("Warning: Empty input detected",entry.getKey());
        }


    }

    @Test
    public void conversionCachedOfflineTest() {
        mockDatabase();
        mockConversion();
        ListData offlineData = new ListData(this.database);
        Report onlineReport = new Report("api-key", this.requestMaker);
        Facade facade = new Facade(offlineData, onlineReport);

        facade.setFee("40");
        Map<String, Double> dummy40 = facade.conversion("CRYPTO", "CRYPTO", "1", true);
        try {
            verify(this.database, atLeastOnce()).getCachedConversionPrices(anyString(), anyString());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        for (Map.Entry<String, Double> entry : dummy40.entrySet()) {
            assertEquals(0.6, entry.getValue(), 0.01);
            assertEquals("OK", entry.getKey());
        }

        facade.setFee("9");
        Map<String, Double> dummy9 = facade.conversion("CRYPTO", "CRYPTO", "200000", true);
        for (Map.Entry<String, Double> entry : dummy9.entrySet()) {
            assertEquals(182000, entry.getValue(), 0.01);
            assertEquals("OK", entry.getKey());
        }
    }

    @Test
    public void conversionErrorCachedOfflineTest() {
        mockDatabase();
        mockConversion();
        ListData offlineData = new ListData(this.database);
        Report onlineReport = new Report("api-key", this.requestMaker);
        Facade facade = new Facade(offlineData, onlineReport);

        facade.setFee("40");

        Map<String, Double> invalidSymbol = facade.conversion("CRYPTO", "DUMMY", "40", true);
        for (Map.Entry<String, Double> entry : invalidSymbol.entrySet()) {
            assertNull(entry.getValue());
            assertEquals("Warning: Make sure you have enter A valid symbol. Find currency symbols on the previous page", entry.getKey());
        }

        Map<String, Double> numberTooLarge = facade.conversion("CRYPTO", "CRYPTO", "99999999999", true);
        for (Map.Entry<String, Double> entry : numberTooLarge.entrySet()) {
            assertNull(entry.getValue());
            assertEquals("Warning: Please Enter a Numeric Input that is non-negative, non-zero and less than 1000000000", entry.getKey());
        }

        Map<String, Double> numberZero = facade.conversion("CRYPTO", "CRYPTO", "0", true);
        for (Map.Entry<String, Double> entry : numberZero.entrySet()) {
            assertNull(entry.getValue());
            assertEquals("Warning: Please Enter a Numeric Input that is non-negative, non-zero and less than 1000000000", entry.getKey());
        }

        Map<String, Double> numberNegative = facade.conversion("CRYPTO", "CRYPTO", "-100", true);
        for (Map.Entry<String, Double> entry : numberNegative.entrySet()) {
            assertNull(entry.getValue());
            assertEquals("Warning: Please Enter a Numeric Input that is non-negative, non-zero and less than 1000000000", entry.getKey());
        }

        Map<String, Double> emptyFrom = facade.conversion("", "BTC", "1", true);
        for (Map.Entry<String, Double> entry : emptyFrom.entrySet()) {
            assertNull(entry.getValue());
            assertEquals("Warning: Empty input detected", entry.getKey());
        }

        Map<String, Double> emptyTo = facade.conversion("BTC", "", "1", true);
        for (Map.Entry<String, Double> entry : emptyTo.entrySet()) {
            assertNull(entry.getValue());
            assertEquals("Warning: Empty input detected", entry.getKey());
        }

        Map<String, Double> emptyAmount = facade.conversion("BTC", "BTC", "", true);
        for (Map.Entry<String, Double> entry : emptyAmount.entrySet()) {
            assertNull(entry.getValue());
            assertEquals("Warning: Empty input detected", entry.getKey());
        }
    }

    @Test
    public void getReportLinkOnlineTest(){
        mockConversion();
        mockCryptoData();
        mockDatabase();
        mockReportRequest();
        ListData onlineData = new ListData("api-key", this.requestMaker, this.database);
        Report onlineReport = new Report("api-key", this.requestMaker);
        Facade facade = new Facade(onlineData,onlineReport);

        Map<String,List<NameValuePair>> listMap = facade.listSymbolName();
        List<NameValuePair> list = listMap.get("OK");

        Map<String,EntityData> map = facade.getCurrencyInfo(list.get(0).getName(),false);
        Map<String, String> link = facade.getReportLink(map.get("OK"),"BTC");
        for(Map.Entry<String, String> entry : link.entrySet()) {
            int index = entry.getValue().lastIndexOf("/");
            String sub = entry.getValue().substring(0, index);
            String remainder = entry.getValue().substring(index);
            assertEquals(sub, "https://pastebin.com");
            assertEquals(remainder, "/abcdefg");
        }

        ListData offlineData = new ListData(this.database);
        facade = new Facade(offlineData,onlineReport);
        listMap = facade.listSymbolName();
        list = listMap.get("OK");

        map = facade.getCurrencyInfo(list.get(0).getName(),false);
        link = facade.getReportLink(map.get("OK"),"BTC");
        for(Map.Entry<String, String> entry : link.entrySet()) {
            int index = entry.getValue().lastIndexOf("/");
            String sub = entry.getValue().substring(0, index);
            String remainder = entry.getValue().substring(index);
            assertEquals(sub, "https://pastebin.com");
            assertEquals(remainder, "/abcdefg");
        }

    }

    @Test
    public void getReportLinkOfflineTest(){
        mockConversion();
        mockCryptoData();
        mockDatabase();
        mockReportRequest();
        ListData onlineData = new ListData("api-key", this.requestMaker, this.database);
        Report offlineReport = new Report();
        Facade facade = new Facade(onlineData,offlineReport);
        Map<String,List<NameValuePair>> listMap = facade.listSymbolName();
        List<NameValuePair> list = listMap.get("OK");

        Map<String, EntityData> map = facade.getCurrencyInfo(list.get(0).getName(),false);
        Map<String, String> link = facade.getReportLink(map.get("OK"),"BTC");
        for(Map.Entry<String, String> entry : link.entrySet()) {
            int index = entry.getValue().lastIndexOf("/");
            String sub = entry.getValue().substring(0, index);
            String remainder = entry.getValue().substring(index);
            assertEquals(sub, "https://pastebin.com");
            assertEquals(remainder.length(), 9);
        }

        ListData offlineData = new ListData(this.database);
        facade = new Facade(offlineData,offlineReport);
        listMap = facade.listSymbolName();
        list = listMap.get("OK");

        map = facade.getCurrencyInfo(list.get(0).getName(),false);
        link = facade.getReportLink(map.get("OK"),"BTC");
        for(Map.Entry<String, String> entry : link.entrySet()) {
            int index = entry.getValue().lastIndexOf("/");
            String sub = entry.getValue().substring(0, index);
            String remainder = entry.getValue().substring(index);
            assertEquals(sub, "https://pastebin.com");
            assertEquals(remainder.length(), 9);
        }
    }
}
