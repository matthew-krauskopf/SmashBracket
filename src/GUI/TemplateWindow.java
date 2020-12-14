package GUI;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import MyUtils.API;

public class TemplateWindow {

    JFrame window = new JFrame();

    // Background color for each window
    static Color bg_color = new Color(46, 52, 61);

    // Fonts
    static Font acumin12 = new Font("Acumin", 0, 12);
    static Font acumin16 = new Font("Acumin", 0, 16);
    static Font helveticaB12 = new Font("Helvetica", Font.BOLD, 12);
    static Font helvetica16 = new Font("Helvetica", 0, 16);
    static Font helveticaB16 = new Font("Helvetica", Font.BOLD, 16);
    static Font helvetica24 = new Font("Helvetica", 0, 24);
    static Font helveticaB24 = new Font("Helvetica", Font.BOLD, 24);
    static Font helvetica30 = new Font("Helvetica", 0, 30);
    static Font helveticaB40 = new Font("Helvetica", Font.BOLD, 40);
    static Font helveticaB50 = new Font("Helvetica", Font.BOLD, 50);

    // Screen Size
    static Dimension SCREEN_SIZE = Toolkit.getDefaultToolkit().getScreenSize();
    static int SCREEN_HEIGHT = SCREEN_SIZE.height*24/25;

    // Adjustments for window properties
    static int edge = 17;
    static int offset = 10;

    public int getTextWidth(JLabel l) {
        return l.getFontMetrics(l.getFont()).stringWidth(l.getText());
    }

    public int getTextWidth(JButton b) {
        return b.getFontMetrics(b.getFont()).stringWidth(b.getText());
    }

    public void dispose() {
        window.dispose();
    }

    public void launch() {
        window.setVisible(true);
    }

    public int min(int a, int b) {
        return (a < b ? a : b);
    }

    public int getCenter(JLabel l) {
        return offset+((window.getWidth()/2) - (l.getWidth()/2));
    }

    public int getCenter(JButton b) {
        return offset+((window.getWidth()/2) - (b.getWidth()/2));
    }

    public int getCenter(JTextField f) {
        return offset+((window.getWidth()/2) - (f.getWidth()/2));
    }
}
