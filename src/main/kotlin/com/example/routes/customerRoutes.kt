package com.example.routes

import com.example.models.Customer
import com.example.models.customerStorage
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.customerRouting() {

    route("/customer") {
        get {
            if (customerStorage.isNotEmpty()) {
                val etag = customerStorage.hashCode().toString()
                call.response.headers.append(HttpHeaders.ETag, etag)
                if (call.request.checkETag(etag)) {
                    call.respond(HttpStatusCode.NotModified)
                } else {
                    call.respond(customerStorage)
                }           
            } else {
                call.respondText("No customers found", status = HttpStatusCode.OK)
            }
        }

        get("{id?}") {
            val id = call.parameters["id"] ?: return@get call.respondText(
                "Missing id",
                status = HttpStatusCode.BadRequest
            )
            val customer =
                customerStorage.find { it.id == id } ?: return@get call.respondText(
                    "No customer with id $id",
                    status = HttpStatusCode.NotFound
                )
            val etag = customer.hashCode().toString()
            call.response.headers.append(HttpHeaders.ETag, etag)
            if (call.request.checkETag(etag)) {
                call.respond(HttpStatusCode.NotModified)
            } else {
                call.respond(customer)
            }
        }

        post {
            val customer = call.receive<Customer>()
            customerStorage.add(customer)
            call.respondText("Customer stored correctly", status = HttpStatusCode.Created)
        }

        delete("{id?}") {
            val id = call.parameters["id"] ?: return@delete call.respond(HttpStatusCode.BadRequest)
            if (customerStorage.removeIf { it.id == id }) {
                call.respondText("Customer removed correctly", status = HttpStatusCode.Accepted)
            } else {
                call.respondText("Not Found", status = HttpStatusCode.NotFound)
            }
        }
    }
}

fun ApplicationRequest.checkETag(etag: String): Boolean {
    val requestETag = header(HttpHeaders.IfNoneMatch)?.removeSurrounding("\"")
    return etag == requestETag
}
