package tradeserver;

import java.awt.*;
import java.io.*;
import java.lang.management.PlatformManagedObject;
import java.net.*;
import java.nio.file.*;
import main.UserAccounts;
import java.util.List;
import javax.swing.*;

public class CardTrade extends JFrame {
    private String playerName;
    //private String filePath;
    private int[] myCards = new int[20];
    private int[] dupes = new int[20];
    private String[] cardnames= new String[20];

    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    // GUI Components
    private JComboBox<String> myCardDropdown;
    private JLabel theirOfferLabel;
    private JLabel statusLabel;
    private JButton offerButton;
    private JButton acceptButton;

    // Trade State
    private String myCurrentOffer = null;
    private String theirCurrentOffer = null;
    private boolean iAccepted = false;
    private boolean theyAccepted = false;

    public CardTrade(String playerName, int[] card, int[] dupes, boolean isHost, String hostIp, int port) {
        this.playerName = playerName;
        //this.filePath = filePath;
        for(int i=0;i<20;i++){
            this.myCards[i]=card[i];
            this.dupes[i]=dupes[i];
        }
        //this.myCards = loadCardsFromFile(filePath);
        
        for (int i=0; i<myCards.length;i++) {
            cardnames[i] = "Card "+(i+1)+" Duplicates: "+dupes[i];
        }

        setupGUI();
        connectNetwork(isHost, hostIp, port);
    }

    private void setupGUI() {
        setTitle("Card Trade Window - " + playerName);
        setSize(420, 260);
        //setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centers the pop-up window
        setLayout(new GridLayout(5, 1, 10, 10));

        // 1. My Card Selection
        JPanel myPanel = new JPanel();
        myPanel.add(new JLabel("Your Cards:"));
        myCardDropdown = new JComboBox<>(cardnames);/*myCards.toString()*/
        myPanel.add(myCardDropdown);
        offerButton = new JButton("Offer Selected Card");
        myPanel.add(offerButton);
        add(myPanel);

        // 2. Partner's Offer Display
        JPanel theirPanel = new JPanel();
        theirOfferLabel = new JLabel("Partner's Offer: [Waiting...]");
        theirOfferLabel.setFont(new Font("Arial", Font.BOLD, 14));
        theirPanel.add(theirOfferLabel);
        add(theirPanel);

        // 3. Accept Button
        JPanel actionPanel = new JPanel();
        acceptButton = new JButton("Accept Trade");
        acceptButton.setEnabled(false); // Disabled until both players offer a card
        actionPanel.add(acceptButton);
        add(actionPanel);

        // 4. Status Bar
        statusLabel = new JLabel("Status: Initializing connection...", SwingConstants.CENTER);
        add(statusLabel);

        // Button Listeners
        offerButton.addActionListener(e -> sendOffer());
        acceptButton.addActionListener(e -> acceptTrade());

        setVisible(true);
    }

    private void connectNetwork(boolean isHost, String hostIp, int port) {
        new Thread(() -> {
            try {
                if (isHost) {
                    updateStatus("Waiting for trading partner on port " + port + "...");
                    ServerSocket serverSocket = new ServerSocket(port);
                    socket = serverSocket.accept();
                } else {
                    updateStatus("Connecting to " + hostIp + ":" + port + "...");
                    socket = new Socket(hostIp, port);
                }

                out = new PrintWriter(socket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                updateStatus("Connected! Select a card to offer.");

                listenForMessages();
            } catch (IOException e) {
                updateStatus("Connection Error: " + e.getMessage());
            }
        }).start();
    }

    private void listenForMessages() {
        try {
            String message;
            while ((message = in.readLine()) != null) {
                if (message.startsWith("OFFER:")) {
                    theirCurrentOffer = message.substring(6);
                    iAccepted = false;
                    theyAccepted = false;
                    SwingUtilities.invokeLater(() -> {
                        theirOfferLabel.setText("Partner's Offer: " + theirCurrentOffer);
                        acceptButton.setEnabled(myCurrentOffer != null && theirCurrentOffer != null);
                        updateStatus("Partner offered " + theirCurrentOffer + ". Click Accept to confirm.");
                    });
                } else if (message.equals("ACCEPT")) {
                    theyAccepted = true;
                    updateStatus("Partner accepted the trade!");
                    checkTradeCompletion();
                }
            }
        } catch (IOException e) {
            updateStatus("Partner disconnected.");
        }
    }

    private void sendOffer() {
        if (out == null || myCardDropdown.getSelectedItem() == null) return;
        myCurrentOffer = myCardDropdown.getSelectedItem().toString();
        iAccepted = false;
        theyAccepted = false;

        out.println("OFFER:" + myCurrentOffer);
        acceptButton.setEnabled(theirCurrentOffer != null);
        updateStatus("You offered: " + myCurrentOffer);
    }

    private void acceptTrade() {
        if (out == null) return;
        iAccepted = true;
        acceptButton.setEnabled(false);
        out.println("ACCEPT");
        updateStatus("You accepted. Waiting for partner...");
        try{
            checkTradeCompletion();
        } catch (IOException e) {
            System.out.println("error");
        }
    }

    private synchronized void checkTradeCompletion() throws IOException{
        if (iAccepted && theyAccepted) {
            // Update local inventory list and save to text file
            myCurrentOffer = myCurrentOffer.substring(6,7);
            int my = Integer.parseInt(myCurrentOffer);
            theirCurrentOffer = theirCurrentOffer.substring(6,7);
            int their = Integer.parseInt(theirCurrentOffer);
            if(myCurrentOffer != null){
                
            } else if(myCurrentOffer == null) {
                throw new IOException("Could not be read");
            }
            
            if(myCards[my]==0){
                JOptionPane.showMessageDialog(null,"No card available. Trade unsucessful", "Card Capacity", JOptionPane.OK_OPTION);
                return;
            }
            else if(myCards[my]==1){
                UserAccounts.updateCard(playerName, my, 0);
            }
            else if(dupes[my]>0){
                dupes[my]=dupes[my]--;
                UserAccounts.updateCard(playerName, my, dupes[my]);
            }

            if(myCards[their]==0){
                UserAccounts.updateDuplicate(playerName, their, 1);
            }
            else{
                UserAccounts.updateDuplicate(playerName, their, 1);
            }
            //saveCardsToFile(filePath, myCards);

            SwingUtilities.invokeLater(() -> {
                JOptionPane.showMessageDialog(this,
                        "Trade Complete!\nYou gave: " + myCurrentOffer + "\nYou received: " + theirCurrentOffer,
                        "Trade Successful",
                        JOptionPane.INFORMATION_MESSAGE);

                // Reset UI for next trade
                myCardDropdown.setModel(new DefaultComboBoxModel<>(cardnames));/*myCards*/
                myCurrentOffer = null;
                theirCurrentOffer = null;
                iAccepted = false;
                theyAccepted = false;
                theirOfferLabel.setText("Partner's Offer: [Waiting...]");
                acceptButton.setEnabled(false);
                updateStatus("Saved to Collection. Ready for another trade.");
            });
        }
    }
/* 
    private List<String> loadCardsFromFile(String path) {
        try {
            return new ArrayList<>(Files.readAllLines(Paths.get(path)));
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error reading file: " + path);
            return new ArrayList<>();
        }
    }

    private void saveCardsToFile(String path, List<String> cards) {
        try {
            Files.write(Paths.get(path), cards);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error saving file: " + path);
        }
    }
*/
    private void updateStatus(String text) {
        SwingUtilities.invokeLater(() -> statusLabel.setText("Status: " + text));
    }

}
