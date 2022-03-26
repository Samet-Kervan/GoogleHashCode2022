import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Scanner;

public class HashCode {
	//After all the updates it still works slow.
	private LinkedList<Ctrb> ctrbs;
	private LinkedList<Project> projects;
	private PriorityQueue<Project> sortedProjects;
	private Hashtable<String,Skill> skills;
	private ArrayList<String> projectPrint;
	private ArrayList<Integer> noPrint;
	private ArrayList<String> employePrint;
	private int dayCount = 0, nextCheck = 0;
	public HashCode(String fileName) {
		skills = new Hashtable<String,Skill>();
		projectPrint = new ArrayList<String>();
		employePrint = new ArrayList<String>();
		noPrint = new ArrayList<Integer>();
		ctrbs = new LinkedList<Ctrb>();
		projects = new LinkedList<Project>();
		readFile(fileName);
		addSkills();
		skills = new Hashtable<String,Skill>();
		sortedProjects = new PriorityQueue<Project>();
		sortProjects();
		projects = new LinkedList<Project>();
		boolean done = true;
		while(sortedProjects.size() > 0) {
			if (done) {
				//System.out.println(projects.getSize() + " " + sortedProjects.getSize());
				boolean flag = searchCtrb();
				if (!flag && projects.size() == 0) {
					break;
				}
			}
			done = doProject();
			dayCount++;
			nextCheck--;
			if (nextCheck <= 0) {
				nextCheck = checkProjects();
			}
		}
		String outFile = fileName.charAt(0)+ "o.txt";
		writeFile(outFile);
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
						skills.put(skill[0], searchS);
					}
				}
				ctrbs.add(ctrb);
			}
			for (int i = 0; i < Integer.parseInt(numbers[1]); i++) {
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
	private void writeFile(String outFile) {
		try {
			File file = new File(outFile);
			file.createNewFile();
			FileWriter writer = new FileWriter(file);
			int co = 0;
			writer.write(projectPrint.size() + "\n");
			for (int i = 0; i < projectPrint.size(); i++) {
				String st = projectPrint.get(i);
				writer.write(st + "\n");
				for (int j = 0; j < noPrint.get(i); j++) {
					writer.write(employePrint.get(co) + " ");
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
		return skills.get(skillName);
	}
	public void addSkills() {
		Enumeration<String> keys = skills.keys();
		while(keys.hasMoreElements()) {
			String key = keys.nextElement();
			for (Ctrb ctrb : ctrbs) {
				Skill skll = ctrb.searchSkill(key);
				if (skll == null) {
					ctrb.addSkill(key, 0);
				}
			}
		}
	}
	public void sortProjects() {
		for(Project project: projects) {
			double before = project.bestBefore;
			double remain = project.remaining;
			double size = project.skills.size();
			double bottom = before * remain * size;
			if (bottom <= 0) {
				bottom = Double.MAX_VALUE;
			}
			project.sortPoint = project.point / bottom;
			sortedProjects.add(project);
		}
	}
	public boolean doProject() {
		boolean flag = false;
		for(Project project: projects) {
			project.remaining--;
			if(project.remaining == 0) {
				flag = true;
				Ctrb[] emps = project.emp;
				for (int i = 0; i < emps.length; i++) {
					Skill empSkill = emps[i].searchSkill(emps[i].currentSkill);
					ctrbs.add(emps[i]);
					if (empSkill.value <= emps[i].currentSkillsPoint) {
						empSkill.value++;
					}
				}
			}
		}
		return flag;
	}
	public int checkProjects() {
		int nextCheck = Integer.MAX_VALUE;
		Object[] projectsToCheck = sortedProjects.toArray();
		for (int i = 0; i < projectsToCheck.length; i++) {
			Project project = (Project) projectsToCheck[i];
			int timeToFinish = project.bestBefore - dayCount + project.point;
			if (timeToFinish <= 0) {
				sortedProjects.remove(project);
			}
			else if(timeToFinish < nextCheck) {
				nextCheck = timeToFinish;
			}
		}
		return nextCheck;
	}
	private boolean searchCtrb() {
		boolean flag = false;
		Object[] projectsToSearch = sortedProjects.toArray();
		for (int i = 0; i < projectsToSearch.length; i++) {
			boolean projectDone = true;
			Project prj = (Project) projectsToSearch[i];
			if (prj.skills.size() > ctrbs.size()) {
				continue;
			}
			Ctrb[] emp = new Ctrb[prj.skills.size()];
			int index = 0;
			for(Skill skill: prj.skills) {
				Ctrb ctrb = greedyFind(skill);
				if (ctrb != null) {
					emp[index] = ctrb;
				}
				else {
					for (int j = 0; j < index; j++) {
						ctrbs.add(emp[j]);
					}
					projectDone = false;
					break;
				}
				index++;
			}
			if (projectDone) {
				flag = true;
				projectPrint.add(prj.name);
				noPrint.add(emp.length);
				for (int j = 0; j < emp.length; j++) {
					employePrint.add(emp[j].name);
				}
				prj.emp = emp;
				projects.add(prj);
				sortedProjects.remove(prj);
			}
		}
		return flag;
	}
	private Ctrb greedyFind(Skill skill) {
		for(Ctrb ctrb: ctrbs){
			Skill ctrbSkill = ctrb.searchSkill(skill.name);
			if (ctrbSkill.value >= skill.value) {
				ctrbs.remove(ctrb);
				ctrb.currentSkill = skill.name;
				ctrb.currentSkillsPoint = skill.value;
				return ctrb;
			}
		}
		return null;
	}
}
