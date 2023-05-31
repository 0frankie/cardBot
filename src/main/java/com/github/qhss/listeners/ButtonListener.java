package com.github.qhss.listeners;

import org.javacord.api.entity.message.MessageBuilder;
import org.javacord.api.entity.message.MessageFlag;
import org.javacord.api.event.interaction.ButtonClickEvent;
import org.javacord.api.listener.interaction.ButtonClickListener;

public class ButtonListener implements ButtonClickListener {

    @Override
    public void onButtonClick(ButtonClickEvent event) {
        switch (event.getButtonInteraction().getCustomId()) {
            case "stand": {

            }
            case "hit": {

            }
            case "double-down": {
                
            }
            case "exit": {
                event.getButtonInteraction().getMessage().delete();
                event.getButtonInteraction().createImmediateResponder().setContent("Exited game").setFlags(MessageFlag.EPHEMERAL).respond();
            }
        }
    }

}