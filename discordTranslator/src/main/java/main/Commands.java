package main;

import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.MessageReaction;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.exceptions.ErrorResponseException;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.ErrorResponse;

import com.google.cloud.translate.*;
import com.google.cloud.translate.Translate.TranslateOption;
import com.google.cloud.translate.Translation;

import java.util.Hashtable;
import java.awt.*;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;


public class Commands extends ListenerAdapter {
    public String currentLanguage = "en";
    public String prefix = "!";

    @Override
    //commands that are executed after being triggered by a message from a user
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        String[] args = event.getMessage().getContentRaw().split(" ");

        //translates the text in the current language
        if (args[0].equals(prefix + "translate")) {
            Message input = event.getMessage();

            String rawMessage = input.getContentRaw();
            rawMessage = rawMessage.replace("!translate", "");
            Translate translate = TranslateOptions.newBuilder().setApiKey("AIzaSyDznFljEcbTE6WBjg0_SBjlDRbhNb1YVs4").build().getService();

            Translation translation = translate.translate(
                    rawMessage,
                    TranslateOption.targetLanguage(currentLanguage) // trying smtb sorry
            );
            System.out.println(translation);
            String finalText = translation.getTranslatedText().replace("&#39;", "'");
            finalText = finalText.replace("&quot;", "\"");

                    event.getMessage().reply(finalText).queue();
        }

        //translates one message in the designated language. does not change the main current language
        if (args[0].equals(prefix + "langtranslate")) {
            String langTemp = currentLanguage;
            Message msg = event.getMessage();
            String rawMsg = msg.getContentRaw();
            rawMsg = rawMsg.replace("!langtranslate ", "");
            char firstLang = rawMsg.charAt(0);
            char secondLang = rawMsg.charAt(1);
            String lang = String.valueOf(firstLang) + secondLang;
            currentLanguage = lang;
            rawMsg=rawMsg.replace((lang+":"), "");

            Translate translat = TranslateOptions.newBuilder().setApiKey("AIzaSyDznFljEcbTE6WBjg0_SBjlDRbhNb1YVs4").build().getService();

            Translation translatio = translat.translate(
                    rawMsg,
                    TranslateOption.targetLanguage(currentLanguage) // trying smtb sorry
                    );

            String finalText = translatio.getTranslatedText().replace("&#39;", "'");
            finalText = finalText.replace("&quot;", "\"");
            event.getMessage().reply(finalText).queue();
            currentLanguage = langTemp;
        }

        //sets the language
        if (args[0].equals(prefix + "language")) {
            Message input = event.getMessage();
            String rawMessage = input.getContentRaw();
            rawMessage = rawMessage.replace("!language ", "");

            Hashtable<String, String> language_dict = new Hashtable<String, String>();
            language_dict.put("spanish", "es");
            language_dict.put("french", "fr");
            language_dict.put("german", "de");
            language_dict.put("hindi", "hi");
            language_dict.put("chinese", "zh");
            language_dict.put("english", "en");
            language_dict.put("dutch", "nl");
            language_dict.put("japanese", "ja");
            language_dict.put("portuguese", "pt");

            if (language_dict.containsKey(rawMessage)) {
                currentLanguage = language_dict.get(rawMessage);
            } else {
                event.getMessage().reply("Unknown language").queue();
            }

            event.getMessage().reply("The language is now set to " + currentLanguage).queue();
        }

        //lists all the available languages
        if (args[0].equals(prefix + "list")) {
            EmbedBuilder languageList = new EmbedBuilder();
            languageList.setTitle("Languages");
            languageList.addField("Spanish (es)", ":flag_es:", true);
            languageList.addField("French (fr)", ":flag_fr:", true);
            languageList.addField("German (de)", ":flag_de:", true);
            languageList.addField("Hindi (hi)", ":flag_in:", true);
            languageList.addField("Chinese (zh)", ":flag_cn:", true);
            languageList.addField("English (en)", ":flag_gb:", true);
            languageList.addField("Dutch (nl)", ":flag_nl:", true);
            languageList.addField("Japanese (ja)", ":flag_jp:", true);
            languageList.addField("Portuguese (pt)", ":flag_pt:", true);
            event.getMessage().reply(languageList.build()).queue();
        }

        //Gives a little intro message in the server
        if ((args[0].equals(prefix + "info"))){
            EmbedBuilder reactions = new EmbedBuilder();
            reactions.setTitle("Info");
            reactions.addField("About This Bot", "This bot allows you to translate messages and text in many different ways.", false);
            reactions.addField("Current Language:", currentLanguage, false);
            event.getMessage().reply(reactions.build()).queue();
        }

        //lists all the commands and syntax
        if (args[0].equals(prefix + "help")) {
            EmbedBuilder help = new EmbedBuilder();
            help.setTitle("Help Menu");
            help.addField("Case Sensitivity", "All commands are case sensitive!", false);
            help.addField("!help", "displays the help menu (this menu)", false);
            help.addField("!info", "Gives description of the bot and what the current language is.", false);
            help.addField("!language [language]", "used to switch the language which you want the text to be translated to", false);
            help.addField("!translate [text]", "Translates [text] to the language specified using !Language", false);
            help.addField("!list", "Lists all the languages possible and the abbreviations for them", false);
            help.addField("!reactions", "Gives a brief description of how to use reactions to translate messages in the chat", false);
            help.addField("!langtranslate [two letter language code]: [text])", "used to translate something in the specified language for only one message", false);
            help.addField("!kitty", "^w^", false);
            help.setColor(Color.BLUE);
            event.getMessage().reply(help.build()).queue();
        }

        //explains how to use reactions to translate
        if (args[0].equals(prefix + "reactions")) {
            EmbedBuilder reactions = new EmbedBuilder();
            reactions.setTitle("Message Reactions");
            reactions.addField("How to use reactions to translate messages", "React to the message with the emoji associated with your desired language according to the list through !list.", false);
            event.getMessage().reply(reactions.build()).queue();
        }

        //kitty pics :D
        if (args[0].equals(prefix + "kitty")) {
            try{
                HttpResponse<String> response = Unirest.get("https://nijikokun-random-cats.p.rapidapi.com/random/kitten")
                        .header("x-rapidapi-host", "nijikokun-random-cats.p.rapidapi.com")
                        .header("x-rapidapi-key", "5d78dababcmsh8c59761c35ab37bp1b8970jsn9ea85ecccf86")
                        .asString();
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                JsonParser jp = new JsonParser();
                JsonElement je = jp.parse(response.getBody());
                String prettyJsonString = gson.toJson(je);
                String[] facts = prettyJsonString.split("\"");
                String theString = "";
                for (int i = 0; i < facts.length; i++){
                    if (facts[i].contains("http")){
                        theString = facts[i];
                    }
                }
                event.getMessage().reply(theString).queue();
            } catch (UnirestException e){
                event.getMessage().reply(e.getMessage()).queue();
            }
        }
    }

//this segment of the code is responsible for translating any messages that are reacted to
//using a country's flag. The message is translated to the language of the country
    public void onMessageReactionAdd(MessageReactionAddEvent e) {
        MessageReaction reaction = e.getReaction(); //gets the reaction as datatype MessageReaction
        MessageReaction.ReactionEmote emote = reaction.getReactionEmote(); //gets the emote of the reaction
        MessageChannel channel = e.getChannel(); //gets the channel that the reaction was in
        String codePoints = emote.getAsCodepoints(); //gets the codepoints for the reaction emote
        String id = e.getMessageId(); //gets the id of the message that got reacted to

        String lang;
        //detects which flag the reaction is and sets the language to the appropriate language
        if (codePoints.equals("U+1f1eaU+1f1f8")) {  //spanish
            lang = "es";
        } else if (codePoints.equals("U+1f1ebU+1f1f7")) { //french
            lang = "fr";
        } else if (codePoints.equals("U+1f1e9U+1f1ea")) { //german
            lang = "de";
        } else if (codePoints.equals("U+1f1f3U+1f1f1")) { //dutch
            lang = "nl";
        } else if (codePoints.equals("U+1f1eeU+1f1f3")) { //hindi
            lang = "hi";
        } else if (codePoints.equals("U+1f1e8U+1f1f3")) { //chinese
            lang = "zh";
        } else if (codePoints.equals("U+1f1ecU+1f1e7")) { //english
            lang = "en";
        } else if (codePoints.equals("U+1f1efU+1f1f5")) { //japanese
            lang = "ja";
        } else if (codePoints.equals("U+1f1f5U+1f1f9")) { //portugese
            lang = "pt";
        } else {
            return;
        }

        channel.retrieveMessageById(id).queue((message) -> {    //get the message text from the message id

            //translate the message text
            Translate translat = TranslateOptions.newBuilder().setApiKey("AIzaSyDznFljEcbTE6WBjg0_SBjlDRbhNb1YVs4").build().getService();
            Translation translatio = translat.translate(
                    message.getContentRaw(),
                    TranslateOption.targetLanguage(lang) // trying smtb sorry
            );
            //Fix issues with quotes not showing up correctly
            String finalText = translatio.getTranslatedText().replace("&#39;", "'");
            finalText = finalText.replace("&quot;", "\"");
            channel.sendMessage(finalText).queue();

        }, (failure) -> {
            // if the retrieve request failed this will be called
            if (failure instanceof ErrorResponseException) {
                ErrorResponseException ex = (ErrorResponseException) failure;
                if (ex.getErrorResponse() == ErrorResponse.UNKNOWN_MESSAGE) {
                    // this means the message doesn't exist
                    channel.sendMessage("That message doesn't exist !").queue();
                }
            }
            failure.printStackTrace();
        });
    }
}