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
    private String description;
    private boolean isActive;
    
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }
    
    private void iniT() {
        if (x1<x2) {
            Tx[0] = x1;
            Tx[1] = x2;
        }else{
            Tx[0] = x2;
            Tx[1] = x1;
        }
        if (z1<z2) {
            Tz[0] = z1;
            Tz[1] = z2;
        }else{
            Tz[0] = z2;
            Tz[1] = z1;
        }
    }
    
    public boolean contains (int x0, int y0, int z0) {
        iniT();
        String tmp = "";
        tmp+= "y:"+y+";";
        tmp+= "y0:"+y0+";";
        tmp+= "x1:"+Tx[0]+";";
        tmp+= "x2:"+Tx[1]+";";
        tmp+= "x0:"+x0+";";
        tmp+= "z1:"+Tz[0]+";";
        tmp+= "z2:"+Tz[1]+";";
        tmp+= "z0:"+z0+";";
        System.out.println (tmp);        
        if (y0>y
            && Tx[0]<=x0 && x0<=Tx[1] 
            && Tz[0]<=z0 && z0<=Tz[1]) {
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "PLOT:\nID:"+getId()
            +"\nx1:"+getX1()
            +"\nx2:"+getX2()
            +"\nz1:"+getZ1()
            +"\nz2:"+getZ2()
            +"\nUserUuid:"+getUserUuid()
            +"\nActive:"+getIsActive();
    }
    
    
}
