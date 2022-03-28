package CryptocurrencyApp.GUI;

import CryptocurrencyApp.Controller.EntityData;
import CryptocurrencyApp.Controller.Facade;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * This class creates a GUI window that the give options of the info type available i.e. latest, cached.
 */
public class SelectInfoTypePage {
    private Facade facade;
    private String symbol;
    protected JFrame mainFrame;
    private JPanel mainPanel;
    private JLabel mainLabel;
    private boolean firstCall;
    private JLabel warningLabel;

    private JButton uncachedButton;
    private JButton cachedButton;

    private ListCryptoPage prevPage;

    //The thread that facade methods will run on
    private SwingWorker sw = null;

    public SelectInfoTypePage(Facade facade, ListCryptoPage prevPage, String symbol, boolean firstCall) {
        this.facade = facade;
        this.symbol = symbol;
        this.firstCall = firstCall;
        this.prevPage = prevPage;
        this.mainFrame = new JFrame("CryptocurrencyApp");
        this.mainFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.mainPanel = new JPanel();
        this.mainPanel.setLayout(null);
        this.mainFrame.setBounds(0, 0, 350, 300);
        this.mainFrame.setVisible(true);
        buildPage();
    }

    private String strFormatter(String str) {
        return String.format("<html><p style = \"text-align: center; width: 100px; align-content: center\"> %s </p></html>", str);
    }

    private void buildPage() {
        this.mainLabel = new JLabel(strFormatter(symbol));
        mainLabel.setBounds(100, 50, 130, 50);
        mainPanel.add(mainLabel);

        this.warningLabel = new JLabel();
        warningLabel.setBounds(100, 200, 130, 50);
        mainPanel.add(warningLabel);

        this.uncachedButton = new JButton("Latest Info");
        uncachedButton.setBounds(100, 100, 130, 50);
        mainPanel.add(uncachedButton);

        uncachedButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                //Make sure no calls to the facade object are made simultaneously
                if(sw == null || sw.isDone()){
                    startCurrencyInfoThread(uncachedButton, symbol, false);
                }
                else{
                    warningLabel.setText(strFormatter("Warning: Too many requests at once, try again later"));
                }
            }

        });

        if(!this.firstCall){
            this.cachedButton = new JButton("Cached Info");
            cachedButton.setBounds(100, 160, 130, 50);
            mainPanel.add(cachedButton);
            cachedButton.addActionListener(new ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    //Make sure no calls to the facade object are made simultaneously
                    if(sw == null || sw.isDone()){
                        startCurrencyInfoThread(cachedButton, symbol, true);
                    }
                    else{
                        warningLabel.setText(strFormatter("Warning: Too many requests at once, try again later"));
                    }
                }
            });
        }

        this.mainFrame.setContentPane(mainPanel);
    }

    private void startCurrencyInfoThread(JButton b, String str, Boolean isCached){
        this.sw = new SwingWorker()
        {
            private String bText = b.getText();

            @Override
            protected Map<String, EntityData> doInBackground() throws Exception
            {
                //The thread will retrieve the appropriate information depending on the button pressed
                //and the symbol of the currency of interest
                publish();
                warningLabel.setText("");
                b.setEnabled(false);
                Map<String, EntityData> map = facade.getCurrencyInfo(str, isCached);
                return map;
            }

            @Override
            protected void process(List chunks)
            {
                //This will show on the warning panel while the thread is running
                b.setText("Loading...");
            }

            @Override
            protected void done()
            {
                //This will run after the thread is done with the doInBackground method
                try
                {
                    Map<String, EntityData> map =  (Map<String, EntityData>) get();
                    b.setText(bText);
                    b.setEnabled(true);

                    //Navigate to the currency info page using the info retrieved
                    navInfoPage(map);
                }
                catch (InterruptedException | ExecutionException e)
                {
                    e.printStackTrace();
                }
            }
        };
        this.sw.execute();

    }

    //This method will navigate to the next page when a get info button has been pressed and the data has been
    //retrieved successfully
    private void navInfoPage(Map<String, EntityData> resultMap) {
        for (Map.Entry<String, EntityData> entry : resultMap.entrySet()) {
            if (entry.getKey().equals("OK")) {
                new CryptoInfoPage(resultMap.get("OK"), facade);

                //Cached data now available
                prevPage.firstCall = false;
                this.mainFrame.dispose();
            } else {
                this.warningLabel.setText(strFormatter(entry.getKey()));
            }

        }
    }
}
