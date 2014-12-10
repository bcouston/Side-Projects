import javax.swing.*;
import java.awt.BorderLayout;
import java.util.*;

public class Schedule {
	
	//Data structures
	
	//week - represents the whole week as a list off map objects
	List<Map<String,String>> week = new ArrayList<Map<String,String>>();
	//lists of names, a seperate copy of the list of names for handling break days and a list of once a week jobs
	List<String> people = Arrays.asList("Ben", "Dan", "George", "Jack", "Matt", "Prash");
	List<String> breakList = new ArrayList<String>(people);
	List<String> onceAWeek = Arrays.asList("B1", "B2", "H");
	//days - list for determining which days to assign the once a week jobs
	List<Integer> days = Arrays.asList(0,1,2,3,4,5,6);
	Random generator = new Random();
	
	public Schedule() {
		breakList.add("-");
		Collections.shuffle(breakList);
		Collections.shuffle(onceAWeek);
		List<Integer> onceDays = pickNRandom(days, 2);	
		for (int n = 0; n < 7; n++) {
			addDay(week, assignChores(n, onceDays, n));
			System.out.println();
		}
		createTable();
	}
	
	private Boolean checkJobTwice (String person, String job, int currentDay) {
		int n = 0;
		for (int x = 0; x < week.size(); x++) {
			//change to contains
			if (week.get(x).get(person).contains(job)) {
				n++;
				//System.out.println(person + " no: " + n);
			}
		}
		if ((n >= 2) || checkJobInARow(person, job, currentDay)) {return true;} else {return false;}
	}
	
	private Boolean checkJobInARow (String person, String job, int currentDay) {
		if (currentDay != 0) {
			if (week.get(currentDay - 1).get(person).equals(job)) {
				System.out.println("Same job in a row!");
				return true;
			} else {return false;}
		} else {return false;}
	}
	
	private Map<String,String> assignChores(int b, List<Integer> d, int currentDay) {
		//create chores list and shuffle chores including break day
		Boolean jobTwice = null;
		List<String> chores = new ArrayList<String>();
		chores.add("KS");
		chores.add("KF");
		chores.add("W");
		chores.add("D");
		chores.add("GT");
		String dayOffFor = breakList.get(b);
		int reinsert = 0;
		List<String> peopleWithJobs = new ArrayList<String>(people);
		if (dayOffFor.equals("-")) {
			//System.out.println("job day 0");
			chores.add(generator.nextInt(peopleWithJobs.size()), onceAWeek.get(0));
		} else {
			reinsert = peopleWithJobs.indexOf(dayOffFor);
			peopleWithJobs.remove(dayOffFor);
		}
		do {
			jobTwice = false;
			Collections.shuffle(chores);
			for (int n = 0; n < peopleWithJobs.size(); n++) {
				if (chores.get(n).equals("-") == false) {
					if (checkJobTwice(peopleWithJobs.get(n), chores.get(n), currentDay)) {
						jobTwice = true;
					}
				}
			}
		} while (jobTwice == true);
		Map<String, String> dayChores = new HashMap<String, String>();
		
		//assign break
		if (dayOffFor.equals("-") == false) {
			peopleWithJobs.add(reinsert, dayOffFor);;
			chores.add(peopleWithJobs.indexOf(dayOffFor), new String ("-"));
		}
		//randomly assign once a week job to someone
		int pForJ = generator.nextInt(peopleWithJobs.size());
		//make sure someone who has washing, a break or another once a day job that day doesn't get the additional job
		while ((chores.get(pForJ).equals("W")) || (chores.get(pForJ).equals("-")) ||
				(chores.get(pForJ).equals("B1")) || (chores.get(pForJ).equals("B2")) || (chores.get(pForJ).equals("H"))) {
			pForJ = generator.nextInt(peopleWithJobs.size());
		}
		//assign chores
		for (int n = 0; n < peopleWithJobs.size(); n++) {
			String person = peopleWithJobs.get(n);
			String job = chores.get(n);
			if ((b == d.get(0)) && (n == pForJ)) {
				job = job + ", " + onceAWeek.get(1);
			}  else if ((b == d.get(1)) && (n == pForJ)) {
				job = job + ", " + onceAWeek.get(2);
			}
			dayChores.put(person, job);
			System.out.println("Person = " + person + ", Job = " + job);
		}
		return dayChores;
	}
	
	private List<Integer> pickNRandom(List<Integer> days2, int n) {
	    List<Integer> copy = new ArrayList<Integer>(days2);
	    Collections.shuffle(copy);
	    return copy.subList(0, n);
	}
	
	private void addDay (List<Map<String,String>> week, Map<String,String> day) {
		week.add(day);
	}
	
	//Visualising the data as a table
	private void createTable() {
	    JFrame frame = new JFrame();
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		Object rowData[][] = new Object[6][7];
		for (int y = 0; y<=6; y++) {
			for (int x = 0; x<=5; x++) {
				rowData[x][y] =  (people.get(x)+ ": " + week.get(y).get(people.get(x)));
			}
		}
	    Object columnNames[] = { "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday" };
	    JTable table = new JTable(rowData, columnNames);

	    JScrollPane scrollPane = new JScrollPane(table);
	    frame.add(scrollPane, BorderLayout.CENTER);
	    frame.setSize(300, 150);
	    frame.setVisible(true);

	}
	
	public static void main(String[] args) {
		Schedule s = new Schedule();
	}

}
