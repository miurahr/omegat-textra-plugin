package tokyo.northside.omegat.textra.dialog;

import org.omegat.util.StringUtil;

import java.awt.Color;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;

/**
 * Handler for displaying ghost text in JTextComponent.
 */
public class GhostTextHandler implements FocusListener, DocumentListener, PropertyChangeListener {
    /**
     * component to add ghost text.
     */
    private final JTextComponent textComponent;
    /**
     * Ghost text to display.
     */
    private final String ghostText;
    /**
     * The color of the ghost text displayed in the associated text component.
     * This color is used to differentiate the ghost text from the user-entered text,
     * typically by setting it to a lighter or more subtle shade.
     */
    private final Color foregroundColor;
    /**
     * Indicates whether the text component is currently empty.
     */
    private boolean isEmpty;

    /**
     * Constructor for GhostTextHandler.
     *
     * @param textComponent JTextComponent to handle ghost text.
     * @param ghostText     Ghost text to display.
     */
    public static void register(JTextComponent textComponent, String ghostText) {
        GhostTextHandler handler = new GhostTextHandler(textComponent, ghostText);
        textComponent.addFocusListener(handler);
        textComponent.getDocument().addDocumentListener(handler);
        textComponent.addPropertyChangeListener("foreground", handler);
    }

    /**
     * Constructor for GhostTextHandler.
     * @param textComponent JTextComponent to handle ghost text.
     * @param ghostText Ghost text to display.
     */
    protected GhostTextHandler(JTextComponent textComponent, String ghostText) {
        this.textComponent = textComponent;
        this.ghostText = ghostText;
        isEmpty = StringUtil.isEmpty(textComponent.getText());
        foregroundColor = textComponent.getForeground();
        if (isEmpty) {
            textComponent.setText(ghostText);
            textComponent.setForeground(Color.GRAY);
        }
    }

    @Override
    public void focusGained(FocusEvent focusEvent) {
        if (isEmpty) {
            textComponent.getDocument().removeDocumentListener(this);
            textComponent.removePropertyChangeListener("foreground", this);
            try {
                textComponent.setText("");
                textComponent.setForeground(foregroundColor);
            } finally {
                textComponent.getDocument().addDocumentListener(this);
                textComponent.addPropertyChangeListener("foreground", this);
            }
        }
    }

    @Override
    public void focusLost(FocusEvent focusEvent) {
        if (isEmpty) {
            textComponent.getDocument().removeDocumentListener(this);
            textComponent.removePropertyChangeListener("foreground", this);
            try {
                textComponent.setText(ghostText);
                textComponent.setForeground(Color.GRAY);
            } finally {
                textComponent.getDocument().addDocumentListener(this);
                textComponent.addPropertyChangeListener("foreground", this);
            }
        }
    }

    @Override
    public void insertUpdate(DocumentEvent documentEvent) {
        isEmpty = StringUtil.isEmpty(textComponent.getText());
    }

    @Override
    public void removeUpdate(DocumentEvent documentEvent) {
        isEmpty = StringUtil.isEmpty(textComponent.getText());
    }

    @Override
    public void changedUpdate(DocumentEvent documentEvent) {
        isEmpty = StringUtil.isEmpty(textComponent.getText());
    }

    @Override
    public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
        isEmpty = StringUtil.isEmpty(textComponent.getText());
    }
}
