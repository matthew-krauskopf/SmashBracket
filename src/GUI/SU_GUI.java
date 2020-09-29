package GUI;

import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import MyUtils.*;

public class SU_GUI {

    static Font font = new Font("Acumin", 0, 16);
    
    // Attach instances of each sub-window
    static PreSeedingWindow PS_window;
    static ImportWindow IR_window;
    static RankingsWindow Rank_window;
    
    public static void main_menu() {
        // TODO Remove later
        ReadFile.clean_tmp_files();
        JFrame window = new JFrame("AutoBracket");

        // Create Buttons
        JButton b1 = new JButton("Seed Bracket");
        b1.setBounds(40,30,200, 40);//x axis, y axis, width, height
        b1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                PS_window = new PreSeedingWindow();
                PS_window.Launch();
            }
        });

        JButton b2 = new JButton("Import Results");
        b2.setBounds(40,90,200, 40);//x axis, y axis, width, height
        b2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                IR_window = new ImportWindow();
                IR_window.Launch();
            }
        });

        JButton b3 = new JButton("View Rankings");
        b3.setBounds(40, 150, 200, 40);
        b3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Rank_window = new RankingsWindow();
                Rank_window.Launch();
            }
        });

        window.add(b1);//adding button in JFrame
        window.add(b2);
        window.add(b3);

        window.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        window.setSize(300,250);
        window.setLayout(null);//using no layout managers
        window.setVisible(true);//making the frame visible
    }
}