package dao;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;

import model.Comments;

public class DBMComments extends DBManager<Comments> {

	public DBMComments(String dbHost, String dbName, String dbTable) {
		super(dbHost, dbName, dbTable);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Comments insert(Comments object) throws SQLException { 
		// TODO Auto-generated method stub
		
		int lastInsertedId = -1;
		String strSQL = "insert into "+ getDbTable() +" values (default, ?, ?, ?, ?, ?, ?)";
		PreparedStatement preparedStatement=null;
		
		try{
			// Statement.RETURN_GENERATED_KEYS   = retorna el ID del añadido
			preparedStatement = getConnected()
					 	.prepareStatement(strSQL, Statement.RETURN_GENERATED_KEYS);
		
			preparedStatement.setString(1, object.getMyUser());
			preparedStatement.setString(2, object.getEmail());
			preparedStatement.setString(3, object.getWebpage());
			preparedStatement.setDate(4, object.getDatum());
			preparedStatement.setString(5, object.getSummary());
			preparedStatement.setString(6, object.getComments());
			
			preparedStatement.executeUpdate();
			ResultSet rs = preparedStatement.getGeneratedKeys();
			
			if (rs.next())
				lastInsertedId = rs.getInt(1);
	
			
		}catch (SQLException e){
		   
			throw e; 
			
		}finally{
			try{
				preparedStatement.close();
			}catch (Exception e1){}
			
		}
		
		object.setId(lastInsertedId);
		return object;
	}

	/**
	 * UPDATE 
	 * @throws SQLException 
	 */
		
	@Override
	public void update(Comments object) throws SQLException {
		
		checkFormatUpdate(object);
		
		String strSQL ="UPDATE " + getDbTable() + " set myuser=?, email=?, webpage=?, summary=?, comments=? WHERE id=?";
		PreparedStatement preparedStatement=null;
		
		try{
			preparedStatement = getConnected()
				 	.prepareStatement(strSQL);
	
			preparedStatement.setString(1, object.getMyUser());
			preparedStatement.setString(2, object.getEmail());
			preparedStatement.setString(3, object.getWebpage());
			// preparedStatement.setDate(4, object.getDatum());
			preparedStatement.setString(4, object.getSummary());
			preparedStatement.setString(5, object.getComments());
			preparedStatement.setInt(6, object.getId());
			
			preparedStatement.executeUpdate();
		
			
		}catch (SQLException e){
			throw e;
		}finally{
			try{
				preparedStatement.close();
			}catch (Exception e1){
			};
		}
	}
		
		
	

    
	/**
	 * Verifica que la operacion sea valida
	 * @param operador
	 */
	
	
	private void checkOperador(String operador) {
		ArrayList<String> columns = new ArrayList<String>(
				Arrays.asList("=", "!=", "<>", "<", ">", "<=", ">=", "LIKE", "BETWEEN", "IN"));
		if(!columns.contains(operador))
			throw new RuntimeException("Error la columna "+ operador + " no es valido");
		
	}

	private void checkColumn(String column) {
		ArrayList<String> columns = new ArrayList<String>(
				Arrays.asList("id", "myuser", "email", "datum", "webpage", "summary", "comments"));
		if(!columns.contains(column))
			throw new RuntimeException("Error la " + column + " no existe en la base de datos");
		
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		super.close();
	}
	
	
	/**
	 * Condiciones bajo las cuales se puede actualizar un objeto
	 * 
	 * @param object
	 */
	public static void checkFormatUpdate(Comments object){
		if(object.getId()==-1){
			throw new RuntimeException("El objeto que trata de actualizar no tiene un id valido");
		}
	}
	
	
	 private ArrayList<Comments> resultSetToComments(ResultSet resultSet) throws SQLException {
		ArrayList<Comments> list = new ArrayList<>();
		
		while (resultSet.next()) {
			
			// lee el resultado i 
        	int id = resultSet.getInt("id");
            String user = resultSet.getString("myuser");
            String email = resultSet.getString("email");
            String webpage = resultSet.getString("webpage");
            String summary = resultSet.getString("summary");
            Date date = resultSet.getDate("datum");
            String comments = resultSet.getString("comments");
            
            
            Comments comment = new Comments(); 

            comment.setId(id);
            comment.setEmail(email);
            comment.setDatum(date);
            comment.setMyUser(user);
            comment.setSummary(summary);
            comment.setComments(comments);
            comment.setWebpage(webpage);
            
            list.add(comment);            
		}
		
		return list;
	}

	@Override
	protected Comments mapDbToObject(ResultSet resultSet) throws SQLException {
		// lee el resultado i 

    	int id = resultSet.getInt("id");
        String user = resultSet.getString("myuser");
        String email = resultSet.getString("email");
        String webpage = resultSet.getString("webpage");
        String summary = resultSet.getString("summary");
        Date date = resultSet.getDate("datum");
        String comments = resultSet.getString("comments");
  

        Comments comment = new Comments();    
        comment.setId(id);
        comment.setEmail(email);
        comment.setDatum(date);
        comment.setMyUser(user);
        comment.setSummary(summary);
        comment.setComments(comments);
        comment.setWebpage(webpage);

        return comment; 
	}

	 
	 
	


	
	// TODO   to delete
	/*
	public Comments select(int id) throws SQLException {
		
		String strSQL = "SELECT id, myuser, email, webpage, summary, datum, comments FROM " + 
				getDbTable() + " WHERE id = ?";
		
		PreparedStatement preparedStatement=null;
		Comments comment = null;		
		try{
			preparedStatement = getConnected()
				 	.prepareStatement(strSQL);
	
			preparedStatement.setInt(1, id);
			
			ResultSet resultSet = preparedStatement.executeQuery();
			
			ArrayList<Comments> list = resultSetToComments(resultSet);
			comment= list.get(0);
			
		}catch (SQLException e){
			throw e;
		}
			
		return comment;
	}
	
	/**
     *  Recupera todos los registros con la condicion que:
     *  
     *  la columna column operador Value, donde pueder ser:
     *  
     *   = 'value'
     *  != 'value'
     *   > 'value'
     *   < 'value'
     *  >= 'value'
     *  <= 'value'
     *  BETWEEN
     *  LIKE 'value%'    // use el % para indicar cualquier cosa
     *  
     * 
     * 
     */
    /*
	@Override
	public ArrayList<Comments> select(String column, String operador,  String value) throws SQLException {
		
		checkColumn(column);
		checkOperador(operador);
		
		value= operador.contains("LIKE")? "'" +value +"'": value;
		
		String strSQL = "SELECT * FROM "+ 
				getDbTable() + " WHERE "+ column + " " + operador + " "+ value;
		
		PreparedStatement preparedStatement=null;
		Comments comment = null;		
		ArrayList<Comments> list = null;
		try{
			preparedStatement = getConnected()
				 	.prepareStatement(strSQL);
				
			ResultSet resultSet = preparedStatement.executeQuery();
			list = resultSetToComments(resultSet);
						
		}catch (SQLException e){
			throw e;
		}finally{
			try{
				preparedStatement.close();
			}catch (Exception e1){
			}
		}
		
		return list;
	}
	
	*/
	/*
	 @Override
		protected ArrayList<Comments> resultSetToGeneric(ResultSet resultSet2) throws SQLException {
			ArrayList<Comments> list = new ArrayList<>();
			
			while (resultSet2.next()) {
				
				// lee el resultado i 
	        	int id = resultSet2.getInt("id");
	            String user = resultSet2.getString("myuser");
	            String email = resultSet2.getString("email");
	            String webpage = resultSet2.getString("webpage");
	            String summary = resultSet2.getString("summary");
	            Date date = resultSet2.getDate("datum");
	            String comments = resultSet2.getString("comments");
	                        
	            Comments comment = new Comments(); 

	            comment.setId(id);
	            comment.setEmail(email);
	            comment.setDatum(date);
	            comment.setMyUser(user);
	            comment.setSummary(summary);
	            comment.setComments(comments);
	            comment.setWebpage(webpage);
	            
	            list.add(comment);            
			}
			return list;
		}
	 	*/
	
}
