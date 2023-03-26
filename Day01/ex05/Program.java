package ex05;

public class Program {
    public static void main(String[] args){
        try {
            MENU_MODE menuMode = (args.length == 0) ? MENU_MODE.PRODUCTION : parseInput(args[0]);
            new Menu(menuMode).run();
        } catch (Exception e){
            System.out.println(e.getMessage());
            System.exit(-1);
        }
    }

    private static MENU_MODE parseInput(String arg){
        final int eqPos = arg.indexOf('=');
        if (eqPos == -1){
            throwError();
        }
        final String key = arg.substring(0, eqPos);
        final String value = arg.substring(eqPos + 1);
        if (!key.equals("--profile")){
            throwError();
        }
        if (value.equals("dev")){
            return MENU_MODE.DEV;
        }
        if (value.equals("production")){
            return MENU_MODE.PRODUCTION;
        }
        throwError();
        return null;
    }
    private static void throwError(){
        throw new RuntimeException(
            "Invalid input\n" +
            "Usage: Program {--profile=[dev,production]}"
        );
    }
}
