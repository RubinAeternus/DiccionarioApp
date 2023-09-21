package com.example.diccionarioapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.opencsv.CSVWriter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;


public class MainActivity extends AppCompatActivity {

    GridView grid;
    ArrayList palabras = new ArrayList();
    ArrayList descripciones = new ArrayList();
    TextView elemento;
    Button btnAgreg;
    Spinner spin;
    SearchView sv;
    TextView txt1;
    private MediaRecorder mediaRecorder;
    private MediaPlayer mediaPlayer;
    private String ruta = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator;
    private String fichero;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnAgreg = findViewById(R.id.btnAgregar);

        sv = (SearchView) findViewById(R.id.svPalabra);
        inicializar();
        Collections.sort(palabras);


        //ADAPTADOR DE GRIDVIEW
        grid = findViewById(R.id.Grd);
        adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, palabras);
       ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, descripciones);
        grid.setAdapter(adapter);



        //ADAPTADOR DE SPINNER



        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                adapter.getFilter().filter(s);
                grid.invalidateViews();
                return false;
            }
        });



        //CLICK LISTENER DE ELEMENTOS
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String opcion = adapterView.getItemAtPosition(i).toString();
                int posi = palabras.indexOf(opcion);
                String descripcion = descripciones.get(posi)+"";
                int posicion = i;

                //Muestra ventana emergente
                mostrarVentanaEmergente(opcion, descripcion, posicion);
                //Toast.makeText(MainActivity.this, ""+posicion, Toast.LENGTH_SHORT).show();

                //Prueba de Funcionamiento
                // Toast.makeText(getApplicationContext(), opcion, Toast.LENGTH_SHORT).show();
            }
        });


        Intent intent = new Intent( this, MainActivity.class );


    }

    public void inicializar (){
        palabras.add("Casa");
        palabras.add("Teclado");
        palabras.add("Oceano");
        ///////////////////////
        descripciones.add("Edificación destinada para ser habitada. Puede organizarse en una o varias plantas");
        descripciones.add("Masa de agua que compone gran parte de la hidrósfera de un cuerpo celeste");
        descripciones.add("Dispositivo de entrada, en parte inspirado en el teclado de las máquinas de escribir, que utiliza un sistema de puntadas o márgenes, para que actúen como palancas mecánicas o interruptores electrónicos que envían toda la información a la computadora");

    }

    public void agregar (String palabra, String desc){

        palabras.add(palabra);
        Collections.sort(palabras);
        int posicion = palabras.indexOf(palabra);
        descripciones.add(posicion, desc);
        grid = findViewById(R.id.Grd);



        grid.invalidateViews();
    }

    public void eliminar (View v){
        int opcion = spin.getSelectedItemPosition();
        palabras.remove(opcion);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, palabras);
        grid.setAdapter(adapter);

        ArrayAdapter<String> adaptador = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, palabras);
        spin.setAdapter(adaptador);

        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                adapter.getFilter().filter(s);
                adaptador.getFilter().filter(s);
                return false;
            }
        });

        grid.invalidateViews();

    }
    public void Acercade(View v) {
        // Código para mostrar la ventana emergente
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Acerca de...");
        builder.setMessage("Creado por: Andrés Alfonso Rubin de la Cruz. 17130839");
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Acciones al hacer clic en el botón Aceptar
                dialog.dismiss();
            }
        });

        builder.show();


    }

    private void mostrarVentanaEmergente(String opcion, String descr, int position) {
        // Código para mostrar la ventana emergente
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(opcion);
        builder.setMessage(descr);
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Acciones al hacer clic en el botón Aceptar
                dialog.dismiss();
            }
        });

        builder.setNeutralButton("Editar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Acciones al hacer clic en el botón Editar

                mostrarVentanaEditarElemento(descr, position);

                dialog.dismiss();
            }
        });

        builder.setNegativeButton("Eliminar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Acciones al hacer clic en el botón Eliminar
                palabras.remove(opcion);
                descripciones.remove(descr);
                //sv.setQuery("", false);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, palabras);
                grid.setAdapter(adapter);

                sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String s) {
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String s) {
                        adapter.getFilter().filter(s);

                        return false;
                    }
                });


                dialog.dismiss();
                grid.invalidateViews();
            }
        });
        builder.show();


    }

    public void mostrarVentanaAgregarElemento(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Agregar elemento");

        // Crear el diseño de la ventana emergente
        View viewInflated = LayoutInflater.from(this).inflate(R.layout.activity_agregar, null);

        // Obtener las referencias de los elementos del diseño
        final EditText editTextTitulo = viewInflated.findViewById(R.id.editTextTitulo);
        final EditText editTextDescripcion = viewInflated.findViewById(R.id.editTextDescripcion);

        builder.setView(viewInflated);

        builder.setPositiveButton("Agregar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Obtener los valores ingresados por el usuario
                String titulo = editTextTitulo.getText().toString();
                String descripcion = editTextDescripcion.getText().toString();
                if (!titulo.isEmpty() && !descripcion.isEmpty()) {

                    // Crear el nuevo elemento con los valores ingresados



                    // Agregar el nuevo elemento al GridView
                    agregar(titulo, descripcion);
                    dialog.dismiss();
                }
                else{
                    Toast.makeText(MainActivity.this, "No puede dejar campos vacios", Toast.LENGTH_SHORT).show();
                    mostrarVentanaAgregarElemento(view);
                }
            }
        });

        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();

    }

    private void mostrarVentanaEditarElemento(String desc, int pos) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Editar elemento");

        View viewInflated = LayoutInflater.from(this).inflate(R.layout.activity_editar, null);
        builder.setView(viewInflated);

         Button   grabar = viewInflated.findViewById(R.id.btnGrabar);
         Button   detener = viewInflated.findViewById(R.id.btnDetener);
         Button   reproducir = viewInflated.findViewById(R.id.btnReproducir);

        //final EditText editTextTitulo = viewInflated.findViewById(R.id.editTextTitulo);
        final EditText editTextDescripcion = viewInflated.findViewById(R.id.editDescripcion);

        // Configura los campos con los valores del elemento seleccionado
       // editTextTitulo.setText(item.getTitulo());
        editTextDescripcion.setText(descripciones.get(pos)+"");

        grabar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fichero = ruta + palabras.get(pos)+"" + ".3gp";

                mediaRecorder = new MediaRecorder();

                mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);

                mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);

                mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_WB);

                mediaRecorder.setOutputFile(fichero);

                try {

                    grabar.setEnabled(false);
                    detener.setEnabled(true);
                    reproducir.setEnabled(false);

                    mediaRecorder.prepare();
                    mediaRecorder.start();
                }catch (IOException ex){
                    Toast.makeText(MainActivity.this, ex+"", Toast.LENGTH_SHORT).show();
                    grabar.setEnabled(true);
                    detener.setEnabled(false);
                    reproducir.setEnabled(false);


                }

            }
        });

        detener.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                grabar.setEnabled(true);
                detener.setEnabled(false);
                reproducir.setEnabled(true);
                mediaRecorder.stop();
                mediaRecorder.release();
            }
        });

        reproducir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer = new MediaPlayer();
                try {
                    mediaPlayer.setDataSource(fichero);
                    mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mediaPlayer) {
                            grabar.setEnabled(false);
                            detener.setEnabled(false);
                            reproducir.setEnabled(false);
                            mediaPlayer.start();

                        }
                    });
                    mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mediaPlayer) {

                            grabar.setEnabled(true);
                            detener.setEnabled(false);
                            reproducir.setEnabled(true);

                            mediaPlayer.release();
                        }
                    });
                }catch (IOException ex){

                    grabar.setEnabled(true);
                    detener.setEnabled(false);
                    reproducir.setEnabled(false);

                    Toast.makeText(MainActivity.this, ex+"", Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Guarda los cambios en el elemento
                //item.setTitulo(editTextTitulo.getText().toString());
                //item.setDescripcion(editTextDescripcion.getText().toString());
                descripciones.set(pos, editTextDescripcion.getText().toString());

                // Actualiza la vista del elemento en el GridView

            }
        });

        builder.setNeutralButton("Enviar SMS", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                enviarSMS(pos);
                // Acciones al hacer clic en el botón Editar
               /* SmsManager smsMgr = SmsManager.getDefault();
                String mensaje = (palabras.get(pos)+"");
                smsMgr.sendTextMessage("8718359676", null,
                        mensaje, null, null);

                Toast.makeText(MainActivity.this, "SMS Enviado" + mensaje, Toast.LENGTH_SHORT).show();*/


            }
        });



        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    public void exportarElementosCSV(View v) {
        File almacenamientoCompartido = getExternalFilesDir(null);
        String rutaAlmacenamientoCompartido = almacenamientoCompartido.getAbsolutePath();
        String csvFilePath = rutaAlmacenamientoCompartido + "/elementos.txt";

        try {
            File csvFile = new File(csvFilePath);
            csvFile.createNewFile();

            CSVWriter csvWriter = new CSVWriter(new FileWriter(csvFile));

            // Escribir los encabezados en el archivo CSV
            String[] encabezados = {"Título", "Descripción"};
            csvWriter.writeNext(encabezados);

            // Escribir cada elemento en el archivo CSV
            for ( int i = 0; i<palabras.size(); i++) {
                String[] fila = {palabras.get(i)+"", descripciones.get(i)+""};
                csvWriter.writeNext(fila);
            }

            csvWriter.close();

            // Notificar al usuario que la exportación se completó con éxito
            if (csvFile.exists()) {
                // Notificar al usuario que la exportación se completó con éxito
                Toast.makeText(this, "Elementos exportados a CSV", Toast.LENGTH_SHORT).show();
            } else {
                // Manejar el caso en el que el archivo no se haya creado correctamente
                Toast.makeText(this, "Error al exportar elementos a CSV", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, ""+e, Toast.LENGTH_SHORT).show();
            // Manejar cualquier error al escribir en el archivo
        }
    }

    private void enviarSMS(int pos){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enviar SMS");

        View viewInflated = LayoutInflater.from(this).inflate(R.layout.activity_sms, null);
        builder.setView(viewInflated);




        builder.setPositiveButton("Enviar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                EditText edit = viewInflated.findViewById(R.id.editTextNumero);
                String numero = edit.getText().toString();
                SmsManager smsMgr = SmsManager.getDefault();
                String mensaje = (palabras.get(pos)+"");
                smsMgr.sendTextMessage(numero, null,
                        mensaje, null, null);

                Toast.makeText(MainActivity.this, "SMS Enviado", Toast.LENGTH_SHORT).show();
                dialog.dismiss();

            }
        });

        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    public void enviarCorreo(View v){
        String[] TO = {"andresru99@outlook.com"}; // Direcciones de correo electrónico de los destinatarios
        String[] CC = {"copia@example.com"}; // Direcciones de correo electrónico en copia
        String asunto = "Asunto del correo";
        String cuerpo = "Cuerpo del correo";

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("message/rfc822");
        intent.setData(Uri.parse("mailto:")); // Establecer el URI para enviar el correo electrónico
        intent.putExtra(Intent.EXTRA_EMAIL, TO); // Destinatarios del correo electrónico
        intent.putExtra(Intent.EXTRA_CC, CC); // Copia del correo electrónico
        intent.putExtra(Intent.EXTRA_SUBJECT, asunto); // Asunto del correo electrónico
        intent.putExtra(Intent.EXTRA_TEXT, cuerpo); // Cuerpo del correo electrónico

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(Intent.createChooser(intent, "Enviar correo electrónico"));
        } else {
            // Mostrar un mensaje o tomar otra acción si no hay una aplicación de correo electrónico disponible
            Toast.makeText(MainActivity.this, "No hay aplicación de correo electrónico disponible", Toast.LENGTH_SHORT).show();
        }


    }

    public void importar(View v){

        ArrayList<String> conceptos = new ArrayList<>();

        try {
            //File directorio = getExternalFilesDir();
            String rutaArchivo = getExternalFilesDir(null) + File.separator + "diccionario.txt";
            File archivo = new File(rutaArchivo);
            BufferedReader br = new BufferedReader(new FileReader(archivo));

            String linea;
            while ((linea = br.readLine()) != null) {
                // Dividir la línea en campos utilizando el carácter barra vertical
                String[] campos = linea.split("\\|");

                // Obtener el campo correspondiente al concepto (puede variar según la estructura del archivo)
                if (campos.length > 0) {
                    String concepto = campos[0];
                    String desci = campos[1];
                    palabras.add(concepto);
                    Collections.sort(palabras);
                    int posicion = palabras.indexOf(concepto);
                    descripciones.add(posicion, desci);

                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, palabras);
                    grid.setAdapter(adapter);
                    grid.invalidateViews();

                }
            }

            br.close();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, e+"", Toast.LENGTH_SHORT).show();
        }


    }


    }




