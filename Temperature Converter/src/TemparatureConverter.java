import javax.swing.*;
import java.awt.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.ImageIO;

public class TemparatureConverter extends JFrame {

    private JTextField inputField;
    private JComboBox<String> fromUnit;
    private JComboBox<String> toUnit;
    private JLabel resultLabel;
    private JLabel healthLabel;
    private BackgroundPanel mainPanel;

    public TemparatureConverter() {
        setTitle("🌡 Temperature Converter + Health Checker");
        setSize(600, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Load background image
        Image bgImage = null;
        try {
            // Change the path below to your image file location
            bgImage = ImageIO.read(new File("background.jpg"));
        } catch (IOException e) {
            System.err.println("Background image not found.");
        }

        mainPanel = new BackgroundPanel(bgImage);
        mainPanel.setLayout(new GridLayout(7, 1, 10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel titleLabel = new JLabel("🌡 Temperature Converter & Health Status", JLabel.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);

        JLabel noteLabel = new JLabel("💡 Enter your body temperature to check your health or convert temperatures", JLabel.CENTER);
        noteLabel.setFont(new Font("Segoe UI", Font.ITALIC, 16));
        noteLabel.setForeground(Color.BLUE);

        inputField = new JTextField();
        inputField.setPreferredSize(new Dimension(150, 25));

        JPanel inputPanel = new JPanel();
        inputPanel.setOpaque(false);
        inputPanel.add(new JLabel("Enter Temperature: "));
        inputPanel.add(inputField);
        inputPanel.setForeground(Color.WHITE);

        fromUnit = new JComboBox<>(new String[]{"Celsius", "Fahrenheit", "Kelvin"});
        toUnit = new JComboBox<>(new String[]{"Celsius", "Fahrenheit", "Kelvin"});

        JPanel unitPanel = new JPanel();
        unitPanel.setOpaque(false);
        unitPanel.add(new JLabel("From: "));
        unitPanel.add(fromUnit);
        unitPanel.add(new JLabel("To: "));
        unitPanel.add(toUnit);

        JButton convertButton = new JButton("Convert 🔄");
        convertButton.setBackground(new Color(100, 149, 237));
        convertButton.setForeground(Color.WHITE);
        convertButton.setFocusPainted(false);
        convertButton.setPreferredSize(new Dimension(120, 35));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.add(convertButton);

        resultLabel = new JLabel("Result: ", JLabel.CENTER);
        resultLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        resultLabel.setForeground(Color.WHITE);

        healthLabel = new JLabel("💙 Health Status: --", JLabel.CENTER);
        healthLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        healthLabel.setForeground(Color.BLACK);

        mainPanel.add(titleLabel);
        mainPanel.add(noteLabel);
        mainPanel.add(inputPanel);
        mainPanel.add(unitPanel);
        mainPanel.add(buttonPanel);
        mainPanel.add(resultLabel);
        mainPanel.add(healthLabel);

        add(mainPanel);

        convertButton.addActionListener(e -> convertTemperature());

        setVisible(true);
    }

    private void convertTemperature() {
        try {
            double inputTemp = Double.parseDouble(inputField.getText());
            String from = (String) fromUnit.getSelectedItem();
            String to = (String) toUnit.getSelectedItem();
            double result = 0;

            if (from.equals(to)) {
                result = inputTemp;
            } else if (from.equals("Celsius") && to.equals("Fahrenheit")) {
                result = (inputTemp * 9 / 5) + 32;
            } else if (from.equals("Celsius") && to.equals("Kelvin")) {
                result = inputTemp + 273.15;
            } else if (from.equals("Fahrenheit") && to.equals("Celsius")) {
                result = (inputTemp - 32) * 5 / 9;
            } else if (from.equals("Fahrenheit") && to.equals("Kelvin")) {
                result = (inputTemp - 32) * 5 / 9 + 273.15;
            } else if (from.equals("Kelvin") && to.equals("Celsius")) {
                result = inputTemp - 273.15;
            } else if (from.equals("Kelvin") && to.equals("Fahrenheit")) {
                result = (inputTemp - 273.15) * 9 / 5 + 32;
            }

            resultLabel.setText("Result: " + String.format("%.2f", result) + " " + getUnitSymbol(to));

            double tempCelsius;
            if (to.equals("Celsius")) {
                tempCelsius = result;
            } else if (to.equals("Fahrenheit")) {
                tempCelsius = (result - 32) * 5 / 9;
            } else {
                tempCelsius = result - 273.15;
            }

            updateHealthStatus(tempCelsius);

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid number!", "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateHealthStatus(double tempCelsius) {
        if (tempCelsius >= 36.1 && tempCelsius <= 37.2) {
            healthLabel.setText("💙 Health Status: Normal");
            healthLabel.setForeground(new Color(0, 128, 255));
        } else if (tempCelsius > 37.2 && tempCelsius <= 38) {
            healthLabel.setText("🌤 Health Status: Mild Fever");
            healthLabel.setForeground(new Color(285, 140, 0));
        } else if (tempCelsius > 38) {
            healthLabel.setText("🔥 Health Status: High Fever");
            healthLabel.setForeground(Color.BLACK);
        } else {
            healthLabel.setText("🌿 Health Status: Low Temperature");
            healthLabel.setForeground(new Color(34, 139, 34));
        }
    }

    private String getUnitSymbol(String unit) {
        switch (unit) {
            case "Celsius": return "°C";
            case "Fahrenheit": return "°F";
            case "Kelvin": return "K";
            default: return "";
        }
    }

    // Custom JPanel to paint background image scaled
    private static class BackgroundPanel extends JPanel {
        private final Image backgroundImage;

        public BackgroundPanel(Image image) {
            this.backgroundImage = image;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (backgroundImage != null) {
                // Draw image scaled to fill panel
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            } else {
                // fallback background color if no image
                g.setColor(new Color(50, 50, 100));
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(TemparatureConverter::new);
    }
}
