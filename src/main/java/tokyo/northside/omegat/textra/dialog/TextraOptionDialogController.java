package tokyo.northside.omegat.textra.dialog;

import org.apache.commons.lang.StringUtils;
import tokyo.northside.omegat.textra.TextraApiClient;
import tokyo.northside.omegat.textra.TextraOptions;

import javax.swing.JComponent;
import javax.swing.KeyStroke;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import static javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE;
import static tokyo.northside.omegat.textra.StringUtil.getString;

public class TextraOptionDialogController {

    private TextraOptionDialogController() {
    }

    public static void show(final TextraOptions data) {
        TextraOptionDialog dialog = new TextraOptionDialog();
        dialog.setModal(true);
        setOptions(dialog, data);
        dialog.getRootPane().setDefaultButton(dialog.buttonOK);
        GhostTextHandler.register(dialog.customIdTextField, getString("CustomIdHint"));
        GhostTextHandler.register(dialog.apikeyField, getString("ApiKeyHint"));
        GhostTextHandler.register(dialog.secretField, getString("ApiSecretHint"));

        dialog.buttonOK.addActionListener(e -> {
            onOK(dialog, data);
            dialog.dispose();
        });

        dialog.buttonCancel.addActionListener(e -> {
            setOptions(dialog, data);
            dialog.dispose();
        });

        dialog.connectionTestButton.addActionListener(e -> {
            String apikey = dialog.apikeyField.getText();
            String apiSecret = dialog.secretField.getText();
            if (StringUtils.isBlank(apikey)) {
                return;
            }
            if (StringUtils.isBlank(apiSecret)) {
                return;
            }
            if (TextraApiClient.checkAuth(getAuthUrl(dialog), apikey, apiSecret)) {
                dialog.connectionStatus.setText(getString("ConnectionStatusOk"));
                dialog.connectionStatus.setForeground(Color.GREEN);
            } else {
                dialog.connectionStatus.setText(getString("ConnectionStatusNG"));
                dialog.connectionStatus.setForeground(Color.RED);
            }
        });

        // call onCancel() when cross is clicked
        dialog.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        dialog.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                setOptions(dialog, data);
                dialog.dispose();
            }
        });

        // call onCancel() on ESCAPE
        dialog.contentPane.registerKeyboardAction(e -> {
                    setOptions(dialog, data);
                },
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        dialog.pack();
        dialog.setVisible(true);
    }

    /**
     * Set Dialog options.
     *
     * @param data option data.
     */
    private static void setOptions(TextraOptionDialog dialog, final TextraOptions data) {
        switch (data.getProvider()) {
            case nict:
                dialog.nictRadioButton.setSelected(true);
                break;
            case minna_personal:
                dialog.kiRadioButton.setSelected(true);
                break;
            default:
                dialog.nictRadioButton.setSelected(true);
                break;
        }
        dialog.usernameField.setText(data.getUsername());
        dialog.apikeyField.setText(data.getApikey());
        dialog.secretField.setText(data.getSecret());
        String custom = data.getCustomId();
        if (custom != null) {
            dialog.customIdTextField.setText(custom);
        }
        switch (data.getMode()) {
            case generalNT:
                dialog.generalNTModeRadioButton.setSelected(true);
                break;
            case minnaNT:
                dialog.minnaNTModeRadioButton.setSelected(true);
                break;
            case patentNT:
                dialog.patentNTModeRadioButton.setSelected(true);
                break;
            case voicetraNT:
                dialog.voiceTraTaiwaNTModeRadioButton.setSelected(true);
                break;
            case fsaNT:
                dialog.financeNTModeRadioButton.setSelected(true);
                break;
            case custom:
                dialog.customRadioButton.setSelected(true);
                break;
            default:
                dialog.generalNTModeRadioButton.setSelected(true);
                break;
        }
    }

    private static void onOK(TextraOptionDialog dialog, final TextraOptions options) {
        if (dialog.nictRadioButton.isSelected()) {
            options.setProvider(TextraOptions.Provider.nict);
        } else if (dialog.kiRadioButton.isSelected()) {
            options.setProvider(TextraOptions.Provider.minna_personal);
        } else {
            options.setProvider(TextraOptions.Provider.nict);
        }
        options.setUsername(dialog.usernameField.getText());
        options.setApikey(dialog.apikeyField.getText());
        options.setSecret(dialog.secretField.getText());
        if (dialog.generalNTModeRadioButton.isSelected()) {
            options.setMode(TextraOptions.Mode.generalNT);
        } else if (dialog.minnaNTModeRadioButton.isSelected()) {
            options.setMode(TextraOptions.Mode.minnaNT);
        } else if (dialog.patentNTModeRadioButton.isSelected()) {
            options.setMode(TextraOptions.Mode.patentNT);
        } else if (dialog.financeNTModeRadioButton.isSelected()) {
            options.setMode(TextraOptions.Mode.fsaNT);
        } else if (dialog.voiceTraTaiwaNTModeRadioButton.isSelected()) {
            options.setMode(TextraOptions.Mode.voicetraNT);
        } else if (dialog.customRadioButton.isSelected()) {
            String enteredCustomId = dialog.customIdTextField.getText();
            options.setMode(TextraOptions.Mode.custom);
            options.setCustomId(enteredCustomId);
        } else {
            options.setMode(TextraOptions.Mode.generalNT);
        }
        options.saveCredentials();
    }

    private static String getAuthUrl(TextraOptionDialog dialog) {
        return getBaseUrl(dialog) + "/oauth2/token.php";
    }

    private static String getBaseUrl(TextraOptionDialog dialog) {
        if (dialog.nictRadioButton.isSelected()) {
            return TextraApiClient.BASE_URL;
        } else if (dialog.kiRadioButton.isSelected()) {
            return TextraApiClient.KI_BASE_URL;
        } else {
            return TextraApiClient.BASE_URL;
        }
    }
}
