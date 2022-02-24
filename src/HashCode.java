import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Scanner;

public class HashCode {
	private LinkedList<Ctrb> cs;
	private LinkedList<Project> projects;
	private PriorityQueue<Project> sortedProjects;
	private int count;
	public HashCode(String fileName) {
		cs = new LinkedList<Ctrb>();
		projects = new LinkedList<Project>();
		readFile(fileName);
		sortedProjects = new PriorityQueue<Project>();
		sortProject();
		selectCtrb();
	}
	private void readFile(String fileName) {
		File file = new File(fileName);
		try {
			Scanner sc = new Scanner(file);
			String start = sc.nextLine();
			String[] numbers = start.split(" ");
			for (int i = 0; i < Integer.parseInt(numbers[0]); i++) {
				String str = sc.nextLine();
				String[] contr = str.split(" ");
				Ctrb ctrb = new Ctrb(contr[0]);
				for (int j = 0; j < Integer.parseInt(contr[1]); j++) {
					str = sc.nextLine();
					String[] skill = str.split(" ");
					ctrb.addSkill(skill[0], Integer.parseInt(skill[1]));
				}
				cs.add(ctrb);
			}
			for (int i = 0; i < Integer.parseInt(numbers[0]); i++) {
				String str = sc.nextLine();
				String[] pr = str.split(" ");
				Project project = new Project(pr[0], Integer.parseInt(pr[1]), Integer.parseInt(pr[2]), Integer.parseInt(pr[3]));
				for (int j = 0; j < Integer.parseInt(pr[4]); j++) {
					str = sc.nextLine();
					String[] skill = str.split(" ");
					project.addSkill(skill[0], Integer.parseInt(skill[1]));
				}
				projects.add(project);
			}
			sc.close();
		} catch (FileNotFoundException e) {
			System.out.println("File does not exists. Try after fixing the problem");
			System.exit(0);
		}
	}
	public void sortProject() {
		for (int i = 0; i < projects.size(); i++) {
			Project prj = projects.get(i);
			int bottom = prj.bestBefore * prj.remaining * prj.skills.size();
			prj.sortPoint = (float) prj.point/(bottom);
			sortedProjects.add(projects.get(i),projects.get(i).sortPoint);
		}
	}
	private void selectCtrb() {
		for (int i = 0; i < sortedProjects.size(); i++) {
			Project pr = sortedProjects.get(i);
			Ctrb[] ele = new Ctrb[pr.skills.size()];
			count = 0;
			for (int j = 0; j < pr.skills.size(); j++) {
				boolean flag = false;
				for (int k = 0; k < cs.size(); k++) {
					Skill cSkill = cs.get(k).searchSkill(pr.skills.get(j).name);
					if (cSkill != null && !cs.get(k).active && cSkill.value == pr.skills.get(j).value) {
						ele[j] = cs.get(k);
						cs.get(k).active = true;
						flag = true;
						break;
					}
				}
				if (!flag) {
					count++;
				}
			}
			if(count != 0) {
				searchCont(ele,pr);
			}
			if (count != 0) {
				
			}
		}
	}
	private void searchCont(Ctrb[] ele, Project pr) {
		for (int j = 0; j < ele.length; j++) {
			if(ele[j] == null) {
				for (int k = 0; k < cs.size(); k++) {
					Skill cSkill = cs.get(k).searchSkill(pr.skills.get(j).name);
					if (cSkill != null && !cs.get(k).active && cSkill.value + 1 == pr.skills.get(j).value && count > 1) {
						cs.get(k).active = true;
						LinkedList<Ctrb> mentors = searchMentor(cSkill);
						if (mentors == null) {
							cs.get(k).active = false;
							continue;
						}
						int nextIndex = nextSkill(ele, j);
						boolean flag = false;
						for (int i = 0; i < mentors.size(); i++) {
							Skill nextS = pr.skills.get(nextIndex);
							Skill mentorS = mentors.get(i).searchSkill(nextS.name);
							if(mentorS != null && mentorS.value > nextS.value) {
								ele[nextIndex] = mentors.get(i);
								ele[j] = cs.get(k);
								ele[nextIndex].active = true;
								count -= 2;
								flag = true;
								break;
							}
							else if(mentorS != null && mentorS.value + 1 == nextS.value) {
								count--;
							}
						}
						if (!flag) {
							cs.get(k).active = false;
						}
					}
				}
			}
		}
	}
	private LinkedList<Ctrb> searchMentor(Skill skill) {
		for (int i = 0; i < array.length; i++) {
			
		}
	}
	private int nextSkill(Ctrb[] ele, int currentIndex) {
		for (int i = currentIndex + 1; i < ele.length; i++) {
			if (ele[i] == null) {
				return i;
			}
		}
		return -1;
	}
}
