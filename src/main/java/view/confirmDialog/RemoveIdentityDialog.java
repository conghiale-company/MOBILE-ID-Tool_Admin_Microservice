package view.confirmDialog;

/**
 * Project: ZNS-Maven
 * Created by Cong Nghia le
 * Date: 2025/01/10
 * Time: 4:45 PM
 */

import controller.IdentityController;
import org.apache.log4j.Logger;
import view.MainView;

import javax.swing.*;

/**
 * @ 2025 Conghiale. All rights reserved
 */

public class RemoveIdentityDialog {
    private static final Logger LOGGER = Logger.getLogger(RemoveIdentityDialog.class);

    /**
     * Displays a confirmation dialog to confirm the removal of an Identity.
     * If the user confirms the removal, it attempts to delete the Identity and provides feedback
     * on whether the operation was successful or failed.
     * If the deletion is successful, the parent view is updated with the changes.
     * In case of any errors, an error message is shown.
     *
     * @param id The unique identifier of the Identity to be deleted.
     * @param enable The status of the Identity (e.g., active or inactive).
     * @param modifiedBy The username or identifier of the person who initiated the removal.
     * @param parent The parent JFrame to center the confirmation dialog.
     * @throws Exception If an error occurs during the deletion process.
     */
    public static void removeIdentityDialog(long id, int enable, String modifiedBy, JFrame parent) throws Exception {
//        Show confirmation dialog
        int confirmation = JOptionPane.showConfirmDialog(
                parent,
                "Are you sure you want to delete the IDENTITY with ID " + id + "?",
                "CONFIRM IDENTITY REMOVE",
                JOptionPane.YES_NO_OPTION
        );

        if (confirmation == JOptionPane.YES_OPTION) {
//            If the user selects Yes -> REMOVE
            String responseCode = IdentityController.deleteIdentity(id, enable, modifiedBy);

            try {
                if (responseCode.equals("0")) {
                    LOGGER.info("[REMOVE IDENTITY] Identity with ID " + id + " has been removed.");
                    JOptionPane.showMessageDialog(parent, "Remove IDENTITY successfully with ID " + id, "REMOVE IDENTITY", JOptionPane.INFORMATION_MESSAGE );
                    if (parent instanceof MainView) {
                        ((MainView) parent).loadAllIdentity();
                        ((MainView) parent).restoreTableSelectionIdentity(true);
                    }
                } else {
                    LOGGER.warn("[REMOVE IDENTITY] IDENTITY with ID " + id + " not found or this IDENTITY has been removed");
                    LOGGER.warn("[REMOVE IDENTITY] responseCode: " + responseCode);
                    JOptionPane.showMessageDialog(parent, "Remove IDENTITY failed with ID " + id, "REMOVE IDENTITY", JOptionPane.WARNING_MESSAGE );
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(parent, "An error occurred while removing IDENTITY with ID " + id, "REMOVE IDENTITY", JOptionPane.ERROR_MESSAGE );
            }
        } else {
//            If the user selects No -> do nothing.
            LOGGER.warn("[REMOVE IDENTITY] User did not confirm deletion of IDENTITY with ID " + id);
        }
    }
}
