import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class MonthlyBillManager extends JFrame {
    private List<Bill> bills = new ArrayList<>();
    private JList<Bill> billList;
    private DefaultListModel<Bill> billListModel;

    public MonthlyBillManager() {
        // Set up the GUI components
        setTitle("Monthly Bill Manager");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(600, 400));
        setLayout(new BorderLayout());

        // Create the bill lists
        billListModel = new DefaultListModel<>();
        billList = new JList<>(billListModel);
        billList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(billList);
        scrollPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        add(scrollPane, BorderLayout.CENTER);

        // toolbar
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);
        add(toolBar, BorderLayout.NORTH);

        //Bill button to the toolbar
        JButton addBillButton = new JButton("Add Bill");
        addBillButton.addActionListener(e -> addBill());
        toolBar.add(addBillButton);

        // "Delete Bill" button to the toolbar
        JButton deleteBillButton = new JButton("Delete Bill");
        deleteBillButton.addActionListener(e -> deleteBill());
        toolBar.add(deleteBillButton);

        // Pack the frame and show it
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void addBill() {
        //dialog to get the bill information from the user
        JTextField nameField = new JTextField();
        JTextField dueDateField = new JTextField();
        JComboBox<BillType> typeComboBox = new JComboBox<>(BillType.values());
        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Due Date (YYYY-MM-DD):"));
        panel.add(dueDateField);
        panel.add(new JLabel("Type:"));
        panel.add(typeComboBox);
        int result = JOptionPane.showConfirmDialog(this, panel, "Add Bill", JOptionPane.OK_CANCEL_OPTION);
        if (result != JOptionPane.OK_OPTION) {
            return;
        }

        // Parse the bill information from the dialog
        String name = nameField.getText();
        LocalDate dueDate;
        try {
            dueDate = LocalDate.parse(dueDateField.getText());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Invalid date format. Please enter a date in the format YYYY-MM-DD.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        BillType type = (BillType) typeComboBox.getSelectedItem();
        Bill bill = new Bill(name, dueDate, type);
        bills.add(bill);
        billListModel.addElement(bill);
    }

    private void deleteBill() {
        // Get the selected bill from the list
        int selectedIndex = billList.getSelectedIndex();
        if (selectedIndex == -1) {
            JOptionPane.showMessageDialog(this, "Please select a bill to delete.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        Bill selectedBill = billList.getSelectedValue();

        // Confirm that the user wants to delete the bill
        int result = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this bill?", "Delete Bill", JOptionPane.YES_NO_OPTION);
        if (result != JOptionPane.YES_OPTION) {
            return;
        }

        // Remove the bill from the list and the model
        bills.remove(selectedBill);
        billListModel.removeElement(selectedBill);
    }

    public static void main(String[] args) {
        // Set the look and feel to the system default
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Start the app
        SwingUtilities.invokeLater(MonthlyBillManager::new);
    }

    private enum BillType {
        CREDIT_CARD("Credit Card"),
        UTILITY("Utility"),
        SUBSCRIPTION("Subscription"),

        OTHER("Other");

        private final String name;

        BillType(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    private static class Bill {
        private final String name;
        private final LocalDate dueDate;
        private final BillType type;

        public Bill(String name, LocalDate dueDate, BillType type) {
            this.name = name;
            this.dueDate = dueDate;
            this.type = type;
        }

        public String getName() {
            return name;
        }

        public LocalDate getDueDate() {
            return dueDate;
        }

        public BillType getType() {
            return type;
        }

        public long getDaysUntilDue() {
            return LocalDate.now().until(dueDate, ChronoUnit.DAYS);
        }

        @Override
        public String toString() {
            return String.format("%s (%s, %d days)", name, type, getDaysUntilDue());
        }
    }
}
