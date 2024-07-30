package entity;

public class ExceptionWrapper extends Exception{

    private ExceptionWrapper() {}

    public static void getExceptionMessage(Exception e) {
        if (e instanceof RecordNotFoundException) {
            System.out.println("1001: no car found in the parking");
        } else if (e instanceof ExitTimeIsSmallerThanEnterTime) {
            System.out.println("Exit Time Is Smaller Than Enter Time");
        }
    }
}
