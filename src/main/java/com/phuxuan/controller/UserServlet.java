package com.phuxuan.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import com.phuxuan.dao.CountryDAO;
import com.phuxuan.dao.ICountryDAO;
import com.phuxuan.dao.UserDAO;
import com.phuxuan.model.Country;
import com.phuxuan.model.User;

@WebServlet(name = "UserServlet", urlPatterns = "/users")
public class UserServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private UserDAO userDAO;
    private ICountryDAO iCountryDAO;
	private String errors = "";
    
    public void init() {
        userDAO = new UserDAO();
        iCountryDAO = new CountryDAO();
        
        if(this.getServletContext().getAttribute("listCountry")==null) {
        	this.getServletContext().setAttribute("listCountry", iCountryDAO.selectAllCountry());
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) {
            action = "";
        }
        try {
            switch (action) {
                case "create":
                    insertUser(request, response);
                    break;
                case "edit":
                    updateUser(request, response);
                    break;
            }
        } catch (SQLException ex) {
            throw new ServletException(ex);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
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
                    deleteUser(request, response);
                    break;
                default:
                    listUser(request, response);
                    break;
            }
        } catch (SQLException ex) {
            throw new ServletException(ex);
        }
    }

    private void listUser(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {
        List<User> listUser = userDAO.selectAllUsers();
        request.setAttribute("listUser", listUser);
        RequestDispatcher dispatcher = request.getRequestDispatcher("user/list.jsp");
        dispatcher.forward(request, response);
    }

    private void showNewForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
      
		/*
		 * List<Country> listCountry = countryDAO.getAllCountry();
		 * request.setAttribute("listCountry", listCountry);
		 */
    	
    	User user  = new User();
    	request.setAttribute("user", user);
    	
        RequestDispatcher dispatcher = request.getRequestDispatcher("user/create.jsp");
        dispatcher.forward(request, response);
    	
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        User existingUser = userDAO.selectUser(id);
        
        
        RequestDispatcher dispatcher = request.getRequestDispatcher("user/edit.jsp");
        request.setAttribute("user", existingUser);
        dispatcher.forward(request, response);

    }

    private void insertUser(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {
		/*
		 * String name = request.getParameter("name"); String email =
		 * request.getParameter("email"); int idcountry =
		 * Integer.parseInt(request.getParameter("idcountry"));
		 * 
		 * 
		 * User newUser = new User(name, email, idcountry); userDAO.insertUser(newUser);
		 * RequestDispatcher dispatcher =
		 * request.getRequestDispatcher("user/create.jsp"); dispatcher.forward(request,
		 * response);
		 */
    	// Ctrl + Shift + / Cach comment code trong eclipse
    	
    	User user = new User();
		boolean flag = true;
		Map<String, String> hashMap = new HashMap<String, String>();  // Luu lai truong nao bi loi va loi gi
		
		
		
		System.out.println(this.getClass() + " insertUser with validate");
		try {
			user.setId(Integer.parseInt(request.getParameter("id")));
			
			String email = request.getParameter("email");
			user.setEmail(email);
			user.setName(request.getParameter("name"));
			user.setPassword(request.getParameter("password"));
			
			System.out.println(this.getClass() + "Country value from request: " + request.getParameter("country") );
			int idCountry = Integer.parseInt(request.getParameter("country"));
			user.setIdcountry(idCountry);
			
			System.out.println(this.getClass() + "User info from request: " + user);
			
			ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
			Validator validator = validatorFactory.getValidator();
			
			Set<ConstraintViolation<User>> constraintViolations = validator.validate(user);
			
			System.out.println("User: " + user);
			
			if (!constraintViolations.isEmpty()) {
				
				errors  = "<ul>";
				// constraintViolations is has error
				for (ConstraintViolation<User> constraintViolation : constraintViolations) {
					errors += "<li>" + constraintViolation.getPropertyPath() + " " + constraintViolation.getMessage()
							+ "</li>";
				}
				errors += "</ul>";
				
				
				request.setAttribute("user", user);
				request.setAttribute("errors", errors);
				
				List<Country> listCountry = iCountryDAO.selectAllCountry();
		    	request.setAttribute("listCountry", listCountry);
		    	
		    	System.out.println(this.getClass() + " !constraintViolations.isEmpty()");
				request.getRequestDispatcher("/user/create.jsp").forward(request, response);
			}else{
				if(userDAO.selectUserByEmail(email)!=null) {
					flag = false;
					hashMap.put("email", "Email exits in database");
					System.out.println(this.getClass() + " Email exits in database");
					
				}
				if(iCountryDAO.selectCountry(idCountry)==null) {
					flag = false;
					hashMap.put("country", "Country value invalid");
					System.out.println(this.getClass() + " Country invalid");
				}
				
				if(flag) {
					// Create user susscess
					userDAO.insertUser(user);
					
					
					User u = new User();
					request.setAttribute("user", u);
					
					request.getRequestDispatcher("user/create.jsp").forward(request, response);
				}else {
					// Error : Email exits in database
					// Error: Country invalid
					errors = "<ul>";
					// One more field has error
					hashMap.forEach(new BiConsumer<String, String>() {
						@Override
						public void accept(String keyError, String valueError) {
							errors += "<li>"  + valueError
										+ "</li>";
							
						}
					});
					errors +="</ul>";
					
					request.setAttribute("user", user);
					request.setAttribute("errors", errors);
					
			    	
			    	System.out.println(this.getClass() + " !constraintViolations.isEmpty()");
					request.getRequestDispatcher("/user/create.jsp").forward(request, response);
				}
				
				
			}
		}
		catch (NumberFormatException ex) {
			System.out.println(this.getClass() + " NumberFormatException: User info from request: " + user);
			errors = "<ul>";
					errors += "<li>" + "Input format not right"
								+ "</li>";
					
			errors += "</ul>";
			
			
			request.setAttribute("user", user);
			request.setAttribute("errors", errors);
			
			request.getRequestDispatcher("/user/create.jsp").forward(request, response);
		}
		catch(Exception ex){
			
		}
		
    }

    private void updateUser(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {
        int id = Integer.parseInt(request.getParameter("id"));
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        int idcountry = Integer.parseInt(request.getParameter("idcountry"));

        User book = new User(id, name, email, idcountry);
        userDAO.updateUser(book);
		/*
		 * RequestDispatcher dispatcher = request.getRequestDispatcher("user/edit.jsp");
		 * dispatcher.forward(request, response);
		 */
        response.sendRedirect("/users");
    }

    private void deleteUser(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {
        int id = Integer.parseInt(request.getParameter("id"));
        userDAO.deleteUser(id);

        List<User> listUser = userDAO.selectAllUsers();
        request.setAttribute("listUser", listUser);
        RequestDispatcher dispatcher = request.getRequestDispatcher("user/list.jsp");
        dispatcher.forward(request, response);
    }
}
