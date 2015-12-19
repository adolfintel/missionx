package org.easyway.gui.base;

import java.util.Vector;

import org.easyway.input.Keyboard;

public class InputLabel extends Panel {

    private static final long serialVersionUID = 1L;
    Label label;

    public InputLabel(int x, int y, int width, int height,
            GuiContainer container) {
        super(x, y, width, height, container);
        label = new Label(0, 0, "", this) {

            @Override
            public void onClick(int x, int y) {
                //super.onClick(x, y);
                InputLabel.this.onClick(x, y);
            }
        };
        rect.setColor(0.0f, 0.4f, 0.4f);
    }

    @Override
    public void onClick(int x, int y) {
        gainFocus();
    }

    @Override
    public void setWidth(int width) {
        setSize(width, height);
    }

    @Override
    public void setHeight(int height) {
        setSize(width, height);
    }

    public String getText() {
        return label.getText().substring(4);
    }

    public void setText(String text) {
        label.setText(text);
    }

    @Override
    public void customDraw() {
        if (!isOnFocus()) {
            rect.setColor(0.0f, 0.4f, 0.4f);
        } else {
            rect.setColor(0.0f, 0.4f, 0.7f);
            Vector<Integer> keys = Keyboard.getDownKeys();
            for (int key : keys) {
                if (Keyboard.isKeyPressed(key)) {
                    int length;
                    if (key == Keyboard.KEY_BACK && (length = label.text.getText().length()) > 4) {
                        label.text.setText(label.text.getText().substring(0,
                                length - 1));
                        continue;
                    }

                    if (isDirectKey(key)) {
                        String skey = Keyboard.getKeyName(key);
                        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
                            skey = skey.toUpperCase();
                        } else {
                            skey = skey.toLowerCase();
                        }
                        append(skey);
                    } else { // isn't a direct key
                        append(getIndirectChar(key));
                    }
                } // end if keypressed( key )

            } // end for all keys
        }// end if (isOnFocus() )
        super.customDraw();
    }

    protected void append(String text) {
        label.text.append(text);
    }

    protected boolean isDirectKey(int key) {
        if (key <= Keyboard.KEY_0 && key >= Keyboard.KEY_1) {
            return true;
        }
        if (key <= Keyboard.KEY_P && key >= Keyboard.KEY_Q) {
            return true;
        }
        if (key <= Keyboard.KEY_L && key >= Keyboard.KEY_A) {
            return true;
        }
        if (key <= Keyboard.KEY_M && key >= Keyboard.KEY_Z) {
            return true;
        }
        return false;
    }

    protected String getIndirectChar(int key) {
        switch (key) {
            case Keyboard.KEY_SPACE:
                return " ";
            case Keyboard.KEY_COMMA:
                return ",";
            case Keyboard.KEY_SLASH:
            case Keyboard.KEY_DIVIDE:
                return "/";
            case Keyboard.KEY_BACKSLASH:
                return "\\";
            case Keyboard.KEY_GRAVE:
                return "?";
            case Keyboard.KEY_APOSTROPHE:
                return "'";
            case Keyboard.KEY_COLON:
                return ":";
            case Keyboard.KEY_EQUALS:
                return "=";
            case Keyboard.KEY_UNDERLINE:
                return "_";
            // case Keyboard.KEY_PERIOD:
            // return
            case Keyboard.KEY_ADD:
                return "+";
            case Keyboard.KEY_SEMICOLON:
                return ";";
            case Keyboard.KEY_SUBTRACT:
            case Keyboard.KEY_MINUS:
                return "-";
            case Keyboard.KEY_MULTIPLY:
                return "*";
            case Keyboard.KEY_PERIOD:
            case Keyboard.KEY_DECIMAL:
                return ".";
            case Keyboard.KEY_NUMPAD0:
                return "0";
            case Keyboard.KEY_NUMPAD1:
                return "1";
            case Keyboard.KEY_NUMPAD2:
                return "2";
            case Keyboard.KEY_NUMPAD3:
                return "3";
            case Keyboard.KEY_NUMPAD4:
                return "4";
            case Keyboard.KEY_NUMPAD5:
                return "5";
            case Keyboard.KEY_NUMPAD6:
                return "6";
            case Keyboard.KEY_NUMPAD7:
                return "7";
            case Keyboard.KEY_NUMPAD8:
                return "8";
            case Keyboard.KEY_NUMPAD9:
                return "9";
            case Keyboard.KEY_LSHIFT:
            case Keyboard.KEY_RSHIFT:
                return "";
            case Keyboard.KEY_CIRCUMFLEX:
                return "^";

            default:
                System.out.println("-KEY not decoded: id: " + key + " name: " + Keyboard.getKeyName(key));
                return "";
        }
    }
}
