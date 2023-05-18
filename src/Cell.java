public class Cell
{
    boolean[] wall;

    public Cell()
    {
        wall= new boolean [] {true,true,true,true};
    }

    public static int NumOfSides()
    {
        return 4;
    }

    public void setWall(boolean value,int side)
    {
        this.wall[side] = value;
    }

    public boolean getWall(int side)
    {
        return wall[side];
    }
}
