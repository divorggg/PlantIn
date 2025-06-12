package com.example.plantin.ui.article

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberImagePainter
import com.example.plantin.R
import com.example.plantin.ui.article.dataclass.Article
import com.example.plantin.ui.article.viewmodel.NewsViewModel
import com.example.plantin.ui.theme.PlantInTheme


class ArticlePage : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            PlantInTheme {
                ArticlePageScreen()
            }
        }
    }
}

@Composable
fun ArticlePageScreen() {
    val viewModel: NewsViewModel = viewModel()

    var currentPage by remember { mutableStateOf(0) }
    val pageSize = 7
    val maxNextClicks = 3
    val articles = viewModel.articles

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .padding(WindowInsets.systemBars.asPaddingValues()),
        topBar = { TopBar() },
        bottomBar = {
            // Bottom bar untuk tombol navigasi
            if (!viewModel.isLoading && articles.isNotEmpty()) {
                BottomNavigationBar(
                    currentPage = currentPage,
                    maxNextClicks = maxNextClicks,
                    pageSize = pageSize,
                    articles = articles,
                    onNextClick = { currentPage++ },
                    onPreviousClick = { currentPage-- }
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            val startIndex = currentPage * pageSize
            val endIndex = ((currentPage + 1) * pageSize).coerceAtMost(articles.size)
            val currentArticles = if (startIndex < articles.size) {
                articles.subList(startIndex, endIndex)
            } else {
                emptyList()
            }

            if (viewModel.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                ArticlePageContent(currentArticles)
            }
        }
    }
}

@Composable
fun BottomNavigationBar(
    currentPage: Int,
    maxNextClicks: Int,
    pageSize: Int,
    articles: List<Article>,
    onNextClick: () -> Unit,
    onPreviousClick: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color.White,
        shadowElevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Tombol Previous
            if (currentPage > 0) {
                Button(
                    onClick = onPreviousClick,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF757575)),
                    modifier = Modifier.weight(1f)
                ) {
                    Text(text = "Previous", color = Color.White)
                }
            } else {
                Spacer(modifier = Modifier.weight(1f))
            }

            // Indikator halaman
            Text(
                text = "Page ${currentPage + 1}",
                modifier = Modifier.padding(horizontal = 16.dp),
                fontWeight = FontWeight.Medium
            )

            // Tombol Next
            val hasMorePages = (currentPage + 1) * pageSize < articles.size
            val withinMaxClicks = currentPage < maxNextClicks

            if (hasMorePages && withinMaxClicks) {
                Button(
                    onClick = onNextClick,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF054D3B)),
                    modifier = Modifier.weight(1f)
                ) {
                    Text(text = "Next", color = Color.White)
                }
            } else {
                Spacer(modifier = Modifier.weight(1f))
            }
        }
    }
}

@Composable
fun ArticlePageContent(articles: List<Article>) {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        items(articles) { article ->
            ArticleItemPage(article)
        }
    }
}

@Composable
fun ArticleItemPage(article: Article) {
    val context = LocalContext.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .shadow(
                elevation = 4.dp,
                shape = RoundedCornerShape(16.dp),
                clip = false
            )
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .clickable {
                // Arahkan ke browser
                val intent = android.content.Intent(android.content.Intent.ACTION_VIEW)
                intent.data = android.net.Uri.parse(article.url)
                context.startActivity(intent)
            },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFFFFFFF).copy(alpha = 0.5f)
        )
    ) {
        Row(modifier = Modifier.padding(8.dp)) {
            article.urlToImage?.let { imageUrl ->
                Image(
                    painter = rememberImagePainter(
                        data = imageUrl,
                        builder = {
                            crossfade(true)
                            placeholder(R.drawable.placeholders_picture_icon) // buat dulu drawable ini
                            error(R.drawable.broken_image)             // buat dulu drawable ini
                        }
                    ),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .padding(8.dp)
                        .size(90.dp)
                        .clip(RoundedCornerShape(8.dp))
                )
            } ?: Box(
                modifier = Modifier
                    .padding(8.dp)
                    .size(90.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.Gray.copy(alpha = 0.3f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No Image",
                    fontSize = 12.sp,
                    color = Color.DarkGray
                )
            }


            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = article.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Start,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 18.sp
                )
                article.description?.let {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = it,
                        fontSize = 12.sp,
                        maxLines = 4,
                        lineHeight = 18.sp,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}


@Composable
fun TopBar() {
    val context = LocalContext.current
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .clip(RoundedCornerShape(topEnd = 24.dp, bottomEnd = 24.dp))
                .background(Color(0xFFFAD7BE))
        ) {
            IconButton(
                onClick = { (context as? Activity)?.finish() },
                modifier = Modifier
                    .size(48.dp)
                    .padding(start = 16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .shadow(elevation = 8.dp, shape = CircleShape)
                        .clip(CircleShape)
                        .background(Color(0xFFFAD7BE))
                        .fillMaxSize()
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.Black,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
            Text(
                text = "Artikel",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.padding(end = 48.dp, start = 8.dp)
            )
        }
    }
}