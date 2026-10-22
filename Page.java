import com.toedter.calendar.JDateChooser;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.*;
import static java.lang.Math.random;
import java.util.*;
import javax.swing.*;
public class Page extends JFrame implements ActionListener{
    JButton next;
    JLabel name,fathername,dob,gen,email,status,address,city,state,pincode;
    JTextField name1,fathername1,email1,address1,city1,state1,pincode1;
    JRadioButton men,women,married,unmarried,others;
    JButton ne;
    JDateChooser d;
    long random;
    String formno;
    Page()
    {
        super("New Account Application Form - Page 1");
        Random a=new Random();
        random=(Math.abs((a.nextLong()%9000L)+1000L));
        JLabel app=new JLabel("Application Number is :"+random);
        app.setFont(new Font("Arial",Font.BOLD,29));
        app.setBounds(200, 50, 400, 30);
        setLayout(null);
        add(app);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        JLabel header=new JLabel("Page 1:Personal Details");
        add(header);
        header.setFont(new Font("Arial",Font.BOLD,24));
        header.setBounds(260,90, 350, 30);


        name=new JLabel("Name");
        add(name);
        name.setFont(new Font("Arial",Font.BOLD,25));
        name.setBounds(150, 140, 150, 30);
        name1=new JTextField();
        add(name1);
        name1.setFont(new Font("Arial",Font.BOLD,20));
        name1.setBounds(310,140,350,30);

        fathername=new JLabel("Father Name");
        add(fathername);
        fathername.setFont(new Font("Arial",Font.BOLD,25));
        fathername.setBounds(150, 200, 220, 30);
        fathername1=new JTextField();
        add(fathername1);
        fathername1.setFont(new Font("Arial",Font.BOLD,20));
        fathername1.setBounds(310,200,350,30);



        dob = new JLabel("Date of Birth");
        add(dob);
        dob.setFont(new Font("Arial", Font.BOLD, 25));
        dob.setBounds(150, 250, 200, 30);

        d = new JDateChooser();
        d.setDateFormatString("dd/MM/yyyy");
        d.setMaxSelectableDate(new Date());
        d.setBounds(310, 240, 300, 35);
        add(d);




        gen=new JLabel("Gender");
        gen.setFont(new Font("Arial",Font.BOLD,25));
        add(gen);
        gen.setBounds(150, 300, 200, 30);


        men=new JRadioButton("Male");
        women=new JRadioButton("Female");
        add(men);
        add(women);
        men.setBounds(330, 300, 100, 30);
        women.setBounds(500,300,100,30);
        men.setFont(new Font("Arial",Font.BOLD,20));
        women.setFont(new Font("Arial",Font.BOLD,20));

        ButtonGroup genderGroup = new ButtonGroup();
        genderGroup.add(men);
        genderGroup.add(women);




        email=new JLabel("Email");
        add(email);
        email.setFont(new Font("Arial",Font.BOLD,25));
        email.setBounds(150, 350, 220, 30);
        email1=new JTextField();
        add(email1);
        email1.setFont(new Font("Arial",Font.BOLD,20));
        email1.setBounds(310,350,350,30);



        status=new JLabel("Marital Status");
        status.setFont(new Font("Arial",Font.BOLD,25));
        add(status);
        status.setBounds(150,390,200,30);
        married=new JRadioButton("Married");
        unmarried=new JRadioButton("Unmarried");
        others=new JRadioButton("others");
        add(married);
        add(unmarried);
        add(others);
        married.setBounds(340, 400, 150, 30);
        unmarried.setBounds(510,400,150,30);
        others.setBounds(690,400,150,30);
        married.setFont(new Font("Arial",Font.BOLD,20));
        unmarried.setFont(new Font("Arial",Font.BOLD,20));
        others.setFont(new Font("Arial",Font.BOLD,20));

        ButtonGroup maritalGroup = new ButtonGroup();
        maritalGroup.add(married);
        maritalGroup.add(unmarried);
        maritalGroup.add(others);

        
        address=new JLabel("Address");
        address.setFont(new Font("Arial",Font.BOLD,25));
        add(address);
        address.setBounds(150,450,200,30);
        address1=new JTextField();
        add(address1);
        address1.setFont(new Font("Arial",Font.BOLD,20));
        address1.setBounds(310,450,350,30);


        city=new JLabel("City");
        city.setFont(new Font("Arial",Font.BOLD,25));
        add(city);
        city.setBounds(150,500,200,30);
        city1=new JTextField();
        add(city1);
        city1.setFont(new Font("Arial",Font.BOLD,20));
        city1.setBounds(310,500,350,30);




        state=new JLabel("state");
        add(state);
        state.setFont(new Font("Arial",Font.BOLD,25));
        state.setBounds(150, 550, 220, 30);
        state1=new JTextField();
        add(state1);
        state1.setFont(new Font("Arial",Font.BOLD,20));
        state1.setBounds(310,550,350,30);

        pincode=new JLabel("Pincode");
        add(pincode);
        pincode.setFont(new Font("Arial",Font.BOLD,25));
        pincode.setBounds(150, 600, 220, 30);
        pincode1=new JTextField();
        add(pincode1);
        pincode1.setFont(new Font("Arial",Font.BOLD,20));
        pincode1.setBounds(310,600,350,30);






        ne=new JButton("Next");
        ne.setBackground(Color.black);
        ne.setForeground(Color.white);
        ne.setBounds(600, 680, 120, 40);
        add(ne);





        ne.addActionListener(this);
        setSize(1000, 1000);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String formno = "" + random;
        String name = name1.getText().trim();
        String fname = fathername1.getText().trim();
        String email = email1.getText().trim();
        String address = address1.getText().trim();
        String city = city1.getText().trim();
        String dobValue = "";
        if (d.getDate() != null) {
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");
            dobValue = sdf.format(d.getDate());
        }
        String state = state1.getText().trim();
        String pincode = pincode1.getText().trim();
        String gen = null;
        if (men.isSelected()) {
            gen = "Men";
        } else if (women.isSelected()) {
            gen = "Women";
        }

        String s = null;
        if (married.isSelected()) {
            s = "Married";
        } else if (unmarried.isSelected()) {
            s = "Unmarried";
        } else if (others.isSelected()) {
            s = "Others";
        }
        try {
            if (name.equals("")) {
                JOptionPane.showMessageDialog(null, "Name is required");
            } else if (fname.equals("")) {
                JOptionPane.showMessageDialog(null, "Father Name is required");
            } else if (dobValue.equals("")) {
                JOptionPane.showMessageDialog(null, "Date of Birth is required");
            } else if (gen == null) {
                JOptionPane.showMessageDialog(null, "Gender is required");
            } else if (email.equals("")) {
                JOptionPane.showMessageDialog(null, "Email is required");
            } else if (s == null) {
                JOptionPane.showMessageDialog(null, "Marital Status is required");
            } else if (address.equals("")) {
                JOptionPane.showMessageDialog(null, "Address is required");
            } else if (city.equals("")) {
                JOptionPane.showMessageDialog(null, "City is required");
            } else if (state.equals("")) {
                JOptionPane.showMessageDialog(null, "State is required");
            } else if (pincode.equals("")) {
                JOptionPane.showMessageDialog(null, "Pincode is required");
            } else {
                conn c = new conn();
                String q1 = "INSERT INTO signup "
                        + "(formno, name, fathername, dob, gender, email, martialstatus, address, city, state, pincode) "
                        + "VALUES ('"
                        + formno + "','"
                        + name + "','"
                        + fname + "','"
                        + dobValue + "','"
                        + gen + "','"
                        + email + "','"
                        + s + "','"
                        + address + "','"
                        + city + "','"
                        + state + "','"
                        + pincode + "')";
                c.s.executeUpdate(q1);
                setVisible(false);
                new Signupone(formno).setVisible(true);
            }
        } catch (Exception p) {
            p.printStackTrace();
        }
    }
    public static void main(String args[])
    {
        new Page();
    }
}
