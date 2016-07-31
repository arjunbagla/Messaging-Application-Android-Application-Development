package edu.purdue.sigapp.arjunbagla.chatapplication;

/**
 * Created by arjunbagla on 10/14/15.
 */
public class ChatMessage {

    private String sender, text;

    public ChatMessage() {

    }

    public ChatMessage(String sender, String text) {
        this.sender = sender;
        this.text = text;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}