package CryptocurrencyApp.GUI;

import CryptocurrencyApp.Controller.EntityData;
import CryptocurrencyApp.Controller.Facade;
import org.apache.http.NameValuePair;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * This class creates a GUI window that displays all the cryptocurrencies available.
 */
public class ListCryptoPage{
    private Facade facade;
    protected JFrame mainFrame;
    private final JPanel mainPanel;

    //panel that displays call currency cards
    private JPanel currencyPanel;

    //panel and label for error messages
    private JPanel warningPanel;
    private JLabel warningLabel;

    //The dimensions of the currency panel will change depending on the size of the data loaded into the page
    private int x;
    private int y;

    //If it is the first time to request from the api
    protected boolean firstCall = true;

    //The thread that facade methods will run on
    private SwingWorker sw = null;


    public ListCryptoPage(Facade facade) {
        this.facade = facade;
        this.mainFrame = new JFrame("CryptocurrencyApp");
        this.mainPanel = new JPanel();
        this.mainPanel.setLayout(null);
        this.mainFrame.setBounds(x, y, 1450, 800);
        this.mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.mainFrame.setVisible(true);
        buildPage();
    }

    private String strFormatterHeading(String str){
        return String.format("<html><h1> %s </h1></html>", str);
    }

    private String strDescriptionFormatter(String str) {
        return String.format("<html><p> %s </p></html>", str);
    }

    //Formats the string in html so that it wraps depending on the width
    private String strFormatter(String str) {
        return String.format("<html><p style = \"text-align: center; width: 100px; align-content: center\"> %s </p></html>", str);
    }



    private void buildPage() {
        //title List of Crypto Currencies
        JLabel title = new JLabel(strFormatterHeading("List of Cryptocurrencies"));
        title.setBounds(30, 10, 600, 50);
        this.mainPanel.add(title);

        //Welcome text
        JLabel welcome = new JLabel(strDescriptionFormatter("Hello! Welcome to CoinMarketCap! Select a cryptocurrency to see more details. <br> Click on the cached data button for data of the cryptocurrency" +
                " stored in out database, or click latest data for a fresh copy of the cryptocurrency's data from the CMC API."));
        welcome.setBounds(30, 50, 1400, 75);
        this.mainPanel.add(welcome);

        this.currencyPanel = new JPanel();
        this.currencyPanel.setLayout(null);
        JScrollPane scrollPane = new JScrollPane(currencyPanel);
        scrollPane.getVerticalScrollBar().setUnitIncrement(25);
        scrollPane.getHorizontalScrollBar().setUnitIncrement(25);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPane.setBounds(20, 120, 1015, 650);

        this.mainPanel.setPreferredSize(new Dimension(1200, 800));
        this.mainPanel.add(scrollPane);

        this.warningPanel = new JPanel();
        this.warningPanel.setBounds(1050, 120,130,650);
        this.warningPanel.setBackground(Color.white);
        this.mainPanel.add(warningPanel);

        this.warningLabel = new JLabel();
        this.warningLabel.setBounds(1060,  150, 100, 650);
        this.warningPanel.add(warningLabel);

        //Thread that calls the facade object which retrieves data and displays the data in the form of panels/cards.
        startListSymbolsThread();

        mainFrame.setContentPane(mainPanel);
        //pack the window to the appropriate size after all currency cards has been displayed
        mainFrame.pack();
    }




    //Method inspired from previously done SOFT2412 assignment GUI code
    //Creates different panels representing different cryptocurrencies
    //Includes their symbol name, and a button to retrieve latest info, button to retreive cached info.
    private void createCard(NameValuePair cardContent, int x, int y) {
        JPanel panel = new JPanel();
        panel.setBounds(x, y, 150, 150);
        panel.setLayout(null);

        JLabel symbol = new JLabel(strFormatter(cardContent.getName()));
        Font nameFont = new Font(symbol.getFont().getName(), Font.BOLD, 15);
        symbol.setFont(nameFont);
        symbol.setBounds(10, 0, 150, 25);
        panel.add(symbol);

        JLabel name = new JLabel(strFormatter(cardContent.getValue()));
        Font mdFont = new Font(symbol.getFont().getName(), Font.PLAIN, 12);
        name.setFont(mdFont);
        name.setBounds(10, 25, 150, 25);
        panel.add(name);

        JButton selectButton = new JButton("Select");
        selectButton.setBounds(10, 80, 130, 50);
        panel.add(selectButton);
        selectButton.addActionListener(e -> navSelectPage(cardContent.getName()));

        panel.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0)));
        this.currencyPanel.add(panel);

    }

    private void startListSymbolsThread(){
        this.sw = new SwingWorker()
        {
            @Override
            protected Map<String, List<NameValuePair>> doInBackground() throws Exception
            {
                //The thread will create cards from the data and add them to the GUI
                //publish() will call the processing method
                publish();
                Map<String,List<NameValuePair>> map = facade.listSymbolName();
                for(Map.Entry<String, List<NameValuePair>> entry : map.entrySet()) {
                    if (!entry.getKey().equals("OK")) {
                        return map;
                    }
                }
                int numRow = 0;
                for (int i = 0; i < map.get("OK").size(); i++) {
                    if (numRow == 5) {
                        numRow = 0;
                    }
                    createCard(map.get("OK").get(i), 25 + 200 * numRow, 200 * (i / 5) + 50);
                    if (x < (20 + 200 * numRow + 175)) {
                        x = 20 + 200 * numRow + 175;
                    }
                    if (y < (200 * (i / 5) + 50 + 200)) {
                        y = 200 * (i / 5) + 50 + 200;
                    }
                    numRow++;
                }
                currencyPanel.setPreferredSize(new Dimension(x, y));
                return map;
            }

            @Override
            protected void process(List chunks)
            {
                //This will show on the warning panel while the thread is running
                warningLabel.setText(strFormatter("Loading in the Cryptocurrency data..."));
            }

            @Override
            protected void done()
            {
                //This will run after the thread is done with the doInBackground method
                try {
                    Map<String,List<NameValuePair>> map = (Map<String,List<NameValuePair>>) get();
                    for(Map.Entry<String, List<NameValuePair>> entry : map.entrySet()) {
                        if (entry.getKey().equals("OK")) {
                            warningLabel.setText(strFormatter("Done loading in the Cryptocurrency data!"));
                        }
                        else{
                            warningLabel.setText(strFormatter(entry.getKey()));
                        }
                    }
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }
        };
        this.sw.execute();


    }

    private void navSelectPage(String symbol){
        new SelectInfoTypePage(facade,this,symbol,firstCall);
    }

}
