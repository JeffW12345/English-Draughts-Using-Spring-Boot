package com.github.jeffw12345.draughts.client.view;

import com.github.jeffw12345.draughts.models.game.Board;
import com.github.jeffw12345.draughts.models.game.SquareContent;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.*;

@Slf4j
public class ViewUtils {

    static void makeGuiDisplayProperlyOnWindowsAndMacs() {
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    public static JButton createButton(String label, boolean setEnabled, DraughtsBoardGui view){
        JButton jButton = new JButton(label);
        jButton.setFont(new java.awt.Font("Arial", Font.BOLD, 18));
        jButton.setEnabled(setEnabled);
        jButton.addActionListener(view);
        return jButton;
    }

    public static JLabel createJLabel(Font font, String initialText){
        JLabel label = new JLabel(initialText);
        label.setFont(font);
        return label;
    }


}
