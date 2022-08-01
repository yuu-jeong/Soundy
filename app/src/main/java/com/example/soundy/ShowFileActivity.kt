package com.example.soundy

import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.core.content.res.ResourcesCompat
import java.util.*

class ShowFileActivity : AppCompatActivity() {

    lateinit var mediaPlayer: MediaPlayer

    lateinit var dbManager: DBManager
    lateinit var sqliteDB: SQLiteDatabase

    lateinit var fileName : String
    lateinit var dirName : String
    lateinit var memoContent : String
    lateinit var routine : String
    lateinit var filePath: String

    lateinit var titleText : TextView
    lateinit var tvFileName : TextView
    lateinit var btnShowUpload : Button
    lateinit var btnMemo : ImageView
    lateinit var tvMemoContent : TextView
//    lateinit var btnQuiz : Button
//    lateinit var btnRoutine : Button
    lateinit var btnBack : ImageButton
    lateinit var btnMypage : ImageButton
    lateinit var btnPlay : ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_file)

        titleText = findViewById(R.id.titleText)
        tvFileName = findViewById(R.id.tvFileName)
        btnShowUpload = findViewById(R.id.btnUpload)
        btnMemo = findViewById(R.id.ivMemo)
        tvMemoContent = findViewById(R.id.tvMemoContent)
//        btnQuiz = findViewById(R.id.btnQuiz)
//        btnRoutine = findViewById(R.id.btnRoutine)
        btnBack = findViewById(R.id.btnBack)
        btnPlay = findViewById(R.id.btnPlay)

        dbManager = DBManager(this, "File", null, 1)
        sqliteDB = dbManager.readableDatabase

        var play_status = false

        /* FileListDetail 에서 파일명(녹음파일명) 받아옴 */
        fileName = intent.getStringExtra("fileName").toString()
        titleText.setText(fileName)
        tvFileName.setText(fileName)
        dirName = intent.getStringExtra("dirName").toString()
        filePath = intent.getStringExtra("filePath").toString()
        Log.d("인텐트", "ShowFileActivity이 받은 filePath: $filePath")

        /* 녹음 파일을 미디어 플레이어에 담기 */
        mediaPlayer = MediaPlayer()
        mediaPlayer.apply {
            setDataSource(filePath)
            prepare()
        }

        btnPlay.setOnClickListener{
            /* 재생 상태에 따라 play, pause 설정 */
            if(play_status == false){
                btnPlay.setImageResource(R.drawable.pause_icon)
                mediaPlayer.start()
                play_status = true
            } else {
                btnPlay.setImageResource(R.drawable.play_icon)
                mediaPlayer.pause()
                play_status = false
            }
        }

        btnShowUpload.setOnClickListener {
            // 업로드 했던 파일을 보여줌
        }

        /* db에서 (녹음)파일명으로 메모 내용을 불러옴 */
        var cursor: Cursor = sqliteDB.rawQuery("select memo from File where fileName = '$fileName';", null)

        while (cursor.moveToNext()) {
            memoContent = cursor.getString(0)
        }

        Log.d("메모", "$memoContent")
        if (memoContent != "") {
            tvMemoContent.setText(memoContent)
        }

        btnMemo.setOnClickListener{
            /* 메모 편집 페이지로 이동 */
            Toast.makeText(this@ShowFileActivity, fileName, Toast.LENGTH_SHORT).show()
            val intent = Intent(this, MemoActivity::class.java)
            intent.putExtra("fileName", fileName)
            intent.putExtra("dirName", dirName)
            intent.putExtra("filePath", filePath)
            startActivity(intent)
            finish()
        }

//        btnQuiz.setOnClickListener {
//            /* 퀴즈 페이지로 이동 */
//            val intent = Intent(this, SttQuizActivity::class.java)
//            startActivity(intent)
//        }
//
//        btnRoutine.setOnClickListener {
//            val cal = Calendar.getInstance()
//            val dateSetListener = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
//                routine = "${year}-${month+1}-${dayOfMonth}"
//                // db에 수정한 날짜 저장
//
//                Toast.makeText(this@ShowFileActivity, "${year}년 ${month+1}월 ${dayOfMonth}일로 수정되었습니다.", Toast.LENGTH_SHORT).show()
//            }
//            DatePickerDialog(this, dateSetListener, cal.get(Calendar.YEAR),cal.get(Calendar.MONTH),cal.get(
//                Calendar.DAY_OF_MONTH)).show()
//        }

        /* 마이페이지 이동 기능 */
        btnMypage=findViewById<ImageButton>(R.id.btnMypage)
        btnMypage.setOnClickListener {
            val intent = Intent(this, MyPageActivity::class.java)
            startActivity(intent)
        }

        /* 뒤로 가기 버튼 클릭 시 */
        btnBack.setOnClickListener {
            finish()
        }
    }
}

