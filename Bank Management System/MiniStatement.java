import java.awt.Color;
import java.awt.Font;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;

public class MiniStatement extends JFrame {

    String pinnumber;

    public MiniStatement(String pinnumber) {
        this.pinnumber = pinnumber;

        setTitle("Mini Statement");
        setLayout(null);

        JLabel text = new JLabel("ICICI BANK");
        text.setBounds(130, 20, 200, 30);
        text.setFont(new Font("System", Font.BOLD, 18));
        add(text);

        JLabel card = new JLabel("Card Number: Loading...");
        card.setBounds(20, 70, 350, 25);
        card.setFont(new Font("System", Font.BOLD, 12));
        add(card);

        JLabel heading = new JLabel("PIN            DATE                         TYPE      AMOUNT");
        heading.setBounds(20, 120,550, 25);
        heading.setFont(new Font("Monospaced", Font.BOLD, 12));
        add(heading);

        JTextArea mini = new JTextArea();
        mini.setBounds(20, 150, 450, 300);
        mini.setFont(new Font("Monospaced", Font.PLAIN, 12));
        mini.setEditable(false);
        mini.setBackground(Color.WHITE);
        add(mini);

        try {
            conn c = new conn();

            String cardQuery = "SELECT cardnumber FROM login WHERE pin = ?";

            try (PreparedStatement ps = c.c.prepareStatement(cardQuery)) {
                ps.setString(1, pinnumber);

                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        String cardnumber = rs.getString("cardnumber");

                        if (cardnumber != null && cardnumber.length() >= 8) {
                            card.setText(
                                "Card Number: "
                                + cardnumber.substring(0, 4)
                                + "XXXXXXXX"
                                + cardnumber.substring(cardnumber.length() - 4)
                            );
                        } else {
                            card.setText("Card Number: " +
                                (cardnumber == null ? "Unavailable" : cardnumber));
                        }
                    } else {
                        card.setText("Card Number: Not found");
                    }
                }
            }

            String bankQuery =
                "SELECT pin, date, type, amount FROM bank WHERE pin = ?";

            StringBuilder transactions = new StringBuilder();

            try (PreparedStatement ps = c.c.prepareStatement(bankQuery)) {
                ps.setString(1, pinnumber);

                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        transactions.append(String.format(
                            "%-12s %-16s %-12s %-10s%n",
                            rs.getString("pin"),
                            rs.getString("date"),
                            rs.getString("type"),
                            rs.getString("amount")
                        ));
                    }
                }
            }

            if (transactions.length() == 0) {
                mini.setText("No transactions found.");
            } else {
                mini.setText(transactions.toString());
            }

        } catch (Exception e) {
            e.printStackTrace();
            mini.setText("Unable to load statement. Check the console.");
        }

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800,800);
        setLocationRelativeTo(null);
        getContentPane().setBackground(Color.WHITE);
        setVisible(true);
    }

    public static void main(String[] args) {
        new MiniStatement("");
    }
}