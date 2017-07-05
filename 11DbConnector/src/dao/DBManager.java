package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.mysql.jdbc.CommunicationsException;

import model.Comments;

public abstract class DBManager<T> implements DBAccess<T> {

	private String dbName;
	private final String dbTable;
	
	private String dbUri;
	private Connection connect;
	
	private ResultSet resultSet2;
	
	
	public DBManager(String dbHost, String dbName, String dbTable){
		this.dbTable = dbTable;
		this.dbName = dbName;
		this.dbUri= "jdbc:mysql://host/dbName?user=root&password=12345";
		dbUri= dbUri.replace("host",dbHost).
				replace("dbName",dbName);
	}
	
	
	@Override
	public void connect(String user, String password) 
			throws SQLException, ClassNotFoundException {
		// TODO Auto-generated method stub
		
		try {
			String uri= dbUri.replace("root",user)
					.replaceAll("12345", password);
			
			// Cargar el driver MYSQL
			Class.forName("com.mysql.jdbc.Driver");
			// jdbc:mysql://ip database // database ?
			connect = DriverManager
					.getConnection("jdbc:mysql://localhost/"+ dbName +"?"
							+ "user=root&password=");
			// Statements allow to issue SQL queries to the Database
			// statement= connect.createStatement();
		}catch (CommunicationsException e){
			System.err.println("Verifique que la base de datos esté activa.");
			close();
			throw e;
		}catch (ClassNotFoundException e){
			System.err.println("Verifique que el driver se ha incluido. com.mysql.jdbc.Driver ");
			close();
			throw e;
		}catch (Exception e){
			close();
			throw e;
		}
	}


	@Override
	public void deleteAll() throws SQLException{
		PreparedStatement preparedStatement=null;
		try{
			preparedStatement = connect
					.prepareStatement("TRUNCATE "+ dbTable);
			preparedStatement.executeUpdate();
			
		}catch (SQLException e){
			close();
			throw e;
		}finally{
			try {
				preparedStatement.close();
			} catch (Exception e1) {} 
		}         
	}
	
	
	@Override
	public void delete(int id) throws SQLException {
		// TODO 
		PreparedStatement preparedStatement=null;
		try{
			preparedStatement = connect
					.prepareStatement("delete from "+ dbTable +" where id= ?");
			preparedStatement.setInt(1, id);
			preparedStatement.executeUpdate();
			
		}catch (SQLException e){
			throw e;
		}finally{
			try {
				preparedStatement.close();
			} catch (Exception e1) {} 
		}
	}
	
	

	@Override
	public T select(int id) throws SQLException {
		
		String strSQL = "SELECT * FROM "+ 
				getDbTable() + " WHERE id = ?";
		
		PreparedStatement preparedStatement=null;
		T generic = null;		
		try{
			preparedStatement = getConnected()
				 	.prepareStatement(strSQL);
			preparedStatement.setInt(1, id);
			ResultSet resultSet = preparedStatement.executeQuery();
			
			ArrayList<T> list = resultSetToGeneric(resultSet);
			generic= list.get(0);
			
		}catch (SQLException e){
			throw e;
		}finally{
			try{
				preparedStatement.close();
			}catch (Exception e1){
			}
		}
			
		return generic;
	}
	
	
	@Override
	public ArrayList<T> select(String column, String operador,  String value) throws SQLException {
		
		// checkColumn(column);
		// checkOperador(operador);
		
		value= operador.contains("LIKE")? "'" +value +"'": value;
		
		String strSQL = "SELECT * FROM "+ 
				getDbTable() + " WHERE "+ column + " " + operador + " "+ value;
		
		PreparedStatement preparedStatement=null;
		ArrayList<T> list = null;
		try{
			preparedStatement = getConnected()
				 	.prepareStatement(strSQL);
				
			ResultSet resultSet = preparedStatement.executeQuery();
			list = resultSetToGeneric(resultSet);
						
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
	
	/**
	 * Transforma el resultado de una consulta resultSet en un objeto de tipo 
	 * T
	 * @param resultSet
	 * @return lista de objetos de tipo T 
	 * @throws SQLException
	 */

	protected  ArrayList<T> resultSetToGeneric(ResultSet resultSet)
			throws SQLException{
		ArrayList<T> list = new ArrayList<>(); 		
		while (resultSet.next()) {
			    T generic= mapDbToObject(resultSet); 
	            list.add(generic);  
		 }		 
		 return list; 		
	}

	

	
	
	@Override
	public void close() {
		try{
 			if (resultSet2 !=null){
 				resultSet2.close();
 				resultSet2=null;
			}
						
			if (connect !=null){
				connect.close();
				connect=null;
			}
		}catch (Exception e){
		
		}
	}
	
	

	
	


	//  getters y setters (metodos accesores)

	public String getDbName() {
		return dbName;
	}
	
	public String getDbTable() {
		return dbTable;
	}
	
	protected Connection getConnected(){
		return connect;
	}
		
	
	//  -----   Metodos Abstractos
	
	@Override
	public  abstract T insert(T object) throws SQLException; 
		
	@Override
	public abstract void update(T object) throws SQLException; 

	
	protected abstract T mapDbToObject(ResultSet resultSet) throws SQLException;  

	
	 
	 
}
