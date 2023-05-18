import javax.swing.*;
import java.awt.*;

public class ButtonPanel extends JPanel
{
    JButton newMazeBtn;
    JTextField textField;
    JLabel errorLbl;
    JButton clearTextBtn;
    JButton solveMazeBtn;
    public ButtonPanel()
    {
        setLayout(new GridLayout(30,0));
        setBackground(new Color(236,207,137));
        
        Label lbl2=new Label("CATOME");
        lbl2.setFont(new Font("Gill Sans", Font.PLAIN, 17));
        add(lbl2);
        Label lbl3=new Label("Help this cat to find his way out ");
        lbl3.setFont(new Font("Gill Sans", Font.PLAIN, 12));
        add(lbl3);

        Label lbl=new Label("Customize Size:");
        lbl.setFont(new Font("Gill Sans", Font.PLAIN, 17));
        add(lbl);
        textField=new JTextField("");
        textField.setFocusable(false);
        add(textField);

        newMazeBtn = new JButton("New Game");
        newMazeBtn.setFont(new Font("Gill Sans", Font.PLAIN, 20));
        newMazeBtn.setFocusable(false);

        errorLbl=new JLabel();
        add(errorLbl);
        add(newMazeBtn);
        add(new JLabel());

        clearTextBtn=new JButton("Reset Input");
        clearTextBtn.setFont(new Font("Gill Sans", Font.PLAIN, 20));
        clearTextBtn.setFocusable(false);
        add(clearTextBtn);

        errorLbl=new JLabel();
        add(errorLbl);
        add(newMazeBtn);
        add(new JLabel());

        solveMazeBtn=new JButton("Hint!");
        solveMazeBtn.setFont(new Font("Gill Sans", Font.PLAIN, 20));
        solveMazeBtn.setFocusable(false);
        add(solveMazeBtn);
        
        
   
    }


    
    

    public JLabel getErrorLbl()
    {
        return errorLbl;
    }

    public JTextField getTextField()
    {
        return textField;
    }

    // Do setAction listener
    public void setAction(ButtonAction action)
    {
        newMazeBtn.addActionListener(action);
        clearTextBtn.addActionListener(action);
        solveMazeBtn.addActionListener(action);
    }
}
