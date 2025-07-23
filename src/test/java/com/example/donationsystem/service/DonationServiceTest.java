package com.example.donationsystem.service;

import com.example.donationsystem.model.Donation;
import com.example.donationsystem.model.Donor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.*;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class DonationServiceTest {
    private DonationService service;

    @BeforeEach
    public void setUp() {
        service = new DonationService();
        // Limpar o banco para testes
        try (Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/donation_system", "postgres", "password");
             Statement stmt = conn.createStatement()) {
            stmt.execute("TRUNCATE TABLE donations, donors RESTART IDENTITY");
        } catch (SQLException e) {
            System.err.println("Erro ao limpar banco: " + e.getMessage());
        }
    }

    @Test
    public void testAddDonor() throws SQLException {
        Donor donor = new Donor("Maria", "maria@example.com");
        service.addDonor(donor);
        assertNotNull(donor.getId());
        assertEquals("Maria", donor.getName());
    }

    @Test
    public void testAddDonation() throws SQLException {
        Donor donor = new Donor("João", "joao@example.com");
        service.addDonor(donor);
        Donation donation = new Donation("alimento", 100, 0.0, donor.getId(), LocalDate.now());
        service.addDonation(donation);
        assertEquals(1, service.getAllDonations().size());
    }

    @Test
    public void testAddDonationInvalidType() {
        Donor donor = new Donor("João", "joao@example.com");
        Donation donation = new Donation("", 100, 0.0, 1L, LocalDate.now());
        assertThrows(IllegalArgumentException.class, () -> service.addDonation(donation));
    }

    @Test
    public void testGetLowStockDonations() throws SQLException {
        Donor donor = new Donor("João", "joao@example.com");
        service.addDonor(donor);
        Donation donation = new Donation("roupa", 5, 0.0, donor.getId(), LocalDate.now());
        service.addDonation(donation);
        assertEquals(1, service.getLowStockDonations("roupa", 10).size());
    }
}