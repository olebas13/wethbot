package bot;

import helper.ReadFileData;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.AnswerInlineQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.inlinequery.inputmessagecontent.InputTextMessageContent;
import org.telegram.telegrambots.meta.api.objects.inlinequery.result.InlineQueryResult;
import org.telegram.telegrambots.meta.api.objects.inlinequery.result.InlineQueryResultArticle;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

public class Bot extends TelegramLongPollingBot {

    private static ReadFileData sData = new ReadFileData();

    private long chatId;

    public String getBotToken() {
        return sData.getBotToken();
    }

    public void onUpdateReceived(Update update) {
        if (update.hasInlineQuery()) {
            AnswerInlineQuery inlineQuery = new AnswerInlineQuery();
            List<InlineQueryResult> results = new ArrayList<>();
            for (int i = 0; i < 5; i++) {
                InlineQueryResultArticle article = new InlineQueryResultArticle();
                article.setId(Integer.toString(i));
                article.setTitle("Title " + i);
                article.setInputMessageContent(new InputTextMessageContent().setMessageText("Article #" + i).enableMarkdown(true));
                results.add(i, article);
            }
            inlineQuery.setInlineQueryId(update.getInlineQuery().getId());
            inlineQuery.setCacheTime(0);
            inlineQuery.setResults(results);
            try {
                execute(inlineQuery);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    public String getBotUsername() {
        return sData.getBotUsername();
    }
}
