package com.example.bedtime1.ui.theme

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.bedtime1.MainActivity
import com.example.bedtime1.R
import com.example.bedtime1.databinding.ActivityStoriesScreenBinding
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.colorResource
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.height
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.example.bedtime.StoryCreationActivity

data class Book(val title: String, val imageUrl: Int)

class StoriesScreen : AppCompatActivity() {

    private lateinit var binding: ActivityStoriesScreenBinding

    // Define a function to navigate to the new activity
    private fun navigateToStoryCreationActivity() {
        val intent = Intent(this, StoryCreationActivity::class.java)
        startActivity(intent)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val onClick: () -> Unit = { // Callback function for button click
                navigateToStoryCreationActivity()
        }
        setContent {
            Bedtime1Theme {
                // User input state variables
                val book1 = Book(title = "Test1", imageUrl = R.drawable.book )
                val book2 = Book(title = "Test2", imageUrl = R.drawable.book )

                val booksState by remember { mutableStateOf(listOf(book1, book2)) }
//                val books = listOf("b1", "b2")

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background)
                    {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(20.dp),
                        verticalArrangement = Arrangement.Top,
                    ) {
                    }
                        BookListScreen(onClick = onClick, books = booksState)

                }
//        binding = ActivityStoriesScreenBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        setSupportActionBar(findViewById(R.id.toolbar))
//        binding.toolbarLayout.title = title
//        binding.fab.setOnClickListener { view ->
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                .setAction("Action", null)
//                .setAnchorView(R.id.fab).show()
        }
            }
    }
    private fun startNextActivity() {
        val intent = Intent(this, StoryCreationActivity::class.java)
        startActivity(intent)
    }
  }

@Composable
//@Preview(showBackground = true) // Show background for better visibility
fun StoriesScreenPreview() {
    Bedtime1Theme {
        Surface(
            modifier = Modifier.fillMaxSize(),
//            color = colorResource(id = ).LightGray // Set a light gray background
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                items(2) { index -> // Increase sample data size
                    Text(
                        text = "Story Item $index",
//                        color = Color.Black // Set contrasting text color
                    )
                }
            }
        }
    }
}

@Composable
fun BookListScreen(onClick: () -> Unit, books: List<Book>) {
    Bedtime1Theme { // Replace with your theme name if different
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                LazyColumn(
                    modifier = Modifier.weight(1f), // Occupy most of the screen
                    contentPadding = PaddingValues(16.dp)
                ) {
                    items(books.chunked(2)) { bookRow ->
                        Row(modifier = Modifier.fillMaxWidth()) {
                            bookRow.forEachIndexed { index, book ->
                                BookCard(book = book, modifier = Modifier.weight(1f))
                                if (index < bookRow.lastIndex) {
                                    Spacer(modifier = Modifier.width(8.dp)) // Spacing between cards
                                }
                            }
                        }
                    }
                }
                Button(
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(16.dp),
                    onClick = onClick
                ) {
                    Text(text = "Create")
                }
            }
        }
    }

}

@Composable
fun BookCard(book: Book, modifier: Modifier) {
    Card(modifier = modifier) {
        Column {
            Image(
                painter = painterResource(id = book.imageUrl), // Replace with image loading logic
                contentDescription = book.title,
                modifier = Modifier
                    .height(150.dp)
                    .fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = book.title, style = MaterialTheme.typography.bodyLarge)
        }
    }
}

