package il.ac.shenkar.david.todolistex2;

/**
 * Created by David on 08-Mar-16.
 */
public enum Sorting {
    TIME,PRIORITY,WAITING,INPROGESS,DONE;

    public static Sorting fromInteger(int x) {
        switch(x) {
            case 0:
                return TIME;
            case 1:
                return PRIORITY;
            case 2:
                return WAITING;
            case 3:
                return INPROGESS;
            case 4:
                return DONE;
            default:
                return TIME;
        }
    }
}