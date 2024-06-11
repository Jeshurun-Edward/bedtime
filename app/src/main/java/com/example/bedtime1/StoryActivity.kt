package com.example.bedtime1

//import android.os.Bundle
//import androidx.activity.enableEdgeToEdge
//import androidx.appcompat.app.AppCompatActivity
//import androidx.core.view.ViewCompat
//import androidx.core.view.WindowInsetsCompat
//
//class StoryActivity : AppCompatActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
//        setContentView(R.layout.activity_story)
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }
//    }
//}

//package com.example.bedtime

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.bedtime1.R
import com.example.bedtime1.ui.theme.Bedtime1Theme


import androidx.compose.foundation.rememberScrollState


class StoryActivity : AppCompatActivity() {

    companion object {
        const val SHORT_STORY_TYPE = 1
        const val MEDIUM_STORY_TYPE = 2
        const val LARGE_STORY_TYPE = 3
    }
    private var title = ""
    private var storyText = ""
    private var moral = ""
    private var storyType = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        title = intent.getStringExtra("title")?:""
        storyText = intent.getStringExtra("story") ?: ""
        moral = intent.getStringExtra("moral")?:""
        storyType = intent.getIntExtra("storyType", 0)
        setContent {
            Bedtime1Theme {
                val scrollState = rememberScrollState(0)

                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    Column(
                        modifier = Modifier.fillMaxSize().verticalScroll(scrollState, reverseScrolling=true),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.titleLarge,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(5.dp))
                        Text(text = storyText,
                            style = MaterialTheme.typography.bodySmall) // Display the story
                        Spacer(modifier = Modifier.height(25.dp))
                        Text(text = moral,
                            style = MaterialTheme.typography.titleSmall)
                    }
                }
            }
        }
    }
}
