package GUI;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import MyUtils.API;
import MyUtils.Match;

public class ImportWindow extends GetLink {

    String title = "Import Results";
    GetAliasWindow get_alias_window;
    ProgressWindow progress_window;
    PingingWindow ping_window;
    JLabel dup_label = new JLabel("Bracket data already imported!");

    public ImportWindow() {
        // Set Window Attributes
        window.setTitle(title);

        // Set fonts and colors
        dup_label.setFont(helveticaB12);
        dup_label.setForeground(Color.WHITE);

        // Set component sizes
        dup_label.setSize(getTextWidth(dup_label), 20);
        window.setSize((2*offset)+field.getWidth()+edge, submit.getY()+80+offset);

        // Set component locations
        dup_label.setLocation(getCenter(dup_label), example.getHeight()+example.getY());

        // Add action listeners
        window.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                window.dispose();
            }
        });

        // Pack items into window
        window.add(dup_label);

        // Set starting visibility
        error.setVisible(false);
        f_error.setVisible(false);
        dup_label.setVisible(false);
    }

    private void launchProgressWindow(String [] entrants, int tourney_id) {
        Match [] results = API.getMatches();
        String tourney_name = API.getTourneyName();
        // Got all the info needed: clean files
        API.cleanTmpFiles();
        // Launch ProgressWindow
        progress_window = new ProgressWindow(entrants, results, tourney_id, tourney_name);
        progress_window.launch();
    }

    private void processHTMLFiles() {
        // Check if bracket has not been entered before
        // Tourney_id == -1 if imported already
        //            == -2 if could not obtain an ID
        int tourney_id = API.checkBracketNew();
        if (tourney_id == -1) {
            error.setVisible(false);
            f_error.setVisible(false);
            dup_label.setVisible(true);
            API.cleanTmpFiles();
            return;
        }
        //
        else if (tourney_id == -2) {
            dup_label.setVisible(false);
            f_error.setVisible(false);
            error.setVisible(true);
            API.cleanTmpFiles();
            return;
        }
        // All good: grab entrants
        String [] entrants = API.getEntrants();
        // Look for new names and ask if alias
        String [] unknown_entrants = API.checkUnknownNames(entrants);
        if (unknown_entrants.length != 0) {
            get_alias_window = new GetAliasWindow(unknown_entrants);
            // Add action listener to GA window so this window closes at same time
            get_alias_window.addCustomWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    window.setVisible(false);
                    get_alias_window.dispose();
                    launchProgressWindow(entrants, tourney_id);
                }
            });
            get_alias_window.launch();
        }
        else {
            window.setVisible(false);
            launchProgressWindow(entrants, tourney_id);
        }
    }

    @Override
    public void action() {
        // Check if URL seems to be valid
        String url = field.getText().trim();
        if (!Utils.validURL(url)) {
            dup_label.setVisible(false);
            error.setVisible(false);
            f_error.setVisible(true);
            return;
        }
        // Generate the needed HTML files
        ping_window = new PingingWindow(url, 3);
        ping_window.addVisibleListener(new ComponentListener () {
            @Override
            public void componentHidden(ComponentEvent e) {
                if (ping_window.isFinished()) {
                    window.toFront();
                    processHTMLFiles();
                }
                ping_window.dispose();
            }
            @Override
            public void componentShown(ComponentEvent e) {}
            @Override
            public void componentMoved(ComponentEvent e) {}
            @Override
            public void componentResized(ComponentEvent e) {}
        });
        ping_window.addCancelListener(new ActionListener () {
            @Override
            public void actionPerformed(ActionEvent e) {
                ping_window.cancelPing();
                ping_window.dispose();
                API.cleanTmpFiles();
            }
        });
        ping_window.launch();
    }
}
