package com.github.jeffw12345.draughts.client.view;

import com.github.jeffw12345.draughts.client.controller.DrawController;
import com.github.jeffw12345.draughts.client.controller.MasterClientController;
import com.github.jeffw12345.draughts.client.controller.WinLossController;
import com.github.jeffw12345.draughts.models.game.Board;
import com.github.jeffw12345.draughts.models.game.SquareContent;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import static com.github.jeffw12345.draughts.client.view.ViewUtils.createJLabel;
import static com.github.jeffw12345.draughts.client.view.ViewUtils.makeGuiDisplayProperlyOnWindowsAndMacs;

@Slf4j
@Getter
public class DraughtsBoardGui implements ActionListener {

    static {
        System.setProperty("java.awt.headless", "false");
    }
    private JFrame frame;
    private JPanel leftPanel, rightPanel, userInfoPanel;
    private JButton offerNewGameButton, offerDrawButton, acceptDrawButton, resignButton;
    private final JButton[][] grid = new JButton[8][8];
    private JLabel topMessageLabel, middleMessageLabel, bottomMessageLabel;
    @Setter
    private String bottomLineMessageText, middleLineMessageText, topLineMessageText;
    private final MasterClientController controller;
    private final DrawController drawController;
    private final WinLossController winLossController;
    private boolean clientIdProvided, colourAssigned;

    public DraughtsBoardGui(MasterClientController controller) {
        this.controller = controller;
        this.drawController = controller.getDrawController();
        this.winLossController = controller.getWinLossController();
    }

    public void setUp() {
        newBoardActions();
        labelSetup();
        if (!colourAssigned){
            controller.getGuiMessageController().setWelcomeMessageWithoutColours();
        } else {
            controller.getGuiMessageController().setWelcomeMessageWithColours();
        }
    }

    void newBoardActions() {
        createAndConfigureFrame();

        createAndConfigureLeftPanel();
        createAndConfigureRightPanel();

        createEmptyBoard();
        addRedMenToBoardForInitialSetup();
        addWhiteMenToBoardForInitialSetup();

        createAndConfigureButtons();

        createAndConfigureInfoPanel();
        addEmptyPanel();

        frame.setVisible(true);
    }

    private void createAndConfigureLeftPanel() {
        leftPanel = new JPanel();
        leftPanel.setBackground(Color.BLACK);
        frame.add(leftPanel);
    }

    private void createAndConfigureRightPanel() {
        rightPanel = new JPanel();

        rightPanel.setBackground(Color.decode("#FFFFCC"));
        rightPanel.setLayout(new GridLayout(3, 0, 0, 20));
        rightPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        frame.add(rightPanel);
    }

    private void createAndConfigureButtons() {
        offerNewGameButton = ViewUtils.createButton("Offer new game", false, this);
        rightPanel.add(offerNewGameButton);

        offerDrawButton = ViewUtils.createButton("Offer draw", false, this);
        rightPanel.add(offerDrawButton);

        resignButton = ViewUtils.createButton("Resign", false, this);
        rightPanel.add(resignButton);

        acceptDrawButton = ViewUtils.createButton("Accept draw", false, this);
        rightPanel.add(acceptDrawButton);
    }

    private void createAndConfigureFrame() {
        makeGuiDisplayProperlyOnWindowsAndMacs();

        frame = new JFrame("English Draughts Game");
        frame.setSize(1000, 500);
        frame.setLayout(new GridLayout(0, 2));
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                log.warn("Closing app due to window close");
                System.exit(1);
                //controller.thisClientGuiCloseActions(); TODO - Reinstate when fixed
            }
        });
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
                            if (e.getSource() == grid[row][col]) {
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
        GuiSquare.getComponent(grid[row][col]).setState(GuiSquareState.RED_KING);
    }

    public void addRedManToSquare(int row, int col) {
        GuiSquare.getComponent(grid[row][col]).setState(GuiSquareState.RED_MAN);
    }

    public void addWhiteKing(int row, int col) {
        GuiSquare.getComponent(grid[row][col]).setState(GuiSquareState.WHITE_KING);
    }

    public void addWhiteMan(int row, int col) {
        GuiSquare.getComponent(grid[row][col]).setState(GuiSquareState.WHITE_MAN);
    }

    void addWhiteMenToBoardForInitialSetup() {
        for (int row = 5; row < 8; row++) {
            for (int column = 1; column < 8; column += 2) {
                if (row == 5 || row == 7) {
                    GuiSquare.getComponent(grid[row][column]).setState(GuiSquareState.WHITE_MAN);
                }
            }
            GuiSquare.getComponent(grid[6][0]).setState(GuiSquareState.WHITE_MAN);
            GuiSquare.getComponent(grid[6][2]).setState(GuiSquareState.WHITE_MAN);
            GuiSquare.getComponent(grid[6][4]).setState(GuiSquareState.WHITE_MAN);
            GuiSquare.getComponent(grid[6][6]).setState(GuiSquareState.WHITE_MAN);

        }
    }

    void addRedMenToBoardForInitialSetup() {
        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < 8; column += 2) {
                if ((row == 0) || (row == 2)) {
                    GuiSquare.getComponent(grid[row][column]).setState(GuiSquareState.RED_MAN);
                }
            }

            GuiSquare.getComponent(grid[1][1]).setState(GuiSquareState.RED_MAN);
            GuiSquare.getComponent(grid[1][3]).setState(GuiSquareState.RED_MAN);
            GuiSquare.getComponent(grid[1][5]).setState(GuiSquareState.RED_MAN);
            GuiSquare.getComponent(grid[1][7]).setState(GuiSquareState.RED_MAN);
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
        GuiSquare.getComponent(grid[row][col]).setState(GuiSquareState.EMPTY);
    }

    public void updateLabels() {
        topMessageLabel.setText(topLineMessageText);
        middleMessageLabel.setText(middleLineMessageText);
        bottomMessageLabel.setText(bottomLineMessageText);
    }
    void labelSetup() {
        Font messagesFont = new Font("Aerial", Font.BOLD, 14);
        topMessageLabel = createJLabel(messagesFont, topLineMessageText);
        userInfoPanel.add(topMessageLabel);

        middleMessageLabel = createJLabel(messagesFont, middleLineMessageText);
        userInfoPanel.add(middleMessageLabel);

        bottomMessageLabel = createJLabel(messagesFont, bottomLineMessageText);
        userInfoPanel.add(bottomMessageLabel);
    }

    private void createEmptyBoard() {
        leftPanel.setLayout(new GridLayout(8, 8));
        for (int row = 7; row > -1; row--) {
            for (int col = 0; col < 8; col++) {
                grid[row][col] = new JButton();
                grid[row][col].addActionListener(this);
                leftPanel.add(grid[row][col]);
                if (((row + col) % 2) == 0) {
                    grid[row][col].setBackground(Color.BLACK);
                    grid[row][col].setOpaque(true);
                } else {
                    grid[row][col].setBackground(Color.WHITE);
                    grid[row][col].setOpaque(true);
                }
                grid[row][col].add(new GuiSquare(GuiSquareState.EMPTY));
            }
        }
    }

    private void createAndConfigureInfoPanel() {
        userInfoPanel = new JPanel();
        userInfoPanel.setBackground(Color.YELLOW);
        userInfoPanel.setLayout(new GridLayout(4, 0, 0, 20));
        rightPanel.add(userInfoPanel);;
    }

    private void addEmptyPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(Color.YELLOW);
        rightPanel.add(panel);;
    }

    public void clientIdProvidedActions() {
        clientIdProvided = true;
        offerNewGameButton.setEnabled(true);
    }
}
