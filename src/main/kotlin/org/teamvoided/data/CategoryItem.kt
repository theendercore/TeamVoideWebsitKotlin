package org.teamvoided.data

import org.teamvoided.database.Category

data class CategoryItem(
    val id: Short,
    val name: String,
    val type: CategoryType,
    val packs: List<PackItem>
) {
    constructor(category: Category, packs: List<PackItem>) : this(
        category.id,
        category.name,
        CategoryType.valueOf(category.type),
        packs
    )
}
