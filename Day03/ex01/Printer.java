package ex01;

public class Printer extends Thread {
    private String msg;
    private int msgCount;
    private ThreadHandler handler;
    private int id;
    private static int lastId = 0;
    
    public Printer(String msg, int msgCount, ThreadHandler handler){
        this.msg = msg;
        this.msgCount = msgCount;
        this.handler = handler;
        id = lastId++;
    }
    
    public void run(){
        for (int i = 0; i < msgCount; i++){
            try {
                handler.handlePrinting(msg, id);
                sleep(21);
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
