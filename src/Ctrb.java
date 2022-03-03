

public class Ctrb {
	public String name;
	public DIBHashTable<String,Skill> skills;
	public boolean active;
	public Ctrb(String name) {
		this.name = name;
		skills = new DIBHashTable<String,Skill>();
		this.active = false;
	}
	public void addSkill(String skillName, int value) {
		Skill sk = new Skill(skillName, value);
		skills.put(skillName, sk);
	}
	public Skill searchSkill(String skillName) {
		return skills.getContent(skillName);
	}
	public void addPointToSkill(String skillName) {
		Skill sk = searchSkill(skillName);
		sk.value++;
	}
}
