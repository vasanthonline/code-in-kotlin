package com.example.spark

import java.io.Serializable;

data class Word(var word: String, var count: Int = 0) : Serializable
