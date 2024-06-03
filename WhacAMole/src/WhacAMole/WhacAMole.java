package WhacAMole;

import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import javax.swing.*;

public class WhacAMole {
    int boardWidth = 600;
    int boardHeight = 700; // Increased height to accommodate the restart button

    JFrame frame = new JFrame("Mario: Whac A Mole");
    JLabel textLabel = new JLabel();
    JLabel highScoreLabel = new JLabel();
    JPanel textPanel = new JPanel();
    JPanel boardPanel = new JPanel(); 
    JPanel bottomPanel = new JPanel();

    JButton[] board = new JButton[9];
    ImageIcon moleIcon;
    ImageIcon plantIcon;

    JButton currMoleTile;
    JButton[] currPlantTiles = new JButton[2]; // Array to hold multiple plants

    Random random = new Random();
    Timer setMoleTimer;
    Timer setPlantTimer;
    int score = 0;
    int highScore = 0;

    WhacAMole() {
        frame.setSize(boardWidth, boardHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        textLabel.setFont(new Font("Arial", Font.PLAIN, 50));
        textLabel.setHorizontalAlignment(JLabel.CENTER);
        textLabel.setText("Score: " + score);
        textLabel.setOpaque(true);

        highScoreLabel.setFont(new Font("Arial", Font.PLAIN, 30));
        highScoreLabel.setHorizontalAlignment(JLabel.CENTER);
        highScoreLabel.setText("High Score: " + highScore);
        highScoreLabel.setOpaque(true);

        textPanel.setLayout(new BorderLayout());
        textPanel.add(textLabel, BorderLayout.NORTH);
        textPanel.add(highScoreLabel, BorderLayout.SOUTH);
        frame.add(textPanel, BorderLayout.NORTH);

        boardPanel.setLayout(new GridLayout(3, 3));
        frame.add(boardPanel);

        Image plantImg = new ImageIcon(getClass().getResource("piranha.png")).getImage();
        plantIcon = new ImageIcon(plantImg.getScaledInstance(150, 150, java.awt.Image.SCALE_SMOOTH));
        
        Image moleImg = new ImageIcon(getClass().getResource("monty.png")).getImage();
        moleIcon = new ImageIcon(moleImg.getScaledInstance(150, 150, java.awt.Image.SCALE_SMOOTH));
        
        for (int i = 0; i < 9; i++) {
            JButton tile = new JButton();
            board[i] = tile;
            boardPanel.add(tile);
            tile.setFocusable(false);

            tile.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    JButton tile = (JButton) e.getSource();
                    if (tile == currMoleTile) {
                        score += 10;
                        textLabel.setText("Score: " + score);
                    } else {
                        for (JButton plantTile : currPlantTiles) {
                            if (tile == plantTile) {
                                gameOver();
                                return;
                            }
                        }
                    }
                }
            });
        }

        JButton restartButton = new JButton("Restart");
        restartButton.setFont(new Font("Arial", Font.PLAIN, 30));
        restartButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                restartGame();
            }
        });
        bottomPanel.add(restartButton);
        frame.add(bottomPanel, BorderLayout.SOUTH);

        setMoleTimer = new Timer(1000, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (currMoleTile != null) {
                    currMoleTile.setIcon(null);
                    currMoleTile = null;
                }
                int num = random.nextInt(9);
                JButton tile = board[num];
                for (JButton plantTile : currPlantTiles) {
                    if (tile == plantTile) return;
                }
                currMoleTile = tile;
                currMoleTile.setIcon(moleIcon);
            }
        });

        setPlantTimer = new Timer(1500, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                for (int i = 0; i < currPlantTiles.length; i++) {
                    if (currPlantTiles[i] != null) {
                        currPlantTiles[i].setIcon(null);
                        currPlantTiles[i] = null;
                    }
                }
                for (int i = 0; i < currPlantTiles.length; i++) {
                    int num = random.nextInt(9);
                    JButton tile = board[num];
                    if (currMoleTile == tile) return;
                    currPlantTiles[i] = tile;
                    currPlantTiles[i].setIcon(plantIcon);
                }
            }
        });

        setMoleTimer.start();
        setPlantTimer.start();
        frame.setVisible(true);
    }

    private void gameOver() {
        if (score > highScore) {
            highScore = score;
            highScoreLabel.setText("High Score: " + highScore);
        }
        textLabel.setText("Game Over: " + score);
        setMoleTimer.stop();
        setPlantTimer.stop();
        for (int i = 0; i < 9; i++) {
            board[i].setEnabled(false);
        }
    }

    private void restartGame() {
        score = 0;
        textLabel.setText("Score: " + score);
        for (int i = 0; i < 9; i++) {
            board[i].setEnabled(true);
            board[i].setIcon(null);
        }
        currMoleTile = null;
        for (int i = 0; i < currPlantTiles.length; i++) {
            currPlantTiles[i] = null;
        }
        setMoleTimer.start();
        setPlantTimer.start();
    }

    public static void main(String[] args) {
        new WhacAMole();
    }
}
