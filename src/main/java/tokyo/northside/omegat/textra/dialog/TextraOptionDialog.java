package tokyo.northside.omegat.textra.dialog;

import org.omegat.util.gui.DelegatingComboBoxRenderer;

import tokyo.northside.omegat.textra.TextraOptionsFactory;

import java.awt.*;

import javax.swing.*;

import static tokyo.northside.omegat.textra.BundleMessageUtil.getString;

public class TextraOptionDialog extends JDialog {
    JPanel contentPane;
    JButton buttonOK;
    JButton buttonCancel;
    JTextField usernameField;
    JTextField apikeyField;
    JTextField secretField;
    JComboBox<String> modeComboBox;
    JComboBox<String> providerComboBox;
    JTextField customIdTextField;
    JLabel connectionStatus;
    JButton connectionTestButton;

    public TextraOptionDialog(Window parent, TextraOptionsFactory factory) {
        initGui();
        setLocationRelativeTo(parent);
    }

    public static void main(String[] args) {
        JFrame jFrame = new JFrame();
        TextraOptionsFactory factory = new TextraOptionsFactory();
        TextraOptionDialog dialog = new TextraOptionDialog(jFrame, factory);
        TextraOptionsFactory textraOptionsFactory = new TextraOptionsFactory();
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
        dialog.pack();
        dialog.setVisible(true);
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
        providerComboBox = new JComboBox<>();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 7;
        c.weightx = 0.0d;
        c.gridwidth = 2;
        c.anchor = GridBagConstraints.LINE_START;
        contentPane.add(providerComboBox, c);
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
        modeComboBox = new JComboBox<>();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 11;
        c.weightx = 0.0d;
        c.anchor = GridBagConstraints.LINE_START;
        contentPane.add(modeComboBox, c);
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
        setContentPane(contentPane);
    }
}
