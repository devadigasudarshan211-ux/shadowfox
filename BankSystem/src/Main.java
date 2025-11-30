public class Main {
    public static void main(String[] args) {
        BankAccount acc = new BankAccount("Sudarshan", 5000);

        acc.deposit(500);
        acc.withdraw(200);

        System.out.println(acc.getAccountSummary());
        System.out.println("Transaction History:");
        for (String t : acc.getTransactions()) {
            System.out.println(t);
        }
    }
}
