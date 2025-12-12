package bank.entities.bank;

import bank.common.ExceptionMessages;
import bank.entities.client.Client;
import bank.entities.loan.Loan;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

public abstract class BaseBank implements Bank {
    private String name;
    private int capacity;
    private Collection<Loan> loans;
    private Collection<Client> clients;

    protected BaseBank(String name, int capacity) {
        this.setName(name);
        this.capacity = capacity;
        this.loans = new ArrayList<>();
        this.clients = new ArrayList<>();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException(ExceptionMessages.BANK_NAME_CANNOT_BE_NULL_OR_EMPTY);
        }
        this.name = name;
    }

    @Override
    public Collection<Client> getClients() {
        return clients;
    }

    @Override
    public Collection<Loan> getLoans() {
        return loans;
    }

    @Override
    public int sumOfInterestRates() {
        return loans.stream()
                .mapToInt(Loan::getInterestRate)
                .sum();
    }

    @Override
    public void addClient(Client client) {
        if (clients.size() >= capacity) {
            throw new IllegalStateException(ExceptionMessages.NOT_ENOUGH_CAPACITY_FOR_CLIENT);
        }
        clients.add(client);
    }

    @Override
    public void removeClient(Client client) {
        clients.remove(client);
    }

    @Override
    public void addLoan(Loan loan) {
        loans.add(loan);
    }

    @Override
    public String getStatistics() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Name: %s, Type: %s", name, this.getClass().getSimpleName()));
        sb.append(System.lineSeparator());
        
        if (clients.isEmpty()) {
            sb.append("Clients: none");
        } else {
            String clientNames = clients.stream()
                    .map(Client::getName)
                    .collect(Collectors.joining(", "));
            sb.append("Clients: ").append(clientNames);
        }
        sb.append(System.lineSeparator());
        
        sb.append(String.format("Loans: %d, Sum of interest rates: %d", loans.size(), sumOfInterestRates()));
        
        return sb.toString();
    }
}

