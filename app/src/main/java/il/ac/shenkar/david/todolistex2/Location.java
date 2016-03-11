package il.ac.shenkar.david.todolistex2;

/**
 * Created by David on 09-Mar-16.
 */
public enum Location {
    Meeting_Room,Office_245,Lobby,NOC,VPsoffice;

    public static Location fromInteger(int x) {
        switch(x) {
            case 0:
                return Meeting_Room;
            case 1:
                return Office_245;
            case 2:
                return Lobby;
            case 3:
                return NOC;
            case 4:
                return VPsoffice;
            default:
                return Meeting_Room;
        }
    }
}
