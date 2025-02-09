package view.modal.identity;

/**
 * Project: ZNS-Maven
 * Created by Cong Nghia le
 * Date: 2025/01/10
 * Time: 10:23 AM
 */

import controller.IdentityController;
import view.MainView;

import javax.swing.*;
import java.awt.*;

/**
 * @ 2025 Conghiale. All rights reserved
 */

public class AddNewIdentityModal extends JDialog {

    /**
     * Creates and displays a modal dialog for adding a new Identity.
     * This dialog allows the user to input the details of a new Identity, including name, remarks, client information, and properties.
     * It includes validation for required fields and handles the submission of a new Identity to the system.
     * Additionally, it allows the user to generate client information (Client ID and Client Secret).
     *
     * @param parent The parent JFrame which the modal dialog will be centered on.
     */
    public AddNewIdentityModal(JFrame parent) {
        super(parent, "Add New Identity", true);
        setLayout(new BorderLayout());
        setSize(500, 400);
        setLocationRelativeTo(parent);
        setModalityType(ModalityType.APPLICATION_MODAL);

//        org.toolMicroservices.Main Panel with GridBagLayout for grid arrangement
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // Spacing between components
        gbc.fill = GridBagConstraints.HORIZONTAL;

//        Labels and text fields
//        [NAME]
        JLabel nameLabel = new JLabel("NAME:");
        JTextField nameField = new JTextField(20);

//        [REMARK EN]
        JLabel remarkEnLabel = new JLabel("REMARK EN:");
        JTextField remarkEnField = new JTextField(20);

//        [REMARK]
        JLabel remarkLabel = new JLabel("REMARK:");
        JTextField remarkField = new JTextField(20);

//        [CLIENT ID]
        JLabel clientIdLabel = new JLabel("CLIENT ID:");
        JTextField clientIdField = new JTextField(20);

//        [CLIENT SECRET]
        JLabel clientSecretLabel = new JLabel("CLIENT SECRET:");
        JTextField clientSecretField = new JTextField(20);

//        [PROPERTIES]
        JLabel propertiesLabel = new JLabel("PROPERTIES:");
        JTextField propertiesField = new JTextField(20);

//        [CREATED BY]
        JLabel createdByLabel = new JLabel("CREATED BY:");
        JTextField createdByField = new JTextField(20);

//        Add components to the panel
        int row = 0;

//        [NAME]
        gbc.gridx = 0;
        gbc.gridy = row;
        panel.add(nameLabel, gbc);
        gbc.gridx = 1;
        panel.add(nameField, gbc);

//        [REMARK EN]
        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        panel.add(remarkEnLabel, gbc);
        gbc.gridx = 1;
        panel.add(remarkEnField, gbc);

//        [REMARK]
        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        panel.add(remarkLabel, gbc);
        gbc.gridx = 1;
        panel.add(remarkField, gbc);

//        [CLIENT ID]
        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        panel.add(clientIdLabel, gbc);
        gbc.gridx = 1;
        panel.add(clientIdField, gbc);

//        [CLIENT SECRET]
        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        panel.add(clientSecretLabel, gbc);
        gbc.gridx = 1;
        panel.add(clientSecretField, gbc);

//        Button GET CLIENT INFO
        row++;
        gbc.gridx = 1;
        gbc.gridy = row;
        JButton getClientInfoButton = new JButton("GET CLIENT INFO");
        getClientInfoButton.addActionListener(e -> {
            // Giả lập dữ liệu CLIENT ID và CLIENT SECRET
//            IdentityController.getClientKey();

            clientIdField.setText("GeneratedClientID");
            clientSecretField.setText("GeneratedClientSecret");
            JOptionPane.showMessageDialog(AddNewIdentityModal.this, "Client info retrieved!", "Info", JOptionPane.INFORMATION_MESSAGE);
        });
        panel.add(getClientInfoButton, gbc);

//        [PROPERTIES]
        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        panel.add(propertiesLabel, gbc);
        gbc.gridx = 1;
        panel.add(propertiesField, gbc);

//        [CREATED BY]
        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        panel.add(createdByLabel, gbc);
        gbc.gridx = 1;
        panel.add(createdByField, gbc);

//        Panel Button
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());

        JButton cancelButton = new JButton("CANCEL");
        cancelButton.addActionListener(e -> dispose()); // Close modal

        JButton addButton = new JButton("ADD IDENTITY");
        addButton.addActionListener(e -> {
//            Handling when pressing ADD USER button
            String name = nameField.getText();
            String remarkEn = remarkEnField.getText();
            String remark = remarkField.getText();
            String clientId = clientIdField.getText();
            String clientSecret = clientSecretField.getText();
            String properties = propertiesField.getText();
            String createdBy = createdByField.getText();

            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(AddNewIdentityModal.this, "NAME are required!", "VALIDATION ERROR", JOptionPane.WARNING_MESSAGE);
            } else if (clientId.isEmpty()) {
                JOptionPane.showMessageDialog(AddNewIdentityModal.this, "CLIENT ID are required!", "VALIDATION ERROR", JOptionPane.WARNING_MESSAGE);
            } else if (clientSecret.isEmpty()) {
                JOptionPane.showMessageDialog(AddNewIdentityModal.this, "CLIENT SECRET are required!", "VALIDATION ERROR", JOptionPane.WARNING_MESSAGE);
            } else {
                try {
                    long identityID = IdentityController.insertIdentity(name, remarkEn, remark, clientId, clientSecret, properties, createdBy);
                    if (identityID != 0) {
                        JOptionPane.showMessageDialog(AddNewIdentityModal.this, "IDENTITY added successfully!", "ADD IDENTITY SUCCESS", JOptionPane.INFORMATION_MESSAGE);
                        if (parent instanceof MainView) {
                            ((MainView) parent).loadAllIdentity();
                            ((MainView) parent).selectRowById(identityID);
                        }
                    } else {
                        JOptionPane.showMessageDialog(AddNewIdentityModal.this, "Add this IDENTITY failed", "ADD IDENTITY ERROR", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
                dispose();
            }
        });

        buttonPanel.add(cancelButton);
        buttonPanel.add(addButton);

//        Add main panel and button panel to modal
        add(panel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

//    public static void main(String[] args) {
//        // Tạo JFrame cha
//        JFrame frame = new JFrame("org.toolMicroservices.Main Frame");
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.setSize(300, 200);
//        frame.setLocationRelativeTo(null);
//
//        JButton openModalButton = new JButton("Open Add New Identity Modal");
//        openModalButton.addActionListener(e -> new AddNewIdentityModal(frame).setVisible(true));
//
//        frame.setLayout(new FlowLayout());
//        frame.add(openModalButton);
//        frame.setVisible(true);
//    }
}