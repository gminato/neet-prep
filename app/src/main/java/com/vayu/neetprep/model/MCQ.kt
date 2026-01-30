package com.vayu.neetprep.model

/**
 * Simple MCQ model used by MCQScreen.
 */
data class MCQ(
    val id: Int,
    val questionText: String,
    val questionImage: String?,
    val options: List<String>,
    val correctOptionIndex: Int
)
