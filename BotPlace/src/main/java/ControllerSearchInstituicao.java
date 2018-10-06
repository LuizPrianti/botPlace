import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.pengrad.telegrambot.model.Update;

public class ControllerSearchInstituicao implements ControllerSearch {

	private Model model;
	private View view;

	public ControllerSearchInstituicao(Model model, View view) {
		this.model = model; // connection Controller -> Model
		this.view = view; // connection Controller -> View
	}

	public void search(Update update) {
		view.sendTypingMessage(update);

	}

	@SuppressWarnings("resource")
	public void searchAPI(String latlon, String categoria) throws IOException {
		String limitePesquisa = "3";

		URL url;

		url = new URL("https://api.foursquare.com/v2/venues/search?ll=" + latlon
				+ "&client_id=WJCBZKPRLMVOR51PALKM3JOUH2EKTW154YHXGGTKLGWLCH01"
				+ "&client_secret=CAKZAZQIPOKGPGZOCNTMMPLBAUBHDS4K5PBBHPCVYLGLMMO2" + "&v=20180609" + "&query="
				+ categoria + "&limit=" + limitePesquisa);

		Scanner scanner;
		scanner = new Scanner(url.openStream());
		String json = scanner.next();
		while (scanner.hasNext())
			json += " " + scanner.next();

		System.out.println(json);
	}
}
