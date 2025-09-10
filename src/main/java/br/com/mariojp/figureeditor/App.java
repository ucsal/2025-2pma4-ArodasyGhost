package br.com.mariojp.figureeditor;

import javax.swing.*;
import java.awt.*;

public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {}

            JFrame frame = new JFrame("Figure Editor — Clique para inserir figuras");
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

            DrawingPanel panel = new DrawingPanel();

            JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            
            JButton colorButton = new JButton("Cor...");
            colorButton.addActionListener(e -> {
                Color newColor = JColorChooser.showDialog(frame, "Escolha a cor da figura", panel.getCurrentColor());
                if (newColor != null) {
                    panel.setCurrentColor(newColor);
                }
            });
            
            String[] shapeTypes = {"Círculo", "Retângulo"};
            JComboBox<String> shapeSelector = new JComboBox<>(shapeTypes);
            shapeSelector.addActionListener(e -> {
                String selected = (String) shapeSelector.getSelectedItem();
                if ("Círculo".equals(selected)) {
                    panel.setShapeFactory(new EllipseFactory());
                } else {
                    panel.setShapeFactory(new RectangleFactory());
                }
            });
            
            topPanel.add(colorButton);
            topPanel.add(new JLabel("Tipo:"));
            topPanel.add(shapeSelector);
            
            frame.setLayout(new BorderLayout());
            frame.add(topPanel, BorderLayout.NORTH);
            frame.add(panel, BorderLayout.CENTER);
            

            frame.setSize(900, 600);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
