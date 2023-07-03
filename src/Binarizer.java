import java.awt.*;
import java.awt.image.BufferedImage;

public class Binarizer {
    private BufferedImage img_inp;
    private BufferedImage img_out;

    private int factor; //Binarization factor

    public Binarizer(int factor) {
        this.factor = factor;
    }

    public BufferedImage getImg_out() {
        return img_out;
    }

    public void setFactor(int factor) {
        this.factor = factor;
    }

    public void setImg_inp(BufferedImage img_inp) {
        this.img_inp = img_inp;
        img_out = new BufferedImage(img_inp.getWidth(), img_inp.getHeight(), img_inp.getType());
    }

    public void binarize() {    //Binarization method
        for (int i = 0; i < img_inp.getWidth(); i++) {
            for (int j = 0; j < img_inp.getHeight(); j++) {
                Color c = new Color(img_inp.getRGB(i, j));
                int r = c.getRed();
                int g = c.getGreen();
                int b = c.getBlue();
                int brightness = (r + g + b) / 3;   //The brightness of a pixel (turn colors into black-white-gray)

                if (brightness >= factor) { //If pixel is bright, paint it into white
                    img_out.setRGB(i, j, Color.WHITE.getRGB());
                } else {    //If pixel is dark, paint it into black
                    img_out.setRGB(i, j, Color.BLACK.getRGB());
                }
            }
        }
    }
}
