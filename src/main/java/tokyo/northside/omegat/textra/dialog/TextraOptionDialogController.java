package tokyo.northside.omegat.textra.dialog;

import org.omegat.util.gui.DelegatingComboBoxRenderer;

import tokyo.northside.omegat.textra.TextraApiClient;
import tokyo.northside.omegat.textra.TextraOptions;
import tokyo.northside.omegat.textra.TextraOptionsFactory;

import java.awt.Color;
import java.awt.Window;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComponent;
import javax.swing.KeyStroke;

import org.apache.commons.lang3.StringUtils;

import static javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE;
import static tokyo.northside.omegat.textra.BundleMessageUtil.getString;

public class TextraOptionDialogController {

    private static final String CUSTOM = "custom";
    private static final String CUSTOM_ID_HINT_KEY = "CustomIdHint";
    private static final String API_KEY_HINT_KEY = "ApiKeyHint";
    private static final String API_SECRET_HINT_KEY = "ApiSecretHint";
    private TextraOptionsFactory factory;

    public TextraOptionDialogController() {}

    public void show(Window parent, TextraOptionsFactory textraOptionsFactory, TextraOptions options) {
        TextraOptionDialog dialog = new TextraOptionDialog(parent);
        dialog.setModal(true);
        setOptions(dialog, options);
        dialog.getRootPane().setDefaultButton(dialog.buttonOK);
        GhostTextHandler.register(dialog.customIdTextField, getString(CUSTOM_ID_HINT_KEY));
        GhostTextHandler.register(dialog.apikeyField, getString(API_KEY_HINT_KEY));
        GhostTextHandler.register(dialog.secretField, getString(API_SECRET_HINT_KEY));
        factory = textraOptionsFactory;

        dialog.providerComboBox.setModel(new DefaultComboBoxModel<>(textraOptionsFactory.getServices()));
        dialog.providerComboBox.setRenderer(new DelegatingComboBoxRenderer<String, String>() {
            @Override
            protected String getDisplayText(String s) {
                return textraOptionsFactory.getName(s);
            }
        });
        String defaultProvider = textraOptionsFactory.getServices()[0];
        dialog.modeComboBox.setModel(new DefaultComboBoxModel<>(textraOptionsFactory.getModes(defaultProvider)));
        dialog.modeComboBox.setRenderer(new DelegatingComboBoxRenderer<String, String>() {
            @Override
            protected String getDisplayText(String s) {
                return textraOptionsFactory.getName(s);
            }
        });
        dialog.providerComboBox.setSelectedItem(defaultProvider);
        dialog.providerComboBox.addActionListener(itemEvent -> {
            if (itemEvent == null) {
                return;
            }
            String v = (String) dialog.providerComboBox.getSelectedItem();
            if (v != null) {
                dialog.modeComboBox.setModel(new DefaultComboBoxModel<>(textraOptionsFactory.getModes(v)));
            }
        });

        dialog.buttonOK.addActionListener(e -> {
            onOK(dialog, options);
            dialog.dispose();
        });

        dialog.buttonCancel.addActionListener(e -> {
            setOptions(dialog, options);
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
                setOptions(dialog, options);
                dialog.dispose();
            }
        });

        // call onCancel() on ESCAPE
        dialog.contentPane.registerKeyboardAction(
                e -> setOptions(dialog, options),
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
    private void setOptions(TextraOptionDialog dialog, final TextraOptions data) {
        dialog.usernameField.setText(data.getUsername());
        dialog.apikeyField.setText(data.getApikey());
        dialog.secretField.setText(data.getSecret());
        String custom = data.getCustomId();
        if (custom != null) {
            dialog.customIdTextField.setText(custom);
        }
        dialog.providerComboBox.setSelectedItem(data.getProvider());
        dialog.modeComboBox.setSelectedItem(data.getMode());
    }

    private void onOK(TextraOptionDialog dialog, final TextraOptions options) {
        String provider = (String) dialog.providerComboBox.getSelectedItem();
        options.setProvider(provider);
        options.setUsername(dialog.usernameField.getText());
        options.setApikey(dialog.apikeyField.getText());
        options.setSecret(dialog.secretField.getText());
        String mode = (String) dialog.modeComboBox.getSelectedItem();
        options.setMode(mode);
        if (CUSTOM.equals(mode)) {
            String enteredCustomId = dialog.customIdTextField.getText();
            options.setCustomId(enteredCustomId);
        }
        options.saveCredentials();
    }

    private String getAuthUrl(TextraOptionDialog dialog) {
        return getBaseUrl(dialog) + "/oauth2/token.php";
    }

    private String getBaseUrl(TextraOptionDialog dialog) {
        return factory.getURL((String) dialog.providerComboBox.getSelectedItem());
    }
}
