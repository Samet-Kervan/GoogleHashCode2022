import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;

public class HashCode {
	private LinkedList<Ctrb> cs;
	private LinkedList<Project> projects;
	private PriorityQueue<Project> sortedProjects;
	private int count;
	private LinkedList<Skill> skills;
	private ArrayList<String> projectNames;
	private ArrayList<Integer> empNo;
	private ArrayList<String> employess;
	public HashCode(String fileName) {
		projectNames = new ArrayList<String>();
		employess = new ArrayList<String>();
		empNo = new ArrayList<Integer>();
		cs = new LinkedList<Ctrb>();
		skills = new LinkedList<Skill>();
		projects = new LinkedList<Project>();
		readFile(fileName);
		addSkills();
		sortedProjects = new PriorityQueue<Project>();
		sortProject();
		int x = 0;
		//sortedProjects.size() > 0
		while(x < 20) {
			sortProject();
			selectCtrb();
			x++;
		}
		writeFile();
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
					Skill searchS = searchSkill(skill[0]);
					if (searchS == null) {
						searchS = new Skill(skill[0], 0);
						skills.add(searchS);
					}
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
	private void writeFile() {
		try {
			File file = new File("output.txt");
			file.createNewFile();
			FileWriter writer = new FileWriter(file);
			int co = 0;
			writer.write(projectNames.size() + "\n");
			for (int i = 0; i < projectNames.size(); i++) {
				String st = projectNames.get(i);
				writer.write(st + "\n");
				for (int j = 0; j < empNo.get(i); j++) {
					writer.write(employess.get(co) + " ");
					co++;
				}
				writer.write("\n");
			}
			writer.flush();
			writer.close();
		} catch (IOException e) {
			System.out.println("There was a problem in writing");
			System.exit(0);
		}
	}
	public Skill searchSkill(String skillName) {
		for (int i = 0; i < skills.size(); i++) {
			if (skillName.equals(skills.get(i).name)) {
				return skills.get(i);
			}
		}
		return null;
	}
	public void addSkills() {
		for (int i = 0; i < cs.size(); i++) {
			for (int j = 0; j < skills.size(); j++) {
				Skill s = cs.get(i).searchSkill(skills.get(j).name);
				if (s == null) {
					s = new Skill(skills.get(j).name,0);
				}
			}
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
			if (!pr.active) {
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
					for (int j = 0; j < ele.length; j++) {
						if (ele[j] == null) {
							Skill pSkill = pr.skills.get(j);
							for (int k = 0; k < cs.size(); k++) {
								Skill cSkill = cs.get(k).searchSkill(pSkill.name);
								if (cSkill != null && !cs.get(k).active && cSkill.value > pSkill.value) {
									ele[j] = cs.get(k);
									count--;
									ele[j].active = true;
								}
							}
						}
					}
				}
				if (count != 0) {
					for (int j = 0; j < ele.length; j++) {
						try {
							ele[j].active = false;
						} catch (Exception e) {
							// TODO: handle exception
						}
					}
					continue;
				}
				else {
					boolean check = true;
					for (int j = 0; j < ele.length; j++) {
						if (ele[j] == null) {
							check = false;
							break;
						}
					}
					if (check) {
						pr.addEmp(ele);
						projectNames.add(pr.name);
						empNo.add(ele.length);
						for (int j = 0; j < ele.length; j++) {
							employess.add(ele[j].name);
						}
						pr.active = true;
					}
				}
			}
			else {
				pr.remaining--;
				if (pr.remaining <= 0) {
					for (int j = 0; j < pr.emp.length; j++) {
						pr.emp[j].active = false;
						if (pr.emp[j].searchSkill(pr.skills.get(j).name).value <= pr.skills.get(j).value) {
							pr.emp[j].addPointToSkill(pr.skills.get(j).name);
						}
					}
					sortedProjects.remove(i);
				}
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
						boolean f = false;
						for (int i = 0; i < ele.length; i++) {
							try {
								Skill a = ele[i].searchSkill(pr.skills.get(j).name);
								if ( a != null) {
									if (ele[i] != null && a.value >= pr.skills.get(j).value) {
										f = true;
									}
								}
							} catch (Exception e) {
								// TODO: handle exception
							}
						}
						if (!f) {
							LinkedList<Ctrb> mentors = searchMentor(cSkill);
							if (mentors == null) {
								cs.get(k).active = false;
								continue;
							}
							int nextIndex = nextSkill(ele, j);
							boolean flag = false;
							if (nextIndex != -1) {
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
									}
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
	}
	private LinkedList<Ctrb> searchMentor(Skill skill) {
		LinkedList<Ctrb> list = new LinkedList<Ctrb>();
		for (int i = 0; i < cs.size(); i++) {
			Skill cSkill = cs.get(i).searchSkill(skill.name);
			if (cSkill != null && !cs.get(i).active) {
				list.add(cs.get(i));
			}
		}
		return list;
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
