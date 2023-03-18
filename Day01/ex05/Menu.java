package ex05;

import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.UUID;

enum MENU_MODE{
    DEV,
    PRODUCTION
}

public class Menu {
    private MENU_MODE menuMode;
    private Scanner scanner;
    private TransactionsService transactionsService;

    private static final int BAD_CMD = 0;

    public Menu(MENU_MODE menuMode){
        this.menuMode = menuMode;
        scanner = new Scanner(System.in);
        transactionsService = new TransactionsService();
    }
    public Menu(MENU_MODE menuMode, Scanner scanner){
        this.menuMode = menuMode;
        this.scanner = scanner;
        transactionsService = new TransactionsService();
    }

    public void run(){
        while (true){
            int cmd = selectCommand();
            if (cmd == BAD_CMD){
                printSeparator();
                continue;
            }
            if (cmd == 1){
                addAUser();
            } else if (cmd == 2){
                viewUserBalances();
            } else if (cmd == 3){
               performATransfer();
            } else if (cmd == 4){
                viewAllTransactions();
            }

            if (menuMode == MENU_MODE.DEV){
                if (cmd == 5){
                    removeTransferById();
                } else if (cmd == 6) {
                    checkTransferValidity();
                } else if (cmd == 7) {
                    System.exit(0);
                }
            } else {
                if (cmd == 5){
                    System.exit(0);
                }
            }

            printSeparator();
        }
    }

    private int selectCommand(){
        System.out.println("1. Add a user");
        System.out.println("2. View user balances");
        System.out.println("3. Perform a transfer");
        System.out.println("4. View all transactions for a specific user");

        if (menuMode == MENU_MODE.DEV){
            System.out.println("5. DEV – remove a transfer by ID");
            System.out.println("6. DEV – check transfer validity");
            System.out.println("7. Finish execution");
        } else {
            System.out.println("5. Finish execution");
        }

        try {
            int cmd = scanner.nextInt();
            scanner.nextLine();
            if (cmd < 1 || (menuMode == MENU_MODE.DEV && cmd > 7) ||
                    (menuMode == MENU_MODE.PRODUCTION && cmd > 5)) {
                printMsg("Invalid command");
                return BAD_CMD;
            } else {
                return cmd;
            }
        } catch (InputMismatchException e){
            scanner.nextLine();
            printMsg("Bad command: " + e.getMessage());
            return BAD_CMD;
        } catch (Exception e){
            printMsg("Bad command: " + e.getMessage());
            return BAD_CMD;
        }
    }

    private void addAUser(){
        printMsg("Enter a user name and a balance");

        try {
            final String inputLine = scanner.nextLine();

            int spacePos = inputLine.indexOf(' ');
            String name = inputLine.substring(0, spacePos);
            int balance = Integer.parseInt(inputLine.substring(spacePos + 1));

            User user = new User();
            user.setName(name);
            user.setBalance(balance);
            transactionsService.addUser(user);
            printMsg("User with id = " + user.getIdentifier() + " is added");
        } catch (Exception e){
            printMsg("Invalid input: " + e.getMessage());
        }
    }
    private void viewUserBalances(){
        printMsg("Enter a user ID");

        try {
            final int inputId = scanner.nextInt();
            scanner.nextLine();

            User user = transactionsService.getUser(inputId);
            printMsg(user.getName() + " - " + user.getBalance());
        } catch (InputMismatchException e){
            printMsg("Invalid input: " + e.getMessage());
            scanner.nextLine();
        } catch (UserNotFoundException e){
            printMsg("Invalid input: " + e.getMessage());
        }
    }
    private void performATransfer(){
        printMsg("Enter a sender ID, a recipient ID, and a transfer amount");

        try {
            final int senderId = scanner.nextInt();
            final int recipientId = scanner.nextInt();
            final int transferAmount = scanner.nextInt();
            scanner.nextLine();

            transactionsService.doTransfer(senderId, recipientId, transferAmount);
        } catch (InputMismatchException e){
            printMsg("Invalid input: " + e.getMessage());
            scanner.nextLine();
        } catch (Exception e){
            printMsg("Invalid input: " + e.getMessage());
        }
    }
    private void viewAllTransactions(){
        printMsg("Enter a user ID");

        try {
            final int userId = scanner.nextInt();
            scanner.nextLine();

            User user = transactionsService.getUser(userId);
            Transaction[] transactions = user.getTransactions().toArray();
            for (Transaction tr : transactions){
                if (tr.getTransferCategory() == TRANSFER_CATEGORY.credits){
                    printMsg("To ", false);
                    printMsg(tr.getRecipient().getName(), false);
                    printMsg("(id = ", false);
                    printMsg(Integer.toString(tr.getRecipient().getIdentifier()), false);
                    printMsg(") ", false);
                } else {
                    printMsg("From ", false);
                    printMsg(tr.getSender().getName(), false);
                    printMsg("(id = ", false);
                    printMsg(Integer.toString(tr.getSender().getIdentifier()), false);
                    printMsg(") ", false);
                }
                printMsg(Integer.toString(tr.getTransferAmount()), false);
                printMsg(" with id = ", false);
                printMsg(tr.getIdentifier().toString());
            }
        } catch (InputMismatchException e){
            printMsg("Invalid input: " + e.getMessage());
            scanner.nextLine();
        } catch (Exception e){
            printMsg("Invalid input: " + e.getMessage());
            printMsg(e.getMessage());
        }
    }
    private void removeTransferById(){
        printMsg("Enter a user ID and a transfer ID");

        try {
            final int userId = scanner.nextInt();
            final UUID uuid = UUID.fromString(scanner.next());

            Transaction tr = transactionsService.getUser(userId).getTransactions().removeById(uuid);
            
            printMsg("Transfer ", false);
            if (tr.getTransferCategory() == TRANSFER_CATEGORY.credits){
                printMsg("To ", false);
                printMsg(tr.getRecipient().getName(), false);
                printMsg("(id = ", false);
                printMsg(Integer.toString(tr.getRecipient().getIdentifier()), false);
                printMsg(") ", false);
            } else {
                printMsg("From ", false);
                printMsg(tr.getSender().getName(), false);
                printMsg("(id = ", false);
                printMsg(Integer.toString(tr.getSender().getIdentifier()), false);
                printMsg(") ", false);
            }
            printMsg(Integer.toString(tr.getTransferAmount()), false);
            printMsg(" removed");
        } catch (InputMismatchException e){
            printMsg("Invalid input: " + e.getMessage());
            scanner.nextLine();
        } catch (Exception e){
            printMsg("Invalid input: " + e.getMessage());
            printMsg(e.getMessage());
        }
    }
    private void checkTransferValidity(){
        printMsg("Check results:");

        for (Transaction tr : transactionsService.checkValidity()){
            if (tr.getTransferCategory() == TRANSFER_CATEGORY.credits){
                printMsg(tr.getSender().getName(), false);
                printMsg("(id = ", false);
                printMsg(Integer.toString(tr.getSender().getIdentifier()), false);
                printMsg(") ", false);
            } else {
                printMsg(tr.getRecipient().getName(), false);
                printMsg("(id = ", false);
                printMsg(Integer.toString(tr.getRecipient().getIdentifier()), false);
                printMsg(") ", false);
            }
            printMsg("has an unacknowledged transfer id = ", false);
            printMsg(tr.getIdentifier().toString(), false);
            if (tr.getTransferCategory() == TRANSFER_CATEGORY.credits){
                printMsg(" to ", false);
                printMsg(tr.getRecipient().getName(), false);
                printMsg("(id = ", false);
                printMsg(Integer.toString(tr.getRecipient().getIdentifier()), false);
                printMsg(") for ", false);
            } else {
                printMsg(" from ", false);
                printMsg(tr.getSender().getName(), false);
                printMsg("(id = ", false);
                printMsg(Integer.toString(tr.getSender().getIdentifier()), false);
                printMsg(") for ", false);
            }
            printMsg(Integer.toString(tr.getTransferAmount()));
        }
    }

    private void printSeparator(){
        printMsg("---------------------------------------------------------");
    }
    private void printMsg(String msg){
        printMsg(msg, true);
    }
    private void printMsg(String msg, boolean newLine){
        if (newLine){
            System.out.println(msg);
        } else {
            System.out.print(msg);
        }
    }
}
