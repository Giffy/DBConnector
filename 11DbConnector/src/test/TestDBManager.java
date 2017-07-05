package test;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;


import org.junit.Assert;
import org.junit.Test;

import dao.DBManager;
import model.Comments;


public class TestDBManager {

	
	
	@Test
	public void testConnection(){
		boolean result= true;
		DBManager dbManager = new MockManager();
		
		try {
			dbManager.connect("root","");
		}catch (Exception e){
			e.printStackTrace();
			result = false;
		}finally{
		    dbManager.close();
		}
		
		Assert.assertEquals(true, result);
	}
	
	
	@Test
	public void testDeleteAll(){
		boolean result=true;
		DBManager dbManager = new MockManager();
		try {
			dbManager.connect("root","");
			dbManager.deleteAll();
		}catch (Exception e){
			e.printStackTrace();
			result = false;
		}finally{
		    dbManager.close();
		}
		
		Assert.assertEquals(true, result);
	}
	

	
	
	@SuppressWarnings("rawtypes")
	public static class MockManager extends DBManager {
		public MockManager(){
			super ("localhost", "dbtest2", "comments");
		}
		
		public Object insert (Object object){
			return null;	
		}

		@Override
		public void update(Object object) {
			// TODO Auto-generated method stub
		}

		@Override
		public Object select(int id) {
			// TODO Auto-generated method stub
			return null;
		}

		
		public ArrayList select(String column, String value) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		protected ArrayList resultSetToGeneric(ResultSet resultSet2) throws SQLException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public ArrayList select(String column, String operador, String value) throws SQLException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		protected Object mapDbToObject(ResultSet resultSet) throws SQLException {
			// TODO Auto-generated method stub
			return null;
		}
		
	}
	
	
}
