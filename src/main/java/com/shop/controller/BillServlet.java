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

@WebServlet("/bills")
public class BillServlet extends HttpServlet {
    private BillDAO dao = new BillDAO();
    private BookDAO bookDao = new BookDAO();

    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String action = req.getParameter("action");
        if (action == null) action = "list";
        try {
            switch (action) {
                case "new":
                    req.setAttribute("books", bookDao.getAll());
                    req.getRequestDispatcher("bill-form.jsp").forward(req, res);
                    break;
                case "edit":
                    req.setAttribute("books", bookDao.getAll());
                    req.setAttribute("bill", dao.getById(Integer.parseInt(req.getParameter("id"))));
                    req.getRequestDispatcher("bill-form.jsp").forward(req, res);
                    break;
                case "delete":
                    dao.delete(Integer.parseInt(req.getParameter("id")));
                    res.sendRedirect("bills");
                    break;
                default:
                    req.setAttribute("bills", dao.getAll());
                    req.getRequestDispatcher("bill-list.jsp").forward(req, res);
            }
        } catch (SQLException e) { throw new ServletException(e); }
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        try {
            int bookId = Integer.parseInt(req.getParameter("bookId"));
            int qty = Integer.parseInt(req.getParameter("quantity"));
            Book book = bookDao.getById(bookId);

            Bill b = new Bill();
            b.setCustomerName(req.getParameter("customerName"));
            b.setBookId(bookId);
            b.setQuantity(qty);
            b.setTotal(book.getPrice() * qty);

            String id = req.getParameter("id");
            if (id == null || id.isEmpty()) dao.add(b);
            else { b.setId(Integer.parseInt(id)); dao.update(b); }
        } catch (SQLException e) { throw new ServletException(e); }
        res.sendRedirect("bills");
    }
}