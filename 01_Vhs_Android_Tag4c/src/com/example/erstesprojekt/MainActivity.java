package com.example.erstesprojekt;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity {

	//damit muss man beim Verwenden der bundle-persistierung-Methode einen Typecast machen private List<Person> liste = new ArrayList<Person>();
	ArrayList<Person> liste = new ArrayList<Person>(); //somit vermeidet man den TypeCast, der bei der Serializierung notwendig ist.
	
	
	//SQLite integration
	DatabaseHelper dbHelper;
	
	
	
	@SuppressWarnings("unchecked")
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //viewHolder = new ViewHolder(); //hier zu früh
        
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        } else {
        	//Notwendiger Zeig um Persistierung zurückzuholen 

        	liste = (ArrayList<Person>) savedInstanceState.getSerializable(KEY_PERSON_LIST);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);  //Merke: (FGL) Nur bei Konstruktoren muss der Aufruf der Super - Klasse als erster Aufruf stehen.
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        	
        	       }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }
    }
    	
    	private ViewHolder viewHolder; //Hier keine Instantiierung vornehmen. Absturz!!!
    	class ViewHolder {
    		
    		
    		//TODO: Eventuell Cachintg/ Memoization
    		EditText editVorname;
    		EditText editNachname;
    		EditText editAlter;
    		Button mybuttondummy;
    		EditText editListe;
    		
    		ListView listViewPerson;
    		
    		
    		
    		
    		public ViewHolder(){    			    		
    			editVorname = (EditText) findViewById(R.id.EditText01);
    			editNachname = (EditText) findViewById(R.id.edit_nachname);
    			editAlter =  (EditText) findViewById(R.id.editAlter);
    			editListe = (EditText) findViewById(R.id.editListe);
    			
    			listViewPerson = (ListView) findViewById(R.id.listViewPerson);
    		}
    	}
    
    public void buttonOkClick(View view){
    	
//    	EditText editText01 = (EditText) findViewById(R.id.EditText01);
    	String vorname = viewHolder.editVorname.getText().toString();
    	
//    	EditText edit_nachname = (EditText) findViewById(R.id.edit_nachname);
    	String nachname = viewHolder.editNachname.getText().toString();
    	
//    	EditText edit_alter =  (EditText) findViewById(R.id.editAlter);
    	String alter = viewHolder.editAlter.getText().toString();
    	int iAlter = Integer.parseInt(alter);
    	
    	String meldung = "1. " + getString(R.string.greeting_text) + vorname + " " + nachname + getString(R.string.alter_text) + iAlter;
    	
    	Context context = getApplicationContext();
    	
    	//######################################################
    	//TODO Über den Inflator den Custom Toast hinzufügen
    	LayoutInflater inflator = getLayoutInflater();
    	View layout = inflator.inflate(R.layout.mein_toast_layout, null); //!!! Abweichung von der Android Dokumentation:  (ViewGroup) findViewById(R.id.layout_verticalLinearLayout1)); den zweiten PArameter nur verwenden, wenn das Objekt schon existiert
    	
    	TextView text = (TextView) layout.findViewById(R.id.test_mein_text); 
    	text.setText(meldung);
    	
    	Toast toast = new Toast(getApplicationContext());//.makeText(context, meldung, Toast.LENGTH_LONG);
    	toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
    	toast.setDuration(Toast.LENGTH_LONG);
    	toast.setView(layout);  //VErsuch einen custom Toast anzuzeigen
    	
    	
    //	toast.setGravity(Gravity.TOP|Gravity.LEFT, 0, 0);
    	toast.show();
    	
    	//####################################################
    	
    	Person p = new Person(vorname, nachname, iAlter);
    	liste.add(p);
    	dbHelper.add(p);
    	
    	String ausgabe = "";
    	for(Person person : liste){
    		ausgabe += person.getNachname()+"\n";    	
    	}
    	viewHolder.editListe.setText(ausgabe);
    	
    	
    	//#### Arbeiten mit der List View ################
    	//ABER: Rechte auf das Addressbuch müssen vorhanden sein. Andorid manifest Permissions.
    	updateAdapter();
    	
    }


    public final String ALC_TAG = "Activity Lifecycle";  //Key für den Logger- Tag
    public final String KEY_PERSON_LIST = "KEY_PERSON_LIST"; //Konstante für die persistierung/serialisierung unserer Liste
	@Override
	protected void onStart() {
				super.onStart();
				Log.d(this.ALC_TAG, "Bin in der onstart() Methode");
				
				 viewHolder = new ViewHolder(); //hier der richtige moment den ViewHolder zu instantiieren
				 
				 //Listener registrieren z.B. auf das ListViewFEld clicken
				 registerOnItemClick();
				 
				 
				 dbHelper = new DatabaseHelper(this.getApplicationContext()); //unsere Klasse für die SQLite Anbindung 
	}


	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.d(this.ALC_TAG, "Bin in der onDestroy() Methode");
	}


	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		Log.d(this.ALC_TAG, "Bin in der onPause() Methode");
	}


	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.d(this.ALC_TAG, "Bin in der onResume() Methode");
	}


	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		Log.d(this.ALC_TAG, "Bin in der onRestart() Methode");
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onRestart();
		Log.d(this.ALC_TAG, "Bin in der onStop() Methode");
	}


	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		
		//NOTWENDIG ZUM PERSISTIERN DER DATEN IM BUNDLE
		super.onSaveInstanceState(outState);
		outState.putSerializable(this.KEY_PERSON_LIST, liste); //liste darf nicht das Interface sein, sondern muss explizit die Klasse ArrayList sein.
		
		
																//GRUND: SERIALIZIERUNG geht nur mit expliziter Klasse, ggf. reicht auch hier ein "dreckiger" Typecast
	}
    
	
	
	//###################### ListView zur AUSWAHL ########################################
	// This is the Adapter being used to display the list's data
    SimpleCursorAdapter mAdapter;
    SimpleCursorAdapter mAdapterSql;

    
    //### Für die Kontakte
    // These are the Contacts rows that we will retrieve
    static final String[] PROJECTION = new String[] {ContactsContract.Data._ID,
            ContactsContract.Data.DISPLAY_NAME};

    // This is the select criteria
    static final String SELECTION = "((" + 
            ContactsContract.Data.DISPLAY_NAME + " NOTNULL) AND (" +
            ContactsContract.Data.DISPLAY_NAME + " != '' ))";
//################
    
	
	private void updateAdapter(){
		//Methode tag 3 - Liste zur Auswahl hinzufügen
		
		  // For the cursor adapter, specify which columns go into which views
		//Mit Kontakten
	    //String[] fromColumns = {ContactsContract.Data.DISPLAY_NAME};
		
		//Mit SQLite
		String[] fromColumns = {"vorname"};
	    int[] toViews = {android.R.id.text1}; // The TextView in simple_list_item_1

	    
	    //Erzeuge einen Cursor über die Datensätze
	    //ContactsContract ist das Adressbuch des Geräts
//	    CursorLoader cursorLoader = new CursorLoader(this, ContactsContract.Data.CONTENT_URI, PROJECTION, SELECTION, null, null);
//        Cursor cursor = cursorLoader.loadInBackground();
//        System.out.println("FGL: CURSOR ANZAHL = " + cursor.getCount());
//        cursor.moveToFirst();
        
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursorSql = db.rawQuery("SELECT _id, vorname FROM person ORDER BY vorname", null);  //%1 etc. könnte mit Selection Arguments ersetzt werden, z.B. wenn eine Where Klausel vorhanden wäre.
        //Merke: _id Kleinschreibung anders als 
        System.out.println("FGL: CURSORSQL ANZAHL = " + cursorSql.getCount());
        
        
	    
	    // Create an empty adapter we will use to display the loaded data.
	    // We pass null for the cursor, then update it in onLoadFinished()
	    //this = context
	    //layout = hier sollte ein eigenen Layout gemacht werden
	    //null ist der Cursor
	    //fromColumn = namen der spalten, die der Cursor zurückliefert
	    //toViews
	    //0    Flags
//	    mAdapter = new SimpleCursorAdapter(this, 
//	            android.R.layout.simple_list_item_1, null,
//	            fromColumns, toViews, 0);
	    
	    //mAdapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_1, cursor, fromColumns, toViews, 0);
	    
	    mAdapterSql = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_1, cursorSql, fromColumns, toViews, 0);
	    
	    
	    
	    
	    //(R.id.listViewPerson).setListAdapter(mAdapter); //Besser über unseren ViewHolder arbeiten.
	    //aus den Kontakten viewHolder.listViewPerson.setAdapter(mAdapter);
	    
	    //aus sqlite
	    viewHolder.listViewPerson.setAdapter(mAdapterSql);
	    
	}
	

	//Auf dem ListView das OnLongClick ereignis ausgeben
	private void registerOnItemClick(){
		viewHolder.listViewPerson.setOnItemClickListener(new OnItemClickListener(){ //Anonyme Klasse, über ein Interface OnItemListerner

			//Eclipse: addUnimplementedMEthods.....
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Log.v(ALC_TAG,"Click auf ListView Position: " + position + " .. id: " + id);
				
				//Nun löschen
				dbHelper.remove(id);
				
				//anschliessend muss der Select neu gemacht werden.
				//Im Falle einer teruern abfrage muss versucht werden dies asynchron zu machen, das sonst das ui blockiert.
				mAdapterSql.getCursor().requery();
				//siehe sonst : mAdapterSql = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_1, cursorSql, fromColumns, toViews, 0);
				
				//das direkte Entfernen aus der Liste funktioniert nicht / da gibt es keine Methode f+r viewHolder.listViewPerson.re
				updateAdapter(); //ruft SQL neu auf
				
				
				//Intent
				Uri webpage = Uri.parse("http://www.google.de");
				Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
				
				//Welche Intent-Aktivitäten gibt es
				PackageManager packageManager = getPackageManager();
				List<ResolveInfo> activities = packageManager.queryIntentActivities(intent, 0);
				for(ResolveInfo info : activities) {
					ActivityInfo aci = info.activityInfo;
					//R.string(aci.descriptionRes);  //NEIN: Ist eine Konstante aus dem Package der speziellen Activität, nicht über das eigenen Projekt "R" erreichbar.
					
					
					
				}
			}
			
		});
	}
	
}
