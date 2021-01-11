package matisse;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.matisse.*;
import escuela.*;


public class GestionDB {

	public static void creaObjetos(String hostname, String dbname) {
		try {
		// Abre la base de datos con el Hostname (localhost), dbname (biblioteca1) y el namespace "biblioteca1".
		MtDatabase db = new MtDatabase(hostname, dbname, new MtPackageObjectFactory("", " escuela"));
		db.open();
		db.startTransaction();
		System.out.println("Conectado a la base de datos " + db.toString() + " de Matisse");
		// Crea un objeto Profesores
		Profesores a1 = new Profesores(db);
		a1.setNombre("Hector");
		a1.setApellidos("Caballero");
		a1.setTelefono(689854468);
		a1.setDni("30989147J");
		System.out.println("Hector Caballero creado...");
		
		// Crea un objeto Cursos
		Cursos l1 = new Cursos(db);
		l1.setFecha("18/01/2021");
		System.out.println("Fecha del curso creado");
		// Crea otro objeto curso
		Asignaturas d2 = new Asignaturas(db);
		d2.setDiaSemana(3);;
		System.out.println("Días creado...");
		
		
		
		// Crea un array de Obras para guardar los libros y hacer las relaciones
		Clases o1[] = new Clases[2];
		o1[0] = l1;
		o1[1] = d2;
		// Guarda las relaciones del autor con las clases impartidas.
		a1.setImparte(o1);
		// Ejecuta un commit para materializar las peticiones.
		db.commit();
		// Cierra la base de datos.
		db.close();
		System.out.println("\nHecho.");
		} catch (MtException mte) {
		System.out.println("MtException : " + mte.getMessage());
		}
		}
	// Borrar todos los objetos de una clase
	public static void borrarTodos(String hostname, String dbname) {
	System.out.println("====================== Borrar Todos =====================\n");
	try {
	MtDatabase db = new MtDatabase(hostname, dbname, new MtPackageObjectFactory("", "escuela"));
	db.open();
	db.startTransaction();
	// Lista todos los objetos Obra que hay en la base de datos, con el método
	// getInstanceNumber
	System.out.println("\n" + Profesores.getInstanceNumber(db) + " Clases en la DB.");
	// Borra todas las instancias de Obra
	Profesores.getClass(db).removeAllInstances();
	// materializa los cambios y cierra la BD
	db.commit();
	db.close();
	System.out.println("\nHecho.");
	} catch (MtException mte) {
	System.out.println("MtException : " + mte.getMessage());
	}
	}
	// Borrar todos los objetos de una clase
		public static void borrarTodos2(String hostname, String dbname) {
		System.out.println("====================== Borrar Todos =====================\n");
		try {
		MtDatabase db = new MtDatabase(hostname, dbname, new MtPackageObjectFactory("", "escuela"));
		db.open();
		db.startTransaction();
		// Lista todos los objetos Obra que hay en la base de datos, con el método
		// getInstanceNumber
		System.out.println("\n" + Clases.getInstanceNumber(db) + " Clases en la DB.");
		// Borra todas las instancias de Obra
		Clases.getClass(db).removeAllInstances();
		// materializa los cambios y cierra la BD
		db.commit();
		db.close();
		System.out.println("\nHecho.");
		} catch (MtException mte) {
		System.out.println("MtException : " + mte.getMessage());
		}
		}
	public static void modificaObjeto(String hostname, String dbname, String nombre, String nuevonombre) {
		System.out.println("=========== Modifica un objeto ==========\n");
		int nProfesores = 0;
		try {
		MtDatabase db = new MtDatabase(hostname, dbname, new MtPackageObjectFactory("", "escuela"));
		db.open();
		db.startTransaction();
		// Lista cuántos objetos Obra con el método getInstanceNumber
		System.out.println("\n" + Profesores.getInstanceNumber(db) + " Profesores en la DB.");
		nProfesores = (int) Profesores.getInstanceNumber(db);
		// Crea un Iterador (propio de Java)
		MtObjectIterator<Profesores> iter = Profesores.<Profesores>instanceIterator(db);
		System.out.println("recorro el iterador de uno en uno y cambio cuando encuentro 'nombre'");
		while (iter.hasNext()) {
		Profesores[] autores = iter.next(nProfesores);
		for (int i = 0; i < autores.length; i++) {
			// Busca una autor con nombre 'nombre'
			if (autores[i].getNombre().compareTo(nombre) == 0) {
			autores[i].setNombre(nuevonombre);
			} else {
			}
			}
			}
			iter.close();
			// materializa los cambios y cierra la BD
			db.commit();
			db.close();
			System.out.println("\nHEcho.");
			} catch (MtException mte) {
			System.out.println("MtException : " + mte.getMessage());
			}
			}
	
	
	public static void ejecutaOQL(String hostname, String dbname) {
		MtDatabase dbcon = new MtDatabase(hostname, dbname);
		// Abre una conexión a la base de datos
		dbcon.open();
		try {
		// Crea una instancia de Statement
		Statement stmt = dbcon.createStatement();
		// Asigna una consulta OQL. Esta consulta lo que hace es utilizar REF() para
		// obtener el objeto
		// directamente en vez de obtener valores concretos (que también podría ser).
		String commandText = "SELECT REF(a) from escuela.Profesores a;";
		// Ejecuta la consulta y obtiene un ResultSet
		ResultSet rset = stmt.executeQuery(commandText);
		Profesores a1;
		// Lee rset uno a uno.
		while (rset.next()) {
		// Obtiene los objetos Autor.
		a1 = (Profesores) rset.getObject(1);
		// Imprime los atributos de cada objeto con un formato determinado.
		System.out.println("Profesores: " + String.format("%16s", a1.getNombre())
		+ String.format("%16s", a1.getApellidos())  + String.format("%16s", a1.getDni())+ String.format("%16s", a1.getTelefono()));
		}
		// Cierra las conexiones
		rset.close();
		stmt.close();
		} catch (SQLException e) {
		System.out.println("SQLException: " + e.getMessage());
		}
		}
		
	static String hostname = "localhost";
	static String dbname = "escuela";
	public static void main(String[] args)
	{ 
		creaObjetos(hostname, dbname);
		borrarTodos(hostname, dbname);
	    borrarTodos2(hostname, dbname);
		//modificaObjeto(hostname, dbname, "Hector", "Ismael");
		ejecutaOQL(hostname, dbname);
	}
}
