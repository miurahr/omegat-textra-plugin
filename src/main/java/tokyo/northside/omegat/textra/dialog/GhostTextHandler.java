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

public class GhostTextHandler implements FocusListener, DocumentListener, PropertyChangeListener {
    private final JTextComponent textComponent;
    private final String ghostText;
    private final Color foregroundColor;
    private boolean isEmpty;

    public static void register(final JTextComponent textComponent, final String ghostText) {
        GhostTextHandler handler = new GhostTextHandler(textComponent, ghostText);
        textComponent.addFocusListener(handler);
        textComponent.getDocument().addDocumentListener(handler);
        textComponent.addPropertyChangeListener("foreground", handler);
    }

    protected GhostTextHandler(final JTextComponent textComponent, final String ghostText) {
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
