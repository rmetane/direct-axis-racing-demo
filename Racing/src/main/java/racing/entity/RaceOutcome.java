package racing.entity;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the race_outcome database table.
 * 
 */
@Entity
@Table(name="race_outcome")
@NamedQuery(name="RaceOutcome.findAll", query="SELECT r FROM RaceOutcome r")
public class RaceOutcome implements Serializable {
	private static final long serialVersionUID = 1L;
	private int id;
	private int score;
	private Car car;
	private Race race;

	public RaceOutcome() {
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


	public int getScore() {
		return this.score;
	}

	public void setScore(int score) {
		this.score = score;
	}


	//bi-directional many-to-one association to Car
	@ManyToOne
	@JoinColumn(name="car_id")
	public Car getCar() {
		return this.car;
	}

	public void setCar(Car car) {
		this.car = car;
	}


	//bi-directional many-to-one association to Race
	@ManyToOne
	@JoinColumn(name="race_id")
	public Race getRace() {
		return this.race;
	}

	public void setRace(Race race) {
		this.race = race;
	}

}