package br.edu.ifsp.scl.ads.pdm.intents

import android.content.Intent
import android.content.Intent.ACTION_DIAL
import android.content.Intent.ACTION_VIEW
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import br.edu.ifsp.scl.ads.pdm.intents.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val amb: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    companion object Constantes {
        const val PARAMETRO_EXTRA = "PARAMETRO_EXTRA"
    }

    private lateinit var parl: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(amb.root)

        setSupportActionBar(amb.toolbarTb)
        supportActionBar?.apply {
            title = getString(R.string.app_name)
            subtitle = this@MainActivity.javaClass.simpleName
        }

        parl = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == RESULT_OK) {
                result.data?.getStringExtra(PARAMETRO_EXTRA)?.let {
                    amb.parametroTv.text = it
                }
            }
        }

        amb.entrarParametroBt.setOnClickListener {
            Intent("MINHA_ACTION_PARA_PROXIMA_TELA").apply {
                amb.parametroTv.text.toString().let{
                    putExtra(PARAMETRO_EXTRA, it)
                }
                parl.launch(this)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item.itemId) {
            R.id.viewMi -> {
                Uri.parse(amb.parametroTv.text.toString()).let {
                    Intent(ACTION_VIEW, it).apply {
                        startActivity(this)
                    }
                }
                true
            }
            R.id.callMi -> { true }
            R.id.dialMi -> {
//                Jeito mais Java de fazer
//                val telUri = Uri.parse("tel: ${amb.parametroTv.text}")
//                val discarIntent = Intent(ACTION_DIAL)
//                discarIntent.setData(telUri)
//                startActivity(discarIntent)

                Uri.parse("tel: ${amb.parametroTv.text}").let {
                    Intent(ACTION_DIAL, it).apply {
                        this.setData(it)
                        startActivity(this)
                    }
                }
                true
            }
            R.id.pickMi -> { true }
            R.id.chooserMi -> { true }
            else -> { false }
        }
    }
}