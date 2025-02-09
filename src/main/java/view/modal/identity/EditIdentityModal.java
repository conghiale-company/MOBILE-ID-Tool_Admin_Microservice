package view.modal.identity;

/**
 * Project: ZNS-Maven
 * Created by Cong Nghia le
 * Date: 2025/01/10
 * Time: 10:23 AM
 */

import controller.IdentityController;
import model.Identity;
import view.MainView;
import view.modal.ConfigInfo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

/**
 * @ 2025 Conghiale. All rights reserved
 */

public class EditIdentityModal extends JDialog {
    /**
     * Creates and displays a modal dialog for editing an Identity.
     * This dialog allows the user to view and update various details of an Identity such as remarks, properties, and other fields.
     * It includes validation for required fields and handles the submission of changes to the Identity.
     *
     * @param parent The parent JFrame which the modal dialog will be centered on.
     * @param identity The Identity object containing the current data to be edited.
     */
    public EditIdentityModal(JFrame parent, Identity identity) throws IOException {
        super(parent, "Edit Identity", true);
        setLayout(new BorderLayout());
        setSize(500, 400);
        setLocationRelativeTo(parent);
        setModalityType(ModalityType.APPLICATION_MODAL);

//        Panel containing input fields
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

//        Labels and text fields
        JLabel idLabel = new JLabel("ID:");
        JTextField idField = new JTextField(20);
        idField.setEditable(false);

        JLabel nameLabel = new JLabel("NAME:");
        JTextField nameField = new JTextField(20);
        nameField.setEditable(false);
        nameField.setToolTipText("This field cannot be edited.");

        JLabel remarkEnLabel = new JLabel("REMARK EN:");
        JTextField remarkEnField = new JTextField(20);

        JLabel remarkLabel = new JLabel("REMARK:");
        JTextField remarkField = new JTextField(20);

        JLabel clientIdLabel = new JLabel("CLIENT ID:");
        JTextField clientIdField = new JTextField(20);
        clientIdField.setEditable(false);
        clientIdField.setToolTipText("This field cannot be edited.");

        JLabel clientSecretLabel = new JLabel("CLIENT SECRET:");
        JTextField clientSecretField = new JTextField(20);
        clientSecretField.setEditable(false);
        clientSecretField.setToolTipText("This field cannot be edited.");

        JLabel propertiesLabel = new JLabel("PROPERTIES:");
        JTextField propertiesField = new JTextField(20);

        JLabel createdByLabel = new JLabel("CREATED BY:");
        JTextField createdByField = new JTextField(20);
        createdByField.setEditable(false);
        createdByField.setToolTipText("This field cannot be edited.");

        JLabel createdAtLabel = new JLabel("CREATED AT:");
        JTextField createdAtField = new JTextField(20);
        createdAtField.setEditable(false);
        createdAtField.setToolTipText("This field cannot be edited.");

        JLabel modifiedByLabel = new JLabel("MODIFIED BY:");
        JTextField modifiedByField = new JTextField(20);
        modifiedByField.setEditable(false);
        modifiedByField.setToolTipText("This field cannot be edited.");

//        Add components to the panel
        int row = 0;

//        [ID]
        gbc.gridx = 0;
        gbc.gridy = row;
        formPanel.add(idLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(idField, gbc);

//        [NAME]
        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        formPanel.add(nameLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(nameField, gbc);

//        [REMARK EN]
        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        formPanel.add(remarkEnLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(remarkEnField, gbc);

//        [REMARK]
        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        formPanel.add(remarkLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(remarkField, gbc);

//        [CLIENT ID]
        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        formPanel.add(clientIdLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(clientIdField, gbc);

//        [CLIENT SECRET]
        row++;
        gbc.gridx = 0;
        gbc.gridy = 5;
        formPanel.add(clientSecretLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(clientSecretField, gbc);

//        [PROPERTIES]
        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        formPanel.add(propertiesLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(propertiesField, gbc);

//        [CREATE BY]
        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        formPanel.add(createdByLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(createdByField, gbc);

//        [CREATE AT]
        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        formPanel.add(createdAtLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(createdAtField, gbc);

//        [MODIFIED BY]
        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        formPanel.add(modifiedByLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(modifiedByField, gbc);

//        Show value of fields
        idField.setText(identity.getId().toString());
        nameField.setText(identity.getName());
        remarkEnField.setText(identity.getRemark_en());
        remarkField.setText(identity.getRemark());
        clientIdField.setText(identity.getClientID());
        clientSecretField.setText(identity.getClientSecret());
        propertiesField.setText(identity.getProperties());
        createdByField.setText(identity.getCreatedBy());
        createdAtField.setText(identity.getCreatedAt().toString());
        modifiedByField.setText(ConfigInfo.getInstance().getUserName());

//        Panel containing buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton cancelButton = new JButton("CANCEL");
        JButton editButton = new JButton("EDIT IDENTITY");

//        Button event handling
        cancelButton.addActionListener(e -> dispose());
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                long id = Long.parseLong(idField.getText());
                String remarkEn = remarkEnField.getText();
                String remark = remarkField.getText();
                String properties = propertiesField.getText();
                String modifiedBy = modifiedByField.getText();

                if (idField.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(EditIdentityModal.this, "ID are required!", "VALIDATION ERROR", JOptionPane.WARNING_MESSAGE);
                } else {
                    try {
                        String responseCode = IdentityController.updateIdentity(id, remarkEn, remark, properties, modifiedBy);
                        if (responseCode.equals("0")) {
                            JOptionPane.showMessageDialog(EditIdentityModal.this, "IDENTITY with ID " + id + " has been edited successfully", "EDIT IDENTITY SUCCESS", JOptionPane.INFORMATION_MESSAGE);
                            if (parent instanceof MainView) {
                                ((MainView) parent).loadAllIdentity();
                                ((MainView) parent).restoreTableSelectionIdentity(false);
                            }
                        } else {
                            JOptionPane.showMessageDialog(EditIdentityModal.this, "Edit IDENTITY with ID " + id + " failed", "EDIT IDENTITY ERROR", JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        });

        buttonPanel.add(cancelButton);
        buttonPanel.add(editButton);

//        Add panels to dialog
        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }
}