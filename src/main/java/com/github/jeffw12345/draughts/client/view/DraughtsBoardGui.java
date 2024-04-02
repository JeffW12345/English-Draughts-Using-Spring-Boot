package com.github.jeffw12345.draughts.client.view;

import com.github.jeffw12345.draughts.client.controller.MasterClientController;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

@Slf4j
//TODO- Explore adding other classes
@Getter
public class DraughtsBoardGui implements ActionListener {
    private JFrame frame;
    private JPanel leftPanel, rightPanel, userInfoPanel;
    private JButton offerNewGameButton, offerDrawButton, acceptDrawButton, resignButton;
    private final JButton[][] square = new JButton[8][8];
    private final Font messagesFont = new Font("Aerial", Font.BOLD, 14);
    private JLabel topMessageLabel, middleMessageLabel, bottomMessageLabel;
    private String bottomLineMessageText, middleLineMessageText, topLineMessageText;
    private final MasterClientController controller;

    public DraughtsBoardGui(MasterClientController controller) {
        this.controller = controller;
    }

    public void setUp() {
        newBoardActions();
        controller.setWelcomeMessage();
        messagesToPlayer();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == offerNewGameButton || e.getSource() == offerDrawButton ||
                e.getSource() == acceptDrawButton || e.getSource() == resignButton) {
            SwingWorker<Void, Void> buttonActionWorker = new SwingWorker<>() {
                @Override
                protected Void doInBackground() {
                    if (e.getSource() == offerNewGameButton) {
                        controller.offerNewGameButtonPressed();
                    } else if (e.getSource() == offerDrawButton) {
                        controller.offerDrawButtonPressed();
                    } else if (e.getSource() == acceptDrawButton) {
                        controller.acceptDrawButtonPressed();
                    } else if (e.getSource() == resignButton) {
                        controller.resignButtonPressed();
                    }
                    return null;
                }
            };
            buttonActionWorker.execute();
        } else {
            SwingWorker<Void, Void> squareClickWorker = new SwingWorker<>() {
                @Override
                protected Void doInBackground() {
                    for (int column = 0; column < 8; column++) {
                        for (int row = 0; row < 8; row++) {
                            if (e.getSource() == square[column][row]) {
                                controller.boardSquareClicked(column, row);
                            }
                        }
                    }
                    return null;
                }
            };
            squareClickWorker.execute();
        }
    }

    public void addRedKing(int col, int row) {
        Square.getComponent(square[col][row]).setState(SquareState.RED_KING);
    }
    public void addRedMan(int col, int row) {
        Square.getComponent(square[col][row]).setState(SquareState.RED_MAN);
    }

    void addRedMenToBoardForInitialSetup() {
        for (int row = 0; row < 3; row++) {
            for (int column = 1; column < 8; column += 2) {
                if ((row == 0) || (row == 2)) {
                    Square.getComponent(square[column][row]).setState(SquareState.RED_MAN);
                }
            }

            ((Square) (square[0][1].getComponents()[0])).setState(SquareState.RED_MAN);
            ((Square) (square[2][1].getComponents()[0])).setState(SquareState.RED_MAN);
            ((Square) (square[4][1].getComponents()[0])).setState(SquareState.RED_MAN);
            ((Square) (square[6][1].getComponents()[0])).setState(SquareState.RED_MAN);
        }
    }

    public void addWhiteKing(int col, int row) {
        Square.getComponent(square[col][row]).setState(SquareState.WHITE_KING);
    }

    public void addWhiteMan(int col, int row) {
        Square.getComponent(square[col][row]).setState(SquareState.WHITE_MAN);
    }

    void addWhiteMenToBoardForInitialSetup() {
        for (int row = 5; row < 8; row++) {
            for (int column = 0; column < 8; column += 2) {
                if ((row == 5) || (row == 7)) {
                    Square.getComponent(square[column][row]).setState(SquareState.WHITE_MAN);
                }
            }

            Square.getComponent(square[1][6]).setState(SquareState.WHITE_MAN);
            ((Square) (square[3][6].getComponents()[0])).setState(SquareState.WHITE_MAN);
            ((Square) (square[5][6].getComponents()[0])).setState(SquareState.WHITE_MAN);
            ((Square) (square[7][6].getComponents()[0])).setState(SquareState.WHITE_MAN);
        }
    }

    JButton createAcceptDrawBtn() {
        acceptDrawButton = new JButton("Accept draw");
        acceptDrawButton.setFont(new java.awt.Font("Arial", Font.BOLD, 18));
        rightPanel.add(acceptDrawButton);
        acceptDrawButton.setEnabled(false);
        acceptDrawButton.addActionListener(this);
        return acceptDrawButton;
    }


    void createEmptyBoard() {
        leftPanel.setLayout(new GridLayout(8, 8));
        for (int row = 0; row < 8; row++) {
            for (int column = 0; column < 8; column++) {
                square[column][row] = new JButton();
                square[column][row].addActionListener(this);
                leftPanel.add(square[column][row]);
                if (((row + column) % 2) != 0) {
                    square[column][row].setBackground(Color.BLACK);
                    square[column][row].setOpaque(true);
                } else {
                    square[column][row].setBackground(Color.WHITE);
                    square[column][row].setOpaque(true);
                }
                square[column][row].add(new Square(SquareState.EMPTY));
            }
        }
    }

    JFrame createFrame() {
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        frame = new JFrame("English Draughts Game");
        frame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        frame.setSize(1000, 500);
        frame.setLayout(new GridLayout(0, 2));
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                //TODO - Add method to send a message to the server.
                log.warn("Exiting as a player has closed their window");
                System.exit(1);
            }
        });
        return frame;
    }

    JPanel createLeftPanel() {
        leftPanel = new JPanel();
        leftPanel.setBackground(Color.BLACK);
        frame.add(leftPanel);
        return leftPanel;
    }

    JButton createOfferDrawBtn() {
        offerDrawButton = new JButton("Offer draw");
        offerDrawButton.setFont(new java.awt.Font("Arial", Font.BOLD, 18));
        rightPanel.add(offerDrawButton);
        offerDrawButton.setEnabled(false);
        offerDrawButton.addActionListener(this);
        return offerDrawButton;
    }

    JButton createOfferNewGameBtn() {
        JButton offerNewGameButton = new JButton("Offer new game");
        offerNewGameButton.setFont(new java.awt.Font("Arial", Font.BOLD, 18));
        rightPanel.add(offerNewGameButton);
        offerNewGameButton.addActionListener(this);
        return offerNewGameButton;
    }

    JButton createResignButton() {
        resignButton = new JButton("Resign");
        resignButton.setFont(new java.awt.Font("Arial", Font.BOLD, 18));
        rightPanel.add(resignButton);
        resignButton.setEnabled(false);
        resignButton.addActionListener(this);
        return resignButton;
    }

    JPanel createRightPanel() {
        rightPanel = new JPanel();
        rightPanel.setBackground(Color.decode("#FFFFCC"));
        frame.add(rightPanel);
        rightPanel.setLayout(new GridLayout(3, 0, 0, 20));
        rightPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        return rightPanel;
    }

    JPanel createUserInfoPanel() {
        userInfoPanel = new JPanel();
        userInfoPanel.setBackground(Color.WHITE);
        userInfoPanel.setLayout(new GridLayout(4, 0, 0, 20));
        rightPanel.add(userInfoPanel);
        return userInfoPanel;
    }

    void newBoardActions() {
        frame = createFrame();
        leftPanel = createLeftPanel();
        rightPanel = createRightPanel();
        createEmptyBoard();
        addRedMenToBoardForInitialSetup();
        addWhiteMenToBoardForInitialSetup();
        offerNewGameButton = createOfferNewGameBtn();
        offerDrawButton = createOfferDrawBtn();
        acceptDrawButton = createAcceptDrawBtn();
        resignButton = createResignButton();
        userInfoPanel = createUserInfoPanel();
        frame.setVisible(true);
    }

    void messagesToPlayer() {
        topMessageLabel = new JLabel();
        topMessageLabel.setFont(messagesFont);
        middleMessageLabel = new JLabel();
        middleMessageLabel.setFont(messagesFont);
        bottomMessageLabel = new JLabel();
        bottomMessageLabel.setFont(messagesFont);
        userInfoPanel.add(topMessageLabel);
        userInfoPanel.add(middleMessageLabel);
        userInfoPanel.add(bottomMessageLabel);
        topMessageLabel.setText(bottomLineMessageText);
        middleMessageLabel.setText(middleLineMessageText);
        bottomMessageLabel.setText(topLineMessageText);
    }

    public void setBlank(int col, int row) {
        Square.getComponent(square[col][row]).setState(SquareState.EMPTY);
    }

    public void updateLabels() {
        topMessageLabel.setText(topLineMessageText);
        middleMessageLabel.setText(middleLineMessageText);
        bottomMessageLabel.setText(bottomLineMessageText);
    }

    public void setBottomLineMessageText(String bottomLineMessageText) {
        this.bottomLineMessageText = bottomLineMessageText;
    }

    public void setMiddleLineMessageText(String middleLineMessageText) {
        this.middleLineMessageText = middleLineMessageText;
    }
    public void setTopLineMessageText(String topLineMessageText) {
        this.topLineMessageText = topLineMessageText;
    }
}