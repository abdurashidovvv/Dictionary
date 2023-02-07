package com.abdurashidov.dictionary

import android.content.IntentFilter
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.abdurashidov.dictionary.adapters.RvAdapter
import com.abdurashidov.dictionary.broadcast.InternetReceiver
import com.abdurashidov.dictionary.databinding.ActivityMainBinding
import com.abdurashidov.dictionary.repository.DictionaryRepository
import com.abdurashidov.dictionary.retrofit.ApiClient
import com.abdurashidov.dictionary.utils.Data
import com.abdurashidov.dictionary.utils.NetworkHelper
import com.abdurashidov.dictionary.utils.Status
import com.abdurashidov.dictionary.viewmodel.MyDictViewModel
import com.abdurashidov.dictionary.viewmodel.MyViewModelFactory
import com.google.android.material.snackbar.Snackbar

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var dictionaryRepository: DictionaryRepository
    private lateinit var rvAdapter: RvAdapter
    private lateinit var viewModel: MyDictViewModel
    private lateinit var networkHelper: NetworkHelper
    private lateinit var internetReceiver: InternetReceiver
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Data.splash = 1

        //Broadcast
        networkHelper = NetworkHelper(this)
        internetReceiver = InternetReceiver()
        val intentFilter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        registerReceiver(internetReceiver, intentFilter)

        //repository
        dictionaryRepository = DictionaryRepository(ApiClient.getApiService())
        viewModel = ViewModelProvider(
            this, MyViewModelFactory(dictionaryRepository)
        )[MyDictViewModel::class.java]


        //livedate, checking the network
        Data.network.observe(this) {network->
            if (network) {
                binding.myRv.visibility = View.VISIBLE
                binding.notInternet.visibility = View.GONE
                binding.constraint2.visibility = View.VISIBLE

                //search words btn
                binding.searchBtn.setOnClickListener {
                    val word = binding.search.text.toString()
                    viewModel.getWordMeaning(word).observe(this) {
                        when (it.status) {
                            Status.LOADING -> {
                                binding.progressBar.visibility = View.VISIBLE
                            }
                            Status.SUCCESS -> {
                                binding.progressBar.visibility = View.GONE
                                if (it.data != null) {
                                    rvAdapter = RvAdapter(it.data)
                                    findViewById<RecyclerView>(R.id.my_rv).adapter = rvAdapter
                                } else {
                                    binding.progressBar.visibility = View.GONE
                                }
                            }
                            Status.ERROR -> {
                                binding.progressBar.visibility = View.GONE
                                Toast.makeText(
                                    this, "This word was not found !", Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                }
                Snackbar.make(binding.root, "Connected internet", Snackbar.LENGTH_SHORT).show()
            } else {
                binding.myRv.visibility = View.GONE
                binding.constraint2.visibility = View.GONE
                binding.notInternet.visibility = View.VISIBLE
                Snackbar.make(binding.root, "Not connected internet", Snackbar.LENGTH_SHORT).show()
            }
        }


    }
}