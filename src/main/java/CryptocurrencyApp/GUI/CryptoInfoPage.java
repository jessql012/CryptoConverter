package CryptocurrencyApp.GUI;

import CryptocurrencyApp.Controller.EntityData;
import CryptocurrencyApp.Controller.Facade;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;

/**
 * This class creates a GUI window that displays information on the chosen cryptocurrency,
 * generate a report on the cryptocurrency, and calculate conversions between cryptocurrencies.
 */
public class CryptoInfoPage{

    private EntityData currencyData;
    private Facade facade;

    protected JFrame mainFrame;
    private JPanel mainPanel;
    private JLabel mainLabel;

    //textfield where the report link will be displayed, can be highlighted and copied from
    private JTextField reportLink;

    //textfield for user to input currency they would like to convert to
    private JTextField conversionCurrency;
    //amount that the user would like to convert
    private JTextField amount;
    //where the result of the conversion, or the error message will be displayed
    private JLabel resultLabel;
    //general error label with multiple uses (report error and concurrent requests error)
    private JLabel warningLabel;

    private JButton getReportButton;
    private JButton baseToEnteredButton;
    private JButton enteredToBaseButton;

    //The thread that facade methods will run on
    private SwingWorker sw = null;

    public CryptoInfoPage(EntityData currencyData, Facade facade){
        this.facade = facade;
        this.currencyData = currencyData;

        this.mainFrame = new JFrame("CryptocurrencyApp");
        this.mainPanel = new JPanel();
        this.mainPanel.setLayout(null);
        this.mainFrame.setBounds(0,0,950,650);
        this.mainFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.mainFrame.setVisible(true);
        this.mainFrame.add(this.mainPanel);
        buildPage();
    }

    private String strFormatterHeading(String str){
        return String.format("<html><h1> %s </h1></html>", str);
    }

    private String strFormatter(String str){
        return String.format("<html><p style = \" text-align:center; font-size = 20px ;width: 300;\"> %s </p></html>", str);
    }


    private void buildPage() {
        this.mainLabel = new JLabel();
        this.mainLabel.setBounds(250,100,600,200);
        this.mainPanel.add(this.mainLabel);

        //Display all info from EntityData object
        JLabel title = new JLabel(strFormatterHeading(currencyData.getSymbol()));
        title.setBounds(50, 30, 400, 75);
        this.mainPanel.add(title);
        JLabel name = new JLabel(strFormatter(String.format("Name: %s", currencyData.getName())));
        name.setBounds(50, 75, 400, 75);
        this.mainPanel.add(name);
        JLabel cmc_rank = new JLabel(strFormatter(String.format("CMC Rank: %d", currencyData.getCmcRank())));
        cmc_rank.setBounds(50, 100, 400, 75);
        this.mainPanel.add(cmc_rank);

        JLabel price = new JLabel(strFormatter(String.format("Price: %.5f USD", currencyData.getPrice())));
        price.setBounds(50, 125, 400, 75);
        this.mainPanel.add(price);
        JLabel volume24H = new JLabel(strFormatter(String.format("Volume Last 24H: %.5f USD", currencyData.getVolume24H())));
        volume24H.setBounds(50, 150, 400, 75);
        this.mainPanel.add(volume24H);
        JLabel percentChange1H = new JLabel(strFormatter(String.format("Percentage Change Last 1H: %.5f%%", currencyData.getPercentChange1H())));
        percentChange1H.setBounds(50, 175, 400, 75);
        this.mainPanel.add(percentChange1H);
        JLabel percentChange24H = new JLabel(strFormatter(String.format("Percentage Change Last 24H: %.5f%%", currencyData.getPercentChange24H())));
        percentChange24H.setBounds(50, 200, 400, 75);
        this.mainPanel.add(percentChange24H);
        JLabel percentChange7D = new JLabel(strFormatter(String.format("Percentage Change Last 7 Days: %.5f%%", currencyData.getPercentChange7D())));
        percentChange7D.setBounds(50, 225, 400, 75);
        this.mainPanel.add(percentChange7D);
        JLabel marketCap = new JLabel(strFormatter(String.format("Market Cap: %.5f USD", currencyData.getMarketCap())));
        marketCap.setBounds(50, 250, 400, 75);
        this.mainPanel.add(marketCap);

        JLabel num_market_pairs = new JLabel(strFormatter(String.format("Number of Market Pairs: %d", currencyData.getNumMarketPairs())));
        num_market_pairs.setBounds(400, 125, 400, 75);
        this.mainPanel.add(num_market_pairs);
        JLabel circulating_supply = new JLabel(strFormatter(String.format("Circulating Supply: %d", currencyData.getCirculatingSupply())));
        circulating_supply.setBounds(400, 150, 400, 75);
        this.mainPanel.add(circulating_supply);
        JLabel total_supply = new JLabel(strFormatter(String.format("Total Supply: %d", currencyData.getTotalSupply())));
        total_supply.setBounds(400, 175, 400, 75);
        this.mainPanel.add(total_supply);
        JLabel max_supply = new JLabel(strFormatter(String.format("Max Supply: %d", currencyData.getMaxSupply())));
        max_supply.setBounds(400, 200, 400, 75);
        this.mainPanel.add(max_supply);
        JLabel last_updated = new JLabel(strFormatter(String.format("Last Updated: %s", currencyData.getLastUpdated().toString())));
        last_updated.setBounds(400, 225, 400, 75);
        this.mainPanel.add(last_updated);
        JLabel date_added = new JLabel(strFormatter(String.format("Date Added: %s", currencyData.getDateAdded().toString())));
        date_added.setBounds(400, 250, 400, 75);
        this.mainPanel.add(date_added);

        //Display currency conversion, report, and back button functionality GUI
        this.resultLabel = new JLabel();
        this.resultLabel.setBounds(50, 500, 400, 75);
        this.mainPanel.add(resultLabel);

        JLabel currencyConversionLabel = new JLabel(strFormatterHeading("Currency Conversion:"));
        currencyConversionLabel.setBounds(50, 350, 400, 75);
        this.mainPanel.add(currencyConversionLabel);

        JLabel currencySymbolLabel = new JLabel(strFormatter("Enter a Cryptocurrency Symbol for Conversion:"));
        currencySymbolLabel.setBounds(50, 400, 400, 75);
        this.mainPanel.add(currencySymbolLabel);

        this.conversionCurrency = new JTextField();
        this.conversionCurrency.setBounds(50,450,100,25);
        this.mainPanel.add(conversionCurrency);

        JLabel amountLabel = new JLabel(strFormatter("Enter an Amount:"));
        amountLabel.setBounds(400, 400, 400, 75);
        this.mainPanel.add(amountLabel);

        this.amount = new JTextField();
        this.amount.setBounds(400,450,100,25);
        this.mainPanel.add(amount);

        //initialise the conversion buttons
        this.enteredToBaseButton = new JButton("Convert to " + currencyData.getSymbol());
        enteredToBaseButton.setBounds(700,475,200,50);
        enteredToBaseButton.addActionListener(e -> conversionButtonAction(enteredToBaseButton));
        this.mainPanel.add(enteredToBaseButton);

        this.baseToEnteredButton = new JButton("Convert from " + currencyData.getSymbol());
        baseToEnteredButton.setBounds(700,400,200,50);
        baseToEnteredButton.addActionListener(e -> conversionButtonAction(baseToEnteredButton));
        this.mainPanel.add(baseToEnteredButton);

        //initialise the back button
        JButton backButton = new JButton("Back");
        backButton.setBounds(700,100,200,50);
        backButton.addActionListener(e -> mainFrame.dispose());
        this.mainPanel.add(backButton);

        //initialise the get report button
        this.getReportButton = new JButton("Get Report");
        getReportButton.setBounds(700,175,200,50);
        getReportButton.addActionListener(e -> reportButtonAction());
        this.mainPanel.add(getReportButton);

        this.reportLink = new JTextField();
        this.reportLink.setBounds(700,250,200,25);
        this.mainPanel.add(this.reportLink);

        this.warningLabel = new JLabel();
        warningLabel.setBounds(700, 300, 200, 100);
        this.mainPanel.add(warningLabel);


        this.mainFrame.setContentPane(this.mainPanel);
    }

    private void conversionButtonAction(JButton b){
        //restore all warning label texts
        warningLabel.setText(strFormatter(""));
        resultLabel.setText("");

        //retrieve user input
        String baseText = currencyData.getSymbol();
        String enteredText = conversionCurrency.getText();

        //Make sure no calls to the facade object are made simultaneously

        if(sw == null || sw.isDone()){
            if(b.equals(baseToEnteredButton)){
                startConversionThread(b,baseText,enteredText,amount.getText(), currencyData.isCached());
            }
            else if(b.equals(enteredToBaseButton)){
                startConversionThread(b,enteredText,baseText,amount.getText(), currencyData.isCached());
            }

        }
        else{
            warningLabel.setText(strFormatter("Warning: Too many requests at once, try again later"));
        }
    }

    private void reportButtonAction(){
        warningLabel.setText(strFormatter(""));
        if(sw == null || sw.isDone()){
            startReportThread(getReportButton,currencyData);
        }
        else{
            warningLabel.setText(strFormatter("Warning: Too many requests at once, try again later"));
        }
    }

    private void startReportThread(JButton b, EntityData currencyData) {
        this.sw = new SwingWorker() {
            private String bText = b.getText();

            @Override
            protected Map<String, String> doInBackground(){
                publish();
                b.setEnabled(false);
                Map<String, String> map = facade.getReportLink(currencyData, currencyData.getSymbol());
                for(Map.Entry<String, String> entry : map.entrySet()) {
                    if (entry.getKey().equals("OK")) {
                        reportLink.setText(entry.getValue());
                    }
                    else {
                        warningLabel.setText(strFormatter(entry.getKey()));
                    }
                }
                return map;
            }

            @Override
            protected void process(List chunks) {
                b.setText("Loading...");
            }

            @Override
            protected void done() {
                b.setText(bText);
                b.setEnabled(true);

            }
        };
        this.sw.execute();
    }

    private void startConversionThread(JButton b, String from, String to, String amount, Boolean isCached) {
        this.sw = new SwingWorker() {
            private String bText = b.getText();

            @Override
            protected Map<String, Double> doInBackground() throws Exception {
                publish();
                b.setEnabled(false);
                Map<String, Double> map = facade.conversion(from, to, amount, false);
                for (Map.Entry<String, Double> entry : map.entrySet()) {
                    if (entry.getKey().equals("OK")) {
                        resultLabel.setText(String.format("%s %s is %f %s", amount, from, entry.getValue(), to));
                    } else {
                        resultLabel.setText(strFormatter(entry.getKey()));
                    }
                }
                return map;
            }

            @Override
            protected void process(List chunks) {
                b.setText("Loading...");
            }

            @Override
            protected void done() {
                Map<String, Double> map = null;
                b.setText(bText);
                b.setEnabled(true);
            }
        };
        this.sw.execute();

    }


}
