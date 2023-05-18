import java.util.*;

public class Maze
{
    // Deklarasi nilai
    public static final int RIGHT = 0;
    public static final int DOWN = 1;
    public static final int LEFT = 2;
    public static final int UP = 3;
    private final int neigh[][]= {{0,1},{1,0},{0,-1},{-1,0}};

    private int height;
    private int width;
    private Cell[][] grid; // Grid maze
    private Position player; // Posisi pemain
    private boolean win; // Win state
    private Stack<Position> solution;

    public Maze(int size)
    {
        this.width=size;
        this.height=size;
        generateMaze();
    }

    public void prepareMaze()
    {
        solution=new Stack<Position>();
        win=false;
        player=new Position(0,0);
        grid =new Cell[height][width];
        for(int i=0;i<height;i++)
            for(int j=0;j<width;j++)
                grid[i][j]=new Cell();
    }
    
    // Generate random maze menggunakan Depth First Search (DFS)
    public void generateMaze()
    {
        prepareMaze();
        Stack<Position> pathStack=new Stack<Position>();
        Position current;
        boolean checked [][]=new boolean[height][width];

        for(boolean[] row:checked) Arrays.fill(row,false);

        Position p=new Position(0,0); // Titik awal diinisialisasi (0,0)
        checked[p.getY()][p.getX()]=true;
        pathStack.push(p); // Push posisi terbaru ke dalam stack

        while(!pathStack.empty())
        {
            current=pathStack.pop();

            // Jika current memiliki neighbors yang belum dicek
            if(anyNonCheckedNeigh(checked,current))
            {
                pathStack.push(current);
                int randNeigh[]=initNShuffleNeigh();

                // Mengecek seluruh neighbors
                for(int side :randNeigh)
                {
                    int indexY=current.getY()+neigh[side][0];
                    int indexX=current.getX()+neigh[side][1];
                    boolean inBounds = ((indexX >= 0) && (indexX < grid.length))&&((indexY >= 0) && (indexY < grid[0].length));
                    // Jika neighbor belum dicek
                    if(inBounds&&!isCheckedNeighbor(checked,current,side))
                    {
                        p=new Position(indexX,indexY);
                        breakWall(current,side);
                        checked[indexY][indexX]=true;
                        pathStack.push(p);
                        break;
                    }
                }
            }
        }
    }

    public void solveMaze()
    {
        boolean checked [][]=new boolean[height][width];
        for(boolean[] row:checked) Arrays.fill(row,false);
        solution.push(new Position(0,0));
        solveMaze(checked);
        settleGame(); 
    }

    // Menggunakan method recursive yang mencari ujung dari maze (Recursive Bactracker)
    private  boolean solveMaze(boolean[][] checked)
    {
        Position current=solution.peek();
        boolean isRightWay;
        checked[current.getY()][current.getX()]=true;

        // Jika current position sudah mencapai ujung
        if(current.getY()==height-1&&current.getX()==width-1)
        {
            return true;
        }
        // Jika current position memiliki neighbors yang belum dicek 
        if(anyNonCheckedNeigh(checked,current))
        {
            for(int i=0;i<4;i++)
            {
                int indexY=current.getY()+neigh[i][0];
                int indexX=current.getX()+neigh[i][1];
                boolean inBounds = ((indexX >= 0) && (indexX < grid.length))&&((indexY >= 0) && (indexY < grid[0].length));
                if(inBounds)
                {
                    // Jika neighbor belum dicek dan tidak ada dinding pemisah dalam cell
                    if (!grid[current.getY()][current.getX()].getWall(i) && !isCheckedNeighbor(checked, current, i))
                    {
                        solution.push(new Position(indexX, indexY));
                        isRightWay = solveMaze(checked);
                        if (isRightWay) return true;
                    }
                }
            }
        }
        // Jika tidak menemukan neighbor yang mencapai ujung maze, pop stack dan return false
        solution.pop();
        return false;
    }
    
    private boolean isCheckedNeighbor(boolean checked [][], Position current, int side)
    {
        return checked[current.getY()+neigh[side][0]][current.getX()+neigh[side][1]];
    }

    private boolean anyNonCheckedNeigh(boolean checked [][],Position current)
    {
        for (int i=0;i<4;i++)
        {
            int indexY=current.getY()+neigh[i][0];
            int indexX=current.getX()+neigh[i][1];
            boolean inBounds = ((indexY >= 0) && (indexY < grid.length))&&((indexX >= 0) && (indexX < grid[0].length));
            if(inBounds)
              if(!isCheckedNeighbor(checked,current,i))
              {
                 return true;
              }
        }
        return false;
    }
    // 0=Right, 1=Down, 2=Left, 3=Up
    private void breakWall(Position p,int side)
    {
        int currentY= p.getY(),currentX= p.getX(),nextX,nextY;
        grid[currentY][currentX].setWall(false,side);
        nextY=p.getY()+neigh[side][0];
        nextX= p.getX()+neigh[side][1];
        grid[nextY][nextX].setWall(false,(side+2)%4);
    }

    private int[] initNShuffleNeigh()
    {
        Random rng=new Random();
        int arr[]={0,1,2,3};
        for(int i=arr.length;i>0;)
        {
            int j=rng.nextInt(i--);
            int temp=arr[j];
            arr[j]=arr[i];
            arr[i]=temp;
        }
        return arr;
    }
    
    // Method pergerakan posisi pemain
    public void Move(int side)
    {
        if(player.getX()+1==width&&player.getY()+1==height&&side==DOWN) settleGame();
        if(CanPerform(side))
        {
            player.setY(player.getY()+neigh[side][0]);
            player.setX(player.getX()+neigh[side][1]);
        }
    }
    
    // Return true ketika pergerakan dapat dilakukan dan sebaliknya
    public Boolean CanPerform(int side)
    {
        return !grid[player.getY()][player.getX()].getWall(side);
    }

    public int getHeight()
    {
        return height;
    }

    public int getWidth()
    {
        return width;
    }

    public Cell[][] getGrid()
    {
        return grid;
    }

    public Position getPlayer()
    {
        return player;
    }

    private void settleGame()
    {
        win=true;
    }

    public boolean isWin()
    {
        return win;
    }

    public void SetSize(int size)
    {
        this.width=size;
        this.height=size;
        generateMaze();
    }

    public Stack<Position> getSolution()
    {
        return solution;
    }

    // Metode untuk mengubah objek menjadi string
    public String toString()
    {
        String str="";
        for(Cell[] row : grid)
        {
            for(Cell single:row)
            {
                if (single.walls[0] && single.walls[1] && single.walls[2] && single.walls[3])
                    str+=".";
                else if (single.walls[0] && !single.walls[1] && !single.walls[2] && !single.walls[3])
                    str+="┤";
                else if (!single.walls[0] && single.walls[1] && !single.walls[2] && !single.walls[3])
                    str+="┴";
                else if (!single.walls[0] && !single.walls[1] && single.walls[2] && !single.walls[3])
                    str+="┝";
                else if (!single.walls[0] && !single.walls[1] && !single.walls[2] && single.walls[3])
                    str+="┬";
                else if (!single.walls[0] && !single.walls[1] && single.walls[2] && single.walls[3])
                    str+="┌";
                else if (single.walls[0] && !single.walls[1] && !single.walls[2] && single.walls[3])
                    str+="┐";
                else if (!single.walls[0] && single.walls[1] && single.walls[2] && !single.walls[3])
                    str+="└";
                else if (single.walls[0] && single.walls[1] && !single.walls[2] && !single.walls[3])
                    str+="┘";
                else if (single.walls[0] && !single.walls[1] && single.walls[2] && !single.walls[3])
                    str+="|";
                else if (!single.walls[0] && single.walls[1] && !single.walls[2] && single.walls[3])
                    str+="─";
                else if (single.walls[0] && single.walls[1] && !single.walls[2] && single.walls[3])
                    str+="─";
                else if (single.walls[0] && single.walls[1] && single.walls[2] && !single.walls[3])
                    str+="|";
                else if (!single.walls[0] && single.walls[1] && single.walls[2] && single.walls[3])
                    str+="─";
                else if (single.walls[0] && !single.walls[1] && single.walls[2] && single.walls[3])
                    str+="|";
                else
                    str+="┼";
            }
            str+="\n";
        }
        return str;
    }
}
