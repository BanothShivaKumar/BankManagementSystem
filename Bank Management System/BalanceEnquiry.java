import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
public class BalanceEnquiry extends JFrame implements ActionListener {
    String pinnumber;
    JButton Back;
    public BalanceEnquiry(String pinnumber) {
        this.pinnumber = pinnumber;
        setLayout(null);
        ImageIcon i1 = null;
        java.net.URL imgURL = transcation.class.getResource("/atm.jpg");
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
        setSize(900, 900);
        image.setBounds(0, 0, 900, 900);
        add(image);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JButton pq = new JButton("Back");
        pq.setBounds(390, 500, 100, 30);
        pq.setFont(new Font("system", Font.BOLD, 16));
        image.add(pq);
        pq.addActionListener(this);

        conn c = new conn();
        int balance = 0;
        try {
            ResultSet rs = c.s.executeQuery("SELECT * FROM bank WHERE pin = '" + pinnumber + "'");
            while (rs.next()) {
                if (rs.getString("type").equalsIgnoreCase("deposit")) {
                    balance += Integer.parseInt(rs.getString("amount"));
                } else {
                    balance -= Integer.parseInt(rs.getString("amount"));
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }

        JLabel pq1 = new JLabel("Your Current Account Balance is Rs " + balance);
        pq1.setFont(new Font("System", Font.BOLD, 16));
        pq1.setBounds(170, 300, 400, 30);
        pq1.setForeground(Color.WHITE);
        image.add(pq1);

        setLocation(300, 0);
        setSize(900, 900);
        setVisible(true);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        setVisible(false);
        new transcation(pinnumber).setVisible(true);
    }
    
    
    public static void main(String[] args) {
        new BalanceEnquiry("");
    }
}
