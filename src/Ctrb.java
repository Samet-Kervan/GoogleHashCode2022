import java.util.ArrayList;

public class Ctrb {
	public String name;
	public ArrayList<Skill> skills;
	public Ctrb(String name) {
		this.name = name;
		skills = new ArrayList<Skill>();
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
	public void addPointToSkill(String skillName) {
		Skill sk = searchSkill(skillName);
		if (sk == null) {
			sk = new Skill(skillName, 1);
			skills.add(sk); 
		}
		else {
			sk.value++;
		}
	}
}
