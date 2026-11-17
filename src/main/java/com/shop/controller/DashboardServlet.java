package com.shop.controller;

import com.shop.dao.BillDAO;
import com.shop.dao.BookDAO;
import com.shop.model.Bill;
import com.shop.model.Book;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet {
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        try {
            List<Book> books = new BookDAO().getAll();
            List<Bill> bills = new BillDAO().getAll();
            double sales = 0;
            for (Bill b : bills) sales += b.getTotal();
            int low = 0;
            for (Book b : books) if (b.getStock() < 5) low++;

            req.setAttribute("bookCount", books.size());
            req.setAttribute("billCount", bills.size());
            req.setAttribute("totalSales", sales);
            req.setAttribute("lowStock", low);
            req.getRequestDispatcher("dashboard.jsp").forward(req, res);
        } catch (SQLException e) { throw new ServletException(e); }
    }
}