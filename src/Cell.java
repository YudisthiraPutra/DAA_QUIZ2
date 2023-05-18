public class Cell
{
    boolean[] walls;

    public Cell()
    {
        walls= new boolean [] {true,true,true,true};
    }

    public static int NumOfSides()
    {
        return 4;
    }

    public void setWall(boolean value,int side)
    {
        this.walls[side] = value;
    }

    public boolean getWall(int side)
    {
        return walls[side];
    }
}