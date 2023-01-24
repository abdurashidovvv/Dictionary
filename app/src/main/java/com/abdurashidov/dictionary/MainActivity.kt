package com.abdurashidov.dictionary

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.abdurashidov.dictionary.adapters.RvAdapter
import com.abdurashidov.dictionary.databinding.ActivityMainBinding
import com.abdurashidov.dictionary.repository.DictionaryRepository
import com.abdurashidov.dictionary.retrofit.ApiClient
import com.abdurashidov.dictionary.utils.Status
import com.abdurashidov.dictionary.viewmodel.MyDictViewModel
import com.abdurashidov.dictionary.viewmodel.MyViewModelFactory

class
MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var dictionaryRepository: DictionaryRepository
    private lateinit var rvAdapter: RvAdapter
    private lateinit var viewModel: MyDictViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        dictionaryRepository= DictionaryRepository(ApiClient.getApiService())
        viewModel= ViewModelProvider(this, MyViewModelFactory(dictionaryRepository))[MyDictViewModel::class.java]


        binding.searchBtn.setOnClickListener {
            val word=binding.search.text.toString()
            viewModel.getWordMeaning(word)
                .observe(this){
                    when(it.status){
                        Status.LOADING->{
                            binding.progressBar.visibility= View.VISIBLE
                        }
                        Status.SUCCESS->{
                            binding.progressBar.visibility=View.GONE
                            rvAdapter=RvAdapter(it.data!!)
                            findViewById<RecyclerView>(R.id.my_rv).adapter=rvAdapter
                        }
                        Status.ERROR->{
                            Toast.makeText(this, "Not data found", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
        }

    }
}