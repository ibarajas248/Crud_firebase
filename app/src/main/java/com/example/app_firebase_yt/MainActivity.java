package com.example.app_firebase_yt;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private EditText txtid, txtnom;
    private Button btnbus, btnmod, btnreg, btneli;
    private ListView lvDatos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtid   = (EditText) findViewById(R.id.txtid);
        txtnom  = (EditText) findViewById(R.id.txtnom);
        btnbus  = (Button)   findViewById(R.id.btnbus);
        btnmod  = (Button)   findViewById(R.id.btnmod);
        btnreg  = (Button)   findViewById(R.id.btnreg);
        btneli  = (Button)   findViewById(R.id.btneli);
        lvDatos = (ListView) findViewById(R.id.lvDatos);



        //llamado a los metodos
        listarLuchadores();
        botonBuscar();
        botonModificar();
        botonRegistrar();
        botonEliminar();


    }//cierra el oncreate




    private void botonBuscar(){
        btnbus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               if (txtid.getText().toString().trim().isEmpty()){
                   ocultarTeclado();
                   Toast.makeText(MainActivity.this, "Digite el ID a buscar", Toast.LENGTH_SHORT).show();
               }else{

                   int id= Integer.parseInt(txtid.getText().toString());

                   FirebaseDatabase db = FirebaseDatabase.getInstance();
                   DatabaseReference dbref = db.getReference(Luchador.class.getSimpleName());
                   //DatabaseReference dbref= db.getReference().child("luchador");

                   dbref.addListenerForSingleValueEvent(new ValueEventListener() {
                       @Override
                       public void onDataChange(@NonNull DataSnapshot snapshot) {

                           String aux = Integer.toString(id);
                           boolean res = false;

                           for(DataSnapshot x: snapshot.getChildren()){

                               //comparando el dato ingresado con los id de firebase
                               if(aux.equalsIgnoreCase(x.child("id").getValue().toString())) { //id atributo en firebase
                                   res = true;
                                   ocultarTeclado();
                                   txtnom.setText(x.child("nombre").getValue().toString());
                                   break;
                               }

                           }
                           if ( res == false){
                               ocultarTeclado();
                               Toast.makeText(MainActivity.this, "ID ("+aux+") no encontrado!!", Toast.LENGTH_SHORT).show();
                           }
                       }


                       @Override
                       public void onCancelled(@NonNull DatabaseError error) {

                       }
                   });


               }// cierra el if/else  inicial.
            }
        });


    }// cierra el método botonBuscar.



    private void botonModificar(){}





    // Método para registrar información en Firebase al hacer clic en el botón.

    private void botonRegistrar(){
        btnreg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Verificar si los campos de ID y nombre están vacíos

                if (txtid.getText().toString().trim().isEmpty()   //trim elimina los espacios en blanco al principio y al final

                        ||txtnom.getText().toString().trim().isEmpty()){
                    ocultarTeclado(); // Ocultar el teclado virtual.

                    Toast.makeText(MainActivity.this, "complete los campos faltantes", Toast.LENGTH_SHORT).show();
                }else{
                    // Obtener los valores de ID y nombre desde los campos de entrada.

                    int id = Integer.parseInt(txtid.getText().toString());
                    String nom = txtnom.getText().toString();

                    // me conecto a la base de datos
                    FirebaseDatabase db = FirebaseDatabase.getInstance().getInstance();

                    // Obtener una referencia al contenedor de datos (tabla) llamado "Luchador".

                    DatabaseReference dbref= db.getReference(Luchador.class.getSimpleName());

                    // Escuchar una sola vez para verificar la existencia de ID y nombre en la base de datos.
                    dbref.addListenerForSingleValueEvent(new ValueEventListener() {

                    /*
                    * --------Explicacion de  public void onDataChange(@NonNull DataSnapshot snapshot) {
                    *
                    * --dbref es una referencia a un nodo o contenedor en la base de datos Firebase, específicamente al nodo "Luchador".
                    * --addListenerForSingleValueEvent agrega un evento de escucha que se ejecutará una sola vez. Esto significa que,
                    * cuando se active este evento, verificará los datos en el nodo "Luchador" una vez y luego dejará de escuchar cambios.
                    * --new ValueEventListener() crea un nuevo objeto que implementa la interfaz
                    *
                    * ValueEventListener. Esta interfaz define dos métodos que deben ser implementados:
                     * */
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            //valida si el id no se encuentra ya insertado

                            String aux = Integer.toString(id); //convierte el numero e  un String
                            boolean res = false;
                            // Verificar si el ID ya existe en la base de datos.

                            for(DataSnapshot x: snapshot.getChildren()){ //los children se refieren a los hijops de un padre
                                if (x.child("id").getValue().toString().equalsIgnoreCase(aux)){
                                    res= true;
                                    ocultarTeclado();
                                    Toast.makeText(MainActivity.this, "error, el id ("+aux+")ya existe", Toast.LENGTH_SHORT).show();
                                    break;
                                }
                            }

                            boolean res2 = false;
                            for(DataSnapshot x: snapshot.getChildren()){ //los children se refieren a los hijops de un padre
                                if (x.child("nombre").getValue().toString().equalsIgnoreCase(nom)){
                                    res2= true;
                                    ocultarTeclado();
                                    Toast.makeText(MainActivity.this, "error, el nombre ("+aux+")ya existe", Toast.LENGTH_SHORT).show();
                                    break;
                                }
                            }

                            if(res ==false && res2==false){ // solo cuando el id y el nombre es unico, entonces se podrá hacer la inserción.
                                // se hace la inserción
                                Luchador luc = new Luchador(id, nom);
                                dbref.push().setValue(luc);
                                ocultarTeclado();
                                Toast.makeText(MainActivity.this, "Luchador registrado correctamente", Toast.LENGTH_SHORT).show();
                                txtid.setText("");
                                txtnom.setText("");
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                            // Manejar errores de Firebase si es necesario.


                        }
                    });

                }//cierra el if/else inicial
            }
        });
    }//cierre del metodo moton registrar

    private void listarLuchadores(){

        //se conecta a la base de datos.
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        //instancia a la base de datos Luchador referencia

        DatabaseReference dbref = db.getReference(Luchador.class.getSimpleName());

        // Se crea una lista de luchadores (ArrayList).
        ArrayList <Luchador> lisluc = new ArrayList <Luchador> ();

        // Se crea un ArrayAdapter para mostrar la lista en una vista ListView.

        ArrayAdapter <Luchador> ada =new ArrayAdapter<Luchador>(MainActivity.this, android.R.layout.simple_list_item_1,lisluc);
        // Se asigna el ArrayAdapter a un ListView (lvDatos).
        lvDatos.setAdapter(ada);

        // Se agrega un ChildEventListener para escuchar los cambios en los datos de la base de datos.

        dbref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                // Cuando se agrega un nuevo luchador a la base de datos, este método se ejecuta.
                // Se obtiene el luchador desde la base de datos.
                Luchador luc = snapshot.getValue(Luchador.class);
                // Se agrega el luchador a la lista.
                lisluc.add(luc);
                // Se notifica al ArrayAdapter que los datos han cambiado para actualizar la vista.

                ada.notifyDataSetChanged();

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                // Cuando un luchador existente en la base de datos se actualiza, este método se ejecuta.
                // Se notifica al ArrayAdapter para actualizar la vista.
                ada.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                // Este método se ejecutaría si un luchador se elimina de la base de datos, pero aquí no se realiza ninguna acción.

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                // Este método se ejecutaría si se mueve un luchador en la base de datos, pero aquí no se realiza ninguna acción.

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Manejo de errores en caso de que ocurra un error en la operación de lectura de datos.


            }
        });
        // Se configura un listener para el evento de clic en un elemento de la lista.

        lvDatos.setOnItemClickListener(new AdapterView.OnItemClickListener(){


            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l){
                // Cuando se hace clic en un elemento de la lista, se obtiene el luchador seleccionado.

                Luchador luc = lisluc.get(i);
                // Se crea un cuadro de diálogo emergente para mostrar información sobre el luchador.

                AlertDialog.Builder a = new AlertDialog.Builder(MainActivity.this);
                a.setCancelable(true);
                a.setTitle("Luchador Seleccionado");
                String msg = "ID : "+luc.getId()+"\n\n";
                msg += "NOMBRE: "+ luc.getNombre();



                a.setMessage(msg);
                a.show();
            }
        });

    }//cierre del metodo listar luchadores





    private void botonEliminar(){}





    private void ocultarTeclado(){
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    } // Cierra el método ocultarTeclado.


}//cierra la clase