import java.util.InputMismatchException;
import java.util.Scanner;

public class Calculator {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n===============================");
            System.out.println("        SMART CALCULATOR       ");
            System.out.println("===============================");
            System.out.println("1. Basic Arithmetic");
            System.out.println("2. Scientific Calculations");
            System.out.println("3. Unit Conversions");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");

            int choice = getSafeInt(scanner);

            switch (choice) {
                case 1:
                    basicArithmetic(scanner);
                    break;

                case 2:
                    scientificCalculations(scanner);
                    break;

                case 3:
                    unitConversions(scanner);
                    break;

                case 4:
                    System.out.println("\nThank you for using Smart Calculator. Have a great day!");
                    return;

                default:
                    System.out.println("⚠ Invalid choice! Please choose between 1 - 4.");
            }
        }
    }

    // Utility to avoid input mismatch errors
    private static int getSafeInt(Scanner scanner) {
        while (true) {
            try {
                return scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.print("Invalid input! Please enter a number: ");
                scanner.nextLine();
            }
        }
    }

    // Utility to handle secure double inputs
    private static double getSafeDouble(Scanner scanner) {
        while (true) {
            try {
                return scanner.nextDouble();
            } catch (InputMismatchException e) {
                System.out.print("Invalid input! Enter a valid number: ");
                scanner.nextLine();
            }
        }
    }

    // ================= BASIC ARITHMETIC =================
    private static void basicArithmetic(Scanner scanner) {
        System.out.println("\n--- Basic Arithmetic ---");

        System.out.print("Enter first number: ");
        double num1 = getSafeDouble(scanner);

        System.out.print("Enter second number: ");
        double num2 = getSafeDouble(scanner);

        System.out.println("\nChoose Operation:");
        System.out.println("1. Addition (+)");
        System.out.println("2. Subtraction (-)");
        System.out.println("3. Multiplication (×)");
        System.out.println("4. Division (÷)");
        System.out.print("Enter your choice: ");

        int operation = getSafeInt(scanner);

        switch (operation) {
            case 1:
                System.out.println("Result: " + (num1 + num2));
                break;

            case 2:
                System.out.println("Result: " + (num1 - num2));
                break;

            case 3:
                System.out.println("Result: " + (num1 * num2));
                break;

            case 4:
                if (num2 == 0) {
                    System.out.println("❌ Error: Cannot divide by zero!");
                } else {
                    System.out.println("Result: " + (num1 / num2));
                }
                break;

            default:
                System.out.println("⚠ Invalid operation selected!");
        }
    }

    // ================= SCIENTIFIC CALCULATIONS =================
    private static void scientificCalculations(Scanner scanner) {
        System.out.println("\n--- Scientific Calculations ---");
        System.out.println("1. Square Root");
        System.out.println("2. Power (a^b)");
        System.out.print("Choose an option: ");

        int option = getSafeInt(scanner);

        switch (option) {
            case 1:
                System.out.print("Enter a number: ");
                double n = getSafeDouble(scanner);
                System.out.println("Square Root = " + Math.sqrt(n));
                break;

            case 2:
                System.out.print("Enter base: ");
                double base = getSafeDouble(scanner);

                System.out.print("Enter exponent: ");
                double exponent = getSafeDouble(scanner);

                System.out.println("Result = " + Math.pow(base, exponent));
                break;

            default:
                System.out.println("⚠ Invalid option selected!");
        }
    }

    // ================= UNIT CONVERSIONS =================
    private static void unitConversions(Scanner scanner) {
        System.out.println("\n--- Unit Conversions ---");
        System.out.println("1. Celsius → Fahrenheit");
        System.out.println("2. Fahrenheit → Celsius");
        System.out.println("3. INR → USD");
        System.out.println("4. USD → INR");
        System.out.print("Select conversion: ");

        int option = getSafeInt(scanner);

        switch (option) {
            case 1:
                System.out.print("Enter °C: ");
                double celsius = getSafeDouble(scanner);
                System.out.println("Fahrenheit = " + ((celsius * 9 / 5) + 32));
                break;

            case 2:
                System.out.print("Enter °F: ");
                double fahrenheit = getSafeDouble(scanner);
                System.out.println("Celsius = " + ((fahrenheit - 32) * 5 / 9));
                break;

            case 3:
                System.out.print("Enter INR: ");
                double inr = getSafeDouble(scanner);
                System.out.println("USD ≈ " + (inr / 84.0));
                break;

            case 4:
                System.out.print("Enter USD: ");
                double usd = getSafeDouble(scanner);
                System.out.println("INR ≈ " + (usd * 84.0));
                break;

            default:
                System.out.println("⚠ Invalid conversion option!");
        }
    }
}
