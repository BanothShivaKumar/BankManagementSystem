import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
public class deposit extends JFrame implements ActionListener{
    String pinnumber;
    JTextField a;
    JButton deposit,Exit;
    deposit(String pinnumber) {
        this.pinnumber = pinnumber;
        setLayout(null);

        ImageIcon i1 = null;
        java.net.URL imgURL = deposit.class.getResource("/atm.jpg");
        if (imgURL == null) {
            imgURL = ClassLoader.getSystemResource("atm.jpg");
        }
        if (imgURL != null) {
            i1 = new ImageIcon(imgURL);
        } else {
            java.io.File f = new java.io.File("atm.jpg");
            if (f.exists()) {
                i1 = new ImageIcon(f.getAbsolutePath());
            } else {
                i1 = new ImageIcon("atm.jpg");
            }
        }

        JLabel image;
        if (i1 != null && i1.getImage() != null) {
            Image i2 = i1.getImage().getScaledInstance(900, 900, Image.SCALE_DEFAULT);
            image = new JLabel(new ImageIcon(i2));
        } else {
            image = new JLabel();
        }
        image.setBounds(0, 0, 900, 900);
        add(image);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel text = new JLabel("Enter Amount You Want to Deposit");
        text.setFont(new Font("System", Font.BOLD, 16));
        text.setForeground(Color.WHITE);
        text.setBounds(180, 300, 400, 20);
        image.add(text);

        a = new JTextField();
        a.setFont(new Font("System", Font.BOLD, 16));
        a.setBounds(180, 330, 250, 20);
        image.add(a);

        deposit = new JButton("Deposit");
        deposit.setBounds(350, 450, 150, 30);
        image.add(deposit);
        deposit.addActionListener(this);

        Exit = new JButton("Exit");
        Exit.setBounds(350, 485, 150, 30);
        image.add(Exit);
        Exit.addActionListener(this);

        setSize(900, 900);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == deposit) {
            String number = a.getText().trim();
            Date date = new Date();
            if (number.equals("")) {
                JOptionPane.showMessageDialog(null, "Please Enter Amount");
                return;
            }
            try {
                double amt = Double.parseDouble(number);
                if (amt <= 0) {
                    JOptionPane.showMessageDialog(null, "Please enter a valid positive amount");
                    return;
                }
            } catch (NumberFormatException nfe) {
                JOptionPane.showMessageDialog(null, "Please enter a valid numeric amount");
                return;
            }

            try {
                conn c = new conn();
                String query = "insert into bank values('" + pinnumber + "','" + date + "','Deposit','" + number + "')";
                c.s.executeUpdate(query);
                JOptionPane.showMessageDialog(null, "Rs. " + number + " Deposited successfully");
                setVisible(false);
                new transcation(pinnumber).setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Transaction Error: " + e.getMessage());
            }
        } else if (ae.getSource() == Exit) {
            setVisible(false);
            new transcation(pinnumber).setVisible(true);
        }
    }
    public static void main(String[] args) {
        new deposit("");
    }
}
