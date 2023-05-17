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

        for(boolean[] row:checked)
            Arrays.fill(row,false);

        Position p=new Position(0,0); //Starting Position 0,0
        checked[p.getY()][p.getX()]=true; //mark the starting cell as visited
        pathStack.push(p); //push current Position to the stack

        while(!pathStack.empty())
        {
            current=pathStack.pop(); //Make the top of the stack our "current" cell

            if(anyNonCheckedNeigh(checked,current)) //if current has any unchecked neighbors
            {
                pathStack.push(current);
                int randNeigh[]=initNShuffleNeigh(); //Init the array with the numbers 0-3 in a random order

                for(int side :randNeigh) //check any neighbor
                {
                    int indexY=current.getY()+neigh[side][0];
                    int indexX=current.getX()+neigh[side][1];
                    boolean inBounds = ((indexX >= 0) && (indexX < grid.length))&&((indexY >= 0) && (indexY < grid[0].length));//True if the random neighbor is inside the grid's bounds
                    if(inBounds&&!isCheckedNeighbor(checked,current,side)) //if neighbor isn't checked and is in bounds
                    {
                        p=new Position(indexX,indexY); //recycle usage of p to push the new chosen cell to the stack
                        breakWall(current,side);
                        checked[indexY][indexX]=true;
                        pathStack.push(p);
                        break;
                    }
                }
            }
        }
    }

    public void solveMaze() //reaches the end of the maze and puts the solution path in the solution stack of the maze | O(n^2)
    {
        //  Stack<Position> path=new Stack<Position>();
        boolean checked [][]=new boolean[height][width];

        for(boolean[] row:checked)
            Arrays.fill(row,false); //initialize as not checked

        // path.push(new Position(0,0)); //starting Position
        solution.push(new Position(0,0)); //starting Position

        solveMaze(checked);

        settleGame(); //signal that the game has ended
        //return path;
    }

    private  boolean solveMaze(boolean[][] checked) //recursive method that searches the end of the maze - Using recursive backtracker | O(n^2)
    {
        Position current=solution.peek();
        boolean isRightWay;
        checked[current.getY()][current.getX()]=true;

        if(current.getY()==height-1&&current.getX()==width-1) //If current reached the end Position
        {
            return true;
        }
        if(anyNonCheckedNeigh(checked,current)) //if current has any unchecked neighbors
        {
            for(int i=0;i<4;i++)
            {
                int indexY=current.getY()+neigh[i][0];
                int indexX=current.getX()+neigh[i][1];
              //  boolean inBounds = ((indexY >= 0) && (indexY < grid.length))&&((indexX >= 0) && (indexX < grid[0].length));//True if the random neighbor is inside the grid's bounds
                boolean inBounds = ((indexX >= 0) && (indexX < grid.length))&&((indexY >= 0) && (indexY < grid[0].length));
                if(inBounds)//If neighbor is in bounds of maze grid
                {
                    if (!grid[current.getY()][current.getX()].getWall(i) && !isCheckedNeighbor(checked, current, i))//If the Neighbor isn't checked and there isn't a wall separating the cells
                    {
                        solution.push(new Position(indexX, indexY));
                        isRightWay = solveMaze(checked);//recursion call to check if neighbor's method call reached the end of the maze

                        if (isRightWay)//if neighbor reached the end of the maze return true
                            return true;
                    }
                }
            }
        }
        solution.pop();//if couldn't find a neighbor that reached the end of the maze, pop the stack and return false
        return false;
    }
    
    private boolean isCheckedNeighbor(boolean checked [][], Position current, int side)//checks if a neighbor of a give cell is checked, returns true if checked,else false. 0=Right,1=Down,2=Left,3=Up - Not checking if neighbor exists | O(1).
    {
        return checked[current.getY()+neigh[side][0]][current.getX()+neigh[side][1]];
    }

    private boolean anyNonCheckedNeigh(boolean checked [][],Position current)//checks if the given cell has any unchecked neighbors(at least 1) | O(1)
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

    private void breakWall(Position p,int side)//"Break" the wall between the given cell and a chosen neighbor. 0=Right,1=Down,2=Left,3=Up - Not checking if neighbor exists | O(1)
    {
        int currentY= p.getY(),currentX= p.getX(),nextX,nextY;
        grid[currentY][currentX].setWall(false,side);
        nextY=p.getY()+neigh[side][0];
        nextX= p.getX()+neigh[side][1];
        grid[nextY][nextX].setWall(false,(side+2)%4);// The evaluation of "(side+2)%4" gives me the wall of the neighbor which is facing to the current cell's direction
    }

    private int[] initNShuffleNeigh()//returns an array of all neighbors(0-3) in a random order - using "Fisher–Yates shuffle" - O(1)
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
        if(player.getX()+1==width&&player.getY()+1==height&&side==DOWN)
            settleGame();

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
