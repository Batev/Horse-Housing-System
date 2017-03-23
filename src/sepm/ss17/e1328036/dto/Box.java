package sepm.ss17.e1328036.dto;

/**
 * Created by evgen on 18.03.2017.
 */
public class Box {
    private float size;
    private int sawdust;
    private int straw;
    private boolean hasWindow;
    private float price;

    public Box(float size, int sawdust, int straw, boolean hasWindow, float price) {
        this.setSize(size);
        this.setSawdust(sawdust);
        this.setStraw(straw);
        this.setHasWindow(hasWindow);
        this.setPrice(price);
    }

    public float getSize() {
        return size;
    }

    public void setSize(float size) {
        this.size = size;
    }

    public int getSawdust() {
        return sawdust;
    }

    public void setSawdust(int sawdust) {
        this.sawdust = sawdust;
    }

    public int getStraw() {
        return straw;
    }

    public void setStraw(int straw) {
        this.straw = straw;
    }

    public boolean hasWindow() {
        return hasWindow;
    }

    public void setHasWindow(boolean hasWindow) {
        this.hasWindow = hasWindow;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Size: " + size + ", Window: " + hasWindow + ", Price: " + price;
    }
}
