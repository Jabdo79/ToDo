package abdo.jonathan;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * Created by Jonathan Abdo
 */
@Controller
public class TaskController {

    //Display all tasks on first visit to webapp
    @RequestMapping(path = "index", method = RequestMethod.GET)
    public String displayAllTasks(Model model, HttpServletRequest request) {
        //Create session variable for sorting tasks by type
        HttpSession session = request.getSession();
        session.setAttribute("statusSort", "All");

        List<Task> tasks = TaskDAO.getAllTasks();
        model.addAttribute("tasks", tasks);
        model.addAttribute("activeCount", TaskDAO.getActiveTaskCount());
        return "index";
    }

    //Display tasks matching the chosen status, store the choice in session to redisplay
    //choice after Adding a task or Removing completed tasks.
    @RequestMapping(path = "index", method = RequestMethod.POST)
    public String displayTasksByStatus(Model model, HttpServletRequest request) {
        List<Task> tasks;

        String displayStatus = request.getParameter("displayStatus");
        HttpSession session = request.getSession();

        //Set to session variable if they haven't chosen a sort
        if(displayStatus == null)
            displayStatus = (String) session.getAttribute("statusSort");

        //When user chooses a sort type, get tasks of that type and update session variable
        if(displayStatus.equals("Active")) {
            tasks = TaskDAO.getTasksByStatus(true);
            session.setAttribute("statusSort", "Active");
        }
        else if(displayStatus.equals("Completed")) {
            tasks = TaskDAO.getTasksByStatus(false);
            session.setAttribute("statusSort", "Completed");
        }
        else {
            tasks = TaskDAO.getAllTasks();
            session.setAttribute("statusSort", "All");
        }

        model.addAttribute("tasks", tasks);
        model.addAttribute("activeCount", TaskDAO.getActiveTaskCount());
        return "index";
    }

    //Add pojo to model to be sent to SpringForm
    @RequestMapping(path = "add_task", method = RequestMethod.GET)
    public String showTaskForm(Model model) {
        model.addAttribute("task", new Task());
        return "add_task";
    }

    //Add task to db if it doesn't exist (after trim and ignoring case(done in TaskDAO))
    @RequestMapping(path = "add_task", method = RequestMethod.POST)
    public String addTask(Model model, HttpServletRequest request, @ModelAttribute("task") Task newTask) {

        if(!TaskDAO.containsTask(newTask.getDescription().trim())) {
            TaskDAO.addTask(newTask);
            model.addAttribute("message", "Your task has been added!");
            //Message could be a number, giving front end more flexibility with displaying messages of their own
        }
        else
            model.addAttribute("message", "That task already exists.");

        return displayTasksByStatus(model, request);
    }

    //Toggle status of task and update db
    @RequestMapping("update_task")
    public String updateTaskStatus(Model model, HttpServletRequest request) {
        String newStatus;
        Task existing = TaskDAO.getTask(Integer.parseInt(request.getParameter("id")));

        if(request.getParameter("status").equals("Active")) {
            newStatus = "completed";
            existing.setActive(false);
        }else{
            newStatus = "active";
            existing.setActive(true);
        }
        TaskDAO.updateTask(existing);

        model.addAttribute("message", "The task: \""+existing.getDescription() + "\" has been marked as " + newStatus + ".");

        return displayTasksByStatus(model, request);
    }

    //Remove task from db by id
    @RequestMapping("remove_task")
    public String removeTask(Model model, HttpServletRequest request) {
        Task existing = TaskDAO.getTask(Integer.parseInt(request.getParameter("id")));

        //If statement catches null error if user resubmits post after already removing task
        if(existing!=null) {
            TaskDAO.removeTask(existing);
            model.addAttribute("message", "The task: \"" + existing.getDescription() + "\" has been removed.");
        }else
            model.addAttribute("message", "That task has already been removed.");

        return displayTasksByStatus(model, request);
    }

    //Remove all completed tasks from db
    @RequestMapping("remove_completed_tasks")
    public String removeCompletedTasks(Model model, HttpServletRequest request) {

        if(TaskDAO.removeCompletedTasks())
            model.addAttribute("message", "All completed tasks have been removed.");
        else
            model.addAttribute("message", "There are no completed tasks to remove.");

        return displayTasksByStatus(model, request);
    }
}