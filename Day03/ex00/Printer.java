package ex00;

public class Printer extends Thread {
    private String msg;
    private int msgCount;
    
    public Printer(String msg, int msgCount){
        this.msg = msg;
        this.msgCount = msgCount;
    }
    
    public void run(){
        for (int i = 0; i < msgCount; i++){
            try {
                System.out.println(msg);
                sleep(21);
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
