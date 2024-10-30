package me.lukasabbe.custommotd.util;

import eu.pb4.placeholders.api.PlaceholderContext;
import eu.pb4.placeholders.api.Placeholders;
import eu.pb4.placeholders.api.TextParserUtils;
import me.lukasabbe.custommotd.Custommotd;
import net.minecraft.text.Text;

public class TextParser {
    public static Text formatText(String text){
        text = text.replace("\\u00A7", "§");
        text = text.replace("\\n","\n");
        text = Placeholders.parseText(Text.of(text), PlaceholderContext.of(Custommotd.server)).getString();
        return TextParserUtils.formatText(text);
    }
}
