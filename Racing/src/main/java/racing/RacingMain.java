package racing;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import racing.menu.MainMenu;

public class RacingMain {	
	public static void main(String[] args) throws IOException {
		Logger.getLogger("org.hibernate").setLevel(Level.OFF);
		Logger.getLogger("com.mysql").setLevel(Level.OFF);
		
		MainMenu menu = new MainMenu();
		menu.launch();
	}
}