import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;

public class Pinchange extends JFrame implements ActionListener {
    JPasswordField pin1, repin;
    JButton change, back;
    String pinnumber;

    public Pinchange(String pinnumber) {
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

        JLabel text = new JLabel("Change the Pin");
        text.setBounds(250, 280, 500, 35);
        text.setForeground(Color.WHITE);
        text.setFont(new Font("system", Font.BOLD, 16));
        image.add(text);

        JLabel pintext = new JLabel("New Pin:");
        pintext.setBounds(160, 320, 500, 35);
        pintext.setForeground(Color.WHITE);
        pintext.setFont(new Font("system", Font.BOLD, 16));
        image.add(pintext);
        

        JLabel repintext = new JLabel("Re-Enter New Pin:");
        repintext.setBounds(160, 360, 500, 35);
        repintext.setForeground(Color.WHITE);
        repintext.setFont(new Font("system", Font.BOLD, 16));
        image.add(repintext);

        pin1 = new JPasswordField();
        pin1.setBounds(330, 325, 160, 30);
        pin1.setForeground(Color.BLACK);
        pin1.setFont(new Font("system", Font.BOLD, 16));
        image.add(pin1);

        repin = new JPasswordField();
        repin.setBounds(330, 370, 160, 30);
        repin.setForeground(Color.BLACK);
        repin.setFont(new Font("system", Font.BOLD, 16));
        image.add(repin);

        change = new JButton("Change");
        change.setBounds(360, 450, 150, 30);
        image.add(change);
        change.addActionListener(this);

        back = new JButton("Exit");
        back.setBounds(360, 485, 150, 30);
        image.add(back);
        back.addActionListener(this);

        setLocation(400, 0);
    }
    public void actionPerformed(ActionEvent pw)
    {
        if(pw.getSource()==back)
        {
            setVisible(false);
            new transcation(pinnumber).setVisible(true);
        }
        else if(pw.getSource()==change)
        {
            try
            {
                String p1=pin1.getText();
                String p2=repin.getText();
                if(!p1.equals(p2))
                {
                    JOptionPane.showMessageDialog(null, "Entered PIN not match");
                }
                if(p1.equals(""))
                {
                    JOptionPane.showMessageDialog(null, "Please enter New PIN");
                    return;
                }
                if(p2.equals(""))
                {
                    JOptionPane.showMessageDialog(null, "Please Re-enter New PIN");
                    return;
                }
                conn c=new conn();
                String q1 = "UPDATE bank SET pin = " + p2 + " WHERE pin = " + pinnumber;
                String q2 = "UPDATE login SET pin = " + p2 + " WHERE pin = " + pinnumber;
                String q3 = "UPDATE signuptwo SET pin = " + p2 + " WHERE pin = " + pinnumber;
                c.s.executeUpdate(q1);
                c.s.executeUpdate(q2);
                c.s.executeUpdate(q3);
                JOptionPane.showMessageDialog(null, "PIN changed successfully");
                setVisible(false);
                new transcation(p2).setVisible(true);
            }
        catch(Exception e)
        {
            System.out.print(e);
        }
        }
    }
    public static void main(String[] args) {
        new Pinchange("").setVisible(true);;
    }
}
