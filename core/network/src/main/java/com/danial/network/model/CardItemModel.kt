package com.danial.network.model

import java.util.UUID

data class CardItemModel(val id: String = UUID.randomUUID().toString(), val label: String ? = "", val number: String, val sheba: String? = null)
