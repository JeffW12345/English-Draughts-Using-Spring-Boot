package com.github.jeffw12345.draughts.client.view;

import com.github.jeffw12345.draughts.client.controller.DrawController;
import com.github.jeffw12345.draughts.client.controller.MasterClientController;
import com.github.jeffw12345.draughts.client.controller.WinLossController;
import com.github.jeffw12345.draughts.models.game.Board;
import com.github.jeffw12345.draughts.models.game.SquareContent;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

@Slf4j
@Getter
public class DraughtsBoardGui implements ActionListener {

    static {
        System.setProperty("java.awt.headless", "false");
    }
    private JFrame frame;
    private JPanel leftPanel, rightPanel, userInfoPanel;
    private JButton offerNewGameButton, offerDrawButton, acceptDrawButton, resignButton;
    private final JButton[][] square = new JButton[8][8];
    private final Font messagesFont = new Font("Aerial", Font.BOLD, 14);
    private JLabel topMessageLabel, middleMessageLabel, bottomMessageLabel;
    private String bottomLineMessageText, middleLineMessageText, topLineMessageText;
    private final MasterClientController controller;
    private final DrawController drawController;
    private final WinLossController winLossController;

    public DraughtsBoardGui(MasterClientController controller) {
        this.controller = controller;
        this.drawController = controller.getDrawController();
        this.winLossController = controller.getWinLossController();
    }

    public void setUp() {
        newBoardActions();
        controller.getGuiMessageController().setWelcomeMessage();
        labelSetup();
    }

    void newBoardActions() {
        createAndConfigureFrame();

        createAndConfigureLeftPanel();
        createAndConfigureRightPanel();

        createEmptyBoard();
        addRedMenToBoardForInitialSetup();
        addWhiteMenToBoardForInitialSetup();

        createAndConfigureButtons();

        frame.setVisible(true);
    }

    private void createAndConfigureFrame() {
        makeGuiDisplayProperlyOnWindowsAndMacs();

        System.out.println("Second check: " + java.awt.GraphicsEnvironment.isHeadless()); // Prints 'true' TODO - Delete

        frame = new JFrame("English Draughts Game");
        frame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        frame.setSize(1000, 500);
        frame.setLayout(new GridLayout(0, 2));
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                controller.thisClientGuiCloseActions();
            }
        });
    }


    private static void makeGuiDisplayProperlyOnWindowsAndMacs() {
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            log.error(e.getMessage());
        }
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
                        drawController.offerDrawButtonPressedActions();
                    } else if (e.getSource() == acceptDrawButton) {
                        drawController.acceptDrawButtonPressed();
                    } else if (e.getSource() == resignButton) {
                        winLossController.resignButtonPressed();
                    }
                    return null;
                }
            };
            buttonActionWorker.execute();
        } else {
            SwingWorker<Void, Void> squareClickWorker = new SwingWorker<>() {
                @Override
                protected Void doInBackground() {
                    for (int row = 0; row < 8; row++) {
                        for (int col = 0; col < 8; col++) {
                            if (e.getSource() == square[row][col]) {
                                controller.boardSquareClicked(row, col);
                            }
                        }
                    }
                    return null;
                }
            };
            squareClickWorker.execute();
        }
    }

    public void addRedKingToSquare(int row, int col) {
        GuiSquare.getComponent(square[row][col]).setState(GuiSquareState.RED_KING);
    }

    public void addRedManToSquare(int row, int col) {
        GuiSquare.getComponent(square[row][col]).setState(GuiSquareState.RED_MAN);
    }

    void addRedMenToBoardForInitialSetup() {
        for (int row = 0; row < 3; row++) {
            for (int col = 1; col < 8; col += 2) {
                if ((row == 0) || (row == 2)) {
                    GuiSquare.getComponent(square[row][col]).setState(GuiSquareState.RED_MAN);
                }
            }

            GuiSquare.getComponent(square[row][0]).setState(GuiSquareState.RED_MAN);
            GuiSquare.getComponent(square[row][2]).setState(GuiSquareState.RED_MAN);
            GuiSquare.getComponent(square[row][4]).setState(GuiSquareState.RED_MAN);
            GuiSquare.getComponent(square[row][6]).setState(GuiSquareState.RED_MAN);
        }
    }

    public void addWhiteKing(int row, int col) {
        GuiSquare.getComponent(square[row][col]).setState(GuiSquareState.WHITE_KING);
    }

    public void addWhiteMan(int row, int col) {
        GuiSquare.getComponent(square[row][col]).setState(GuiSquareState.WHITE_MAN);
    }

    void addWhiteMenToBoardForInitialSetup() {
        for (int row = 5; row < 8; row++) {
            for (int col = 0; col < 8; col += 2) {
                if ((row == 5) || (row == 7)) {
                    GuiSquare.getComponent(square[row][col]).setState(GuiSquareState.WHITE_MAN);
                }
            }

            GuiSquare.getComponent(square[row][0]).setState(GuiSquareState.WHITE_MAN);
            GuiSquare.getComponent(square[row][2]).setState(GuiSquareState.WHITE_MAN);
            GuiSquare.getComponent(square[row][4]).setState(GuiSquareState.WHITE_MAN);
            GuiSquare.getComponent(square[row][6]).setState(GuiSquareState.WHITE_MAN);
        }
    }


    public void repaintBoard(Board board) {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                SquareContent squareContent = board.getSquareContentAtRowAndColumn(row, col);
                if (squareContent == SquareContent.RED_MAN) {
                    addRedManToSquare(row, col);
                }
                if (squareContent == SquareContent.WHITE_MAN) {
                    addWhiteMan(row, col);
                }
                if (squareContent == SquareContent.RED_KING) {
                    addRedKingToSquare(row, col);
                }
                if (squareContent == SquareContent.WHITE_KING) {
                    addWhiteKing(row, col);
                }
                if (squareContent == SquareContent.EMPTY) {
                    setSquareBlank(row, col);
                }
            }
        }
        frame.repaint();
        frame.setVisible(true);
    }

    public void setSquareBlank(int row, int col) {
        GuiSquare.getComponent(square[row][col]).setState(GuiSquareState.EMPTY);
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

    private void createAndConfigureButtons() {
        createAndConfigureNewGameButton();
        createAndConfigureOfferDrawButton();
        createAndConfigureAcceptDrawButton();
        createAndConfigureResignButton();
        createAndConfigureInfoPanel();
        createAndConfigureOfferNewGameButton();
    }

    void labelSetup() {
        bottomMessageLabel = new JLabel();
        bottomMessageLabel.setFont(messagesFont);
        bottomMessageLabel.setText(topLineMessageText);
        userInfoPanel.add(bottomMessageLabel);

        middleMessageLabel = new JLabel();
        middleMessageLabel.setFont(messagesFont);
        middleMessageLabel.setText(middleLineMessageText);
        userInfoPanel.add(middleMessageLabel);

        topMessageLabel = new JLabel();
        topMessageLabel.setFont(messagesFont);
        topMessageLabel.setText(bottomLineMessageText);
        userInfoPanel.add(topMessageLabel);
    }

    private void createAndConfigureAcceptDrawButton() {
        acceptDrawButton = new JButton("Accept draw");
        acceptDrawButton.setFont(new Font("Arial", Font.BOLD, 18));
        rightPanel.add(acceptDrawButton);
        acceptDrawButton.setEnabled(false);
        acceptDrawButton.addActionListener(this);
    }


    private void createEmptyBoard() {
        leftPanel.setLayout(new GridLayout(8, 8));
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                square[row][col] = new JButton();
                square[row][col].addActionListener(this);
                leftPanel.add(square[row][col]);
                if (((row + col) % 2) != 0) {
                    square[row][col].setBackground(Color.BLACK);
                    square[row][col].setOpaque(true);
                } else {
                    square[row][col].setBackground(Color.WHITE);
                    square[row][col].setOpaque(true);
                }
                square[row][col].add(new GuiSquare(GuiSquareState.EMPTY));
            }
        }
    }


    private void createAndConfigureLeftPanel() {
        leftPanel = new JPanel();
        leftPanel.setBackground(Color.BLACK);
        frame.add(leftPanel);
    }

    private void createAndConfigureOfferDrawButton() {
        offerDrawButton = new JButton("Offer draw");
        offerDrawButton.setFont(new Font("Arial", Font.BOLD, 18));
        rightPanel.add(offerDrawButton);
        offerDrawButton.setEnabled(false);
        offerDrawButton.addActionListener(this);
    }

    private void createAndConfigureNewGameButton() {
        offerNewGameButton = new JButton("Offer new game");
        offerNewGameButton.setFont(new Font("Arial", Font.BOLD, 18));
        rightPanel.add(offerNewGameButton);
        offerNewGameButton.addActionListener(this);
    }

    private void createAndConfigureResignButton() {
        resignButton = new JButton("Resign");
        resignButton.setFont(new java.awt.Font("Arial", Font.BOLD, 18));
        rightPanel.add(resignButton);
        resignButton.setEnabled(false);
        resignButton.addActionListener(this);
    }

    private void createAndConfigureRightPanel() {
        rightPanel = new JPanel();
        rightPanel.setBackground(Color.decode("#FFFFCC"));
        frame.add(rightPanel);
        rightPanel.setLayout(new GridLayout(3, 0, 0, 20));
        rightPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
    }

    private void createAndConfigureInfoPanel() {
        userInfoPanel = new JPanel();
        userInfoPanel.setBackground(Color.WHITE);
        userInfoPanel.setLayout(new GridLayout(4, 0, 0, 20));
        rightPanel.add(userInfoPanel);
    }

    private void createAndConfigureOfferNewGameButton() {
        JButton offerNewGameButton = new JButton("Offer new game");
        offerNewGameButton.setFont(new java.awt.Font("Arial", Font.BOLD, 18));
        rightPanel.add(offerNewGameButton);
        offerNewGameButton.addActionListener(this);
    }
}
