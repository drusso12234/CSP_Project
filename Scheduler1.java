package scheduler;

/**
 * A stub for your first scheduler code
 */
public class Scheduler1 implements Scheduler {

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
		ScheduleChoice[] schedule = new ScheduleChoice[pProblem.getCourseList().length];
		System.out.println(pProblem);
		return schedule;
	}

	//TODO
	//Add degree Heursitic, by counting class frequency
	//Write solver, should be similar to parts 1&2
	//Calculate MRV
	//Write method to check and enforce constraints
	
	public int[][] setupConstraints(SchedulingProblem pProblem) {
		int days = 5;
		int times = 4;
		int rooms = pProblem.getRoomList().length;
		int slots = rooms * days * times;
		int[][] vars = new int[pProblem.getCourseList().length][slots];

		for (int[] domain : vars){
			int i = 0;
			for (int room = 1; room <= rooms; room++) {
				for (int day = 1; day <= days; day++) {
					for (int time = 1; time <= times; time++) {
						domain[i] = room * 100 + day * 10 + time;
						i++;
					}
				}
			}
		}
		return vars;
	}

}