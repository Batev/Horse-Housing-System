package sepm.ss17.e1328036.dto;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;
import java.net.MalformedURLException;

/**
 * Created by evgen on 18.03.2017.
 */
public class Box {

    private int bid;
    private float size;
    private int sawdust;
    private int straw;
    private boolean hasWindow;
    private float price;
    private boolean isDeleted;
    private String image;
    private ImageView picture;

    public Box(int bid, float size, int sawdust, int straw, boolean hasWindow, float price, String image, boolean isDeleted) {
        this.bid = bid;
        this.size = size;
        this.sawdust = sawdust;
        this.straw = straw;
        this.hasWindow = hasWindow;
        this.price = price;
        this.isDeleted = isDeleted;
        this.image = image;
    }

    public int getBid() {
        return bid;
    }

    public void setBid(int bid) {
        this.bid = bid;
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

    public boolean getHasWindow() {
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

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public ImageView getPicture() {
        File file = new File(getImage());
        Image value = null;
        try {
            value = new Image(file.toURI().toURL().toExternalForm());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        picture.setImage(value);
        picture.setVisible(true);

        return picture;
    }

    public void setPicture(ImageView picture) {
        this.picture = picture;
    }

    @Override
    public String toString() {
        return "Size: " + size + ", Window: " + hasWindow + ", Price: " + price;
    }
}
