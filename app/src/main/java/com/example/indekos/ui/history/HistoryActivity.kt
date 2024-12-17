package com.example.indekos.ui.history

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.indekos.databinding.ActivityHistoryBinding
import com.example.indekos.ui.detailHistory.DetailHistoryActivity
import com.example.indekos.ui.addData.AddDataActivity
import com.example.indekos.util.Preferences
import com.example.indekos.util.adapter.IndekosHistoryAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HistoryActivity : AppCompatActivity() {

    override fun onBackPressed() {
        Intent(this, AddDataActivity::class.java).also {
            startActivity(it)
            finish()
        }
    }

    private val userId by lazy { Preferences.getUserId(this@HistoryActivity) }
    private val binding by lazy { ActivityHistoryBinding.inflate(layoutInflater) }
    private lateinit var adapter: IndekosHistoryAdapter
    private val viewModel: HistoryViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        supportActionBar?.hide()

        adapter = IndekosHistoryAdapter(
            onItemClick = {
                navigateToDetailHistoryActivity(it)
            }
        )
        binding.rvDataHistory.adapter = adapter
        binding.rvDataHistory.layoutManager = LinearLayoutManager(this)

        userId?.let { viewModel.getListIndekosByUserId(it.toInt()) }
        Log.d("USER ID", "ID USER: $userId")
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.indekosList.collect { indekosList ->
                    binding.progressBar2.visibility = android.view.View.GONE

                    if (indekosList.isEmpty()) {
                        binding.tvDataKosong.visibility = android.view.View.VISIBLE
                    } else {
                        binding.tvDataKosong.visibility = android.view.View.GONE
                        adapter.submitList(indekosList)
                    }
                }
            }
        }

        binding.btnBackToAddData.setOnClickListener {
            Intent(this, AddDataActivity::class.java).also {
                startActivity(it)
                finish()
            }
        }
    }

    private fun navigateToDetailHistoryActivity(indekosId: String) {
        val intent = Intent(this, DetailHistoryActivity::class.java)
        intent.putExtra("indekosId", indekosId)
        startActivity(intent)
    }
}