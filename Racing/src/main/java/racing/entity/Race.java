package racing.entity;

import java.io.Serializable;
import javax.persistence.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the race database table.
 * 
 */
@Entity
@NamedQuery(name="Race.findAll", query="SELECT r FROM Race r")
public class Race implements Serializable {
	private static final long serialVersionUID = 1L;
	private int id;
	private Date raceTime;
	private Track track;
	private List<RaceOutcome> raceOutcomes;

	public Race() {
	}


	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(updatable=false)
	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}


	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="race_time")
	public Date getRaceTime() {
		return this.raceTime;
	}

	public void setRaceTime(Date raceTime) {
		this.raceTime = raceTime;
	}


	//bi-directional many-to-one association to Track
	@ManyToOne
	@JoinColumn(name="track_id")
	public Track getTrack() {
		return this.track;
	}

	public void setTrack(Track track) {
		this.track = track;
	}


	//bi-directional many-to-one association to RaceOutcome
	@OneToMany(mappedBy="race",cascade = CascadeType.ALL)
	public List<RaceOutcome> getRaceOutcomes() {
		if(this.raceOutcomes == null) {
			this.raceOutcomes = new ArrayList<RaceOutcome>();
		}
		return this.raceOutcomes;
	}

	public void setRaceOutcomes(List<RaceOutcome> raceOutcomes) {
		this.raceOutcomes = raceOutcomes;
	}

	public RaceOutcome addRaceOutcome(RaceOutcome raceOutcome) {
		getRaceOutcomes().add(raceOutcome);
		raceOutcome.setRace(this);

		return raceOutcome;
	}

	public RaceOutcome removeRaceOutcome(RaceOutcome raceOutcome) {
		getRaceOutcomes().remove(raceOutcome);
		raceOutcome.setRace(null);

		return raceOutcome;
	}

}