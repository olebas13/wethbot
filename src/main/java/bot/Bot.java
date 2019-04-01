package bot;

import helper.ReadFileData;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.ForwardMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

public class Bot extends TelegramLongPollingBot {

    private static ReadFileData sData = new ReadFileData();

    private Long chatId;
    private Integer messageId;
    private SendMessage msg;


    public String getBotToken() {
        return sData.getBotToken();
    }

    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            chatId = update.getMessage().getChatId();
            if (update.getMessage().getText().equals("/start")) {
                msg = new SendMessage();
                msg.setChatId(chatId);
                msg.setText("Inline keyboard");
                msg.setReplyMarkup(getInlineKeyboard());
            }
            try {
                execute(msg);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        } else if (update.hasCallbackQuery()) {
            AnswerCallbackQuery callbackQuery = new AnswerCallbackQuery();
            callbackQuery.setCallbackQueryId(update.getCallbackQuery().getId());
            messageId = update.getCallbackQuery().getMessage().getMessageId();
            switch (update.getCallbackQuery().getData()) {
                case "forward":
                    ForwardMessage forwardMessage = new ForwardMessage(chatId, chatId, messageId);
                    try {
                        execute(forwardMessage);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                    break;
                case "reply":
                    msg = new SendMessage();
                    msg.setChatId(chatId);
                    msg.setText("Отвечаем на сообщение");
                    msg.setReplyToMessageId(messageId);
                    try {
                        execute(msg);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                    break;
                case "edit":
                    EditMessageText editMessageText = new EditMessageText();
                    editMessageText.setText(msg.getText() + " (edited)");
                    editMessageText.setChatId(chatId);
                    editMessageText.setMessageId(messageId);
                    editMessageText.setReplyMarkup(getInlineKeyboard());
                    try {
                        execute(editMessageText);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                    break;
                case "delete":
                    DeleteMessage deleteMessage = new DeleteMessage();
                    deleteMessage.setChatId(chatId);
                    deleteMessage.setMessageId(messageId);
                    try {
                        execute(deleteMessage);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    break;
            }
            try {
                execute(callbackQuery);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    private InlineKeyboardMarkup getInlineKeyboard() {
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        List<InlineKeyboardButton> firstRow = new ArrayList<>();
        List<InlineKeyboardButton> secondRow = new ArrayList<>();
        firstRow.add(new InlineKeyboardButton().setText("Forward").setCallbackData("forward"));
        firstRow.add(new InlineKeyboardButton().setText("Reply").setCallbackData("reply"));
        secondRow.add(new InlineKeyboardButton().setText("Edit").setCallbackData("edit"));
        secondRow.add(new InlineKeyboardButton().setText("Delete").setCallbackData("delete"));
        keyboard.add(firstRow);
        keyboard.add(secondRow);
        keyboardMarkup.setKeyboard(keyboard);
        return keyboardMarkup;
    }

    public String getBotUsername() {
        return sData.getBotUsername();
    }
}
