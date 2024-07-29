package me.lukasabbe.custommotd.config;

import me.lukasabbe.custommotd.Custommotd;
import net.fabricmc.loader.api.FabricLoader;
import org.yaml.snakeyaml.Yaml;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public class Config {
    public String Motd = null;
    public String linkToPhoto = null;
    public int maxPlayerCount = -1;
    public int currentPlayerCount = -1;
    public List<String> playerList = null;
    public int pingLvl = -1;
    public int msLvl = -1;

    public Config(){
        loadConfig();
    }

    private void loadConfig() {
        Path configPath = FabricLoader.getInstance().getConfigDir().resolve("custom-motd-config.yml");
        if(!Files.exists(configPath))createConfig(configPath);
        Yaml yaml = new Yaml();
        try{
            Map<String, Object> configMap = yaml.load(new FileReader(configPath.toFile()));
            if(configMap.containsKey("First-line")){
                Motd = (String) configMap.get("First-line");
            }
            if(configMap.containsKey("link-to-photo")){
                linkToPhoto = (String) configMap.get("link-to-photo");
            }
            if(configMap.containsKey("max-player-count")){
                maxPlayerCount = (int) configMap.get("max-player-count");
            }
            if(configMap.containsKey("player-count")){
                currentPlayerCount = (int) configMap.get("player-count");
            }
            if(configMap.containsKey("plyer-list")){
                playerList = (List<String>) configMap.get("plyer-list");
            }
            if(configMap.containsKey("ping-level")){
                pingLvl = (int) configMap.get("ping-level");
            }
            if(configMap.containsKey("ms-level")){
                msLvl = (int) configMap.get("ms-level");
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void createConfig(Path configPath){
        FabricLoader.getInstance().getModContainer(Custommotd.MOD_ID).ifPresent(modContainer -> {
            Path path = modContainer.findPath("custom-motd-config.yml").orElseThrow();
            try {
                Files.copy(path,configPath);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
    public void reloadConfig(){
        loadConfig();
    }
}
