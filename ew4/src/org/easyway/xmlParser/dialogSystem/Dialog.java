/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.easyway.xmlParser.dialogSystem;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import javax.swing.plaf.metal.MetalBorders.OptionDialogBorder;

/**
 *
 * @author Daniele Paggi
 */
public class Dialog {

    private Collection<DialogOption> options;
    private WeakReference<DialogListener> listener = null;
    private String text;
    private Talkerable talker;

    public Dialog() {
        options = new ArrayList<DialogOption>(10);
    }

    public String getText() {
        return text;
    }

    public void setTalker(Talkerable talker) {
        this.talker = talker;
        DialogListener obj = getWeakListener();
        if (obj != null) {
            obj.onChangeTalker(talker);
        }
    }

    public Talkerable getTalker() {
        return talker;
    }

    public void setText(String text) {
        this.text = text;

        DialogListener obj = getWeakListener();
        if (obj != null) {
            obj.onChangeMainText(text);
        }
    }

    private DialogListener getWeakListener() {
        if (listener == null) {
            return null;
        }
        DialogListener obj = listener.get();
        return obj;
    }

    public void endDialog() {
        DialogListener obj = getWeakListener();
        if (obj != null) {
            obj.onEndDialog();
        }
    }

    public void addOption(DialogOption option) {
        if (option == null) {
            throw new RuntimeException("null option");
        }
        if (options.contains(option)) {
            // option already contained
            return;
        }
        option.setDialog(this);
        options.add(option);

        DialogListener obj = getWeakListener();
        if (obj != null) {
            obj.onAddOption(option);
        }
    }

    public void resetOptions() {
        options.clear();

        DialogListener obj = getWeakListener();
        if (obj != null) {
            obj.onResetOption();
        }
    }

    protected void changedOption(DialogOption opt) {
        if (opt == null) {
            throw new RuntimeException("it's changed a null option");
        }
        DialogListener obj = getWeakListener();
        if (obj != null) {
            obj.onChangeOption(opt);
        }
    }

    public Collection<DialogOption> getOptions() {
        return options;
    }

    public void setListener(DialogListener dl) {
        listener = new WeakReference(dl);
        if (text != null) {
            dl.onChangeMainText(text);
        }
        dl.onResetOption();
        for (DialogOption opt : options) {
            dl.onAddOption(opt);
        }
        if (talker != null) {
            dl.onChangeTalker(talker);
        }
    }
}
