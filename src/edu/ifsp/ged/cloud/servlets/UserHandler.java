package edu.ifsp.ged.cloud.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;

import edu.ifsp.ged.commons.facade.DBFacade;
import edu.ifsp.ged.commons.models.UserModel;

/**
 * Servlet implementation class UserHandler
 */
@WebServlet("/UserHandler")
public class UserHandler extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UserHandler() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//getting message		
		try {
			//getting the message from servlet parameters
			String message = request.getParameter("m");			
			//user object
			UserModel newUser = new UserModel();
			//Dserializando o json e armagenando no objeto
			newUser.deserializeFromJson(message);
			//database facade
			DBFacade db = new DBFacade();		
			//creating the connection and registering the new user
			db.createConnection();
			//registrar usuário
			db.userRegist(newUser);
			//fechar a conexão com o banco
			db.closeConnection();			
		} catch (JSONException e) {
			// TODO return a error to browser
			e.printStackTrace();
		} catch (Exception e) {
			// TODO return a error to browser
			e.printStackTrace();
		}		
	}

}
