public class BankApp {
    public static void main(String[] args) {
        Bank bank = new Bank(1, "SAVINGS");
        bank.setBalance(100);
        System.out.println("User ID: " + bank.getUserId());
        System.out.println("Account Type: " + bank.getAccountType());
        System.out.print("Balance: " + bank.getBalance());
    }
}

class Bank {
    private int userId;
    private double balance;
    private String accountType;

    public Bank(int userId, String accountType) {
        this.userId = userId;
        this.balance = 0.0;
        this.accountType = accountType;
    }

    public int getUserId() {
        return this.userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public double getBalance() {
        return this.balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getAccountType() {
        return this.accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }
}
