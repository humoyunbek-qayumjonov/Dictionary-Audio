package com.humoyunbek.dictionary

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentActivity
import androidx.viewpager.widget.ViewPager
import com.humoyunbek.dictionary.adapter.PictureAdapter
import com.humoyunbek.dictionary.adapter.ViewPagerAdapter
import com.humoyunbek.dictionary.databinding.ActivityMainBinding
import com.humoyunbek.dictionary.model.PagerModel

class MainActivity : AppCompatActivity() {
    lateinit var binding:ActivityMainBinding
    lateinit var list:ArrayList<PagerModel>
    lateinit var viewPagerAdapter:ViewPagerAdapter
    lateinit var sharedPreferences: SharedPreferences
    lateinit var editor: SharedPreferences.Editor
    lateinit var pictureAdapter:PictureAdapter
    lateinit var listImage:ArrayList<Int>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPreferences = getSharedPreferences("Dictionary",Context.MODE_PRIVATE)
        val key = sharedPreferences.getInt("enter",-1)
        if (key == 1){
            val intent = Intent(this,LiveActivity::class.java)
            startActivity(intent)
            finish()
        }
        list = ArrayList()
        list.add(PagerModel("Dastur kimlar uchun","Bu dastur ingiliz tilida so`zlashishni o`rganuvchi barcha talabalar uchun"))
        list.add(PagerModel("Dastur talabaga nima bera oladi","Qidirilgan so`zning qanfay talaffuz qilinishi,Audio holatda o`qilishi"))
        list.add(PagerModel("Boshqa dasturlasrdan nimasi bilan ajralib turadi","Qidirilgan so`zni uning ishtirokidagi gaplar bilan misol keltirishi"))

        listImage = ArrayList()
        listImage.add(R.drawable.illustation_people)
        listImage.add(R.drawable.illustration2)
        listImage.add(R.drawable.illustration3)
        viewPagerAdapter = ViewPagerAdapter(list, supportFragmentManager)
        binding.viewPager2.adapter = viewPagerAdapter
        binding.tabLayout.setupWithViewPager(binding.viewPager2)



        binding.viewPager2.setOnPageChangeListener(object :ViewPager.OnPageChangeListener{
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                when(position){
                    0->{
                        binding.imageIllustration.setImageResource(R.drawable.illustation_people)
                    }
                    1->{
                        binding.imageIllustration.setImageResource(R.drawable.illustration2)
                    }
                    2->{
                        binding.imageIllustration.setImageResource(R.drawable.illustration3)
                    }
                }

                if (position == 2){
                    binding.nextBtn.visibility = View.VISIBLE
                }
            }

            override fun onPageSelected(position: Int) {

            }

            override fun onPageScrollStateChanged(state: Int) {

            }

        })

        binding.nextBtn.setOnClickListener {
            val intent = Intent(this,LiveActivity::class.java)
            startActivity(intent)
            sharedPreferences = getSharedPreferences("Dictionary", Context.MODE_PRIVATE)
            editor = sharedPreferences.edit()
            editor.putInt("enter",1)
            editor.commit()
            finish()
        }
    }
}