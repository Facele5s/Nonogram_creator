import java.awt.*;
import java.awt.image.BufferedImage;

public class Scaler {
    private BufferedImage img_inp;
    private BufferedImage img_out;
    private int scale;  //Scale factor

    public Scaler(int scale) {
        this.scale = scale;
    }

    public BufferedImage getImg_out() {
        return img_out;
    }

    public void setScale(int scale) {
        this.scale = scale;
    }

    public void setImg_inp(BufferedImage img_inp) {
        this.img_inp = img_inp;
    }

    public void scale() {
        int w = img_inp.getWidth() / scale; //Get scaled width
        int h = img_inp.getHeight() / scale;    //Get scaled height

        img_out = new BufferedImage(w, h, img_inp.getType());   //Create blank scaled picture
        Graphics2D g = img_out.createGraphics();
        g.drawImage(img_inp, 0, 0, w, h, null);
        g.dispose();
    }
}
