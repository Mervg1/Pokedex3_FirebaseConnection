package itesm.com.pokedex3

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.database.*

data class Pokemon(val name: String?, val type:String?){
    constructor() : this(" ", "")

}

class MainActivity : AppCompatActivity() {
    // Incluye las variables de Analytics:
    private lateinit var analytics: FirebaseAnalytics
    private lateinit var bundle: Bundle

    private lateinit var database : FirebaseDatabase
    private lateinit var reference : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        database = FirebaseDatabase.getInstance()
        reference = database.getReference("pokemons")

        //inicializa las variables:
        analytics = FirebaseAnalytics.getInstance(this)
        bundle = Bundle()

    }

    public fun addPokemon(view: View){

        // Evento hacia analytics
        bundle.putString("edu_itesm_pokedex_main", "added_pokemon")
        analytics.logEvent("main",bundle)

        val nombre = findViewById<EditText>(R.id.nombre).text
        val tipo = findViewById<EditText>(R.id.tipo).text
        if(nombre.isNotEmpty() && nombre.isNotBlank() && tipo.isNotEmpty() && tipo.isNotBlank()){
            val pokemon = Pokemon(nombre.toString(), tipo.toString())
            val id = reference.push().key
            reference.child(id!!).setValue(pokemon)
        }else{
            Toast.makeText(applicationContext, "error", Toast.LENGTH_LONG).show()
        }


    }

    public fun getPokemon(view: View){
        reference.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var listaPokemon = ArrayList<Pokemon>()
                for(pokemon in snapshot.children){
                    var objeto = pokemon.getValue(Pokemon::class.java)
                    listaPokemon.add(objeto as Pokemon)
                }

                Log.i("pokemons", listaPokemon.toString())


            }

            override fun onCancelled(error: DatabaseError) {

            }


        })
    }

}