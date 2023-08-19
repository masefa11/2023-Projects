import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class BitcoinInvestmentManagerwithFileIOandExceptionHandling {
    public static void main(String[] args) throws Driver.PersonNotFoundException, IOException
    {
        float currentPrice;
        Scanner sc = new Scanner(System.in);
        int choice;
        while (true) {
            System.out.print("One BitCoin is currently worth $" + Driver.getDollarPrice(Driver.getData()) +
                    "\n1. Buy Bitcoin\n" +
                    "2. See everyones current value in USD\n" +
                    "3. See one persons gain/loss\n" +
                    "4. Quit\n");
            choice = sc.nextInt();
            if (choice == 1) {
                currentPrice= Driver.getDollarPrice(Driver.getData());
                Driver.buyBitCoin(currentPrice);
            } else if (choice == 2)
            {
                currentPrice= Driver.getDollarPrice(Driver.getData());
                Driver.getCurrentValue(currentPrice);
            } else if (choice == 3) {
                Driver.getDollarPrice(Driver.getData());
                System.out.print("Enter a name\n");
                String name = sc.next();
                float originalInvestmentUSD = Float.parseFloat(Driver.getPersonFromFile(name, "initialInvestmentUSD.txt").split(":")[1]);
                float currentValue = Float.parseFloat(Driver.getPersonFromFile(name, "C:\\Users\\Meyaa\\Downloads\\Assignment6\\clientBC.txt").split(":")[1])* Driver.getDollarPrice(Driver.getData());
                float changeInValue = currentValue - originalInvestmentUSD;
                System.out.println("Original Investment: $" + originalInvestmentUSD + "\nNumber of bitcoins: " + Float.parseFloat(Driver.getPersonFromFile(name, "C:\\Users\\Meyaa\\Downloads\\Assignment6\\clientBC.txt").split(":")[1]) +"\nCurrent Value: $"+ currentValue +"\nChange in value: $" + changeInValue);
            } else {
                break;
            }
        }
        sc.close();
    }
}

class Driver {
    public static ArrayList<String> getData() {
        ArrayList<String> data = new ArrayList<>();
        try {
            Socket mySocket = new Socket("api.coindesk.com", 80);
            OutputStream os = mySocket.getOutputStream();
            PrintWriter pw = new PrintWriter(os);
            pw.println("GET http://api.coindesk.com/v1/bpi/currentprice.json HTTP/1.0\n\n");
            pw.flush();
            Scanner sc  = new Scanner(mySocket.getInputStream());
            while(sc.hasNextLine())
            {
                data.add(sc.nextLine());
            }
            return data;
        }
        catch (IOException e) {
            System.out.println(e);
            return data;
        }

    }

    public static float getDollarPrice(ArrayList<String> lines)
    {
        boolean header=true;
        String json="";
        for(String line : lines) {
            if(line.equals("")) {
                header=false;
                continue;
            }
            if(header==false) {
                json=line;
                break;
            }
        }
        //System.out.println("Json: "+json);
        String[] jsonParts=json.split(":");
        String priceLine=jsonParts[19];
        String justPrice=priceLine.replace("},\"GBP\"","");
        float price=Float.parseFloat(justPrice);
        return price;
    }

    public static void buyBitCoin(float bitCoin) {

        try {
            File file = new File("initialInvestmentUSD.txt");
            File file2 = new File("C:/Users/Meyaa/Downloads/Assignment6/clientBC.txt");
            Scanner sc = new Scanner(file);
            PrintWriter pw = new PrintWriter(file2);
            ArrayList<String> lines = new ArrayList<String>();
            while (sc.hasNextLine())
            {
                String line = sc.nextLine();
                String[] splitLines = line.split(":");
                float newPrice = Float.parseFloat(splitLines[1])/bitCoin;
                lines.add(splitLines[0]+":"+newPrice);
            }
            sc.close();
            for(String l: lines)
            {
                pw.println(l);
            }
            pw.close();
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
    }

    public static void getCurrentValue(float currentBitCoinValue) {
        try {
            File file = new File("C:/Users/Meyaa/Downloads/Assignment6/clientBC.txt");
            Scanner sc = new Scanner(file);
            while (sc.hasNextLine())
            {
                String l = sc.nextLine();
                String[] splitLines = l.split(":");
                float bitcoin= Float.parseFloat(splitLines[1]);
                float value =bitcoin * currentBitCoinValue;
                System.out.println(splitLines[0] + ":$" + value);
            }
            sc.close();
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
    }


    public static class PersonNotFoundException extends Exception {
        public PersonNotFoundException(String m) {
            super(m);
        }
    }

    public static String getPersonFromFile(String nameOfPerson, String nameOfFile) throws PersonNotFoundException
    {
        try {
            File file = new File(nameOfFile);
            Scanner sc = new Scanner(file);
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                String[] splitLines = line.split(":");
                String name = splitLines[0];
                if (name.equals(nameOfPerson))
                {
                    return line;
                }
            }
            sc.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        throw new PersonNotFoundException(nameOfPerson);
    }
}