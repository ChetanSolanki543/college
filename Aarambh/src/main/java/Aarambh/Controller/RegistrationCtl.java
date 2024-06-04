package Aarambh.Controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Aarambh.Bean.Bean;
import Aarambh.Util.DataUtility;
import Aarambh.Util.ServletUtility;
import Aarambh.model.Model;

@WebServlet(name = "RegistrationCtl", urlPatterns = { "/RegistrationCtl" })
public class RegistrationCtl extends BaseCtl {
	private static final long serialVersionUID = 1L;
	public static final String OP_SIGN_UP = "SignUp";
       
   	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

   		ServletUtility.forward(getView(), request, response);
   		
   	}
   	
   	
	protected Bean populateBeans(HttpServletRequest request) {

		System.out.println("UserRegistrationCtl Method populatebean Started");
		Bean bean = new Bean();

//		bean.setRoleId(RoleBean.STUDENT);

		bean.setId(DataUtility.getLong(request.getParameter("id")));
		bean.setFullName(DataUtility.getString(request.getParameter("fullName")));
		bean.setCourse(DataUtility.getString(request.getParameter("course")));
		bean.setLogin(DataUtility.getString(request.getParameter("login")));
		bean.setPass(DataUtility.getString(request.getParameter("pass")));
		bean.setConfirmPass(DataUtility.getString(request.getParameter("confirmPass")));
		
		populateDTO(bean, request);
		return bean;
		}
	
   	private void populateDTO(Bean bean, HttpServletRequest request) {
		// TODO Auto-generated method stub
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String op = DataUtility.getString(request.getParameter("operation"));

		//get model
		Model model = new Model();
		//long id = DataUtility.getLong(request.getParameter("id"));

		if (OP_SIGN_UP.equalsIgnoreCase(op)) {
			Bean bean = (Bean) populateBeans(request);
			try {
				System.out.println("UserRegistrationCtl Method doPost Operation SignUp Mila");
				long pk =  model.registerUsers(bean);

			//	bean.setId(pk);
				// request.getSession().setAttribute("UserBean", bean);
				ServletUtility.setSuccessMessage("User Successfully Register", request);
				ServletUtility.forward(getView(), request, response);
				return;
			} catch (Exception e) {
				e.printStackTrace();
				ServletUtility.handleException(e, request, response);
				return;
			}
		} else if (OP_RESET.equalsIgnoreCase(op)) {
			System.out.println("UserRegistrationCtl Method doPost Operation Reset Mila Redirect UserRegCtl");
			ServletUtility.redirect(ORSView.USER_REGISTRATION_CTL, request, response);
		}

	}


	@Override
	protected String getView() {
		// TODO Auto-generated method stub
		return null;
	}

}
