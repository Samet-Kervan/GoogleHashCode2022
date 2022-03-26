import java.util.Hashtable;

public class Ctrb {
	public String name;
	public Hashtable<String,Skill> skills;
	public String currentSkill;
	public int currentSkillsPoint;
	public Ctrb(String name) {
		this.name = name;
		skills = new Hashtable<String,Skill>();
	}
	public void addSkill(String skillName, int value) {
		Skill sk = new Skill(skillName, value);
		skills.put(skillName, sk);
	}
	public Skill searchSkill(String skillName) {
		return skills.get(skillName);
	}
	public void addPointToSkill(String skillName) {
		Skill sk = searchSkill(skillName);
		sk.value++;
	}
}
