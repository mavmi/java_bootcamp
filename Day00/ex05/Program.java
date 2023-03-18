package ex05;

import java.util.Scanner;

public class Program {
    private static final int MAX_STUDENTS = 10;
    private static final int MAX_LESSONS_PER_WEEK = 10;

    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in);

        String[] students = new String[MAX_STUDENTS];
        int studentsCount = getStudents(scanner, students);

        int[] lessons = new int[MAX_LESSONS_PER_WEEK * 2];
        int lessonsCount = getLessons(scanner, lessons);

        int[] attendance = new int[studentsCount * lessonsCount * 5 * 4];
        int attendanceCount = getAttendance(scanner, attendance, students, studentsCount, lessons, lessonsCount);

        int[] calendar = new int[30 * 3];
        int calendarCount = getCalendar(calendar, lessons, lessonsCount);
        if (calendarCount == 0){
            System.exit(0);
        }
        
        print(calendar, calendarCount, students, studentsCount, attendance, attendanceCount);
    }

    private static void print(int[] calendar, int calendarCount, String[] students, int studentsCount, int[] attendance, int attendanceCount){
        final String eight = "        ";
        final String nine = "         ";
        final String ten = "          ";

        System.out.print(ten);
        for (int i = 0; i < calendarCount; i += 3){
            System.out.print(calendar[i] + ":00 " + getDayOfWeek(calendar[i + 1]) + " ");
            int date = calendar[i + 2];
            if (date < 10){
                System.out.print(" ");
            }
            System.out.print(date + "|");
        }
        System.out.print("\n");

        for (int studentId = 0; studentId < studentsCount; studentId++){
            final String studentName = students[studentId];
            for (int i = 0; i < 10 - studentName.length(); i++){
                System.out.print(" ");
            }
            System.out.print(studentName);

            for (int calen = 0; calen < calendarCount; calen += 3){
                for (int att = 0; att < attendanceCount; att += 4){
                    if (attendance[att] == studentId && calendar[calen] == attendance[att + 1] && calendar[calen + 2] == attendance[att + 2]){
                        if (attendance[att + 3] == 1){
                            System.out.print(nine);
                            System.out.print(1);
                        } else {
                            System.out.print(eight);
                            System.out.print(-1);
                        }
                        break;
                    } else if (att + 4 >= attendanceCount){
                        System.out.print(ten);
                    }
                }
                System.out.print("|");
                if (calen + 3 >= calendarCount){
                    System.out.print("\n");
                }
            }
        }
    }

    private static int getStudents(Scanner scanner, String[] students){
        int i = 0;
        while (true){
            String inputLine = scanner.nextLine();
            if (inputLine.equals(".")){
                return i;
            }
            students[i++] = inputLine;
        }
    }
    private static int getLessons(Scanner scanner, int[] lessons){
        int i = 0;
        while (true){
            String inputLine = scanner.nextLine();
            if (inputLine.equals(".")){
                break;
            }
            char[] inputArray = inputLine.toCharArray();
            lessons[i] = inputArray[0] - '0';
            lessons[i + 1] = getDayOfWeek(inputArray[2], inputArray[3]);
            i += 2;
        }

        for (int j = 0; j < i - 2; j += 2){
            if (lessons[j + 1] > lessons[j + 3] || (lessons[j + 1] == lessons[j + 3] && lessons[j] > lessons[j + 2])){
                int tmp1 = lessons[j];
                int tmp2 = lessons[j + 1];
                lessons[j] = lessons[j + 2];
                lessons[j + 1] = lessons[j + 3];
                lessons[j + 2] = tmp1;
                lessons[j + 3] = tmp2;
                j = -2;
            }
        }

        return i;
    }
    private static int getAttendance(Scanner scanner, int[] attendance, String[] students, int studentsCount, int[] lessons, int lessonsCount){
        int i = 0;
        while (true){
            String inputLine = scanner.nextLine();
            if (inputLine.equals(".")){
                return i;
            }
            char[] inputArray = inputLine.toCharArray();

            int begin = 0;
            int spacePos = find(inputArray, begin, ' ');
            int studentId = findStudent(new String(inputArray, 0, spacePos), students, studentsCount);

            int lessonTime = inputArray[spacePos + 1] - '0';

            begin = spacePos + 3;
            spacePos = find(inputArray, begin, ' ');
            int date = parseDate(new String(inputArray, begin, spacePos - begin));

            int isHere = -1;
            if (new String(inputArray, spacePos + 1, inputArray.length - spacePos - 1).equals("HERE")){
                isHere = 1;
            }

            attendance[i] = studentId;
            attendance[i + 1] = lessonTime;
            attendance[i + 2] = date;
            attendance[i + 3] = isHere;
            i += 4;
        }
    }
    private static int getCalendar(int[] calendar, int[] lessons, int lessonsCount){
        int calendarCount = 0;

        for (int day = 1; day <= 30; day++){
            int dayNum = day % 7 + 1;
            for (int i = 0; i < lessonsCount; i += 2){
                if (dayNum == lessons[i + 1]){
                    calendar[calendarCount] = lessons[i];
                    calendar[calendarCount + 1] = lessons[i + 1];
                    calendar[calendarCount + 2] = day;
                    calendarCount += 3;
                }
            }
        }

        return calendarCount;
    }

    private static int getDayOfWeek(char c1, char c2){
        if (c1 == 'M' && c2 == 'O'){
            return 1;
        } else if (c1 == 'T' && c2 == 'U'){
            return 2;
        } else if (c1 == 'W' && c2 == 'E'){
            return 3;
        } else if (c1 == 'T' && c2 == 'H'){
            return 4;
        } else if (c1 == 'F' && c2 == 'R'){
            return 5;
        } else if (c1 == 'S' && c2 == 'A'){
            return 6;
        } else if (c1 == 'S' && c2 == 'U'){
            return 7;
        } else {
            return 0;
        }
    }
    private static String getDayOfWeek(int dayNum){
        switch (dayNum){
            case 1:
                return "MO";
            case 2:
                return "TU";
            case 3:
                return "WE";
            case 4:
                return "TH";
            case 5:
                return "FR";
            case 6:
                return "SA";
            case 7:
                return "SU";
            default:
                return "";
        }
    }
    private static int findStudent(String name, String[] students, int studentsCount){
        for (int i = 0; i < studentsCount; i++){
            if (name.equals(students[i])){
                return i;
            }
        }

        return -1;
    }
    private static int parseDate(String date){
        int result = 0;

        char[] dateArray = date.toCharArray();
        for (int i = 0; i < dateArray.length; i++){
            result = result * 10 + (dateArray[i] - '0');
        }

        return result;
    }

    private static int find(char[] array, int begin, char c){
        while (begin < array.length){
            if (array[begin] == c){
                return begin;
            }
            begin++;
        }

        return -1;
    }
}
