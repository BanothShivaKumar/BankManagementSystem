import java.awt.*;
import java.awt.event.*;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
public class Signupone extends JFrame implements ActionListener
{
    JRadioButton yes,no,y,n;
    JLabel religion,cate,i,Eq,occup,Pan1,adh1,senior,eq;
    JComboBox<String> combo1,combo2,combo3,combo4;
    JTextField inc,pan2,adh2;
    JButton next;
    String formno;
    Signupone(String formno)
    {
        super("New Account Application Form - Page 2");
        this.formno=formno;
        JLabel appl=new JLabel("Page 2: Personal Details");
        appl.setFont(new Font("Arial",Font.BOLD,23));
        appl.setBounds(230, 80, 400, 30);
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        add(appl);
        religion=new JLabel("Religion:");
        religion.setFont(new Font("Arial",Font.BOLD,18));
        String rel[]={"Hindu","Muslim","Sikh","Christian","Other"};
        combo1=new JComboBox<>(rel);
        add(combo1);
        add(religion);
        religion.setBounds(150, 140, 120, 30);
        combo1.setBounds(330, 140, 220, 30);

        cate=new JLabel("Category:");
        cate.setFont(new Font("Arial",Font.BOLD,18));
        String cat[]={"General","OBC","Sc","St"};
        combo2=new JComboBox<>(cat);
        add(cate);
        add(combo2);
        cate.setBounds(150,190,120,30);
        combo2.setBounds(330,190,220,30);

        i=new JLabel("Income:");
        add(i);
        i.setFont(new Font("Arial",Font.BOLD,18));
        i.setBounds(150,240,200,30);
        inc=new JTextField();
        add(inc);
        inc.setBounds(330,240,220,30);


        Eq=new JLabel("<html>Educational<br>Qualification:</html>");
        add(Eq);
        Eq.setFont(new Font("Arial",Font.BOLD,18));
        String educ[]={"Graduated","Intermediate","High School","Other"};
        combo3=new JComboBox<>(educ);
        add(combo3);
        Eq.setBounds(150,290,250,60);
        combo3.setBounds(330,310,220,30);


        occup=new JLabel("Occupation: ");
        add(occup);
        occup.setFont(new Font("Arial",Font.BOLD,18));
        String ocp[]={"Self Employed","Student","Salaried","Unemployed","Other"};
        combo4=new JComboBox<>(ocp);
        add(combo4);
        occup.setBounds(150,360,250,60);
        combo4.setBounds(330,380,220,30);



        Pan1=new JLabel("Pan Number:");
        add(Pan1);
        Pan1.setFont(new Font("Arial",Font.BOLD,18));
        Pan1.setBounds(150,420,250,60);
        pan2=new JTextField();
        add(pan2);
        pan2.setBounds(330,440,220,30);

        adh1=new JLabel("<html>Aadhaar<br>Number:</html>");
        add(adh1);
        adh1.setFont(new Font("Arial",Font.BOLD,18));
        adh1.setBounds(150,480,250,60);
        adh2=new JTextField();
        add(adh2);
        adh2.setBounds(330,500,220,30);


        senior =new JLabel("Senior Citizen: ");
        add(senior);
        senior.setFont(new Font("Arial",Font.BOLD,18));
        senior.setBounds(150,560,250,60);
        yes=new JRadioButton("Yes");
        no=new JRadioButton("No");
        add(yes);
        add(no);
        yes.setBounds(330, 570, 70, 30);
        no.setBounds(450, 570, 70, 30);
        
        ButtonGroup seniorGroup = new ButtonGroup();
        seniorGroup.add(yes);
        seniorGroup.add(no);

        eq=new JLabel("<html>Existing<br> Account:</html>");
        add(eq);
        eq.setFont(new Font("Arial",Font.BOLD,18));
        eq.setBounds(150,620,250,60);
        y=new JRadioButton("Yes");
        n=new JRadioButton("No");
        add(y);
        add(n);
        y.setBounds(330,640,70,30);
        n.setBounds(450,640,70,30);

        ButtonGroup existingGroup = new ButtonGroup();
        existingGroup.add(y);
        existingGroup.add(n);

        next=new JButton("Next");
        next.setBackground(Color.black);
        next.setForeground(Color.white);
        next.setFont(new Font("Arial",Font.BOLD,18));
        add(next);
        next.setBounds(450,720,120,40);
        next.addActionListener(this);

        setSize(900,900);
        setLocationRelativeTo(null);
        setVisible(true);
    }
    @Override
    public void actionPerformed(ActionEvent e)
    {
        String c = combo2.getSelectedItem().toString();
        String income = inc.getText().trim();
        String ed = combo3.getSelectedItem().toString();
        String oc = combo4.getSelectedItem().toString();
        String pa = pan2.getText().trim();
        String a = adh2.getText().trim();

        String sen = yes.isSelected() ? "Yes" : "No";
        String exis = y.isSelected() ? "Yes" : "No";

        String rel = combo1.getSelectedItem().toString();

        if (income.isEmpty() || pa.isEmpty() || a.isEmpty()) {
            JOptionPane.showMessageDialog(
                this,
                "Please fill Income, PAN Number and Aadhaar Number."
            );
            return;
        }

        try {
            Double.parseDouble(income);
        } catch (NumberFormatException nfe) {
            JOptionPane.showMessageDialog(this, "Please enter a valid numeric income.");
            return;
        }

        if ((!yes.isSelected() && !no.isSelected())
                || (!y.isSelected() && !n.isSelected())) {
            JOptionPane.showMessageDialog(
                this,
                "Please select Yes or No for both questions."
            );
            return;
        }
        conn con = new conn();

        String q = "INSERT INTO signupone VALUES('" + rel + "','" + c + "','"
        + income + "','" + ed + "','" + oc + "','" + pa + "','"
        + a + "','" + sen + "','" + exis + "')";

        try {
            con.s.executeUpdate(q);
            if(e.getSource()==next)
            {
                setVisible(false);
                new Signuptwo(formno).setVisible(true);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        new Signupone(" ");
    }
}