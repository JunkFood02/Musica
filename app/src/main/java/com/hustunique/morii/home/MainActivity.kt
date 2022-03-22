package com.hustunique.morii.home

import android.app.ActivityOptions
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.transition.Slide
import android.util.Log
import android.view.*
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.hustunique.morii.music.MusicSelectActivity
import com.hustunique.morii.util.BaseActivity
import com.hustunique.morii.util.MyApplication
import com.hustunique.morii.util.MyApplication.Companion.musicDiaryList
import morii.R
import morii.databinding.ActivityMainBinding

class MainActivity : BaseActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: MusicDiaryAdapter
    private var showAssistantDialog: Boolean = true
    private var sortByDateAscending: Boolean = true
    private lateinit var sharedPref: SharedPreferences;
    override fun onCreate(savedInstanceState: Bundle?) {
        window.requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS)
        window.exitTransition = Slide()
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        showAssistantDialog()
        initUI()
        setRecyclerView()
    }

    private fun showAssistantDialog() {
        sharedPref = this.getSharedPreferences(
            MyApplication.PREFERENCE_FILE_KEY, Context.MODE_PRIVATE
        )
        showAssistantDialog = sharedPref.getBoolean("showAssistantDialog", true);
        if (showAssistantDialog) {
            val builder = MaterialAlertDialogBuilder(this);
            builder.setMessage(
                "在音乐选取页面，左右滑动选取一段符合你当前心情的音乐\n" +
                        "\n" +
                        "在创作页面，自由选取风格不同的白噪音素材进行混合\n" +
                        "\n" +
                        "在编辑界面，使用文字及图片记录你当前的心情\n" +
                        "\n" +
                        "在首页回顾你之前创建的音乐日记，并且将日记导出分享给他人（目前暂仅支持分享音频文件）"
            ).setTitle("欢迎使用Morii音乐日记").setNegativeButton(
                "关闭且不再显示"
            ) { dialog, which ->
                with(sharedPref.edit()) {
                    putBoolean("showAssistantDialog", false)
                    apply()
                }
            }
                .setPositiveButton(
                    "完成"
                ) { _, _ -> }.show()

        }
    }

    private fun setRecyclerView() {
        adapter = MusicDiaryAdapter(this, musicDiaryList)
        binding.musicDiaryRecyclerView.apply {
            adapter = this@MainActivity.adapter
            layoutManager =
                LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false)
        }
    }

    override fun onResume() {
        super.onResume()
        adapter.notifyItemInserted(musicDiaryList.size)
    }

    private fun initUI() {
        Log.d(TAG, "initUI: ")
        binding.buttonMain.setOnClickListener {
            val intent = Intent(this@MainActivity, MusicSelectActivity::class.java)
            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle())
        }
        binding.reverseSwitchButton.setOnClickListener { reverseRecyclerView() }
        binding.reverseSwitchButton.setOnLongClickListener {
            with(sharedPref.edit()) {
                putBoolean("showAssistantDialog", true)
                apply()
            }
            return@setOnLongClickListener true;
        }
    }

    private fun reverseRecyclerView() {
        if (sortByDateAscending) {
            binding.sortButtonText.setText("正序排列")
            binding.musicDiaryRecyclerView.layoutManager =
                LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true).apply {
                    stackFromEnd = true
                }
        } else {
            binding.sortButtonText.setText("倒序排列")
            binding.musicDiaryRecyclerView.layoutManager =
                LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        }
        sortByDateAscending = sortByDateAscending.not()
    }


    companion object {
        private const val TAG = "MainActivity"
    }

}