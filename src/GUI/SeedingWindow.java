package GUI;

import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import MyUtils.*;


public class SeedingWindow {

    JFrame window = new JFrame("Suggested Seeding");
    BracketPanel match_panel;
    JTable [] set_tables;
    JLabel [] w_round_labels;
    JLabel [] l_round_labels;
    JScrollPane matchups_sc_pane;
    JScrollPane seeded_sc_pane;
    JList<String> list;

    String [] entrants;
    Set [] sets;

    Color bg_color = new Color(46, 52, 61);
    Font font = new Font("Acumin", 0, 16);
    Font rounds_font = new Font("Helvetica", Font.BOLD, 16);

    int sq_entrants;
    int x_edge = 10;
    int set_gap = 75;

    int round;
    int max_win_rs;

    public void MakeSeedingList() {
        DefaultListModel<String> l1 = new DefaultListModel<>();
        for (int i = 0; i < entrants.length; i++) {
            l1.addElement((i+1) + ": " + entrants[i]);
        }
        list = new JList<>(l1);
        seeded_sc_pane = new JScrollPane(list);
    }

    public void MakeSeedingWindow() {
        // Set variables used over both winners and losers
        sq_entrants = (sets.length+3)/2;
        int tot = 0;
        int set_count = 0;
        max_win_rs = sq_entrants/2;

        // Calculate how many labels and set tables are needed
        w_round_labels = new JLabel[(int) (Math.log10(sq_entrants)/Math.log10(2))];
        l_round_labels = new JLabel[2 * ( (int)(Math.log10(sq_entrants)/Math.log10(2)) - 1)];
        set_tables = new JTable[sets.length];
        match_panel = new BracketPanel(set_tables);
        match_panel.setLayout(null);

        // Pack winner matches
        round = 1;
        int end = sq_entrants/2;

        while (tot < sq_entrants-1) {
            // Create label for this round and add it to panel
            w_round_labels[round-1] = GenerateLabel(round, 1);
            match_panel.add(w_round_labels[round-1]);

            int [] set_order = API.get_visual_order(0, end);
            // Go to the end of this round
            for (int cur = 0; cur < end ; cur++) {
                // Generate JTable
                set_tables[set_count] = GenerateJTable(sets[set_count]);

                // Set location of JTable
                int x_pos = 200*(round-1)+x_edge;
                int y_pos = (round == 1 ? set_gap*(cur+1)-25 : GetWinnersYLocation(set_tables, set_count, end, cur));
                set_tables[set_count].setLocation(x_pos, y_pos);

                // Set set JTable invisible if match is a Bye
                if (sets[set_count].l_player.equals("Bye")) {
                    set_tables[set_count].setVisible(false);
                }
                match_panel.add(set_tables[set_count]);
                set_count++;
            }
            tot += end;
            end /= 2;
            round++;
        }

        // Pack loser matches
        round = 1;
        end = sq_entrants/4;
        while(tot < sets.length) {
            l_round_labels[round-1] = GenerateLabel(round, -1);
            match_panel.add(l_round_labels[round-1]);
            // Go to the end of this round
            for (int cur = 0; cur < end ; cur++) {
                // Generate JTable
                set_tables[set_count] = GenerateJTable(sets[tot+cur]);

                // Set location of JTable
                int x_pos = 200*(round-1)+x_edge;
                int y_pos = (round == 1 ? set_gap*(max_win_rs+cur+1)+25 : GetLosersYLocation(set_tables, set_count, end, cur, round));
                set_tables[set_count].setLocation(x_pos, y_pos);

                // Set set JTable invisible if match is a Bye
                if (sets[set_count].l_player.equals("Bye")) {
                    set_tables[set_count].setVisible(false);
                }
                match_panel.add(set_tables[set_count]);
                set_count++;
            }
            tot += end;
            round++;
            // Number of sets in loser's only cuts in half every other round
            if (round % 2 == 1) end /= 2;
        }
    }

    private JLabel GenerateLabel(int round, int side) {
        JLabel label = new JLabel(String.format("%s's Round %d", (side == 1 ? "Winner" : "Loser"), round));
        label.setBounds(200*(round-1)+x_edge,(side == 1 ? 0 : set_gap*max_win_rs+50),200,30);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);

        label.setFont(rounds_font);
        label.setForeground(Color.WHITE);

        return label;
    }

    private JTable GenerateJTable(Set set_info) {
        // Create data with extra spaces in front of players' names
        String [][] data = {{Integer.toString(set_info.h_seed),"  " + set_info.h_player},
                            {Integer.toString(set_info.l_seed),"  " + set_info.l_player}};
        // Set columns names, despite them doing unused
        String [] columns = {"Seed", "Player"};
        JTable jt = new JTable(data, columns);
        jt.setSize(150, 50);
        // Set colors and fonts
        jt.setBackground(Color.WHITE);
        jt.setFont(font);
        // Disable table editing
        jt.setEnabled(false);
        // Set size of table rows and columns
        int places = (int) Math.ceil(Math.log10((double)entrants.length));
        jt.setRowHeight(jt.getHeight()/2);
        TableColumn first_column = jt.getColumnModel().getColumn(0);
        // NOTE: Change 16 to size of font used
        first_column.setMinWidth(16*places);
        first_column.setMaxWidth(first_column.getMinWidth());
        // Center first column text
        DefaultTableCellRenderer cR = new DefaultTableCellRenderer();
        cR.setHorizontalAlignment(JLabel.CENTER);
        first_column.setCellRenderer(cR);
        // Return finished table
        return jt;
    }

    private int GetWinnersYLocation(JTable [] set_tables, int set_count, int end, int cur) {
        // Find locations of previous matches that lead into this one
        int pos_above = set_tables[set_count-(end*2) + cur].getY();
        int pos_below = set_tables[set_count-(end*2) + cur + 1].getY();
        return (pos_above+pos_below)/2;
    }

    private int GetLosersYLocation(JTable [] set_tables, int set_count, int end, int cur, int round) {
        // Odd numbered rounds condense
        if (round % 2 == 1) {
            int pos_above = set_tables[set_count-(end*2) + cur].getY();
            int pos_below = set_tables[set_count-(end*2) + cur + 1].getY();
            return (pos_above+pos_below)/2;
        }
        // Even number rounds just shift over
        else {
            int pos = set_tables[set_count-end].getY();
            return pos;
        }
    }

    public void highlight_player(int seed) {
        // Highlight selected player's sets in list
        for (int i = 0; i < set_tables.length; i++) {
            String h_seed = (String)(set_tables[i].getValueAt(0,0));
            String l_seed = (String)(set_tables[i].getValueAt(1,0));
            if (h_seed.trim().equals(Integer.toString(seed+1)) ||
                l_seed.trim().equals(Integer.toString(seed+1)))
            {
                set_tables[i].setBackground(Color.YELLOW);
            }
            else {
                set_tables[i].setBackground(Color.WHITE);
            }
        }
    }

    public SeedingWindow(String [] fed_entrants, Set [] fed_sets) {
        // Attach fed in arguments
        entrants = fed_entrants;
        sets = fed_sets;

        // Call JComponent construction functions
        MakeSeedingList();
        MakeSeedingWindow();

        matchups_sc_pane = new JScrollPane(match_panel);

        // Set Window Attributes
        window.setLayout(null);

        // Set fonts and colors
        list.setFont(font);
        list.setBackground(bg_color);
        list.setForeground(Color.WHITE);

        match_panel.setBackground(bg_color);

        // Set component sizes
        window.setSize(1500,750);

        seeded_sc_pane.setSize((int)(window.getWidth()*.125), window.getHeight()-40);
        matchups_sc_pane.setSize(window.getWidth()-seeded_sc_pane.getWidth(), window.getHeight()-40);

        // Set component locations
        //match_panel.setLocation(0, 0);
        seeded_sc_pane.setLocation(0, 0);
        matchups_sc_pane.setLocation(seeded_sc_pane.getX()+seeded_sc_pane.getWidth(), 0);

        // Set Misc.
        //match_panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        match_panel.setMinimumSize(new Dimension(window.getWidth()-(int)(window.getWidth()*.125)-20, window.getHeight()-40));
        int index = (sq_entrants-1)+(sq_entrants/4)-1;
        match_panel.setPreferredSize(new Dimension(200*(round-1), (set_tables[index].getY() + set_tables[index].getHeight() + 20)));

        // Set Scrollbar Policies
        matchups_sc_pane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        matchups_sc_pane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        matchups_sc_pane.getVerticalScrollBar().setUnitIncrement(16);

        seeded_sc_pane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        seeded_sc_pane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        // Add action listeners
        window.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                window.dispose();
            }
        });

        list.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    highlight_player(list.getSelectedIndex());
                }
            }
        });

        // Pack items into window
        window.getContentPane().add(seeded_sc_pane);
        window.getContentPane().add(matchups_sc_pane);
    }

    public void Launch() {
        window.setVisible(true);
    }
}
