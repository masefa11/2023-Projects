import java.util.Scanner;

public class BlueRayDiskCollection {
    static class BlueRayDisk {
        public String title, director;
        public int yearOfRelease;
        public double cost;

        BlueRayDisk(String t, String d, int y, double c) {
            this.title = t;
            this.director = d;
            this.yearOfRelease = y;
            this.cost = c;
        }

        @Override
        public String toString() {
            return ("$" + cost + " " + yearOfRelease + " " + title + ", " + director);
        }
    }

    static class Node {
        private BlueRayDisk data;
        private Node next;

        public Node(BlueRayDisk data) {
            this.data = data;
            this.next = null;
        }

    }

    static class BlueRayCollection {
        private Node head;

        BlueRayCollection() {
            head = null;
        }

        public void add(String title, String director, int yearOfRelease, double cost) {
            BlueRayDisk blueRayDisk = new BlueRayDisk(title, director, yearOfRelease, cost);
            Node temp = new Node(blueRayDisk);
            if (head == null) {
                head = temp;
            } else {
                Node current = head;
                while (current.next != null) {
                    current = current.next;
                }
                current.next = temp;
            }
        }

        public void show_all() {
            Node current = head;
            while (current != null) {
                System.out.println(current.data.toString());
                current = current.next;
            }
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        BlueRayCollection blueRayCollection = new BlueRayCollection();
        while (true) {
            System.out.print("\n0. Quit\n" +
                    "1. Add BlueRay to collection\n" +
                    "2. See collection\n");
            int choice = sc.nextInt();
            if (choice == 1)
            {
                System.out.print("What is the tile?\n");
                String title = sc.next();
                System.out.print("What is the director?\n");
                String director = sc.next();
                System.out.print("What is the year of release?\n");
                int year = sc.nextInt();
                System.out.print("What is the cost?\n");
                double cost = sc.nextDouble();
                blueRayCollection.add(title, director, year, cost);
            }
            else if (choice == 2)
            {
                blueRayCollection.show_all();

            }
            else if (choice == 0)
            {
                break;
            }

        }
    }
}
