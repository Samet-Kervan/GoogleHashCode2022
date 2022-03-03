
public class Project {
	public String name;
	public DoubleLinkedList<Skill> skills;
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
		skills = new DoubleLinkedList<Skill>();
	}
	public void addSkill(String skillName, int value) {
		Skill sk = new Skill(skillName, value);
		skills.add(sk);
	}
	public Skill searchSkill(String skillName) {
		for (int i = 0; i < skills.getSize(); i++) {
			if (skills.get(i).getContent().name.equals(skillName)) {
				return skills.get(i).getContent();
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
}
