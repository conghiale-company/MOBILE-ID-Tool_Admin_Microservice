package view.modal.identity;

/**
 * Project: TOOL_ADMIN_MICROSERVICE
 * Created by Cong Nghia le
 * Date: 2025/01/14
 * Time: 4:44 PM
 */

import model.Identity;
import view.MainView;

import javax.swing.*;
import java.awt.*;

/**
 * @ 2025 Conghiale. All rights reserved
 */

public class DetailIdentityModal extends JDialog {

    /**
     * Creates and displays a modal dialog that shows the detailed information of a given Identity.
     * The identity details are presented in a read-only format, with fields displaying various attributes
     * such as ID, Enabled status, Name, Remarks, Client information, and timestamps.
     *
     * @param parent The parent JFrame used to center the modal dialog relative to it.
     * @param identity The Identity object that contains the details to be displayed in the modal.
     */
    public DetailIdentityModal(JFrame parent, Identity identity) {
        super(parent, "Identity Detail", true);  // Set up modal dialog
        setLayout(new BorderLayout());
        setSize(500, 400);  // Set the size of the modal dialog
        setLocationRelativeTo(parent);  // Center the dialog relative to the parent
        setModalityType(ModalityType.APPLICATION_MODAL);

//        Create panel with GridBagLayout
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);  // Spacing between components
        gbc.fill = GridBagConstraints.HORIZONTAL;

//        Create and set up labels and text fields (non-editable)
        JLabel idLabel = new JLabel("ID:");
        JTextField idField = new JTextField(String.valueOf(identity.getId()), 20);
        idField.setEditable(false);

        JLabel enabledLabel = new JLabel("ENABLE:");
        JTextField enabledField = new JTextField(String.valueOf(identity.getEnabled()), 20);
        enabledField.setEditable(false);

        JLabel nameLabel = new JLabel("NAME:");
        JTextField nameField = new JTextField(identity.getName(), 20);
        nameField.setEditable(false);

        JLabel remarkEnLabel = new JLabel("REMARK EN:");
        JTextField remarkEnField = new JTextField(identity.getRemark_en(), 20);
        remarkEnField.setEditable(false);

        JLabel remarkLabel = new JLabel("REMARK:");
        JTextField remarkField = new JTextField(identity.getRemark(), 20);
        remarkField.setEditable(false);

        JLabel clientIdLabel = new JLabel("CLIENT ID:");
        JTextField clientIdField = new JTextField(identity.getClientID(), 20);
        clientIdField.setEditable(false);

        JLabel clientSecretLabel = new JLabel("CLIENT SECRET:");
        JTextField clientSecretField = new JTextField(identity.getClientSecret(), 20);
        clientSecretField.setEditable(false);

        JLabel propertiesLabel = new JLabel("PROPERTIES:");
        JTextField propertiesField = new JTextField(identity.getProperties(), 20);
        propertiesField.setEditable(false);

        JLabel createdByLabel = new JLabel("CREATED BY:");
        JTextField createdByField = new JTextField(identity.getCreatedBy(), 20);
        createdByField.setEditable(false);

        JLabel createdAtLabel = new JLabel("CREATED AT:");
        JTextField createdAtField = new JTextField(identity.getCreatedAt().toString(), 20);
        createdAtField.setEditable(false);

        JLabel modifiedByLabel = new JLabel("MODIFIED BY:");
        JTextField modifiedByField = new JTextField(identity.getModifiedBy(), 20);
        modifiedByField.setEditable(false);

        JLabel modifiedAtLabel = new JLabel("MODIFIED AT:");
        JTextField modifiedAtField = new JTextField(identity.getModifiedAt().toString(), 20);
        modifiedAtField.setEditable(false);

//        Add components to the panel
        int row = 0;

//        Add ID field
        gbc.gridx = 0;
        gbc.gridy = row;
        panel.add(idLabel, gbc);
        gbc.gridx = 1;
        panel.add(idField, gbc);

//        Add ENABLE field
        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        panel.add(enabledLabel, gbc);
        gbc.gridx = 1;
        panel.add(enabledField, gbc);

//        Add NAME field
        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        panel.add(nameLabel, gbc);
        gbc.gridx = 1;
        panel.add(nameField, gbc);

//        Add REMARK EN field
        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        panel.add(remarkEnLabel, gbc);
        gbc.gridx = 1;
        panel.add(remarkEnField, gbc);

//        Add REMARK field
        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        panel.add(remarkLabel, gbc);
        gbc.gridx = 1;
        panel.add(remarkField, gbc);

//        Add CLIENT ID field
        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        panel.add(clientIdLabel, gbc);
        gbc.gridx = 1;
        panel.add(clientIdField, gbc);

//        Add CLIENT SECRET field
        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        panel.add(clientSecretLabel, gbc);
        gbc.gridx = 1;
        panel.add(clientSecretField, gbc);

//        Add PROPERTIES field
        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        panel.add(propertiesLabel, gbc);
        gbc.gridx = 1;
        panel.add(propertiesField, gbc);

//        Add CREATED BY field
        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        panel.add(createdByLabel, gbc);
        gbc.gridx = 1;
        panel.add(createdByField, gbc);

//        Add CREATED AT field
        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        panel.add(createdAtLabel, gbc);
        gbc.gridx = 1;
        panel.add(createdAtField, gbc);

//        Add MODIFIED BY field
        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        panel.add(modifiedByLabel, gbc);
        gbc.gridx = 1;
        panel.add(modifiedByField, gbc);

//        Add MODIFIED AT field
        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        panel.add(modifiedAtLabel, gbc);
        gbc.gridx = 1;
        panel.add(modifiedAtField, gbc);

//        Panel for buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());

        JButton closeButton = new JButton("CLOSE");
        closeButton.addActionListener(e -> {
            if (parent instanceof MainView) {
                try {
                    ((MainView) parent).loadAllIdentity();
                    ((MainView) parent).restoreTableSelectionIdentity(true);
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
            dispose();
        }); // Close the modal
        buttonPanel.add(closeButton);

//        Add panel and button panel to the modal
        add(panel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }
}
