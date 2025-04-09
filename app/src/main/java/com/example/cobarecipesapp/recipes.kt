package com.example.cobarecipesapp

import com.example.cobarecipesapp.domain.Category
import com.example.cobarecipesapp.domain.Recipe
import com.example.cobarecipesapp.domain.Ingredient

object STUB {

    private val categories = listOf(
        Category(
            id = 0,
            title = "Бургеры",
            description = "Рецепты всех популярных видов бургеров",
            imageUrl = "burger.png",
        ),
        Category(
            id = 1,
            title = "Десерты",
            description = "Самые вкусные рецепты десертов специально для вас",
            imageUrl = "dessert.png",
        ),
        Category(
            id = 2,
            title = "Пицца",
            description = "Пицца на любой вкус и цвет. Лучшая подборка для тебя",
            imageUrl = "pizza.png",
        ),
        Category(
            id = 3,
            title = "Рыба",
            description = "Печеная, жареная, сушеная, любая рыба на твой вкус",
            imageUrl = "fish.png",
        ),
        Category(
            id = 4,
            title = "Супы",
            description = "От классики до экзотики: мир в одной тарелке",
            imageUrl = "soup.png",
        ),
        Category(
            id = 5,
            title = "Салаты",
            description = "Хрустящий калейдоскоп под соусом вдохновения",
            imageUrl = "salad.png",
        ),
    )

    private val burgerRecipes = listOf(
        Recipe(
            id = 0,
            title = "Классический гамбургер",
            ingredients = listOf(
                Ingredient("1", "шт.", "Булочка для гамбургера"),
                Ingredient("150", "г", "Фарш говяжий"),
                Ingredient("0.5", "шт.", "Лук репчатый"),
                Ingredient("1", "лист", "Салат айсберг"),
                Ingredient("1", "ломтик", "Помидор"),
                Ingredient("1", "ломтик", "Сыр чеддер"),
                Ingredient("1", "ст. ложка", "Майонез"),
                Ingredient("1", "ст. ложка", "Кетчуп"),
                Ingredient("0.5", "ч. ложки", "Горчица"),
                Ingredient("1", "г", "Соленые огурцы"),
                Ingredient("1", "г", "Соль"),
                Ingredient("0.5", "г", "Перец черный молотый")
            ),
            method = listOf(
                "Разогрейте сковороду и обжаривайте фарш до готовности.",
                "Лук нарежьте кольцами и добавьте к фаршу. Обжаривайте вместе до золотистой корки.",
                "Разрежьте булочку на две части и поджарьте их до золотистости.",
                "На нижнюю часть булочки положите лист салата, ломтик помидора и сыр.",
                "Добавьте фарш с луком и огурцами.",
                "Смажьте верхнюю часть булочки майонезом, кетчупом и горчицей.",
                "Положите верхнюю часть булочки на остальные ингредиенты и подавайте."
            ),
            imageUrl = "burger-hamburger.png"
        ),
        Recipe(
            id = 1,
            title = "Чизбургер",
            ingredients = listOf(
                Ingredient("1", "шт.", "Булочка для гамбургера"),
                Ingredient("150", "г", "Фарш говяжий"),
                Ingredient("0.5", "шт.", "Лук репчатый"),
                Ingredient("1", "лист", "Салат айсберг"),
                Ingredient("1", "ломтик", "Помидор"),
                Ingredient("1", "ломтик", "Сыр чеддер"),
                Ingredient("1", "ст. ложка", "Майонез"),
                Ingredient("1", "ст. ложка", "Кетчуп"),
                Ingredient("0.5", "ч. ложки", "Горчица"),
                Ingredient("1", "г", "Соленые огурцы"),
                Ingredient("1", "г", "Соль"),
                Ingredient("0.5", "г", "Перец черный молотый")
            ),
            method = listOf(
                "Разогрейте сковороду и обжаривайте фарш до готовности.",
                "Лук нарежьте кольцами и добавьте к фаршу. Обжаривайте вместе до золотистой корки.",
                "Разрежьте булочку на две части и поджарьте их до золотистости.",
                "На нижнюю часть булочки положите лист салата, ломтик помидора и сыр.",
                "Добавьте фарш с луком и огурцами.",
                "Смажьте верхнюю часть булочки майонезом, кетчупом и горчицей.",
                "Положите верхнюю часть булочки на остальные ингредиенты и подавайте."
            ),
            imageUrl = "burger-cheeseburger.png"
        ),
        Recipe(
            id = 2,
            title = "Бургер с грибами и сыром",
            ingredients = listOf(
                Ingredient("1", "шт.", "Булочка для гамбургера"),
                Ingredient("150", "г", "Фарш говяжий"),
                Ingredient("0.5", "шт.", "Лук репчатый"),
                Ingredient("1", "лист", "Салат айсберг"),
                Ingredient("1", "ломтик", "Помидор"),
                Ingredient("2", "шт.", "Грибы шампиньоны"),
                Ingredient("1", "ст. ложка", "Майонез"),
                Ingredient("1", "ст. ложка", "Кетчуп"),
                Ingredient("0.5", "ч. ложки", "Горчица"),
                Ingredient("1", "г", "Соль"),
                Ingredient("0.5", "г", "Перец черный молотый")
            ),
            method = listOf(
                "Разогрейте сковороду и обжаривайте фарш до готовности.",
                "Лук нарежьте кольцами и добавьте к фаршу. Обжаривайте вместе до золотистой корки.",
                "Грибы порежьте и обжарьте до золотистой корки.",
                "Разрежьте булочку на две части и поджарьте их до золотистости.",
                "На нижнюю часть булочки положите лист салата, ломтик помидора и фарш.",
                "Добавьте обжаренные грибы.",
                "Смажьте верхнюю часть булочки майонезом, кетчупом и горчицей.",
                "Положите верхнюю часть булочки на остальные ингредиенты и подавайте."
            ),
            imageUrl = "burger-mushrooms.png"
        ),
        Recipe(
            id = 3,
            title = "Бургер с курицей и авокадо",
            ingredients = listOf(
                Ingredient("1", "шт.", "Булочка для гамбургера"),
                Ingredient("150", "г", "Куринное филе"),
                Ingredient("0.5", "шт.", "Лук репчатый"),
                Ingredient("1", "лист", "Салат айсберг"),
                Ingredient("1", "ломтик", "Помидор"),
                Ingredient("0.5", "шт.", "Авокадо"),
                Ingredient("1", "ст. ложка", "Майонез"),
                Ingredient("1", "ст. ложка", "Кетчуп"),
                Ingredient("0.5", "ч. ложки", "Горчица"),
                Ingredient("1", "г", "Соль"),
                Ingredient("0.5", "г", "Перец черный молотый")
            ),
            method = listOf(
                "Куринное филе нарежьте на порционные куски и обжаривайте до готовности.",
                "Лук нарежьте кольцами и добавьте к фаршу. Обжаривайте вместе до золотистой корки.",
                "Авокадо порежьте ломтиками.",
                "Разрежьте булочку на две части и поджарьте их до золотистости.",
                "На нижнюю часть булочки положите лист салата, ломтик помидора и курицу.",
                "Добавьте ломтики авокадо.",
                "Смажьте верхнюю часть булочки майонезом, кетчупом и горчицей.",
                "Положите верхнюю часть булочки на остальные ингредиенты и подавайте."
            ),
            imageUrl = "burger-avocado.png"
        ),
        Recipe(
            id = 4,
            title = "Бургер с рыбой",
            ingredients = listOf(
                Ingredient("1", "шт.", "Булочка для гамбургера"),
                Ingredient("150", "г", "Филе рыбы (треска, лосось и т. д.)"),
                Ingredient("0.5", "шт.", "Лук репчатый"),
                Ingredient("1", "лист", "Салат айсберг"),
                Ingredient("1", "ломтик", "Помидор"),
                Ingredient("1", "ст. ложка", "Майонез"),
                Ingredient("1", "ст. ложка", "Кетчуп"),
                Ingredient("0.5", "ч. ложки", "Горчица"),
                Ingredient("1", "г", "Соль"),
                Ingredient("0.5", "г", "Перец черный молотый")
            ),
            method = listOf(
                "Рыбное филе приготовьте по вашему рецепту (можно пожарить, запечь или приготовить на пару).",
                "Лук нарежьте кольцами и добавьте к рыбе. Обжаривайте вместе до золотистой корки.",
                "Разрежьте булочку на две части и поджарьте их до золотистости.",
                "На нижнюю часть булочки положите лист салата, ломтик помидора и рыбу с луком.",
                "Смажьте верхнюю часть булочки майонезом, кетчупом и горчицей.",
                "Положите верхнюю часть булочки на остальные ингредиенты и подавайте."
            ),
            imageUrl = "burger-fish.png"
        ),
        Recipe(
            id = 5,
            title = "Бургер с беконом",
            ingredients = listOf(
                Ingredient("1", "шт.", "Булочка для гамбургера"),
                Ingredient("150", "г", "Фарш говяжий"),
                Ingredient("0.5", "шт.", "Лук репчатый"),
                Ingredient("1", "лист", "Салат айсберг"),
                Ingredient("1", "ломтик", "Помидор"),
                Ingredient("2", "ломтика", "Бекон"),
                Ingredient("1", "ст. ложка", "Майонез"),
                Ingredient("1", "ст. ложка", "Кетчуп"),
                Ingredient("0.5", "ч. ложки", "Горчица"),
                Ingredient("1", "г", "Соль"),
                Ingredient("0.5", "г", "Перец черный молотый")
            ),
            method = listOf(
                "Разогрейте сковороду и обжаривайте фарш до готовности.",
                "Лук нарежьте кольцами и добавьте к фаршу. Обжаривайте вместе до золотистой корки.",
                "Бекон обжарьте до хрустящей корки.",
                "Разрежьте булочку на две части и поджарьте их до золотистости.",
                "На нижнюю часть булочки положите лист салата, ломтик помидора и фарш.",
                "Добавьте ломтики бекона.",
                "Смажьте верхнюю часть булочки майонезом, кетчупом и горчицей.",
                "Положите верхнюю часть булочки на остальные ингредиенты и подавайте."
            ),
            imageUrl = "burger-bacon.png"
        ),
        Recipe(
            id = 6,
            title = "Веганский бургер",
            ingredients = listOf(
                Ingredient("1", "шт.", "Веганская булочка для гамбургера"),
                Ingredient("150", "г", "Фарш из нута"),
                Ingredient("0.25", "шт.", "Красный лук"),
                Ingredient("1", "ст. ложка", "Томатный соус"),
                Ingredient("1", "ст. ложка", "Горчица"),
                Ingredient("0.5", "г", "Капуста краснокочанная"),
                Ingredient("0.5", "г", "Морковь"),
                Ingredient("0.5", "г", "Соль"),
                Ingredient("0.25", "г", "Черный перец"),
                Ingredient("0.25", "г", "Паприка"),
                Ingredient("0.25", "г", "Чеснок в порошке")
            ),
            method = listOf(
                "Фарш из нута приготовьте по вашему рецепту (можно использовать консервированный).",
                "Лук и капусту нарежьте кольцами, морковь на тонкие полоски.",
                "Веганскую булочку поджарьте.",
                "На нижнюю часть булочки положите фарш из нута.",
                "Добавьте лук, капусту и морковь.",
                "Смажьте верхнюю часть булочки томатным соусом и горчицей.",
                "Положите верхнюю часть булочки на остальные ингредиенты и подавайте."
            ),
            imageUrl = "burger-vegan.png"
        ),
        Recipe(
            id = 7,
            title = "Острый гамбургер",
            ingredients = listOf(
                Ingredient("1", "шт.", "Булочка для гамбургера"),
                Ingredient("150", "г", "Фарш говяжий"),
                Ingredient("0.5", "шт.", "Лук репчатый"),
                Ingredient("1", "лист", "Салат айсберг"),
                Ingredient("1", "ломтик", "Помидор"),
                Ingredient("1", "ломтик", "Сыр чеддер"),
                Ingredient("1", "ст. ложка", "Майонез"),
                Ingredient("1", "ст. ложка", "Кетчуп"),
                Ingredient("1", "ч. ложка", "Соус табаско"),
                Ingredient("1", "г", "Соль"),
                Ingredient("0.5", "г", "Перец черный молотый")
            ),
            method = listOf(
                "Разогрейте сковороду и обжаривайте фарш до готовности.",
                "Лук нарежьте кольцами и добавьте к фаршу. Обжаривайте вместе до золотистой корки.",
                "Разрежьте булочку на две части и поджарьте их до золотистости.",
                "На нижнюю часть булочки положите лист салата, ломтик помидора и сыр.",
                "Добавьте фарш с луком.",
                "Смажьте верхнюю часть булочки майонезом, кетчупом и соусом табаско.",
                "Положите верхнюю часть булочки на остальные ингредиенты и подавайте."
            ),
            imageUrl = "burger-chili.png"
        )
    )

    fun getCategories() = categories

    fun getRecipesByCategoryId(categoryId: Int): List<Recipe> =
        if (categoryId == 0) burgerRecipes else listOf()

}