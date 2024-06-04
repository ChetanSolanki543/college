package Aarambh.model;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Aarambh.Bean.Bean;
import Aarambh.Util.EmailBuilder;
import Aarambh.Util.EmailMessage;
import Aarambh.Util.EmailUtility;
import Aarambh.Util.JDBCDataSource;


public class Model {

		public int nextPK() throws Exception {


			String sql = "SELECT MAX(ID) FROM TECH";
			Connection conn = null;
			int pk = 0;
			try {
				conn = JDBCDataSource.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql);
				ResultSet rs = pstmt.executeQuery();
				while (rs.next()) {
					pk = rs.getInt(1);
				}
				rs.close();
			} catch (Exception e) {

				throw new Exception("Exception : Exception in getting PK");
			} finally {
				JDBCDataSource.closeConnection(conn);
			}
			return pk + 1;

		}

		 public long add(Bean bean) throws Exception {

			String sql = "INSERT INTO TECH VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

			Connection conn = null;
			int pk = 0;

			Bean existbean = findByLogin(bean.getLogin());                               
			if (existbean != null) {
				throw new Exception("login Id already exists");

			}

			try {
				conn = JDBCDataSource.getConnection();
				pk = nextPK();

				conn.setAutoCommit(false);
				PreparedStatement pstmt = conn.prepareStatement(sql);

				pstmt.setInt(1, pk);
				pstmt.setString(2, bean.getFullName());
				pstmt.setString(3, bean.getCourse());
				pstmt.setString(4, bean.getLogin());
				pstmt.setString(5, bean.getPass());
				pstmt.setString(6, bean.getConfirmPass());
				
				// date of birth caste by sql date
				
//				pstmt.setDate(6, new Date(bean.getDob().getTime()));

//				pstmt.setString(7, bean.getMobileNo());
//				pstmt.setLong(8, bean.getRoleId());
//				pstmt.setInt(9, bean.getUnSuccessfulLogin());
//				pstmt.setString(10, bean.getGender());
//				pstmt.setTimestamp(11, bean.getLastLogin());
//				pstmt.setString(12, bean.getLock());
//				pstmt.setString(13, bean.getRegisterdIP());
//				pstmt.setString(14, bean.getLastLoginIP());
//				pstmt.setString(15, bean.getCreatedBy());
//				pstmt.setString(16, bean.getModifiedBy());
//				pstmt.setTimestamp(17, bean.getCreatedDatetime());
//				pstmt.setTimestamp(18, bean.getModifiedDatetime());

				int a = pstmt.executeUpdate();
				System.out.println(a);
				conn.commit();
				pstmt.close();

			} catch (Exception e) {
				try {
					e.printStackTrace();
					conn.rollback();

				} catch (Exception e2) {
					e2.printStackTrace();
					// application exception
					throw new Exception("Exception : add rollback exceptionn" + e2.getMessage());
				}
			}

			finally {
				JDBCDataSource.closeConnection(conn);
			}
			return pk;

		}

		public void delete(Bean bean) throws Exception {
			String sql = "DELETE FROM ST_USER WHERE ID=?";
			Connection conn = null;
			try {
				conn = JDBCDataSource.getConnection();
				conn.setAutoCommit(false);
				PreparedStatement pstmt = conn.prepareStatement(sql);
				pstmt.setLong(1, bean.getId());
				pstmt.executeUpdate();
				conn.commit();
				pstmt.close();
			} catch (Exception e) {
				try {
					conn.rollback();
				} catch (Exception e2) {
					throw new Exception("Exception: Delete rollback Exception" + e2.getMessage());
				}
			} finally {
				JDBCDataSource.closeConnection(conn);
			}
		}

		public Bean findByLogin(String login) throws Exception {
			String sql = "SELECT * FROM ST_USER WHERE login=?";
			Bean bean = null;
			Connection conn = null;
			try {
				conn = JDBCDataSource.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, login);
				ResultSet rs = pstmt.executeQuery();
				while (rs.next()) {
					bean = new Bean();
					bean.setId(rs.getLong(1));
					bean.setFullName(rs.getString(2));
					bean.setCourse(rs.getString(3));
					bean.setLogin(rs.getString(4));
					bean.setPass(rs.getString(5));
					bean.setConfirmPass(rs.getString(6));
				}
				rs.close();

			} catch (Exception e) {
				e.printStackTrace();
				throw new Exception("Exception: Exception in getting user by Login");
			} finally {
				JDBCDataSource.closeConnection(conn);
			}
			return bean;
		}

		public Bean findByPK(long pk) throws Exception {
			String sql = "SELECT * FROM ST_USER WHERE ID=?";
			Bean bean = null;
			Connection conn = null;
			try {
				conn = JDBCDataSource.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql);
				pstmt.setLong(1, pk);
				ResultSet rs = pstmt.executeQuery();
				while (rs.next()) {
					bean = new Bean();
					bean.setId(rs.getLong(1));
					bean.setFullName(rs.getString(2));
					bean.setCourse(rs.getString(3));
					bean.setLogin(rs.getString(4));
					bean.setPass(rs.getString(5));
					bean.setConfirmPass(rs.getString(6));

				}
				rs.close();
			} catch (Exception e) {
				e.printStackTrace();
				throw new Exception("Exception : Exception in getting User by pk");
			} finally {
				JDBCDataSource.closeConnection(conn);
			}
			return bean;
		}

		public void update(Bean bean) throws Exception {
			String sql = "UPDATE ST_USER SET FIRST_NAME=?,LAST_NAME=?,LOGIN=?,PASSWORD=?,DOB=?,	CONFIRMPASS=?  WHERE ID=?";
			Connection conn = null;
			Bean existBean = findByLogin(bean.getLogin());
			if (existBean != null && !(existBean.getId() == bean.getId())) {
				throw new Exception("LoginId is Already Exist");
			}
			try {
				conn = JDBCDataSource.getConnection();
				conn.setAutoCommit(false);
				PreparedStatement pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, bean.getFullName());
				pstmt.setString(2, bean.getCourse());
				pstmt.setString(3, bean.getLogin());
				pstmt.setString(4, bean.getPass());
				pstmt.setString(5, bean.getConfirmPass());
				pstmt.setLong(6, bean.getId());
				pstmt.executeUpdate();
				conn.commit();
				pstmt.close();
			} catch (Exception e) {
				e.printStackTrace();
				try {
					conn.rollback();
				} catch (Exception e2) {
					e2.printStackTrace();
					throw new Exception("Exception : Update Rollback Exception " + e2.getMessage());
				}
			} finally {
				JDBCDataSource.closeConnection(conn);
			}
		}

//		public List search(Bean bean) throws Exception {
//			return search(bean, 0, 0);
//		}

		public List search(Bean bean, int pageNo, int pageSize) throws Exception {
			StringBuffer sql = new StringBuffer("SELECT * FROM ST_USER where 1=1");
			if (bean != null) {
				if (bean.getFullName() != null && bean.getFullName().length() > 0) {
					sql.append(" AND FIRST_NAME like '" + bean.getFullName() + "%'");
				}
				if (bean.getLogin() != null && bean.getLogin().length() > 0) {
					sql.append(" AND LOGIN like '" + bean.getLogin() + "%'");
				}
				
				if (bean.getId() > 0) {
					sql.append(" AND id = " + bean.getId() );
				}
				
				if (bean.getPass() != null && bean.getPass().length() > 0) {
		   			sql.append(" AND PASSWORD like '" + bean.getPass() + "%'");
				}
				
				
			}
			// if page size is greater than zero then apply pagination
			if (pageSize > 0) {
				// Calculate start record index
				pageNo = (pageNo - 1) * pageSize;

				sql.append(" Limit " + pageNo + ", " + pageSize);
				// sql.append(" limit " + pageNo + "," + pageSize);
			}

			System.out.println(sql);
			List list = new ArrayList();
			Connection conn = null;
			try {
				conn = JDBCDataSource.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql.toString());
				ResultSet rs = pstmt.executeQuery();
				while (rs.next()) {
					bean = new Bean();
					bean.setId(rs.getLong(1));
					bean.setFullName(rs.getString(2));
					bean.setCourse(rs.getString(3));
					bean.setLogin(rs.getString(4));
					bean.setPass(rs.getString(5));
					bean.setConfirmPass(rs.getString(6));
					list.add(bean);

				}
				rs.close();
			} catch (Exception e) {
				throw new Exception("Exception: Exception in Search User");
			} finally {
				JDBCDataSource.closeConnection(conn);
			}
			return list;

		}

//		public List getRoles(Bean bean) throws Exception {
//			String sql = "SELECT * FROM ST_USER WHERE ROLE_ID=?";
//			Connection conn = null;
//			List list = new ArrayList();
//			try {
//				conn = JDBCDataSource.getConnection();
//				PreparedStatement pstmt = conn.prepareStatement(sql);
//				pstmt.setLong(1, bean.getRoleId());
//				ResultSet rs = pstmt.executeQuery();
//				while (rs.next()) {
//					bean = new UserBean();
//					bean.setFirstName(rs.getString(2));
//					bean.setLastName(rs.getString(3));
//					bean.setLogin(rs.getString(4));
//					bean.setPassword(rs.getString(5));
//					bean.setDob(rs.getDate(6));
//					bean.setMobileNo(rs.getString(7));
//					bean.setRoleId(rs.getLong(8));
//					bean.setUnSuccessfulLogin(rs.getInt(9));
//					bean.setGender(rs.getString(10));
//					bean.setLastLogin(rs.getTimestamp(11));
//					bean.setLock(rs.getString(12));
//					bean.setRegisterdIP(rs.getString(13));
//					bean.setLastLoginIP(rs.getString(14));
//					bean.setCreatedBy(rs.getString(15));
//					bean.setModifiedBy(rs.getString(16));
//					bean.setCreatedDatetime(rs.getTimestamp(17));
//					bean.setModifiedDatetime(rs.getTimestamp(18));
//
//					list.add(bean);
//
//				}
//				rs.close();
//			} catch (Exception e) {
//				log.error("DateBase Exception ", e);
//				throw new ApplicationException("Exception: Exceptin in Get Roles");
//			} finally {
//				JDBCDataSource.closeConnection(conn);
//			}
//			log.debug("Model Get Roles End");
//			return list;
//
//		}
//
		public Bean authenticate(String login, String password) throws Exception {
			StringBuffer sql = new StringBuffer("SELECT * FROM TECH WHERE LOGIN =? AND PASSWORD =?");
			Bean bean = null;
			Connection conn = null;
			try {
				conn = JDBCDataSource.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql.toString());
				pstmt.setString(1, login);
				pstmt.setString(2, password);
				ResultSet rs = pstmt.executeQuery();
				while (rs.next()) {
					bean = new Bean();
					bean.setId(rs.getLong(1));
					bean.setFullName(rs.getString(2));
					bean.setCourse(rs.getString(3));
					bean.setLogin(rs.getString(4));
					bean.setPass(rs.getString(5));
					bean.setConfirmPass(rs.getString(6));
				}
			} catch (Exception e) {
				throw new Exception("Exception : Exception in get roles");

			} finally {
				JDBCDataSource.closeConnection(conn);
			}
			return bean;

		}

//		public List list() throws ApplicationException {
//			return list(0, 0);
//		}

		public List list(int pageNo, int pageSize) throws Exception {
			ArrayList list = new ArrayList();
			StringBuffer sql = new StringBuffer("select * from Tech");

			if (pageSize > 0) {
				pageNo = (pageNo - 1) * pageSize;
				sql.append(" limit " + pageNo + "," + pageSize);
			}

			System.out.println("preload........"+sql);
			Connection conn = null;

			try {
				conn = JDBCDataSource.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql.toString());
				ResultSet rs = pstmt.executeQuery();
				while (rs.next()) {
					Bean bean = new Bean();
					bean.setId(rs.getLong(1));
					bean.setFullName(rs.getString(2));
					bean.setCourse(rs.getString(3));
					bean.setLogin(rs.getString(4));
					bean.setPass(rs.getString(5));
					bean.setConfirmPass(rs.getString(6));
					list.add(bean);

				}
				rs.close();
			} catch (Exception e) {
				throw new Exception("Exception : Exception in getting list of users");
			} finally {
				JDBCDataSource.closeConnection(conn);
			}
			return list;
		}

		public boolean changePassword(Long id, String oldPassword, String newPassword)
				throws Exception {

			boolean flag = false;
			Bean beanexist = null;

			
			beanexist = findByPK(id);
			
			if (beanexist != null && beanexist.getPass().equals(oldPassword)) {
				beanexist.setPass(newPassword);

				try {
					update(beanexist);
				} catch (Exception e) {
					throw new Exception("LoninId is already exist");
				}
				flag = true;
			} else {
				throw new Exception("Login not exist");
			}

			HashMap<String, String> map = new HashMap<String, String>();

			map.put("login", beanexist.getLogin());
			map.put("pass", beanexist.getPass());
			map.put("fullName", beanexist.getFullName());
			

			String message = EmailBuilder.getChangePasswordMessage(map);
			EmailMessage msg = new EmailMessage();
			msg.setTo(beanexist.getLogin());
			msg.setSubject("SUNRAYS ORS Password has been changed Successfuly.");
			msg.setMessage(message);
			msg.setMessageType(EmailMessage.HTML_MSG);

			EmailUtility.sendMail(msg);

			return flag;
		}

		public long registerUsers(Bean bean) throws Exception {
			long pk = add(bean);

			HashMap<String, String> map = new HashMap<String, String>();
			map.put("login", bean.getLogin());
			map.put("pass", bean.getPass());

			String message = EmailBuilder.getUserRegistrationMessage(map);
			EmailMessage msg = new EmailMessage();

			msg.setTo(bean.getLogin());
			msg.setSubject("Registration is Successful for ORS Project Sunilos");
			msg.setMessage(message);
			msg.setMessageType(EmailMessage.HTML_MSG);
	 
			EmailUtility.sendMail(msg);
			return pk;
		}

		public boolean forgetPassword(String login) throws Exception {
			Bean userData = findByLogin(login);
			boolean flag = false;

			if (userData == null) {
				throw new Exception("Email Id does not exist !");
			}

			HashMap<String, String> map = new HashMap<String, String>();
			map.put("login", userData.getLogin());
			map.put("password", userData.getPass());
			map.put("fullName", userData.getFullName());
			

			String message = EmailBuilder.getForgetPasswordMessage(map);

			EmailMessage msg = new EmailMessage();
			msg.setTo(login);
			msg.setSubject("Tech Titans Password reset");
			msg.setMessage(message);
			msg.setMessageType(EmailMessage.HTML_MSG);

			EmailUtility.sendMail(msg);
			flag = true;
			return flag;
		}
	

	
}
