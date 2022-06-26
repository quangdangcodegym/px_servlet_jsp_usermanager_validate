package com.phuxuan.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.phuxuan.dao.CountryDAO;
import com.phuxuan.dao.ICountryDAO;
import com.phuxuan.model.Country;
import com.phuxuan.model.User;



@WebServlet(name = "CountryServlet", urlPatterns = "/country")
public class CountryServlet extends HttpServlet{
	
	private static final long serialVersionUID = 1L;
    private ICountryDAO iCountryDAO;


	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String action = request.getParameter("action");
        if (action == null) {
            action = "";
        }
        
        try {
            switch (action) {
                case "create":
                    showNewForm(request, response);
                    break;
                case "edit":
                    showEditForm(request, response);
                    break;
                case "delete":
                    deleteCountry(request, response);
                    break;
                default:
                    listCountry(request, response);
                    break;
            }
        } catch (Exception ex) {
            throw new ServletException(ex);
        }
	}

	

	private void listCountry(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		List<Country> listCountry = iCountryDAO.selectAllCountry();
        request.setAttribute("listCountry", listCountry);
        
        
        RequestDispatcher dispatcher = request.getRequestDispatcher("/country/list.jsp");
        dispatcher.forward(request, response);
		
	}



	private void deleteCountry(HttpServletRequest request, HttpServletResponse response) throws SQLException, ServletException, IOException {
		int id = Integer.parseInt(request.getParameter("id"));
		
        iCountryDAO.deleteCountry(id);

        List<Country> listCountry = iCountryDAO.selectAllCountry();
        request.setAttribute("listCountry", listCountry);
        
        RequestDispatcher dispatcher = request.getRequestDispatcher("/country/list.jsp");
        dispatcher.forward(request, response);
		
	}



	private void showEditForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int id = Integer.parseInt(request.getParameter("id"));
        Country existingCountry = iCountryDAO.selectCountry(id);
        
        RequestDispatcher dispatcher = request.getRequestDispatcher("/country/edit.jsp");
        request.setAttribute("country", existingCountry);
        
        dispatcher.forward(request, response);
		
	}



	private void showNewForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		RequestDispatcher dispatcher = request.getRequestDispatcher("/country/create.jsp");
        dispatcher.forward(request, response);
		
	}



	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String action = request.getParameter("action");
        if (action == null) {
            action = "";
        }
        try {
            switch (action) {
                case "create":
                    insertCountry(request, response);
                    break;
                case "edit":
                    updateCountry(request, response);
                    break;
            }
        } catch (Exception ex) {
            throw new ServletException(ex);
        }
	}

	



	private void updateCountry(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
		int id = Integer.parseInt(request.getParameter("id"));
		
		
        String name = request.getParameter("name");

        Country country = new Country(id, name);
        iCountryDAO.updateCountry(country);
		/*
		 * RequestDispatcher dispatcher = request.getRequestDispatcher("user/edit.jsp");
		 * dispatcher.forward(request, response);
		 */
        response.sendRedirect("/country");
		
	}



	private void insertCountry(HttpServletRequest request, HttpServletResponse response) throws SQLException, ServletException, IOException {
		String name = request.getParameter("name");
        
		Country country = new Country();
		country.setName(name);
		
        iCountryDAO.insertCountry(country);
        
        RequestDispatcher dispatcher = request.getRequestDispatcher("/country/create.jsp");
        dispatcher.forward(request, response);
		
	}



	@Override
	public void init() throws ServletException {
		iCountryDAO = new CountryDAO();
	}
	
	

}
