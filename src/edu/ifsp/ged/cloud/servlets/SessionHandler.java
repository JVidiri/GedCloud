package edu.ifsp.ged.cloud.servlets;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

import edu.ifsp.ged.commons.facade.DBFacade;
import edu.ifsp.ged.commons.models.StationLogin;
import edu.ifsp.ged.commons.models.UserLogin;
import edu.ifsp.ged.commons.utils.hash.HashHandler;

/**
 * Servlet implementation class SessionHandler
 */
@WebServlet("/SessionHandler")
public class SessionHandler extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public SessionHandler() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//getting the json message of sender
		String message = request.getParameter("m");
		System.out.println("m = "+message);
		DBFacade db = new DBFacade();
		try {
			db.createConnection();		
			JSONObject jsonMessage = new JSONObject(message);
			//getting the session type to create
			String accessType = jsonMessage.getString("type");
			if (accessType.equals("adminConsole")){
				//get the login information
				StationLogin receivedLogin = new StationLogin(jsonMessage.getString("CNPJ"),jsonMessage.getString("stationPassword"));
				//verify through the server database		
				StationLogin bdLogin = db.getStationLoginByCNPJ(receivedLogin.getCNPJ());
				if (bdLogin != null){					
					if (receivedLogin.getStationPassword().equals(bdLogin.getStationPassword())){
						//generate a hash passphrase
						StringBuilder hashPhrase = new StringBuilder();
						hashPhrase.append(bdLogin.getCNPJ());
						hashPhrase.append(bdLogin.getStationPassword());
						//generate new hash						
						String saltedHash = HashHandler.createHash(hashPhrase.toString());						
						//save hash in active our session table
						db.addActiveSessionRegist(saltedHash);
						//TODO save hash into the user session
						response.sendRedirect("/GedAdminConsole/index2.jsp");
					}else{
						//send to login credentials error page
						response.sendRedirect("/GedAdminConsole/login.jsp?erro=login");
					}
				}else{
					//send to login credentials error page
					response.sendRedirect("/GedAdminConsole/login.jsp?erro=login");
				}
			}else if (accessType.equals("mobile")){
				//get the login information
				UserLogin receivedLogin = new UserLogin(jsonMessage.getString("userName"),jsonMessage.getString("userPassword"));
				//verify through the server databases				
				UserLogin bdLogin = db.getUserLoginByUserName(receivedLogin.getUserName());
				if (bdLogin != null){
					if (HashHandler.validatePassword(receivedLogin.getUserPassword() ,bdLogin.getUserPassword())){
						//generate a hash passphrase
						StringBuilder hashPhrase = new StringBuilder();
						hashPhrase.append(bdLogin.getUserName());
						hashPhrase.append(bdLogin.getUserPassword());
						//generate new hash						
						String saltedHash = HashHandler.createHash(hashPhrase.toString());						
						//save hash in active our session table
						db.addActiveSessionRegist(saltedHash);				
						//write the new session hash into the return page. 
						response.getWriter();								
					}else{
						//send to login credentials error page
						response.sendRedirect("/GedAdminConsole/login.jsp?erro=login");
					}
				}else{
					//send to login credentials error page
					response.sendRedirect("/GedAdminConsole/login.jsp?erro=login");
				}
			}else{
				//can implement more clients in the future.
				response.sendError(HttpServletResponse.SC_BAD_REQUEST);
			}			
		} catch (JSONException e) {
			e.printStackTrace();
			//500
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		} catch (SQLException e) {
			e.printStackTrace();
			//500
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			//500
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
			//500
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
			e.printStackTrace();
			//500
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}finally{
			try {
				db.closeConnection();
			} catch (Exception e) {
				//500
				e.printStackTrace();
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			}
		}		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);		
	}
}
