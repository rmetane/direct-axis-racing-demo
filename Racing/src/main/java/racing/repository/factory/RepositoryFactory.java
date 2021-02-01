package racing.repository.factory;

import racing.entity.Car;
import racing.entity.Race;
import racing.entity.RaceOutcome;
import racing.entity.Track;
import racing.repository.CarRepository;
import racing.repository.IRepository;
import racing.repository.RaceOutcomeRepository;
import racing.repository.RaceRepository;
import racing.repository.TrackRepository;

public class RepositoryFactory {
	@SuppressWarnings("unchecked")
	public <T> IRepository<T> getRepository(Class<T> clazz){
		if(clazz == Car.class) {
			return (IRepository<T>) new CarRepository();
		}
		
		if(clazz == Race.class) {
			return (IRepository<T>) new RaceRepository();
		}
		
		if(clazz == Track.class) {
			return (IRepository<T>) new TrackRepository();
		}
		
		if(clazz == RaceOutcome.class) {
			return (IRepository<T>) new RaceOutcomeRepository();
		}
		
		return null;
	}
}