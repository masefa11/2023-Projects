import jdk.dynalink.beans.StaticClass;
import java.io.*;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Scanner;

public class GradeBookFiles {
    public static class Student
    {
        private final int[] quizScore = new int[10];
        private final int[] hwScore = new int[10];
        private int midterm;
        private int finalExam;
        private double quizAverage;
        private double hwAverage;
        private double average;
        private final String name;
        private int id;

        Student(String s)
        {
            String[] split = s.split(",");
            this.name = split[0];
            try {
                this.id = Integer.parseInt(split[1]);
            }
            catch (NumberFormatException e)
            {
return;
            }
            quizScore[0] = Integer.parseInt(split[2]);
            quizScore[1] = Integer.parseInt(split[3]);
            quizScore[2] = Integer.parseInt(split[4]);
            quizScore[3] = Integer.parseInt(split[5]);
            quizScore[4] = Integer.parseInt(split[6]);
            quizScore[5] = Integer.parseInt(split[7]);
            quizScore[6] = Integer.parseInt(split[8]);
            quizScore[7] = Integer.parseInt(split[9]);
            quizScore[8] = Integer.parseInt(split[10]);
            quizScore[9] = Integer.parseInt(split[11]);

            hwScore[0] = Integer.parseInt(split[12]);
            hwScore[1] = Integer.parseInt(split[13]);
            hwScore[2] = Integer.parseInt(split[14]);
            hwScore[3] = Integer.parseInt(split[15]);
            hwScore[4] = Integer.parseInt(split[16]);
            hwScore[5] = Integer.parseInt(split[17]);
            hwScore[6] = Integer.parseInt(split[18]);
            hwScore[7] = Integer.parseInt(split[19]);
            hwScore[8] = Integer.parseInt(split[20]);
            hwScore[9] = Integer.parseInt(split[21]);

            midterm = Integer.parseInt(split[22]);
            finalExam = Integer.parseInt(split[23]);
        }


        public String getName() {
            return name;
        }

        public int getId()
        {
            return id;
        }
        void calcQuizAverage()
        {
            int[] quiz = Arrays.copyOf(quizScore, quizScore.length);
            Arrays.sort(quiz);
            double total = 0;
            for (int i = 1; i < quiz.length; i++) {
                total += quiz[i];
            }
            quizAverage = total / 9.0;
        }
        void calcHWAverage()
        {
            int[] hw = Arrays.copyOf(hwScore, hwScore.length);
            Arrays.sort(hw);
            double total = 0;
            for (int i = 1; i < hw.length; i++) {
                total += hw[i];
            }
            hwAverage = total / 9.0;
        }
        void calcOverallAverage()
        {
            average = 0.4*quizAverage +  0.1*hwAverage + 0.2*midterm + 0.3*finalExam;
        }
        String getGrade()
        {
            StringBuilder s = new StringBuilder("Name: " + name + "\n");
            for (int i = 0; i < 10; i++)
            {
                s.append("Quiz ").append(i).append(": ").append(quizScore[i]).append("\n");
            }
            s.append("Quiz Avg: ").append(quizAverage).append("\n");
            for (int i = 0; i < 10; i++) {
                s.append("HW ").append(i).append(": ").append(hwScore[i]).append("\n");
            }
            s.append("HW Avg: ").append(hwAverage).append("\n");
            s.append("Midterm: ").append(midterm).append("\n");
            s.append("Final Exam: ").append(finalExam).append("\n");
            s.append("Overall Average: ").append(average).append("\n");
            return s.toString();
        }
    }
    public static class GradeBook {

        protected static LinkedList<Student> students = new LinkedList<>();

        public GradeBook(String file) {
            try
            {
                String line =" ";
                File file1 = new File(file);
                Scanner sc = new Scanner(file1);
                while (sc.hasNextLine())
                {
                    line = sc.nextLine();
                    students.add(new Student(line));
                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }

        public static Student getStudent(String name)
        {
            for (Student s : students) {
                if (s.getName().equals(name)) {
                    return s;
                }
            }
            return null;
        }

        public static void getStudentGrade(String name) {
            Student student = getStudent(name);
            if (student != null)
            {
                System.out.println(student.getGrade());
            } else {
                System.out.println("Not found.");
            }
        }

        public LinkedList<String> getAllStudentName() {
            LinkedList<String> listOfNames = new LinkedList<>();
            for (Student s : students)
            {
                listOfNames.add(s.getName());
            }
            return listOfNames;
        }
    }

    public static class StatisticGradeBook extends GradeBook implements Runnable {
        StatisticGradeBook(String fileName)
        {
            super(fileName);
        }

        @Override
        public void run() {
            LinkedList<String> studentNames = getAllStudentName();
            int count = studentNames.size();
            int processed = 0;
            for (Student s : students) {
                s.calcQuizAverage();
                s.calcHWAverage();
                s.calcOverallAverage();
                processed++;
                if (processed % 100 == 0)
                {
                    System.out.println("Calculating grades " + processed + " out of " + (count-1));
                }
            }
            System.out.println("All grades are calculated");
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        StatisticGradeBook statisticGradeBook = new StatisticGradeBook("C:\\Users\\Meyaa\\Downloads\\Assignment7-Spreadsheet (1).csv");
        Thread thread = new Thread(statisticGradeBook);
        thread.start();

        System.out.print("What student would you like to see grades for ");
        String student = sc.nextLine();

        GradeBook.getStudentGrade(student);
    }
}
