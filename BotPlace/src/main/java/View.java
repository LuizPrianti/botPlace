import java.io.IOException;
import java.util.List;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.TelegramBotAdapter;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ChatAction;
import com.pengrad.telegrambot.model.request.Keyboard;
import com.pengrad.telegrambot.model.request.KeyboardButton;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.GetUpdates;
import com.pengrad.telegrambot.request.SendChatAction;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.BaseResponse;
import com.pengrad.telegrambot.response.GetUpdatesResponse;
import com.pengrad.telegrambot.response.SendResponse;

public class View implements Observer {

	TelegramBot bot = TelegramBotAdapter.build("683241741:AAGdvzJy6zZzKiwLbM7sUUJm0hNG7NHlG6A");

	// Object that receives messages
	GetUpdatesResponse updatesResponse;
	// Object that send responses
	SendResponse sendResponse;
	// Object that manage chat actions like "typing action"
	BaseResponse baseResponse;

	int queuesIndex = 0;

	ControllerSearch controllerSearch; // Strategy Pattern -- connection View -> Controller

	
	String categoria = "";
	
	boolean searchBehaviour = false;

	private Model model;

	public View(Model model) {
		this.model = model;
	}

	public void setControllerSearch(ControllerSearch controllerSearch) { // Strategy Pattern
		this.controllerSearch = controllerSearch;
	}

	ControllerSearchInstituicao controllerSearchInstituicao = new ControllerSearchInstituicao(model, this);

	public void receiveUsersMessages() {

		// infinity loop
		while (true) {

			// taking the Queue of Messages
			updatesResponse = bot.execute(new GetUpdates().limit(100).offset(queuesIndex));

			// Queue of messages
			List<Update> updates = updatesResponse.updates();
			
			// taking each message in the Queue
			for (Update update : updates) {
				
				// updating queue's index
				queuesIndex = update.updateId() + 1;

				if (this.searchBehaviour == true) {
					this.callController(update);

				} else if (update.message().text().equals("/start")) {
					setControllerSearch(new ControllerSearchInstituicao(model, this));
					sendResponse = bot.execute(new SendMessage(update.message().chat().id(), "O que está procurando?"));

				} else if (update.message().text().equals("Teatro") || (update.message().text().equals("Ensino Médio"))
						|| (update.message().text().equals("Ensino Fundamental"))
						|| (update.message().text().equals("Universidade"))) {
					categoria = update.message().text();
					setControllerSearch(new ControllerSearchInstituicao(model, this));

					// InlineKeyboard button = new InlineKeyboard("Enviar Localizaçao");
					KeyboardButton button = new KeyboardButton("Enviar Localizaçao");
					KeyboardButton[][] listButtons = { { button.requestLocation(true) } };
					ReplyKeyboardMarkup reply = new ReplyKeyboardMarkup(listButtons).resizeKeyboard(true)
							.oneTimeKeyboard(true);
					// bot.execute(new SendMessage(id, message).replyMarkup(keyboard));
					sendResponse = bot.execute(new SendMessage(update.message().chat().id(), "Me envie sua localização")
							.replyMarkup(reply));

					// sendResponse = bot.execute(new SendLocation(update.message().chat().id(),
					// latitude, longitude));

					this.searchBehaviour = true;
				} 
				
				if (update.message().location() != null) {
					String latlon = update.message().location().latitude() + ","
							+ update.message().location().longitude();
					System.out.println(latlon);
					System.out.println(categoria);
					try {
						controllerSearchInstituicao.searchAPI(latlon, categoria);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					
					categoria = "";
				}

			}
			
			

		}

	}

	public void callController(Update update) {
		this.controllerSearch.search(update);
	}

	public void update(long chatId, String studentsData) {
		sendResponse = bot.execute(new SendMessage(chatId, studentsData));
		this.searchBehaviour = false;
	}

	public void sendTypingMessage(Update update) {
		baseResponse = bot.execute(new SendChatAction(update.message().chat().id(), ChatAction.typing.name()));
	}

}