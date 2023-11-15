package com.notsecurebank.servlet;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class AccountViewServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private static final Logger LOG = LogManager.getLogger(AccountViewServlet.class);

    public AccountViewServlet() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LOG.info("doGet");

        // show account balance for a particular account
        if (request.getRequestURL().toString().endsWith("showAccount")) {
            String accountName = request.getParameter("listAccounts");
            if (accountName == null) {
                response.sendRedirect(request.getContextPath() + "/bank/main.jsp");
                return;
            }
            LOG.info("Balance for accountName = '" + accountName + "'.");
            RequestDispatcher dispatcher = request.getRequestDispatcher("/bank/balance.jsp?acctId=" + accountName);
            dispatcher.forward(request, response);
            return;
        }
        // this shouldn't happen
        else if (request.getRequestURL().toString().endsWith("showTransactions"))
            doPost(request, response);
        else
            super.doGet(request, response);
    }

    private String sanitize(String input) {
        return input.replaceAll("[\n\r\t]", "");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LOG.info("doPost");

        // show transactions within the specified date range (if any)
        if (request.getRequestURL().toString().endsWith("showTransactions")) {
            String startTime = request.getParameter("startDate");
            String endTime = request.getParameter("endDate");

            // Handle nulls and sanitize
            String sanitizedStartTime = startTime != null ? sanitize(startTime) : "N/A";
            String sanitizedEndTime = endTime != null ? sanitize(endTime) : "N/A";

            // URL encode
            String encodedStartTime = URLEncoder.encode(sanitizedStartTime, StandardCharsets.UTF_8);
            String encodedEndTime = URLEncoder.encode(sanitizedEndTime, StandardCharsets.UTF_8);

            LOG.info("Transactions within " + sanitizedStartTime + " and " + sanitizedEndTime + ".");
            RequestDispatcher dispatcher = request.getRequestDispatcher("/bank/transaction.jsp?startTime=" + encodedStartTime + "&endTime=" + encodedEndTime);
            dispatcher.forward(request, response);
        }
    }
}
