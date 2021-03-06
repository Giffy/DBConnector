package test;

import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Test;

import dao.DBMComments;
import dao.DBManager;
import model.Comments;
import test.TestDBManager.MockManager;

public class TestDBMComments {

	
	// @Test
	@Deprecated
	public void testInsert(){
		boolean result=true;
		
		DBMComments dbManager = new DBMComments("localhost", "dbtest2", "comments");
		Comments comments1 = getMockDBMComments(); 
		
		try {
			dbManager.connect("root","");
			dbManager.insert(comments1);
		} catch (SQLException e){				// individualmente
			e.printStackTrace();
		} catch (ClassNotFoundException e) {	// individualmente
			result= false;
			e.printStackTrace();
		} catch (Exception e) {					// o agrupadas porque ambas descienden de Exception  
			e.printStackTrace();
			
		} finally{
			dbManager.close();
		}
		
		Assert.assertEquals(true, result);
		Assert.assertEquals(true, comments1.getId()!=-1);
	}
	
	

	@Test
	public void testUpdate(){
		boolean result=true;
		
		DBMComments dbManager = new DBMComments("localhost", "dbtest2", "comments");
		Comments comments1 = getMockDBMComments(); 
		Comments commentsUpdated = null; 
				
		try {
			dbManager.connect("root","");
			dbManager.insert(comments1);
			
				comments1.setMyUser("Actualizado");
				comments1.setComments("Me han actualizado");
				comments1.setDatum(new Date(213415));
								
				dbManager.update(comments1);
				
				// commentsUpdated = dbManager.select(comments1.getId());
				
			
		} catch (SQLException e){				// individualmente
			result= false; 
			dbManager.close();
		} catch (ClassNotFoundException e) {	// individualmente
			e.printStackTrace();
	
		/* } catch (Exception e) {					// o agrupadas porque ambas descienden de Exception  
			e.printStackTrace();
			dbManager.close(); */
		} finally{
			dbManager.close();
		}
		
		Assert.assertEquals("Actualizado", comments1.getMyUser());
		Assert.assertEquals("Me han actualizado", comments1.getComments());
		Assert.assertEquals(true, comments1.getId()!=-1);
		Assert.assertEquals(new Date(213415).toString(), comments1.getDatum().toString());
		
	}
		
	@Test
	public void testGet(){
		boolean result=true;
		DBMComments dbManager = new DBMComments("localhost", "dbtest2", "comments");
		Comments comments1 = getMockDBMComments(); 
		Comments results = null;
		
		try {
			dbManager.connect("root","");
			dbManager.insert(comments1);
							
			results = dbManager.select(comments1.getId());
			
		} catch (Exception e) {					// o agrupadas porque ambas descienden de Exception  
			result = false;
			e.printStackTrace();
			
		} finally{
			dbManager.close();
		}
		
		Assert.assertEquals(true, result);
		Assert.assertEquals(comments1.getMyUser(),results.getMyUser()); 
		Assert.assertEquals(comments1.getComments(),results.getComments()); 
	}
		
	

	@Test
	public void testDeleteAll(){
	
		boolean result=true;
		DBMComments dbManager = new DBMComments("localhost", "dbtest2", "comments");
		
		try {
			dbManager.connect("root","");
			dbManager.deleteAll();
		}catch (Exception e) {					// o agrupadas porque ambas descienden de Exception  
			result = false;
			e.printStackTrace();
			
		} finally{
			dbManager.close();
		}
		
		Assert.assertEquals(true, result);
		
	
	}
	
	
	@Test
	public void testSelect(){
		
		boolean result=true;
		DBMComments dbManager = new DBMComments("localhost", "dbtest2", "comments");
				
		Comments comments1 = getMockDBMComments("User1" , "user1@poo.com"); 
		Comments comments2 = getMockDBMComments("User2" , "user2@poo.com");
		Comments comments3 = getMockDBMComments("User1" , "user3@poo.com");
		Comments comments4 = getMockDBMComments("User4" , "user4@poo.com");
		Comments comments5 = getMockDBMComments("Jordi" , "user4@poo.com");
		
		ArrayList<Comments> results1 = null;
		ArrayList<Comments> results2 = null;
		ArrayList<Comments> results3 = null;
		
		try {
			dbManager.connect("root","");
			
			dbManager.deleteAll();
			
			dbManager.insert(comments1);
			dbManager.insert(comments2);
			dbManager.insert(comments3);
			dbManager.insert(comments4);
			dbManager.insert(comments5);
							
			results1 = dbManager.select("myuser", "LIKE", "User%");
			results2 = dbManager.select("myuser", "=", "'User1'");
			results3 = dbManager.select("id", "BETWEEN", "2 AND 5");
					
			
						
		} catch (Exception e) {					// o agrupadas porque ambas descienden de Exception  
			result = false;
			e.printStackTrace();
			
		} finally{
			dbManager.close();
		}
		
		Assert.assertEquals(true, result);
		Assert.assertEquals(4, results1.size());
 		Assert.assertEquals(2, results2.size());
		Assert.assertEquals(4, results3.size());
	}
		
	
	private Comments getMockDBMComments() {
		return getMockDBMComments("root", "root@root.com");
	}
	
	private Comments getMockDBMComments(String myUser, String eMail) {
				
		Comments comments1 = new Comments();
		comments1.setMyUser( myUser );
		comments1.setEmail( eMail );
		comments1.setSummary("Esto es un resumen");
		comments1.setComments("Esto es un comentario");
		comments1.setDatum(new Date(System.currentTimeMillis()));
		comments1.setWebpage("hola.com");
		return comments1;
	}


	
	
}
