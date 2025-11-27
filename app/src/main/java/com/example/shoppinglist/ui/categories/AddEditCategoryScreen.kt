package com.example.shoppinglist.ui.categories

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.shoppinglist.data.entities.Category
import com.example.shoppinglist.data.entities.Product
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditProductScreen(
    productToEdit: Product? = null,
    categories: List<Category>,
    onBackClick: () -> Unit,
    onSaveClick: (Product) -> Unit
) {
    var productName by remember { mutableStateOf(productToEdit?.ProductName ?: "") }
    var quantity by remember { mutableStateOf(productToEdit?.Quantity?.toString() ?: "1") }
    var unit by remember { mutableStateOf(productToEdit?.Unit ?: "") }
    var selectedCategory by remember { 
        mutableStateOf(categories.find { it.category_id == productToEdit?.categoryId } ?: categories.firstOrNull()) 
    }
    var notes by remember { mutableStateOf(productToEdit?.Notes ?: "") }
    
    var expanded by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (productToEdit == null) "New Product" else "Edit Product") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = productName,
                onValueChange = { productName = it },
                label = { Text("Product Name") },
                modifier = Modifier.fillMaxWidth()
            )

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                OutlinedTextField(
                    value = quantity,
                    onValueChange = { if (it.all { char -> char.isDigit() }) quantity = it },
                    label = { Text("Quantity") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1f)
                )

                OutlinedTextField(
                    value = unit,
                    onValueChange = { unit = it },
                    label = { Text("Unit (e.g., kg, pcs)") },
                    modifier = Modifier.weight(1f)
                )
            }

            // Category Dropdown
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded },
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = selectedCategory?.category_name ?: "Select Category",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Category") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
                    modifier = Modifier.menuAnchor().fillMaxWidth()
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    categories.forEach { category ->
                        DropdownMenuItem(
                            text = { Text(category.category_name) },
                            onClick = {
                                selectedCategory = category
                                expanded = false
                            }
                        )
                    }
                }
            }

            OutlinedTextField(
                value = notes,
                onValueChange = { notes = it },
                label = { Text("Notes") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3
            )

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    val newProduct = Product(
                        Id = productToEdit?.Id ?: 0,
                        ProductName = productName,
                        DateAdded = productToEdit?.DateAdded ?: Date(),
                        Quantity = quantity.toIntOrNull() ?: 1,
                        categoryId = selectedCategory?.category_id,
                        Unit = unit,
                        Notes = notes
                    )
                    onSaveClick(newProduct)
                },
                enabled = productName.isNotBlank() && selectedCategory != null,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save Product")
            }
        }
    }
}


