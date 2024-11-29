import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;
import static java.lang.Integer.parseInt; // static so you can use parseInt() instead of Integer.parseInt()


public class Main {

    private static Scanner sc = new Scanner(System.in);
    private static String StartDate;
    private static String EndDate;
    private static int CandleStickSelection;
    private static String FilePath = "N:\\Computing\\A Level\\Java\\Elite Programming Club\\Stock Market Analysis\\CSCO.csv";
    private static File Data = new File(FilePath);
    private static String[][] data;
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
    private static LocalDate start;
    private static LocalDate end;
    private static String Bold = "\033[1m";
    private static String noBold = "\033[0m";

    private static String ShootingStarFilePath = "N:\\Computing\\A Level\\Java\\Elite Programming Club\\Stock Market Analysis\\ShootingStar.csv";
    private static String DarkCloudCoverFilePath = "N:\\Computing\\A Level\\Java\\Elite Programming Club\\Stock Market Analysis\\DarkCloudCover.csv";
    private static String ThreeWhiteSoldiersFilePath = "N:\\Computing\\A Level\\Java\\Elite Programming Club\\Stock Market Analysis\\ThreeWhiteSoldiers.csv";




    public static void main(String[] args) throws IOException {
        System.out.println("Would you like to check the file status? (Yes/No)");
        String selectionStatus = sc.nextLine();
        if (selectionStatus.equalsIgnoreCase("yes")) {
            CheckFileStatus();
        }

        data = readCSV(FilePath);
        System.out.println("Would you like to check the array status? (Yes/No)");
        String selectionStatusArray = sc.nextLine();
        if (selectionStatusArray.equalsIgnoreCase("yes")) {
            PrintArrayCheck();
        }

        System.out.println("This program contains the last 14 years of historical data of CSCO's Stock from 10/07/2010 to 11/05/2024.");
        
        enterDates();

    }

    public static void PrintArrayCheck() {
        for (String[] row : data) {
            for (String value : row) {
                System.out.print(value + " ");
            }
            System.out.println();
        }
    }

    public static void CheckFileStatus() {
        if (Data.exists()) {
            System.out.println("File name: " + Data.getName());
            System.out.println("Absolute path: " + Data.getAbsolutePath());
            System.out.println("Writeable: " + Data.canWrite());
            System.out.println("Readable " + Data.canRead());
            System.out.println("File size in bytes " + Data.length());
        } else {
            System.out.println("The file does not exist.");
        }
    }

    public static String[][] readCSV(String filePath) throws IOException { // says if there's any input output issue
        BufferedReader br = new BufferedReader(new FileReader(filePath));
        String line;
        int rowCount = 0;

        // first count the rows
        while ((line = br.readLine()) != null) {
            rowCount++;
        }
        // Close and reopen BufferedReader to reset it to the start of the file
        br.close();
        br = new BufferedReader(new FileReader(filePath));
        // Initialize the 2D array with the number of rows counted
        String[][] data = new String[rowCount][];
        // Second pass: read the file again and fill the array
        int index = 0;
        while ((line = br.readLine()) != null) {
            line = line.replace("\"", ""); // removes quotation marks in csv file
            data[index] = line.split(",");  // Split each line by commas
            index++;
        }
        br.close();
        return data;
    }

    public static void enterDates() {
        while (true) {
            System.out.println("Please enter the start date in MM/DD/YYYY format: ");
            StartDate = sc.nextLine();
            int Start_month = parseInt(StartDate.substring(0, 2));
            int Start_day = parseInt(StartDate.substring(3, 5));
            int Start_year = parseInt(StartDate.substring(6, 10));

            try {
                start = LocalDate.parse(StartDate, formatter);
                if (Start_month > 0 && Start_month < 13 && Start_day > 0 && Start_day < 31 && Start_year > 2009 && Start_year < 2025) {
                    System.out.println("You entered a valid start date: " + start);
                    break;
                } else {
                    System.out.println("Invalid date. Enter a date between 10/07/2010 and 11/05/2024");
                    continue;
                }
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Please use MM/DD/YYYY.");
            }
        }
        while (true) {
            System.out.println("Please enter the end date in MM/DD/YYYY format: ");
            EndDate = sc.nextLine();
            int End_month = parseInt(EndDate.substring(0, 2));
            int End_day = parseInt(EndDate.substring(3, 5));
            int End_year = parseInt(EndDate.substring(6, 10));
            try {
                end = LocalDate.parse(EndDate, formatter);
                if (End_month > 0 && End_month < 13 && End_day > 0 && End_day < 31 && End_year > 2009 && End_year < 2025) {
                    System.out.println("You entered a valid start date: " + end);
                    break;
                } else {
                    System.out.println("Invalid date. Enter a date between 10/07/2010 and 11/05/2024");
                    continue;
                }
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Please use MM/DD/YYYY.");
            }
        }
        CandleStickChoose();
    }

    public static void CandleStickChoose() {
        System.out.println("1. Three White Soldiers");
        System.out.println("2. Dark Cloud Cover");
        System.out.println("3. Shooting Star");
        do {
            System.out.println("Select which candlestick pattern you would like to observe (1,2 or 3) : ");
            CandleStickSelection = sc.nextInt();
        } while ((CandleStickSelection < 1 || CandleStickSelection > 3));

        switch (CandleStickSelection) {
            case 1:
                ThreeWhiteSoldiers(data);
                break;
            case 2:
                DarkCloudCover(data);
                break;
            case 3:
                ShootingStar(data);
                break;
        }

    }

    public static void ThreeWhiteSoldiers(String[][] data) {
        System.out.println("You have chosen the Three White Soldiers pattern. ");
        boolean patternDetected = false;
        boolean isFirstRow = true;

        for (int i = 2; i < data.length; i++) {
            String[] row1 = data[i - 2]; // two days before current
            String[] row2 = data[i - 1]; // day before current
            String[] row3 = data[i]; //current day

            // first row is header and cannot be converted into an integer or formatted into time thus skipp first row
            if (isFirstRow) {
                isFirstRow = false;
                continue;
            }

            String date1 = row1[0];
            String date2 = row2[0];
            String date3 = row3[0];

            LocalDate rowDate1 = LocalDate.parse(date1, formatter);
            LocalDate rowDate2 = LocalDate.parse(date2, formatter);
            LocalDate rowDate3 = LocalDate.parse(date3, formatter);

            // checking whether it before the end and after the start dates entered by user
            if(rowDate1.isAfter(start) && rowDate1.isBefore(end) &&
                    rowDate2.isAfter(start) && rowDate2.isBefore(end) &&
                    rowDate3.isAfter(start) && rowDate3.isBefore(end)) {

                // setting open and closes for the 3 days
                double open1 = Double.parseDouble(row1[1]);
                double close1 = Double.parseDouble(row1[2]);
                double open2 = Double.parseDouble(row2[1]);
                double close2 = Double.parseDouble(row2[2]);
                double open3 = Double.parseDouble(row3[1]);
                double close3 = Double.parseDouble(row3[2]);

                if(CheckThreeWhiteSoldiers(open1,close1,open2,close2,open3,close3)) {
                    if (close1 < close2 && close2 < close3) {
                        System.out.println(Bold + "The Three White Soldier Pattern has been detected from: " + date3 + " to: " + date1 + noBold);
                        patternDetected = true;
                    }
                }
            }
        }
        if (!patternDetected) {
            System.out.println(Bold + "No Three White Soldier pattern detected." + noBold);
        }

        AnotherPattern();
    }

    public static boolean CheckThreeWhiteSoldiers(double open1, double close1, double open2, double close2, double open3, double close3) {
        return (close1 > open1) &&
                (close2 > open2) &&
               (close3 > open3) &&
                (close1<close2  && close2 < close3);
    }

    public static void DarkCloudCover(String[][] data) {
        System.out.println("You have chosen the Dark Cloud Cover pattern. ");
        boolean patternDetected = false;
        boolean isFirstRow = true;

        for (int i = 1; i < data.length; i++) {
            if (isFirstRow) {
                isFirstRow = false;
                continue;
            }

            String rowDate1 = data[i - 1][0];
            String rowDate2 = data[i][0];

                LocalDate date1 = LocalDate.parse(rowDate1, formatter);
                LocalDate date2 = LocalDate.parse(rowDate2, formatter);

                if (date1.isAfter(start) && date1.isBefore(end) &&
                    date2.isAfter(start) && date2.isBefore(end)) {

                    double open1 = Double.parseDouble(data[i - 1][2]);
                    double close1 = Double.parseDouble(data[i - 1][1]);
                    double high1 = Double.parseDouble(data[i - 1][3]);

                    double open2 = Double.parseDouble(data[i][2]);
                    double close2 = Double.parseDouble(data[i][1]);

                    if (CheckDarkCloudCover(open1, close1, high1, open2, close2)) {
                        System.out.println(Bold + "Dark Cloud Cover pattern detected from: " + date2 + " to: " + date1 + noBold);
                        patternDetected = true;
                    }
                }
    }
        if (!patternDetected) {
            System.out.println(Bold + "No Dark Cloud Cover pattern detected." + noBold);
        }
        AnotherPattern();
    }

    public static boolean CheckDarkCloudCover(double prevOpen, double prevClose, double prevHigh, double currentOpen, double currentClose) {
        return (prevClose > prevOpen)
                && (currentOpen > prevHigh)
                && (currentClose < (prevOpen + prevClose) / 2)
                && (currentClose < currentOpen);
    }


    public static void ShootingStar(String[][] data) {
        System.out.println("You have chosen the Shooting Star pattern.");
        boolean patternDetected = false;
        boolean isFirstRow = true;


        for (String[] row : data) {
            if (isFirstRow) {
                isFirstRow = false; // Skip the first row
                continue;
            }
            if (row.length < 5) continue; // Skip invalid rows
            String date = row[0];
            LocalDate rowDate = LocalDate.parse(date, formatter);

            if (rowDate.isAfter(start) && rowDate.isBefore(end)){

                double open = Double.parseDouble(row[2]);
                double close = Double.parseDouble(row[1]);
                double high = Double.parseDouble(row[3]);
                double low = Double.parseDouble(row[4]);

                if (CheckShootingStar(open, close, high, low)) {
                    System.out.println(Bold + "Shooting Star pattern detected on " + date + noBold);
                    patternDetected = true;
                }
        }
            }
            if (!patternDetected) {
                System.out.println(Bold + "No Shooting Star pattern detected." + noBold);
            }
            AnotherPattern();
        
    }

    public static boolean CheckShootingStar(double open, double close, double high, double low) {
        double bodySize = Math.abs(close - open);
        double upperShadow = high - Math.max(open, close);
        double lowerShadow = Math.min(open, close) - low;

        return bodySize < upperShadow / 2 && // Body is small compared to upper shadow
                upperShadow >= 2 * bodySize && // Upper shadow is at least twice the size of the body
                lowerShadow <= bodySize * 0.1; // Lower shadow is very small or non-existent
    }

    public static void AnotherPattern() {
        sc.nextLine();
        int AnotherGoSelection;
        System.out.println("1. Look for another pattern with different same dates.");
        System.out.println("2. Look for another pattern with same dates.");
        System.out.println("3. Exit the program.");
        do{
            System.out.println("Select which option you would like to choose (1,2 or 3) : ");
            AnotherGoSelection = sc.nextInt();
        } while((AnotherGoSelection < 1 || AnotherGoSelection > 3));

        switch(AnotherGoSelection){
            case 1:
                sc.nextLine();
                enterDates();
                break;
            case 2:
                CandleStickChoose();
                break;
            case 3:
                System.out.println("You have exited the program.");
                System.exit(0);
                break;
        }
    }
}


