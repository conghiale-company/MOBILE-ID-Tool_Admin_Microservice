package org.toolMicroservices;

import controller.*;
import view.MainView;
import view.modal.ConfigInfo;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            MainView mainView = new MainView();

            try {
                mainView.loadAllIdentity();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            IdentityController controller = new IdentityController(mainView);
            ConnectorController connectorController = new ConnectorController(mainView);
            IdentityConnectorController identity_ConnectorController = new IdentityConnectorController(mainView);
            AuditLogController auditLogController = new AuditLogController(mainView);

            mainView.setLocationRelativeTo(null);
            mainView.setVisible(true);

//            Demo mainView = new Demo();
//            mainView.createUIComponents();
        });
    }
}
