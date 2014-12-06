package fr.plaitant.reycraft.metier;

import java.util.ArrayList;

public class Post {
	private int id;
	private String titre;
	private String description;
	private static ArrayList<Post> listePost = new ArrayList<Post>();
	
	public Post(){
		
	}

	public Post(int id, String titre, String description) {
		super();
		this.id = id;
		this.titre = titre;
		this.description = description;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setTitre(String titre) {
		this.titre = titre;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getTitre() {
		return titre;
	}

	public String getDescription() {
		return description;
	}
	
	public void addPost(Post thePost){
		listePost.add(thePost);
	}
	
	public ArrayList<Post> getPost(){
		return listePost;
	}
	
}
