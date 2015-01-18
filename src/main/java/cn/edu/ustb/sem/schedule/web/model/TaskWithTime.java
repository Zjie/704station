package cn.edu.ustb.sem.schedule.web.model;

import java.util.List;

import cn.edu.ustb.sem.schedule.web.model.DataTable.Text;
import cn.edu.ustb.sem.schedule.web.model.Tasks.Task;
import cn.edu.ustb.sem.schedule.web.model.Processes.Process;

public class TaskWithTime {

	private String start;
	
	private String end;
	
	private List<Task> tasks;
	
	private List<Process> processes;
	
	private List<Text> startTexts;
	
	private List<Text> endTexts;

	public String getStart() {
		return start;
	}

	public void setStart(String start) {
		this.start = start;
	}

	public String getEnd() {
		return end;
	}

	public void setEnd(String end) {
		this.end = end;
	}

	public List<Task> getTasks() {
		return tasks;
	}

	public void setTasks(List<Task> tasks) {
		this.tasks = tasks;
	}

	public List<Process> getProcesses() {
		return processes;
	}

	public void setProcesses(List<Process> processes) {
		this.processes = processes;
	}

	public List<Text> getStartTexts() {
		return startTexts;
	}

	public void setStartTexts(List<Text> startTexts) {
		this.startTexts = startTexts;
	}

	public List<Text> getEndTexts() {
		return endTexts;
	}

	public void setEndTexts(List<Text> endTexts) {
		this.endTexts = endTexts;
	}
	
}
