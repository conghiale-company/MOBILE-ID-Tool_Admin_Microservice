package view.modal;

/**
 * Project: ZNS-Maven
 * Created by Cong Nghia le
 * Date: 2025/01/09
 * Time: 11:34 AM
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * @ 2025 Conghiale. All rights reserved
 */

public class AddModal {
    private static void openNewModal(String title, JFrame parent, int width, int height) {
//        Create a modal JDialog
        JDialog modalDialog = new JDialog(parent, title, true);
        modalDialog.setSize(width, height);
        modalDialog.setLayout(new FlowLayout());
//        modalDialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE); // Không đóng bằng nút X

//        Modal Content
        JLabel message = new JLabel("This is a modal dialog.");
        JButton closeButton = new JButton("Close");

        // Sự kiện nút Close
        closeButton.addActionListener((ActionEvent e) -> modalDialog.dispose());

        modalDialog.add(message);
        modalDialog.add(closeButton);
        modalDialog.setLocationRelativeTo(parent); // Canh giữa cửa sổ chính
        modalDialog.setVisible(true); // Hiển thị Modal
    }
}
