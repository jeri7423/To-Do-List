import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.*;

public class TodoApp extends JFrame {
    private CurvedTextField taskInput;
    private CurvedButton addTaskButton;
    private CurvedButton addDateButton;
    private CurvedButton addTimeButton;
    private JPanel tasksPanel;
    private JLabel dateLabel;
    private JLabel timeLabel;

    public TodoApp() {
        setTitle("Simple To Do List");
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        setLocationRelativeTo(null);

        // Instructions label
        JLabel instructions = new JLabel("MY TO DO LIST");
        instructions.setBounds(40, 10, 420, 40);
        instructions.setHorizontalAlignment(SwingConstants.CENTER);
        instructions.setFont(new Font("Poppins", Font.PLAIN, 18));
        add(instructions);

        // Task input field with placeholder
        taskInput = new CurvedTextField(20);
        taskInput.setBounds(25, 60, 300, 40);
        taskInput.setFont(new Font("Poppins", Font.PLAIN, 16));
        taskInput.setText("Task to be done...");
        taskInput.setForeground(Color.GRAY);

        // Manage placeholder text behavior
        taskInput.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (taskInput.getText().equals("Task to be done...")) {
                    taskInput.setText("");
                    taskInput.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (taskInput.getText().isEmpty()) {
                    taskInput.setText("Task to be done...");
                    taskInput.setForeground(Color.GRAY);
                }
            }
        });

        // Add task button
        addTaskButton = new CurvedButton("Add", 20);
        addTaskButton.setBounds(340, 60, 100, 40);
        addTaskButton.setBackground(new Color(128, 82, 236));
        addTaskButton.setForeground(Color.WHITE);
        addTaskButton.setFont(new Font("Poppins", Font.PLAIN, 16));

        // Add date button
        addDateButton = new CurvedButton("Date", 20);
        addDateButton.setBounds(50, 110, 150, 40);
        addDateButton.setBackground(new Color(128, 82, 236));
        addDateButton.setForeground(Color.WHITE);
        addDateButton.setFont(new Font("Poppins", Font.PLAIN, 16));

        // Add time button
        addTimeButton = new CurvedButton("Time", 20);
        addTimeButton.setBounds(250, 110, 150, 40);
        addTimeButton.setBackground(new Color(128, 82, 236));
        addTimeButton.setForeground(Color.WHITE);
        addTimeButton.setFont(new Font("Poppins", Font.PLAIN, 16));

        // Date label to display the selected date
        dateLabel = new JLabel();
        dateLabel.setBounds(25, 160, 200, 40);
        dateLabel.setFont(new Font("Poppins", Font.PLAIN, 16));
        add(dateLabel);

        // Time label to display the selected time
        timeLabel = new JLabel();
        timeLabel.setBounds(240, 160, 200, 40);
        timeLabel.setFont(new Font("Poppins", Font.PLAIN, 16));
        add(timeLabel);

        // Panel to hold tasks
        tasksPanel = new JPanel();
        tasksPanel.setLayout(new BoxLayout(tasksPanel, BoxLayout.Y_AXIS));
        tasksPanel.setBackground(null);

        // Scroll pane for the tasks panel
        JScrollPane scrollPane = new JScrollPane(tasksPanel);
        scrollPane.setBounds(25, 190, 415, 220);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBorder(null); // Remove border

        // Add components to the frame
        add(taskInput);
        add(addTaskButton);
        add(addDateButton);
        add(addTimeButton);
        add(scrollPane);

        // Add task action
        addTaskButton.addActionListener(e -> addTask(taskInput.getText()));

        // Date button action
        addDateButton.addActionListener(e -> selectDate());

        // Time button action
        addTimeButton.addActionListener(e -> selectTime());

        setVisible(true);
    }

    // Method to add a new task
    private void addTask(String task) {
        if (task.isEmpty() || task.equals("Task to be done...")) {
            JOptionPane.showMessageDialog(this, "Please enter a valid task.");
            return;
        }

        String dateText = dateLabel.getText();
        String timeText = timeLabel.getText();
        final String taskWithDateTime;

        if (!dateText.isEmpty() || !timeText.isEmpty()) {
            taskWithDateTime = task +"  " +" on " + dateText + " at " + timeText;
        } else {
            taskWithDateTime = task;
        }

        JPanel taskPanel = new JPanel();
        taskPanel.setLayout(new BorderLayout());
        taskPanel.setPreferredSize(new Dimension(400, 40));
        taskPanel.setMaximumSize(new Dimension(415, 40));
        taskPanel.setOpaque(false);

        JLabel taskLabel = new JLabel(taskWithDateTime);
        taskLabel.setFont(new Font("Poppins", Font.PLAIN, 16));
        taskLabel.setPreferredSize(new Dimension(300, 40));

        CurvedButton deleteButton = new CurvedButton("Delete", 20);
        deleteButton.setBackground(new Color(128, 82, 236));
        deleteButton.setForeground(Color.WHITE);
        deleteButton.setPreferredSize(new Dimension(100, 40));

        // Create a separator
        JSeparator separator = new JSeparator();
        separator.setMaximumSize(new Dimension(400, 10));

        deleteButton.addActionListener(e -> {
            tasksPanel.remove(taskPanel);
            tasksPanel.remove(separator);
            tasksPanel.revalidate();
            tasksPanel.repaint();
        });

        // Action to mark the task as completed
        taskLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (taskLabel.getFont().getStyle() == Font.PLAIN) {
                    taskLabel.setFont(taskLabel.getFont().deriveFont(Font.ITALIC | Font.BOLD));
                    taskLabel.setForeground(Color.GRAY);
                    taskLabel.setText("<html><strike>" + taskWithDateTime + "</strike></html>");
                } else {
                    taskLabel.setFont(taskLabel.getFont().deriveFont(Font.PLAIN));
                    taskLabel.setForeground(Color.BLACK);
                    taskLabel.setText(taskWithDateTime);
                }
            }
        });

        taskPanel.add(taskLabel, BorderLayout.WEST);
        taskPanel.add(deleteButton, BorderLayout.EAST);

        tasksPanel.add(taskPanel);
        tasksPanel.add(separator);

        tasksPanel.revalidate();
        tasksPanel.repaint();

        taskInput.setText(""); // Clear the text field after adding task
        dateLabel.setText(""); // Clear the date label
        timeLabel.setText(""); // Clear the time label
    }

    // Method to select a date
    private void selectDate() {
        JSpinner dateSpinner = new JSpinner(new SpinnerDateModel());
        dateSpinner.setEditor(new JSpinner.DateEditor(dateSpinner, "dd-MM-yyyy"));

        int option = JOptionPane.showOptionDialog(
                this,
                dateSpinner,
                "Select Date",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                null,
                null
        );

        if (option == JOptionPane.OK_OPTION) {
            Date date = (Date) dateSpinner.getValue();
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            String dateString = dateFormat.format(date);
            dateLabel.setText(dateString);
        }
    }

    // Method to select a time
    private void selectTime() {
        JSpinner timeSpinner = new JSpinner(new SpinnerDateModel());
        timeSpinner.setEditor(new JSpinner.DateEditor(timeSpinner, "HH:mm"));

        int option = JOptionPane.showOptionDialog(
                this,
                timeSpinner,
                "Select Time",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                null,
                null
        );

        if (option == JOptionPane.OK_OPTION) {
            Date time = (Date) timeSpinner.getValue();
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
            String timeString = timeFormat.format(time);
            timeLabel.setText(timeString);
        }
    }

    // CurvedTextField with rounded corners
    class CurvedTextField extends JTextField {
        private int radius;

        public CurvedTextField(int radius) {
            this.radius = radius;
            setOpaque(false);
            setBorder(BorderFactory.createEmptyBorder()); // Remove default border
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), radius, radius));
            super.paintComponent(g);
            g2.dispose();
        }

        @Override
        protected void paintBorder(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(Color.BLACK); // Border color
            g2.draw(new RoundRectangle2D.Float(0, 0, getWidth() - 1, getHeight() - 1, radius, radius));
            g2.dispose();
        }
    }

    // CurvedButton with rounded corners
    class CurvedButton extends JButton {
        private int radius;

        public CurvedButton(String text, int radius) {
            super(text);
            this.radius = radius;
            setContentAreaFilled(false);
            setFocusPainted(false);
            setOpaque(false);
            setBorder(BorderFactory.createEmptyBorder()); // Remove default border
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), radius, radius));
            super.paintComponent(g);
            g2.dispose();
        }

        @Override
        protected void paintBorder(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(Color.BLACK);// Border color
            g2.draw(new RoundRectangle2D.Float(0, 0, getWidth() - 1, getHeight() - 1, radius, radius));
            g2.dispose();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(TodoApp::new);
    }
}
