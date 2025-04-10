package me.lukasabbe.custommotd.util;

import eu.pb4.placeholders.api.PlaceholderContext;
import eu.pb4.placeholders.api.Placeholders;
import eu.pb4.placeholders.api.parsers.NodeParser;
import me.lukasabbe.custommotd.Custommotd;
import net.minecraft.text.Text;

public class TextParser {
    public static Text formatText(String text){
        text = text.replace("\\u00A7", "§");
        text = text.replace("\\n","\n");
        NodeParser parser = NodeParser.builder().globalPlaceholders().simplifiedTextFormat().legacyAll().build();
        text = Placeholders.parseText(Text.of(text), PlaceholderContext.of(Custommotd.server)).getString();
        return  parser.parseNode(text).toText();
    }
}
