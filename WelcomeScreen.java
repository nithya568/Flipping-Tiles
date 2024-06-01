import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class WelcomeScreen extends JFrame {
    private Image backgroundImage;

    public WelcomeScreen() {
        setTitle("Memory Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Load the background image
        backgroundImage = new ImageIcon("background.jpg").getImage(); // Change the path to your background image

        // Set up the layered pane to hold components with a background image
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(600, 400));

        // Create the background panel
        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (backgroundImage != null) {
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                }
            }
        };
        backgroundPanel.setBounds(0, 0, 600, 400);
        layeredPane.add(backgroundPanel, JLayeredPane.DEFAULT_LAYER);

        // Create a panel for the title
        JPanel titlePanel = new JPanel();
        titlePanel.setOpaque(false); // Make the title panel transparent
        titlePanel.setLayout(new GridBagLayout());
        JLabel titleLabel = new JLabel("FLIPPING TILES");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 48)); // Increase font size
        titleLabel.setForeground(Color.WHITE); // Set the text color to white for better contrast
        titlePanel.add(titleLabel, new GridBagConstraints());
        titlePanel.setBounds(0, 150, 600, 100); // Lower the title panel slightly
        layeredPane.add(titlePanel, JLayeredPane.PALETTE_LAYER);

        // Create a panel for the button
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false); // Make the button panel transparent
        buttonPanel.setLayout(new FlowLayout());
        JButton startButton = new JButton("Start Game");
        startButton.setFont(new Font("Arial", Font.PLAIN, 24)); // Increase button font size
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // Close the welcome screen
                new MemoryGame(); // Open the memory game window
            }
        });
        buttonPanel.add(startButton);
        buttonPanel.setBounds(0, 300, 600, 50);
        layeredPane.add(buttonPanel, JLayeredPane.PALETTE_LAYER);

        // Add the layered pane to the frame
        add(layeredPane);

        pack(); // Adjust the frame size to fit the components
        setLocationRelativeTo(null); // Center the frame
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(WelcomeScreen::new);
    }
}