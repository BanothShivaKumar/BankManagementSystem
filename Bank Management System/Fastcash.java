import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.util.Date;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
public class Fastcash extends JFrame implements ActionListener{
    JButton deposit,fastcash,PinChange,Cashwithdrawal,MiniStatement,balanceenquiry,Exit;
    String pinnumber;
    public Fastcash(String pinnumber) {
        this.pinnumber = pinnumber;
        setLayout(null);

        ImageIcon i1 = null;
        java.net.URL imgURL = Fastcash.class.getResource("/atm.jpg");
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

        JLabel text = new JLabel("Select Withdrawal Amount");
        text.setBounds(225, 300, 700, 35);
        text.setForeground(Color.WHITE);
        text.setFont(new Font("system", Font.BOLD, 16));
        image.add(text);

        deposit = new JButton("Rs 100");
        deposit.setBounds(170, 415, 120, 30);
        deposit.setBackground(Color.WHITE);
        deposit.setForeground(Color.BLACK);
        deposit.addActionListener(this);
        deposit.setFont(new Font("system", Font.BOLD, 12));
        image.add(deposit);

        fastcash = new JButton("Rs 1000");
        fastcash.setBounds(170, 450, 120, 30);
        fastcash.setBackground(Color.WHITE);
        fastcash.setForeground(Color.BLACK);
        fastcash.addActionListener(this);
        fastcash.setFont(new Font("system", Font.BOLD, 12));
        image.add(fastcash);

        PinChange = new JButton("Rs 2000");
        PinChange.setBounds(170, 485, 120, 30);
        PinChange.setBackground(Color.WHITE);
        PinChange.addActionListener(this);
        PinChange.setForeground(Color.BLACK);
        PinChange.setFont(new Font("system", Font.BOLD, 12));
        image.add(PinChange);

        Cashwithdrawal = new JButton("Rs 5000");
        Cashwithdrawal.setBounds(370, 415, 130, 30);
        Cashwithdrawal.setBackground(Color.WHITE);
        Cashwithdrawal.setForeground(Color.BLACK);
        Cashwithdrawal.addActionListener(this);
        Cashwithdrawal.setFont(new Font("system", Font.BOLD, 12));
        image.add(Cashwithdrawal);

        MiniStatement = new JButton("Rs 10000");
        MiniStatement.setBounds(370, 450, 130, 30);
        MiniStatement.setBackground(Color.WHITE);
        MiniStatement.setForeground(Color.BLACK);
        MiniStatement.addActionListener(this);
        MiniStatement.setFont(new Font("system", Font.BOLD, 12));
        image.add(MiniStatement);

        Exit = new JButton("Exit");
        Exit.setBounds(370, 520, 130, 30);
        Exit.setBackground(Color.WHITE);
        Exit.setForeground(Color.BLACK);
        Exit.setFont(new Font("system", Font.BOLD, 12));
        image.add(Exit);
        Exit.addActionListener(this);

        setSize(900, 900);
        setLocationRelativeTo(null);
        setUndecorated(true);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == Exit) {
            setVisible(false);
            new transcation(pinnumber).setVisible(true);
        } else {
            String amount = ((JButton) ae.getSource()).getText().substring(3);
            conn c = new conn();
            try {
                ResultSet rs = c.s.executeQuery("select * from bank where pin = '" + pinnumber + "'");
                int balance = 0;
                while (rs.next()) {
                    if (rs.getString("type").equalsIgnoreCase("deposit")) {
                        balance += Integer.parseInt(rs.getString("amount"));
                    } else {
                        balance -= Integer.parseInt(rs.getString("amount"));
                    }
                }
                if (balance < Integer.parseInt(amount)) {
                    JOptionPane.showMessageDialog(null, "Insufficient Balance");
                    return;
                }

                Date date = new Date();
                conn c1 = new conn();
                c1.s.executeUpdate("insert into bank values('" + pinnumber + "','" + date + "','withdrawal','" + amount + "')");
                JOptionPane.showMessageDialog(null, "Rs " + amount + " Debited Successfully");
                setVisible(false);
                new transcation(pinnumber).setVisible(true);
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }
    public static void main(String[] args) {
        new Fastcash("");
    }
}
