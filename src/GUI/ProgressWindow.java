package GUI;

import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import MyUtils.*;

public class ProgressWindow {

    static JFrame window = new JFrame("Import Progress");
    static JLabel message = new JLabel("Checking if data is new...", SwingConstants.CENTER);
    static JButton ok_button = new JButton("OK");

    public void Launch(String[] entrants, String url) {

        message.setBounds(0, 10, 300, 20);
        message.setAlignmentX(Component.CENTER_ALIGNMENT);
        ok_button.setBounds(70, 50, 160, 50);
        ok_button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                window.setVisible(false);
                ReadFile.clean_tmp_files();
            }
        });

        window.setLayout(null);
        window.add(message); window.add(ok_button);
        window.setSize(320,150);
        window.setVisible(true);

        int status = API.AddBracketData(url, entrants);
        if (status == 1) {
            message.setText("Done!");
        }
        else if (status == 0) {
            message.setText("Tourney results already exist in database!");
        }
        else {
            message.setText("Unknown Error");
        }
    }
}