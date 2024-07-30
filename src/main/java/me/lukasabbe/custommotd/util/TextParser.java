package me.lukasabbe.custommotd.util;

import eu.pb4.placeholders.api.PlaceholderContext;
import eu.pb4.placeholders.api.Placeholders;
import me.lukasabbe.custommotd.Custommotd;
import net.minecraft.text.Text;

public class TextParser {
    public static String formatText(String text){
        text = text.replace("\\u00A7", "§");
        text = text.replace("\\n","\n");
        text = Placeholders.parseText(Text.of(text), PlaceholderContext.of(Custommotd.server)).getString();
        return text;
    }
}
