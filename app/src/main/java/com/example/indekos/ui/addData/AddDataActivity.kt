package com.example.indekos.ui.addData

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.indekos.R
import com.example.indekos.databinding.ActivityAddDataBinding
import com.example.indekos.ui.history.HistoryActivity
import com.example.indekos.ui.home.HomeActivity
import com.example.indekos.ui.login.LoginActivity
import com.example.indekos.ui.splash.SplashScreenActivity
import com.example.indekos.util.Preferences
import com.example.indekos.util.adapter.PhotosAdapterAdd
import com.example.indekos.util.createCustomTempFile
import com.example.indekos.util.uriToFile
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.io.File

@AndroidEntryPoint
class AddDataActivity : AppCompatActivity() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var currentLocation: Location? = null
    private val binding by lazy { ActivityAddDataBinding.inflate(layoutInflater) }
    private var latIndekos: Double? = null
    private var longIndekos: Double? = null
    private var file: File? = null
    private lateinit var photoPath: String
    private lateinit var photoAdapter: PhotosAdapterAdd
    private val photoList = mutableListOf<String>()
    private val viewModel: AddDataViewModel by viewModels()
    private val userId by lazy { Preferences.getUserId(this@AddDataActivity) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        supportActionBar?.hide()

        // Set Username
        lifecycleScope.launch {
            userId?.let {
                viewModel.getUserbyId(it.toInt()).observe(this@AddDataActivity) {
                    binding.tvUsername.text = it.username
                }
            }
        }

        // Logika Currency Formatter
        setupEditTextCurrency()

        // Inisialisasi FusedLocationProviderClient
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        // Minta izin lokasi
        if (!arePermissionsGranted()) {
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
        } else {
            getLastLocation()
        }

        // Button ke main activity
        binding.btnToExplore.setOnClickListener {
            Intent(this, HomeActivity::class.java).also {
                startActivity(it)
                finish()
            }
        }

        // Button ke history activity
        binding.btnToAllData.setOnClickListener {
            Intent(this, HistoryActivity::class.java).also {
                startActivity(it)
                finish()
            }
        }

        // Button ke logout activity
        binding.btnLogout.setOnClickListener {
            Preferences.logout(this)
            Toast.makeText(this, "Selamat Tinggal", Toast.LENGTH_SHORT).show()
            Intent(this, LoginActivity::class.java).also {
                startActivity(it)
                finish()
            }
        }

        // Button check lokasi
        binding.btnCheckLokasi.setOnClickListener {
            if (currentLocation != null) {
                getLastLocation()
                latIndekos = currentLocation?.latitude
                longIndekos = currentLocation?.longitude
                binding.etLokasi.setText("$latIndekos, $longIndekos")
                binding.etLokasi.isEnabled = false
                Toast.makeText(
                    this,
                    "Lokasi berhasil diambil $latIndekos, $longIndekos",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Log.d("AddDataActivity", "Lokasi gagal: $latIndekos, $longIndekos")
                Log.d(
                    "AddDataActivity",
                    "Lokasi current: ${currentLocation?.latitude}, ${currentLocation?.longitude}"
                )
                Toast.makeText(this, "Lokasi Tidak Valid", Toast.LENGTH_SHORT).show()
            }
        }

        // Button tambah foto banner
        binding.btnPhotoBannerCamera.setOnClickListener {
            startCamera()
        }
        binding.btnPhotoBannerGallery.setOnClickListener {
            startGallery()
        }

        // Button tambah foto indekos
        photoAdapter = PhotosAdapterAdd(photoList)
        binding.rvPhotos.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        binding.rvPhotos.adapter = photoAdapter
        binding.btnPhotosGallery.setOnClickListener {
            showPhotoSelectionDialog()
        }

        binding.btnAddData.setOnClickListener {
            val namaIndekos = binding.etNamaIndekos.text.toString()
            val hargaIndekos = binding.etHargaPerBulan.text.toString()
            val jumlahBedroom = binding.etJumlahBedroom.text.toString()
            val jumlahCupboard = binding.etJumlahCupboard.text.toString()
            val jumlahKitchen = binding.etJumlahKitchen.text.toString()
            val alamat = binding.etAlamat.text.toString()
            val kota = binding.etKota.text.toString()
            val provinsi = binding.etProvinsi.text.toString()
            val file = file.toString()
            Log.d("TAG", "Photo: $photoList")

            if (namaIndekos.isNotEmpty() && hargaIndekos.isNotEmpty() && binding.etLokasi.text?.isNotEmpty() == true) {
                if (latIndekos != null && longIndekos != null) {
                    userId?.toInt()?.let { id ->
                        viewModel.insertIndekos(
                            id,
                            namaIndekos,
                            hargaIndekos,
                            jumlahBedroom,
                            jumlahCupboard,
                            jumlahKitchen,
                            latIndekos!!,
                            longIndekos!!,
                            alamat,
                            kota,
                            provinsi,
                            photoList,
                            file
                        )
                    }
                    binding.apply {
                        etNamaIndekos.text?.clear()
                        etHargaPerBulan.text?.clear()
                        etJumlahBedroom.text?.clear()
                        etJumlahCupboard.text?.clear()
                        etJumlahKitchen.text?.clear()
                        etAlamat.text?.clear()
                        etKota.text?.clear()
                        etProvinsi.text?.clear()
                        etLokasi.text?.clear()
                        etLokasi.isEnabled = true
                        ivPhotoBanner.setImageResource(R.drawable.null_image)
                    }
                    Toast.makeText(this, "Data berhasil ditambahkan", Toast.LENGTH_SHORT).show()
                    photoList.clear()
                    photoAdapter.notifyDataSetChanged()
                } else {
                    Toast.makeText(this, "Lokasi tidak valid", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Harap lengkapi data", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupEditTextCurrency() {
        binding.etHargaPerBulan.addTextChangedListener(object : TextWatcher {
            private var isUpdating = false

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable) {
                if (isUpdating) {
                    return
                }

                isUpdating = true

                val originalText = s.toString()

                // Hapus semua tanda titik sebelum memformat angka
                val cleanText = originalText.replace(".", "")

                // Format ulang angka dengan menambahkan titik setiap 3 angka
                val formattedText = formatCurrency(cleanText)

                // Set teks yang telah diformat ke EditText
                binding.etHargaPerBulan.setText(formattedText)

                // Posisikan kursor di akhir teks
                binding.etHargaPerBulan.setSelection(formattedText.length)
                isUpdating = false
            }

            private fun formatCurrency(value: String): String {

                // Hapus tanda minus jika ada
                var isNegative = false
                var cleanValue = value
                if (cleanValue.startsWith("-")) {
                    isNegative = true
                    cleanValue = cleanValue.substring(1)
                }

                // Format ulang angka dengan menambahkan titik setiap 3 angka
                val stringBuilder = StringBuilder(cleanValue)
                val length = stringBuilder.length
                var i = length - 3
                while (i > 0) {
                    stringBuilder.insert(i, ".")
                    i -= 3
                }

                // Tambahkan tanda minus kembali jika angka negatif
                if (isNegative) {
                    stringBuilder.insert(0, "-")
                }

                return stringBuilder.toString()
            }
        })
    }

    private fun arePermissionsGranted(): Boolean {
        REQUIRED_PERMISSIONS.forEach { permission ->
            if (ActivityCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                return false
            }
        }
        return true
    }

    @SuppressLint("MissingPermission")
    private fun getLastLocation() {
        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            Log.d("AddDataActivity", "Location: ${location?.latitude}, ${location?.longitude}")
            if (location != null) {
                currentLocation = location
                latIndekos = location.latitude
                longIndekos = location.longitude
                Log.d("AddDataActivity", "Lokasi Terakhir: $latIndekos, $longIndekos")
            } else {
                Log.d("AddDataActivity", "Lokasi terakhir gagal diambil")
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation()
            } else {
                AlertDialog.Builder(this)
                    .setTitle("Izin Lokasi Diperlukan")
                    .setMessage("Untuk menggunakan fitur ini, Anda perlu memberikan izin untuk mengakses lokasi Anda.")
                    .setPositiveButton("Pengaturan") { _, _ ->
                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                        val uri = Uri.fromParts("package", packageName, null)
                        intent.data = uri
                        startActivity(intent)
                    }
                    .setNegativeButton("Batal") { dialog, _ -> dialog.dismiss() }
                    .show()
            }
        }
    }

    // Rv Photo Function
    private fun showPhotoSelectionDialog() {
        val options = resources.getStringArray(R.array.photo_options)
        AlertDialog.Builder(this).apply {
            setTitle(getString(R.string.select_photo_method))
            setItems(options) { _, which ->
                when (which) {
                    0 -> startCameraPhotos() // "Take Photo"
                    1 -> startGalleryPhotos() // "Choose from Gallery"
                }
            }
            setNegativeButton(android.R.string.cancel) { dialog, _ -> dialog.dismiss() }
        }.show()
    }

    //Camera Function
    @SuppressLint("QueryPermissionsNeeded")
    private fun startCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.resolveActivity(packageManager)
        createCustomTempFile(application).also {
            val photoURI: Uri = FileProvider.getUriForFile(
                this, "com.example.indekos", it
            )
            photoPath = it.absolutePath
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            launcherIntentCamera.launch(intent)
        }
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            val myFile = File(photoPath)
            file = myFile
            val result = BitmapFactory.decodeFile(myFile.path)
            Glide.with(this)
                .load(result)
                .into(binding.ivPhotoBanner)
        }
    }

    //Camera Function Rv Photos
    @SuppressLint("QueryPermissionsNeeded")
    private fun startCameraPhotos() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.resolveActivity(packageManager)
        createCustomTempFile(application).also {
            val photoURI: Uri = FileProvider.getUriForFile(
                this, "com.example.indekos", it
            )
            photoPath = it.absolutePath
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            launcherIntentCameraPhotos.launch(intent)
        }
    }

    private val launcherIntentCameraPhotos = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            val myFile = File(photoPath)
            val photoFilePath = myFile.absolutePath
            photoList.add(photoFilePath)
            photoAdapter.notifyDataSetChanged()
        }
    }

    //Gallery Function
    private fun startGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            val selectedImg: Uri = it.data?.data as Uri
            val myFile = uriToFile(selectedImg, this)
            file = myFile
            Glide.with(this)
                .load(selectedImg)
                .into(binding.ivPhotoBanner)
        }
    }

    //Gallery Function Rv Photos
    private fun startGalleryPhotos() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGalleryPhotos.launch(chooser)
    }

    private val launcherIntentGalleryPhotos = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            val selectedImg: Uri = it.data?.data as Uri
            val myFile = uriToFile(selectedImg, this)
            val photoFilePath = myFile.absolutePath
            photoList.add(photoFilePath)
            photoAdapter.notifyDataSetChanged()
        }
    }

    companion object {
        private val REQUIRED_PERMISSIONS =
            arrayOf(Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION)
        private const val REQUEST_CODE_PERMISSIONS = 123
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this, SplashScreenActivity::class.java))
    }
}