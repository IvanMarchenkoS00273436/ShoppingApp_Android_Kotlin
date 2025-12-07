package com.example.shoppinglist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import com.example.shoppinglist.ui.products.ProductDetailsScreen
import com.example.shoppinglist.ui.products.ProductListScreen
import com.example.shoppinglist.ui.products.ProductViewModel
import com.example.shoppinglist.ui.products.ProductViewModelFactory
import com.example.shoppinglist.ui.settings.SettingsScreen
import com.example.shoppinglist.ui.theme.ShoppingListTheme

// Main Activity hosting the Compose UI
class MainActivity : ComponentActivity() {
    // Entry point of the application
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

        // 3. Product ViewModel requires both ProductDao and CategoryDao
        val productViewModel = ViewModelProvider(
            this,
            ProductViewModelFactory(db.productDao(), db.categoryDao())
        )[ProductViewModel::class.java]

        setContent {
            // Theme State
            val systemDark = isSystemInDarkTheme()
            var isDarkTheme by remember { mutableStateOf(systemDark) }

            ShoppingListTheme(darkTheme = isDarkTheme) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen(
                        categoryViewModel, 
                        productViewModel,
                        isDarkTheme,
                        onThemeChange = { isDarkTheme = it }
                    )
                }
            }
        }
    }
}

// Main Screen with Bottom Navigation
@Composable
fun MainScreen(
    categoryViewModel: CategoryViewModel,
    productViewModel: ProductViewModel,
    isDarkTheme: Boolean,
    onThemeChange: (Boolean) -> Unit
) {
    var currentTab by remember { mutableStateOf("products") }

    // Scaffold with Bottom Navigation
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
                "settings" -> SettingsScreen(isDarkTheme, onThemeChange)
            }
        }
    }
}

// Navigation Host for Products
@Composable
fun ProductsNavHost(viewModel: ProductViewModel) {
    var screen by remember { mutableStateOf("list") }
    var selectedProduct by remember { mutableStateOf<Product?>(null) } // Used for Edit and Details

    val products by viewModel.allProducts.collectAsState(initial = emptyList())
    val categories by viewModel.allCategories.collectAsState(initial = emptyList())

    when (screen) {
        "list" -> {
            ProductListScreen(
                products = products,
                categories = categories,
                onAddProductClick = {
                    selectedProduct = null
                    screen = "add_edit"
                },
                onProductClick = { product ->
                    selectedProduct = product
                    screen = "details"
                },
                onEditProduct = { product ->
                    selectedProduct = product
                    screen = "add_edit"
                },
                onDeleteProduct = { product ->
                    viewModel.deleteProduct(product)
                }
            )
        }
        "details" -> {
            if (selectedProduct != null) {
                // Ensure we have the latest version of the product from the list
                val currentProduct = products.find { it.Id == selectedProduct!!.Id } ?: selectedProduct!!
                val category = categories.find { it.category_id == currentProduct.categoryId }

                ProductDetailsScreen(
                    product = currentProduct,
                    category = category,
                    onBackClick = { screen = "list" },
                    onEditClick = { screen = "add_edit" },
                    onDeleteClick = {
                        viewModel.deleteProduct(currentProduct)
                        screen = "list"
                    }
                )
            } else {
                // Fallback if state is lost
                screen = "list"
            }
        }
        "add_edit" -> {
            AddEditProductScreen(
                productToEdit = selectedProduct,
                categories = categories,
                onBackClick = { 
                    // If we were editing from details, go back to details, else list
                    screen = if (selectedProduct != null && selectedProduct!!.Id != 0L) "details" else "list" 
                },
                onSaveClick = { product ->
                    if (product.Id == 0L) {
                        viewModel.addProduct(product)
                        screen = "list"
                    } else {
                        viewModel.updateProduct(product)
                        screen = "list"
                    }
                }
            )
        }
    }
}

// Navigation Host for Categories
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
                    screen = "list"
                }
            )
        }
    }
}