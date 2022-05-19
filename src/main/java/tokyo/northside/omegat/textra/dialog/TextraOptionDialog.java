package tokyo.northside.omegat.textra.dialog;

import tokyo.northside.omegat.textra.TextraOptions;
import tokyo.northside.omegat.textra.TextraOptions.Mode;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

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

    {
        initGui();
    }

    public TextraOptionDialog(final TextraOptions data) {
        GhostTextHandler.register(customIdTextField, "Input like c-12345...");
        GhostTextHandler.register(apikeyField, "Input 41 HEX chars...");
        GhostTextHandler.register(secretField, "Input 32 HEX chars...");
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
        final JPanel idPanel = new JPanel();
        idPanel.setLayout(new GridBagLayout());
        final JLabel usernameLabel = new JLabel();
        usernameLabel.setText("Username");
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        idPanel.add(usernameLabel, c);
        usernameField = new JTextField();
        usernameField.setHorizontalAlignment(JTextField.LEFT);
        usernameField.setPreferredSize(new Dimension(200, 20));
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 0;
        idPanel.add(usernameField, c);
        final JLabel label2 = new JLabel();
        label2.setText("ApiKey");
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 1;
        idPanel.add(label2, c);
        apikeyField = new JTextField();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 1;
        idPanel.add(apikeyField, c);
        final JLabel label3 = new JLabel();
        label3.setText("ApiSecret");
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 2;
        idPanel.add(label3, c);
        secretField = new JTextField();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 2;
        idPanel.add(secretField, c);
        //
        final JPanel modePanel = new JPanel();
        modePanel.setLayout(new GridBagLayout());
        modePanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black),
                null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        final JLabel label4 = new JLabel();
        label4.setText("Select Translation Mode");
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        modePanel.add(label4, c);
        generalNTModeRadioButton = new JRadioButton();
        generalNTModeRadioButton.setSelected(true);
        generalNTModeRadioButton.setText("GeneralNTMode");
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 1;
        modePanel.add(generalNTModeRadioButton, c);
        patentNTModeRadioButton = new JRadioButton();
        patentNTModeRadioButton.setText("PatentNTMode");
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 2;
        modePanel.add(patentNTModeRadioButton, c);
        voiceTraTaiwaNTModeRadioButton = new JRadioButton();
        voiceTraTaiwaNTModeRadioButton.setText("VoiceTraNTMode");
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 3;
        modePanel.add(voiceTraTaiwaNTModeRadioButton, c);
        financeNTModeRadioButton = new JRadioButton();
        financeNTModeRadioButton.setText("FsaNTMode");
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 4;
        modePanel.add(financeNTModeRadioButton, c);
        minnaNTModeRadioButton = new JRadioButton();
        minnaNTModeRadioButton.setText("minnaNTMode");
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 5;
        modePanel.add(minnaNTModeRadioButton, c);
        customRadioButton = new JRadioButton();
        customRadioButton.setText("Custom");
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 6;
        modePanel.add(customRadioButton, c);
        customIdTextField = new JTextField();
        customIdTextField.setHorizontalAlignment(JTextField.LEFT);
        customIdTextField.setPreferredSize(new Dimension(100, 20));
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 6;
        modePanel.add(customIdTextField, c);
        //
        final JPanel servicePanel = new JPanel();
        servicePanel.setLayout(new BoxLayout(servicePanel, BoxLayout.PAGE_AXIS));
        servicePanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black),
                null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        final JLabel label5 = new JLabel();
        label5.setText("Select service");
        servicePanel.add(label5);
        nictRadioButton = new JRadioButton();
        nictRadioButton.setSelected(true);
        nictRadioButton.setText("NICT TexTra (nonprofit)");
        servicePanel.add(nictRadioButton);
        kiRadioButton = new JRadioButton();
        kiRadioButton.setText("KI TexTra personal business");
        servicePanel.add(kiRadioButton);
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
        final JLabel titleLabel = new JLabel();
        titleLabel.setText("Configurations for OmegaT TexTra Plugin");
        //
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        contentPane.add(titleLabel, c);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 1;
        contentPane.add(idPanel, c);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 3;
        contentPane.add(modePanel, c);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 2;
        contentPane.add(servicePanel, c);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 4;
        contentPane.add(buttonPanel, c);
    }

}
