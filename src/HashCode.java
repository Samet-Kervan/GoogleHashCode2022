import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class HashCode {
	private DoubleLinkedList<Ctrb> ctrbs;
	private DoubleLinkedList<Ctrb> activeCtrbs;
	private DoubleLinkedList<Project> projects;
	private PriorityQueue<Project> sortedProjects;
	private DIBHashTable<String,Skill> skills;
	private ArrayList<String> projectPrint;
	private ArrayList<Integer> noPrint;
	private ArrayList<String> employePrint;
	private int activeCtrbCount = 0;
	private int dayCount = 0, nextCheck = 0;
	private int doneCount = 0;
	FileWriter writer;
	public HashCode(String fileName) {
		projectPrint = new ArrayList<String>();
		employePrint = new ArrayList<String>();
		noPrint = new ArrayList<Integer>();
		ctrbs = new DoubleLinkedList<Ctrb>();
		activeCtrbs = new DoubleLinkedList<Ctrb>();
		skills = new DIBHashTable<String,Skill>();
		projects = new DoubleLinkedList<Project>();
		readFile(fileName);
		addSkills();
		skills = new DIBHashTable<String,Skill>();
		sortedProjects = new PriorityQueue<Project>(true);
		sortProject();
		projects = new DoubleLinkedList<Project>();
		boolean done = true;
		while(sortedProjects.getSize() > 0) {
			if (done) {
				//System.out.println(projects.getSize() + " " + sortedProjects.getSize());
				boolean flag = searchCtrb();
				if (!flag && projects.getSize() == 0) {
					break;
				}
			}
			done = doProject();
			dayCount++;
			nextCheck--;
			if (nextCheck <= 0) {
				nextCheck = checkProjects();
				System.out.println("remaining: " + sortedProjects.getSize() + " day: " + dayCount 
						+ " next: " + nextCheck + " done: " + doneCount);
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
		return skills.getContent(skillName);
	}
	public void addSkills() {
		DoubleLinkedListNode<Ctrb> node = ctrbs.getFirst();
		while(node != null) {
			Ctrb ctrb = node.getContent();
			ArrayList<Skill> skls = skills.getAll();
			for (int i = 0; i < skls.size(); i++) {
				Skill skll = ctrb.searchSkill(skls.get(i).name);
				if (skll == null) {
					ctrb.addSkill(skls.get(i).name, 0);
				}
			}
			node = node.getNext(); 
		}
	}
	public void sortProject() {
		DoubleLinkedListNode<Project> node = projects.getFirst();
		while(node != null) {
			Project prj = node.getContent();
			double before = prj.bestBefore;
			double remain = prj.remaining;
			double size = prj.skills.getSize();
			double bottom = before * remain * size;
			if (bottom <= 0) {
				bottom = Double.MAX_VALUE;
			}
			prj.sortPoint = prj.point / bottom;
			sortedProjects.add(prj, prj.sortPoint);
			node = node.getNext();
		}
	}
	public boolean doProject() {
		boolean flag = false;
		//System.out.println("Current projects: " + projects.getSize());
		DoubleLinkedListNode<Project> node = projects.getFirst();
		while(node != null) {
			boolean doneFlag = false;
			Project project = node.getContent();
			project.remaining--;
			if (project.remaining == 0) {
				DoubleLinkedListNode<Skill> skillNode = project.skills.getFirst();
				flag = true;
				doneFlag = true;
				doneCount++;
				Ctrb[] emps = project.emp;
				for (int i = 0; i < emps.length; i++) {
					Skill skill = skillNode.getContent();
					Skill empSkill = emps[i].searchSkill(skill.name);
					emps[i].active = false;
					activeCtrbCount--;
					if (empSkill.value <= skill.value) {
						empSkill.value++;
					}
					skillNode = skillNode.getNext();
				}
			}
			if (doneFlag) {
				DoubleLinkedListNode<Project> nodeNext = node.getNext();
				projects.remove(node);
				node = nodeNext;
			}
			else {
				node = node.getNext();
			}
		}
		return flag;
	}
	public int checkProjects() {
		PriorityQueueNode<Project> prjNode = sortedProjects.getFirst();
		int nextCheck = Integer.MAX_VALUE;
		while(prjNode != null) {
			Project project = prjNode.getContent();
			int timeToFinish = project.bestBefore - dayCount;
			if (timeToFinish <= 0) {
				PriorityQueueNode<Project>  nextNode = prjNode.getNext();
				sortedProjects.remove(prjNode);
				prjNode = nextNode;
			}
			else if(timeToFinish < nextCheck) {
				nextCheck = timeToFinish;
			}
			else {
				prjNode = prjNode.getNext();
			}
		}
		return nextCheck;
	}
	private boolean searchCtrb() {
		boolean flag = false;
		PriorityQueueNode<Project> prjNode = sortedProjects.getFirst();
		while(prjNode != null) {
			boolean projectDone = true;
			Project prj = prjNode.getContent();
			Ctrb[] emp = new Ctrb[prj.skills.getSize()];
			DoubleLinkedListNode<Skill> searchSkillNode = prj.skills.getFirst();
			int index = 0;
			if (ctrbs.getSize() - activeCtrbCount >=  emp.length) {
				while(searchSkillNode != null) {
					if (emp[index] == null) {
						Skill searchingSkill = searchSkillNode.getContent();
						Ctrb ctrb = findPerfectCtrb(searchingSkill);
						if (ctrb != null) {
							ctrb.active = true;
							emp[index] = ctrb;
						}
						else {
							ctrb = findMentorCtrb(prj,emp,searchingSkill,index);
							if (ctrb != null) {
								ctrb.active = true;
								emp[index] = ctrb;
							}
							else {
								ctrb = findOverCtrb(searchingSkill);
								if (ctrb != null) {
									ctrb.active = true;
									emp[index] = ctrb;
								}
								else {
									for (int i = 0; i < index; i++) {
										emp[i].active = false;
									}
									projectDone = false;
									break;
								}
							}
						}
					}
					index++;
					searchSkillNode = searchSkillNode.getNext();
				}
				if (projectDone) {
					//System.out.println(prj.name);
					projectPrint.add(prj.name);
					noPrint.add(emp.length);
					for (int i = 0; i < emp.length; i++) {
						employePrint.add(emp[i].name);
						activeCtrbCount++;
					}
					prj.emp = emp;
					projects.add(prj);
					PriorityQueueNode<Project> next = prjNode.getNext();
					sortedProjects.remove(prjNode);
					prjNode = next;
				}
				else {
					prjNode = prjNode.getNext();
				}
			}
			else {
				prjNode = prjNode.getNext();
			}
		}
		return flag;
	}
	private Ctrb findPerfectCtrb(Skill skill) {
		DoubleLinkedListNode<Ctrb> ctrbNode = ctrbs.getFirst();
		while(ctrbNode != null) {
			Ctrb ctrb = ctrbNode.getContent();
			if (!ctrb.active) {
				Skill ctrbSkill = ctrb.searchSkill(skill.name);
				if (ctrbSkill.value == skill.value) {
					return ctrb;
				}
			}
			ctrbNode = ctrbNode.getNext();
		}
		return null;
	}
	private Ctrb findMentorCtrb(Project prj, Ctrb[] emp, Skill searchingSkill, int index) {
		if (index < emp.length) {
			
		}
		return null;
	}
	private Ctrb findOverCtrb(Skill skill) {
		DoubleLinkedListNode<Ctrb> ctrbNode = ctrbs.getFirst();
		while(ctrbNode != null) {
			Ctrb ctrb = ctrbNode.getContent();
			if (!ctrb.active) {
				Skill ctrbSkill = ctrb.searchSkill(skill.name);
				if (ctrbSkill.value > skill.value) {
					return ctrb;
				}
			}
			ctrbNode = ctrbNode.getNext();
		}
		return null;
	}
}
