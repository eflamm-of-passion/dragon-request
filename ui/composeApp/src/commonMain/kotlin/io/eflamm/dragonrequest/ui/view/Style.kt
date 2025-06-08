package io.eflamm.dragonrequest.ui.view

import androidx.compose.material.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun sendButtonColors() = ButtonDefaults.buttonColors(backgroundColor = Color.Blue, contentColor = Color.White)

@Composable
fun saveButtonColors() = ButtonDefaults.buttonColors(backgroundColor = Color.Green, contentColor = Color.White)

@Composable
fun deleteButtonColors() = ButtonDefaults.buttonColors(backgroundColor = Color.Red, contentColor = Color.White)

@Composable
fun addButtonColors() = ButtonDefaults.buttonColors(backgroundColor = Color.Blue, contentColor = Color.White)
