package abdo.jonathan;

import javax.swing.*;

/**
 * Created by Jonathan on 8/11/2016.
 */
public class ToDoGui {
    private JPanel body;
    private JLabel title;
    private JLabel activeCount;
    private JButton add;
    private JComboBox filter;
    private JPanel menu;
    private JPanel header;
    private JButton remove;
    private JTable tasksTable;
    private JPanel tasksTablePanel;
    private JScrollPane tasksTableSPane;

    public static void main(String[] args) {
        JFrame frame = new JFrame("ToDoGui");
        frame.setContentPane(new ToDoGui().body);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        String[] columnNames = {"Description", "Status", "Remove"};
        Object[][] data = {{"Task 1", "Active", "Remove"},{"Task 2", "Completed", "Remove"}};
        tasksTable = new JTable(data, columnNames);
    }
}
