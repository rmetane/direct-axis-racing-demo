package racing.menu;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import racing.RacingMain;
import racing.entity.Car;
import racing.entity.Race;
import racing.entity.RaceOutcome;
import racing.entity.Track;
import racing.repository.IRepository;
import racing.repository.factory.RepositoryFactory;

public class RaceMenu extends MainMenu{
	private static final Logger LOGGER = Logger.getLogger(RacingMain.class.getName());

	private BufferedReader reader;
	private RepositoryFactory repoFactory;
	
	public RaceMenu(BufferedReader reader) {
		this.reader = reader;
		repoFactory = new RepositoryFactory();
	}
	
	/**
	 * Launches and handle menu items for new races
	 */
	public boolean launch() {
		int count = 0;
		boolean showRacingMenu = true;
		
		do {
			System.out.println("____________________________________________________________________________");
			System.out.println("TRACK OPTIONS");
			System.out.println("------------------------");
			System.out.println("  1. Suppy New Track.");
			System.out.println("  2. Use An Existing Track.");
			System.out.println("  ----------------------");
			System.out.println("  3. Main Menu.");
			System.out.println();
			System.out.print("Please select: ");
		
			int option = getOptionAsInt();
		
			switch(option) {
			case 1:
				System.out.println();
				raceOnNewTrack();
				return true;				
			case 2:
				System.out.println();
				raceOnExistingTrack();
				return true;
			case 3:
				return true;
			default:
				System.out.println("____________________________________________________________________________");			
				
				if(++count >= 3) {
					System.out.println("Exiting...");
					return false;
				} else {
					System.out.println("Invalid option selected.");
					System.out.println();
					System.out.print("NB: system will exit after " + (3 - count) + " more failed ");
					System.out.println(count > 1 ? "attempt." : "attempts.");
				}
			}
		} while(showRacingMenu);
		
		return true;
	}
		
	private String getOptionAsString()  {	
		try {
			return reader.readLine();			
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "Failed to read input", e);
			return "";
		}
	}

	private void raceOnNewTrack() {
		int count = 0;
		String trackSequence;
		
		trackSequence = captureTrack();
	
		while(!validateTrack(trackSequence) && count < 3) {
			printErrorMessage("Invalid track supplied. Track should only contain 0s and 1s.");
			count++;			
			
			if(count < 3) {
				trackSequence = captureTrack();
			} else {
				System.out.println("Returning to Main Menu...");
				System.out.println();
				pause();
				return;
			}
		}
		
		Track track = new Track();
		track.setSequence(trackSequence);
		
		try(IRepository<Track> trackRepo = repoFactory.getRepository(Track.class)){
			trackRepo.create(track);
			processRace(track);
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "Something went terribly wrong...", e);
		}
	}

	private String captureTrack() {
		System.out.println("____________________________________________________________________________");
		System.out.println("SUPPLY TRACK");
		System.out.println("---------------");
		System.out.println();
		System.out.print("Please specify track: ");
		
		return getOptionAsString().trim();
	}

	private boolean validateTrack(String trackSequence) {
		return !trackSequence.matches("[^01]{0,}");
	}
	
	private void raceOnExistingTrack() {
		int count = 0;
		
		List<Track> tracks = retrieveTracks();
		List<Integer> trackIdList = tracks.stream().map(tr -> tr.getId()).collect(Collectors.toList());
	
		int option = getSelectedTrack(tracks);
	
		while(!trackIdList.contains(option) && count < 3) {
			printValidValueErrorMessage("Invalid track select.", trackIdList.toString());
			count++;			
			
			if(count < 3) {
				option = getSelectedTrack(tracks);
			} else {
				System.out.println("Returning to Main Menu...");
				System.out.println();
				pause();
				return;
			}
		}
		
		final int finalOption = option;
		Track track = tracks.stream().filter(tr -> tr.getId() == finalOption).findFirst().get();
		
		try(IRepository<Track> trackRepo = repoFactory.getRepository(Track.class)){
			processRace(track);
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "Something went terribly wrong...", e);
		}
	}
	
	private int getSelectedTrack(List<Track> tracks) {
		System.out.println("____________________________________________________________________________");
		System.out.println("EXISTING TRACKS");
		System.out.println("---------------");
		tracks.stream().forEach(track -> System.out.println("  " + track.getId() + ". " + track.getSequence()));
		System.out.println();
		System.out.print("Please select a track: ");
	
		return getOptionAsInt();
	}
	
	private List<Track> retrieveTracks() {
		
		try(IRepository<Track> trackDAO = repoFactory.getRepository(Track.class)){
			return trackDAO.findAll();
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "Something went terribly wrong...", e);
		}
		
		return new ArrayList<Track>();
	}
	
	private void processRace(Track track) {
		try(IRepository<Car> carRepo = repoFactory.getRepository(Car.class)){
			List<Car> cars = carRepo.findAll();
			
			displayRacingCarsDetails(cars);
			Optional<Race> optRace = race(cars,track);
			
			if(optRace.isPresent()) {
				displayRaceResults(optRace.get());
			} else {
				printErrorMessage("A horrible crash just occured in the track, someone please call 911!");
			}
			
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "Something went terribly wrong...", e);
		}
	}

	private Optional<Race> race(List<Car> cars, Track track) {
		int straightCount = (int)track.getSequence().chars().filter(ch -> ch == '1').count();
		int cornerCount = (int)track.getSequence().chars().filter(ch -> ch == '0').count();
		Race race = new Race();
		race.setTrack(track);
		race.setRaceTime(new Date());
		
		for(Car car : cars) {
			int raceScore = straightCount * (car.getTopSpeed() + car.getAcceleration()) + cornerCount * (car.getCorneringAbility() + car.getBraking());
			RaceOutcome outcome = new RaceOutcome();
			outcome.setScore(raceScore);
			outcome.setCar(car);
			race.addRaceOutcome(outcome);
		}
		
		try(IRepository<Race> raceRepo = repoFactory.getRepository(Race.class)){
			raceRepo.create(race);
		} catch (Exception e) {
			e.printStackTrace();
			return Optional.empty();
		}
		
		return Optional.of(race);
	}

	private void displayRacingCarsDetails(List<Car> cars) {
		System.out.println("____________________________________________________________________________");
		System.out.println("The following cars will be participating in the race:");
		System.out.println("-----------------------------------------------------");
		System.out.println();
		System.out.format("%-20s %-12s %-12s %-12s %-12s\n", "Name", "Acceleration", "Braking", "Top Speed", "Cornering Ability");
		System.out.format("%-21s%-13s%-13s%-13s%-13s\n", "-------------------|", "------------|", "-----------|", "-----------|", "-----------------");
		cars.stream().forEach(car -> System.out.format("%-20s %-12d %-12d %-12d %-12d\n", car.getName(), car.getAcceleration(), car.getBraking(), car.getTopSpeed(), car.getCorneringAbility()));
		System.out.format("%-21s%-13s%-13s%-13s%-13s\n", "-------------------|", "------------|", "-----------|", "-----------|", "-----------------");
		System.out.format("%-20s %-12s %-12s %-12s %-12s\n", "Name", "Acceleration", "Braking", "Top Speed", "Cornering Ability");
		System.out.println();
		
		pause();
	}
}
