package il.ac.shenkar.david.todolistex2;

import android.util.Log;

import java.util.HashMap;
import java.util.List;

/**
 * Created by David on 14-Mar-16.
 */
public class TaskHashList
{
    private static HashMap<Integer, List<Task>> task_list_map;
    private static int int_seq;

    public static void Initialize ()
    {
        int_seq = 1;
        task_list_map = new HashMap<Integer, List<Task>>();
    }

    public static void addTaskList (List<Task> t)
    {
        task_list_map.put(int_seq, t);
        int_seq++;
    }

    public static void resetTaskList (int key,List<Task> t)
    {
        task_list_map.put(key, t);
    }

    public static List<Task> getTaskList (int pos)
    {
        return task_list_map.get(pos);
    }
}
