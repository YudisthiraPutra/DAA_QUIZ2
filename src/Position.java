public class Position
{
	// Deklarasi x dan y sebagai titik koordinar
    private int x, y;

    // Inisialisasi titik koordinat x dan y
    Position(int x, int y)
    {
        this.y = y;
        this.x = x;
    }

    public void setY(int y)
    {
        this.y = y;
    }

    public void setX(int x)
    {
        this.x = x;
    }

    public int getX()
    {
        return x;
    }

    public int getY()
    {
        return y;
    }

    public String toString()
    {
        return "Position{" + "y=" + y + ", x=" + x + '}';
    }
}