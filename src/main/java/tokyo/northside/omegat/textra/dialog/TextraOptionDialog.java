package tokyo.northside.omegat.textra.dialog;

import tokyo.northside.omegat.textra.TextraOptions;
import tokyo.northside.omegat.textra.TextraOptions.Mode;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
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

    /** Resource bundle that contains all the strings */
    private ResourceBundle bundle;

    public TextraOptionDialog(final TextraOptions data) {
        bundle = ResourceBundle.getBundle("tokyo.northside.omegat.textra.TextraMachineTranslation");
        initGui();
        setContentPane(contentPane);
        setModal(true);
        setOptions(data);
        getRootPane().setDefaultButton(buttonOK);
        GhostTextHandler.register(customIdTextField, getString("CustomIdHint"));
        GhostTextHandler.register(apikeyField, getString("ApiKeyHint"));
        GhostTextHandler.register(secretField, getString("ApiSecretHint"));

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
        switch (data.getProvider()) {
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
        String custom = data.getCustomId();
        if (custom != null) {
            customIdTextField.setText(custom);
        }
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
                break;
            default:
                generalNTModeRadioButton.setSelected(true);
                break;
        }
    }

    private void onOK(final TextraOptions options) {
        if (nictRadioButton.isSelected()) {
            options.setProvider(TextraOptions.Provider.nict);
        } else if (kiRadioButton.isSelected()) {
            options.setProvider(TextraOptions.Provider.minna_personal);
        } else {
            options.setProvider(TextraOptions.Provider.nict);
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
        } else if (customRadioButton.isSelected()) {
            String enteredCustomId = customIdTextField.getText();
            options.setMode(Mode.custom);
            options.setCustomId(enteredCustomId);
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
        TextraOptions options = null;
        try {
            options = new TextraOptions();
        } catch (IOException e) {
            e.printStackTrace();
        }
        TextraOptionDialog dialog = new TextraOptionDialog(options);
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }

    private void initGui() {
        contentPane = new JPanel();
        contentPane.setLayout(new GridBagLayout());
        final JPanel buttonPanel = new JPanel();
        buttonOK = new JButton();
        buttonOK.setText("OK");
        buttonPanel.add(buttonOK);
        buttonCancel = new JButton();
        buttonCancel.setText("Cancel");
        buttonPanel.add(buttonCancel);
        GridBagConstraints c = new GridBagConstraints();
        //
        final JLabel titleLabel = new JLabel();
        titleLabel.setText(getString("TexTraOptionDialogTitle"));
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 2;
        c.anchor = GridBagConstraints.CENTER;
        contentPane.add(titleLabel, c);
        //
        final JLabel usernameLabel = new JLabel();
        usernameLabel.setText(getString("Username"));
        c.fill = GridBagConstraints.NONE;
        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 1;
        c.weightx = 0.0d;
        c.anchor = GridBagConstraints.LINE_START;
        contentPane.add(usernameLabel, c);
        usernameField = new JTextField();
        usernameField.setHorizontalAlignment(JTextField.LEFT);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 1;
        c.weightx = 1.0d;
        c.anchor = GridBagConstraints.LINE_START;
        contentPane.add(usernameField, c);
        final JLabel label2 = new JLabel();
        label2.setText(getString("ApiKey"));
        label2.setToolTipText(getString("APIKeyToolTipText"));
        c.fill = GridBagConstraints.NONE;
        c.gridx = 0;
        c.gridy = 2;
        c.weightx = 0.0d;
        c.anchor = GridBagConstraints.LINE_START;
        contentPane.add(label2, c);
        apikeyField = new JTextField();
        apikeyField.setToolTipText(getString("APIKeyToolTipText"));
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 2;
        c.weightx = 1.0d;
        c.anchor = GridBagConstraints.LINE_START;
        contentPane.add(apikeyField, c);
        final JLabel label3 = new JLabel();
        label3.setText(getString("ApiSecret"));
        label3.setToolTipText(getString("SecretToolTipText"));
        c.fill = GridBagConstraints.NONE;
        c.gridx = 0;
        c.gridy = 3;
        c.weightx = 0.0d;
        c.anchor = GridBagConstraints.LINE_START;
        contentPane.add(label3, c);
        secretField = new JTextField();
        secretField.setToolTipText(getString("SecretToolTipText"));
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 3;
        c.weightx = 1.0d;
        c.anchor = GridBagConstraints.LINE_START;
        contentPane.add(secretField, c);
        //
        final JPanel modePanel = new JPanel();
        modePanel.setLayout(new GridBagLayout());
        final JLabel label4 = new JLabel();
        label4.setText(getString("SelectMode"));
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 0.0d;
        c.anchor = GridBagConstraints.LINE_START;
        modePanel.add(label4, c);
        generalNTModeRadioButton = new JRadioButton();
        generalNTModeRadioButton.setSelected(true);
        generalNTModeRadioButton.setText(getString("GeneralNTMode"));
        generalNTModeRadioButton.setToolTipText(getString("GeneralNTModeToolTip"));
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 1;
        c.weightx = 0.0d;
        c.anchor = GridBagConstraints.LINE_START;
        modePanel.add(generalNTModeRadioButton, c);
        patentNTModeRadioButton = new JRadioButton();
        patentNTModeRadioButton.setText(getString("PatentNTMode"));
        patentNTModeRadioButton.setToolTipText(getString("PatentNTModeToolTip"));
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 2;
        c.weightx = 0.0d;
        c.anchor = GridBagConstraints.LINE_START;
        modePanel.add(patentNTModeRadioButton, c);
        voiceTraTaiwaNTModeRadioButton = new JRadioButton();
        voiceTraTaiwaNTModeRadioButton.setText(getString("VoiceTraNTMode"));
        voiceTraTaiwaNTModeRadioButton.setToolTipText(getString("VoiceTraNTModeToolTip"));
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 3;
        c.weightx = 0.0d;
        c.anchor = GridBagConstraints.LINE_START;
        modePanel.add(voiceTraTaiwaNTModeRadioButton, c);
        financeNTModeRadioButton = new JRadioButton();
        financeNTModeRadioButton.setText(getString("FsaNTMode"));
        financeNTModeRadioButton.setToolTipText(getString("FsaNTModeToolTip"));
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 4;
        c.weightx = 0.0d;
        c.anchor = GridBagConstraints.LINE_START;
        modePanel.add(financeNTModeRadioButton, c);
        minnaNTModeRadioButton = new JRadioButton();
        minnaNTModeRadioButton.setText(getString("minnaNTMode"));
        minnaNTModeRadioButton.setToolTipText(getString("minnaNTModeToolTip"));
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 5;
        c.weightx = 0.0d;
        c.anchor = GridBagConstraints.LINE_START;
        modePanel.add(minnaNTModeRadioButton, c);
        customRadioButton = new JRadioButton();
        customRadioButton.setText(getString("Custom"));
        c.fill = GridBagConstraints.NONE;
        c.gridx = 0;
        c.gridy = 6;
        c.weightx = 0.0d;
        c.anchor = GridBagConstraints.LINE_START;
        modePanel.add(customRadioButton, c);
        customIdTextField = new JTextField();
        customIdTextField.setHorizontalAlignment(JTextField.LEFT);
        customIdTextField.setMinimumSize(new Dimension(100, 24));
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 6;
        c.weightx = 0.0d;
        c.anchor = GridBagConstraints.LINE_START;
        modePanel.add(customIdTextField, c);
        //
        final JLabel label5 = new JLabel();
        label5.setText("Select service");
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 4;
        c.weightx = 0.0d;
        c.anchor = GridBagConstraints.LINE_START;
        contentPane.add(label5, c);
        nictRadioButton = new JRadioButton();
        nictRadioButton.setSelected(true);
        nictRadioButton.setText("NICT TexTra (nonprofit)");
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 5;
        c.weightx = 0.0d;
        c.gridwidth = 2;
        c.anchor = GridBagConstraints.LINE_START;
        contentPane.add(nictRadioButton, c);
        kiRadioButton = new JRadioButton();
        kiRadioButton.setText("KI TexTra personal business");
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 6;
        c.weightx = 0.0d;
        c.gridwidth = 2;
        c.anchor = GridBagConstraints.LINE_START;
        contentPane.add(kiRadioButton, c);
        //
        ButtonGroup buttonGroup;
        buttonGroup = new ButtonGroup();
        buttonGroup.add(generalNTModeRadioButton);
        buttonGroup.add(generalNTModeRadioButton);
        buttonGroup.add(patentNTModeRadioButton);
        buttonGroup.add(voiceTraTaiwaNTModeRadioButton);
        buttonGroup.add(financeNTModeRadioButton);
        buttonGroup.add(minnaNTModeRadioButton);
        buttonGroup.add(customRadioButton);
        buttonGroup = new ButtonGroup();
        buttonGroup.add(kiRadioButton);
        buttonGroup.add(nictRadioButton);
        //
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 7;
        c.gridwidth = 2;
        c.anchor = GridBagConstraints.LINE_START;
        contentPane.add(modePanel, c);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 8;
        c.gridwidth = 2;
        c.anchor = GridBagConstraints.LINE_START;
        contentPane.add(buttonPanel, c);
    }

    private String getString(String key) {
        return bundle.getString(key);
    }

}
