package br.edu.ifsp.scl.ads.pdm.intents

import android.content.Intent
import android.content.Intent.ACTION_CALL
import android.content.Intent.ACTION_DIAL
import android.content.Intent.ACTION_VIEW
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
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
    private lateinit var pcarl: ActivityResultLauncher<String>

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

        pcarl = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            permissaoConcedida ->
            if (permissaoConcedida) {
                // Chamar a chamada
            }
            else {
                Toast.makeText(this, "Permissāo necessária!", Toast.LENGTH_SHORT).show()
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

            R.id.callMi -> {
                Uri.parse("tel: ${amb.parametroTv.text}").let {
                    Intent(ACTION_CALL, it).apply {
                        data = it
                        startActivity(this)
                    }
                }
                true
            }

            R.id.dialMi -> {
                Uri.parse("tel: ${amb.parametroTv.text}").let {
                    Intent(ACTION_DIAL, it).apply {
                        data = it
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

    private fun chamarOuDiscar(chamar: Boolean) {
        Uri.parse("tel: ${amb.parametroTv.text}").let {
            Intent(if (chamar) ACTION_CALL else ACTION_DIAL, it).apply {
                data = it
                startActivity(this)
            }
        }
    }
}