package com.weeklycompass;

public class Task {
	public int TaskId;
	public String TaskTitle;
	public String TaskContent;
	public TASKSTATE TaskStatus;
	
	public enum TASKSTATE
	{
		NOT_STARTED,
		IN_PROGRESS,
		FINISHED;

		@Override
		public String toString() {
			switch(this)
			{
			case NOT_STARTED:
				return "NOT START";
			case IN_PROGRESS:
				return "IN PROGRESS";
			case FINISHED:
				return "COMPLETED";
				default:
					return "";
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return TaskTitle;
	}

	@Override
	public boolean equals(Object o) {
		Task t = (Task)o;
		return this.TaskId == t.TaskId;
	}

	public int TaskStateToInt(TASKSTATE state)
	{
		if(state == TASKSTATE.NOT_STARTED)
		{
			return 0;
		}
		else if(state == TASKSTATE.IN_PROGRESS)
		{
			return 1;
		}
		else if(state == TASKSTATE.FINISHED)
		{
			return 2;
		}
		else
		{
			return 0;
		}
	}
	
	public TASKSTATE IntToTaskState(int i)
	{
		switch(i)
		{
		case 0:
			return TASKSTATE.NOT_STARTED;
		case 1:
			return TASKSTATE.IN_PROGRESS;
		case 2:
			return TASKSTATE.FINISHED;
			default:
				return TASKSTATE.NOT_STARTED;
		}
	}

}
