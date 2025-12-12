package bank.core;

import bank.common.ConstantMessages;
import bank.common.ExceptionMessages;
import bank.entities.bank.Bank;
import bank.entities.bank.BranchBank;
import bank.entities.bank.CentralBank;
import bank.entities.client.Adult;
import bank.entities.client.Client;
import bank.entities.client.Student;
import bank.entities.loan.Loan;
import bank.entities.loan.MortgageLoan;
import bank.entities.loan.StudentLoan;
import bank.repositories.LoanRepository;

import java.util.ArrayList;
import java.util.Collection;

public class ControllerImpl implements Controller {
    private LoanRepository loans;
    private Collection<Bank> banks;

    public ControllerImpl() {
        this.loans = new LoanRepository();
        this.banks = new ArrayList<>();
    }

    @Override
    public String addBank(String type, String name) {
        if (!(type.equals("CentralBank") || type.equals("BranchBank"))) {
            throw new NullPointerException(ExceptionMessages.INVALID_BANK_TYPE);
        }

        Bank bank;
        switch (type) {
            case "CentralBank":
                bank = new CentralBank(name);
                banks.add(bank);
                break;
            case "BranchBank":
                bank = new BranchBank(name);
                banks.add(bank);
                break;
        }

        return String.format(ConstantMessages.SUCCESSFULLY_ADDED_BANK_OR_LOAN_TYPE, type);
    }

    @Override
    public String addLoan(String type) {
        if (!(type.equals("StudentLoan") || type.equals("MortgageLoan"))) {
            throw new IllegalArgumentException(ExceptionMessages.INVALID_LOAN_TYPE);
        }

        Loan loan;
        switch (type) {
            case "StudentLoan":
                loan = new StudentLoan();
                loans.addLoan(loan);
                break;
            case "MortgageLoan":
                loan = new MortgageLoan();
                loans.addLoan(loan);
                break;
        }

        return String.format(ConstantMessages.SUCCESSFULLY_ADDED_BANK_OR_LOAN_TYPE, type);
    }

    @Override
    public String returnedLoan(String bankName, String loanType) {
        Bank bank = getBankByName(bankName);
        Loan loan = loans.findFirst(loanType);

        if (loan == null) {
            throw new IllegalArgumentException(String.format(ExceptionMessages.NO_LOAN_FOUND, loanType));
        }

        bank.addLoan(loan);
        loans.removeLoan(loan);

        return String.format(ConstantMessages.SUCCESSFULLY_ADDED_CLIENT_OR_LOAN_TO_BANK, loanType, bankName);
    }

    @Override
    public String addClient(String bankName, String clientType, String clientName, String clientID, double income) {
        if (!(clientType.equals("Student") || clientType.equals("Adult"))) {
            throw new IllegalArgumentException(ExceptionMessages.INVALID_CLIENT_TYPE);
        }

        Bank bank = getBankByName(bankName);
        Client client;

        switch (clientType) {
            case "Student":
                client = new Student(clientName, clientID, income);
                break;
            case "Adult":
                client = new Adult(clientName, clientID, income);
                break;
            default:
                throw new IllegalArgumentException(ExceptionMessages.INVALID_CLIENT_TYPE);
        }

        String bankType = bank.getClass().getSimpleName();
        if (bankType.equals("CentralBank") && clientType.equals("Adult")) {
            bank.addClient(client);
            return String.format(ConstantMessages.SUCCESSFULLY_ADDED_CLIENT_OR_LOAN_TO_BANK, clientType, bankName);
        } else if (bankType.equals("BranchBank") && clientType.equals("Student")) {
            bank.addClient(client);
            return String.format(ConstantMessages.SUCCESSFULLY_ADDED_CLIENT_OR_LOAN_TO_BANK, clientType, bankName);
        } else {
            return ConstantMessages.UNSUITABLE_BANK;
        }
    }

    @Override
    public String finalCalculation(String bankName) {
        Bank bank = getBankByName(bankName);

        double clientIncome = bank.getClients().stream()
                .mapToDouble(Client::getIncome)
                .sum();

        double loanAmount = bank.getLoans().stream()
                .mapToDouble(Loan::getAmount)
                .sum();

        double funds = clientIncome + loanAmount;

        return String.format(ConstantMessages.FUNDS_BANK, bankName, funds);
    }

    @Override
    public String getStatistics() {
        StringBuilder sb = new StringBuilder();

        for (Bank bank : banks) {
            sb.append(bank.getStatistics());
            sb.append(System.lineSeparator());
        }

        return sb.toString().trim();
    }

    private Bank getBankByName(String name) {
        return banks.stream()
                .filter(bank -> bank.getName().equals(name))
                .findFirst()
                .orElse(null);
    }
}
