package pl.karczmarczyk.ownplace.models;

/**
 *
 * @author mateusz
 */
public class Plot {
    private int id;
    private int x1;
    private int z1;
    private int x2;
    private int z2;
    private int area;
    private String userUuid;
    
    private final int y = 40;
    
    private int[] Tx = new int[2];
    private int[] Tz = new int[2];

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getX1() {
        return x1;
    }

    public void setX1(int x1) {
        this.x1 = x1;
    }

    public int getZ1() {
        return z1;
    }

    public void setZ1(int z1) {
        this.z1 = z1;
    }

    public int getX2() {
        return x2;
    }

    public void setX2(int x2) {
        this.x2 = x2;
    }

    public int getZ2() {
        return z2;
    }
    
    public int getY () {
        return this.y;
    }

    public void setZ2(int z2) {
        this.z2 = z2;
    }

    public int getArea() {
        return area;
    }

    public void setArea(int area) {
        this.area = area;
    }

    public String getUserUuid() {
        return userUuid;
    }

    public void setUserUuid(String userUuid) {
        this.userUuid = userUuid;
    }
    
    private void iniT() {
        if (x1<x2) {
            Tx[0] = x1;
            Tx[1] = x2;
        }
        if (z1<z2) {
            Tz[0] = z1;
            Tz[1] = z2;
        }
    }
    
    public boolean contains (int x0, int y0, int z0) {
        iniT();
        if (y0>y
            && Tx[0]<=x0 && x0<=Tx[1] 
            && Tz[0]<=z0 && z0<=Tz[1]) {
            return true;
        }
        return false;
    }
}
