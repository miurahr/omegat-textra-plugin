package tokyo.northside.omegat.textra.dialog;

import java.awt.*;

import javax.swing.*;

import static tokyo.northside.omegat.textra.StringUtil.getString;

public class TextraOptionDialog extends JDialog {
    JPanel contentPane;
    JButton buttonOK;
    JButton buttonCancel;
    JTextField usernameField;
    JTextField apikeyField;
    JTextField secretField;
    JRadioButton generalNTModeRadioButton;
    JRadioButton patentNTModeRadioButton;
    JRadioButton voiceTraTaiwaNTModeRadioButton;
    JRadioButton financeNTModeRadioButton;
    JRadioButton minnaNTModeRadioButton;
    JRadioButton nictRadioButton;
    JRadioButton kiRadioButton;
    JRadioButton customRadioButton;
    JTextField customIdTextField;
    JLabel connectionStatus;
    JButton connectionTestButton;

    public TextraOptionDialog(Window parent) {
        initGui();
        setLocationRelativeTo(parent);
    }

    public static void main(String[] args) {
        JFrame jFrame = new JFrame();
        TextraOptionDialog dialog = new TextraOptionDialog(jFrame);
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }

    private void initGui() {
        contentPane = new JPanel();
        contentPane.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        // Option dialog title
        final JLabel titleLabel = new JLabel();
        titleLabel.setText(getString("TexTraOptionDialogTitle"));
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 2;
        c.weighty = 1.0d;
        c.gridheight = 1;
        c.anchor = GridBagConstraints.CENTER;
        contentPane.add(titleLabel, c);
        //
        final JPanel panel0 = new JPanel();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 2;
        c.weighty = 1.0d;
        c.anchor = GridBagConstraints.LINE_START;
        contentPane.add(panel0, c);
        // Username field
        final JLabel usernameLabel = new JLabel();
        usernameLabel.setText(getString("Username"));
        c.fill = GridBagConstraints.NONE;
        c.gridx = 0;
        c.gridy = 2;
        c.gridwidth = 1;
        c.weightx = 0.0d;
        c.gridheight = 1;
        c.anchor = GridBagConstraints.LINE_START;
        contentPane.add(usernameLabel, c);
        usernameField = new JTextField();
        usernameField.setHorizontalAlignment(JTextField.LEFT);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 2;
        c.weightx = 1.0d;
        c.gridheight = 1;
        c.anchor = GridBagConstraints.LINE_START;
        contentPane.add(usernameField, c);
        // Api key field
        final JLabel label2 = new JLabel();
        label2.setText(getString("ApiKey"));
        label2.setToolTipText(getString("APIKeyToolTipText"));
        c.fill = GridBagConstraints.NONE;
        c.gridx = 0;
        c.gridy = 3;
        c.weightx = 0.0d;
        c.anchor = GridBagConstraints.LINE_START;
        contentPane.add(label2, c);
        apikeyField = new JTextField();
        apikeyField.setToolTipText(getString("APIKeyToolTipText"));
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 3;
        c.weightx = 1.0d;
        c.anchor = GridBagConstraints.LINE_START;
        contentPane.add(apikeyField, c);
        // Api secret field
        final JLabel label3 = new JLabel();
        label3.setText(getString("ApiSecret"));
        label3.setToolTipText(getString("SecretToolTipText"));
        c.fill = GridBagConstraints.NONE;
        c.gridx = 0;
        c.gridy = 4;
        c.weightx = 0.0d;
        c.anchor = GridBagConstraints.LINE_START;
        contentPane.add(label3, c);
        secretField = new JTextField();
        secretField.setToolTipText(getString("SecretToolTipText"));
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 4;
        c.weightx = 1.0d;
        c.anchor = GridBagConstraints.LINE_START;
        contentPane.add(secretField, c);
        //
        final JPanel panel1 = new JPanel();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 5;
        c.gridwidth = 2;
        c.weighty = 1.0d;
        c.anchor = GridBagConstraints.LINE_START;
        contentPane.add(panel1, c);
        //
        // Service selection
        final JLabel label5 = new JLabel();
        label5.setText("Select service");
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 6;
        c.weightx = 0.0d;
        c.anchor = GridBagConstraints.LINE_START;
        contentPane.add(label5, c);
        nictRadioButton = new JRadioButton();
        nictRadioButton.setSelected(true);
        nictRadioButton.setText("NICT TexTra (nonprofit)");
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 7;
        c.weightx = 0.0d;
        c.gridwidth = 2;
        c.anchor = GridBagConstraints.LINE_START;
        contentPane.add(nictRadioButton, c);
        kiRadioButton = new JRadioButton();
        kiRadioButton.setText("KI TexTra personal business");
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 8;
        c.weightx = 0.0d;
        c.gridwidth = 2;
        c.anchor = GridBagConstraints.LINE_START;
        contentPane.add(kiRadioButton, c);
        //
        final JPanel panel2 = new JPanel();
        connectionStatus = new JLabel();
        connectionStatus.setText(getString("ConnectionStatusNotTested"));
        connectionTestButton = new JButton();
        connectionTestButton.setText(getString("ConnectionTestBtn"));
        connectionTestButton.setToolTipText(getString("ConnectionTestBtnToolTip"));
        panel2.add(connectionTestButton);
        panel2.add(connectionStatus);
        //
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 9;
        c.gridwidth = 2;
        c.weighty = 1.0d;
        c.anchor = GridBagConstraints.LINE_START;
        contentPane.add(panel2, c);
        //  mode selection
        final JLabel label4 = new JLabel();
        label4.setText(getString("SelectMode"));
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 10;
        c.weighty = 0.0d;
        c.gridheight = 1;
        c.anchor = GridBagConstraints.LINE_START;
        contentPane.add(label4, c);
        generalNTModeRadioButton = new JRadioButton();
        generalNTModeRadioButton.setSelected(true);
        generalNTModeRadioButton.setText(getString("GeneralNTMode"));
        generalNTModeRadioButton.setToolTipText(getString("GeneralNTModeToolTip"));
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 11;
        c.weightx = 0.0d;
        c.anchor = GridBagConstraints.LINE_START;
        contentPane.add(generalNTModeRadioButton, c);
        patentNTModeRadioButton = new JRadioButton();
        patentNTModeRadioButton.setText(getString("PatentNTMode"));
        patentNTModeRadioButton.setToolTipText(getString("PatentNTModeToolTip"));
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 12;
        c.weightx = 0.0d;
        c.anchor = GridBagConstraints.LINE_START;
        contentPane.add(patentNTModeRadioButton, c);
        voiceTraTaiwaNTModeRadioButton = new JRadioButton();
        voiceTraTaiwaNTModeRadioButton.setText(getString("VoiceTraNTMode"));
        voiceTraTaiwaNTModeRadioButton.setToolTipText(getString("VoiceTraNTModeToolTip"));
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 13;
        c.weightx = 0.0d;
        c.anchor = GridBagConstraints.LINE_START;
        contentPane.add(voiceTraTaiwaNTModeRadioButton, c);
        financeNTModeRadioButton = new JRadioButton();
        financeNTModeRadioButton.setText(getString("FsaNTMode"));
        financeNTModeRadioButton.setToolTipText(getString("FsaNTModeToolTip"));
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 14;
        c.weightx = 0.0d;
        c.anchor = GridBagConstraints.LINE_START;
        contentPane.add(financeNTModeRadioButton, c);
        minnaNTModeRadioButton = new JRadioButton();
        minnaNTModeRadioButton.setText(getString("minnaNTMode"));
        minnaNTModeRadioButton.setToolTipText(getString("minnaNTModeToolTip"));
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 15;
        c.weightx = 0.0d;
        c.anchor = GridBagConstraints.LINE_START;
        contentPane.add(minnaNTModeRadioButton, c);
        customRadioButton = new JRadioButton();
        customRadioButton.setText(getString("Custom"));
        c.fill = GridBagConstraints.NONE;
        c.gridx = 0;
        c.gridy = 16;
        c.weightx = 0.0d;
        c.anchor = GridBagConstraints.LINE_START;
        contentPane.add(customRadioButton, c);
        customIdTextField = new JTextField();
        customIdTextField.setHorizontalAlignment(JTextField.LEFT);
        customIdTextField.setMinimumSize(new Dimension(100, 24));
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 16;
        c.weightx = 0.0d;
        c.anchor = GridBagConstraints.LINE_START;
        contentPane.add(customIdTextField, c);
        //
        final JPanel panel3 = new JPanel();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 17;
        c.gridwidth = 2;
        c.anchor = GridBagConstraints.LINE_START;
        contentPane.add(panel3, c);
        // OK/Cancel buttons
        final JPanel buttonPanel = new JPanel();
        buttonOK = new JButton();
        buttonOK.setText("OK");
        buttonPanel.add(buttonOK);
        buttonCancel = new JButton();
        buttonCancel.setText("Cancel");
        buttonPanel.add(buttonCancel);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 18;
        c.gridwidth = 2;
        c.anchor = GridBagConstraints.LINE_START;
        contentPane.add(buttonPanel, c);
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
        setContentPane(contentPane);
    }
}
