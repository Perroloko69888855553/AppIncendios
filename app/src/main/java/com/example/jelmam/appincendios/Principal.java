package com.example.jelmam.appincendios;
//Prueba de cambio
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;


import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.opencsv.CSVReader;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Principal extends AppCompatActivity {

    private File archCSV;
    private TextView mensaje;


    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.principal);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Comprobar Si tiene conexión
        //Sí tiene descargar
        mensaje = (TextView) findViewById(R.id.tv_mensaje);
        archCSV = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "parteIncendios.csv");

        String puedo = Environment.getExternalStorageState();
        Log.d("EXISTE", puedo + archCSV.exists());
        Log.d("EXISTE", Environment.getExternalStorageDirectory().getAbsolutePath() + "parteIncendios.csv");
        descargarCSV();




        lecturaCSV();

        Button dale = (Button) (findViewById(R.id.b_prueba));

        dale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                File tempArch = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "parteIncendios.csv");
                Log.d("EXISTE", tempArch.getAbsolutePath());
                Toast.makeText(Principal.this, "Existe = "+tempArch.isFile() + " Ruta = "+ tempArch.getAbsolutePath() + " Leer = " + tempArch.canRead(), Toast.LENGTH_SHORT).show();

            }
        });

    }





    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_principal, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.tv_titulo);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }

    public void lecturaCSV(){
        Log.d("ENTRA", "ENTRA LECTURACSV?");
        CSVReader reader = null;
        ListView lista = (ListView) findViewById(R.id.lv_items);
        ArrayList<String> casos = new ArrayList<>();
        ArrayAdapter<String> adaptador = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, casos);
        lista.setAdapter(adaptador);
        try {

            //File fileRuta = new File (getFilesDir().getAbsolutePath() + "/parteIncendios.csv");
            FileReader fileReader = new FileReader(archCSV);
            if(fileReader == null ){ mensaje.append("FileReader = vacio"+fileReader.toString());}
            reader = new CSVReader(fileReader);
            if(reader == null ){ mensaje.append("Reader = vacio"+reader.toString());}
            String [] nextLine;
           // assert reader != null;
            if(reader != null){
                Log.d("EXISTE", "READER NO ESTA VACIO");
                nextLine = reader.readNext();
                casos.add(nextLine[0].toString());
                adaptador.setNotifyOnChange(true);
                /*
                while (nextLine[0] != null) {

                    // nextLine[] es la linea entera con los componentes de esta en un array
                    ((TextView) findViewById(R.id.tv_texto)).append("\t");
                    for( int i = 0 ; i < nextLine.length ; i++){

                        //((TextView) findViewById(R.id.tv_texto)).append(nextLine[i] );
                        casos.add(nextLine[1]);
                    }

                    adaptador.setNotifyOnChange(true);
                    nextLine = reader.readNext();
                }
                */
            }



        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    public void descargarCSV(){

        DownloadManager downloadManager;
        long refer;
       // BroadcastReceiver downloadcomplete;
       // BroadcastReceiver notificationClick;

        downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        Uri csvDirUrl = Uri.parse("https://datosabiertos.jcyl.es/web/jcyl/risp/es/medio-ambiente/incendios_forestales/1284333417830.csv");

        DownloadManager.Request request = new DownloadManager.Request(csvDirUrl);
       // request.setTitle("parteIncendios");
       // request.setDescription("Downloading");
       // request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
       // request.setVisibleInDownloadsUi(false);


        //Sí ya existe un archivo anterior es eliminado.
        Log.d("PERRO","EXISTE ES -  "+ archCSV.getAbsolutePath() +"  - = "+archCSV.exists());
        if(archCSV.exists() == true ){
            Log.d("PERRO","Si existe");
            boolean bTemp = archCSV.delete();
        }
        //request.setDestinationUri(Uri.parse(archCSV.getAbsolutePath()));
        //request.setDestinationInExternalFilesDir(Principal.this, getFilesDir().getAbsolutePath(),"parteIncendios.csv");

        Log.d("PERRO", "descargarCSV:  ::  "+  getFilesDir().getAbsolutePath());
        Log.d("PERRO", "descargarCSV:  ::  "+  archCSV.getAbsolutePath());

        request.setDestinationInExternalFilesDir(Principal.this, Environment.getExternalStorageDirectory().getAbsolutePath(),"parteIncendios.csv");
        //getFilesDir().getAbsolutePath()
        //request.setDestinationInExternalPublicDir(Principal.this, getFilesDir().getAbsolutePath(), "parteIncendios.csv");
       // File archivo = new File(getFilesDir().getAbsolutePath(), "parteIncendios.csv");
        long downloadReference = downloadManager.enqueue(request);
        //La renferencia son para movidas mias de pruebas, no son importantes en el desarrollo de la aplicación
        //System.out.print("Pues esta es la referencia : " + downloadReference);

    }


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }
    }


}
