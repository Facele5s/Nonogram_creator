import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Arrays;

public class Nonocreator {
    private BufferedImage img;
    private BufferedImage img_nono;
    private int[][] vertical;   //Vertical lines
    private int[][] horizontal; //Horizontal lines

    public void setImg(BufferedImage img) {
        this.img = img;
        vertical = new int[img.getWidth()][img.getHeight() / 2 + 1];    //Every line can have maximum (n / 2 + 1) cells
        horizontal = new int[img.getHeight()][img.getWidth() / 2 + 1];

        for (int[] line : vertical) {
            Arrays.fill(line, 0);
        }

        for (int[] line : horizontal) {
            Arrays.fill(line, 0);
        }
    }

    public BufferedImage getNono() {
        return img_nono;
    }

    public void createNono() {
        Color c;
        int maxw = 0;   //Maximum quantity of horizontal number cells
        int maxh = 0;   //Maximum quantity of vertical number cells

        for (int i = 0; i < img.getWidth(); i++) {  //Vertical lines
            boolean flag = false;
            int ind = 1;
            int count = 0;
            int series = 0; //Number of black areas. The 1st place in array

            for (int j = 0; j < img.getHeight(); j++) { //Count black cells in a row
                c = new Color(img.getRGB(i, j));
                if (c.equals(Color.BLACK)) {
                    flag = true;
                    count++;
                    if (j == img.getHeight() - 1) {
                        vertical[i][ind] = count;
                        series++;
                    }
                } else {
                    if (flag) {
                        vertical[i][ind] = count;
                        series++;
                        flag = false;
                        ind++;
                    }

                    count = 0;
                }
            }
            vertical[i][0] = series;
            maxh = Math.max(maxh, series);
        }

        for (int i = 0; i < img.getHeight(); i++) { //Horizontal lines
            boolean flag = false;
            int ind = 1;
            int count = 0;
            int series = 0; //Number of black areas. The 1st place in array

            for (int j = 0; j < img.getWidth(); j++) {  //Count black cells in a row
                c = new Color(img.getRGB(j, i));
                if (c.equals(Color.BLACK)) {
                    flag = true;
                    count++;
                    if (j == img.getWidth() - 1) {
                        horizontal[i][ind] = count;
                        series++;
                    }

                } else {
                    if (flag) {
                        horizontal[i][ind] = count;
                        series++;
                        flag = false;
                        ind++;
                    }

                    count = 0;
                }
            }
            horizontal[i][0] = series;
            maxw = Math.max(maxw, series);
        }

        int nono_width = maxw + img.getWidth(); //Number of all horizontal cells in nonogram
        int nono_height = maxh + img.getHeight();   //Number of all vertical cells in nonogram

        img_nono = new BufferedImage(nono_width * 40 + 15, nono_height * 40 + 15, img.getType());   //Nonogram
        Graphics2D g = img_nono.createGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, img_nono.getWidth(), img_nono.getHeight());    //Background

        g.setColor(new Color(0xe0e0e0));    //Left upper corner
        g.fillRect(5, 5, maxw * 40, maxh * 40);

        g.setColor(Color.BLACK);
        g.setStroke(new BasicStroke(5));
        g.drawRect(2, 2, img_nono.getWidth() - 5, img_nono.getHeight() - 5);    //Outer frame
        g.drawLine(maxw * 40 + 7, 0, maxw * 40 + 7, img_nono.getHeight());  //Vertical line-separator
        g.drawLine(0, maxh * 40 + 7, img_nono.getWidth(), maxh * 40 + 7);   //Horizontal line-separator

        int x = maxw * 40 + 10;
        int y = 5;

        g.setStroke(new BasicStroke(1));

        for (int i = 0; i < img.getWidth(); i++) {  //Horizontal cells
            for (int j = 0; j < maxh; j++) {
                g.drawRect(x + i * 40, y + j * 40, 40, 40);
            }
        }

        x = 5;
        y = maxh * 40 + 10;

        for (int i = 0; i < maxw; i++) {    //Vertical cells
            for (int j = 0; j < img.getHeight(); j++) {
                g.drawRect(x + i * 40, y + j * 40, 40, 40);
            }
        }

        x = maxw * 40 + 10;
        y = maxh * 40 + 10;

        for (int i = 0; i < img.getWidth(); i++) {  //Inner cells
            for (int j = 0; j < img.getHeight(); j++) {
                g.drawRect(x + i * 40, y + j * 40, 40, 40);
            }
        }

        g.setStroke(new BasicStroke(4));    //Vertical lines every 5 cells
        for (int i = (maxw + 5) * 40 + 11; i < img_nono.getWidth(); i += 200) {
            g.drawLine(i, 0, i, img_nono.getHeight());
        }

        for (int i = (maxh + 5) * 40 + 11; i < img_nono.getHeight(); i += 200) {    //Horizontal lines every 5 cells
            g.drawLine(0, i, img_nono.getWidth(), i);
        }

        Font font12 = new Font("Arial", Font.PLAIN, 30);
        Font font3 = new Font("Arial", Font.PLAIN, 20);

        x = maxw * 40 + 10;
        y = maxh * 40 + 5;

        for (int i = 0; i < vertical.length; i++) { //Vertical numbers
            for (int j = vertical[i][0]; j > 0; j--) {
                int offX;
                int offY;
                if (vertical[i][j] > 99) {  //Choose the font size
                    g.setFont(font3);
                    offX = 3;
                    offY = -10;
                } else if (vertical[i][j] > 9) {
                    g.setFont(font12);
                    offX = 2;
                    offY = -7;
                } else {
                    g.setFont(font12);
                    offX = 11;
                    offY = -7;
                }

                g.drawString(Integer.toString(vertical[i][j]), x + i * 40 + offX, y - (vertical[i][0] - j) * 40 + offY);
            }
        }

        x = maxw * 40 - 35;
        y = maxh * 40 + 50;

        for (int i = 0; i < horizontal.length; i++) {   //Horizontal numbers
            for (int j = horizontal[i][0]; j > 0; j--) {
                int offX;
                int offY;
                if (horizontal[i][j] > 99) {    //Choose the font size
                    g.setFont(font3);
                    offX = 3;
                    offY = -10;
                } else if (horizontal[i][j] > 9) {
                    g.setFont(font12);
                    offX = 2;
                    offY = -7;
                } else {
                    g.setFont(font12);
                    offX = 11;
                    offY = -7;
                }

                g.drawString(Integer.toString(horizontal[i][j]), x - (horizontal[i][0] - j) * 40 + offX, y + i * 40 + offY);
            }
        }

    }
}
