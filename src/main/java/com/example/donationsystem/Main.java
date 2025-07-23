package com.example.donationsystem;

import com.example.donationsystem.model.Donation;
import com.example.donationsystem.model.Donor;
import com.example.donationsystem.service.DonationService;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        DonationService service = new DonationService();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\nSistema de Gestão de Doações para ONGs");
            System.out.println("1. Cadastrar doador");
            System.out.println("2. Cadastrar doação");
            System.out.println("3. Listar doações");
            System.out.println("4. Verificar estoque baixo");
            System.out.println("5. Enviar lembrete a doador");
            System.out.println("6. Sair");
            System.out.print("Escolha uma opção: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Limpar buffer

            try {
                switch (choice) {
                    case 1:
                        System.out.print("Nome do doador: ");
                        String donorName = scanner.nextLine();
                        System.out.print("Email do doador: ");
                        String donorEmail = scanner.nextLine();
                        Donor donor = new Donor(donorName, donorEmail);
                        service.addDonor(donor);
                        System.out.println("Doador cadastrado com ID: " + donor.getId());
                        break;
                    case 2:
                        System.out.print("Tipo de doação (dinheiro/alimento/roupa): ");
                        String type = scanner.nextLine();
                        System.out.print("Quantidade: ");
                        int quantity = scanner.nextInt();
                        System.out.print("Valor (se dinheiro, senão 0): ");
                        double value = scanner.nextDouble();
                        System.out.print("ID do doador: ");
                        long donorId = scanner.nextLong();
                        Donation donation = new Donation(type, quantity, value, donorId, LocalDate.now());
                        service.addDonation(donation);
                        System.out.println("Doação cadastrada com sucesso!");
                        break;
                    case 3:
                        System.out.println("Doações cadastradas:");
                        for (Donation d : service.getAllDonations()) {
                            System.out.println(d.getType() + " - Quantidade: " + d.getQuantity() + ", Valor: " + d.getValue() + ", Doador ID: " + d.getDonorId() + ", Data: " + d.getDonationDate());
                        }
                        break;
                    case 4:
                        System.out.print("Tipo de doação para verificar estoque baixo: ");
                        String lowStockType = scanner.nextLine();
                        System.out.print("Quantidade mínima: ");
                        int minQuantity = scanner.nextInt();
                        for (Donation d : service.getLowStockDonations(lowStockType, minQuantity)) {
                            System.out.println("Estoque baixo: " + d.getType() + " com " + d.getQuantity() + " unidades");
                        }
                        break;
                    case 5:
                        System.out.print("ID do doador para lembrete: ");
                        long reminderDonorId = scanner.nextLong();
                        service.sendDonorReminder(reminderDonorId);
                        break;
                    case 6:
                        System.out.println("Saindo...");
                        scanner.close();
                        return;
                    default:
                        System.out.println("Opção inválida!");
                }
            } catch (SQLException e) {
                System.out.println("Erro no banco de dados: " + e.getMessage());
            } catch (IllegalArgumentException e) {
                System.out.println("Erro: " + e.getMessage());
            }
        }
    }
}