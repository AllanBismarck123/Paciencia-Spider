package com.example.paciencia_spider
import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import kotlin.properties.Delegates

class GameActivity : AppCompatActivity() {
    private lateinit var player: TextView
    private lateinit var reload: ImageButton
    private lateinit var exit: ImageButton
    private lateinit var userName: String
    private lateinit var gameFragment: GameFragment
    private var qtdNaipes by Delegates.notNull<Int>()
    private lateinit var b: Bundle

    override fun onCreate(savedInstanceState: Bundle?) {
        supportActionBar?.hide()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        player = findViewById(R.id.player)
        gameFragment = GameFragment()

        if(intent != null) {
            b = intent.extras!!
            userName = b.getString("userName").toString()
            qtdNaipes = b.getInt("qtdNaipes")
            player.text = userName.uppercase()
        }

        reload = findViewById(R.id.button_reload)

        reload.setOnClickListener {
            showDialog("RELOAD", gameFragment)
        }

        exit = findViewById(R.id.button_exit)

        exit.setOnClickListener {
            showDialog("EXIT", gameFragment)
        }

        loadFragment(gameFragment)
    }


    private fun loadFragment(fragment: Fragment) {
        val fragmentLoad = supportFragmentManager.beginTransaction()
        fragmentLoad.replace(R.id.frameFragment, fragment)
        fragment.arguments = b
        fragmentLoad.commit()
    }

    private fun reloadFragment(fragment: Fragment) {
        val fragmentLoad = supportFragmentManager.beginTransaction()
        fragmentLoad.remove(fragment)
        fragmentLoad.commit()

        gameFragment = GameFragment()
        loadFragment(gameFragment)
    }

    private fun showDialog(type: String, fragment: GameFragment) {
        val configDataBase = ConfigDataBaseUser(this)
        configDataBase.setDefeats(1)

        val builder = AlertDialog.Builder(this)
        builder.setTitle("AVISO")

        builder.setCancelable(false)

        if(type == "RELOAD") {
            builder.setMessage("Tem certeza que deseja reiniciar? Essa partida ser?? marcada como derrota!")
            builder.setPositiveButton("Sim") {
                    _, _ ->
                reloadFragment(fragment)
                Toast.makeText(this, "Jogo reiniciado com sucesso!", Toast.LENGTH_LONG).show()
            }
            builder.setNegativeButton("N??o") {
                    _, _ ->
            }
        } else {
            builder.setMessage("Tem certeza que deseja sair?")
            builder.setPositiveButton("Sim") {
                    _, _ ->
                val intent = Intent(this, FirstActivity::class.java)
                startActivity(intent)
                finish()
                Toast.makeText(this, "Jogo finalizado!", Toast.LENGTH_LONG).show()
            }
            builder.setNegativeButton("N??o") {
                    _, _ ->
            }
        }

        val dialog = builder.create()
        dialog.show()
    }

}