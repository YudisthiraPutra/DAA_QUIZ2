import javax.swing.*;
import java.awt.*;
import java.util.Stack;

public class MazePanel extends JPanel
{
	// Ukuran bidak game dan jarak bidak dengan tepi maze
    final int size=600;
    final int MARGIN=18;
    Maze maze;
    public JPanel panel;

    // Ukuran bidak game
    public MazePanel(Maze maze)
    {
    	setSize(size, size);
        this.maze = maze;
        Image img2 = Toolkit.getDefaultToolkit().getImage("1.png");
        Image img = Toolkit.getDefaultToolkit().getImage("point.png");

        // Menggunakan MediaTracker untuk memastikan gambar telah selesai dimuat
        MediaTracker tracker = new MediaTracker(this);
        tracker.addImage(img2, 0);
        tracker.addImage(img, 1);
        try {
            tracker.waitForAll();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    
    // Method menggambar objek maze pada panel
    protected void paintComponent(Graphics g)
    {
        Image img2 = Toolkit.getDefaultToolkit().getImage("1.png");
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g.drawImage(img2, 0,0,null);
        g2.setColor(Color.black);
        g2.setStroke(new BasicStroke(7));

        int n=maze.getHeight();
        int m=maze.getWidth();
        int width=size/m;
        int height=size/n;
        Image img = Toolkit.getDefaultToolkit().getImage("point.png");
        

        drawMaze(g2,n,m,height,width);

        // Jika game belum selesai, panel akan menampilkan posisi pemain
        if(!maze.isWin())
        {
            Position player = maze.getPlayer();
            g.drawImage(img, MARGIN +(player.getX() * width + width / 2)-((width/2)/2),MARGIN+(player.getY() * height + height / 2) - ((width/2)/2),width/2,width/2, null);
        }
        // Jika game sudah selesai akan menampilkan sebuah teks
        else
        {
            Stack<Position> path=maze.getSolution();
            if(path.empty())
            {
            	int winning = JOptionPane.showConfirmDialog(null,
    					"You Win!, Would you like to play again?",
    					"Winning Confirmation", JOptionPane.YES_NO_OPTION,
    					 JOptionPane.INFORMATION_MESSAGE);
    					 if (winning == JOptionPane.YES_OPTION) 
    					 {
    						 maze.SetSize(MazeGUI.DefaultMazeSize);
    	
    						 ButtonAction.panel.repaint();
    						 panel.repaint();
    					 } 
    					 else 
    					 {
    						 panel.repaint();
    					
    					 }
            }
            else
            {
                drawSolution(g2,width,height,maze.getSolution(),Color.green);
            }
        }

        // Menggambar garis-garis pada panel
        g2.setColor(Color.RED);
        g2.drawLine(MARGIN, MARGIN, MARGIN+width-1, MARGIN);
        g2.setColor(Color.GREEN);
        g2.drawLine(MARGIN+(m-1)*width,MARGIN+n*height,MARGIN+m*width,MARGIN+n*height);
    }
    

    // Menggambar dinding pada panel
    private void drawMaze(Graphics2D g,int n,int m,int height,int width)
    {
        for(int i=0; i<n; i++)
        {
            for (int j =0; j <m; j++)
            {
                Cell[][] grid=maze.getGrid();
                if(grid[i][j].getWall(Maze.UP))
                    g.drawLine(MARGIN+(j*width),MARGIN+i*height,MARGIN+((1+j)*width),MARGIN+i*height);

                if(grid[i][j].getWall(Maze.DOWN))
                    g.drawLine(MARGIN+j*width,MARGIN +(i+1)*height,MARGIN+(1+j)*width,MARGIN+(i+1)*height);

                if(grid[i][j].getWall(Maze.LEFT))
                    g.drawLine(MARGIN+ (j * width),MARGIN+ i * height,  MARGIN+(j * width), MARGIN+(i + 1) * height);

                if(grid[i][j].getWall(Maze.RIGHT))
                    g.drawLine(MARGIN+((1 + j) * width),MARGIN+ i * height, MARGIN+((1 + j) * width), MARGIN+(i + 1) * height);
            }
        }
    }

    // Menggambarkan solusi saat pemain tidak tau solusinya
    private void drawSolution(Graphics2D g,int width,int height,Stack<Position> path,Color c)
    {
        g.setColor(c);
        while(!path.empty())
        {
            Position p=path.pop();
            if(!path.empty())
            {
                g.drawLine(MARGIN+p.getX()*width+width/2,MARGIN+p.getY()*height+height/2,MARGIN+path.peek().getX()*width+width/2,MARGIN+path.peek().getY()*height+height/2);
            }
        }
    }
}