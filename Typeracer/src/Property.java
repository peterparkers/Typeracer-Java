import java.io.Serializable;

public class Property implements Serializable {

	private static final long serialVersionUID = 5950169519310163575L;
	private int missed;
	private int score;
	
	public Property(int missed, int score) {
		this.missed = missed;
		this.score = score;
	}
	
	public int getMissed() {
		return missed;
	}
	
	public void setMissed(int missed) {
		this.missed = missed;
	}
	public int getScore() {
		return score;
	}
	
	public void setScore(int score) {
		this.score = score;
	}
	
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		
		Property property = (Property) o;
		
		if (missed != property.missed) return false;
		if (score != property.score) return false;
		return true;
	}
	
	public int hashCode() {
		return missed;
	}
	public String toString() {
		return "Missed = " + getMissed() + " , Score = " + getScore() + " ;";
	}
}