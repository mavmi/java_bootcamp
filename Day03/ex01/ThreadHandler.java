package ex01;

public class ThreadHandler {
    private int idToPrint = 0;

    synchronized public void handlePrinting(String msg, int id){
        try {
            while (idToPrint != id){
                wait();
            }
            idToPrint = (id == 0) ? 1 : 0;
            System.out.println(msg);
            notify();
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }
}
