package app.examen.ejercicio23grupo4;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import app.examen.ejercicio23grupo4.configuracion.BaseDatos;
import app.examen.ejercicio23grupo4.configuracion.TransaccionesRegistro;

public class MainActivity extends AppCompatActivity {


    //RecyclerView
    private RecyclerView registrosRv;

    //DB Helper
    private BaseDatos dbHelper;

    //Action Bar
    ActionBar actionBar;
    //Ordenar Opciones
    String ordenarByNewest = TransaccionesRegistro.C_ADDED_TIMESTAMP + " DESC";
    String ordenarByoldest = TransaccionesRegistro.C_ADDED_TIMESTAMP + " ASC";
    String ordenarTitleASC = TransaccionesRegistro.C_DESCRIPCION + " ASC";
    String ordenarTitleDESC = TransaccionesRegistro.C_DESCRIPCION + " DESC";

    //Para actualizar Registros, actualiza con la ultima opcion de ordenacion Elegida
    String currentOrderByStatus = ordenarByNewest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button addfoto=(Button) findViewById(R.id.btnAddFoto);

        registrosRv = findViewById(R.id.RegistrosRv);
        //Inicializamos db helper Clase
        dbHelper = new BaseDatos(this);

        //Inicializacion ActionBar
        actionBar = getSupportActionBar();
        actionBar.setTitle("Registros");

        // Cargando Registros
        cargarRegistros(ordenarByNewest);

        addfoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, GuardarPhotograhActivity.class);
                intent.putExtra("isEditMode", false);//desea establecer nuevos datos, set false.
                startActivity(intent);
            }
        });
    }
    private void cargarRegistros(String orderBy){
        currentOrderByStatus=orderBy;

        AdapterRegistro adapterRegistro = new AdapterRegistro(MainActivity.this,
                dbHelper.getAllRegistros(orderBy));

        registrosRv.setAdapter(adapterRegistro);

        //Establecer el numero de Registros
        actionBar.setSubtitle("Total: "+dbHelper.getRecordsCount());
    }

    @Override
    protected void onResume(){
        super.onResume();
        cargarRegistros(currentOrderByStatus);// Refresca o actualiza la lista de registros
    }

    private void searchRecords(String query){
        AdapterRegistro adapterRegistro = new AdapterRegistro(MainActivity.this,
                dbHelper.buscarRegistros(query));

        registrosRv.setAdapter(adapterRegistro);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        //inflate menu
        getMenuInflater().inflate(R.menu.menu_main, menu);

        //searchView
        MenuItem item = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView)item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // buscar cuando se hace clic en el botón de búsqueda en el teclado
                searchRecords(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // busca mientras escribes
                searchRecords(newText);
                return true;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        //maneja elementos del menu

        int id = item.getItemId();
        if(id==R.id.action_sort){
            //Mostrar Opciones de orden
            sortOptioDialog();
        }else if(id==R.id.action_delete_all){
            //Elimina todos los datos
            dbHelper.deleteAllData();
            onResume();
        }

        return super.onOptionsItemSelected(item);
    }

    private void sortOptioDialog() {
        //Opciones para mostrar en el dialog
        String[] opciones={"Descripcion Ascendente", "Descripcion Descendente", "El mas nuevo", "El mas antiguo"};
        AlertDialog.Builder builder= new AlertDialog.Builder(this);
        builder.setTitle("Ordenar Por").setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
               if(i== 0){
                   cargarRegistros(ordenarTitleASC);
               }
               if(i==1){
                   cargarRegistros(ordenarTitleDESC);

               }
               if(i==2){
                   cargarRegistros(ordenarByNewest);

               }
               if(i==3){
                   cargarRegistros(ordenarByoldest);

               }

            }
        }).create().show();//show Dialog

    }
}