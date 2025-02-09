package view;

import DAO.ConnectorDAO;
import DAO.UserDAO;
import controller.*;
import model.*;
import org.apache.log4j.Logger;
import view.confirmDialog.RemoveIdentityDialog;
import view.modal.identity.AddNewIdentityModal;
import view.modal.identity.DetailIdentityModal;
import view.modal.identity.EditIdentityModal;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.sql.*;
import java.util.*;
import java.util.List;

public class MainView extends JFrame {
    private final Logger LOGGER = Logger.getLogger(MainView.class);

    //add user
    private JTextField txtUserName = new JTextField(30);
    private JTextField txtUserEmail = new JTextField(30);
    private JTextField txtUserPhone = new JTextField(30);
    private JTextField txtUserPassword = new JTextField(30);
    private JTextField txtUserPersonalCode = new JTextField(30);
    private JButton btnAddUserSubmit = new JButton("Submit");

    // add identity
    public Long getSelectedRowID;
    public JTextField txtIdentityName = new JTextField(30);
    public JComboBox OwnerName = new JComboBox();
    public JTextField OwnerEmail = new JTextField(50);
    public JTextField txtIdentityPhone = new JTextField(30);
    public JTextField txtIdentityEmail = new JTextField(30);
    public JButton btnLogo = new JButton("Choose Logo");
    public JLabel lblLogoThumbnail = new JLabel();
    public JLabel lblLogoPath = new JLabel("No file selected");
    public JRadioButton rbOrganizational = new JRadioButton("ORGANIZATIONAL");
    public JRadioButton rbIndividual = new JRadioButton("INDIVIDUAL");
    public JTextField txtIdentityClientID = new JTextField(30);
    public JTextField txtIdentityClientSecret = new JTextField(30);
    public JTextField txtIdentityRemark = new JTextField(30);
    public JTextField txtIdentityHmac = new JTextField(30);
    public JTextField txtIdentityCreatedBy = new JTextField(30);
    public JButton btnAddIdentitySubmit = new JButton("Submit");
    public JButton btnUpdateIdentitySubmit = new JButton("Update");
    public JButton btnAddConnectorSubmit = new JButton("submit Connector");

    // select connector
    public JComboBox identityName = new JComboBox();
    public JComboBox connectorName = new JComboBox();
    public JTextField connectorProvider = new JTextField();
    public JTextField connectorCallBackURL = new JTextField();
    public JTextField connectorRemark = new JTextField();
    public JTextField connectorOfficeAccountID = new JTextField();

    //table
    public DefaultTableModel identityTableModel;
    public DefaultTableModel ConnectorTableModel;
    private DefaultTableModel auditLogTableModel;
    private DefaultTableModel iConnectorTableModel;
    private JTable identityTable;
    private JTable ConnectorTable;
    private JTable auditLogTable;
    private JTable identityConnectorTable;

    private String selectControlerLogo;
    private JLabel lblLogoImage;
    public JTextArea txtSecretKey = new JTextArea(10, 30);
    private JScrollPane scrollSecretKey = new JScrollPane(txtSecretKey);

    //Connector
    public JTextField txtConnectorName = new JTextField(30);
    public JTextField txtConnectorProvider = new JTextField(30);
    public JTextField txtConnectorCallBackURL = new JTextField(30);
    public JTextField txtConnectorRemark = new JTextField(30);
    public JTextField txtConnectorRemarkEn = new JTextField(30);
    public JTextArea txtConnectorProperties = new JTextArea(25, 30);
    public JScrollPane scrollProperties = new JScrollPane(txtConnectorProperties);
    public JRadioButton rbEnabled = new JRadioButton("ENABLED");
    public JRadioButton rbDisabled = new JRadioButton("DISABLED");
    public JButton btnConnectorLogo = new JButton("Choose Logo");
    public JLabel lblConnectorLogoImage;
    public JLabel lblConnectorLogoThumbnail = new JLabel();
    public JLabel lblConnectorLogoPath = new JLabel("No file selected");
    public JTextField txtConnectorCreatedBy = new JTextField(30);

    //main panel
    private JTabbedPane tabbedPane;

    public MainView() {
        setTitle("IDENTITY MANAGEMENT");
        setSize(1600, 1200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

//        Create JTabbedPane to manage tabs
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Arial", Font.BOLD, 15));

//        Create IDENTITY Panel, CONNECTOR Panel, IDENTITY-CONNECTOR Panel and AUDIT-LOG Panel
        JPanel identityPanel = identityUI();
        JPanel ConnectorPanel = connectorUI();
        JPanel IdentityConnectorPanel = createIConnectorTablePanel();
        JPanel auditLogPanel = createAuditLogPanel();

        // Add panels to the tabbed pane
        tabbedPane.addTab("IDENTITY", identityPanel);
//        tabbedPane.addTab("FUNCTION", ConnectorPanel);
        tabbedPane.addTab("CONNECTOR", ConnectorPanel);
        tabbedPane.addTab("IDENTITY CONNECTOR", IdentityConnectorPanel);
//        tabbedPane.addTab("IDENTITY FUNCTION", IdentityConnectorPanel);
//        tabbedPane.addTab("IDENTITY IP ACCESS", IdentityConnectorPanel);
        tabbedPane.addTab("AUDIT LOG", auditLogPanel);

        // Add the tabbed pane to the frame
        add(tabbedPane, BorderLayout.CENTER);
    }

    public DefaultTableModel getIdentityTableModel() {
        return identityTableModel;
    }

    public DefaultTableModel getConnectorTableModel() {
        return ConnectorTableModel;
    }

    public DefaultTableModel getIConnectorTableModel() { return iConnectorTableModel; }

    public DefaultTableModel getAuditLogTableModel() {
        return auditLogTableModel;
    }

    private int identitySelectedRowIndex = -1; // Lưu chỉ số dòng được chọn

    private JPanel identityUI() {
        JPanel containerPanel = new JPanel(new BorderLayout());
//        Left side: identityPanel
        JPanel identityPanel = createIdentityTablePanel();

//        Right side: button panel
        JPanel buttonPanel = createButtonPanel(
                e -> {
//                    openNewWindow("Add New User", addNewUserFormPanel(), 500, 400);
//                    restoreTableSelectionIdentity();
                    LOGGER.info("[CREATE IDENTITY UI] Currently adding new users is not supported.");
                    JOptionPane.showMessageDialog(MainView.this, "[CREATE IDENTITY UI] Currently adding new users is not supported", "ADD USER", JOptionPane.WARNING_MESSAGE);
                },
                e -> {
                    new AddNewIdentityModal(MainView.this).setVisible(true);
                },
                e -> {
                    if (getSelectedRowID != null) {
                        try {
                            Identity identity = IdentityController.getIdentityByID(getSelectedRowID);
                            if (identity != null) {
                                new EditIdentityModal(MainView.this, identity);
                            } else {
                                JOptionPane.showMessageDialog(MainView.this, "IDENTITY not found with ID " + getSelectedRowID, "EDIT IDENTITY", JOptionPane.WARNING_MESSAGE);
                            }
                        } catch (Exception ex) {
                            throw new RuntimeException(ex);
                        }
                    } else {
                        JOptionPane.showMessageDialog(MainView.this, "Please select an IDENTITY from the table.", "EDIT IDENTITY", JOptionPane.WARNING_MESSAGE);
                    }
                },
                e -> {
                    if (getSelectedRowID != null) {
                        try {
                            Identity identity = IdentityController.getIdentityByID(getSelectedRowID);
                            if (identity != null) {
                                RemoveIdentityDialog.removeIdentityDialog(identity.getId(), identity.getEnabled(), identity.getModifiedBy(), MainView.this);
                            } else {
                                JOptionPane.showMessageDialog(MainView.this, "IDENTITY not found with ID " + getSelectedRowID, "REMOVE IDENTITY", JOptionPane.WARNING_MESSAGE);
                            }
                        } catch (Exception ex) {
                            throw new RuntimeException(ex);
                        }
                    } else {
                        JOptionPane.showMessageDialog(MainView.this, "Please select an identity from the table", "REMOVE IDENTITY", JOptionPane.WARNING_MESSAGE);
                    }
                },
                e -> {
                    if (getSelectedRowID != null) {
                        try {
                            Identity identity = IdentityController.getIdentityByID(getSelectedRowID);
                            if (identity != null) {
                                new DetailIdentityModal(MainView.this, identity).setVisible(true);
                            } else {
                                JOptionPane.showMessageDialog(MainView.this, "IDENTITY not found with ID " + getSelectedRowID, "DETAIL IDENTITY", JOptionPane.WARNING_MESSAGE);
                            }
                        } catch (Exception ex) {
                            throw new RuntimeException(ex);
                        }
                    } else {
                        JOptionPane.showMessageDialog(MainView.this, "Please select an IDENTITY from the table.", "EDIT IDENTITY", JOptionPane.WARNING_MESSAGE);
                    }
                }
        );

        containerPanel.add(identityPanel, BorderLayout.CENTER); // Add identityPanel on the left (center of BorderLayout)
        containerPanel.add(buttonPanel, BorderLayout.EAST);

        return containerPanel;
    }

    private JPanel createIdentityTablePanel() {
//        String[] columnNames = {"ID", "ENABLED", "NAME", "REMARK EN", "REMARK", "CLIENT ID", "CLIENT SECRET",
//                "PROPERTIES", "CREATED BY", "CREATED AT", "MODIFIED BY", "MODIFIED AT"};
        String[] columnNames = {"ID", "ENABLED", "NAME", "REMARK EN", "REMARK", "CLIENT ID", "CLIENT SECRET",
                "PROPERTIES"};

        identityTableModel = new DefaultTableModel(columnNames, 0);
        identityTable = new JTable(identityTableModel) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JScrollPane scrollPane = new JScrollPane(identityTable);

//        Add JTextField for filtering
        JTextField tfSearch = new JTextField();
        tfSearch.setToolTipText("Enter text to search");

//        Create a TableRowSorter to apply the filter
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(identityTableModel);
        identityTable.setRowSorter(sorter);

//        Listen for input events in JTextField
        tfSearch.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                applyFilter();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                applyFilter();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                applyFilter();
            }

            private void applyFilter() {
                String text = tfSearch.getText();
                if (text.trim().isEmpty()) {
                    sorter.setRowFilter(null); // Do not apply filter if no data is available
                } else {
                    sorter.setRowFilter(RowFilter.regexFilter("(?i)" + text)); // Case-insensitive filters
                }
            }
        });

//        Add Clear Filter button
        JButton btnClear = new JButton("CLEAR");

//        Listen for Clear Filter button event
        btnClear.addActionListener(e -> {
            tfSearch.setText("");
            sorter.setRowFilter(null); // Loại bỏ bộ lọc
        });

        JPanel tablePanel = new JPanel(new BorderLayout());
        JPanel filterPanel = new JPanel(new BorderLayout());
        filterPanel.add(new JLabel("SEARCH: "), BorderLayout.WEST);
        filterPanel.add(tfSearch, BorderLayout.CENTER);
        filterPanel.add(btnClear, BorderLayout.EAST);

        tablePanel.add(filterPanel, BorderLayout.NORTH);
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        identityTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
            int selectedRow = identityTable.getSelectedRow();

            if (selectedRow != -1) {
                if (selectedRow == identitySelectedRowIndex) {
                    identityTable.clearSelection(); // Deselect the line
                    identitySelectedRowIndex = -1; // Reset selected line
                    getSelectedRowID = null; // Reset the IDENTITY value of the selected ID
                } else {
                    identitySelectedRowIndex = selectedRow; // Update current row
                    getSelectedRowID = (Long) identityTableModel.getValueAt(selectedRow, 0); // Get the IDENTITY ID value of the selected row
                    identityTable.setRowSelectionInterval(selectedRow, selectedRow); // Select the line the user clicked
                }
            }
            }
        });

        return tablePanel;
    }

//    [ACTION]
    public void loadAllIdentity() throws Exception {
        List<Identity> identities = IdentityController.getAllIdentities();

//        Clear existing rows from the table
        this.identityTableModel.setRowCount(0);

//        Add rows to the identity table model
        for (Identity identity : identities) {
            this.identityTableModel.addRow(new Object[]{
                    identity.getId(), identity.getEnabled(), identity.getName(), identity.getClientID(), identity.getClientSecret(),
                    identity.getProperties()
            });
        }

    }

//    [ACTION]
    public void selectRowById(Long id) {
    int rowCount = identityTableModel.getRowCount(); // Number of rows in table
    boolean rowFound = false; // Flag to determine if ID was found

    for (int row = 0; row < rowCount; row++) {
//        Get the value of the first column (ID) in the current row
        Long rowId = (Long) identityTableModel.getValueAt(row, 0);

//        If ID matches
        if (rowId != null && rowId.equals(id)) {
            identityTable.setRowSelectionInterval(row, row); // Select that item
            identitySelectedRowIndex = row; // Update selected item value
            getSelectedRowID = id; // Update ID value
            rowFound = true; // Mark item as found
            break;
        }
    }

//    If ID is not found, reselect the selected row.
    if (!rowFound) {
        restoreTableSelectionIdentity(false);
    }
}

//    [ACTION]
    public void restoreTableSelectionIdentity(boolean isReset) {
        if (isReset) {
            identitySelectedRowIndex = -1;
        } else if (identitySelectedRowIndex != -1) {
            identityTable.setRowSelectionInterval(identitySelectedRowIndex, identitySelectedRowIndex);
        }
    }

    private JPanel addNewUserFormPanel() {
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblUserName = new JLabel("User Name");
        JLabel lblUserEmail = new JLabel("Email");
        JLabel lblUserPhone = new JLabel("Phone");
        JLabel lblUserPassword = new JLabel("Password");
        JLabel lblUserPersonalCode = new JLabel("Personal Code");

        // Add form fields to the panel
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(lblUserName, gbc);
        gbc.gridx = 1;
        formPanel.add(txtUserName, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(lblUserEmail, gbc);
        gbc.gridx = 1;
        formPanel.add(txtUserEmail, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(lblUserPhone, gbc);
        gbc.gridx = 1;
        formPanel.add(txtUserPhone, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(lblUserPassword, gbc);
        gbc.gridx = 1;
        formPanel.add(txtUserPassword, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        formPanel.add(lblUserPersonalCode, gbc);
        gbc.gridx = 1;
        formPanel.add(txtUserPersonalCode, gbc);

        // Add the submit button
        gbc.gridx = 1;
        gbc.gridy = 8;
        formPanel.add(btnAddUserSubmit, gbc);

        // Add the ActionListener to the submit button
        btnAddUserSubmit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String userName = txtUserName.getText();
                String email = txtUserEmail.getText();
                String phone = txtUserPhone.getText();
                String password = txtUserPassword.getText();
                String personalCode = txtUserPersonalCode.getText();
                String hmac = "";
                String createdBy = "";

                // Call the controller's method to add the user
                try {
                    String responseCode = UserDAO.getInstance().addUser(userName, email, personalCode, phone, password, hmac, createdBy);

                    if ("0".equals(responseCode)) {
                        JOptionPane.showMessageDialog(null, "User added successfully!");
                    } else {
                        JOptionPane.showMessageDialog(null, "Error adding user. Response Code: " + responseCode);
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "Database error: " + ex.getMessage());
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        return formPanel;
    }

    private void populateOwnerComboBox() {
        try {
            OwnerName.removeAllItems();

            List<String> userList = UserDAO.getInstance().getUsersList();
            for (String userName : userList) {
                OwnerName.addItem(userName);
            }

        } catch (Exception ex) {
            // Hiển thị thông báo lỗi nếu xảy ra lỗi khi tải danh sách user
            JOptionPane.showMessageDialog(null, "Error loading user: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

//    HAS BEEN REPLACED --> view.Modal.AddNewIdentityModal AddNewIdentityModal
    private JPanel  addIdentityFormPanel(Identity identity) {
        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc1 = new GridBagConstraints();
        gbc1.insets = new Insets(10, 10, 10, 10);
        gbc1.fill = GridBagConstraints.BOTH;

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        // Form labels and fields
        JLabel lblName = new JLabel("Name");
        JLabel lblBusinessType = new JLabel("Business Type");
        JLabel lblOwnerName = new JLabel("Owner Name");
        JLabel lblClientID = new JLabel("Client ID");
        JLabel lblClientSecret = new JLabel("Client Secret");
        JLabel lblPhone = new JLabel("Phone");
        JLabel lblEmail = new JLabel("Email");
        JLabel lblLogo = new JLabel("Logo");
        JLabel lblHmac = new JLabel("Hmac");
        JLabel lblCreatedBy = new JLabel("Created By");

        ButtonGroup typeGroup = new ButtonGroup();
        typeGroup.add(rbOrganizational);
        typeGroup.add(rbIndividual);

        // Layout positioning of fields
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(lblName, gbc);
        gbc.gridx = 1;
        formPanel.add(txtIdentityName, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(lblBusinessType, gbc);
        JPanel radioPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        radioPanel.add(rbOrganizational);
        radioPanel.add(rbIndividual);
        gbc.gridx = 1;
        gbc.gridy = 1;
        formPanel.add(radioPanel, gbc);

        populateOwnerComboBox();
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(lblOwnerName, gbc);
        gbc.gridx = 1;
        formPanel.add(OwnerName, gbc);

        // Add "Add New User" button next to "Owner Name"
        JButton btnAddNewUser = new JButton("Add New User");
        gbc.gridx = 2;
        gbc.gridy = 2;
        formPanel.add(btnAddNewUser, gbc);

        btnAddNewUser.addActionListener(e -> openNewWindow("Add New User", addNewUserFormPanel(), 500, 500));

        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(lblClientID, gbc);
        gbc.gridx = 1;
        formPanel.add(txtIdentityClientID, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        formPanel.add(lblClientSecret, gbc);
        gbc.gridx = 1;
        formPanel.add(txtIdentityClientSecret, gbc);

        JButton getClientKey = new JButton("get client key");
        gbc.gridx = 2;
        gbc.gridy = 3;
        formPanel.add(getClientKey, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        formPanel.add(lblPhone, gbc);
        gbc.gridx = 1;
        formPanel.add(txtIdentityPhone, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        formPanel.add(lblEmail, gbc);
        gbc.gridx = 1;
        formPanel.add(txtIdentityEmail, gbc);

        gbc.gridx = 0;
        gbc.gridy = 7;
        formPanel.add(lblLogo, gbc);
        gbc.gridx = 1;
        formPanel.add(btnLogo, gbc);
        lblLogoThumbnail.setPreferredSize(new Dimension(100, 100));
        gbc.gridy = 8;
        formPanel.add(lblLogoThumbnail, gbc);

        btnLogo.addActionListener(e -> {
            chooseLogo(lblLogoThumbnail);
            lblLogoThumbnail.repaint();
        });

        gbc.gridx = 0;
        gbc.gridy = 9;
        formPanel.add(lblHmac, gbc);
        gbc.gridx = 1;
        formPanel.add(txtIdentityHmac, gbc);

        gbc.gridx = 0;
        gbc.gridy = 10;
        formPanel.add(lblCreatedBy, gbc);
        gbc.gridx = 1;
        formPanel.add(txtIdentityCreatedBy, gbc);

        gbc1.gridx = 0;
        gbc1.gridy = 0;
        gbc1.weightx = 0.5;
        gbc1.weighty = 1.0;
        mainPanel.add(formPanel, gbc1);

        // Add Submit and MoveToConnector buttons
        gbc1.gridx = 0;
        gbc1.gridy = 1;
        gbc1.gridwidth = 2;
        gbc1.weightx = 0;
        gbc1.weighty = 0;
        gbc1.anchor = GridBagConstraints.SOUTHEAST;

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnAddIdentitySubmit.setPreferredSize(new Dimension(150, 35));
        JButton btnMoveToConnector = new JButton("Submit & Choose Connector");
        btnMoveToConnector.setPreferredSize(new Dimension(200, 35));
        buttonPanel.add(btnAddIdentitySubmit);
        buttonPanel.add(btnMoveToConnector);
        mainPanel.add(buttonPanel, gbc1);

        IdentityController identityController = new IdentityController(MainView.this);

        getClientKey.addActionListener(e -> {
            identityController.getClientKey();
        });

        btnAddIdentitySubmit.addActionListener(e -> {
//            identityController.insertIdentity();
            try {
                identityController.refreshIdentityTable();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });

        btnMoveToConnector.addActionListener(e -> {
//            identityController.insertIdentity();
            try {
                identityController.refreshIdentityTable();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
            tabbedPane.setSelectedIndex(2);
            SwingUtilities.getWindowAncestor(mainPanel).dispose(); // Đóng cửa sổ addIdentityFormPanel
        });

        if (identity != null) {
            formPanel.revalidate();
            formPanel.repaint();
            identityController.fillTextFields(identity, lblLogoThumbnail);
        }

        return mainPanel;
    }

//    HAS BEEN REPLACED --> view.Modal.EditIdentityModal EditIdentityModal
    private JPanel editIdentityFormPanel(Identity identity) {
        JPanel formPanel = new JPanel(new GridBagLayout());
//        GridBagConstraints gbc = new GridBagConstraints();
//        gbc.insets = new Insets(10, 10, 10, 10);
//        gbc.fill = GridBagConstraints.HORIZONTAL;
//
//        // Form labels and fields
//        JLabel lblName = new JLabel("Name");
//        JLabel lblBusinessType = new JLabel("Business Type");
//        JLabel lblOwnerName = new JLabel("Owner Name");
//        JLabel lblClientID = new JLabel("Client ID");
//        JLabel lblClientSecret = new JLabel("Client Secret");
//        JLabel lblPhone = new JLabel("Phone");
//        JLabel lblEmail = new JLabel("Email");
//        JLabel lblLogo = new JLabel("Logo");
//        JLabel lblHmac = new JLabel("Hmac");
//        JLabel lblCreatedBy = new JLabel("Created By");
//
//        ButtonGroup typeGroup = new ButtonGroup();
//        typeGroup.add(rbOrganizational);
//        typeGroup.add(rbIndividual);
//
//        // Layout positioning of fields
//        gbc.gridx = 0;
//        gbc.gridy = 0;
//        formPanel.add(lblName, gbc);
//        gbc.gridx = 1;
//        formPanel.add(txtIdentityName, gbc);
//
//        gbc.gridx = 0;
//        gbc.gridy = 1;
//        formPanel.add(lblBusinessType, gbc);
//        JPanel radioPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
//        radioPanel.add(rbOrganizational);
//        radioPanel.add(rbIndividual);
//        gbc.gridx = 1;
//        gbc.gridy = 1;
//        formPanel.add(radioPanel, gbc);
//
//        populateOwnerComboBox();
//        gbc.gridx = 0;
//        gbc.gridy = 2;
//        formPanel.add(lblOwnerName, gbc);
//        gbc.gridx = 1;
//        formPanel.add(OwnerName, gbc);
//
//        // Add "Add New User" button next to "Owner Name"
//        JButton btnAddNewUser = new JButton("Add New User");
//        gbc.gridx = 2;
//        gbc.gridy = 2;
//        formPanel.add(btnAddNewUser, gbc);
//
//        btnAddNewUser.addActionListener(e -> openNewWindow("Add New User", addNewUserFormPanel(), 500, 500));
//
//        gbc.gridx = 0;
//        gbc.gridy = 3;
//        formPanel.add(lblClientID, gbc);
//        gbc.gridx = 1;
//        formPanel.add(txtIdentityClientID, gbc);
//
//        gbc.gridx = 0;
//        gbc.gridy = 4;
//        formPanel.add(lblClientSecret, gbc);
//        gbc.gridx = 1;
//        formPanel.add(txtIdentityClientSecret, gbc);
//
//        JButton getClientKey = new JButton("get client key");
//        gbc.gridx = 2;
//        gbc.gridy = 3;
//        formPanel.add(getClientKey, gbc);
//
//        gbc.gridx = 0;
//        gbc.gridy = 5;
//        formPanel.add(lblPhone, gbc);
//        gbc.gridx = 1;
//        formPanel.add(txtIdentityPhone, gbc);
//
//        gbc.gridx = 0;
//        gbc.gridy = 6;
//        formPanel.add(lblEmail, gbc);
//        gbc.gridx = 1;
//        formPanel.add(txtIdentityEmail, gbc);
//
//        gbc.gridx = 0;
//        gbc.gridy = 7;
//        formPanel.add(lblLogo, gbc);
//        gbc.gridx = 1;
//        formPanel.add(btnLogo, gbc);
//        lblLogoThumbnail.setPreferredSize(new Dimension(100, 100));
//        gbc.gridy = 8;
//        formPanel.add(lblLogoThumbnail, gbc);
//
//        btnLogo.addActionListener(e -> {
//            chooseLogo(lblLogoThumbnail);
//            lblLogoThumbnail.repaint();
//        });
//
//        gbc.gridx = 0;
//        gbc.gridy = 9;
//        formPanel.add(lblHmac, gbc);
//        gbc.gridx = 1;
//        formPanel.add(txtIdentityHmac, gbc);
//
//        gbc.gridx = 0;
//        gbc.gridy = 10;
//        formPanel.add(lblCreatedBy, gbc);
//        gbc.gridx = 1;
//        formPanel.add(txtIdentityCreatedBy, gbc);
//
//        gbc.gridx = 1;
//        gbc.gridy = 14;
//        formPanel.add(btnUpdateIdentitySubmit, gbc);
//
//        btnLogo.addActionListener(e -> {
//            chooseLogo(lblLogoThumbnail);
//            lblLogoThumbnail.repaint();
//        });
//
//        IdentityController identityController = new IdentityController(MainView.this);
//        if (identity != null) {
//            formPanel.revalidate();
//            formPanel.repaint();
//            identityController.fillTextFields(identity, lblLogoThumbnail);
//        }
//
//        btnUpdateIdentitySubmit.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//                identityController.updateIdentity();
//                try {
//                    identityController.refreshIdentityTable();
//                } catch (Exception ex) {
//                    throw new RuntimeException(ex);
//                }
//            }
//        });

        return formPanel;
    }

//    HAS BEEN REPLACED --> view.confirmDialog.IdentityConfirmDialog removeIdentityConfirmDialog
    private void deleteIdentityFormPanel(Long IdentityID) {
//        // Hiển thị hộp thoại xác nhận
//        int confirmation = JOptionPane.showConfirmDialog(
//                null,
//                "Are you sure you want to delete?",
//                "Confirm Delete",
//                JOptionPane.YES_NO_OPTION
//        );
//
//        if (confirmation == JOptionPane.YES_OPTION) {
//            // Nếu người dùng chọn Yes, thực hiện xóa
//            IdentityController Econtroller = new IdentityController(MainView.this);
//            Econtroller.deleteIdentity(IdentityID);
//        } else {
//            // Nếu người dùng chọn No, không làm gì
//            JOptionPane.showMessageDialog(null, "Delete cancelled.");
//        }
    }

//    HAS BEEN REPLACED --> view.confirmDialog.DetailIdentityModal removeIdentityConfirmDialog
    private JPanel viewIdentityFormPanel(Identity identity) {
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Form labels and fields
        JLabel lblName = new JLabel("Name");
        JLabel lblBusinessType = new JLabel("Business Type");
        JLabel lblOwnerName = new JLabel("Owner Name");
        JLabel lblClientID = new JLabel("Client ID");
        JLabel lblClientSecret = new JLabel("Client Secret");
        JLabel lblPhone = new JLabel("Phone");
        JLabel lblEmail = new JLabel("Email");
        JLabel lblLogo = new JLabel("Logo");
        JLabel lblHmac = new JLabel("Hmac");
        JLabel lblCreatedBy = new JLabel("Created By");

        ButtonGroup typeGroup = new ButtonGroup();
        typeGroup.add(rbOrganizational);
        typeGroup.add(rbIndividual);

        // Layout positioning of fields
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(lblName, gbc);
        gbc.gridx = 1;
        formPanel.add(txtIdentityName, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(lblBusinessType, gbc);
        JPanel radioPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        radioPanel.add(rbOrganizational);
        radioPanel.add(rbIndividual);
        gbc.gridx = 1;
        gbc.gridy = 1;
        formPanel.add(radioPanel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(lblOwnerName, gbc);
        gbc.gridx = 1;
        formPanel.add(OwnerName, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(lblClientID, gbc);
        gbc.gridx = 1;
        formPanel.add(txtIdentityClientID, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        formPanel.add(lblClientSecret, gbc);
        gbc.gridx = 1;
        formPanel.add(txtIdentityClientSecret, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        formPanel.add(lblPhone, gbc);
        gbc.gridx = 1;
        formPanel.add(txtIdentityPhone, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        formPanel.add(lblEmail, gbc);
        gbc.gridx = 1;
        formPanel.add(txtIdentityEmail, gbc);

        gbc.gridx = 0;
        gbc.gridy = 7;
        formPanel.add(lblLogo, gbc);
        gbc.gridx = 1;
        formPanel.add(btnLogo, gbc);
        lblLogoThumbnail.setPreferredSize(new Dimension(100, 100));
        gbc.gridy = 8;
        formPanel.add(lblLogoThumbnail, gbc);

        btnLogo.addActionListener(e -> {
            chooseLogo(lblLogoThumbnail);
            lblLogoThumbnail.repaint();
        });

        gbc.gridx = 0;
        gbc.gridy = 9;
        formPanel.add(lblHmac, gbc);
        gbc.gridx = 1;
        formPanel.add(txtIdentityHmac, gbc);

        gbc.gridx = 0;
        gbc.gridy = 10;
        formPanel.add(lblCreatedBy, gbc);
        gbc.gridx = 1;
        formPanel.add(txtIdentityCreatedBy, gbc);

        IdentityController identityController = new IdentityController(MainView.this);
        if (identity != null) {
            formPanel.revalidate();
            formPanel.repaint();
            identityController.fillTextFields(identity, lblLogoThumbnail);
        }

        return formPanel;
    }

//    [UNNECESSARY]
    private void clearAddIdentityForm() {
        txtIdentityName.setText("");
        if (OwnerName.getItemCount() > 0) {
            OwnerName.setSelectedIndex(0);
        }
        txtIdentityPhone.setText("");
        txtIdentityEmail.setText("");
        lblLogoThumbnail.setIcon(null);
        rbOrganizational.setSelected(false);
        rbIndividual.setSelected(false);
        txtIdentityClientID.setText("");
        txtIdentityClientSecret.setText("");
        txtIdentityRemark.setText("");
//        txtIdentityHmac.setText("");
        txtIdentityCreatedBy.setText("");
    }

    private void updateLogo(JLabel lblLogoImage, String logoBase64) {
        if (logoBase64 != null && !logoBase64.isEmpty()) {
            try {
                logoBase64 = logoBase64.replaceAll("\\s+", "");
                byte[] logoBytes = Base64.getDecoder().decode(logoBase64);
                ImageIcon logoIcon = new ImageIcon(logoBytes);
                Image logoImage = logoIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                lblLogoImage.setIcon(new ImageIcon(logoImage));
                lblLogoImage.setText("");
            } catch (IllegalArgumentException e) {
                // Xử lý lỗi Base64 không hợp lệ
                lblLogoImage.setIcon(null); // Xóa hình ảnh
                lblLogoImage.setText("Invalid logo data"); // Hiển thị lỗi
                e.printStackTrace();
            }
        } else {
            // Khi logoBase64 null hoặc rỗng
            lblLogoImage.setIcon(null); // Xóa hình ảnh
            lblLogoImage.setText("Logo not available"); // Hiển thị thông báo
        }
    }

    // Connector Panel --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    private int connectorSelectedRowIndex = -1; // Lưu chỉ số dòng được chọn

    private JPanel createConnectorTablePanel() {
        String[] columnNames = {"ID", "ENABLED", "PROVIDER_NAME", "NAME", "REMARK", "REMARK_EN", "PROPERTIES", "CALLBACK_URL", "CREATE BY", "CREATE AT"};
        ConnectorTableModel = new DefaultTableModel(columnNames, 0);
        ConnectorTable = new JTable(ConnectorTableModel) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        ConnectorTable.getColumnModel().getColumn(6).setPreferredWidth(30);

        JScrollPane ConnectorScrollPane = new JScrollPane(ConnectorTable);

        // Thêm JTextField để lọc
        JTextField filterTextField = new JTextField();
        filterTextField.setToolTipText("Enter text to filter");

        // Thêm nút Clear Filter
        JButton clearButton = new JButton("Clear");


        // Tạo TableRowSorter để áp dụng bộ lọc
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(ConnectorTableModel);
        ConnectorTable.setRowSorter(sorter);

        // Lắng nghe sự kiện nhập vào JTextField
        filterTextField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                applyFilter();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                applyFilter();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                applyFilter();
            }

            private void applyFilter() {
                String text = filterTextField.getText();
                if (text.trim().length() == 0) {
                    sorter.setRowFilter(null); // Không áp dụng bộ lọc nếu không có dữ liệu
                } else {
                    sorter.setRowFilter(RowFilter.regexFilter("(?i)" + text)); // Bộ lọc không phân biệt chữ hoa chữ thường
                }
            }
        });

        // Lắng nghe sự kiện nút Clear Filter
        clearButton.addActionListener(e -> {
            filterTextField.setText("");
            sorter.setRowFilter(null); // Loại bỏ bộ lọc
        });

        // Panel chứa JTextField, nút Clear và bảng
        JPanel ConnectorTablePanel = new JPanel(new BorderLayout());
        JPanel filterPanel = new JPanel(new BorderLayout());
        filterPanel.add(new JLabel("Filter: "), BorderLayout.WEST);
        filterPanel.add(filterTextField, BorderLayout.CENTER);
        filterPanel.add(clearButton, BorderLayout.EAST);

        ConnectorTablePanel.add(filterPanel, BorderLayout.NORTH);
        ConnectorTablePanel.add(ConnectorScrollPane, BorderLayout.CENTER);

        ConnectorTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = ConnectorTable.getSelectedRow();

                if (selectedRow != -1) {
                    if (selectedRow == connectorSelectedRowIndex) {
                        ConnectorTable.clearSelection(); // Bỏ chọn dòng
                        connectorSelectedRowIndex = -1; // Reset trạng thái
                        getSelectedRowID = null; // Đặt lại giá trị nếu cần
                    } else {
                        connectorSelectedRowIndex = selectedRow; // Cập nhật dòng hiện tại
                        getSelectedRowID = (Long) ConnectorTableModel.getValueAt(selectedRow, 0);
                        ConnectorTable.setRowSelectionInterval(selectedRow, selectedRow); // Chọn dòng
                    }
                }
            }
        });

        return ConnectorTablePanel;
    }

    private JPanel connectorUI() {
        JPanel ConnectorPanel = new JPanel(new BorderLayout());

//        Left side: tablePanel
        JPanel ConnectorTablePanel = createConnectorTablePanel();

//        Right side: button panel
        JPanel buttonPanel = createButtonPanel(
                e -> {
                    LOGGER.info("[CREATE IDENTITY UI] Currently adding new users is not supported.");
                    JOptionPane.showMessageDialog(MainView.this, "[CREATE IDENTITY UI] Currently adding new users is not supported", "ADD USER", JOptionPane.WARNING_MESSAGE);
                },
                e -> {
                    ConnectorModel connectorModel = null;
                    if (getSelectedRowID != null) {
                        try {
                            connectorModel = ConnectorDAO.getInstance().getConnectorByID(getSelectedRowID);
                        } catch (Exception ex) {
                            throw new RuntimeException(ex);
                        }
                    } else {
                        clearAddConnectorForm();
                    }

                    openNewWindow("Add connectorModel", connectorFormPanel(connectorModel), 1000, 900);
                    restoreConnectorTableSelection();
                },
                e -> {
                    if (getSelectedRowID != null) {
                        ConnectorModel connectorModel;
                        try {
                            connectorModel = ConnectorDAO.getInstance().getConnectorByID(getSelectedRowID);
                        } catch (Exception ex) {
                            throw new RuntimeException(ex);
                        }

                        if (connectorModel != null) {
                            openNewWindow("Edit connectorModel", editConnectorFormPanel(connectorModel), 1000, 900);
                        } else {
                            JOptionPane.showMessageDialog(null, "Failed to retrieve connectorModel details.");
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Please select a connector from the table.");
                    }
                    restoreConnectorTableSelection();
                },
                e -> {
                    if (getSelectedRowID != null) {
                        deleteConnectorFormPanel(getSelectedRowID);
                    } else {
                        JOptionPane.showMessageDialog(null, "Please select a connector from the table.");
                    }
                    restoreConnectorTableSelection();
                },
                e -> {
                    if (getSelectedRowID != null) {
                        ConnectorModel connectorModel;
                        try {
                            connectorModel = ConnectorDAO.getInstance().getConnectorByID(getSelectedRowID);
                        } catch (Exception ex) {
                            throw new RuntimeException(ex);
                        }

                        if (connectorModel != null) {
                            openNewWindow("View connectorModel", viewConnectorFormPanel(connectorModel), 1000, 900);
                        } else {
                            JOptionPane.showMessageDialog(null, "Failed to retrieve connector details.");
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Please select a connector from the table.");
                    }
                    restoreConnectorTableSelection();
                }
        );

        ConnectorPanel.add(ConnectorTablePanel, BorderLayout.CENTER);
        ConnectorPanel.add(buttonPanel, BorderLayout.EAST);

        return ConnectorPanel;
    }

    private void restoreConnectorTableSelection() {
        if (connectorSelectedRowIndex >= 0 && connectorSelectedRowIndex < ConnectorTable.getRowCount()) {
            ConnectorTable.setRowSelectionInterval(connectorSelectedRowIndex, connectorSelectedRowIndex);
        } else {
            // Reset index if the saved index is invalid
            connectorSelectedRowIndex = -1;
        }
    }

    private JPanel connectorFormPanel(ConnectorModel Connector) {
        JPanel connectorFormPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        ButtonGroup statusGroup = new ButtonGroup();
        statusGroup.add(rbEnabled);
        statusGroup.add(rbDisabled);

        gbc.gridx = 0;
        gbc.gridy = 0;
        connectorFormPanel.add(new JLabel("Name"), gbc);
        gbc.gridx = 1;
        connectorFormPanel.add(txtConnectorName, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        connectorFormPanel.add(new JLabel("Provider"), gbc);
        gbc.gridx = 1;
        connectorFormPanel.add(txtConnectorProvider, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        connectorFormPanel.add(new JLabel("Remark"), gbc);
        gbc.gridx = 1;
        connectorFormPanel.add(txtConnectorRemark, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        connectorFormPanel.add(new JLabel("Remark"), gbc);
        gbc.gridx = 1;
        connectorFormPanel.add(txtConnectorRemarkEn, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        connectorFormPanel.add(new JLabel("Callback URL"), gbc);
        gbc.gridx = 1;
        connectorFormPanel.add(txtConnectorCallBackURL, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 1;
        connectorFormPanel.add(new JLabel("Properties"), gbc);
        gbc.gridx = 1;
        connectorFormPanel.add(scrollProperties, gbc);

        gbc.gridx = 0;
        gbc.gridy = 8;
        connectorFormPanel.add(new JLabel("Created By"), gbc);
        gbc.gridx = 1;
        connectorFormPanel.add(txtConnectorCreatedBy, gbc);

        gbc.gridx = 0;
        gbc.gridy = 9;
        gbc.gridwidth = 4;
        connectorFormPanel.add(btnAddConnectorSubmit, gbc);

        ConnectorController Ccontroller = new ConnectorController(MainView.this);
        if (Connector != null) {
            connectorFormPanel.revalidate();
            connectorFormPanel.repaint();
            Ccontroller.fillConnectorPanel(Connector, lblConnectorLogoThumbnail);
        }

        btnAddConnectorSubmit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Ccontroller.addConnector();
                try {
                    Ccontroller.refreshConnectorTable();
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        return connectorFormPanel;
    }

    private JPanel editConnectorFormPanel(ConnectorModel Connector) {
        JPanel connectorFormPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        ButtonGroup statusGroup = new ButtonGroup();
        statusGroup.add(rbEnabled);
        statusGroup.add(rbDisabled);

        gbc.gridx = 0;
        gbc.gridy = 0;
        connectorFormPanel.add(new JLabel("Name"), gbc);
        gbc.gridx = 1;
        connectorFormPanel.add(txtConnectorName, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        connectorFormPanel.add(new JLabel("Provider"), gbc);
        gbc.gridx = 1;
        connectorFormPanel.add(txtConnectorProvider, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        connectorFormPanel.add(new JLabel("Remark"), gbc);
        gbc.gridx = 1;
        connectorFormPanel.add(txtConnectorRemark, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        connectorFormPanel.add(new JLabel("Remark"), gbc);
        gbc.gridx = 1;
        connectorFormPanel.add(txtConnectorRemarkEn, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        connectorFormPanel.add(new JLabel("Callback URL"), gbc);
        gbc.gridx = 1;
        connectorFormPanel.add(txtConnectorCallBackURL, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 1;
        connectorFormPanel.add(new JLabel("Properties"), gbc);
        gbc.gridx = 1;
        connectorFormPanel.add(scrollProperties, gbc);

        gbc.gridx = 0;
        gbc.gridy = 8;
        connectorFormPanel.add(new JLabel("Created By"), gbc);
        gbc.gridx = 1;
        connectorFormPanel.add(txtConnectorCreatedBy, gbc);

        gbc.gridx = 0;
        gbc.gridy = 9;
        gbc.gridwidth = 4;
        connectorFormPanel.add(btnAddConnectorSubmit, gbc);

        ConnectorController Ccontroller = new ConnectorController(MainView.this);
        if (Connector != null) {
            connectorFormPanel.revalidate();
            connectorFormPanel.repaint();
            Ccontroller.fillConnectorPanel(Connector, lblConnectorLogoImage);
        }

        btnConnectorLogo.addActionListener(e -> {
            chooseLogo(lblConnectorLogoThumbnail);
            lblConnectorLogoThumbnail.repaint();
        });

        btnAddConnectorSubmit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Ccontroller.editController();
                try {
                    Ccontroller.refreshConnectorTable();
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        return connectorFormPanel;
    }

    private void deleteConnectorFormPanel(Long connectorID) {
        // Hiển thị hộp thoại xác nhận
        int confirmation = JOptionPane.showConfirmDialog(
                null,
                "Are you sure you want to delete?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION
        );

        if (confirmation == JOptionPane.YES_OPTION) {
            // Nếu người dùng chọn Yes, thực hiện xóa
            ConnectorController Ccontroller = new ConnectorController(MainView.this);
            Ccontroller.deleteConnector(connectorID);
        } else {
            // Nếu người dùng chọn No, không làm gì
            JOptionPane.showMessageDialog(null, "Delete cancelled.");
        }
    }

    private JPanel viewConnectorFormPanel(ConnectorModel Connector) {
        JPanel connectorFormPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        ButtonGroup statusGroup = new ButtonGroup();
        statusGroup.add(rbEnabled);
        statusGroup.add(rbDisabled);

        gbc.gridx = 0;
        gbc.gridy = 0;
        connectorFormPanel.add(new JLabel("Name"), gbc);
        gbc.gridx = 1;
        connectorFormPanel.add(txtConnectorName, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        connectorFormPanel.add(new JLabel("Provider"), gbc);
        gbc.gridx = 1;
        connectorFormPanel.add(txtConnectorProvider, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        connectorFormPanel.add(new JLabel("Remark"), gbc);
        gbc.gridx = 1;
        connectorFormPanel.add(txtConnectorRemark, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        connectorFormPanel.add(new JLabel("Remark"), gbc);
        gbc.gridx = 1;
        connectorFormPanel.add(txtConnectorRemarkEn, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        connectorFormPanel.add(new JLabel("Callback URL"), gbc);
        gbc.gridx = 1;
        connectorFormPanel.add(txtConnectorCallBackURL, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 1;
        connectorFormPanel.add(new JLabel("Properties"), gbc);
        gbc.gridx = 1;
        connectorFormPanel.add(scrollProperties, gbc);

        gbc.gridx = 0;
        gbc.gridy = 8;
        connectorFormPanel.add(new JLabel("Created By"), gbc);
        gbc.gridx = 1;
        connectorFormPanel.add(txtConnectorCreatedBy, gbc);

        ConnectorController Ccontroller = new ConnectorController(MainView.this);
        if (Connector != null) {
            connectorFormPanel.revalidate();
            connectorFormPanel.repaint();
            Ccontroller.fillConnectorPanel(Connector, lblConnectorLogoImage);
        }

        return connectorFormPanel;
    }

    private void clearAddConnectorForm() {
        txtConnectorName.setText("");
        txtConnectorProvider.setText("");
        txtConnectorCallBackURL.setText("");
        txtConnectorRemark.setText("");
        lblConnectorLogoThumbnail.setIcon(null);
        rbEnabled.setSelected(false);
        rbDisabled.setSelected(false);
        txtConnectorCreatedBy.setText("");
    }

    // ----------------------------------------------------------------------------------------------
    private int identityConnectorSelectedRowIndex = -1; // Lưu chỉ số dòng được chọn

    private JPanel iConnectorTablePanel() {
        String[] columnNames = {"ID", "ENABLED", "IDENTITY ID", "CONNECTOR ID", "DESCRIPTION", "HMAC", "CREATED BY", "CREATED AT", "MODIFIED BY", "MODIFIED AT"};
        iConnectorTableModel = new DefaultTableModel(columnNames, 0);
        identityConnectorTable = new JTable(iConnectorTableModel) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        identityConnectorTable.getColumnModel().getColumn(6).setPreferredWidth(30);

        JScrollPane ScrollPane = new JScrollPane(identityConnectorTable);

        // Thêm JTextField để lọc
        JTextField filterTextField = new JTextField();
        filterTextField.setToolTipText("Enter text to filter");

        // Thêm nút Clear Filter
        JButton clearButton = new JButton("Clear");

        // Tạo TableRowSorter để áp dụng bộ lọc
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(iConnectorTableModel);
        identityConnectorTable.setRowSorter(sorter);

        // Lắng nghe sự kiện nhập vào JTextField
        filterTextField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                applyFilter();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                applyFilter();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                applyFilter();
            }

            private void applyFilter() {
                String text = filterTextField.getText();
                if (text.trim().length() == 0) {
                    sorter.setRowFilter(null); // Không áp dụng bộ lọc nếu không có dữ liệu
                } else {
                    sorter.setRowFilter(RowFilter.regexFilter("(?i)" + text)); // Bộ lọc không phân biệt chữ hoa chữ thường
                }
            }
        });

        // Lắng nghe sự kiện nút Clear Filter
        clearButton.addActionListener(e -> {
            filterTextField.setText("");
            sorter.setRowFilter(null); // Loại bỏ bộ lọc
        });

        // Panel chứa JTextField, nút Clear và bảng
        JPanel tablePanel = new JPanel(new BorderLayout());
        JPanel filterPanel = new JPanel(new BorderLayout());
        filterPanel.add(new JLabel("Filter: "), BorderLayout.WEST);
        filterPanel.add(filterTextField, BorderLayout.CENTER);
        filterPanel.add(clearButton, BorderLayout.EAST);

        tablePanel.add(filterPanel, BorderLayout.NORTH);
        tablePanel.add(ScrollPane, BorderLayout.CENTER);

        identityConnectorTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = identityConnectorTable.getSelectedRow();

                if (selectedRow != -1) {
                    if (selectedRow == identityConnectorSelectedRowIndex) {
                        identityConnectorTable.clearSelection(); // Bỏ chọn dòng
                        identityConnectorSelectedRowIndex = -1; // Reset trạng thái
                        getSelectedRowID = null; // Đặt lại giá trị nếu cần
                    } else {
                        identityConnectorSelectedRowIndex = selectedRow; // Cập nhật dòng hiện tại
                        getSelectedRowID = (Long) iConnectorTableModel.getValueAt(selectedRow, 0);
                        identityConnectorTable.setRowSelectionInterval(selectedRow, selectedRow); // Chọn dòng
                    }
                }
            }
        });

        return tablePanel;
    }

    private JPanel createIConnectorTablePanel() {
        // org.toolMicroservices.Main container panel with BorderLayout
        JPanel identityConnectorPanel = new JPanel(new BorderLayout());

        // Left side: tablePanel----------------------------
        JPanel identityConnectorTablePanel = iConnectorTablePanel();
        identityConnectorPanel.add(identityConnectorTablePanel, BorderLayout.CENTER);

        // Right side: button panel-------------------------
        JPanel buttonPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Padding for buttons
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JButton btnAdd = new JButton("Add connector");
        JButton btnDelete = new JButton("Delete");

        gbc.gridx = 0;
        gbc.gridy = 0;
        buttonPanel.add(btnAdd, gbc);

        gbc.gridy = 1;
        buttonPanel.add(btnDelete, gbc);

        btnAdd.addActionListener(e -> {
            openNewWindow("Add Connector", selectConnectorPanel(), 700, 900);
            restoreConnectorTableSelection();
        });

        btnDelete.addActionListener(e -> {
            if (getSelectedRowID != null) {
                deleteIdentityConnectorFormPanel(getSelectedRowID);
            } else {
                JOptionPane.showMessageDialog(null, "Please select a connector from the table.");
            }
            restoreConnectorTableSelection();
        });

        identityConnectorPanel.add(buttonPanel, BorderLayout.EAST);

        return identityConnectorPanel;
    }

    private void populateConnectorNameComboBox() {
        try {
            connectorName.removeAllItems();

            Map<String, ConnectorModel> connectorMap = ConnectorDAO.getInstance().getConnectorList();

            // Populate the combo box with connector names
            for (String name : connectorMap.keySet()) {
                connectorName.addItem(name);
            }

            // Add action listener to handle selection events
            connectorName.addActionListener(e -> {
                if (connectorName.getSelectedIndex() != -1) {
                    String name = connectorName.getSelectedItem().toString();
                    ConnectorModel selectedConnector = connectorMap.get(name);

                    if (selectedConnector != null) {
                        String provider = selectedConnector.getProvider();
                        String callbackurl = selectedConnector.getCallbackUrl();
                        String remark = selectedConnector.getRemark();

                        // Cập nhật các trường
                        connectorProvider.setText(provider);
                        connectorCallBackURL.setText(callbackurl);
                        connectorRemark.setText(remark);

                        // Gọi phương thức updateLogo để cập nhật hình ảnh
                        updateLogo(lblLogoImage, selectControlerLogo);
                    }

                    // Ẩn/hiện scrollSecretKey dựa trên lựa chọn của connectorName
                    if ("ZNS_MOBILE_ID".equals(name)) {
                        scrollSecretKey.setVisible(true);
                    } else {
                        scrollSecretKey.setVisible(false);
                    }

                    // Làm mới giao diện để thay đổi hiển thị
                    scrollSecretKey.getParent().revalidate();
                    scrollSecretKey.getParent().repaint();
                }
            });
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error loading Connector: " + e.getMessage());
        }
    }

    private JPanel selectConnectorPanel() {
        JPanel selectConnectorPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Custom font for labels
        Font labelFont = new Font("Arial", Font.BOLD, 12);

        // ComboBox
        populateConnectorNameComboBox();

        JLabel lblIdentityName = new JLabel("Identity Name:");
        lblIdentityName.setFont(labelFont);
        lblIdentityName.setPreferredSize(new Dimension(100, 20));
        gbc.gridx = 0;
        gbc.gridy = 0;
        selectConnectorPanel.add(lblIdentityName, gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 2;
        selectConnectorPanel.add(identityName, gbc);
        gbc.gridwidth = 1;

        JLabel lblName = new JLabel("Name:");
        lblName.setFont(labelFont);
        lblName.setPreferredSize(new Dimension(100, 20));
        gbc.gridx = 0;
        gbc.gridy = 1;
        selectConnectorPanel.add(lblName, gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 2;
        selectConnectorPanel.add(connectorName, gbc);
        gbc.gridwidth = 1;

        JLabel lblProvider = new JLabel("Provider:");
        lblProvider.setFont(labelFont);
        gbc.gridx = 0;
        gbc.gridy = 2;
        selectConnectorPanel.add(lblProvider, gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 2;
        selectConnectorPanel.add(connectorProvider, gbc);
        connectorProvider.setEditable(false);

        JLabel lblCallBackURL = new JLabel("Callback URL:");
        lblCallBackURL.setFont(labelFont);
        gbc.gridx = 0;
        gbc.gridy = 3;
        selectConnectorPanel.add(lblCallBackURL, gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 2;
        selectConnectorPanel.add(connectorCallBackURL, gbc);
        connectorCallBackURL.setEditable(false);

        JLabel lblRemark = new JLabel("Remark:");
        lblRemark.setFont(labelFont);
        gbc.gridx = 0;
        gbc.gridy = 4;
        selectConnectorPanel.add(lblRemark, gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 2;
        selectConnectorPanel.add(connectorRemark, gbc);
        connectorRemark.setEditable(false);

        JLabel lblOfficeAccountID = new JLabel("Office Account ID:");
        lblOfficeAccountID.setFont(labelFont);
        gbc.gridx = 0;
        gbc.gridy = 5;
        selectConnectorPanel.add(lblOfficeAccountID, gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 2;
        selectConnectorPanel.add(connectorOfficeAccountID, gbc);
        connectorOfficeAccountID.setEditable(false);

        JLabel lblLogo = new JLabel("Logo:");
        lblLogo.setFont(labelFont);
        gbc.gridx = 0;
        gbc.gridy = 6;
        selectConnectorPanel.add(lblLogo, gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 2;
        lblLogoImage = new JLabel("Logo not available");
        lblLogoImage.setFont(new Font("Arial", Font.ITALIC, 12));
        lblLogoImage.setHorizontalAlignment(SwingConstants.CENTER);
        lblLogoImage.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        lblLogoImage.setOpaque(true);
        lblLogoImage.setBackground(Color.WHITE);
        selectConnectorPanel.add(lblLogoImage, gbc);

        JLabel lblSecretKey = new JLabel("Secret Key:");
        lblSecretKey.setFont(labelFont);
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 1;
        selectConnectorPanel.add(lblSecretKey, gbc);
        lblSecretKey.setVisible(false); // Ẩn mặc định

        gbc.gridx = 1;
        gbc.gridwidth = 2;
        selectConnectorPanel.add(scrollSecretKey, gbc);
        scrollSecretKey.setVisible(false); // Ẩn mặc định

        // Buttons
        JButton btnAddConnector = new JButton("Add new Connector");
        gbc.gridx = 1;
        gbc.gridy = 8;
        selectConnectorPanel.add(btnAddConnector, gbc);

        btnAddConnector.addActionListener(e -> {
            ConnectorModel connector = null;
            openNewWindow("Add new Connector", connectorFormPanel(connector), 1000, 900);
        });

        return selectConnectorPanel;
    }

    private void deleteIdentityConnectorFormPanel(Long icID) {
        // Hiển thị hộp thoại xác nhận
        int confirmation = JOptionPane.showConfirmDialog(
                null,
                "Are you sure you want to delete?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION
        );

        if (confirmation == JOptionPane.YES_OPTION) {
            // Nếu người dùng chọn Yes, thực hiện xóa
            IdentityConnectorController icController = new IdentityConnectorController(MainView.this);
            icController.DeleteIConnector(icID);
        } else {
            // Nếu người dùng chọn No, không làm gì
            JOptionPane.showMessageDialog(null, "Delete cancelled.");
        }
    }

    // ----------------------------------------------------------------------------------------------
    private JPanel createButtonPanel(ActionListener addUserAction, ActionListener addAction, ActionListener editAction, ActionListener deleteAction, ActionListener detailAction) {
        JPanel buttonPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Padding for buttons
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JButton btnAddUser = new JButton("ADD USER");
        JButton btnAddIdentity = new JButton("ADD IDENTITY");
        JButton btnEditIdentity = new JButton("EDIT IDENTITY");
        JButton btnRemoveIdentity = new JButton("REMOVE IDENTITY");
        JButton btnDetailIdentity = new JButton("DETAIL IDENTITY");

        btnAddUser.addActionListener(addUserAction);
        btnAddIdentity.addActionListener(addAction);
        btnEditIdentity.addActionListener(editAction);
        btnRemoveIdentity.addActionListener(deleteAction);
        btnDetailIdentity.addActionListener(detailAction);

        gbc.gridx = 0;
        gbc.gridy = 0;
        buttonPanel.add(btnAddUser, gbc);

        gbc.gridy = 1;
        buttonPanel.add(btnAddIdentity, gbc);

        gbc.gridy = 2;
        buttonPanel.add(btnEditIdentity, gbc);

        gbc.gridy = 3;
        buttonPanel.add(btnRemoveIdentity, gbc);

        gbc.gridy = 4;
        buttonPanel.add(btnDetailIdentity, gbc);

        return buttonPanel;
    }

    // Helper method to open a new window
    private void openNewWindow(String title, JPanel formPanel, int width, int height) {
        JFrame newFrame = new JFrame(title);
        newFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        newFrame.setLocationRelativeTo(null);
        newFrame.setSize(width, height);
        newFrame.add(formPanel);
        newFrame.setVisible(true);
    }

    private void chooseLogo(JLabel lblLogoThumbnail) {
        // Ensure the file chooser is not repeatedly initialized
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setDialogTitle("Select Logo Image");

        // Set file filter for image types
        fileChooser.setFileFilter(new FileNameExtensionFilter("Image Files", "jpg", "jpeg", "png"));

        int result = fileChooser.showOpenDialog(null);

        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            String logoPath = selectedFile.getAbsolutePath();

            lblLogoPath.setText(logoPath); // Display the full path if needed
            lblConnectorLogoPath.setText(logoPath);

            try {
                BufferedImage logoImage = ImageIO.read(selectedFile);

                if (logoImage != null) {
                    // Get original dimensions
                    int originalWidth = logoImage.getWidth();
                    int originalHeight = logoImage.getHeight();

                    // Desired thumbnail size
                    int thumbnailWidth = 100;
                    int thumbnailHeight = 100;

                    // Calculate the scaling factor to preserve aspect ratio
                    double scaleWidth = (double) thumbnailWidth / originalWidth;
                    double scaleHeight = (double) thumbnailHeight / originalHeight;
                    double scaleFactor = Math.min(scaleWidth, scaleHeight);

                    // Compute new dimensions while keeping aspect ratio
                    int newWidth = (int) (originalWidth * scaleFactor);
                    int newHeight = (int) (originalHeight * scaleFactor);

                    // Scale the image
                    Image scaledImage = logoImage.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);

                    // Set the scaled image as the thumbnail
                    lblLogoThumbnail.setIcon(new ImageIcon(scaledImage));
                } else {
                    JOptionPane.showMessageDialog(null, "Unsupported image format or error reading the image.");
                }
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Error loading image: " + ex.getMessage());
            }
        }
    }

    // audit Panel -----------------------------------------------------------------------------------------------------
    private JPanel createAuditLogPanel() {
        JPanel auditLogPanel = new JPanel(new BorderLayout());

        JPanel auditLogTablePanel = createAuditLogTablePanel();
        auditLogPanel.add(auditLogTablePanel, BorderLayout.CENTER);

        return auditLogPanel;
    }

    private JPanel createAuditLogTablePanel() {
        String[] columnNames = {"ID", "STATUS", "URI", "REQUEST_IP", "REQUEST", "RESPONSE", "HMAC", "CREATE BY", "CREATE AT"};
        auditLogTableModel = new DefaultTableModel(columnNames, 0);
        auditLogTable = new JTable(auditLogTableModel) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JScrollPane scrollPane = new JScrollPane(auditLogTable);
        JPanel auditLogTablePanel = new JPanel(new BorderLayout());
        auditLogTablePanel.add(new JLabel("Audit Log", JLabel.CENTER), BorderLayout.NORTH);
        auditLogTablePanel.add(scrollPane, BorderLayout.CENTER);

        return auditLogTablePanel;
    }

    //ETC....
    public void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }


}
