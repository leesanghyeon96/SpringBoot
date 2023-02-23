package com.mysite.sbb;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class JDBCUtil {
		//DB�� ���� �����ϴ� Ŭ���� 
	//1. DB�� �����ϴ� �޼ҵ� 
		//static : ��ü ���� ���� Ŭ�����̸����� �ٷ� ȣ�� 
	public static Connection getConnection() {
		
		String driver = "oracle.jdbc.driver.OracleDriver";
		//String driver = "org.h2.Driver";
		String url = "jdbc:oracle:thin:@localhost:1521:XE"; 
		//String url = "jdbc:h2:tcp://localhost/~/test"; 
		
		Connection conn = null; 
		
		try {
			Class.forName(driver); 
			conn = DriverManager.getConnection(url,"C##HR3","1234");
			//conn = DriverManager.getConnection(url,"sa","");
			
			System.out.println(" DB 연결이 잘 되었습니다. ");   //Ȯ���� �ּ� ó�� 
			return conn; 
			
		}catch (Exception e) {
			e.printStackTrace();
			System.out.println(" DB 연결을 실패했습니다. ");
		}
		return null; 
	}
	
	
	//2. DB������ �����ϴ� �޼ҵ� : Connection, PreparedStatement ��ü�� ���� 
		//Insert, Update, Delete 
	public static void close (PreparedStatement pstmt, Connection conn) {
		if (pstmt != null) {
			try {
				if (!pstmt.isClosed()) {	//pstmt ��ü�� ���ŵ��� �ʴ� ���¶��
					pstmt.close();
					System.out.println("pstmt ���� close()");
				}
			}catch (Exception e) {
				e.printStackTrace();
			}finally {
				pstmt = null; 
			}
		}
		
		if (conn != null) {
			try {
				if (!conn.isClosed()) {
					conn.close();
					System.out.println("conn ���� close()");
				}
			}catch (Exception e) {
				e.printStackTrace();
			}finally {
				conn = null; 
			}

		}
	}
	
	//3. DB������ �����ϴ� �޼ҵ� : Connection, PreparedStatement, ResultSet ��ü�� ���� 
		//Select 
	public static void close (ResultSet rs, PreparedStatement pstmt, Connection conn) {
		if (rs != null) {
			try {
				if (!rs.isClosed()) {	//pstmt ��ü�� ���ŵ��� �ʴ� ���¶��
					rs.close();
				}
			}catch (Exception e) {
				e.printStackTrace();
			}finally {
				rs = null; 
			}
		}
		
		
		
		if (pstmt != null) {
			try {
				if (!pstmt.isClosed()) {	//pstmt ��ü�� ���ŵ��� �ʴ� ���¶��
					pstmt.close();
				}
			}catch (Exception e) {
				e.printStackTrace();
			}finally {
				pstmt = null; 
			}
		}
		
		if (conn != null) {
			try {
				if (!conn.isClosed()) {
					conn.close();
				}
			}catch (Exception e) {
				
			}finally {
				conn = null; 
			}

		}
	}
	
	public static void main(String[] args) {
		JDBCUtil jdbc = new JDBCUtil();
		
		jdbc.getConnection();
		
	}

}
