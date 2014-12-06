package fr.plaitant.reycraft.bdd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import fr.plaitant.reycraft.metier.Post;

public class BlogBDD {
	private static final int VERSION_BDD = 1;
	private static final String NOM_BDD = "reycraft.db";
 
	private static final String TABLE_BLOG = "table_blog";
	private static final String COL_ID = "ID";
	private static final int NUM_COL_ID = 0;
	private static final String COL_TITRE = "Titre";
	private static final int NUM_COL_ISBN = 1;
	private static final String COL_DESCRIPTION = "Description";
	private static final int NUM_COL_TITRE = 2;
 
	private SQLiteDatabase bdd;
 
	private ReycraftSQLite maBaseSQLite;
 
	public BlogBDD(Context context){
		//On cr�er la BDD et sa table
		maBaseSQLite = new ReycraftSQLite(context, NOM_BDD, null, VERSION_BDD);
	}
 
	public void open(){
		//on ouvre la BDD en �criture
		bdd = maBaseSQLite.getWritableDatabase();
	}
 
	public void close(){
		//on ferme l'acc�s � la BDD
		bdd.close();
	}
 
	public SQLiteDatabase getBDD(){
		return bdd;
	}
 
	public long insertPost(Post post){
		//Cr�ation d'un ContentValues (fonctionne comme une HashMap)
		ContentValues values = new ContentValues();
		//on lui ajoute une valeur associ� � une cl� (qui est le nom de la colonne dans laquelle on veut mettre la valeur)
		values.put(COL_TITRE, post.getTitre());
		values.put(COL_DESCRIPTION, post.getDescription());
		//on ins�re l'objet dans la BDD via le ContentValues
		return bdd.insert(TABLE_BLOG, null, values);
	}
 
	public int updatePost(int id, Post post){
		//La mise � jour d'un livre dans la BDD fonctionne plus ou moins comme une insertion
		//il faut simple pr�ciser quelle livre on doit mettre � jour gr�ce � l'ID
		ContentValues values = new ContentValues();
		values.put(COL_TITRE, post.getTitre());
		values.put(COL_DESCRIPTION, post.getDescription());
		return bdd.update(TABLE_BLOG, values, COL_ID + " = " +id, null);
	}
 
	public int removePostWithID(int id){
		//Suppression d'un livre de la BDD gr�ce � l'ID
		return bdd.delete(TABLE_BLOG, COL_ID + " = " +id, null);
	}
 
	public Post getPostWithTitre(String titre){
		//R�cup�re dans un Cursor les valeur correspondant � un livre contenu dans la BDD (ici on s�lectionne le livre gr�ce � son titre)
		Cursor c = bdd.query(TABLE_BLOG, new String[] {COL_ID, COL_TITRE, COL_DESCRIPTION}, COL_TITRE + " LIKE \"" + titre +"\"", null, null, null, null);
		return cursorToPost(c);
	}
 
	//Cette m�thode permet de convertir un cursor en un livre
	private Post cursorToPost(Cursor c){
		//si aucun �l�ment n'a �t� retourn� dans la requ�te, on renvoie null
		if (c.getCount() == 0)
			return null;
 
		//Sinon on se place sur le premier �l�ment
		c.moveToFirst();
		//On cr�� un livre
		Post post = new Post();
		//on lui affecte toutes les infos gr�ce aux infos contenues dans le Cursor
		post.setId(c.getInt(NUM_COL_ID));
		post.setTitre(c.getString(NUM_COL_ISBN));
		post.setDescription(c.getString(NUM_COL_TITRE));
		//On ferme le cursor
		c.close();
 
		//On retourne le livre
		return post;
	}
}
