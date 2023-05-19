package imgconvertt.compressimg.convrtformat.activities

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import imgconvertt.compressimg.convrtformat.R

import imgconvertt.compressimg.convrtformat.databinding.ActivityMainBinding
import imgconvertt.compressimg.convrtformat.enums.Convertor_Enum
import imgconvertt.compressimg.convrtformat.utils.IntentUtils.ENUMVALUE
import imgconvertt.compressimg.convrtformat.utils.RateUsUtils

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    var convertorEnum: Convertor_Enum? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

/*
        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
            setWindowFlag(
                this,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                true
            )
        }
        if (Build.VERSION.SDK_INT >= 19) {
            window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        }

        //make fully Android Transparent Status bar

        //make fully Android Transparent Status bar
        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(
                this,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                false
            )
            window.statusBarColor = Color.TRANSPARENT
        }*/

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window: Window = window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.setStatusBarColor(Color.parseColor("#45AAE2"))
        }

        toolbarSetup()
        getIntentInfo()
        btnClicksInfo()

    }

    fun setWindowFlag(activity: Activity, bits: Int, on: Boolean) {
        val win = activity.window
        val winParams = win.attributes
        if (on) {
            winParams.flags = winParams.flags or bits
        } else {
            winParams.flags = winParams.flags and bits.inv()
        }
        win.attributes = winParams
    }

    private fun btnClicksInfo() {

        binding.ivMenu.setOnClickListener {
            menuClick(it)
        }

        binding.convertformatJpeg.setOnClickListener {
            startActivity(
                Intent(this, RearrangeActivity::class.java).putExtra(
                    ENUMVALUE,
                    Convertor_Enum.CONVERTOR_JPEG
                ).putExtra(resources.getString(R.string.from_rearrange), false)
                    .putExtra(resources.getString(R.string.from_convertformat), true)
                    .putParcelableArrayListExtra(
                        resources.getString(R.string.selected_images),
                        null
                    )
            )
        }

        binding.convertformatBmp.setOnClickListener {
            startActivity(
                Intent(this, RearrangeActivity::class.java).putExtra(
                    ENUMVALUE, Convertor_Enum.CONVERTOR_BMP
                ).putExtra(resources.getString(R.string.from_rearrange), false)
                    .putExtra(resources.getString(R.string.from_convertformat), true)
                    .putParcelableArrayListExtra(
                        resources.getString(R.string.selected_images),
                        null
                    )
            )

        }
        binding.convertformatGif.setOnClickListener {
            startActivity(
                Intent(this, RearrangeActivity::class.java).putExtra(
                    ENUMVALUE, Convertor_Enum.CONVERTOR_GIF
                ).putExtra(resources.getString(R.string.from_rearrange), false)
                    .putExtra(resources.getString(R.string.from_convertformat), true)
                    .putParcelableArrayListExtra(
                        resources.getString(R.string.selected_images),
                        null
                    )
            )

        }

        binding.convertformatPng.setOnClickListener {
            startActivity(
                Intent(this, RearrangeActivity::class.java).putExtra(
                    ENUMVALUE, Convertor_Enum.CONVERTOR_PNG
                ).putExtra(resources.getString(R.string.from_rearrange), false)
                    .putExtra(resources.getString(R.string.from_convertformat), true)
                    .putParcelableArrayListExtra(
                        resources.getString(R.string.selected_images),
                        null
                    )
            )

        }
        binding.convertformatJpg.setOnClickListener {

            startActivity(
                Intent(this, RearrangeActivity::class.java).putExtra(
                    ENUMVALUE, Convertor_Enum.CONVERTOR_JPG
                )
                    .putExtra(resources.getString(R.string.from_rearrange), false)
                    .putExtra(resources.getString(R.string.from_convertformat), true)
                    .putParcelableArrayListExtra(
                        resources.getString(R.string.selected_images),
                        null
                    )
            )

        }
        binding.convertformatWebp.setOnClickListener {
            startActivity(
                Intent(this, RearrangeActivity::class.java).putExtra(
                    ENUMVALUE, Convertor_Enum.CONVERTOR_WEBP
                )
                    .putExtra(resources.getString(R.string.from_rearrange), false)
                    .putExtra(resources.getString(R.string.from_convertformat), true)
                    .putParcelableArrayListExtra(
                        resources.getString(R.string.selected_images),
                        null
                    )
            )
        }
        binding.convertformatPdf.setOnClickListener {
            startActivity(
                Intent(this, RearrangeActivity::class.java).putExtra(
                    ENUMVALUE, Convertor_Enum.CONVERTOR_PDF
                )
                    .putExtra(resources.getString(R.string.from_rearrange), false)
                    .putExtra(resources.getString(R.string.from_convertformat), true)
                    .putParcelableArrayListExtra(
                        resources.getString(R.string.selected_images),
                        null
                    )
            )
        }

        binding.llMyPdf.setOnClickListener {
            startActivity(Intent(this, MyCreationnActivity::class.java))
        }
    }

    private fun getIntentInfo() {

        convertorEnum = intent.getSerializableExtra(ENUMVALUE) as? Convertor_Enum

    }

    private fun toolbarSetup() {

    }

    private fun menuClick(it: View) {
        val popupMenu = PopupMenu(this, it)
        popupMenu.inflate(R.menu.main_menu)
        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.share_app -> try {
                    val ix = Intent(Intent.ACTION_SEND)
                    ix.type = "text/plain"
                    ix.putExtra(
                        Intent.EXTRA_SUBJECT,
                        resources.getString(R.string.app_name)
                    )
                    val link =
                        ("Download " + resources.getString(R.string.app_name) + " app from   - https://play.google.com/store/apps/details?id="
                                + packageName)
                    ix.putExtra(Intent.EXTRA_TEXT, link)
                    startActivity(Intent.createChooser(ix, "Share Application"))
                } catch (e: Exception) {
                }
                R.id.rate_us -> {

                    //   RateUsUtils.initRating(this@MainActivity).check(this@MainActivity, false)

                }
                R.id.privacy_policy -> startActivity(
                    Intent(
                        this, PrivacyPolicyActivity::class.java
                    )
                )
            }
            false
        }
        popupMenu.show()
    }
}