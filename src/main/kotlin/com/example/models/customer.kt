package com.example.models

import kotlinx.serialization.Serializable

@Serializable
data class Customer(val id: String,
                    val firstName: String,
                    val lastName: String,
                    val phoneNumber: String,
                    val email: String,
                    val country: String
                    )

val customerStorage = mutableListOf<Customer>(
        Customer(
                    "100",
                    "Jane",
                    "Smith",
                    "555678",
                    "jane.smith@company.com",
                    "Estonia"
        ),
        Customer(
                    "200",
                    "John",
                    "Smith",
                    "555878",
                    "john.smith@company.com",
                    "Germany"
        ),
        Customer(
                    "300",
                    "Mary",
                    "Smith",
                    "553878",
                    "mary.smith@company.com",
                    "Latvia"
    )
)
