package com.example.donationsystem.service;

import com.example.donationsystem.model.Donation;
import com.example.donationsystem.model.Donor;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DonationService {
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/donation_system";
    private static final String USER = "postgres";
    private static final String PASS = "password";

    public void addDonor(Donor donor) throws SQLException {
        String sql = "INSERT INTO donors (name, email) VALUES (?, ?) RETURNING id";
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, donor.getName());
            stmt.setString(2, donor.getEmail());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                donor.setId(rs.getLong("id"));
            }
        }
    }

    public void addDonation(Donation donation) throws SQLException {
        if (donation.getType() == null || donation.getType().isEmpty()) {
            throw new IllegalArgumentException("Tipo de doação não pode ser vazio");
        }
        if (donation.getQuantity() < 0 || donation.getValue() < 0) {
            throw new IllegalArgumentException("Quantidade ou valor não podem ser negativos");
        }
        String sql = "INSERT INTO donations (type, quantity, value, donor_id, donation_date) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, donation.getType());
            stmt.setInt(2, donation.getQuantity());
            stmt.setDouble(3, donation.getValue());
            stmt.setLong(4, donation.getDonorId());
            stmt.setDate(5, Date.valueOf(donation.getDonationDate()));
            stmt.executeUpdate();
        }
    }

    public List<Donation> getAllDonations() throws SQLException {
        List<Donation> donations = new ArrayList<>();
        String sql = "SELECT * FROM donations";
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Donation donation = new Donation();
                donation.setId(rs.getLong("id"));
                donation.setType(rs.getString("type"));
                donation.setQuantity(rs.getInt("quantity"));
                donation.setValue(rs.getDouble("value"));
                donation.setDonorId(rs.getLong("donor_id"));
                donation.setDonationDate(rs.getDate("donation_date").toLocalDate());
                donations.add(donation);
            }
        }
        return donations;
    }

    public List<Donation> getLowStockDonations(String type, int minQuantity) throws SQLException {
        List<Donation> lowStock = new ArrayList<>();
        String sql = "SELECT type, SUM(quantity) as total FROM donations WHERE type = ? GROUP BY type HAVING SUM(quantity) < ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, type);
            stmt.setInt(2, minQuantity);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Donation donation = new Donation();
                donation.setType(rs.getString("type"));
                donation.setQuantity(rs.getInt("total"));
                lowStock.add(donation);
            }
        }
        return lowStock;
    }

    public void sendDonorReminder(Long donorId) throws SQLException {
        String sql = "SELECT name, email FROM donors WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, donorId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                System.out.println("Lembrete enviado para " + rs.getString("name") + " (" + rs.getString("email") + "): Continue doando!");
            }
        }
    }
}