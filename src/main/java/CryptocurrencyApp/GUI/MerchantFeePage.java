package CryptocurrencyApp.GUI;

import CryptocurrencyApp.Controller.Facade;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * This class creates a GUI window that asks the user to enter a merchant fee percentage for currency exchanges.
 */
public class MerchantFeePage {
    private Facade facade;
    protected JFrame mainFrame;
    private JPanel mainPanel;
    private JLabel mainLabel;
    private JLabel warningLabel;
    private JButton submitButton;

    //Where the user will enter their input
    private JTextField textField;
    //The thread that facade methods will run on
    private SwingWorker sw = null;

    public MerchantFeePage(Facade facade){
        this.facade = facade;
        this.mainFrame = new JFrame("CryptocurrencyApp");
        this.mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.mainFrame.setLayout(new GridLayout(3, 1));
        this.mainFrame.setBounds(0, 0, 400, 300);
        this.mainPanel = new JPanel();
        this.mainPanel.setLayout(null);
        buildPage();
    }

    private String strFormatter(String str) {
        return String.format("<html><p style = \"text-align: center; width: 200px; align-content: center\"> %s </p></html>", str);
    }

    private void buildPage() {
        // Add status label to show the status of the slider
        this.mainLabel = new JLabel(strFormatter("Please enter an integer merchant fee percentage between 0 and 99 for currency conversions"), JLabel.CENTER);
        mainLabel.setBounds(0, 20, 400, 50);
        this.mainPanel.add(mainLabel);

        this.textField = new JTextField();
        this.textField.setBounds(120,100,50,25);
        this.mainPanel.add(textField);

        JLabel percent = new JLabel("%");
        percent.setBounds(175,100,25,25);
        this.mainPanel.add(percent);

        this.submitButton = new JButton("Submit");
        submitButton.setBounds(200,100,75,25);
        submitButton.addActionListener(e -> submitButtonAction());
        this.mainPanel.add(submitButton);

        this.warningLabel = new JLabel();
        warningLabel.setBounds(50, 150, 400, 50);
        this.mainPanel.add(warningLabel);

        this.mainFrame.setContentPane(this.mainPanel);
        mainFrame.setVisible(true);

    }

    private void submitButtonAction(){
        //restore all warning label texts
        warningLabel.setText(strFormatter(""));

        //retrieve user input
        String input = textField.getText();

        //Make sure no calls to the facade object are made simultaneously
        if(sw == null || sw.isDone()){
            startFeeThread(input);
        }
        else{
            warningLabel.setText(strFormatter("Warning: Too many requests at once, try again later"));
        }
    }

    private void startFeeThread(String str) {
        this.sw = new SwingWorker() {
            private String bText = submitButton.getText();

            @Override
            protected Object doInBackground(){
                publish();
                submitButton.setEnabled(false);
                return facade.setFee(str);

            }

            @Override
            protected void process(List chunks) {
                submitButton.setText("Loading...");
            }

            @Override
            protected void done() {
                try {
                    boolean success = (boolean) get();
                    if(success){
                        //navigate to the list of cryptocurrencies
                        navListCryptoPage();
                    }
                    else{
                        warningLabel.setText(strFormatter("Warning: Invalid input"));
                    }
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
                submitButton.setText(bText);
                submitButton.setEnabled(true);

            }
        };
        this.sw.execute();
    }

    private void navListCryptoPage(){
        new ListCryptoPage(this.facade);
        this.mainFrame.dispose();
    }
}
