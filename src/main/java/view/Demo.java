package view;

/**
 * Project: ZNS-Maven
 * Created by Cong Nghia le
 * Date: 2025/01/07
 * Time: 9:39 AM
 */

import javax.swing.*;
import java.awt.*;

/**
 * @ 2025 Conghiale. All rights reserved
 */

public class Demo {

    private JPanel panel1;
    private JTabbedPane tabbedPane1;
    private JPanel panel2;
    private JPanel panel3;
    private JButton btn01;
    private JLabel lb02;
    private JLabel lb03;

    public void createUIComponents() {
        // TODO: place custom component creation code here
        panel2.setPreferredSize(new Dimension(1200, 1000));
        panel3.setPreferredSize(new Dimension(1200, 1000));

        panel3.add(lb03);

        tabbedPane1.add("Tab 1" ,panel2);
        tabbedPane1.add("Tab 2", panel3);

        panel1.setLayout(new BorderLayout());
        panel1.add(tabbedPane1, BorderLayout.CENTER);
        panel1.setPreferredSize(new Dimension(1200, 1000));

        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Demo");
            frame.setContentPane(panel1);

            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLocationRelativeTo(null);
            frame.setSize(1200, 1000);
            frame.pack();
            frame.setVisible(true);
        });
    }
}
