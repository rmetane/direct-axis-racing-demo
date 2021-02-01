package racing.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the track database table.
 * 
 */
@Entity
@NamedQuery(name="Track.findAll", query="SELECT t FROM Track t")
public class Track implements Serializable {
	private static final long serialVersionUID = 1L;
	private int id;
	private String description;
	private String sequence;
	private List<Race> races;

	public Track() {
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


	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}


	public String getSequence() {
		return this.sequence;
	}

	public void setSequence(String sequence) {
		this.sequence = sequence;
	}


	//bi-directional many-to-one association to Race
	@OneToMany(mappedBy="track", cascade = CascadeType.ALL)
	public List<Race> getRaces() {
		return this.races;
	}

	public void setRaces(List<Race> races) {
		this.races = races;
	}

	public Race addRace(Race race) {
		getRaces().add(race);
		race.setTrack(this);

		return race;
	}

	public Race removeRace(Race race) {
		getRaces().remove(race);
		race.setTrack(null);

		return race;
	}

}