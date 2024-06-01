import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MemoryGame extends JFrame implements ActionListener {
    private int size; // Variable to store the grid size
    private final int[] POSSIBLE_SIZES = {2, 4, 6}; // Possible grid sizes
    private final int[] POSSIBLE_PAIRS = {2, 8, 18}; // Corresponding pairs for each size

    private JButton[] buttons;
    private int[] cardValues;
    private int openCards = 0;
    private int firstCardIndex = -1;
    private int secondCardIndex = -1;
    private int matchedPairs = 0;
    private int chances = 0;
    private JLabel chancesLabel;
    private Timer gameTimer;
    private int secondsPassed = 0;
    private JLabel timerLabel;
    private Color buttonColor = Color.BLUE;

    public MemoryGame() {
        setTitle("Memory Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        initializeMenu();
        chooseGridSize(); // Prompt the user to choose the grid size
        initializeGame();
        pack(); // Adjust frame size to fit components
        setLocationRelativeTo(null); // Center the frame
        setVisible(true);
    }

    // Method to prompt the user to choose the grid size
    private void chooseGridSize() {
        // Create a dialog box with options for grid size
        String[] options = new String[POSSIBLE_SIZES.length];
        for (int i = 0; i < POSSIBLE_SIZES.length; i++) {
            options[i] = POSSIBLE_SIZES[i] + "x" + POSSIBLE_SIZES[i];
        }
        String selectedOption = (String) JOptionPane.showInputDialog(this, "Choose grid size:", "Grid Size",
                JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
        if (selectedOption != null) {
            size = Integer.parseInt(selectedOption.substring(0, 1));
        } else {
            System.exit(0); // Exit if the user cancels
        }
    }

    private void initializeGame() {
        int totalCards = size * size;
        int pairs = getPairsForSize(size); // Get the corresponding number of pairs

        setSize(100 * size, 120 * size); // Adjust frame size based on grid size
        setLayout(new GridLayout(size + 2, size)); // Adjust layout based on grid size

        buttons = new JButton[totalCards];
        cardValues = new int[totalCards];
        List<Integer> values = new ArrayList<>();

        for (int i = 0; i < pairs; i++) {
            values.add(i);
            values.add(i);
        }

        Collections.shuffle(values);

        Font largeFont = new Font("Arial", Font.BOLD, 24);

        for (int i = 0; i < totalCards; i++) {
            buttons[i] = new JButton();
            buttons[i].setOpaque(true);
            buttons[i].setBackground(buttonColor);
            buttons[i].addActionListener(this);
            buttons[i].setFont(largeFont);
            buttons[i].setBorder(new LineBorder(Color.BLACK));  // Add border to each button
            add(buttons[i]);
            cardValues[i] = values.get(i);
        }

        chancesLabel = new JLabel("Chances: 0", SwingConstants.CENTER);
        chancesLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        add(chancesLabel);

        timerLabel = new JLabel("Time: 0s", SwingConstants.CENTER);
        timerLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        add(timerLabel);

        gameTimer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                secondsPassed++;
                timerLabel.setText("Time: " + secondsPassed + "s");
            }
        });
        gameTimer.start();
    }

    private int getPairsForSize(int size) {
        for (int i = 0; i < POSSIBLE_SIZES.length; i++) {
            if (POSSIBLE_SIZES[i] == size) {
                return POSSIBLE_PAIRS[i];
            }
        }
        return 0; // Return 0 if size is not found (shouldn't happen)
    }

    private void initializeMenu() {
        JMenuBar menuBar = new JMenuBar();
        JMenu optionsMenu = new JMenu("Options");

        JMenu difficultyMenu = new JMenu("Difficulty");

        for (int i = 0; i < POSSIBLE_SIZES.length; i++) {
    		final int gridSize = POSSIBLE_SIZES[i];
    		JMenuItem sizeMenuItem = new JMenuItem(gridSize + "x" + gridSize);
    		sizeMenuItem.addActionListener(e -> {
        	size = gridSize;
        	restartGame();
    		});
    	difficultyMenu.add(sizeMenuItem);
	}


        optionsMenu.add(difficultyMenu);

        JMenuItem buttonColorMenuItem = new JMenuItem("Choose Button Color");
        buttonColorMenuItem.addActionListener(e -> {
            Color selectedColor = JColorChooser.showDialog(null, "Choose Button Color", buttonColor);
            if (selectedColor != null) {
                buttonColor = selectedColor;
                for (JButton button : buttons) {
                    button.setBackground(buttonColor);
                }
            }
        });
        optionsMenu.add(buttonColorMenuItem);

        JMenuItem backgroundColorMenuItem = new JMenuItem("Choose Background Color");
        backgroundColorMenuItem.addActionListener(e -> {
            Color selectedColor = JColorChooser.showDialog(null, "Choose Background Color", getBackground());
            if (selectedColor != null) {
                getContentPane().setBackground(selectedColor);
            }
        });
        optionsMenu.add(backgroundColorMenuItem);

        menuBar.add(optionsMenu);
        setJMenuBar(menuBar);
    }

    private void restartGame() {
        gameTimer.stop(); // Stop the game timer
        for (JButton button : buttons) {
            remove(button); // Remove all buttons from the frame
        }
        initializeGame(); // Reinitialize the game with the new size
        pack(); // Adjust frame size
        setLocationRelativeTo(null); // Center the frame
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton clickedButton = (JButton) e.getSource();

        // Find the index of the clicked button
        int clickedIndex = -1;
        for (int i = 0; i < buttons.length; i++) {
            if (clickedButton == buttons[i]) {
                clickedIndex = i;
                break;
            }
        }

        // Ignore clicks on already matched cards or cards already open
        if (cardValues[clickedIndex] == -1 || clickedButton.getText().equals("*")) {
            return;
        }

        // Show the value of the clicked card
        clickedButton.setText(Integer.toString(cardValues[clickedIndex]));

        // If it's the first card being opened
        if (openCards == 0) {
            firstCardIndex = clickedIndex;
            openCards++;
        }
        // If it's the second card being opened
        else if (openCards == 1) {
            secondCardIndex = clickedIndex;
            chances++; // Increment chances counter
            chancesLabel.setText("Chances: " + chances); // Update chances label

            // Check if the cards match
            if (cardValues[firstCardIndex] == cardValues[secondCardIndex]) {
                matchedPairs++; // Increment matched pairs counter
                cardValues[firstCardIndex] = -1; // Mark the cards as matched
                cardValues[secondCardIndex] = -1;

                // Check if all pairs are matched
                if (matchedPairs == cardValues.length / 2) {
                    gameTimer.stop(); // Stop the game timer
                    JOptionPane.showMessageDialog(this, "Congratulations! You've matched all pairs in " + chances + " chances and " + secondsPassed + " seconds!");
                    resetGame(); // Reset the game
                }
            } else {
                // If the cards don't match, flip them back after 1 second
                Timer flipBackTimer = new Timer(1000, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        buttons[firstCardIndex].setText("");
                        buttons[secondCardIndex].setText("");
                    }
                });
                flipBackTimer.setRepeats(false);
                flipBackTimer.start();
            }

            openCards = 0; // Reset the number of open cards
        }
    }

    private void resetGame() {
        // Reset all game variables and labels
        openCards = 0;
        firstCardIndex = -1;
        secondCardIndex = -1;
        matchedPairs = 0;
        chances = 0;
        secondsPassed = 0;
        chancesLabel.setText("Chances: 0");
        timerLabel.setText("Time: 0s");

        // Reset card values and shuffle them
        List<Integer> values = new ArrayList<>();
        for (int i = 0; i < size * size / 2; i++) {
            values.add(i);
            values.add(i);
        }
        Collections.shuffle(values);
        for (int i = 0; i < cardValues.length; i++) {
            cardValues[i] = values.get(i);
        }

        // Reset button texts
        for (JButton button : buttons) {
            button.setText("");
        }

        // Restart the game timer
        gameTimer.start();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MemoryGame::new);
    }
}