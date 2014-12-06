package fr.plaitant.reycraft;

import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import fr.bdd.reycraft.BlogBDD;
import fr.metier.reycraft.Blog;
import fr.metier.reycraft.Post;

public class MainActivity extends FragmentActivity implements Runnable {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
     * will keep every loaded fragment in memory. If this becomes too memory
     * intensive, it may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link android.support.v4.view.ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;
    private ProgressDialog mprogressDialog;
    private static BlogBDD blogBDD;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mprogressDialog = new ProgressDialog(this);
        mprogressDialog.setCancelable(false);
        mprogressDialog.setCanceledOnTouchOutside(false);
        // Message de la barre de progression
        mprogressDialog.setMessage("Chargement en cours...");
        // Titre de la barre de progression
        mprogressDialog.setTitle("ReyCraft");
        // Style de la barre de progression(STYLE_HORIZONTAL ou STYLE_SPINNER)
        mprogressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        // Affichage de la barre de progression
        //mprogressDialog.show();
           
        Thread thread = new Thread(this);
        thread.start();
        
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the app.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        
        Blog blog = new Blog();
        blog.execute();
        try {
			blog.get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        
        blogBDD = new BlogBDD(this);
        Post post = new Post(1, "Teste", "BLABLA");
        blogBDD.open();
        blogBDD.insertPost(post);
        
        //Pour v�rifier que l'on a bien cr�� notre livre dans la BDD
        //on extrait le livre de la BDD gr�ce au titre du livre que l'on a cr�� pr�c�demment
        Post postFromBdd = blogBDD.getPostWithTitre(post.getTitre());
        //Si un livre est retourn� (donc si le livre � bien �t� ajout� � la BDD)
        if(postFromBdd != null){
        	//On affiche les infos du livre dans un Toast
        	//Toast.makeText(this, postFromBdd.getDescription(), Toast.LENGTH_LONG).show();
        }else{
        	Toast.makeText(this, "Echec", Toast.LENGTH_LONG).show();
        }
     }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        //Log.d("moi", "Le menu est cr�e");
        return true;
    }
    
    

    /**
     * A {@link android.support.v4.app.FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a DummySectionFragment (defined as a static inner class
            // below) with the page number as its lone argument.
            Fragment fragment = new DummySectionFragment();
            Bundle args = new Bundle();
            args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, position + 1);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.title_section1).toUpperCase(l);
                case 1:
                    return getString(R.string.title_section2).toUpperCase(l);
                case 2:
                    return getString(R.string.title_section3).toUpperCase(l);
            }
            return null;
        }
    }

    /**
     * A dummy fragment representing a section of the app, but that simply
     * displays dummy text.
     */
    public static class DummySectionFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        public static final String ARG_SECTION_NUMBER = "section_number";

        public DummySectionFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
        	super.onCreate(savedInstanceState);
            View rootView = inflater.inflate(R.layout.fragment_main_dummy, container, false);
            //TextView dummyTextView = (TextView) rootView.findViewById(R.id.section_label);
            
            switch (getArguments().getInt(ARG_SECTION_NUMBER)) {
            case 1:
            	View rootViewb = inflater.inflate(R.layout.blog_main, container, false);
                /*TextView dummyTextViewb = (TextView) rootViewb.findViewById(R.id.section_label);
            	dummyTextViewb.setText(Integer.toString(getArguments().getInt(ARG_SECTION_NUMBER)));*/
            	ListView lvListe = (ListView) rootViewb.findViewById(R.id.lvListe);
            	ArrayList<String> listePosts = new ArrayList<String>();
            	Post post = new Post();
            	ArrayList<Post> listePost = post.getPost();
            	//Log.d("moi", "Avant boucle");
            	for (Post p : listePost) {
            		//Log.d("moi", "Dans Boucle");
					String sp = p.getTitre().toUpperCase() + "\n ------------------ \n"
							+ p.getDescription() + "\n";
            		listePosts.add(sp);
            	}
            	//Log.d("moi", "Apr�s Boucle");
        		
        		lvListe.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, listePosts));
                //lvListe.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_2, listePosts));
        		return rootViewb;
            case 2:
            	View rootViews = inflater.inflate(R.layout.server_main, container, false);
                return rootViews;
            case 3:
            	View rootViewc = inflater.inflate(R.layout.connexion_main, container, false);
            	return rootViewc;
            default:
            	return rootView;
            }
        }
              
    }
    
    
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
    	//Log.d("moi", "T'as  cliqu� sur un item du menu");
        switch (item.getItemId()) {
            case R.id.action_about:
            	Log.d("moi", "A propos");
                Intent intentA = new Intent(MainActivity.this, AboutActivity.class);
            	startActivity(intentA);
                return true;
            case R.id.action_settings:
            	Log.d("moi", "Param");
            	Intent intentS = new Intent(MainActivity.this, SettingsActivity.class);
            	startActivity(intentS);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    
    public boolean onItemSelected(View v){
    	Intent intent = new Intent(MainActivity.this, BlogPost.class);
    	startActivity(intent);
    	return true;
    }

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			// Traitement num�ro 1
			// Mettre un traitement � la place de Thread.sleep()
			// Attente de 5000ms = 5s
			Thread.sleep(5000);
			handler.sendEmptyMessage(1);
			// Traitement num�ro 2
			// Mettre un traitement � la place de Thread.sleep()
			// Attente de 3000ms = 3s
			Thread.sleep(3000);
			handler.sendEmptyMessage(2);
			// Traitement num�ro 3
			// Mettre un traitement � la place de Thread.sleep()
			// Attente de 5000ms = 5s
			Thread.sleep(5000);
			handler.sendEmptyMessage(3);
			// Traitement num�ro 4
			// Mettre un traitement � la place de Thread.sleep()
			// Attente de 2000ms = 2s
			Thread.sleep(2000);
			handler.sendEmptyMessage(4);
			// Traitement par d�faut
			// Mettre un traitement � la place de Thread.sleep()
			// Attente de 10000ms = 10s
			Thread.sleep(5000);
			handler.sendEmptyMessage(5);
		}catch (InterruptedException e) {
			e.printStackTrace();
		}

	}
	
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			int i = msg.what;
			switch (i){
				case 1:
					mprogressDialog.setMessage("T�l�chargement des stupides termin�...");
					mprogressDialog.setProgress(17);
					break; 
				case 2:
					mprogressDialog.setMessage("Alibi v�rifi� termin�...");
					mprogressDialog.setProgress(30);
					break;
				case 3:
					mprogressDialog.setMessage("Nettoyag de la pi�ce termin�...");
					mprogressDialog.setProgress(52);
					break;
				case 4:
					mprogressDialog.setMessage("Fini!!! AVF !");
					mprogressDialog.setProgress(100);
					break;
				default:
					// Fermer le message
					mprogressDialog.dismiss();
			}
		
		}	
		
	};
	
	
	public BlogBDD getBlogBDD(){
		return blogBDD;
	}
}
