import java.util.LinkedList;

public class Project {
	public String name;
	public LinkedList<Skill> skills;
	public int remaining;
	public int point;
	public int bestBefore;
	public float sortPoint;
	public Project(String name, int remaining, int point, int bestBefore) {
		this.name = name;
		this.remaining = remaining;
		this.point = point;
		this.bestBefore = bestBefore;
		skills = new LinkedList<Skill>();
	}
	public void addSkill(String skillName, int value) {
		Skill sk = searchSkill(skillName);
		if (sk == null) {
			sk = new Skill(skillName, value);
			skills.add(sk);
		}
	}
	public Skill searchSkill(String skillName) {
		for (int i = 0; i < skills.size(); i++) {
			if (skills.get(i).name.equals(skillName)) {
				return skills.get(i);
			}
		}
		return null;
	}
}
