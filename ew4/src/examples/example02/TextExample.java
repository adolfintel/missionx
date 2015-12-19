package examples.example02;

import java.awt.Color;

import org.easyway.objects.text.EWFont;
import org.easyway.objects.text.RpgText;
import org.easyway.objects.text.Text;
import org.easyway.objects.text.TextAlign;
import org.easyway.system.state.Game;

public class TextExample extends Game {

    public static void main(String args[]) {
        new TextExample();
    }

    public TextExample() {
        super(1024, 768, 24, false);
    }
    RpgText rpgText;

    @Override
    public void creation() {

        // indicates where the new text should be placed..
        int y = 0;
        // if (y + 1 == 1)
        // System.exit(1);

        // ------- INIT FONTS ------
        // Load from the fonts of the System the "Arial" Font
        // note: the EWFont in the future will be replace with a new one
        EWFont font = new EWFont("Arial");
        // change the font's size
        font.setSize(22);

        // -------- TEXT's BASICS -----
        // create a Text
        // (x, y, message to write, font)
        Text txt = new Text(0, y, "Simple Text", font);
        y += txt.getHeight();

        // create an empty text
        Text txt2 = new Text(0, y, "", font);
        y += txt2.getHeight();
        // you can change dinamically the text with the following method
        txt2.setText("Now I've assigned a new text");

        // create a new formatter color (Red: 0, Green: 255, Blue: 0)
        String greenColor = Text.createColor(0, 255, 0);
        // create a colorized text
        Text txt3 = new Text(0, y, "colorized " + greenColor + "font", font);
        y += txt3.getHeight();

        // gradient colorized text
        // note that the color is specified with: java.awt.Color class
        String message = Text.createGradientText(
                "Text with gradient from Green to blue", Color.GREEN,
                Color.BLUE);
        Text txt4 = new Text(0, y, message, font);
        y += txt4.getHeight();

        // ------ SPECIAL CHARS: \n -----
        // we can use the \n for splitting in more that 1 line the text (as in
        // the println, printf, ..)
        Text txt5 = new Text(30, y,
                "with a \\n\nwe can write a text on more\nthat 1 lines", font);
        // note that the getHeight() consider the number of lines too
        y += txt5.getHeight();

        // creates a text with a typing-effect
        String text = "This text is created with the " + Text.createColor(255, 0, 0) // red
                + "RpgText" + Text.createColor(255, 255, 255) // White
                + " class!\n" + Text.createGradientText(
                "The RpgText can be colorized as a normal text",
                Color.PINK, Color.MAGENTA) + Text.createColor(255, 255, 255) // white again
                + "\nand support all the other features of the normal text";

        rpgText = new RpgText(font, 1024, y, text);
        rpgText.setAlignH(TextAlign.RIGHT);
        // start the typing speed with the speed specified (in ms: 1000 =
        // 1second)
        rpgText.startEffect(100);

        // we can use setAlignH and setAlignV to change the align of the text
        // as default alignV is TOP and alignH is LEFT
        Text alignedText = new Text(1024, 0, "Aligned Text TOP/RIGHT", font);
        alignedText.setAlignH(TextAlign.RIGHT);

        Text centerScreenText = new Text(1024 / 2, 768, "Bottom Centered Text",
                font);
        centerScreenText.setAlignH(TextAlign.MIDDLE);
        centerScreenText.setAlignV(TextAlign.BOTTOM);

    }

    @Override
    public void loop() {
        // if the effect of the rpgText is terminated:
        if (rpgText.isEffectTerminated()) {
            // select the number of visible chars:
            rpgText.setCaretPosition(0);
            // start the effect again (with the previous typing-speed)
            rpgText.startEffect();
        }
    }
}
