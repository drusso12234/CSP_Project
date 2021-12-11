package scheduler;

/**
 * A stub for your secondScheduler1.java scheduler code
 */
public class Scheduler2 implements Scheduler {


	Evaluator eval = new Evaluator();
	/**
	 * @see scheduler.Scheduler#authors()
	 */
	public String authors() {
		return "David Russo\nZoe Liang";
	}

	/**
	 * @see scheduler.Scheduler#schedule(scheduler.SchedulingProblem)
	 */
	public ScheduleChoice[] schedule(SchedulingProblem pProblem) {
		ScheduleChoice[] schedule = generateSchedule(pProblem);
		return minConflicts(pProblem, schedule, 100);
	}

	public ScheduleChoice[] minConflicts(SchedulingProblem pProblem, ScheduleChoice[] schedule, int steps) {



		int days = 5;
		int times = 4;
		Room[] rooms = pProblem.getRoomList();

		for (int step = 0; step < steps; step++) {
			int curEval = eval.violatedConstraints(pProblem, schedule);
			if (curEval == 0) {
				return schedule;
			}

			boolean needSwap = false;
			int minIndex = -1;
			int minDay = -1;
			int minTime = -1;
			Room minRoom = null;
			int minEval = curEval;
			int varIndex = findMostConflicts(pProblem, schedule);
			ScheduleChoice var = schedule[varIndex];
			Course varCourse = var.getCourse();

			//iterate through all values
			for (Room room : rooms) {
				for (int day = 0; day < days; day++) {
					for (int time = 0; time < times; time++) {
						ScheduleChoice[] testSchedule = schedule.clone();
						boolean swap = false;
						//Once we have a value, see if another exam is scheduled there
						for (int i = 0; i < testSchedule.length; i++){
							if (testSchedule[i].getRoom() == room &&
									testSchedule[i].getDay() == day
									&& testSchedule[i].getTimeSlot() == time) {
								int newEval = eval.violatedConstraints(pProblem, swapVariables(testSchedule, varIndex, i));
								if(newEval < minEval) {
									minEval = newEval;
									minDay = day;
									minTime = time;
									minRoom = room;
									minIndex = i;
									needSwap = true;
								}
								swap = true;
								break;
							}
						}
						if (!swap) {
							testSchedule[varIndex] = new ScheduleChoice(varCourse, room, day, time);
							int newEval = eval.violatedConstraints(pProblem, testSchedule);
							if(newEval < minEval) {
								minEval = newEval;
								minDay = day;
								minTime = time;
								minRoom = room;
								needSwap = false;
							}
						}
					}
				}
			}
			if (minEval != curEval) {
				if (needSwap) {
					schedule = swapVariables(schedule, varIndex, minIndex);
				}
				else
					schedule[varIndex] = new ScheduleChoice(varCourse, minRoom, minDay, minTime);
			}

		}
		return schedule;
	}


	public ScheduleChoice[] swapVariables(ScheduleChoice[] schedule, int lhs, int rhs) {
		ScheduleChoice var = schedule[lhs];
		Room varRoom = var.getRoom();
		int varTime = var.getTimeSlot();
		int varDay = var.getDay();
		Course varCourse = var.getCourse();

		ScheduleChoice swap = schedule[rhs];
		Room swapRoom = swap.getRoom();
		int swapTime = swap.getTimeSlot();
		int swapDay = swap.getDay();
		Course swapCourse = swap.getCourse();

		schedule[lhs] = new ScheduleChoice(varCourse, swapRoom, swapDay, swapTime);
		schedule[rhs] = new ScheduleChoice(swapCourse, varRoom, varDay, varTime);

		return schedule;

	}

	public int findMostConflicts(SchedulingProblem pProblem, ScheduleChoice[] schedule) {
		Student[] studentList = pProblem.getStudentList();
		int[] conflicts = new int[pProblem.getCourseList().length];
		int most = 0;
		int mostIndex = -1;
		//for each scheduling choice
		for (int i = 0; i < schedule.length; i++) {
			ScheduleChoice choice = schedule[i];
			Course[] potentialConflicts = new Course[pProblem.getRoomList().length - 1];
			Course course = choice.getCourse();
			int j = 0;
			//Create list of courses that conflict with current course
			for (ScheduleChoice compare : schedule) {

				if (compare.getDay() == choice.getDay() && compare.getTimeSlot() == choice.getTimeSlot() && compare != choice) {
					potentialConflicts[j] = compare.getCourse();
					j++;
				}
			}
			//Iterate through students
			for (Student student : studentList) {
				if (student.goesTo(course)) {
					//Check if student also goes to conflicting courses
					for (Course con : potentialConflicts) {
						if (con != null && student.goesTo(con)) {
							conflicts[i]++;
							if (conflicts[i] > most) {
								most = conflicts[i];
								mostIndex = i;
							}
						}
					}
				}
			}

		}
		return mostIndex;
	}

	public ScheduleChoice[] generateSchedule(SchedulingProblem pProblem) {
		int days = 5;
		int times = 4;
		Room[] rooms = pProblem.getRoomList();
		Course[] courses = pProblem.getCourseList();
		ScheduleChoice[] schedule = new ScheduleChoice[pProblem.getCourseList().length];

		int i = 0;
		for (Room room : rooms) {
			for (int day = 0; day < days; day++) {
				for (int time = 0; time < times; time++) {
					if (i < courses.length) {
						schedule[i] = new ScheduleChoice(courses[i], room, day, time);
						i++;
					}
				}
			}
		}
		return schedule;
	}
}