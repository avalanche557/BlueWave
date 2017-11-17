package com.distributedpipeline.persistence.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.distributedpipeline.persistence.domain.TaskLibrary;
import com.distributedpipeline.persistence.domain.Tasks;
import com.distributedpipeline.persistence.domain.Workflow;
import com.distributedpipeline.persistence.exceptions.TaskLibraryNotFoundException;
import com.distributedpipeline.persistence.exceptions.WorkflowNotFoundException;
import com.distributedpipeline.persistence.repo.PersistenceTaskRepo;
import com.distributedpipeline.persistence.repo.PersistenceWorkflowRepo;
import com.distributedpipeline.persistence.utility.LogExecutionTime;

@Service
public class PersistenceServiceImpl implements PersistenceService {
	
	/*------------------------ Repositories Autowired ----------------------------------*/
	@Autowired
	private PersistenceWorkflowRepo persistenceWorkflowRepo;
	
	@Autowired
	private PersistenceTaskRepo persistenceTaskRepo;
	
	
	/*---------------------- getters and setters for repos-----------------------------*/
     
	public PersistenceWorkflowRepo getPersistenceWorkflowRepo() {
		return persistenceWorkflowRepo;
	}

	public void setPersistenceWorkflowRepo(PersistenceWorkflowRepo persistenceWorkflowRepo) {
		this.persistenceWorkflowRepo = persistenceWorkflowRepo;
	}

	public PersistenceTaskRepo getPersistenceTaskRepo() {
		return persistenceTaskRepo;
	}

	public void setPersistenceTaskRepo(PersistenceTaskRepo persistenceTaskRepo) {
		this.persistenceTaskRepo = persistenceTaskRepo;
	}
	
	/*------------------------                    ----------------------------------------
                               Method For Workflow    
     --------------------------                    --------------------------------------*/
	
	
	/*----------------------- Method to get workflow by name -----------------------------*/
	@Override
	@LogExecutionTime
	public Workflow getWorkflowByName(String workFlowName) throws WorkflowNotFoundException {
	       return persistenceWorkflowRepo.getWorkflowByworkFlowName(workFlowName);
       }
		
	
	
	/*------------------------- Method to get all workflows -----------------------------*/
	@Override
	@LogExecutionTime
	public Iterable<Workflow> getWorkflow() throws WorkflowNotFoundException {
		return persistenceWorkflowRepo.findAll();
	}

	/*--------------------- Method to save workflow to repository -----------------------*/	
	@Override
	@LogExecutionTime
	public String addWorkflow(Workflow workflow) {
		String workFlowName = workflow.getWorkFlowName();
		if(persistenceWorkflowRepo.getWorkflowByworkFlowName(workFlowName)==null) {
			persistenceWorkflowRepo.save(workflow);
			return "Workflow saved";
		}
		else {
			return "Workflow with the same Workflow name already exists";
		}
	}
	
	/*-------------------------- Method to update a workflow ----------------------------*/
	@Override
	@LogExecutionTime
	public Workflow updateWorkflow(Workflow workFlow) {
		String workflowName = workFlow.getWorkFlowName();
		persistenceWorkflowRepo.deleteByworkFlowName(workflowName);
		return persistenceWorkflowRepo.save(workFlow);
	}

	/*--------------------------- Method to delete workflow ----------------------------*/
	@Override
	@LogExecutionTime
	public boolean deleteWorkflow(String workflowName) {
		if(persistenceWorkflowRepo.getWorkflowByworkFlowName(workflowName) != null) {
			persistenceWorkflowRepo.deleteByworkFlowName(workflowName);
			return true;
		}
		else {
			return false;
		}
	}
	
    /*------------------------                    ----------------------------------------
	                            Method For TaskLibrary    
	--------------------------                    --------------------------------------*/
	
	
	/*--------------------- Method to get tasklibrary by taskname------------------------*/
    @Override
    @LogExecutionTime
	public TaskLibrary gettaskLibraryByName(String taskName) throws TaskLibraryNotFoundException{
		return persistenceTaskRepo.getTaskLibraryByTaskName(taskName);
	} 
	
	/*----------------------- Method to get all tasklibraries ---------------------------*/
	@Override
	@LogExecutionTime
	public Iterable<TaskLibrary> getTaskLibrary() throws TaskLibraryNotFoundException {
		return persistenceTaskRepo.findAll();
	}
	
	/*--------------------------- Method to save a tasklibrary -------------------------*/
	@Override
	@LogExecutionTime
	public TaskLibrary addTaskLibrary(TaskLibrary taskLibrary) {
		return persistenceTaskRepo.save(taskLibrary);
	}
	
	/*-------------------------- Method to update a tasklibrary ------------------------*/
	@Override
	@LogExecutionTime
	public TaskLibrary updateTaskLibrary(TaskLibrary taskLibrary) {
        String taskName = taskLibrary.getTaskName();
		persistenceTaskRepo.deleteByTaskName(taskName);
		return persistenceTaskRepo.save(taskLibrary);
		
	}
	
	/*-------------------------- Method to delete tasklibrary by id -------------------*/
	@Override
	@LogExecutionTime
	public boolean deleteTaskLibrary(String taskName) {
		if(persistenceTaskRepo.getTaskLibraryByTaskName(taskName) != null)  {
			persistenceTaskRepo.deleteByTaskName(taskName);
			return true;
		}
		else {
			return false;
		}
	}   
	
	/*------------------------------- Check Permissions for different users ------------------- */
	@Override
	@LogExecutionTime
	public String userPermissions(String workFlowName, String userName) {
		Workflow workflow = persistenceWorkflowRepo.getWorkflowByworkFlowName(workFlowName);
		if(Arrays.toString(workflow.getCanEditUser()).contains(userName)) {
			return "user can edit workflow";
		}
		else if(Arrays.toString(workflow.getCanExecuteUser()).contains(userName)) {
			return "user can execute workflow";
		}
		else if(Arrays.toString(workflow.getCanViewUser()).contains(userName)) {
			return "user can view workflow";
		}
		else {
			return "user not authorised to access workflow";
		}
	}
	
	/*-------------------------- Method to get tasks inside workflow -----------------------------*/
	
	@Override
	@LogExecutionTime
	public List<String> getTasksOfWorkflow(String workFlowName) {
		
		Workflow workflow = persistenceWorkflowRepo.getWorkflowByworkFlowName(workFlowName);
		Map<String,Tasks> tasks = new HashMap<String,Tasks>();
		tasks = workflow.getTasks();
		List<String> task_list = new ArrayList<String>();
		Set<String> keys = tasks.keySet();	
		for (String key : keys) {
			task_list.add(key);
		}
		return task_list;
	}
	
	/*-------------------------- Method to get details of a task in a workflow -----------------------*/
	
	@Override
	@LogExecutionTime
	public Tasks getDetailsOfTask(String workFlowName, String task_name) {
		
		Workflow workflow = persistenceWorkflowRepo.getWorkflowByworkFlowName(workFlowName);
		Map<String,Tasks> tasks = new HashMap<String,Tasks>();
		Map<String,Tasks> mapOfTasks = new HashMap<String,Tasks>();
		tasks = workflow.getTasks();
		Tasks taskDetails = null;
		for (Map.Entry<String, Tasks> entry : tasks.entrySet()) {	
			if(entry.getKey().equals(task_name)) {
				taskDetails = entry.getValue();
			}
		}
		return taskDetails;
	}
}
