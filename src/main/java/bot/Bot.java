package bot;

import helper.ReadFileData;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ForceReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;

public class Bot extends TelegramLongPollingBot {

    private static ReadFileData sData = new ReadFileData();

    private long chatId;

    public String getBotToken() {
        return sData.getBotToken();
    }



    public void onUpdateReceived(Update update) {
        chatId = update.getMessage().getChatId();
        SendMessage msg = new SendMessage();

        String text = update.getMessage().getText();
        String mark = String.format("*Hello, %s!*\n" +
                "_Italic text_", update.getMessage().getFrom().getFirstName());
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setSelective(true);
        keyboardMarkup.setResizeKeyboard(true);
        keyboardMarkup.setOneTimeKeyboard(false);
        ArrayList<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow firstRow = new KeyboardRow();
        KeyboardRow secondRow = new KeyboardRow();
        KeyboardRow thirdRow = new KeyboardRow();

        switch (text) {
            case "Ответить":
                msg.setChatId(chatId);
                msg.setText("Отвечаю");
                ForceReplyKeyboard replyKeyboard = new ForceReplyKeyboard();
                replyKeyboard.getForceReply();
                msg.setReplyMarkup(replyKeyboard);
                break;
            case "Закрыть":
                msg.setChatId(chatId);
                msg.setText("Закрываю клавиатуру");
                ReplyKeyboardRemove remove = new ReplyKeyboardRemove();
                remove.getRemoveKeyboard();
                msg.setReplyMarkup(remove);
                break;
            default:
                firstRow.add(new KeyboardButton("Отправить местоположение").setRequestLocation(true));
                secondRow.add("Ответить");
                secondRow.add("Закрыть");
                thirdRow.add(new KeyboardButton("Отправить контакт").setRequestContact(true));
                keyboard.add(firstRow);
                keyboard.add(secondRow);
                keyboard.add(thirdRow);
                keyboardMarkup.setKeyboard(keyboard);
                msg.setChatId(chatId);
                msg.setReplyMarkup(keyboardMarkup);
                msg.setText("Выбрать...");
                break;
        }

        try {
            execute(msg);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public String getBotUsername() {
        return sData.getBotUsername();
    }
}
