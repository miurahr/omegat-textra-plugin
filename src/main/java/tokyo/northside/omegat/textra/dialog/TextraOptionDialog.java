package tokyo.northside.omegat.textra.dialog;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import tokyo.northside.omegat.textra.TextraOptions;
import tokyo.northside.omegat.textra.TextraOptions.TranslationMode;
import tokyo.northside.omegat.textra.WebBrowser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ResourceBundle;

import static tokyo.northside.omegat.textra.OmegatTextraMachineTranslation.API_KEY_URL;
import static tokyo.northside.omegat.textra.OmegatTextraMachineTranslation.REGISTRATION_URL;
import static tokyo.northside.omegat.textra.TextraOptions.TranslationMode.*;


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
    private JRadioButton generalPlusModeRadioButton;
    private JRadioButton addressModeRadioButton;
    private JRadioButton patentModeRadioButton;
    private JRadioButton JPOModeRadioButton;
    private JRadioButton JPOPatentOrderModeRadioButton;
    private JRadioButton JPONICTPatentModeRadioButton;
    private JRadioButton JPONICTPatentOrderRadioButton;
    private JButton registerNewTexTraUserButton;
    private JButton checkTexTraAPIKeyButton;
    private ButtonGroup modeButtonGroup;

    private boolean updated;

    /**
     * Dialog constructor.
     */
    public TextraOptionDialog() {
        updated = false;
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        registerNewTexTraUserButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                try {
                    WebBrowser.launch(REGISTRATION_URL);
                } catch (Exception e) {
                    // TODO: implement me.
                }
            }
        });
        checkTexTraAPIKeyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                try {
                    WebBrowser.launch(API_KEY_URL);
                } catch (Exception e) {
                    // TODO: implement me.
                }
            }
        });
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
        TranslationMode mode = data.getMode();
        switch (mode) {
            case ADDRESS:
                addressModeRadioButton.setSelected(true);
                break;
            case GENERIC:
                generalModeRadioButton.setSelected(true);
                break;
            case PATENT:
                patentModeRadioButton.setSelected(true);
                break;
            case JPO:
                JPOModeRadioButton.setSelected(true);
                break;
            case JPO_CLAIM:
                JPOPatentOrderModeRadioButton.setSelected(true);
                break;
            case JPO_NICT:
                JPONICTPatentModeRadioButton.setSelected(true);
                break;
            case JPO_NICT_CLAIM:
                JPONICTPatentOrderRadioButton.setSelected(true);
                break;
            case MINNA:
            default:
                generalPlusModeRadioButton.setSelected(true);
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
        data.setMode(valueOf(modeButtonGroup.getSelection().getActionCommand()));
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
        if (!TranslationMode.valueOf(modeButtonGroup.getSelection().getActionCommand()).equals(data.getMode())) {
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
        generalModeRadioButton.setActionCommand("GENERAL");
        generalModeRadioButton.setSelected(true);
        this.$$$loadButtonText$$$(generalModeRadioButton, ResourceBundle.getBundle("tokyo/northside/omegat/textra/TextraMachineTranslation").getString("GeneralMode"));
        panel6.add(generalModeRadioButton, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        generalPlusModeRadioButton = new JRadioButton();
        generalPlusModeRadioButton.setActionCommand("MINNA");
        this.$$$loadButtonText$$$(generalPlusModeRadioButton, ResourceBundle.getBundle("tokyo/northside/omegat/textra/TextraMachineTranslation").getString("GeneralPlusMode"));
        panel6.add(generalPlusModeRadioButton, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        addressModeRadioButton = new JRadioButton();
        addressModeRadioButton.setActionCommand("ADDRESS");
        this.$$$loadButtonText$$$(addressModeRadioButton, ResourceBundle.getBundle("tokyo/northside/omegat/textra/TextraMachineTranslation").getString("AddressMode"));
        panel6.add(addressModeRadioButton, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        patentModeRadioButton = new JRadioButton();
        patentModeRadioButton.setActionCommand("PATENT");
        this.$$$loadButtonText$$$(patentModeRadioButton, ResourceBundle.getBundle("tokyo/northside/omegat/textra/TextraMachineTranslation").getString("PatentMode"));
        panel6.add(patentModeRadioButton, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel7 = new JPanel();
        panel7.setLayout(new GridLayoutManager(4, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel5.add(panel7, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        JPOModeRadioButton = new JRadioButton();
        JPOModeRadioButton.setActionCommand("JPO");
        this.$$$loadButtonText$$$(JPOModeRadioButton, ResourceBundle.getBundle("tokyo/northside/omegat/textra/TextraMachineTranslation").getString("JpoPatentMode"));
        panel7.add(JPOModeRadioButton, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        JPOPatentOrderModeRadioButton = new JRadioButton();
        JPOPatentOrderModeRadioButton.setActionCommand("JPO_PATENT_CLAIM");
        this.$$$loadButtonText$$$(JPOPatentOrderModeRadioButton, ResourceBundle.getBundle("tokyo/northside/omegat/textra/TextraMachineTranslation").getString("JpoOrderMode"));
        panel7.add(JPOPatentOrderModeRadioButton, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        JPONICTPatentModeRadioButton = new JRadioButton();
        JPONICTPatentModeRadioButton.setActionCommand("JPO_NICT");
        this.$$$loadButtonText$$$(JPONICTPatentModeRadioButton, ResourceBundle.getBundle("tokyo/northside/omegat/textra/TextraMachineTranslation").getString("JpoNictPatentMode"));
        panel7.add(JPONICTPatentModeRadioButton, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        JPONICTPatentOrderRadioButton = new JRadioButton();
        JPONICTPatentOrderRadioButton.setActionCommand("JPO_NICT_CLAIM");
        this.$$$loadButtonText$$$(JPONICTPatentOrderRadioButton, ResourceBundle.getBundle("tokyo/northside/omegat/textra/TextraMachineTranslation").getString("JpoNictOrderMode"));
        panel7.add(JPONICTPatentOrderRadioButton, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
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
        modeButtonGroup.add(generalPlusModeRadioButton);
        modeButtonGroup.add(addressModeRadioButton);
        modeButtonGroup.add(JPOModeRadioButton);
        modeButtonGroup.add(patentModeRadioButton);
        modeButtonGroup.add(JPONICTPatentOrderRadioButton);
        modeButtonGroup.add(JPONICTPatentModeRadioButton);
        modeButtonGroup.add(JPOPatentOrderModeRadioButton);
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
