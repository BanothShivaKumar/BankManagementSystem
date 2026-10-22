import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
public class Signuptwo extends JFrame implements ActionListener {
    JButton submit, cancel;
    JRadioButton savings, current, fixed, RecurringDepositAccount;
    JCheckBox ATM, MobileBanking, InternetBanking, Email, estatement, ChequeBook, dec;
    String formno;

    public Signuptwo(String formno) {
        super("New Account Application Form - Page 3");
        this.formno = formno;
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel appl = new JLabel("Page 3: Account Details");
        appl.setFont(new Font("Arial", Font.BOLD, 23));
        appl.setBounds(260, 80, 400, 30);
        add(appl);

        JLabel Acc = new JLabel("Account Type:");
        Acc.setBounds(100, 150, 200, 30);
        Acc.setFont(new Font("Raleway", Font.BOLD, 20));
        add(Acc);

        savings = new JRadioButton("Savings");
        savings.setBounds(100, 185, 200, 30);
        savings.setFont(new Font("Raleway", Font.BOLD, 17));
        add(savings);

        current = new JRadioButton("Current Account");
        current.setBounds(100, 215, 200, 40);
        current.setFont(new Font("Raleway", Font.BOLD, 17));
        add(current);

        fixed = new JRadioButton("Fixed Deposit Account");
        fixed.setBounds(320, 190, 250, 30);
        fixed.setFont(new Font("Raleway", Font.BOLD, 17));
        add(fixed);

        RecurringDepositAccount = new JRadioButton("Recurring Deposit Account");
        RecurringDepositAccount.setBounds(320, 215, 250, 40);
        RecurringDepositAccount.setFont(new Font("Raleway", Font.BOLD, 17));
        add(RecurringDepositAccount);

        ButtonGroup accountGroup = new ButtonGroup();
        accountGroup.add(savings);
        accountGroup.add(current);
        accountGroup.add(fixed);
        accountGroup.add(RecurringDepositAccount);

        JLabel cardnumber = new JLabel("Card Number: ");
        JLabel Number = new JLabel("XXXX-XXXX-4121");
        JLabel six = new JLabel("Your 16 Digit Card Number");
        cardnumber.setFont(new Font("Raleway", Font.BOLD, 20));
        cardnumber.setBounds(100, 300, 600, 30);
        Number.setBounds(310, 300, 600, 30);
        Number.setFont(new Font("Raleway", Font.BOLD, 20));
        six.setBounds(100, 330, 600, 30);
        six.setFont(new Font("Raleway", Font.BOLD, 15));
        add(cardnumber);
        add(Number);
        add(six);

        JLabel pin = new JLabel("PIN");
        pin.setBounds(100, 390, 600, 30);
        pin.setFont(new Font("Raleway", Font.BOLD, 20));
        add(pin);

        JLabel pin2 = new JLabel("XXXX");
        pin2.setBounds(310, 390, 600, 30);
        pin2.setFont(new Font("Raleway", Font.BOLD, 20));
        add(pin2);

        JLabel pintext = new JLabel("Your 4-Digit PIN");
        pintext.setBounds(100, 420, 600, 30);
        pintext.setFont(new Font("Raleway", Font.BOLD, 15));
        add(pintext);

        JLabel Servicerequired = new JLabel("Service Required:");
        Servicerequired.setBounds(100, 480, 600, 30);
        Servicerequired.setFont(new Font("Raleway", Font.BOLD, 20));
        add(Servicerequired);

        ATM = new JCheckBox("ATM");
        ATM.setFont(new Font("Raleway", Font.BOLD, 17));
        ATM.setBounds(100, 530, 150, 30);
        add(ATM);

        MobileBanking = new JCheckBox("Mobile Banking");
        MobileBanking.setFont(new Font("Raleway", Font.BOLD, 17));
        MobileBanking.setBounds(350, 530, 200, 30);
        add(MobileBanking);

        InternetBanking = new JCheckBox("Internet Banking");
        InternetBanking.setFont(new Font("Raleway", Font.BOLD, 17));
        InternetBanking.setBounds(100, 570, 200, 30);
        add(InternetBanking);

        Email = new JCheckBox("Email & SMS Alert");
        Email.setFont(new Font("Raleway", Font.BOLD, 17));
        Email.setBounds(350, 570, 200, 30);
        add(Email);

        estatement = new JCheckBox("E-Statement");
        estatement.setFont(new Font("Raleway", Font.BOLD, 17));
        estatement.setBounds(100, 610, 150, 30);
        add(estatement);

        ChequeBook = new JCheckBox("Cheque Book");
        ChequeBook.setFont(new Font("Raleway", Font.BOLD, 17));
        ChequeBook.setBounds(350, 610, 150, 30);
        add(ChequeBook);

        dec = new JCheckBox();
        dec.setBounds(100, 700, 30, 30);
        add(dec);

        JLabel declaration = new JLabel("I hereby declare that the above information is true and correct to the best of my knowledge.");
        declaration.setBounds(140, 700, 850, 30);
        declaration.setFont(new Font("Raleway", Font.BOLD, 15));
        add(declaration);

        submit = new JButton("Submit");
        submit.setBounds(250, 750, 100, 30);
        submit.setFont(new Font("Raleway", Font.BOLD, 15));
        submit.setBackground(Color.blue);
        submit.setForeground(Color.white);
        add(submit);
        submit.addActionListener(this);

        cancel = new JButton("Cancel");
        cancel.setBounds(450, 750, 100, 30);
        cancel.setFont(new Font("Raleway", Font.BOLD, 15));
        cancel.setBackground(Color.black);
        cancel.setForeground(Color.white);
        add(cancel);
        cancel.addActionListener(this);

        setSize(850, 850);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == submit) {
            String accounttype = null;
            if (savings.isSelected()) {
                accounttype = "Savings";
            } else if (current.isSelected()) {
                accounttype = "Current";
            } else if (fixed.isSelected()) {
                accounttype = "Fixed";
            } else if (RecurringDepositAccount.isSelected()) {
                accounttype = "RecurringDepositAccount";
            }

            if (accounttype == null || accounttype.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please select account type");
                return;
            }

            if (!dec.isSelected()) {
                JOptionPane.showMessageDialog(null, "Please agree to the declaration statement.");
                return;
            }

            Random rand = new Random(); 
            String cardnumber = String.valueOf(Math.abs(rand.nextLong() % 9000000000000000L) + 1000000000000000L);
            String pin = String.valueOf(rand.nextInt(9000) + 1000);
            String faculity = "";
            if (ATM.isSelected()) faculity += "ATM ";
            if (MobileBanking.isSelected()) faculity += "Mobile Banking ";
            if (InternetBanking.isSelected()) faculity += "Internet Banking ";
            if (Email.isSelected()) faculity += "Email & SMS Alert ";
            if (estatement.isSelected()) faculity += "E-Statement ";
            if (ChequeBook.isSelected()) faculity += "Cheque Book ";
            faculity = faculity.trim();

            try {
                conn co = new conn();
                String query = "INSERT INTO Signuptwo VALUES ('"
                        + formno + "','"
                        + accounttype + "','"
                        + cardnumber + "','"
                        + pin + "','"
                        + faculity + "')";

                String query1 = "INSERT INTO login VALUES ('"
                        + formno + "','"
                        + cardnumber + "','"
                        + pin + "')";

                co.s.executeUpdate(query);
                co.s.executeUpdate(query1);

                JOptionPane.showMessageDialog(null, "Card Number: " + cardnumber + "\nPin Number: " + pin);
                setVisible(false);
                new deposit(pin).setVisible(true);
            } catch (Exception ap) {
                ap.printStackTrace();
                JOptionPane.showMessageDialog(null, "Database Error: " + ap.getMessage());
            }
        } else if (e.getSource() == cancel) {
            setVisible(false);
            new Login().setVisible(true);
        }
    }

    public static void main(String[] args) {
        Signuptwo s = new Signuptwo("");
    }

}
