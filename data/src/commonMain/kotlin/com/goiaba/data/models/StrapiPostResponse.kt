package com.goiaba.data.models

data class TestPost(
    var title: String,
)

data class StrapiPostResponse(
    val data: List<Data>,
    val meta: Meta
) {
    data class Data(
        val attributes: Attributes,
        val id: Int
    ) {
        data class Attributes(
            val createdAt: String,
            val title: String,
            val updatedAt: String
        )
    }

    data class Meta(
        val pagination: Pagination
    ) {
        data class Pagination(
            val page: Int,
            val pageCount: Int,
            val pageSize: Int,
            val total: Int
        )
    }
}

data class ApiCharacter(
    val name: String,
    val gender: String,
    val culture: String,
    val born: String,
    val died: String,
    val aliases: List<Int>,
    val tvSeries: List<String>,
    val playedBy: List<String>,
)

data class ApiCharacterResponse(
    val name: String,
    val gender: String,
    val culture: String,
    val born: String,
    val died: String,
    val titles: List<String>,
    val aliases: List<String>,
    val playedBy: List<String>,
    val tvSeries: List<String>
)