package com.example.techorbit.ui.activity

import android.content.Intent
import android.content.res.Resources
import android.graphics.*
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidmads.library.qrgenearator.QRGContents
import androidmads.library.qrgenearator.QRGEncoder
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.drawToBitmap
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupWithNavController
import com.example.techorbit.R
import com.example.techorbit.model.error.Error
import com.example.techorbit.model.signup.SignupData
import com.example.techorbit.services.ResultOf
import com.example.techorbit.utils.*
import com.example.techorbit.viewmodel.HomeViewmodel
import com.google.android.material.card.MaterialCardView
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar.*
import kotlinx.android.synthetic.main.app_bar.settings
import org.koin.android.viewmodel.ext.android.viewModel
import java.io.ByteArrayOutputStream
import java.lang.RuntimeException
import java.nio.charset.Charset
import android.graphics.drawable.Drawable
import android.graphics.Bitmap.CompressFormat

import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import android.graphics.BitmapFactory

import android.graphics.Bitmap
import android.os.Environment
import android.text.format.DateFormat
import android.util.Log
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.random.Random


class HomeActivity : AppCompatActivity() {

    private var doubleBackToExitPressedOnce = false
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    var id = 0

    private val viewmodel: HomeViewmodel by viewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setData()

//        wallet.text = TechorbitSharedPreference(this).getValue(KEYS.BALANCE) + " AED"

        navController = findNavController(R.id.nav_host_fragment)
        bottom_nav_view.setupWithNavController(navController)

        appBarConfiguration = AppBarConfiguration(setOf(R.id.homeFragment), drawer_layout)

        nav_view.setupWithNavController(navController)

        val nav_menu = nav_view.menu
        val logout: MenuItem = nav_menu.findItem(R.id.logout)
        logout.setOnMenuItemClickListener { menuItem ->

            val username = TechorbitSharedPreference(this).getValue(KEYS.USERNAME)
            val intent = Intent(this, SignUpActivity::class.java)
            intent.putExtra("username", username)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

            startActivity(intent)
            TechorbitSharedPreference(this).removeAll()
            false
        }



        notification.setOnClickListener {
            navController.navigate(R.id.notificationFragment)
        }
        wallet.setOnClickListener {
            showToast("Fetching balance")
            setData()
        }

        refresh_bt.setOnClickListener {

            refreshToken()
        }




        drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)


        menu.setOnClickListener {
            when {

                drawer_layout.isDrawerOpen(Gravity.RIGHT) -> {
                    drawer_layout.closeDrawer(Gravity.RIGHT)
                    drawer_layout.openDrawer(Gravity.LEFT)
                }
                drawer_layout.isDrawerOpen(Gravity.LEFT) -> {
                    drawer_layout.closeDrawer(Gravity.LEFT)
                }
                else -> drawer_layout.openDrawer(Gravity.LEFT)

            }
        }
        settings.setOnClickListener {

            nav_view_right.menu.clear()
            nav_view_right.inflateMenu(R.menu.settings_menu)
            when {
                drawer_layout.isDrawerOpen(Gravity.LEFT) -> {
                    drawer_layout.closeDrawer(Gravity.LEFT)
                    drawer_layout.openDrawer(Gravity.RIGHT)
                }
                drawer_layout.isDrawerOpen(Gravity.RIGHT) -> {
                    drawer_layout.closeDrawer(Gravity.RIGHT)
                }
                else -> drawer_layout.openDrawer(Gravity.RIGHT)

            }
        }


        val report = bottom_nav_view.menu.findItem(R.id.report)
        report.setOnMenuItemClickListener {
            nav_view_right.menu.clear()
            nav_view_right.inflateMenu(R.menu.report_menu)


            when {
                drawer_layout.isDrawerOpen(Gravity.LEFT) -> {
                    drawer_layout.closeDrawer(Gravity.LEFT)
                    drawer_layout.openDrawer(Gravity.RIGHT)
                }
                drawer_layout.isDrawerOpen(Gravity.RIGHT) -> {
                    drawer_layout.closeDrawer(Gravity.RIGHT)
                }
                else -> drawer_layout.openDrawer(Gravity.RIGHT)

            }
            true

        }
        navigtionItemSelected()


    }

    fun dateConversion(input: String): String? {
        var output = ""
        if (!input.isEmpty()) {
            try {
                var date = input.replace("T"," ")
                var format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)
                val newDate = format.parse(date)
                format = SimpleDateFormat("dd MMM yyyy HH:mm:ss", Locale.ENGLISH)
                output = format.format(newDate)
            } catch (e: ParseException) {
                e.printStackTrace()
            }
        }
        return output
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return item.onNavDestinationSelected(navController) ||
                super.onOptionsItemSelected(item)
    }

    //open drawer when drawer icon clicked and back btn pressed
//    override fun onSupportNavigateUp(): Boolean {
//        return findNavController(R.id.nav_host_fragment).navigateUp(appBarConfiguration)
//    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(Gravity.LEFT) || drawer_layout.isDrawerOpen(Gravity.RIGHT)) {
            drawer_layout.closeDrawer(Gravity.LEFT);
            drawer_layout.closeDrawer(Gravity.RIGHT);
        } else {
            if (navController.currentDestination?.id == navController.graph.startDestination) {
                if (doubleBackToExitPressedOnce) super.onBackPressed();
                else onHandlerStart();
            } else super.onBackPressed();

        }
    }

    private fun deleteAppData() {
        val packagename = applicationContext.packageName
        val runtime = Runtime.getRuntime()
        runtime.exec("pm clear $packageName")
    }

    fun setData() {

        val header: String? = "Bearer " + TechorbitSharedPreference(this).getValue(KEYS.TOKEN)
        viewmodel.getWallet(header!!)

        viewmodel.liveData.observe(this, Observer {
            when (it) {
                is ResultOf.Succes -> {

                    TechorbitSharedPreference(this).save(KEYS.BALANCE, it.result.balance.toString())
                    wallet.text = TechorbitSharedPreference(this).getValue(KEYS.BALANCE)
                }
                is ResultOf.Failiure -> {
                    showToast("Something went wrong! check your connection")
                }
            }
        })


    }

    private fun onHandlerStart() {
        doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show();
        Handler(mainLooper).postDelayed(Runnable {
            doubleBackToExitPressedOnce = false
        }, 2000)
    }

    fun navigtionItemSelected() {
        nav_view_right.setNavigationItemSelectedListener {
            selectDrawerItem(it)
            true
        }
    }

    fun selectDrawerItem(menuItem: MenuItem) {
        drawer_layout.closeDrawers()
        when (menuItem.itemId) {
            R.id.wallet_report -> navController.navigate(R.id.walletReportFragment)
//            R.id.transaction_report -> navController.navigate(R.id.transactionFragment)
            R.id.terminal_sales_report -> navController.navigate(R.id.terminalsReportFragment)
//            R.id.merchant_sales_report -> navController.navigate(R.id.merchantReportFragment)
            R.id.purchase_report -> navController.navigate(R.id.purchaseReportFragment)
//            R.id.reciept_report -> navController.navigate(R.id.receiptReportFragment)
            R.id.vendor_outstanding_report -> navController.navigate(R.id.vendorOutstandingFragment)

            R.id.inventory_report -> navController.navigate(R.id.inventoryReportFragment)

            R.id.reciepet_report -> navController.navigate(R.id.receiptReportFragment)

            R.id.bluetoothsettings -> {
                startActivity(Intent(Settings.ACTION_BLUETOOTH_SETTINGS))
            }

            R.id.testprint -> {

                PrinterFunction(this).call_device_detail_printer_bluetooth()
//                startActivity(Intent(this,ScanActivity::class.java))
//                logo_print()

//                createImageFromString("Nithin  Joseph")


            }

            R.id.recharge_terminal_report -> {
                navController.navigate(R.id.rechargeTerminalReportFragment)
            }

            R.id.clean_data -> {
                deleteAppData()
            }
            else -> {
                Toast.makeText(this, "false", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun refreshToken() {
        val username = TechorbitSharedPreference(this).getValue(KEYS.USER)
        val password = TechorbitSharedPreference(this).getValue(KEYS.PASS)
        val deviceid = TechorbitSharedPreference(this).getValue(KEYS.DEVICEID)

        Toast.makeText(this, "Refreshing....", Toast.LENGTH_LONG).show()
        viewmodel.signup(username, password, deviceid)
        viewmodel.data.observe(this, Observer {
            when (it) {

                is ResultOf.Succes -> {
                    Toast.makeText(this, "Refresh success....", Toast.LENGTH_SHORT).show()

                    saveData(it)
                }
                is ResultOf.Failiure -> {
                    Toast.makeText(this, "Refresh Failed....", Toast.LENGTH_SHORT).show()
                    showToast("${it.errorCode}")

                    if (it.errorCode == 400) {
                        it.errorBody?.let { errorbody ->
                            val body = errorbody
                            val source = body?.source()
                            val buffer = source?.buffer
                            val responcestring =
                                buffer?.clone()?.readString(Charset.forName("UTF-8"))
                            val gson = Gson().fromJson(responcestring, Error::class.java)
                            showToast(gson.error_message!!)
                        }

                    }
                    if (it.isNetworkError) showToast("No internet")

                }
            }
        })

    }

    private fun saveData(it: ResultOf.Succes<SignupData>) {
        TechorbitSharedPreference(this).save(KEYS.TOKEN, it.result.token)
    }

    private fun logo_print() {

        val pathName = "/storage/emulated/0/AppFiles/print.jpg"
        var width = 0
        val level = 50
        val opts = BitmapFactory.Options()
        opts.inJustDecodeBounds = false
        opts.inSampleSize = 1
        opts.inPreferredConfig = Bitmap.Config.RGB_565
        val bitmap = BitmapFactory.decodeFile(pathName, opts)
        try {
                val bmp = BitmapFactory.decodeResource(resources,R.drawable.free)
            if (bmp != null) {
                val command: ByteArray = Utils.decodeBitmap(bmp)

                BluetoothUtil.logoPrint(command)
            } else {
            }
        } catch (e: Exception) {
        }



    }





    private fun viewToBitmap() {
        val view = LayoutInflater.from(this).inflate(R.layout.fragment_profile, null, false)
        val constraintLayout = view.findViewById<MaterialCardView>(R.id.materialCardView)
        val bitmap = createBitmapFromView(constraintLayout, 300, 300)
        val command = Utils.decodeBitmap(bitmap)
        BluetoothUtil.logoPrint(command)


    }

    fun createBitmapFromView(view: View, width: Int, height: Int): Bitmap {
        if (width > 0 && height > 0) {
            view.measure(
                View.MeasureSpec.makeMeasureSpec(
                    convertDpToPixels(width), View.MeasureSpec.EXACTLY
                ),
                View.MeasureSpec.makeMeasureSpec(
                    convertDpToPixels(height), View.MeasureSpec.EXACTLY
                )
            )
        }
        view.layout(0, 0, view.measuredWidth, view.measuredHeight)
        val bitmap = Bitmap.createBitmap(
            view.measuredWidth,
            view.measuredHeight, Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        val background = view.background
        background?.draw(canvas)
        view.draw(canvas)
        return bitmap
    }



    fun convertDpToPixels(dp: Int): Int {
        return Math.round(
            TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp.toFloat(), Resources.getSystem().getDisplayMetrics()
            )
        )
    }

}
