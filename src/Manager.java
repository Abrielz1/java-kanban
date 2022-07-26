import java.util.*;
public class Manager {

   HashMap<Integer, ArrayList<Epic>> epicHash = new HashMap<>();
   HashMap<Integer, ArrayList<SubTask>>subEpicHash = new HashMap<>();

    ArrayList<Epic> pid= new ArrayList<>();
    ArrayList<SubTask> dip= new ArrayList<>();
    ArrayList<Task> taskArray = new ArrayList<>();


int id = 0;
int subId = 0;
int taskId = 0;
int addEpic(Epic epic){

pid.add(epic);
epicHash.put(id, pid);
id++;
return id;
}

void addSubEpic(SubTask subTask){

dip.add(subTask);
subEpicHash.put(subId, dip);
subId++;
}

void addTask(Task task){

taskArray.add(task);
taskId++;
}

void printEpic(){

   for (Integer dip : epicHash.keySet()) {
       for (Epic val : epicHash.get(dip)) {
           System.out.print(val.idEpicTask + " ");
           System.out.print(val.nameEpicTask + " ");
           System.out.print(val.explanationEpicTask + " ");
           System.out.print(val.statusEpicTask + " " + "\n");
       }
   }
        for (Integer dip: subEpicHash.keySet()) {
            for (SubTask val: subEpicHash.get(dip)) {
                System.out.print(val.idSubTask + " ");
                System.out.print(val.nameSubTask + " ");
                System.out.print(val.explanationSubTask + " ");
                System.out.print(val.statusSubTask + " " + "\n");
            }
        }
    }
}
