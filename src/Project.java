import java.util.LinkedList;

public class Project implements Comparable<Project> {
	public String name;
	public LinkedList<Skill> skills;
	public int remaining;
	public int point;
	public int bestBefore;
	public double sortPoint;
	public boolean active;
	public Ctrb[] emp;
	public int[] idx;
	public Project(String name, int remaining, int point, int bestBefore) {
		this.name = name;
		this.remaining = remaining;
		this.point = point;
		this.bestBefore = bestBefore;
		this.active = false;
		skills = new LinkedList<Skill>();
	}
	public void addSkill(String skillName, int value) {
		Skill sk = new Skill(skillName, value);
		skills.add(sk);
	}
	public Skill searchSkill(String skillName) {
		for (int i = 0; i < skills.size(); i++) {
			if (skills.get(i).name.equals(skillName)) {
				return skills.get(i);
			}
		}
		return null;
	}
	public void addEmp(Ctrb[] emp) {
		this.emp = emp;
	}
	public void addidx(int[] idx) {
		this.idx = idx;
	}
	@Override
	public int compareTo(Project o) {
		return o.sortPoint > this.sortPoint ? 1 : -1;
	}
}
