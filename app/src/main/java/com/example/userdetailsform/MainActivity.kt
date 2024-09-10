package com.example.userdetailsform

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import com.example.userdetailsform.database.UserDatabase
import com.example.userdetailsform.model.UserEntity
import com.example.userdetailsform.repository.UserRepo
import com.example.userdetailsform.ui.theme.UserDetailsFormTheme
import com.example.userdetailsform.viewModel.UserViewModel
import java.util.Calendar

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //initializing database, dao and repository instance
        val db = UserDatabase.getDatabase(this)
        val userDao = db.userDao()
        val repository = UserRepo(userDao)
        val viewModel = ViewModelProvider(this)[UserViewModel::class.java]

        //setting up repository in viewmodel
        viewModel.setRepository(repository)

        enableEdgeToEdge()
        setContent {
            UserDetailsFormTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    UserDetailForm(
                        modifier = Modifier.padding(innerPadding), this, viewModel
                    )
                }
            }
            //observing livedata depending on response
            viewModel.successLiveData.observeForever { success ->
                Toast.makeText(this, getString(success), Toast.LENGTH_SHORT).show()
            }
            viewModel.errorLiveData.observeForever { error ->
                Toast.makeText(this, getString(error), Toast.LENGTH_SHORT).show()
            }
        }
    }
}

@Composable
fun UserDetailForm(modifier: Modifier = Modifier, context: MainActivity, viewModel: UserViewModel) {
    Column {
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = stringResource(id = R.string.fill_your_details), color = colorScheme.primary,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 28.sp,
            modifier = modifier.align(Alignment.CenterHorizontally)
        )
        Form(context, viewModel)
    }
}

@Composable
fun Form(context: MainActivity, viewModel: UserViewModel) {
    /*creating mutable fields for storing values of name, age, address, dob,
    also for their error messages */
    val name = remember {
        mutableStateOf("")
    }
    val isNameError = remember { mutableStateOf(false) }
    val nameErrorMessage = remember { mutableStateOf("") }
    val age = remember {
        mutableStateOf("")
    }
    val isAgeError = remember { mutableStateOf(false) }
    val ageErrorMessage = remember { mutableStateOf("") }
    val address = remember {
        mutableStateOf("")
    }
    val isAddressError = remember { mutableStateOf(false) }
    val addressErrorMessage = remember { mutableStateOf("") }
    val dateFormat = stringResource(id = R.string.date_format)
    val dob = remember {
        mutableStateOf(dateFormat)
    }
    val isDateNotSelected = remember {
        mutableStateOf(false)
    }
    val dobErrorMessage = remember { mutableStateOf("") }

    //form with submission button to store values in database
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Column {
            OutlinedTextField(
                value = name.value,
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                onValueChange = {
                    name.value = it
                    isNameError.value = name.value.trim().isEmpty()
//                    nameErrorMessage.value = if (isNameError.value) "Name cannot be empty" else ""
                },
                label = {
                    Text(text = stringResource(id = R.string.name))
                })
            if (isNameError.value) {
                Text(
                    text = stringResource(id = R.string.error_name),
                    color = colorScheme.error,
                    style = typography.bodySmall,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        Column {
            OutlinedTextField(
                value = age.value,
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number, // Set keyboard type to Number
//                    imeAction = ImeAction.Done // Optional: Specify what action button to show on keyboard
                ),
                modifier = Modifier.fillMaxWidth(),
                onValueChange = {
                    if (it.all { char -> char.isDigit() } || it.isEmpty()) {
                        age.value = it
                    }
                    isAgeError.value = age.value.isEmpty()
                },
                label = {
                    Text(text = stringResource(id = R.string.age))
                })
            if (isAgeError.value) {
                Text(
                    text = stringResource(id = R.string.error_age),
                    color = colorScheme.error,
                    style = typography.bodySmall,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        Column {
            Text(text = stringResource(id = R.string.dob))
            Spacer(modifier = Modifier.height(4.dp))
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.clickable {
                val c = Calendar.getInstance()
                val year = c.get(Calendar.YEAR)
                val month = c.get(Calendar.MONTH)
                val day = c.get(Calendar.DAY_OF_MONTH)
                val dpd = DatePickerDialog(
                    context,
                    { _, years, monthOfYear, dayOfMonth ->
                        dob.value = "$years-${monthOfYear + 1}-$dayOfMonth"
                        isDateNotSelected.value = false
                    },
                    year,
                    month,
                    day
                )

                dpd.show()
            }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_calendar),
                    contentDescription = ""
                )
                Spacer(modifier = Modifier.width(8.dp))
                if (isDateNotSelected.value) {
                    Text(text = stringResource(id = R.string.date_format))
                } else {
                    Text(text = dob.value)
                }
            }
            if (isDateNotSelected.value) {
                Text(
                    text = stringResource(id = R.string.error_dob),
                    color = colorScheme.error,
                    style = typography.bodySmall,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        Column {
            OutlinedTextField(
                value = address.value,
                modifier = Modifier.fillMaxWidth(),
                maxLines = 2,
                onValueChange = {
                    address.value = it
                    isAddressError.value = address.value.trim().isEmpty()
                },
                label = {
                    Text(text = stringResource(id = R.string.address))
                })
            if (isAddressError.value) {
                Text(
                    text = stringResource(id = R.string.error_address),
                    color = colorScheme.error,
                    style = typography.bodySmall,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            val errorName = stringResource(id = R.string.error_name)
            val errorAge = stringResource(id = R.string.error_age)
            val errorDob = stringResource(id = R.string.error_dob)
            val errorAddress = stringResource(id = R.string.error_address)
            Button(onClick = {
                isNameError.value = name.value.trim().isEmpty()
                nameErrorMessage.value = if (isNameError.value) errorName else ""
                isAgeError.value = age.value.trim().isEmpty()
                ageErrorMessage.value = if (isAgeError.value) errorAge else ""
                isDateNotSelected.value = dob.value == dateFormat
                dobErrorMessage.value = if (isDateNotSelected.value) errorDob else ""
                isAddressError.value = address.value.trim().isEmpty()
                addressErrorMessage.value = if (isAddressError.value) errorAddress else ""
                if(!isNameError.value && !isAgeError.value && !isDateNotSelected.value && !isAddressError.value){
                    //calling function in viewmodel for inserting values in database
                    viewModel.insertBooking(
                        UserEntity(
                            name = name.value.trim(),
                            age = age.value,
                            dob = dob.value,
                            address = address.value.trim()
                        )
                    )
                }
            }) {
                Text(
                    text = stringResource(id = R.string.submit),
                    color = colorScheme.background,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.background(colorScheme.primary),
                    fontSize = 20.sp
                )
            }
        }
    }
}