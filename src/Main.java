import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Main extends JFrame implements ChangeListener, ActionListener {
    JButton btn_file;
    JButton btn_save;
    JTextField tbox_filename;
    JSlider slider_bin_factor;
    JSlider slider_scale;

    JLabel label_pic;
    JLabel label_brightness;
    JLabel label_quality;

    JPanel p_filepath;
    JPanel p_pic;
    JPanel p_sliders;

    JFileChooser fc;

    String filename;

    BufferedImage img_src;
    BufferedImage img_bin;
    BufferedImage img_scaled;

    Font font_labels;

    Binarizer br;

    Scaler sc;

    Nonocreator nn;


    public Main() {
        super("Nonogram creator");  //Window
        int width = 900;
        int height = 720;
        setSize(width, height);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);

        font_labels = new Font("Arial", Font.BOLD, 21);

        p_filepath = new JPanel();  //Panel for filepath
        p_filepath.setBounds(0, 0, 900, 75);
        p_filepath.setLayout(null);
        add(p_filepath);

        p_pic = new JPanel();   //Panel for picture
        p_pic.setBounds(0, 75, 560, 645);
        p_pic.setLayout(null);
        add(p_pic);

        p_sliders = new JPanel();   //Panel for sliders
        p_sliders.setBounds(560, 75, 340, 645);
        p_sliders.setLayout(null);
        add(p_sliders);

        tbox_filename = new JTextField();   //Filepath textbox
        tbox_filename.setPreferredSize(new Dimension(980, 30));
        tbox_filename.setEditable(false);
        tbox_filename.setBounds(135, 25, 700, 25);
        p_filepath.add(tbox_filename);

        BufferedImage img_frame = new BufferedImage(500, 500, BufferedImage.TYPE_INT_ARGB); //Empty picture frame
        Graphics2D g = img_frame.createGraphics();
        g.setStroke(new BasicStroke(2));
        g.setColor(Color.BLACK);
        g.drawRect(1, 1, 498, 498);

        label_pic = new JLabel("Your picture will be here");    //Label with picture
        label_pic.setIcon(new ImageIcon(img_frame));
        label_pic.setVerticalTextPosition(JLabel.BOTTOM);
        label_pic.setHorizontalTextPosition(JLabel.CENTER);
        label_pic.setFont(font_labels);
        label_pic.setBounds(20, 5, 500, 550);
        label_pic.setIconTextGap(10);
        p_pic.add(label_pic);

        slider_bin_factor = new JSlider(0, 255, 127);   //Binarization slider
        slider_bin_factor.setEnabled(false);
        slider_bin_factor.setPreferredSize(new Dimension(450, 30));
        slider_bin_factor.addChangeListener(this);
        slider_bin_factor.setBounds(0, 175, 300, 40);
        p_sliders.add(slider_bin_factor);

        slider_scale = new JSlider(1, 10, 1);   //Scale slider
        slider_scale.setEnabled(false);
        slider_scale.setPreferredSize(new Dimension(450, 30));
        slider_scale.addChangeListener(this);
        slider_scale.setBounds(0, 240, 300, 40);
        p_sliders.add(slider_scale);

        label_brightness = new JLabel("Brightness/Contrast");   //Label for 1st slider
        label_brightness.setFont(font_labels);
        label_brightness.setBounds(0, 215, 300, 25);
        label_brightness.setHorizontalAlignment(JLabel.CENTER);
        p_sliders.add(label_brightness);

        label_quality = new JLabel("Quality");  //Label for 2nd slider
        label_quality.setFont(font_labels);
        label_quality.setBounds(0, 280, 300, 25);
        label_quality.setHorizontalAlignment(JLabel.CENTER);
        p_sliders.add(label_quality);

        btn_file = new JButton();   //File choose button
        ImageIcon ico_folder = new ImageIcon("Folder_ico.png");
        btn_file.setPreferredSize(new Dimension(48, 48));
        btn_file.setIcon(ico_folder);
        btn_file.setBounds(20, 13, 48, 48);
        btn_file.addActionListener(this);
        p_filepath.add(btn_file);

        fc = new JFileChooser();    //File chooser
        fc.setFileFilter(new FileNameExtensionFilter("Pictures (*.jpg, *.png, *.jpeg)", "png", "PNG", "jpg", "JPG", "jpeg", "JPEG"));

        btn_save = new JButton();   //Nonogram save button
        btn_save.setEnabled(false);
        ImageIcon ico_save = new ImageIcon("Save_ico.png");
        btn_save.setPreferredSize(new Dimension(48, 48));
        btn_save.setIcon(ico_save);
        btn_save.setBounds(73, 13, 48, 48);
        btn_save.addActionListener(this);
        p_filepath.add(btn_save);

        br = new Binarizer(slider_bin_factor.getValue());

        sc = new Scaler(slider_scale.getValue());

        nn = new Nonocreator();

        setVisible(true);
    }

    public static void main(String[] args) {
        new Main();
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        if (img_src != null) {
            if (e.getSource() == slider_bin_factor) {   //If binarization slider has changes
                br.setFactor(slider_bin_factor.getValue());
                br.binarize();
                img_bin = br.getImg_out();
            }

            if (e.getSource() == slider_scale) {    //If scale slider has changes
                sc.setScale(slider_scale.getValue());   //1. Picture gets binarized
                sc.setImg_inp(img_src);
                sc.scale();
                img_scaled = sc.getImg_out();

                br.setImg_inp(img_scaled);  //2. Picture gets scaled
                br.binarize();
                img_bin = br.getImg_out();
            }

            drawImage(img_bin);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btn_file) {    //If file choose button was pressed
            fc.showOpenDialog(this);
            if (fc.getSelectedFile() != null) {
                filename = fc.getSelectedFile().getAbsolutePath();  //Get file name
                filename = filename.replace('\\', '/');
                tbox_filename.setText(filename);    //Set the file name into the textbox

                try {
                    img_src = ImageIO.read(new File(filename)); //Attempt to read picture file
                } catch (IOException ex) {
                    tbox_filename.setText("Image input error");
                }

                if (img_src != null) {  //If picture file was successfully read
                    slider_bin_factor.setEnabled(true); //Enable sliders and save button
                    slider_scale.setEnabled(true);
                    btn_save.setEnabled(true);

                    sc.setImg_inp(img_src);

                    long min_scale = (long) img_src.getWidth() * img_src.getHeight() * 5625 / Integer.MAX_VALUE;
                    min_scale = (int) Math.sqrt(min_scale) + 1;
                    long max_scale = (long) img_src.getWidth() * img_src.getHeight() / 100;
                    max_scale = (int) Math.sqrt(max_scale);
                    //Minimum and maximum scale factors (to not exceed picture limitations)
                    slider_scale.setMinimum((int) min_scale);
                    slider_scale.setMaximum((int) max_scale);
                    slider_scale.setValue((int) min_scale);
                    sc.setScale((int) min_scale);
                    System.out.println((int) min_scale);
                    sc.scale(); //Picture gets scaled
                    img_scaled = sc.getImg_out();

                    br.setImg_inp(img_scaled);  //2. Picture gets binarized
                    br.binarize();
                    img_bin = br.getImg_out();

                    drawImage(img_bin);
                    label_pic.setText("");  //Remove "Your picture will be here" caption
                }

            }

        }

        if (e.getSource() == btn_save) {    //If save button was pressed
            nn.setImg(img_bin); //The picture is sent to nonocreator
            nn.createNono();

            StringBuilder sb = new StringBuilder(filename); //Erase file extension from file name
            int ind_dot = filename.lastIndexOf('.');
            sb.delete(ind_dot, sb.length());

            if (sb.toString().contains("doggo")) {  //Easter egg)
                int ind_slash = filename.lastIndexOf('/');
                sb.delete(ind_slash + 1, sb.length());
                sb.append("white_guy.png");
            } else {
                sb.append("_nonogram.png");
            }

            File output_file = new File(sb.toString()); //Create output file

            try {
                ImageIO.write(nn.getNono(), "png", output_file);    //Save the result into output file
            } catch (IOException ex) {
                System.out.println("Output error");
            }
        }
    }

    public void drawImage(BufferedImage img) {  //A method to fit a picture into a 500x500 frame
        int scaled_width = 500;
        int scaled_height = 500;

        if (img.getHeight() != img.getWidth()) {    //Picture is scaled according to bigger side
            double k;
            if (img.getHeight() > img.getWidth()) {
                k = img.getWidth() / (double) img.getHeight();
                scaled_width = (int) (k * 500);
            } else {
                k = img.getHeight() / (double) img.getWidth();
                scaled_height = (int) (k * 500);
            }
        }

        label_pic.setIcon(new ImageIcon(img.getScaledInstance(scaled_width, scaled_height, Image.SCALE_DEFAULT)));
    }

}