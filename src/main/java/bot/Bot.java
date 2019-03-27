package bot;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import helper.ReadFileData;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
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


        if (update.hasMessage()) {
            SendMessage mMessage = new SendMessage();
            chatId = update.getMessage().getChatId();
            if (update.getMessage().getText().contains("/start")) {
                mMessage.setChatId(chatId);
                mMessage.setText("Inline keyboard");
                InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
                List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
                List<InlineKeyboardButton> firstRow = new ArrayList<>();
                List<InlineKeyboardButton> secondRow = new ArrayList<>();
                firstRow.add(new InlineKeyboardButton("Google").setUrl("https://google.com"));
                secondRow.add(new InlineKeyboardButton("Reply").setCallbackData("reply"));
                secondRow.add(new InlineKeyboardButton("Forward").setCallbackData("forward"));
                keyboard.add(firstRow);
                keyboard.add(secondRow);
                inlineKeyboardMarkup.setKeyboard(keyboard);
                mMessage.setReplyMarkup(inlineKeyboardMarkup);
                try {
                    execute(mMessage);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
        } else if (update.hasCallbackQuery()) {
            SendMessage mMessage = new SendMessage();
            AnswerCallbackQuery answerCallbackQuery = new AnswerCallbackQuery();
            chatId = update.getCallbackQuery().getMessage().getChatId();
            if (update.getCallbackQuery().getData().equals("reply")) {
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                String json = gson.toJson(update.getCallbackQuery());
                mMessage.setChatId(chatId);
                mMessage.setText(json);
                try {
                    execute(mMessage);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
            if (update.getCallbackQuery().getData().equals("forward")) {
                answerCallbackQuery.setCallbackQueryId(update.getCallbackQuery().getId());
//                answerCallbackQuery.setShowAlert(true);
                answerCallbackQuery.setText(update.getCallbackQuery().getData());
                try {
                    execute(answerCallbackQuery);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
        }


    }

    public String getBotUsername() {
        return sData.getBotUsername();
    }
}
