package racing.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the car database table.
 * 
 */
@Entity
@NamedQuery(name="Car.findAll", query="SELECT c FROM Car c")
public class Car implements Serializable {
	private static final long serialVersionUID = 1L;
	private int id;
	private int acceleration;
	private int braking;
	private int corneringAbility;
	private String description;
	private String name;
	private int topSpeed;
	private List<RaceOutcome> raceOutcomes;

	public Car() {
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


	public int getAcceleration() {
		return this.acceleration;
	}

	public void setAcceleration(int acceleration) {
		this.acceleration = acceleration;
	}


	public int getBraking() {
		return this.braking;
	}

	public void setBraking(int braking) {
		this.braking = braking;
	}


	@Column(name="cornering_ability")
	public int getCorneringAbility() {
		return this.corneringAbility;
	}

	public void setCorneringAbility(int corneringAbility) {
		this.corneringAbility = corneringAbility;
	}


	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}


	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}


	@Column(name="top_speed")
	public int getTopSpeed() {
		return this.topSpeed;
	}

	public void setTopSpeed(int topSpeed) {
		this.topSpeed = topSpeed;
	}


	//bi-directional many-to-one association to RaceOutcome
	@OneToMany(mappedBy="car", cascade = CascadeType.ALL)
	public List<RaceOutcome> getRaceOutcomes() {
		return this.raceOutcomes;
	}

	public void setRaceOutcomes(List<RaceOutcome> raceOutcomes) {
		this.raceOutcomes = raceOutcomes;
	}

	public RaceOutcome addRaceOutcome(RaceOutcome raceOutcome) {
		getRaceOutcomes().add(raceOutcome);
		raceOutcome.setCar(this);

		return raceOutcome;
	}

	public RaceOutcome removeRaceOutcome(RaceOutcome raceOutcome) {
		getRaceOutcomes().remove(raceOutcome);
		raceOutcome.setCar(null);

		return raceOutcome;
	}

}