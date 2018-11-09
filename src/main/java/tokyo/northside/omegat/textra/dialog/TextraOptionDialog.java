package tokyo.northside.omegat.textra.dialog;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import tokyo.northside.omegat.textra.TextraOptions;
import tokyo.northside.omegat.textra.WebBrowser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ResourceBundle;


/**
 * Dialog for configure TexTra options.
 *
 * @author Hiroshi Miura
 */
public class TextraOptionDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JLabel userNameLabel;
    private JLabel secretLabel;
    private JLabel apiKeyLabel;
    private JTextField userNameTextField;
    private JTextField apikeyTextField;
    private JTextField secretTextField;
    private JRadioButton generalModeRadioButton;
    private JRadioButton patentModeRadioButton;
    private JRadioButton patentOrderModeRadioButton;
    private JButton registerNewTexTraUserButton;
    private JButton checkTexTraAPIKeyButton;
    private ButtonGroup modeButtonGroup;

    private static final String REGISTRATION_URL = "https://mt-auto-minhon-mlt.ucri.jgn-x.jp/content/register/";
    private static final String API_KEY_URL = "https://mt-auto-minhon-mlt.ucri.jgn-x.jp/content/mt/";

    private boolean updated;

    /**
     * Dialog constructor.
     */
    public TextraOptionDialog() {
        updated = false;
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        buttonOK.addActionListener(e -> onOK());
        buttonCancel.addActionListener(e -> onCancel());
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        registerNewTexTraUserButton.addActionListener(actionEvent ->  WebBrowser.launch(REGISTRATION_URL));
        checkTexTraAPIKeyButton.addActionListener(actionEvent -> WebBrowser.launch(API_KEY_URL));
    }

    private void onOK() {
        updated = true;
        dispose();
    }

    private void onCancel() {
        dispose();
    }

    /**
     * Setter for Dialog data.
     *
     * @param data dialog data.
     */
    public void setData(final TextraOptions data) {
        userNameTextField.setText(data.getUsername());
        apikeyTextField.setText(data.getApikey());
        secretTextField.setText(data.getSecret());
        switch (data.getMode()) {
            case generalN:
                generalModeRadioButton.setSelected(true);
                break;
            case patentN:
                patentModeRadioButton.setSelected(true);
                break;
            case patent_claimN:
                patentOrderModeRadioButton.setSelected(true);
                break;
            default:
                generalModeRadioButton.setSelected(true);
                break;
        }
    }

    /**
     * Getter for dialog data.
     *
     * @param data dialog data.
     */
    public void getData(final TextraOptions data) {
        data.setUsername(userNameTextField.getText());
        data.setApikey(apikeyTextField.getText());
        data.setSecret(secretTextField.getText());
        data.setMode(modeButtonGroup.getSelection().getActionCommand());
    }

    /**
     * Is dialog options changed?
     *
     * @param data previous data.
     * @return true if user modified values.
     */
    public boolean isModified(final TextraOptions data) {
        if (!updated) {
            return false;
        }
        if (userNameTextField.getText() != null ? !userNameTextField.getText().equals(data.getUsername()) : data.getUsername() != null)
            return true;
        if (apikeyTextField.getText() != null ? !apikeyTextField.getText().equals(data.getApikey()) : data.getApikey() != null)
            return true;
        if (secretTextField.getText() != null ? !secretTextField.getText().equals(data.getSecret()) : data.getSecret() != null)
            return true;
        if (!data.isMode(modeButtonGroup.getSelection().getActionCommand())) {
            return true;
        }
        return false;
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }

    /**
     * Only for test purpose.
     *
     * @param args command line arguments.
     */
    public static void main(String[] args) {
        TextraOptionDialog dialog = new TextraOptionDialog();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        contentPane = new JPanel();
        contentPane.setLayout(new GridLayoutManager(3, 1, new Insets(10, 10, 10, 10), -1, -1));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.add(panel1, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, 1, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        panel1.add(spacer1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1, true, false));
        panel1.add(panel2, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        buttonOK = new JButton();
        buttonOK.setText("OK");
        panel2.add(buttonOK, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        buttonCancel = new JButton();
        buttonCancel.setText("Cancel");
        panel2.add(buttonCancel, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(3, 2, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.add(panel3, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new GridLayoutManager(3, 2, new Insets(0, 0, 0, 0), -1, -1));
        panel3.add(panel4, new GridConstraints(0, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        userNameLabel = new JLabel();
        this.$$$loadLabelText$$$(userNameLabel, ResourceBundle.getBundle("tokyo/northside/omegat/textra/TextraMachineTranslation").getString("Username"));
        panel4.add(userNameLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        userNameTextField = new JTextField();
        panel4.add(userNameTextField, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        apiKeyLabel = new JLabel();
        this.$$$loadLabelText$$$(apiKeyLabel, ResourceBundle.getBundle("tokyo/northside/omegat/textra/TextraMachineTranslation").getString("ApiKey"));
        apiKeyLabel.setToolTipText(ResourceBundle.getBundle("tokyo/northside/omegat/textra/TextraMachineTranslation").getString("APIKeyToolTipText"));
        panel4.add(apiKeyLabel, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        apikeyTextField = new JTextField();
        panel4.add(apikeyTextField, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        secretLabel = new JLabel();
        this.$$$loadLabelText$$$(secretLabel, ResourceBundle.getBundle("tokyo/northside/omegat/textra/TextraMachineTranslation").getString("ApiSecret"));
        secretLabel.setToolTipText(ResourceBundle.getBundle("tokyo/northside/omegat/textra/TextraMachineTranslation").getString("SecretToolTipText"));
        panel4.add(secretLabel, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        secretTextField = new JTextField();
        panel4.add(secretTextField, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JPanel panel5 = new JPanel();
        panel5.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        panel3.add(panel5, new GridConstraints(2, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panel5.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(-16579837)), ResourceBundle.getBundle("tokyo/northside/omegat/textra/TextraMachineTranslation").getString("SelectMode")));
        final JPanel panel6 = new JPanel();
        panel6.setLayout(new GridLayoutManager(4, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel5.add(panel6, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        generalModeRadioButton = new JRadioButton();
        generalModeRadioButton.setActionCommand("GENERIC");
        generalModeRadioButton.setSelected(true);
        this.$$$loadButtonText$$$(generalModeRadioButton, ResourceBundle.getBundle("tokyo/northside/omegat/textra/TextraMachineTranslation").getString("GeneralMode"));
        generalModeRadioButton.setToolTipText(ResourceBundle.getBundle("tokyo/northside/omegat/textra/TextraMachineTranslation").getString("GeneralModeToolTip"));
        panel6.add(generalModeRadioButton, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        patentModeRadioButton = new JRadioButton();
        patentModeRadioButton.setActionCommand("PATENT");
        this.$$$loadButtonText$$$(patentModeRadioButton, ResourceBundle.getBundle("tokyo/northside/omegat/textra/TextraMachineTranslation").getString("PatentMode"));
        panel6.add(patentModeRadioButton, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel7 = new JPanel();
        panel7.setLayout(new GridLayoutManager(4, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel5.add(panel7, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        patentOrderModeRadioButton = new JRadioButton();
        patentOrderModeRadioButton.setActionCommand("PATENT_CLAIM");
        this.$$$loadButtonText$$$(patentOrderModeRadioButton, ResourceBundle.getBundle("tokyo/northside/omegat/textra/TextraMachineTranslation").getString("PatentOrderMode"));
        panel7.add(patentOrderModeRadioButton, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        registerNewTexTraUserButton = new JButton();
        this.$$$loadButtonText$$$(registerNewTexTraUserButton, ResourceBundle.getBundle("tokyo/northside/omegat/textra/TextraMachineTranslation").getString("RegisterTexTraService"));
        registerNewTexTraUserButton.setToolTipText(ResourceBundle.getBundle("tokyo/northside/omegat/textra/TextraMachineTranslation").getString("RegisterTexTraServiceToolTip"));
        panel3.add(registerNewTexTraUserButton, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        checkTexTraAPIKeyButton = new JButton();
        this.$$$loadButtonText$$$(checkTexTraAPIKeyButton, ResourceBundle.getBundle("tokyo/northside/omegat/textra/TextraMachineTranslation").getString("ShowAPIKeySecretOnBrowser"));
        checkTexTraAPIKeyButton.setToolTipText(ResourceBundle.getBundle("tokyo/northside/omegat/textra/TextraMachineTranslation").getString("ShowAPIKeySecretOnBrowserToolTip"));
        panel3.add(checkTexTraAPIKeyButton, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        this.$$$loadLabelText$$$(label1, ResourceBundle.getBundle("tokyo/northside/omegat/textra/TextraMachineTranslation").getString("TexTraOptionDialogTitle"));
        contentPane.add(label1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        modeButtonGroup = new ButtonGroup();
        modeButtonGroup.add(generalModeRadioButton);
        modeButtonGroup.add(patentModeRadioButton);
        modeButtonGroup.add(patentOrderModeRadioButton);
    }

    /**
     * @noinspection ALL
     */
    private void $$$loadLabelText$$$(JLabel component, String text) {
        StringBuffer result = new StringBuffer();
        boolean haveMnemonic = false;
        char mnemonic = '\0';
        int mnemonicIndex = -1;
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) == '&') {
                i++;
                if (i == text.length()) break;
                if (!haveMnemonic && text.charAt(i) != '&') {
                    haveMnemonic = true;
                    mnemonic = text.charAt(i);
                    mnemonicIndex = result.length();
                }
            }
            result.append(text.charAt(i));
        }
        component.setText(result.toString());
        if (haveMnemonic) {
            component.setDisplayedMnemonic(mnemonic);
            component.setDisplayedMnemonicIndex(mnemonicIndex);
        }
    }

    /**
     * @noinspection ALL
     */
    private void $$$loadButtonText$$$(AbstractButton component, String text) {
        StringBuffer result = new StringBuffer();
        boolean haveMnemonic = false;
        char mnemonic = '\0';
        int mnemonicIndex = -1;
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) == '&') {
                i++;
                if (i == text.length()) break;
                if (!haveMnemonic && text.charAt(i) != '&') {
                    haveMnemonic = true;
                    mnemonic = text.charAt(i);
                    mnemonicIndex = result.length();
                }
            }
            result.append(text.charAt(i));
        }
        component.setText(result.toString());
        if (haveMnemonic) {
            component.setMnemonic(mnemonic);
            component.setDisplayedMnemonicIndex(mnemonicIndex);
        }
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return contentPane;
    }
}
