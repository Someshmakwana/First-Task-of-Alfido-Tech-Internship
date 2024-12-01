import java.io.*;
import java.util.Scanner;

class BankAccount {
    private String accountNumber;
    private String accountHolderName;
    private double balance;

    public BankAccount(String accountNumber, String accountHolderName, double initialBalance) {
        this.accountNumber = accountNumber;
        this.accountHolderName = accountHolderName;
        this.balance = initialBalance;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
            System.out.println("Deposited: " + amount);
        } else {
            System.out.println("Invalid deposit amount.");
        }
    }

    public void withdraw(double amount) {
        if (amount > 0 && amount <= balance) {
            balance -= amount;
            System.out.println("Withdrawn: " + amount);
        } else {
            System.out.println("Insufficient balance or invalid amount.");
        }
    }

    public void displayBalance() {
        System.out.println("Current Balance: " + balance);
    }

    @Override
    public String toString() {
        return accountNumber + "," + accountHolderName + "," + balance;
    }

    public static BankAccount fromString(String accountData) {
        String[] parts = accountData.split(",");
        return new BankAccount(parts[0], parts[1], Double.parseDouble(parts[2]));
    }
}

public class BankingSystem {
    private static final String FILE_NAME = "accounts.txt";

    public static void saveAccount(BankAccount account) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, true))) {
            writer.write(account.toString());
            writer.newLine();
        } catch (IOException e) {
            System.out.println("Error saving account: " + e.getMessage());
        }
    }

    public static BankAccount loadAccount(String accountNumber) {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                BankAccount account = BankAccount.fromString(line);
                if (account.getAccountNumber().equals(accountNumber)) {
                    return account;
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading account: " + e.getMessage());
        }
        return null;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n--- Banking System ---");
            System.out.println("1. Create Account");
            System.out.println("2. Deposit");
            System.out.println("3. Withdraw");
            System.out.println("4. Check Balance");
            System.out.println("5. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1: // Create Account
                    System.out.print("Enter Account Number: ");
                    String accountNumber = scanner.next();
                    System.out.print("Enter Account Holder Name: ");
                    String accountHolderName = scanner.next();
                    System.out.print("Enter Initial Balance: ");
                    double initialBalance = scanner.nextDouble();

                    BankAccount newAccount = new BankAccount(accountNumber, accountHolderName, initialBalance);
                    saveAccount(newAccount);
                    System.out.println("Account created successfully!");
                    break;

                case 2: // Deposit
                    System.out.print("Enter Account Number: ");
                    accountNumber = scanner.next();
                    BankAccount account = loadAccount(accountNumber);

                    if (account != null) {
                        System.out.print("Enter deposit amount: ");
                        double depositAmount = scanner.nextDouble();
                        account.deposit(depositAmount);
                        System.out.println("Updated Balance:");
                        account.displayBalance();
                        saveAccount(account); // Update account info
                    } else {
                        System.out.println("Account not found!");
                    }
                    break;

                case 3: // Withdraw
                    System.out.print("Enter Account Number: ");
                    accountNumber = scanner.next();
                    account = loadAccount(accountNumber);

                    if (account != null) {
                        System.out.print("Enter withdrawal amount: ");
                        double withdrawalAmount = scanner.nextDouble();
                        account.withdraw(withdrawalAmount);
                        System.out.println("Updated Balance:");
                        account.displayBalance();
                        saveAccount(account); // Update account info
                    } else {
                        System.out.println("Account not found!");
                    }
                    break;

                case 4: // Check Balance
                    System.out.print("Enter Account Number: ");
                    accountNumber = scanner.next();
                    account = loadAccount(accountNumber);

                    if (account != null) {
                        System.out.println("Account Holder: " + account.getAccountNumber());
                        account.displayBalance();
                    } else {
                        System.out.println("Account not found!");
                    }
                    break;

                case 5: // Exit
                    System.out.println("Exiting... Thank you!");
                    scanner.close();
                    return;

                default:
                    System.out.println("Invalid option. Try again.");
            }
        }
    }
}
