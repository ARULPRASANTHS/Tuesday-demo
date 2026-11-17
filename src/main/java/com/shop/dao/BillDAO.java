package com.shop.dao;

import com.shop.model.Bill;
import com.shop.util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

//import com.shop.util.Bill;
import com.shop.util.DBConnection;

public class BillDAO {
    private static final String SELECT =
        "SELECT b.id, b.customer_name, b.book_id, k.title, b.quantity, b.total, b.bill_date " +
        "FROM bills b JOIN books k ON b.book_id = k.id ";

    public List<Bill> getAll() throws SQLException {
        List<Bill> list = new ArrayList<>();
        try (Connection c = DBConnection.get(); Statement s = c.createStatement();
             ResultSet rs = s.executeQuery(SELECT + "ORDER BY b.id DESC")) {
            while (rs.next()) list.add(map(rs));
        }
        return list;
    }

    public Bill getById(int id) throws SQLException {
        try (Connection c = DBConnection.get();
             PreparedStatement ps = c.prepareStatement(SELECT + "WHERE b.id=?")) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            return rs.next() ? map(rs) : null;
        }
    }

    public void add(Bill b) throws SQLException {
        try (Connection c = DBConnection.get();
             PreparedStatement ps = c.prepareStatement(
                 "INSERT INTO bills(customer_name,book_id,quantity,total) VALUES(?,?,?,?)")) {
            ps.setString(1, b.getCustomerName()); ps.setInt(2, b.getBookId());
            ps.setInt(3, b.getQuantity()); ps.setDouble(4, b.getTotal());
            ps.executeUpdate();
        }
    }

    public void update(Bill b) throws SQLException {
        try (Connection c = DBConnection.get();
             PreparedStatement ps = c.prepareStatement(
                 "UPDATE bills SET customer_name=?,book_id=?,quantity=?,total=? WHERE id=?")) {
            ps.setString(1, b.getCustomerName()); ps.setInt(2, b.getBookId());
            ps.setInt(3, b.getQuantity()); ps.setDouble(4, b.getTotal());
            ps.setInt(5, b.getId());
            ps.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        try (Connection c = DBConnection.get();
             PreparedStatement ps = c.prepareStatement("DELETE FROM bills WHERE id=?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    private Bill map(ResultSet rs) throws SQLException {
        Bill b = new Bill();
        b.setId(rs.getInt("id"));
        b.setCustomerName(rs.getString("customer_name"));
        b.setBookId(rs.getInt("book_id"));
        b.setBookTitle(rs.getString("title"));
        b.setQuantity(rs.getInt("quantity"));
        b.setTotal(rs.getDouble("total"));
        b.setBillDate(rs.getTimestamp("bill_date"));
        return b;
    }
}