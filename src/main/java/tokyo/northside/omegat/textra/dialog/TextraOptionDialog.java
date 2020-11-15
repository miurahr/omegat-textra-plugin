package tokyo.northside.omegat.textra.dialog;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;

import tokyo.northside.omegat.textra.TextraOptions;
import tokyo.northside.omegat.textra.TextraOptions.Mode;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.lang.reflect.Method;
import java.util.ResourceBundle;

public class TextraOptionDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField usernameField;
    private JTextField apikeyField;
    private JTextField secretField;
    private JRadioButton generalNTModeRadioButton;
    private JRadioButton patentNTModeRadioButton;
    private JRadioButton voiceTraTaiwaNTModeRadioButton;
    private JRadioButton financeNTModeRadioButton;
    private JRadioButton minnaNTModeRadioButton;
    private JRadioButton nictRadioButton;
    private JRadioButton kiRadioButton;
    private JRadioButton customRadioButton;
    private JTextField customIdTextField;


    public TextraOptionDialog(final TextraOptions data) {
        setContentPane(contentPane);
        setModal(true);
        setOptions(data);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(e -> onOK(data));

        buttonCancel.addActionListener(e -> onCancel(data));

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel(data);
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(e -> onCancel(data),
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    /**
     * Set Dialog options.
     *
     * @param data option data.
     */
    public void setOptions(final TextraOptions data) {
        switch (data.getServer()) {
            case nict:
                nictRadioButton.setSelected(true);
                break;
            case minna_personal:
                kiRadioButton.setSelected(true);
                break;
            default:
                nictRadioButton.setSelected(true);
                break;
        }
        usernameField.setText(data.getUsername());
        apikeyField.setText(data.getApikey());
        secretField.setText(data.getSecret());
        customIdTextField.setText(data.getCustomId());
        switch (data.getMode()) {
            case generalNT:
                generalNTModeRadioButton.setSelected(true);
                break;
            case minnaNT:
                minnaNTModeRadioButton.setSelected(true);
                break;
            case patentNT:
                patentNTModeRadioButton.setSelected(true);
                break;
            case voicetraNT:
                voiceTraTaiwaNTModeRadioButton.setSelected(true);
                break;
            case fsaNT:
                financeNTModeRadioButton.setSelected(true);
                break;
            case custom:
                customRadioButton.setSelected(true);
            default:
                generalNTModeRadioButton.setSelected(true);
                break;
        }
    }

    private void onOK(final TextraOptions options) {
        if (nictRadioButton.isSelected()) {
            options.setServer(TextraOptions.Server.nict);
        } else if (kiRadioButton.isSelected()) {
            options.setServer(TextraOptions.Server.minna_personal);
        } else {
            options.setServer(TextraOptions.Server.nict);
        }
        options.setUsername(usernameField.getText());
        options.setApikey(apikeyField.getText());
        options.setSecret(secretField.getText());
        if (generalNTModeRadioButton.isSelected()) {
            options.setMode(Mode.generalNT);
        } else if (minnaNTModeRadioButton.isSelected()) {
            options.setMode(Mode.minnaNT);
        } else if (patentNTModeRadioButton.isSelected()) {
            options.setMode(Mode.patentNT);
        } else if (financeNTModeRadioButton.isSelected()) {
            options.setMode(Mode.fsaNT);
        } else if (voiceTraTaiwaNTModeRadioButton.isSelected()) {
            options.setMode(Mode.voicetraNT);
        } else {
            options.setMode(Mode.generalNT);
        }
        options.saveCredentials();
        dispose();
    }

    private void onCancel(final TextraOptions data) {
        setOptions(data);
        dispose();
    }

    public static void main(String[] args) {
        TextraOptions options = new TextraOptions();
        TextraOptionDialog dialog = new TextraOptionDialog(options);
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
        contentPane.setLayout(new GridLayoutManager(4, 1, new Insets(10, 10, 10, 10), -1, -1));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.add(panel1, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, 1, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        panel1.add(spacer1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1, true, false));
        panel1.add(panel2, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        buttonOK = new JButton();
        buttonOK.setText("OK");
        panel2.add(buttonOK, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        buttonCancel = new JButton();
        buttonCancel.setText("Cancel");
        panel2.add(buttonCancel, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(4, 2, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.add(panel3, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new GridLayoutManager(3, 2, new Insets(0, 0, 0, 0), -1, -1));
        panel3.add(panel4, new GridConstraints(2, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        this.$$$loadLabelText$$$(label1, this.$$$getMessageFromBundle$$$("tokyo/northside/omegat/textra/TextraMachineTranslation", "Username"));
        panel4.add(label1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        usernameField = new JTextField();
        panel4.add(usernameField, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label2 = new JLabel();
        this.$$$loadLabelText$$$(label2, this.$$$getMessageFromBundle$$$("tokyo/northside/omegat/textra/TextraMachineTranslation", "ApiKey"));
        panel4.add(label2, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        apikeyField = new JTextField();
        apikeyField.setToolTipText(this.$$$getMessageFromBundle$$$("tokyo/northside/omegat/textra/TextraMachineTranslation", "APIKeyToolTipText"));
        panel4.add(apikeyField, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label3 = new JLabel();
        this.$$$loadLabelText$$$(label3, this.$$$getMessageFromBundle$$$("tokyo/northside/omegat/textra/TextraMachineTranslation", "ApiSecret"));
        panel4.add(label3, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        secretField = new JTextField();
        secretField.setToolTipText(this.$$$getMessageFromBundle$$$("tokyo/northside/omegat/textra/TextraMachineTranslation", "SecretToolTipText"));
        panel4.add(secretField, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JPanel panel5 = new JPanel();
        panel5.setLayout(new GridLayoutManager(6, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel3.add(panel5, new GridConstraints(3, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panel5.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        generalNTModeRadioButton = new JRadioButton();
        generalNTModeRadioButton.setSelected(true);
        this.$$$loadButtonText$$$(generalNTModeRadioButton, this.$$$getMessageFromBundle$$$("tokyo/northside/omegat/textra/TextraMachineTranslation", "GeneralNTMode"));
        generalNTModeRadioButton.setToolTipText(this.$$$getMessageFromBundle$$$("tokyo/northside/omegat/textra/TextraMachineTranslation", "GeneralNTModeToolTip"));
        panel5.add(generalNTModeRadioButton, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        patentNTModeRadioButton = new JRadioButton();
        this.$$$loadButtonText$$$(patentNTModeRadioButton, this.$$$getMessageFromBundle$$$("tokyo/northside/omegat/textra/TextraMachineTranslation", "PatentNTMode"));
        patentNTModeRadioButton.setToolTipText(this.$$$getMessageFromBundle$$$("tokyo/northside/omegat/textra/TextraMachineTranslation", "PatentNTModeToolTip"));
        panel5.add(patentNTModeRadioButton, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        voiceTraTaiwaNTModeRadioButton = new JRadioButton();
        this.$$$loadButtonText$$$(voiceTraTaiwaNTModeRadioButton, this.$$$getMessageFromBundle$$$("tokyo/northside/omegat/textra/TextraMachineTranslation", "VoiceTraNTMode"));
        voiceTraTaiwaNTModeRadioButton.setToolTipText(this.$$$getMessageFromBundle$$$("tokyo/northside/omegat/textra/TextraMachineTranslation", "VoiceTraNTModeToolTip"));
        panel5.add(voiceTraTaiwaNTModeRadioButton, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        financeNTModeRadioButton = new JRadioButton();
        this.$$$loadButtonText$$$(financeNTModeRadioButton, this.$$$getMessageFromBundle$$$("tokyo/northside/omegat/textra/TextraMachineTranslation", "FsaNTMode"));
        financeNTModeRadioButton.setToolTipText(this.$$$getMessageFromBundle$$$("tokyo/northside/omegat/textra/TextraMachineTranslation", "FsaNTModeToolTip"));
        panel5.add(financeNTModeRadioButton, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        minnaNTModeRadioButton = new JRadioButton();
        this.$$$loadButtonText$$$(minnaNTModeRadioButton, this.$$$getMessageFromBundle$$$("tokyo/northside/omegat/textra/TextraMachineTranslation", "minnaNTMode"));
        minnaNTModeRadioButton.setToolTipText(this.$$$getMessageFromBundle$$$("tokyo/northside/omegat/textra/TextraMachineTranslation", "minnaNTModeToolTip"));
        panel5.add(minnaNTModeRadioButton, new GridConstraints(5, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label4 = new JLabel();
        this.$$$loadLabelText$$$(label4, this.$$$getMessageFromBundle$$$("tokyo/northside/omegat/textra/TextraMachineTranslation", "SelectMode"));
        panel5.add(label4, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel6 = new JPanel();
        panel6.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel3.add(panel6, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        nictRadioButton = new JRadioButton();
        nictRadioButton.setSelected(true);
        nictRadioButton.setText("NICT TexTra (nonprofit)");
        panel6.add(nictRadioButton, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        kiRadioButton = new JRadioButton();
        kiRadioButton.setText("KI TexTra personal business");
        panel6.add(kiRadioButton, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label5 = new JLabel();
        label5.setText("Select service");
        panel3.add(label5, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel7 = new JPanel();
        panel7.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.add(panel7, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label6 = new JLabel();
        this.$$$loadLabelText$$$(label6, this.$$$getMessageFromBundle$$$("tokyo/northside/omegat/textra/TextraMachineTranslation", "TexTraOptionDialogTitle"));
        panel7.add(label6, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        ButtonGroup buttonGroup;
        buttonGroup = new ButtonGroup();
        buttonGroup.add(generalNTModeRadioButton);
        buttonGroup.add(generalNTModeRadioButton);
        buttonGroup.add(patentNTModeRadioButton);
        buttonGroup.add(voiceTraTaiwaNTModeRadioButton);
        buttonGroup.add(financeNTModeRadioButton);
        buttonGroup.add(minnaNTModeRadioButton);
        buttonGroup = new ButtonGroup();
        buttonGroup.add(kiRadioButton);
        buttonGroup.add(nictRadioButton);
    }

    private static Method $$$cachedGetBundleMethod$$$ = null;

    private String $$$getMessageFromBundle$$$(String path, String key) {
        ResourceBundle bundle;
        try {
            Class<?> thisClass = this.getClass();
            if ($$$cachedGetBundleMethod$$$ == null) {
                Class<?> dynamicBundleClass = thisClass.getClassLoader().loadClass("com.intellij.DynamicBundle");
                $$$cachedGetBundleMethod$$$ = dynamicBundleClass.getMethod("getBundle", String.class, Class.class);
            }
            bundle = (ResourceBundle) $$$cachedGetBundleMethod$$$.invoke(null, path, thisClass);
        } catch (Exception e) {
            bundle = ResourceBundle.getBundle(path);
        }
        return bundle.getString(key);
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
