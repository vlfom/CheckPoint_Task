package display;

import javafx.util.Pair;
import user.User;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Table {
    static ArrayList<String[]> data = new ArrayList<String[]>();
    static ExampleTableModel exampleTableModel;
    static public ArrayList<User> users;
    static JTable table;
    static Set<Pair<Integer, Integer>> marked = new HashSet<Pair<Integer, Integer>>();

    public Table() {
        JFrame guiFrame = new JFrame();

        guiFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        guiFrame.setTitle("Mobile Network");
        guiFrame.setSize(1170, 800);
        guiFrame.setResizable(false);

        guiFrame.setLocationRelativeTo(null);

        exampleTableModel = new ExampleTableModel();
        table = new JTable(exampleTableModel);

        table.setAutoCreateRowSorter(true);
        table.getTableHeader().setPreferredSize(new Dimension(100, 60));
        table.setRowHeight(50);
        table.setFont(new Font("Serif", Font.PLAIN, 20));
        table.getTableHeader().setFont(new Font("Serif", Font.BOLD, 24));
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.setDefaultRenderer(String.class, centerRenderer);
        table.getTableHeader().setBackground(new Color(250,250,230)) ;

        table.setGridColor(Color.lightGray);
        table.setBackground(new Color(230, 230, 230));
        table.getColumnModel().setColumnMargin(10) ;

        Integer[] dimensions = {250, 150, 200, 250, 150, 150};
        for (int i = 0; i < 6; ++i) {
            table.getColumnModel().getColumn(i).setMinWidth(dimensions[i]);
            table.getColumnModel().getColumn(i).setMaxWidth(dimensions[i]);
        }

        JScrollPane tableScrollPane = new JScrollPane(table);

        guiFrame.add(tableScrollPane);
        guiFrame.setVisible(true);

        table.setDefaultRenderer(String.class, new BoardTableCellRenderer());
    }

    static class BoardTableCellRenderer extends DefaultTableCellRenderer {

        private static final long serialVersionUID = 1L;

        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int col) {
            Component c = super.getTableCellRendererComponent(table, value,
                    isSelected, hasFocus, row, col);
            c.setForeground(Color.black);
            if (marked.contains(new Pair<Integer,Integer>(row,col))) {
                c.setBackground(new Color(220, 250, 220));
            } else {
                c.setBackground(new Color(240, 240, 240));
            }
            return c;
        }
    }

    synchronized public static String[] constructInfo(int index) {
        User user = users.get(index);
        String
                money = (new BigDecimal(user.money).setScale(2, BigDecimal.ROUND_HALF_UP).toString()) + " $",
                companion = (user.getCompanion() == null ? "-" : user.getCompanion().name),
                lastSms = (user.lastSms == null ? "-" :
                        Duration.between(user.lastSms, LocalDateTime.now()).getSeconds() + " seconds ago");
        if( user.upMarked )
            money += " + " + (new BigDecimal(user.lastUpAmount).setScale(2, BigDecimal.ROUND_HALF_UP).toString()) + "$" ;
        return new String[]{
                user.name,
                money,
                user.operator.name,
                companion,
                lastSms,
                "Not in use"
        };
    }

    synchronized public static void update(int index) {
        data.set(index, constructInfo(index));
        exampleTableModel.fireTableDataChanged();
    }

    synchronized public static void updateAll() {
        data = new ArrayList<String[]>(users.size());
        for (int i = 0; i < users.size(); ++i)
            if (data.size() <= i)
                data.add(constructInfo(i));
            else
                data.set(i, constructInfo(i));
        exampleTableModel.fireTableDataChanged();
    }

    synchronized public static void markCell(int row, int column) {
        marked.add(new Pair<Integer, Integer>(row, column)) ;
    }

    synchronized public static void unmarkCell(int row, int column) {
        marked.remove(new Pair<Integer, Integer>(row, column)) ;
    }

    class ExampleTableModel extends AbstractTableModel {
        String[] columnNames = {"Person", "Money left", "Operator", "Talking with",
                "Last sms", "Internet"};

        @Override
        synchronized public int getRowCount() {
            return data.size();
        }

        @Override
        synchronized public int getColumnCount() {
            return columnNames.length;
        }

        @Override
        synchronized public Object getValueAt(int row, int column) {
            return data.get(row)[column];
        }

        @Override
        synchronized public String getColumnName(int column) {
            return columnNames[column];
        }

        @Override
        synchronized public Class getColumnClass(int c) {
            return getValueAt(0, c).getClass();
        }
    }
}