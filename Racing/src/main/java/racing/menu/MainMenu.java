package racing.menu;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Comparator;
import java.util.logging.Level;
import java.util.logging.Logger;

import racing.RacingMain;
import racing.entity.Race;
import racing.entity.RaceOutcome;

public class MainMenu {
private static final Logger LOGGER = Logger.getLogger(RacingMain.class.getName());
	
	private BufferedReader reader;
	public boolean launch() {
		int count = 0;
		boolean continueRacing = true;
		
		RaceMenu raceMenu = new RaceMenu(getReader());
		ResultsMenu resultsMenu = new ResultsMenu(getReader());
		
		do {
			System.out.println("____________________________________________________________________________");
			System.out.println("MAIN MENU");
			System.out.println("------------------");
			System.out.println("  1. Start Race.");
			System.out.println("  2. Race Results.");
			System.out.println("  ----------------");
			System.out.println("  3. Exit.");
			System.out.println();
			System.out.print("Please select: ");
		
			int option = getOptionAsInt();
			
			if(continueRacing) {
				System.out.println();	
			}
		
			switch(option) {
			case 1:
				continueRacing = raceMenu.launch();
				count = 0;
				break;
			case 2:
				continueRacing = resultsMenu.launch();
				count = 0;
				break;
			case 3:
				System.out.println("____________________________________________________________________________");
				System.out.println("Goodbye...");
				continueRacing = false;
				break;
			default:
				System.out.println("____________________________________________________________________________");			
				
				if(++count >= 3) {
					continueRacing = false;
					System.out.println("Exiting...");
				} else {
					printValidValueErrorMessage("Invalid option selected.", "[1, 2, 3]");
					System.out.println();
					System.out.print("NB: system will exit after " + (3 - count) + " more failed ");
					System.out.println(count > 1 ? "attempt." : "attempts.");
				}
			}
			
			if(continueRacing) {
				System.out.println("____________________________________________________________________________");	
			}
			
		} while(continueRacing);
		
		closeReader();
		
		return false;
	}

	private void closeReader() {
		try {
			getReader().close();
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, "Failed to close reader.", e);
		}
	}

	protected int getOptionAsInt()  {	
		try {
			String input = getReader().readLine();
			return Integer.parseInt(input);			
		} catch (Exception e) {
			return -1;
		}
	}
	
	protected void pause() {
		try {
			System.out.print("Press ENTER to continue...");
			getReader().readLine();
			System.out.println();
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, "Something went terribly wrong...", e);
		}
	}
	
	protected BufferedReader getReader() {
		if(reader == null) {
			reader = new BufferedReader(new InputStreamReader(System.in));
		}

		return reader;
	}
	
	protected void printErrorMessage(String message) {
		System.out.println();
		System.out.println("____________________________________________________________________________");
		System.out.println("ERROR");
		System.out.println("-----");
		System.out.println();
		System.out.println(message);
		System.out.println();
	}
	
	protected void printValidValueErrorMessage(String message, String validValues) {
		System.out.println();
		System.out.println("____________________________________________________________________________");
		System.out.println("ERROR");
		System.out.println("-----");
		System.out.println();
		System.out.println(message);
		System.out.println("Valid values: " + validValues);
		System.out.println();
	}
	
	protected void displayRaceResults(Race race) {
		race.getRaceOutcomes().sort(Comparator.comparing(RaceOutcome::getScore).reversed());
		
		System.out.println();
		System.out.println("____________________________________________________________________________");
		System.out.println("RACE RESULTS:");
		System.out.println("-------------");
		System.out.println();
		System.out.format("%-20s %-5s\n", "Name", "Score");
		System.out.format("%-20s %-5s\n", "--------------------", "-----");
		race.getRaceOutcomes().stream().limit(3).forEach(outcome -> System.out.format("%-20s %-5d\n", outcome.getCar().getName(), outcome.getScore()));
		System.out.println();
		
		pause();
	}
}