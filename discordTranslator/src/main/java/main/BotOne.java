package main;

import javax.security.auth.login.LoginException;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;

public class BotOne {
    public static void main(String[] args) throws Exception{
        JDABuilder jda = JDABuilder.createDefault("ODk2NDUxOTQ1NTgwMDc3MDk2.YWHT7w.-4yxaDE30IEu_8ToO80fOtpLGaQ");
        jda.setActivity(Activity.watching("!help"));
        jda.setStatus(OnlineStatus.ONLINE);
        jda.addEventListeners(new Commands());
        jda.build();
    }

}
