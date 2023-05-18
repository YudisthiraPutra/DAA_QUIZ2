import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ButtonAction implements ActionListener
{
    private Maze mazes;
    public static JPanel panel;
    ButtonPanel bPanel;
    public ButtonAction(Maze maze,MazePanel panel,ButtonPanel bPanel)
    {
        this.bPanel=bPanel;
        this.panel=panel;
        this.maze=maze;
    }
    
    public void actionPerformed(ActionEvent e)
    {
        JTextField text=bPanel.getTextField();
        if(e.getActionCommand().equals("New Game"))
        {
            JLabel error=bPanel.getErrorLbl();
            if(!text.getText().isEmpty())
            {
                try
                {
                    int size = Integer.parseInt(bPanel.getTextField().getText());
                    if(size<=20)
                    {
                        if(size!=0)
                        {
                            maze.SetSize(size);
                            error.setText("");
                            panel.repaint();
                        }
                        else error.setText("Please input minimumly 0");
                    }
                    else error.setText("Please input maximumly 20");
                }
                catch (NumberFormatException exe)
                {
                    bPanel.getErrorLbl().setText("It Must Be Number");
                }
                text.setText("");
            }
            else
            {
                maze.SetSize(MazeGUI.DefaultMazeSize);
                panel.repaint();
            }
        }
        else if(e.getActionCommand().equals("Reset Input"))
        {
            text.setText("");
   

        }
        else if(e.getActionCommand().equals("Hint!"))
        {
            maze.solveMaze();
            panel.repaint();
        }
    }
}
