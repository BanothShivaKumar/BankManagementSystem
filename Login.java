import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class Login extends JFrame implements ActionListener {
    JButton b1, b2, b3;
    JTextField a;
    JPasswordField b;
    Login() {
        super("AUTOMATED TELLER MACHINE");
        a = new JTextField();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JLabel header = new JLabel("🏦 Welcome To The Bank");
        header.setFont(new Font("Osward", Font.BOLD, 25));
        header.setBounds(190, 90, 300, 40);
        add(header);
        b = new JPasswordField();
        JLabel c = new JLabel("Card Number :");
        c.setFont(new Font("Arial", Font.BOLD, 20));
        JLabel d = new JLabel("Pin Number :");
        d.setFont(new Font("Arial", Font.BOLD, 20));
        setLocation(400, 0);
        add(a);
        add(b);
        add(c);
        add(d);
        setLayout(null);
        a.setBounds(250, 150, 200, 25);
        b.setBounds(250, 200, 200, 25);
        c.setBounds(100, 150, 200, 25);
        d.setBounds(100, 200, 200, 25);
        b1 = new JButton("Sign Up");
        b1.setBounds(250, 240, 90, 25);
        b1.setBackground(Color.orange);
        b1.setForeground(Color.black);
        b1.addActionListener(this);

        b2 = new JButton("Clear");
        b2.setBounds(350, 240, 95, 25);
        b2.setBackground(Color.orange);
        b2.setForeground(Color.black);
        b2.addActionListener(this);

        b3 = new JButton("Login");
        b3.setBackground(Color.orange);
        b3.setBounds(250, 290, 200, 25);
        b3.setForeground(Color.black);
        b3.addActionListener(this);
        add(b1);
        add(b2);
        add(b3);

        setSize(800, 800);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        String q1;
        try {
            if (e.getSource() == b3) {
                conn c = new conn();
                String cardnumber = a.getText().trim();
                String pinnumber = new String(b.getPassword()).trim();
                q1 = "select * from login where cardnumber = '" + cardnumber + "' and pin = '" + pinnumber + "'";
                java.sql.ResultSet rs = c.s.executeQuery(q1);
                if (rs.next()) {
                    setVisible(false);
                    new transcation(pinnumber).setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(null, "The Card Number or Pin is Incorrect");
                }
            } else if (e.getSource() == b2) {
                a.setText("");
                b.setText("");
            } else if (e.getSource() == b1) {
                setVisible(false);
                new Page().setVisible(true);
            }
        } catch (Exception ap) {
            ap.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new Login();
    }
}