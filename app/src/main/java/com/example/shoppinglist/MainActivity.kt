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
import com.example.shoppinglist.data.entities.Product
import com.example.shoppinglist.ui.categories.AddEditCategoryScreen
import com.example.shoppinglist.ui.categories.CategoryListScreen
import com.example.shoppinglist.ui.categories.CategoryViewModel
import com.example.shoppinglist.ui.categories.CategoryViewModelFactory
import com.example.shoppinglist.ui.products.AddEditProductScreen
import com.example.shoppinglist.ui.products.ProductListScreen
import com.example.shoppinglist.ui.products.ProductViewModel
import com.example.shoppinglist.ui.products.ProductViewModelFactory
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

        // 2. Create ViewModels
        val categoryViewModel = ViewModelProvider(
            this,
            CategoryViewModelFactory(db.categoryDao())
        )[CategoryViewModel::class.java]

        val productViewModel = ViewModelProvider(
            this,
            ProductViewModelFactory(db.productDao(), db.categoryDao())
        )[ProductViewModel::class.java]

        setContent {
            ShoppingListTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen(categoryViewModel, productViewModel)
                }
            }
        }
    }
}

@Composable
fun MainScreen(
    categoryViewModel: CategoryViewModel,
    productViewModel: ProductViewModel
) {
    var currentTab by remember { mutableStateOf("products") }

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
                "products" -> ProductsNavHost(productViewModel)
                "categories" -> CategoriesNavHost(categoryViewModel)
                "settings" -> SettingsPlaceholder()
            }
        }
    }
}

@Composable
fun ProductsNavHost(viewModel: ProductViewModel) {
    var screen by remember { mutableStateOf("list") }
    var productToEdit by remember { mutableStateOf<Product?>(null) }

    val products by viewModel.allProducts.collectAsState(initial = emptyList())
    val categories by viewModel.allCategories.collectAsState(initial = emptyList())

    when (screen) {
        "list" -> {
            ProductListScreen(
                products = products,
                categories = categories,
                onAddProductClick = {
                    productToEdit = null
                    screen = "add_edit"
                },
                onEditProduct = { product ->
                    productToEdit = product
                    screen = "add_edit"
                },
                onDeleteProduct = { product ->
                    viewModel.deleteProduct(product)
                }
            )
        }
        "add_edit" -> {
            AddEditProductScreen(
                productToEdit = productToEdit,
                categories = categories,
                onBackClick = { screen = "list" },
                onSaveClick = { product ->
                    if (product.Id == 0L) {
                        viewModel.addProduct(product)
                    } else {
                        viewModel.updateProduct(product)
                    }
                    screen = "list"
                }
            )
        }
    }
}

@Composable
fun CategoriesNavHost(viewModel: CategoryViewModel) {
    var screen by remember { mutableStateOf("list") }
    var categoryToEdit by remember { mutableStateOf<Category?>(null) }
    
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
                        viewModel.addCategory(name)
                    } else {
                        val updatedCategory = categoryToEdit!!.copy(category_name = name)
                        viewModel.updateCategory(updatedCategory)
                    }
                    screen = "list"          }
        )
    }
}
}

@Composable
fun SettingsPlaceholder() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Settings Screen - Coming Soon!")
    }
}