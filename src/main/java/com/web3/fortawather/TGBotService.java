package com.web3.fortawather;

import com.google.common.collect.Maps;
import com.vdurmont.emoji.EmojiParser;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChatAdministrators;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.chatmember.ChatMemberAdministrator;
import org.telegram.telegrambots.meta.api.objects.chatmember.ChatMemberOwner;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class TGBotService {

    @Autowired
    private FortaWatcherBot fortaWatcherBot;

    @Autowired
    private RestTemplate restTemplate;


    private Map<String, String> cache = Maps.newConcurrentMap();

    private final String ok = EmojiParser.parseToUnicode(":white_check_mark:");
    private final String bad = EmojiParser.parseToUnicode(":x:");
    @Autowired
    public WatcherCfg watcherCfg;

    @Scheduled(cron = "0 0/1 * * * *")
    public void sendMessage() {
        watcherCfg.getAddressList()
                .forEach(address -> {
                    try {
                        Thread.sleep(Duration.ofMillis(300).toMillis());
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    SendMessage build = SendMessage.builder()
                            .chatId(watcherCfg.getChatId())
                            .text(getStatistics(address))
                            .build();
                    build.enableMarkdown(true);
                    try {
                        fortaWatcherBot.execute(build);
                    } catch (TelegramApiException e) {
                        log.error("TelegramApiException", e);
                    }
                });

    }

    private String getStatistics(String address) {

        String url = "https://api.forta.network/stats/sla/scanner/%s".formatted(address);

        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        log.info("{}", response);
        if (response.getStatusCodeValue() == HttpStatus.OK.value()) {
            JSONObject respBody = new JSONObject(response.getBody());
            String scannerId = respBody.getString("scannerId");
            JSONObject statistics = respBody.getJSONObject("statistics");
            String lowestScoresTime = getBangkokTimeZoneTime(respBody
                    .getJSONArray("lowestScores")
                    .getJSONObject(0)
                    .getString("minute"));

            boolean ok = getStatus(scannerId, lowestScoresTime, statistics);
            return """
                    %s
                    scannerId：
                    %s
                    startTime：
                    %s
                    endTime:
                    %s
                    lowestScoresTime：
                    %s
                    statistics：
                    %s
                    """.formatted(
                    ok ? this.ok : bad,
                    scannerId,
                    getBangkokTimeZoneTime(respBody.getString("startTime")),
                    getBangkokTimeZoneTime(respBody.getString("endTime")),
                    lowestScoresTime,
                    statistics
            ).concat(ok ? "" : getAdmin());
        }

        return "请稍后";
    }

    public boolean getStatus(String scannerId, String lowestScoresTime, JSONObject statistics) {
//        if (Double.parseDouble(statistics.getString("min"))<0.8) {
//            String warnTime = cache.computeIfPresent(scannerId,(k,v)->{
//                LocalDateTime cacheTime = LocalDateTime.parse(v, DateTimeFormatter.ofPattern(DateUtils.YYYY_MM_DD_HH_MM_SS));
//            });
//
//        }
//
//        boolean ok = statistics.getDouble("min") > 0.8;
//        boolean ok = statistics.getDouble("avg") > 0.8;
        return statistics.getDouble("min") > 0.9;

    }

    public String getBangkokTimeZoneTime(String time) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        if (time.lastIndexOf(".") > -1) {
            time = time.substring(0, time.lastIndexOf("."));
        } else {
            time = time.substring(0, time.lastIndexOf("Z"));
        }
        LocalDateTime localDateTime = LocalDateTime.parse(time,
                dateTimeFormatter).plusHours(8);

        return localDateTime.format(DateTimeFormatter.ofPattern(Constant.YYYY_MM_DD_HH_MM_SS));
    }

    public String getAdmin() {
        String chatId= watcherCfg.getChatId();
        GetChatAdministrators admins = GetChatAdministrators.builder().chatId(chatId).build();
        try {
            return Optional.ofNullable(fortaWatcherBot.execute(admins))
                    .orElse(new ArrayList<>())
                    .stream()
                    .filter(chatMember -> {
                        final String status = chatMember.getStatus();
                        return status.equals(ChatMemberOwner.STATUS)
                                || status.equals(ChatMemberAdministrator.STATUS);
                    })
                    .map(member -> """
                            [%s](tg://user?id=%d)
                            """
                            .formatted(
                                    member.getUser().getUserName(),
                                    member.getUser().getId()
                            )
                    ).collect(Collectors.joining("\r\n"));
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}
