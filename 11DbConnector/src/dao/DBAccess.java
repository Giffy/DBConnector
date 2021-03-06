package dao;

import java.sql.SQLException;
import java.util.ArrayList;

public interface DBAccess <T>{

	/**  
	 * Conexion a la base de datos
	 * 
	 * @param user				usuario de la base de datos
	 * @param password			palabra secreta de la base de datos
	 * @throws SQLException		si no se puede abrir la base de datos, no autorizado ... etc
	 * @throws ClassNotFoundException	Si el driver no esta instalado
	 */
	public void connect(String user, String password)
			throws SQLException, ClassNotFoundException;
	
	/**
	 * Inserta un objeto T en la tabla
	 * 
	 * @param object  	object de tipo T
	 * @return
	 */
	public T insert(T object) throws SQLException;
		
	/**
	 * Actualiza un objeto tipo T, donde T contiene las columnas de la tabla a actualizar
	 * @param object
	 * @throws SQLException 
	 */
	public void update (T object) throws SQLException;
	
		
	/**
	 * Recupera un objeto tipo T segun el id, los miembres de T contienen los valores de 
	 * columnas de la tabla
	 * 
	 * @param id
	 * @return
	 * @throws SQLException 
	 */
	public T select(int id) throws SQLException;
	
	
	/**
	 * Ejecuta in selec segun strSQL, y retorna un ArrayList con todos los objetos 
	 * recuperados
	 * 
	 * @param column
	 * @param value TODO
	 * @return
	 * @throws SQLException 
	 */
	public ArrayList<T> select (String column, String operador, String value) throws SQLException;
	
	/**
	 * Elimina el registro segun el id
	 * 
	 * @param id
	 * @throws SQLException 
	 */
	public void delete(int id) throws SQLException;
	
	public void deleteAll() throws SQLException;
	
	public void close();
	
	
	
	
}
