package com.web3.fortawather;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@Slf4j
public class FortaWatcherBot extends TelegramLongPollingBot {


    @Autowired
    public WatcherCfg watcherCfg;


    @Override
    public String getBotToken() {
        return watcherCfg.getBotToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        log.info("{}", update);

    }






    @Override
    public String getBotUsername() {
        return "fortaWatcher";
    }


}


