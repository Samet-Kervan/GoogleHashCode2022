import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Scanner;

public class HashCode {
	private LinkedList<Ctrb> cs;
	private LinkedList<Project> projects;
	private PriorityQueue<Project> sortedProjects;
	public HashCode(String fileName) {
		cs = new LinkedList<Ctrb>();
		projects = new LinkedList<Project>();
		readFile(fileName);
		sortedProjects = new PriorityQueue<Project>();
		sortProject();
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
		int x  = 5;
	}
}
