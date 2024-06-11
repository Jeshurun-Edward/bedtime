package com.example.bedtime

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.*
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.bedtime1.R
import com.example.bedtime1.ui.theme.Bedtime1Theme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import android.util.Log
import com.example.bedtime1.BuildConfig
import com.example.bedtime1.StoryActivity
import com.example.bedtime1.data.PromptResponse
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.BlockThreshold
import com.google.ai.client.generativeai.type.HarmCategory
import com.google.ai.client.generativeai.type.SafetySetting
import com.google.ai.client.generativeai.type.generationConfig
import kotlinx.coroutines.MainScope
//import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

//import kotlinx.coroutines.withContext


class StoryCreationActivity : AppCompatActivity() {
    // Gemini model (initialized once)
    private val generativeModel by lazy {
        GenerativeModel(
            modelName = "gemini-1.5-flash", // Replace with the appropriate model name
            apiKey = BuildConfig.API_KEY, // Replace with your actual API key
            generationConfig = generationConfig {
                temperature = 0.4f
                topK = 32
                topP = 1f
                maxOutputTokens = 8000
            },
            safetySettings = listOf(
                SafetySetting(HarmCategory.HARASSMENT, BlockThreshold.MEDIUM_AND_ABOVE),
                SafetySetting(HarmCategory.HATE_SPEECH, BlockThreshold.MEDIUM_AND_ABOVE),
                SafetySetting(HarmCategory.SEXUALLY_EXPLICIT, BlockThreshold.MEDIUM_AND_ABOVE),
                SafetySetting(HarmCategory.DANGEROUS_CONTENT, BlockThreshold.MEDIUM_AND_ABOVE),
            )
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Bedtime1Theme {
                // User input state variables
                var charactersInput by remember { mutableStateOf("") }
                var moralInput by remember { mutableStateOf("") }
                var selectedLength by remember { mutableStateOf("Short") }

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize().padding(20.dp),
                        verticalArrangement = Arrangement.Top,
                    )
                    {
                        Text(text = stringResource(R.string.create_story))
                        Spacer(modifier = Modifier.height(50.dp))
                        OutlinedTextField(
                            value = charactersInput,
                            onValueChange = { charactersInput = it },
                            label = { Text(text = stringResource(R.string.enter_characters)) },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(50.dp))
                        OutlinedTextField(
                            value = moralInput,
                            onValueChange = { moralInput = it },
                            label = { Text(text = stringResource(R.string.moral)) },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(50.dp))
                        Text(text = stringResource(R.string.length))
                        Row(modifier = Modifier.fillMaxWidth()) {
                            RadioButton(
                                selected = selectedLength == "Short",
                                onClick = { selectedLength = "Short" },
                                modifier = Modifier.semantics { contentDescription = "Short" }
                            )
                            RadioButton(
                                selected = selectedLength == "Medium",
                                onClick = { selectedLength = "Medium" },
                                modifier = Modifier.semantics { contentDescription = "Medium" }
                            )
                            RadioButton(
                                selected = selectedLength == "Large",
                                onClick = { selectedLength = "Large" },
                                modifier = Modifier.semantics { contentDescription = "Large" }
                            )
                        }
                        Spacer(modifier = Modifier.height(20.dp))
                        Row(modifier = Modifier.fillMaxWidth()){
                            Text(text = stringResource(R.string.short_length))
                            Spacer(modifier = Modifier.width(10.dp))

                            Text(text = stringResource(R.string.medium_length))
                            Spacer(modifier = Modifier.width(10.dp))

                            Text(text = stringResource(R.string.long_length))
                            Spacer(modifier = Modifier.width(10.dp))

                        }
                        Spacer(modifier = Modifier.height(50.dp))
                        Button(onClick = { createStory(generativeModel, charactersInput.trim(), moralInput.trim(), selectedLength.trim()) }) {
                            Text(text = stringResource(R.string.create_button))
                        }
                    }
                }
                // Handle system bar insets (optional for avoiding content overlapping status bar/navigation bar)
                val window = window
                ViewCompat.setOnApplyWindowInsetsListener(window.decorView) { v, insets ->
                    val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                    v.setPadding(
                        systemBars.left,
                        systemBars.top,
                        systemBars.right,
                        systemBars.bottom
                    )
                    insets
                }
            }
        }


    }
    private fun createStory(generativeModel: GenerativeModel, charactersInput: String, moralInput: String, selectedLength: String) {
        Log.d("BedtimeApp", "Characters: $charactersInput, Moral: $moralInput, Length: $selectedLength")
        val prompt = """Write a story which teaches moral: $moralInput, using the Characters: $charactersInput, story length should be: $selectedLength.
        Always talk about the moral only in the end, build the story first with good scenes without specifying morals even in character description, only write
        what's happening, helping the user to engage fully, don't usage complex words to describe scenes, write it in a way children would understand. In the end,
        You are writing prompt to mobius model, write a one line prompt which creates a cartoon book cover image of story which instructs how the characters are supposed to be and in
        what background. Give output as json with following keys- title, story, moral, imagePrompt, dont' write json just start the output with { 
        """.trimMargin()

        // Launch a coroutine for the suspend function call
        MainScope().launch {  // Use Dispatchers.IO for network calls
            val response = generativeModel.generateContent(prompt = prompt)
            val respText : String  = response.text.toString()

            val resJson: PromptResponse =   Json.decodeFromString(respText)
            Log.d("BedtimApp","reponse: $resJson")


            val title =  resJson.title
            val storyText = resJson.story
            val moral = resJson.moral
//            val imgPrompt = resJson.imagePrompt


            // Update UI with storyText on the main thread
//            withContext(Dispatchers.Main) {
            when (selectedLength) {
                "Short" -> startActivityForStory(title, storyText, moral, StoryActivity.SHORT_STORY_TYPE)
                "Medium" ->startActivityForStory(title, storyText, moral, StoryActivity.MEDIUM_STORY_TYPE)
                "Large" -> startActivityForStory(title, storyText, moral, StoryActivity.LARGE_STORY_TYPE)
            }
//            }
        }
    }
        // Handle user inputs here (e.g., navigate to another screen, store data in a database)
        // You can potentially start a new activity to display the generated story
        // val intent = Intent(this, StoryActivity::class.java)
        // intent.putExtra("characters", charactersInput)
        // intent.putExtra("moral", moralInput)
        // intent.putExtra("length", selectedLength)
        // startActivity(intent)

    private fun startActivityForStory(title: String, story: String, moral : String,storyType: Int) {
        val intent = Intent(this, StoryActivity::class.java)
        intent.putExtra("title", title)
        intent.putExtra("story", story)
        intent.putExtra("moral", moral)
        intent.putExtra("storyType", storyType)
        startActivity(intent)
    }
}




