package racing.menu;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import racing.entity.Race;
import racing.repository.IRepository;
import racing.repository.factory.RepositoryFactory;

public class ResultsMenu extends MainMenu {
private static final Logger LOGGER = Logger.getLogger(ResultsMenu.class.getName());
	
	private RepositoryFactory repoFactory;
	private static final String GENERAL_EXC_MSG = "Something went terribly wrong...";
	private static final String INVALID_TRACK_MSG = "Invalid track select.";
	
	public ResultsMenu(BufferedReader reader) {
		repoFactory = new RepositoryFactory();
	}
	
	/**
	 * Launches and handle race results menu items
	 */
	public boolean launch() {
		int count = 0;
		List<Race> races = retrieveRace();		
		List<Integer> raceIdList = races.stream().map(tr -> tr.getId()).collect(Collectors.toList());
	
		int option = getSelectedRace(races);
	
		while(!raceIdList.contains(option) && count < 3) {
			printValidValueErrorMessage(INVALID_TRACK_MSG, raceIdList.toString());
			count++;			
			
			if(count < 3) {
				option = getSelectedRace(races);
			} else {
				System.out.println("Returning to Main Menu...");
				System.out.println();
				pause();
				return true;
			}
		}
				
		try(IRepository<Race> raceRepo = repoFactory.getRepository(Race.class)){
			displayRaceResults(raceRepo.find(option).get());
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, GENERAL_EXC_MSG, e);
		}
		
		return true;
	}
	
	private int getSelectedRace(List<Race> races) {
		System.out.println("____________________________________________________________________________");
		System.out.println("RACES");
		System.out.println("---------------");
		races.stream().forEach(race -> System.out.println("  " + race.getId() + ". " + race.getRaceTime() + ". " + race.getTrack().getSequence()));
		System.out.println();
		System.out.print("Please select a race: ");
	
		return getOptionAsInt();
	}
	
	private List<Race> retrieveRace() {
		
		try(IRepository<Race> raceDAO = repoFactory.getRepository(Race.class)){
			return raceDAO.findAll();
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, GENERAL_EXC_MSG, e);
		}
		
		return new ArrayList<Race>();
	}
}
