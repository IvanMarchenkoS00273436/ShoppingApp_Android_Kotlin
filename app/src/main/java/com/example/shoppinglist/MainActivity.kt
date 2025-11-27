package com.example.shoppinglist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.example.shoppinglist.data.dbcontext.AppDatabase
import com.example.shoppinglist.data.entities.Category
import com.example.shoppinglist.ui.categories.AddEditCategoryScreen
import com.example.shoppinglist.ui.categories.CategoryListScreen
import com.example.shoppinglist.ui.categories.CategoryViewModel
import com.example.shoppinglist.ui.categories.CategoryViewModelFactory
import com.example.shoppinglist.ui.theme.ShoppingListTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // 1. Initialize Room Database
        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "shopping-list-db"
        ).build()

        // 2. Create ViewModel using Factory
        val categoryViewModel = ViewModelProvider(
            this,
            CategoryViewModelFactory(db.categoryDao())
        )[CategoryViewModel::class.java]

        setContent {
            ShoppingListTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen(categoryViewModel)
                }
            }
        }
    }
}

@Composable
fun MainScreen(categoryViewModel: CategoryViewModel) {
    var currentTab by remember { mutableStateOf("categories") }

    Scaffold(
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = currentTab == "products",
                    onClick = { currentTab = "products" },
                    icon = { Icon(Icons.Default.ShoppingCart, contentDescription = "Products") },
                    label = { Text("Products") }
                )
                NavigationBarItem(
                    selected = currentTab == "categories",
                    onClick = { currentTab = "categories" },
                    icon = { Icon(Icons.Default.List, contentDescription = "Categories") },
                    label = { Text("Categories") }
                )
                NavigationBarItem(
                    selected = currentTab == "settings",
                    onClick = { currentTab = "settings" },
                    icon = { Icon(Icons.Default.Settings, contentDescription = "Settings") },
                    label = { Text("Settings") }
                )
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            when (currentTab) {
                "products" -> ProductsPlaceholder()
                "categories" -> CategoriesNavHost(categoryViewModel)
                "settings" -> SettingsPlaceholder()
            }
        }
    }
}

@Composable
fun CategoriesNavHost(viewModel: CategoryViewModel) {
    // Local navigation state for the Categories tab
    var screen by remember { mutableStateOf("list") }
    var categoryToEdit by remember { mutableStateOf<Category?>(null) }
    
    // 3. Collect data from Room via ViewModel
    val categories by viewModel.allCategories.collectAsState(initial = emptyList())

    when (screen) {
        "list" -> {
            CategoryListScreen(
                categories = categories,
                onAddCategoryClick = {
                    categoryToEdit = null
                    screen = "add_edit"
                },
                onEditCategory = { category ->
                    categoryToEdit = category
                    screen = "add_edit"
                },
                onDeleteCategory = { category ->
                    viewModel.deleteCategory(category)
                }
            )
        }
        "add_edit" -> {
            AddEditCategoryScreen(
                categoryToEdit = categoryToEdit,
                onBackClick = { screen = "list" },
                onSaveClick = { name ->
                    if (categoryToEdit == null) {
                        // Create New
                        viewModel.addCategory(name)
                    } else {
                        // Update Existing
                        val updatedCategory = categoryToEdit!!.copy(category_name = name)
                        viewModel.updateCategory(updatedCategory)
                    }
                    screen = "list"
                }
            )
        }
    }
}

@Composable
fun ProductsPlaceholder() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Products Screen - Coming Soon!")
    }
}

@Composable
fun SettingsPlaceholder() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Settings Screen - Coming Soon!")
    }
}